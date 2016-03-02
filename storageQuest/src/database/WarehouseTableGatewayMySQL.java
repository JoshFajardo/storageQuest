package database;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;


import models.Part;
import models.PartList;
import models.Warehouse;
import models.WarehousePart;

public class WarehouseTableGatewayMySQL implements WarehouseTableGateway{
	private static final SimpleDateFormat DB_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	private static final boolean DEBUG = true;
	
	private Connection conn = null;
	
	
	
	public WarehouseTableGatewayMySQL() throws GatewayException{
		
		DataSource ds = null;
		try{
			ds = getDataSource();
			
		}catch (RuntimeException | IOException e1){
			throw new GatewayException(e1.getMessage());
		}
		if(ds == null){
			throw new GatewayException("datasource is null!");
		}
		try{
			conn = ds.getConnection();
		}catch (SQLException e){
			throw new GatewayException("SQL Error: "+ e.getMessage());
		}
		
	}
	
	public Warehouse fetchWarehouse(long id) throws GatewayException{
		Warehouse w = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try{
			st = conn.prepareStatement("select * from warehouse where id = ? ");
			st.setLong(1, id);
			rs = st.executeQuery();
			
			rs.next();
			w = new Warehouse(rs.getLong("id"),rs.getString("Warehouse_name"),
					rs.getString("address"),rs.getString("city"),
					rs.getString("state"),rs.getString("zip"),rs.getInt("StorageCap"));
		}catch(SQLException e){
			throw new GatewayException(e.getMessage());
		}finally {
			try{
				// this is closing the connection
				if(rs != null)
					rs.close();
				if(st != null)
					st.close();
			}catch (SQLException e){
				throw new GatewayException("SQL Error: "+ e.getMessage());
			}
		}
		return w;
	}

	public boolean warehouseAlreadyExists(long id, String wn) throws GatewayException{
		PreparedStatement st = null;
		ResultSet rs = null;
		try{
			st = conn.prepareStatement("select count(id) as num_records "
					+ " from warehouse where warehouse_name = ? and id <> ? ");
			st.setString(1, wn);
			st.setLong(2, id);
			rs = st.executeQuery();
			
			rs.next();
			if(rs.getInt("num_records") > 0)
				return true;
			
		}catch (SQLException e){
			throw new GatewayException(e.getMessage());
		}finally {
			try{
				if(rs != null)
					rs.close();
				if(st != null)
					st.close();
			}catch (SQLException e){
				throw new GatewayException("SQL Error: "+ e.getMessage());
			}
		}
		return false;
	}
	
	
	public void deleteWarehouse(long id) throws GatewayException{
		PreparedStatement st = null;
		try{
			//auto commit is turned off
			conn.setAutoCommit(false);
			
			// delete any warehouse records for the given id
			st = conn.prepareStatement("delete from warehouse_part where warehouse_id = ? ");
			st.setLong(1, id);
			st.executeUpdate();
			
			st.close();
			st = conn.prepareStatement("delete from warehouse where id = ? ");
			st.setLong(1,id);
			st.executeUpdate();
			
			//everything worked, so commit
			conn.commit();
			
		}catch (SQLException e){
			//rolls back
			try{
				conn.rollback();
			}catch (SQLException e1){
				throw new GatewayException(e.getMessage());
			}
			throw new GatewayException (e.getMessage());
		}finally {
			try{
				if(st != null)
					st.close();
				//turn auto commit back on
				conn.setAutoCommit(true);
			}catch (SQLException e){
				throw new GatewayException(e.getMessage());
			}
		}
	}
	
	public long insertWarehouse(Warehouse w) throws GatewayException {
		
		long newId = Warehouse.INVALID_ID;
		PreparedStatement st = null;
		ResultSet rs = null;
		try{
			st = conn.prepareStatement("insert warehouse (warehouse_name, address, city, state, zip, StorageCap)"
					+ " values ( ?, ?, ?, ?, ?, ? ) ", PreparedStatement.RETURN_GENERATED_KEYS);
			st.setString(1, w.getWarehouseName());
			st.setString(2, w.getAddress());
			st.setString(3, w.getCity());
			st.setString(4, w.getState());
			st.setString(5, w.getZip());
			st.setInt(6, w.getStorageCapacity());
			st.executeUpdate();
			
			rs = st.getGeneratedKeys();
			if(rs !=null && rs.next()){
				newId = rs.getLong(1);
			}else{
				throw new GatewayException("Could not fetch new record Id");
			}
		 	
		}catch (SQLException | NullPointerException e){
			throw new GatewayException(e.getMessage());
		}finally{
			
			try{
				if(st !=null)
					st.close();
			}catch (SQLException e){
				throw new GatewayException("SQL Error: "+e.getMessage());
			}
		}
		return newId;
	}
	public void saveWarehouse(Warehouse w) throws GatewayException {
		
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("update warehouse "
					+ " set warehouse_name = ?, address = ?, city = ?, state = ?, zip = ?, StorageCap = ?"
					+ " where id = ? ");
			st.setString(1, w.getWarehouseName());
			st.setString(2, w.getAddress());
			st.setString(3, w.getCity());
			st.setString(4, w.getState());
			st.setString(5, w.getZip());
			st.setInt(6, w.getStorageCapacity());
			st.setLong(7, w.getId());	
			st.executeUpdate();
		} catch (SQLException e) {
			throw new GatewayException(e.getMessage());
		} finally {
			
			try {
				if(st != null)
					st.close();
			} catch (SQLException e) {
				throw new GatewayException("SQL Error: " + e.getMessage());
			}
		}
	}
	
    
	public List<Warehouse> fetchWarehouse() throws GatewayException {
		ArrayList<Warehouse> ret = new ArrayList<Warehouse>();
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			//fetch the Warehouse
			st = conn.prepareStatement("select * from warehouse");
			rs = st.executeQuery();
			//adds each to a list of warehouses
			while(rs.next()) {
				Warehouse w = new Warehouse(rs.getLong("id"),rs.getString("Warehouse_name"),
						rs.getString("address"),rs.getString("city"),
						rs.getString("state"),rs.getString("zip"),rs.getInt("StorageCap"));

				ret.add(w);
			}
		} catch (SQLException e) {
			throw new GatewayException(e.getMessage());
		} finally {
			
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
	
	public void close() {
		if(DEBUG)
			System.out.println("Closing db connection...");
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

    //gets the information of the database form the db.properties file
	private DataSource getDataSource() throws RuntimeException, IOException {
		
		Properties props = new Properties();
		FileInputStream fis = null;
        fis = new FileInputStream("db.properties");
        props.load(fis);
        fis.close();
        
        
        MysqlDataSource mysqlDS = new MysqlDataSource();
        mysqlDS.setURL(props.getProperty("MYSQL_DB_URL"));
        mysqlDS.setUser(props.getProperty("MYSQL_DB_USERNAME"));
        mysqlDS.setPassword(props.getProperty("MYSQL_DB_PASSWORD"));
        return mysqlDS;
	}

    // list of warehouse parts from the database
	@Override
	public List<WarehousePart> fetchWarehouseParts(Warehouse w, PartList pList) throws GatewayException {
		ArrayList<WarehousePart> ret = new ArrayList<WarehousePart>();
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			//fetch warehouse
			st = conn.prepareStatement("select * from warehouse_part where warehouse_id = ?");
			st.setLong(1, w.getId());
			rs = st.executeQuery();
			//add each to list of people to return
			while(rs.next()) {
				Part p = pList.findById(rs.getLong("part_id"));
				if(p == null)
					throw new GatewayException("No part found in the part list with id: " + rs.getLong("part_id"));
				WarehousePart wp = new WarehousePart(w, p);
				try {
					String testDate = rs.getString("warehouse_start_date");
					if(testDate.length() > 0 && !testDate.equals("0000-00-00"))
						wp.setOwnerStartDate(DB_DATE_FORMAT.parse(testDate));
				} catch (ParseException | SQLException e) {
					System.err.println("Invalid warehouse start date read from database. Initializing to now...");
				}
				ret.add(wp);
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
	public void saveWarehousePart(WarehousePart wp) throws GatewayException {
		//execute the update 
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("update warehouse_part "
					+ " set warehouse_start_date = ? "
					+ " where warehouse_id = ? and part_id = ? ");
			st.setString(1, DB_DATE_FORMAT.format(wp.getOwnerStartDate()));
			st.setLong(2, wp.getOwner().getId());	
			st.setLong(3, wp.getPart().getId());	
			st.executeUpdate();
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
	}

	@Override
	public void insertWarehousePart(WarehousePart wp) throws GatewayException {
		//execute the insert and throw exception if any problem
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("insert into warehouse_part "
					+ " (warehouse_id, part_id, owner_start_date) "
					+ " values (?, ?, ?) ");
			st.setLong(1, wp.getOwner().getId());	
			st.setLong(2, wp.getPart().getId());	
			st.setString(3, DB_DATE_FORMAT.format(wp.getOwnerStartDate()));
			st.executeUpdate();
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
	}

	@Override
	public void deleteWarehousePart(WarehousePart wp) throws GatewayException {
		
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("delete from warehouse_part "
					+ " where warehouse_id = ? and part_id = ? ");
			st.setLong(1, wp.getOwner().getId());	
			st.setLong(2, wp.getPart().getId());	
			st.executeUpdate();
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
	}
}
