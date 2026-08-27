package com.example.demo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.exception.gmoEroorException;
import com.example.demo.model.GmopgDto;
import com.example.demo.service.GmopgJsonWebService;

@Controller
public class GmopgJsonWebController {

	private final GmopgJsonWebService gmoApiService;

	public GmopgJsonWebController(GmopgJsonWebService gmoApiService) {
		this.gmoApiService = gmoApiService;
	}

	/**
	 * メニュー画面
	 * 
	 * @return String 遷移先画面
	 */
	@GetMapping("/")
	public String index() {
		return "index";
	}

	/**
	 * 取引データ入力
	 * 
	 * @param model モデル
	 * @return String 遷移先画面
	 */
	@GetMapping("/entryTran")
	public String entryTranForm(Model model) {
		GmopgDto dto = new GmopgDto();
		model.addAttribute("dto", dto);
		return "entryTran";
	}

	/**
	 * 取引登録実行
	 * 
	 * @param dto 画面パラメータ
	 * @param model モデル
	 * @return String 遷移先画面
	 */
	@PostMapping("/entryTran")
	public String entryTranSubmit(@ModelAttribute GmopgDto dto, Model model) {
		try {
			String orderId = gmoApiService.executeEntryTran(dto.getMemberID(), dto.getAmount());
			GmopgDto gmopgDto = gmoApiService.executeSearchMember(dto);
			gmopgDto.setOrderID(orderId);
			model.addAttribute("dto", gmopgDto);
			return "entryTranResult";
		} catch (gmoEroorException e) {
			model.addAttribute("err", e.getGmoErrorList());
			return "errorInfo";
		} catch (Exception e) {
			return "error";
		}
	}

	/**
	 * 決済情報入力
	 * 
	 * @param model モデル
	 * @return String 遷移先画面
	 */
	@GetMapping("/execTran")
	public String execTranForm(Model model) {
		List<GmopgDto> dto = gmoApiService.selectExecTran();
		model.addAttribute("dto", dto);
		return "execTran";
	}

	/**
	 * 決済実行
	 * 
	 * @param orderId 決済実行オーダーID
	 * @param dto 取引データ画面パラメータ
	 * @param model モデル
	 * @return String 遷移先画面
	 */
	@PostMapping("/execTran")
	public String execTranSubmit(@RequestParam("selectOrderID") String orderId, @ModelAttribute GmopgDto dto,
			Model model) {
		try {
			GmopgDto gmopgDto = gmoApiService.executeExecTran(orderId);
			gmopgDto = gmoApiService.executeSearchMember(gmopgDto);
			model.addAttribute("dto", gmopgDto);
			return "execTranResult";
		} catch (gmoEroorException e) {
			model.addAttribute("err", e.getGmoErrorList());
			return "errorInfo";
		} catch (Exception e) {
			return "error";
		}

	}

	/**
	 * 実売上情報入力
	 * 
	 * @param model モデル
	 * @return String 遷移先画面
	 */
	@GetMapping("/alterTran")
	public String alterTranForm(Model model) {
		List<GmopgDto> dto = gmoApiService.selectAlterTran();
		model.addAttribute("dto", dto);
		return "alterTran";
	}

	/**
	 * 実売上実行
	 * 
	 * @param orderId 決済実行オーダーID
	 * @param dto 取引データ画面パラメータ
	 * @param model モデル
	 * @return String 遷移先画面
	 */
	@PostMapping("/alterTran")
	public String alterTranSubmit(@RequestParam("selectOrderID") String orderId, @ModelAttribute GmopgDto dto,
			Model model) {
		try {
			GmopgDto gmopgDto = gmoApiService.executeAlterTran(orderId);
			gmopgDto = gmoApiService.executeSearchMember(gmopgDto);
			model.addAttribute("dto", gmopgDto);
			return "alterTranResult";
		} catch (gmoEroorException e) {
			model.addAttribute("err", e.getGmoErrorList());
			return "errorInfo";
		} catch (Exception e) {
			return "error";
		}
	}

	/**
	 * 取引取消情報入力
	 * 
	 * @param model モデル
	 * @return String 遷移先画面
	 */
	@GetMapping("/cancel")
	public String cancelForm(Model model) {
		List<GmopgDto> dto = gmoApiService.selectCancel();
		model.addAttribute("dto", dto);
		return "cancel";
	}

	/**
	 * 取引取消実行
	 * 
	 * @param orderId 決済実行オーダーID
	 * @param dto 取引データ画面パラメータ
	 * @param model モデル
	 * @return String 遷移先画面
	 */
	@PostMapping("/cancel")
	public String cancelSubmit(@RequestParam("selectOrderID") String orderId, @ModelAttribute GmopgDto dto,
			Model model) {
		try {
			GmopgDto gmopgDto = gmoApiService.executeCancel(orderId);
			gmopgDto = gmoApiService.executeSearchMember(gmopgDto);
			model.addAttribute("dto", gmopgDto);
			return "cancelResult";
		} catch (gmoEroorException e) {
			model.addAttribute("err", e.getGmoErrorList());
			return "errorInfo";
		} catch (Exception e) {
			return "error";
		}
	}

	/**
	 * 取引参照情報入力
	 * 
	 * @param model モデル
	 * @return String 遷移先画面
	 */
	@GetMapping("/searchTrade")
	public String searchTradeForm(Model model) {
		model.addAttribute("dto", new GmopgDto());
		return "searchTrade";
	}

	/**
	 * 取引状態取得実行
	 * 
	 * @param dto 取引データ画面パラメータ
	 * @param model モデル
	 * @return String 遷移先画面
	 */
	@PostMapping("/searchTrade")
	public String searchTradeSubmit(@ModelAttribute GmopgDto dto, Model model) {
		try {
			GmopgDto gmopgDto = gmoApiService.executeSearchTrade(dto.getOrderID());
			gmopgDto = gmoApiService.executeSearchMember(gmopgDto);
			model.addAttribute("dto", gmopgDto);
			return "searchTradeResult";
		} catch (gmoEroorException e) {
			model.addAttribute("err", e.getGmoErrorList());
			return "errorInfo";
		} catch (Exception e) {
			return "error";
		}
	}

}