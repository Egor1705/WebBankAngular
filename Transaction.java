package impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Transaction {

	private final static String URL = "jdbc:mysql://localhost/Bank?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";

	private final static String DRIVER = "com.mysql.cj.jdbc.Driver";
	private final static String root = "root";
	private final static String password = "Bottomass1375";

	private static Scanner sc = new Scanner(System.in);

	public static String openBill(String id_Bill) throws SQLException {

		try {
			Class.forName(DRIVER);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Connection con = DriverManager.getConnection(URL, root, password);
		Statement st = con.createStatement();


		String sql = "select id_bill from bill where id_bill='" + id_Bill + "';";

		ResultSet resultSet1 = st.executeQuery(sql);
		String name1 = "";
		while (resultSet1.next()) {
			name1 = resultSet1.getString(1);
			// System.out.println("------ " + name1);
		}
		resultSet1.close();
		int resultSet = 0;
		String value = "";
		String sql1 = "select status_of_bill from bill where id_bill='" + id_Bill + "';";
		ResultSet resultSetStatus = st.executeQuery(sql1);
		String name2 = "";
		while (resultSetStatus.next()) {
			name2 = resultSetStatus.getString(1);
			// System.out.println("------ " + name2);
		}
		resultSetStatus.close();

		if (!name1.equals(id_Bill)) {
			// System.out.println("Такого счета не существует \n");
			value = "There is no such bill";

		}

		else if (name2.equals("Open")) {
			// System.out.println("Этот счет уже закрыт \n");
			value = "Open";

		} else {
			String sql2 = "update  Bill set status_of_bill = 'Open' where id_bill = '" + id_Bill + "' ";
			resultSet = st.executeUpdate(sql2);
			// System.out.println("Счет закрыт \n");
			value = "correct";
		}

		con.close();
		return value;
	}

	public static String closeBill(String id_Bill) throws SQLException {

		try {
			Class.forName(DRIVER);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // необязательно

		Connection con = DriverManager.getConnection(URL, root, password);
		Statement st = con.createStatement();

		String sql = "select id_bill from bill where id_bill='" + id_Bill + "';";

		ResultSet resultSet1 = st.executeQuery(sql);
		String name1 = "";
		while (resultSet1.next()) {
			name1 = resultSet1.getString(1);
			// System.out.println("------ " + name1);
		}
		resultSet1.close();
		int resultSet = 0;
		String value = "";
		String sql1 = "select status_of_bill from bill where id_bill='" + id_Bill + "';";
		ResultSet resultSetStatus = st.executeQuery(sql1);
		String name2 = "";
		while (resultSetStatus.next()) {
			name2 = resultSetStatus.getString(1);
			// System.out.println("------ " + name2);
		}
		resultSetStatus.close();

		if (!name1.equals(id_Bill)) {
			// System.out.println("Такого счета не существует \n");
			value = "There is no such bill";

		}

		else if (name2.equals("Close")) {
			// System.out.println("Этот счет уже закрыт \n");
			value = "Close";

		} else {
			String sql2 = "update  Bill set status_of_bill = 'Close' where id_bill = '" + id_Bill + "' ";
			resultSet = st.executeUpdate(sql2);
			// System.out.println("Счет закрыт \n");
			value = "correct";
		}

		con.close();
		return value;
	}

	public static boolean login(String id, String pwd) throws SQLException {
		Connection con = null;
		Statement statement = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(URL, root, password);
			if (con != null) {
				System.out.println("Connected to the database");
			}
		} catch (ClassNotFoundException ex) {
			System.out.println("ERROR");
			ex.printStackTrace();
		}
		Statement st = con.createStatement();

		ResultSet resultSet1 = st.executeQuery("select login, password from credentials " + "where login = '" + id
				+ "' and password = '" + pwd + "';");

		if (resultSet1.next() == false) {
			System.out.println("No such user.");
			return false;
		} else {
			System.out.println("Found user!");
		}
		resultSet1.close();
		return true;
	}

	public static int putMoney(String idForPut, int sum) throws SQLException {
		// int k = 0;

		try {
			Class.forName(DRIVER);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // необязательно

		Connection con = DriverManager.getConnection(URL, root, password);
		Statement st = con.createStatement();

		String sql = "select id_bill from bill where id_bill='" + idForPut + "';";

		ResultSet resultSet1 = st.executeQuery(sql);
		String name1 = "";
		while (resultSet1.next()) {
			name1 = resultSet1.getString(1);
			// System.out.println("------ " + name1);
		}
		resultSet1.close();
		int resultSet = 0;

		String sql1 = "select status_of_bill from bill where id_bill='" + idForPut + "';";
		ResultSet resultSetStatus = st.executeQuery(sql1);
		String status = "";
		while (resultSetStatus.next()) {
			status = resultSetStatus.getString(1);
			// System.out.println("------ " + name2);
		}
		resultSetStatus.close();

		if (!name1.equals(idForPut)) {
			// System.out.println("Такого счета не существует \n");
			// String output = "{\"response\": \"no such bill\"+ 1}";
			status = "There is no such bill";
			sum = -1;
		}

		else if (status.equals("Close")) {
			// System.out.println("Этот счет уже закрыт \n");

			sum = -2;

		} else {
			String sqlCount = "select count(*) from Transaction;";
			ResultSet rsCount = st.executeQuery(sqlCount);
			String nameCount = "";
			while (rsCount.next()) {
				nameCount = rsCount.getString(1);
			}
			int i = 1 + Integer.parseInt(nameCount);

			String sql2 = "insert into Transaction values('" + i + "', '" + sum + "', ' " + idForPut + "')";
			resultSet = st.executeUpdate(sql2);

		}

		con.close();

		return sum;
	}

	public static String showBill(String id_Bill) throws SQLException {

		try {
			Class.forName(DRIVER);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // необязательно

		Connection con = DriverManager.getConnection(URL, root, password);
		Statement st = con.createStatement();
		String sql = "select id_bill from bill where id_bill='" + id_Bill + "';";

		ResultSet resultSet1 = st.executeQuery(sql);
		String name1 = "";
		while (resultSet1.next()) {
			name1 = resultSet1.getString(1);
			// System.out.println("------ " + name1);
		}
		resultSet1.close();

		String sql1 = "select status_of_bill from bill where id_bill='" + id_Bill + "';";
		ResultSet resultSetStatus = st.executeQuery(sql1);
		String name2 = "";
		while (resultSetStatus.next()) {
			name2 = resultSetStatus.getString(1);
			// System.out.println("------ " + name2);
		}
		resultSet1.close();

		ResultSet resultSet = null;
		String sum = "";
		if (!name1.equals(id_Bill)) {
			// System.out.println("Такого счета не существует \n");
		} else if (name2.equals("Close")) {
			// System.out.println("Любые транзакции невозможны,счет закрыт \n");
		} else {
			String sql2 = "select sum(sum) from transaction \r\n" + "where id_transact_bill='" + id_Bill + "';";
			resultSet = st.executeQuery(sql2);
			while (resultSet.next()) {
				sum = resultSet.getString(1);

			}
		}

		return sum;

	}

	public static int takeMoney(String idForTake, int sum) throws SQLException {
		// int k = 0;
		try {
			Class.forName(DRIVER);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // необязательно

		Connection con = DriverManager.getConnection(URL, root, password);
		Statement st = con.createStatement();
		String sql = "select id_bill from bill where id_bill='" + idForTake + "';";

		ResultSet resultSet1 = st.executeQuery(sql);
		String name1 = "";
		while (resultSet1.next()) {
			name1 = resultSet1.getString(1);
			// System.out.println("------ " + name1);
		}
		resultSet1.close();

		String sql1 = "select status_of_bill from bill where id_bill='" + idForTake + "';";
		ResultSet resultSetStatus = st.executeQuery(sql1);
		String name2 = "";
		while (resultSetStatus.next()) {
			name2 = resultSetStatus.getString(1);
			// System.out.println("------ " + name2);
		}
		resultSet1.close();

		String sql2 = "select sum(sum) from transaction \r\n" + "where id_transact_bill='" + idForTake + "';";
		ResultSet resultSet = st.executeQuery(sql2);
		String name3 = "";

		while (resultSet.next()) {
			name3 = resultSet.getString(1);
			// System.out.println("sum "+ name3);
		}

		int count = 0;
		try {
			count = Integer.parseInt(name3);
		} catch (NumberFormatException e) {

		}

		String sqlCount = "select count(*) from Transaction;";
		ResultSet rsCount = st.executeQuery(sqlCount);
		String nameCount = "";
		while (rsCount.next()) {
			nameCount = rsCount.getString(1);
		}

		int i = 1 + Integer.parseInt(nameCount);

		if (!name1.equals(idForTake)) {
			// System.out.println("Такого счета не существует \n");
			// String output = "{\"response\": \"no such bill\"+ 1}";
			// status = "There is no such bill";
			sum = -1;
		}

		else if (name2.equals("Close")) {
			// System.out.println("Этот счет уже закрыт \n");

			sum = -2;

		} else if (sum > count) {

			sum = -3;
		}

		else {
			String sql4 = "insert into Transaction values('" + i + "', '" + -sum + "','" + idForTake + "')";
			int resultSet3 = st.executeUpdate(sql4);
		}

		con.close();

		return sum;
	}

}
