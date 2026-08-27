package com.example.demo.model;

import lombok.Data;

/**
 * 取引データ
 */
@Data
public class EntryTranDto {
	private String shopID;
	private String shopPass;
	private String orderID;
	private String jobCd;
	private String itemCode;
	private String amount;
	private String tax;
	private String tdFlag;
	private String tdTenantName;
	private String tds2Type;
	private String tdRequired;
	private String accessID;
	private String accessPass;
	private String memberID;
	private String status;
	private String processDate;
}
