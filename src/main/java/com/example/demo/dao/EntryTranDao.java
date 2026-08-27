package com.example.demo.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.example.demo.model.EntryTranDto;

@Component
public class EntryTranDao {
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
	 * 取引データをDBに保存する。
	 *
	 * @param EntryTranDto 
	 * @throws SQLException
	 * @throws RuntimeException
	 */
	public void insert(EntryTranDto entryTranDto) {
		String sql = """
				INSERT INTO ENTRYTRAN (
				    shopID, shopPass, orderID, jobCd, itemCode, amount, tax,
				    tdFlag, tdTenantName, tds2Type, tdRequired, accessID, accessPass,
				    memberId, status
				) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
				""";

		try (Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, entryTranDto.getShopID());
			pstmt.setString(2, entryTranDto.getShopPass());
			pstmt.setString(3, entryTranDto.getOrderID());
			pstmt.setString(4, entryTranDto.getJobCd());
			pstmt.setString(5, entryTranDto.getItemCode());
			pstmt.setString(6, entryTranDto.getAmount());
			pstmt.setString(7, entryTranDto.getTax());
			pstmt.setString(8, entryTranDto.getTdFlag());
			pstmt.setString(9, entryTranDto.getTdTenantName());
			pstmt.setString(10, entryTranDto.getTds2Type());
			pstmt.setString(11, entryTranDto.getTdRequired());
			pstmt.setString(12, entryTranDto.getAccessID());
			pstmt.setString(13, entryTranDto.getAccessPass());
			pstmt.setString(14, entryTranDto.getMemberID());
			pstmt.setString(15, entryTranDto.getStatus());

			pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("取引データの保存に失敗しました。", e);
		}
	}

	/**
	 * オーダーIDをキーにして取引データの取引状態を更新する。
	 *
	 * @param orderID オーダーID
	 * @param status 取引状態
	 * @return boolean 更新可否
	 * @throws SQLException
	 * @throws RuntimeException
	 */
	public boolean updateStatus(String orderID, String status) {
		String sql = "UPDATE ENTRYTRAN SET STATUS = ?, PROCESSDATE = CURRENT_TIMESTAMP WHERE ORDERID = ?";

		int rs;

		try (Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, status);
			pstmt.setString(2, orderID);
			rs = pstmt.executeUpdate();

		} catch (SQLException e) {
			throw new RuntimeException("取引データの更新に失敗しました。", e);
		}

		return (rs == 1); // 1件更新しているか判定
	}

	/**
	 * 取引状態をキーにして取引状態が一致する取引データのリストを取得する。
	 *
	 * @param status 取引状態
	 * @return List<EntryTranDto> 取引データリスト
	 * @throws SQLException
	 * @throws RuntimeException
	 */
	public List<EntryTranDto> selectStatus(String status) {
		List<EntryTranDto> list = new ArrayList<>();
		String sql = "SELECT * FROM ENTRYTRAN WHERE STATUS = ?";

		try (Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, status);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				EntryTranDto entryTranDto = new EntryTranDto();
				entryTranDto.setShopID(rs.getString("shopID"));
				entryTranDto.setShopPass(rs.getString("shopPass"));
				entryTranDto.setOrderID(rs.getString("orderID"));
				entryTranDto.setJobCd(rs.getString("jobCd"));
				entryTranDto.setItemCode(rs.getString("itemCode"));
				entryTranDto.setAmount(rs.getString("amount"));
				entryTranDto.setTax(rs.getString("tax"));
				entryTranDto.setTdFlag(rs.getString("tdFlag"));
				entryTranDto.setTdTenantName(rs.getString("tdTenantName"));
				entryTranDto.setTds2Type(rs.getString("tds2Type"));
				entryTranDto.setTdRequired(rs.getString("tdRequired"));
				entryTranDto.setAccessID(rs.getString("accessID"));
				entryTranDto.setAccessPass(rs.getString("accessPass"));
				entryTranDto.setMemberID(rs.getString("memberId"));
				entryTranDto.setStatus(rs.getString("status"));
				entryTranDto.setProcessDate(rs.getString("processDate"));
				list.add(entryTranDto);
			}
		} catch (SQLException e) {
			throw new RuntimeException("取引データの取得に失敗しました。", e);
		}
		return list;
	}

	/**
	 * オーダーIDをキーにして取引データを１件取得する。
	 *
	 * @param orderID オーダーID
	 * @return EntryTranDto 取引データ
	 * @throws SQLException
	 * @throws RuntimeException
	 */
	public EntryTranDto selectOrderID(String orderID) {
		EntryTranDto entryTranDto = new EntryTranDto();
		String sql = "SELECT * FROM ENTRYTRAN WHERE orderID = ?";

		try (Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, orderID);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				entryTranDto.setShopID(rs.getString("shopID"));
				entryTranDto.setShopPass(rs.getString("shopPass"));
				entryTranDto.setOrderID(rs.getString("orderID"));
				entryTranDto.setJobCd(rs.getString("jobCd"));
				entryTranDto.setItemCode(rs.getString("itemCode"));
				entryTranDto.setAmount(rs.getString("amount"));
				entryTranDto.setTax(rs.getString("tax"));
				entryTranDto.setTdFlag(rs.getString("tdFlag"));
				entryTranDto.setTdTenantName(rs.getString("tdTenantName"));
				entryTranDto.setTds2Type(rs.getString("tds2Type"));
				entryTranDto.setTdRequired(rs.getString("tdRequired"));
				entryTranDto.setAccessID(rs.getString("accessID"));
				entryTranDto.setAccessPass(rs.getString("accessPass"));
				entryTranDto.setMemberID(rs.getString("memberID"));
				entryTranDto.setStatus(rs.getString("status"));
				entryTranDto.setProcessDate(rs.getString("processDate"));
			}

		} catch (SQLException e) {
			throw new RuntimeException("取引データの取得に失敗しました。", e);
		}

		return entryTranDto;
	}

}
