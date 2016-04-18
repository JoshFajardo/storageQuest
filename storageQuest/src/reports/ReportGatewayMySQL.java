package reports;

import java.io.FileInputStream;
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

import database.GatewayException;

import models.Part;
import models.Warehouse;
import models.WarehousePart;

public class ReportGatewayMySQL implements ReportGateway {
	private static final SimpleDateFormat DB_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	
	
	private Connection conn = null;
	
	public ReportGatewayMySQL() throws GatewayException{
		
		DataSource ds = null;
		try{
			ds = getDataSource();
		}catch (RuntimeException  | IOException e){
			throw new GatewayException(e.getMessage());
		}
		if(ds == null){
			throw new GatewayException("Datasource is null!");
		}
		try{
			conn = ds.getConnection();
			
		}catch(SQLException e1){
			throw new GatewayException("SQL Error: "+e1.getMessage());
		}
	}
	public List<WarehousePart> fetchWarehouseAndParts() throws GatewayException{
		List<WarehousePart> ret = new ArrayList<WarehousePart>();
		PreparedStatement st = null;
		ResultSet rs = null;
		try{
			
			st = conn.prepareStatement("select a.id as warehouse_id, a.warehouse_name"
					+ " , a.address, a.city, a.city, a.state, a.zip, a.StorageCap"
					+ " , c.id as part_id, c.part_number, c.part_name, c.vendor, c.vendor_part_num, c.unit_quanitity "
					+ " , b.quantity"
					+ "  from warehouse a"
					+ " inner join warehouse_part b on a.id = b.warehouse_id "
					+ " inner join part c on b.part_id = c.id "
					+ "order by a.warehouse_name, c.part_number, c.part_name ");
			rs = st.executeQuery();
			
			while(rs.next()){
				Warehouse w = new Warehouse(rs.getLong("warehouse_id"),rs.getString("Warehouse_name"),
						rs.getString("address"),rs.getString("city"),
						rs.getString("state"),rs.getString("zip"),rs.getInt("StorageCap"));
				
				Part p = new Part (rs.getLong("part_id"),rs.getString("part_name"),rs.getString("part_number"),
						rs.getString("vendor"),rs.getString("vendor_part_num"),rs.getString("unit_quanitity"));
				
				WarehousePart wp = new WarehousePart(w,p);

				ret.add(wp);
			}
		}catch(SQLException e){
			throw new GatewayException (e.getMessage());
		}finally{
			
			try{
				if(rs != null)
					rs.close();
				if(st != null)
					st.close();
			}catch(SQLException e){
				throw new GatewayException ("SQL Error: " + e.getMessage());
			}
		}
		return ret;
	}
	
	private DataSource getDataSource() throws RuntimeException, IOException{
		
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
	
	@Override
	public void close(){
		try{
			conn.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
		
	}


}
