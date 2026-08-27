package com.example.demo.config;

import lombok.experimental.FieldNameConstants;

@FieldNameConstants
public class GmopgConst {
	/**
	 * GMO 取引状態、ジョブコード
	 */
	private String UNPROCESSED; // 未決済
	private String AUTH; // 仮売上
	private String SALES; // 実売上
	private String VOID; // 取消
	private String RETURN; // 返品
	private String RETURNX; // 月跨返品
	private String CANCEL; // キャンセル

	/**
	 * エラーキーワード
	 */
	private String Error;
}