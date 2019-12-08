import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import impl.Transaction;

public class SocketWithGet {
	private final static String URL = "jdbc:mysql://localhost/Bank?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";

	private final static String DRIVER = "com.mysql.cj.jdbc.Driver";
	private final static String root = "root";
	private final static String password = "Bottomass1375";

	public static void main(String[] args) throws IOException, SQLException {
		// TODO Auto-generated method stub

		Date today = new Date();
		String header = "HTTP/1.1 201 \r\n" + today + "\r\n" + "Content-Type: text/json\r\n"
				+ "Access-Control-Allow-Origin: *\r\n" + "Connection: close\r\n" + "\r\n";

		for (;;) {
			try {
				ServerSocket serverSock = new ServerSocket(5010);
				System.out.println("Waiting for new connections");
				Socket sock = serverSock.accept();
				InputStream sis = sock.getInputStream();

				BufferedReader br = new BufferedReader(new InputStreamReader(sis));
				String request = br.readLine();
				System.out.println("request: " + request);

				String[] requestParam = request.split(" ");
				String requestURL = requestParam[1];
				String[] params = requestURL.split("&");

				Map<String, String> query_pairs = new LinkedHashMap<String, String>();
				for (String pair : params) {
					int idx = pair.indexOf("=");
					query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"),
							URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
				}

				String id = query_pairs.get("/login?log");
				String pwd = query_pairs.get("pwd");
				String idForSum = query_pairs.get("sum");
				String idForOpen = query_pairs.get("/openBill?open");
				String idForClose = query_pairs.get("/closeBill?close");
				String idForPut = query_pairs.get("/putMoney?put");
				String idForTake = query_pairs.get("/takeMoney?take");
				String idForShowSum = query_pairs.get("/showSum?sumOfBill");
				int statusPut = 0;
				int statusTake = 0;
				int showSum = 0;

				String[] requestURL2 = requestURL.split("=");
				String loginURL = requestURL2[0];
				System.out.println("loginURL: " + loginURL);

				/* validating login */
				if (loginURL.equals("/login?log")) {
					boolean valid = Transaction.login(id, pwd);
					System.out.println("Valid: " + valid);
					if (!valid) {
						String output = "{\"response\": \"fail\"}";
						sendResponse(header, output, sock);
					} else {
						String output = "{\"response\": \"success\"}";
						sendResponse(header, output, sock);
					}
				}

				if (loginURL.equals("/openBill?open")) {
					String status = Transaction.openBill(idForOpen);
					System.out.println("Status is " + status);
					if (status.equals("There is no such bill")) {
						String output = "{\"response\": \"no such bill\"}";
						sendResponse(header, output, sock);
					} else if (status.equals("Open")) {
						String output = "{\"response\": \"it's just opened\"}";
						sendResponse(header, output, sock);
					}

					else {
						String output = "{\"response\": \"success\"}";
						sendResponse(header, output, sock);
					}
				}

				if (loginURL.equals("/closeBill?close")) {
					String status = Transaction.closeBill(idForClose);
					if (status.equals("There is no such bill")) {
						String output = "{\"response\": \"no such bill\"}";
						sendResponse(header, output, sock);
					} else if (status.equals("Close")) {
						String output = "{\"response\": \"it's just closed\"}";
						sendResponse(header, output, sock);
					} else {
						String output = "{\"response\": \"success\"}";
						sendResponse(header, output, sock);
					}
				}
				if (loginURL.equals("/putMoney?put")) {
					statusPut = Transaction.putMoney(idForPut, Integer.parseInt(idForSum));

					if (statusPut == -1) {
						String output = "{\"response\": \"no such bill\"}";
						sendResponse(header, output, sock);
					} else if (statusPut == -2) {
						String output = "{\"response\": \"close!Cannot do any transactions\"}";
						sendResponse(header, output, sock);
					} else {
						String output = "{\"response\": " + statusPut + " }";
						sendResponse(header, output, sock);

					}
				}

				if (loginURL.equals("/takeMoney?take")) {
					statusTake = Transaction.takeMoney(idForTake, Integer.parseInt(idForSum));

					if (statusTake == -1) {
						String output = "{\"response\": \"no such bill\"}";
						sendResponse(header, output, sock);
					} else if (statusTake == -2) {
						String output = "{\"response\": \"close!Cannot do any transactions\"}";
						sendResponse(header, output, sock);
					} else if (statusTake == -3) {
						String output = "{\"response\": \"There are not enough money\"}";
						sendResponse(header, output, sock);
					} else {
						String output = "{\"response\": " + statusTake + " }";
						sendResponse(header, output, sock);
					}

				}

				if (loginURL.equals("/showSum?sumOfBill")) {
					showSum = showSum(idForShowSum);
					if (showSum == -1) {
						String output = "{\"response\": \"no such bill\"}";
						sendResponse(header, output, sock);
					} else if (showSum == -2) {
						String output = "{\"response\": \"close!Cannot do any transactions\"}";
						sendResponse(header, output, sock);
					} else {
						String output = "{\"response\": " + showSum + " }";
						sendResponse(header, output, sock);
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	public static int showSum(String idForShowSum) throws SQLException {
		try {
			Class.forName(DRIVER);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // необязательно

		Connection con = DriverManager.getConnection(URL, root, password);
		Statement st = con.createStatement();
		String sql = "select id_bill from bill where id_bill='" + idForShowSum + "';";

		ResultSet resultSet1 = st.executeQuery(sql);
		String name1 = "";
		while (resultSet1.next()) {
			name1 = resultSet1.getString(1);
			// System.out.println("------ " + name1);
		}
		resultSet1.close();

		String sql1 = "select status_of_bill from bill where id_bill='" + idForShowSum + "';";
		ResultSet resultSetStatus = st.executeQuery(sql1);
		String name2 = "";
		int sum = 0;
		while (resultSetStatus.next()) {
			name2 = resultSetStatus.getString(1);
			// System.out.println("------ " + name2);
		}
		resultSet1.close();
		ResultSet resultSet = null;

		if (!name1.equals(idForShowSum)) {
			// System.out.println("Такого счета не существует \n");
			sum = -1;
		} else if (name2.equals("Close")) {
			// System.out.println("Любые транзакции невозможны,счет закрыт \n");
			sum = -2;
		} else {
			String sql2 = "select sum(sum) from transaction \r\n" + "where id_transact_bill='" + idForShowSum + "';";
			resultSet = st.executeQuery(sql2);
			while (resultSet.next()) {
				sum = resultSet.getInt(1);

			}
		}

		return sum;

	}

	public static void sendResponse(String header, String output, Socket sock) {
		System.out.println(header);
		String httpResponse = header + output;
		System.out.println(httpResponse);
		try {
			sock.getOutputStream().write(httpResponse.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException ex) {
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
