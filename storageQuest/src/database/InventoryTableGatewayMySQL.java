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
	
	
	
	
}
