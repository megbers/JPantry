package org.egbers.homeautomation.kitchen.upc.output;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.sql.DataSource;

import org.codehaus.jettison.json.JSONObject;
import org.egbers.homeautomation.kitchen.upc.domain.Item;
import org.springframework.beans.factory.annotation.Autowired;

public class DerbyBasedUPCListDAO implements UPCListDAO {

	private DataSource dataSource;

	public void write(final JSONObject product) throws Exception {
		Connection conn = dataSource.getConnection();
		Statement stmt = conn.createStatement();

		String number = product.optString("number");
		String name = product.optString("description");

		String sql = "INSERT INTO needed_products (upc, description)" + " VALUES ('" + number + "','" + name + "')";

		stmt.execute(sql);
		stmt.close();
		conn.commit();
	}

	public String read() throws Exception {
		Connection conn = dataSource.getConnection();
		Statement stmt = conn.createStatement();
		String sql;
		sql = "SELECT upc, description FROM needed_products";

		ResultSet rs = stmt.executeQuery(sql);

		StringBuilder builder = new StringBuilder();
		while (rs.next()) {
			// Retrieve by column name
			String upc = rs.getString("upc");
			String description = rs.getString("description");
			builder.append(upc).append(" - ").append(description).append("\n");
		}
		rs.close();
		stmt.close();

		return builder.toString();
	}

	public void reset() throws Exception {
		Connection conn = dataSource.getConnection();
		Statement stmt = conn.createStatement();

		String sql = "DELETE FROM needed_products";

		stmt.execute(sql);
		stmt.close();
		conn.commit();

	}

	public DataSource getDataSource() {
		return dataSource;
	}

	@Autowired
	public void setDataSource(final DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public Item find(String upcCode) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
