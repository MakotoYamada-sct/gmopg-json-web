package com.example.demo.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.stereotype.Component;

@Component
public class EntryTranSeqDao {
	// H2データベース接続情報
	private static final String JDBC_URL = "jdbc:h2:tcp://localhost/~/gmopg-json-web";
	private static final String USER = "sa";
	private static final String PASSWORD = "";

	/**
	 * DB接続を取得する。
	 *
	 * @return Connection
	 * @throws SQLException
	 */
	private Connection getConnection() throws SQLException {
		return DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
	}

	/**
	 * オーダーIDに付与する連番をシーケンスから取得する。
	 *
	 * @return long オーダーID用の連番
	 * @throws SQLException
	 * @throws RuntimeException
	 */
	public long nextVal() {
		String sql = """
				SELECT NEXTVAL('ENTRYTRANSEQ')
				""";

		long nextVal = 0;

		try (Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				nextVal = rs.getLong(1);
			}

			return nextVal;

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("オーダーIDの連番取得に失敗しました", e);
		}
	}

}