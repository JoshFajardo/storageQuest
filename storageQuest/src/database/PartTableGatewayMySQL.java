package database;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;



import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import models.Part;
import models.Warehouse;

public class PartTableGatewayMySQL implements PartTableGateway {

	private static final boolean DEBUG = true;
	private Connection conn = null;
	//this creates the database connection
	
	public PartTableGatewayMySQL() throws GatewayException {
		
		DataSource ds = null;
		try{
			ds = getDataSource();
		}catch(RuntimeException | IOException e1 ){
			throw new GatewayException(e1.getMessage());
		}
		if(ds == null){
			throw new GatewayException("Datasource is null!");
		}
		try{
			conn=ds.getConnection();
			
		}catch(SQLException e){
			throw new GatewayException("SQL Error: "+ e.getMessage());
		}
	}
	
	
	public void close(){
		if(DEBUG)
			System.out.println("Closing the database connection");
		try{
			conn.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	//creates datasource with information in db.properties
	private DataSource getDataSource() throws RuntimeException, IOException {
		
		Properties props = new Properties();
		FileInputStream fis = null;
        fis = new FileInputStream("db.properties");
        props.load(fis);
        fis.close();
        
        //using the information from db.properties
        MysqlDataSource mysqlDS = new MysqlDataSource();
        mysqlDS.setURL(props.getProperty("MYSQL_DB_URL"));
        mysqlDS.setUser(props.getProperty("MYSQL_DB_USERNAME"));
        mysqlDS.setPassword(props.getProperty("MYSQL_DB_PASSWORD"));
        return mysqlDS;
	}

	@Override
	public Part fetchPart(long id) throws GatewayException {
		Part p = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			//fetch part
			conn.setAutoCommit(false);
			st = conn.prepareStatement("select * from part where id = ? ");
			st.setLong(1, id);
			rs = st.executeQuery();
			//should only be 1
			rs.next();
			p = new Part(rs.getLong("id"), rs.getString("part_name"), rs.getString("part_number")
					, rs.getString("vendor"), rs.getString("vendor_part_num")
					, rs.getString("unit_quanitity"));
			
		} catch (SQLException e) {
			throw new GatewayException(e.getMessage());
		} finally {
			//cleaning up
			try {
				if(rs != null)
					rs.close();
				if(st != null)
					st.close();
			} catch (SQLException e) {
				throw new GatewayException("SQL Error: " + e.getMessage());
			}
		}
		
		return p;
	}

	@Override
	public void deletePart(long id) throws GatewayException {
		PreparedStatement st = null;
		try {
			
			conn.setAutoCommit(false);
		
			
			//MAKE SURE TO INCLUDE THIS
			
			st = conn.prepareStatement("delete from warehouse_part where part_id = ? ");
			st.setLong(1, id);
			st.executeUpdate();
			
			//part
			st.close();
			
			st = conn.prepareStatement("delete from part where id = ? ");
			st.setLong(1, id);
			st.executeUpdate();
			
			//if we get here, everything worked without exception so commit the changes
			conn.commit();

		} catch (SQLException e) {
			//roll back
			try {
				conn.rollback();
			} catch (SQLException e1) {
				throw new GatewayException(e1.getMessage());
			}
			throw new GatewayException(e.getMessage());
		} finally {
			//clean up
			try {
				if(st != null)
					st.close();
				//turn autocommit back on
				conn.setAutoCommit(true);
			} catch (SQLException e) {
				throw new GatewayException(e.getMessage());
			}
		}
	}

	@Override
	public long insertPart(Part p) throws GatewayException {
		
		long newId = Part.INVALID_ID;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("insert part (part_name, part_number, vendor, vendor_part_num, unit_quanitity) "
					+ " values ( ?, ?, ?, ?, ? ) ", PreparedStatement.RETURN_GENERATED_KEYS);
			st.setString(1, p.getPartName());
			st.setString(2, p.getPartNumber());
			st.setString(3, p.getVendor());
			st.setString(4, p.getVendorPartNumber());
			st.setString(5, p.getUnitQuanitity());
			
			st.executeUpdate();
			//get the generated key
			rs = st.getGeneratedKeys();
			if(rs != null && rs.next()) {
			    newId = rs.getLong(1);
			} else {
				throw new GatewayException("Could not fetch new record Id");
			}
		} catch (SQLException e) {
			throw new GatewayException(e.getMessage());
		} finally {
			//clean up
			try {
				if(st != null)
					st.close();
			} catch (SQLException e) {
				throw new GatewayException("SQL Error: " + e.getMessage());
			}
		}
		return newId;
	}

	@Override
	public void savePart(Part p) throws GatewayException {
		//execute the update and throw exception if any problem
		PreparedStatement st = null;
		
		try {
			conn.setAutoCommit(false);
			
			conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
			
			st = conn.prepareStatement("update part "
					+ " set part_name = ?, part_number = ?, vendor = ?, vendor_part_num = ?, unit_quanitity = ? "
					+ " where id = ? ");
			st.setString(1, p.getPartName());
			st.setString(2, p.getPartNumber());
			st.setString(3, p.getVendor());
			st.setString(4, p.getVendorPartNumber());
			st.setString(5, p.getUnitQuanitity());
			st.setLong(6, p.getId());
			st.executeUpdate();
			
			
		} catch (SQLException e) {
			throw new GatewayException(e.getMessage());
		} finally {
			//clean up
			try {		
				if(st != null)
					st.close();
					
					//conn.commit();
					//conn.setAutoCommit(true);
			} catch (SQLException e) {
				throw new GatewayException("SQL Error: " + e.getMessage());
			}
		}	
	}
	
	@Override
	public List<Part> fetchParts() throws GatewayException {
		ArrayList<Part> ret = new ArrayList<Part>();
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			//fetch parts
			st = conn.prepareStatement("select * from part ");
			rs = st.executeQuery();
			//add each to list of parts to return
			while(rs.next()) {
				Part p = new Part(rs.getLong("id"), rs.getString("part_name"), rs.getString("part_number")
						, rs.getString("vendor"), rs.getString("vendor_part_num")
						, rs.getString("unit_quanitity"));
				ret.add(p);
			}
		} catch (SQLException e) {
			throw new GatewayException(e.getMessage());
		} finally {
			//clean up
			try {
				if(rs != null)
					rs.close();
				if(st != null)
					st.close();
			} catch (SQLException e) {
				throw new GatewayException("SQL Error: " + e.getMessage());
			}
		}
		
		return ret;
	}
	
	
	public List<Part> getPartsByWarehouseId(long id) throws GatewayException{
		ArrayList<Part> ret = new ArrayList<Part>();
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			//fetch parts
			st = conn.prepareStatement("select * from part where id = ?");
			st.setLong(1, id);
			rs = st.executeQuery();
			//add each to list of parts to return
			while(rs.next()) {
				Part p = new Part(rs.getLong("id"), rs.getString("part_name"), rs.getString("part_number")
						, rs.getString("vendor"), rs.getString("vendor_part_num")
						, rs.getString("unit_quanitity"));
				ret.add(p);
			}
		} catch (SQLException e) {
			throw new GatewayException(e.getMessage());
		} finally {
			//clean up
			try {
				if(rs != null)
					rs.close();
				if(st != null)
					st.close();
			} catch (SQLException e) {
				throw new GatewayException("SQL Error: " + e.getMessage());
			}
		}
		
		return ret;
	}

	@Override
	public List<Part> check4PartAssociation(long id) throws GatewayException{
		
		ArrayList<Part> ret = new ArrayList<Part>();
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			//fetch parts
			st = conn.prepareStatement("select * from warehouse_part where part_id = ? ");
			st.setLong(1,id);
			rs = st.executeQuery();
			//add each to list of parts to return
			while(rs.next()) {
				Part p = new Part(rs.getLong("id"), rs.getString("part_name"), rs.getString("part_number")
						, rs.getString("vendor"), rs.getString("vendor_part_num")
						, rs.getString("unit_quanitity"));
				ret.add(p);
			}
		} catch (SQLException e) {
			throw new GatewayException(e.getMessage());
		} finally {
			//clean up
			try {
				if(rs != null)
					rs.close();
				if(st != null)
					st.close();
			} catch (SQLException e) {
				throw new GatewayException("SQL Error: " + e.getMessage());
			}
		}
		
		return ret;
	}
	
}
