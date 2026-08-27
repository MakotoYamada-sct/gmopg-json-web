package com.example.demo.service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.config.GmopgConfig;
import com.example.demo.config.GmopgConst;
import com.example.demo.dao.EntryTranDao;
import com.example.demo.dao.EntryTranSeqDao;
import com.example.demo.exception.gmoEroorException;
import com.example.demo.model.EntryTranDto;
import com.example.demo.model.GmopgDto;

@Service
public class GmopgJsonWebService {

	@Autowired
	private GmopgConfig gmopgConfig;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private EntryTranDao entryTranDao;

	@Autowired
	private EntryTranSeqDao entryTranSeqDao;

	/**
	 * 取引登録実行
	 * 
	 * @param memberId 会員ID
	 * @param amount 金額
	 * @return String オーダーID
	 */
	public String executeEntryTran(String memberId, String amount) {
		// [EntryTran] 取引登録
		Map<String, Object> entryReq = new HashMap<>();
		entryReq.put("shopID", gmopgConfig.getShopId());
		entryReq.put("shopPass", gmopgConfig.getShopPass());

		long nextVal = entryTranSeqDao.nextVal();
		String orderId = gmopgConfig.getShopOrderPrefix() + String.format("%08d", nextVal);
		entryReq.put("orderID", orderId);

		entryReq.put("jobCd", GmopgConst.Fields.AUTH);
		entryReq.put("amount", amount);

		Map entryRes = callApi("/EntryTran.json", entryReq);

		if (entryRes.containsKey(GmopgConst.Fields.Error))
			throw new gmoEroorException(entryRes);

		// 取引登録データをDBに保存
		EntryTranDto entryTranDto = new EntryTranDto();

		entryTranDto.setShopID(entryReq.get("shopID").toString());
		entryTranDto.setShopPass(entryReq.get("shopPass").toString());
		entryTranDto.setOrderID(entryReq.get("orderID").toString());
		entryTranDto.setJobCd(entryReq.get("jobCd").toString());
		entryTranDto.setAmount(entryReq.get("amount").toString());
		entryTranDto.setMemberID(memberId);
		entryTranDto.setStatus(GmopgConst.Fields.UNPROCESSED);

		entryTranDto.setAccessID(entryRes.get("accessID").toString());
		entryTranDto.setAccessPass(entryRes.get("accessPass").toString());

		entryTranDao.insert(entryTranDto);

		return orderId;
	}

	/**
	 * 決済可能取引データ検索
	 * 
	 * @return List<GmopgDto> 画面パラメータリスト
	 */
	public List<GmopgDto> selectExecTran() {

		// 未決済の取引登録情報をDBから取得
		List<EntryTranDto> entryTranDtoList = entryTranDao.selectStatus(GmopgConst.Fields.UNPROCESSED);

		// 取得した未決済の取引データを画面パラメータにコピー
		List<GmopgDto> gmopgDtoList = copyList(entryTranDtoList);

		return gmopgDtoList;
	}

	/**
	 * 取引データコピー
	 * 
	 * @param entryTranDtoList 取引データリスト
	 * @return List<GmopgDto> 画面パラメータリスト
	 */
	public List<GmopgDto> copyList(List<EntryTranDto> entryTranDtoList) {
		return entryTranDtoList.stream()
				.map(source -> {
					GmopgDto target = new GmopgDto();
					// 同名のプロパティを自動コピー
					BeanUtils.copyProperties(source, target);
					return target;
				})
				.collect(Collectors.toList());
	}

	/**
	 * 決済実行
	 * 
	 * @param orderId オーダーID
	 * @return GmopgDto 画面パラメータ
	 */
	public GmopgDto executeExecTran(String orderId) {

		// 取引データをDBから取得
		EntryTranDto entryTranDto = entryTranDao.selectOrderID(orderId);

		// 取引データがDB未登録
		if (entryTranDto.getOrderID().isEmpty())
			throw new RuntimeException("ExecTran.json   取引データなし");

		// [ExecTran] 決済実行（登録済み会員のカード使用）
		Map<String, Object> execReq = new HashMap<>();
		execReq.put("accessID", entryTranDto.getAccessID());
		execReq.put("accessPass", entryTranDto.getAccessPass());
		execReq.put("orderID", entryTranDto.getOrderID());
		execReq.put("method", "1"); // 一括
		execReq.put("siteID", gmopgConfig.getSiteId());
		execReq.put("sitePass", gmopgConfig.getSitePass());
		execReq.put("memberID", entryTranDto.getMemberID());
		execReq.put("seqMode", "0"); // 論理モード
		execReq.put("cardSeq", "0"); // カード登録連番

		Map execRes = callApi("/ExecTran.json", execReq);

		if (execRes.containsKey(GmopgConst.Fields.Error))
			throw new gmoEroorException(execRes);

		// DBの取引データを更新し、更新したデータを画面パラメータにコピー
		GmopgDto gmopgDto = new GmopgDto();
		if (entryTranDao.updateStatus(entryTranDto.getOrderID(), GmopgConst.Fields.AUTH)) {
			BeanUtils.copyProperties(entryTranDto, gmopgDto);
			gmopgDto.setApiResponse(execRes.toString());

		}

		return gmopgDto;
	}

	/**
	 * 仮売上取引データ検索
	 * 
	 * @return List<GmopgDto> 画面パラメータリスト
	 */
	public List<GmopgDto> selectAlterTran() {

		// 未決済の取引登録情報をDBから取得
		List<EntryTranDto> entryTranDtoList = entryTranDao.selectStatus(GmopgConst.Fields.AUTH);

		// 取得した仮売上の取引データを画面パラメータにコピー
		List<GmopgDto> gmopgDtoList = copyList(entryTranDtoList);

		return gmopgDtoList;
	}

	/**
	 * 実売上実行
	 * 
	 * @param orderId オーダーID
	 * @return GmopgDto 画面パラメータ
	 */
	public GmopgDto executeAlterTran(String orderId) {

		// 取引データをDBから取得
		EntryTranDto entryTranDto = entryTranDao.selectOrderID(orderId);

		// 取引データがDB未登録
		if (entryTranDto.getOrderID().isEmpty())
			throw new RuntimeException("AlterTran.json   決済実行情報なし");

		// [AlterTran] 実売上
		Map<String, Object> execReq = new HashMap<>();
		execReq.put("shopID", gmopgConfig.getShopId());
		execReq.put("shopPass", gmopgConfig.getShopPass());
		execReq.put("accessID", entryTranDto.getAccessID());
		execReq.put("accessPass", entryTranDto.getAccessPass());
		execReq.put("jobCd", GmopgConst.Fields.SALES);
		execReq.put("amount", entryTranDto.getAmount());

		Map execRes = callApi("/AlterTran.json", execReq);

		if (execRes.containsKey(GmopgConst.Fields.Error))
			throw new gmoEroorException(execRes);

		// DBの取引データを更新し、更新したデータを画面パラメータにコピー
		GmopgDto gmopgDto = new GmopgDto();
		if (entryTranDao.updateStatus(orderId, GmopgConst.Fields.SALES)) {
			BeanUtils.copyProperties(entryTranDto, gmopgDto);
			gmopgDto.setApiResponse(execRes.toString());

		}

		return gmopgDto;
	}

	/**
	 * 取消可能取引データ検索
	 * 
	 * @return List<GmopgDto> 画面パラメータリスト
	 */
	public List<GmopgDto> selectCancel() {

		List<EntryTranDto> authList = entryTranDao.selectStatus(GmopgConst.Fields.AUTH);
		List<EntryTranDto> salesList = entryTranDao.selectStatus(GmopgConst.Fields.SALES);

		// 2つのリストをマージしてソートした新しいリストを作成
		List<EntryTranDto> entryTranDtoList = Stream.concat(authList.stream(), salesList.stream())
				.sorted(Comparator.comparing(EntryTranDto::getOrderID))
				.collect(Collectors.toList());

		// 取得した仮売上の取引データを画面パラメータにコピー
		List<GmopgDto> gmopgDtoList = copyList(entryTranDtoList);

		return gmopgDtoList;
	}

	/**
	 * 取消実行
	 * 
	 * @param orderId オーダーID
	 * @return GmopgDto 画面パラメータ
	 */
	public GmopgDto executeCancel(String orderId) {

		// 取引データをDBから取得
		EntryTranDto entryTranDto = entryTranDao.selectOrderID(orderId);

		// 取引データがDB未登録
		if (entryTranDto.getOrderID().isEmpty())
			throw new RuntimeException("AlterTran.json   取消可能情報なし");

		Map<String, Object> execReq = new HashMap<>();
		execReq.put("shopID", gmopgConfig.getShopId());
		execReq.put("shopPass", gmopgConfig.getShopPass());
		execReq.put("accessID", entryTranDto.getAccessID());
		execReq.put("accessPass", entryTranDto.getAccessPass());
		execReq.put("jobCd", GmopgConst.Fields.CANCEL);

		Map execRes = callApi("/AlterTran.json", execReq);

		if (execRes.containsKey(GmopgConst.Fields.Error))
			throw new gmoEroorException(execRes);
		// 取り消した取引データの取引状態でDBの取引データを更新し、
		// 更新したデータを画面パラメータにコピー
		GmopgDto gmopgDto = executeSearchTrade(orderId);
		if (entryTranDao.updateStatus(orderId, gmopgDto.getStatus())) {
			gmopgDto.setApiResponse(execRes.toString());
		}

		return gmopgDto;
	}

	/**
	 * 取消データ照会
	 * 
	 * @param orderId オーダーID
	 * @return GmopgDto 画面パラメータ
	 */
	public GmopgDto executeSearchTrade(String orderId) {

		Map<String, Object> execReq = new HashMap<>();
		execReq.put("shopID", gmopgConfig.getShopId());
		execReq.put("shopPass", gmopgConfig.getShopPass());
		execReq.put("orderID", orderId);

		Map execRes = callApi("/SearchTrade.json", execReq);

		if (execRes.containsKey(GmopgConst.Fields.Error))
			throw new gmoEroorException(execRes);

		GmopgDto gmopgDto = new GmopgDto();

		gmopgDto.setOrderID(execRes.get("orderID").toString());
		gmopgDto.setMemberID(execRes.get("memberID").toString());
		gmopgDto.setAmount(execRes.get("amount").toString());
		gmopgDto.setStatus(execRes.get("status").toString());

		DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		LocalDateTime dateTime = LocalDateTime.parse(execRes.get("processDate").toString(), inputFormatter);
		DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		String output = dateTime.format(outputFormatter);
		gmopgDto.setProcessDate(output);

		gmopgDto.setApiResponse(execRes.toString());

		return gmopgDto;
	}

	/**
	 * 会員データ照会
	 * 
	 * @param gmopgDto 元になる画面パラメータ
	 * @return GmopgDto 画面パラメータ
	 */
	public GmopgDto executeSearchMember(GmopgDto gmopgDto) {

		Map<String, Object> execReq = new HashMap<>();
		execReq.put("siteID", gmopgConfig.getSiteId());
		execReq.put("sitePass", gmopgConfig.getSitePass());
		execReq.put("memberID", gmopgDto.getMemberID());

		Map execRes = callApi("/SearchMember.json", execReq);

		if (execRes.containsKey(GmopgConst.Fields.Error))
			throw new gmoEroorException(execRes);

		gmopgDto.setMemberName(execRes.get("memberName").toString());

		return gmopgDto;
	}

	/**
	 * リクエスト実行
	 * 
	 * @param orderId オーダーID
	 * @param requestBody リクエスト内容
	 * @return Map リクエスト結果
	 */
	private Map callApi(String endpoint, Map<String, Object> requestBody) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAcceptCharset(Arrays.asList(StandardCharsets.UTF_8));
		HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
		try {
			return restTemplate.postForObject(gmopgConfig.getApiUrl() + endpoint, entity, Map.class);
		} catch (Exception e) {
			Map<String, String> err = new HashMap<>();
			err.put(GmopgConst.Fields.Error, e.getMessage());
			return err;
		}
	}
}