package com.example.demo.exception;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.demo.config.GmopgConst;
import com.example.demo.model.GmoErrorDto;

import lombok.Getter;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

public class gmoEroorException extends RuntimeException {

	@Getter
	private List<GmoErrorDto> gmoErrorList;

	public gmoEroorException(Map<String, String> entryRes) {
		super();

		// JSONの配列/オブジェクト（[...] または {...}）にマッチする正規表現
		Pattern pattern = Pattern.compile("[\\[\\{].*[\\]\\}]");
		Matcher matcher = pattern.matcher(entryRes.get(GmopgConst.Fields.Error).toString());

		String jsonPart = "";

		if (matcher.find()) {
			jsonPart = matcher.group();
			//	System.out.println("抽出結果: " + jsonPart);
		} else {
			System.out.println("JSON部分が見つかりませんでした。");
		}

		ObjectMapper mapper = new ObjectMapper();

		gmoErrorList = mapper.readValue(jsonPart, new TypeReference<List<GmoErrorDto>>() {
		});

	}

}
