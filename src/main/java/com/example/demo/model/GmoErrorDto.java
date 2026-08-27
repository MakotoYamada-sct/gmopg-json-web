package com.example.demo.model;

import lombok.Data;

/**
 * エラーデータ
 */
@Data
public class GmoErrorDto {
	private String errCode;
	private String errInfo;

	public GmoErrorDto(String errCode, String errInfo) {
		setErrCode(errCode);
		setErrInfo(errInfo);
	}

	@Override
	public String toString() {
		return "{errCode='" + errCode + "', errInfo='" + errInfo + "'}";
	}
}
