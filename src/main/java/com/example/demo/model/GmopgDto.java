package com.example.demo.model;

import lombok.Data;

/**
 * 画面パラメータ
 */
@Data
public class GmopgDto {
	private String orderID;
	private String memberID;
	private String memberName;
	private String amount;
	private String accessID;
	private String accessPass;
	private String status;
	private String message;
	private String apiResponse; // 画面確認用の生JSON文字列
	private String processDate;
}
