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
import models.WarehouseList;
import models.InventoryItem;


public class InventoryTableGatewayMySQL implements InventoryTableGateway {

	private static final SimpleDateFormat DB_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	private static final boolean DEBUG = true;
	
	private Connection conn = null;
	
	public InventoryTableGatewayMySQL() throws GatewayException{
		
		DataSource ds = null;
		try{
			ds = getDataSource();
		}catch(RuntimeException | IOException e1){
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
	
	private DataSource getDataSource() throws RuntimeException, IOException{
		
		Properties props = new Properties();
		FileInputStream fis = null;
		fis = new FileInputStream("db.properties");
		props.load(fis);
		
		MysqlDataSource mysqlDS = new MysqlDataSource();
		mysqlDS.setURL(props.getProperty("MYSQL_DB_URL"));
        mysqlDS.setUser(props.getProperty("MYSQL_DB_USERNAME"));
        mysqlDS.setPassword(props.getProperty("MYSQL_DB_PASSWORD"));
        return mysqlDS;
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

	@Override
	public InventoryItem fetchInventory(long id) throws GatewayException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<InventoryItem> fetchInventory() throws GatewayException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void getWarehouseListValues(List<Integer> keys, List<String> values) throws GatewayException {
		// gets warehouse name and id
		PreparedStatement st = null;
		ResultSet rs = null;
		try{
			//fetch the warehouse name
			st = conn.prepareStatement("select warehouse_name from warehouse order by id ");
			rs = st.executeQuery();
			// go through the records and populate the list
			while(rs.next()){
				keys.add(rs.getInt("id"));
				values.add(rs.getString("warehouse_name"));
			}
		}catch(SQLException e){
			throw new GatewayException(e.getMessage());
		}finally{
			
			try{
				if(rs != null)
					rs.close();
				if(st != null)
					st.close();
			}catch (SQLException e){
				throw new GatewayException("SQL Exception: "+ e.getMessage());			
			}
		}
	}

	@Override
	public void getPartListValues(List<Integer> keys, List<String> values) throws GatewayException {
		// TODO Auto-generated method stub
		
	}
}
