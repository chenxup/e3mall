package cn.e3mall.search.controller;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.e3mall.pojo.SearchResult;
import cn.e3mall.search.service.SearchItemService;

@Controller
public class SearchItemController {

	@Autowired
	private SearchItemService searchItemService;
	@Value("${ROWS}")
	private int ROWS;

	@RequestMapping("/search.html")
	public String SearchItem(@RequestParam(defaultValue = "1") Integer page, String keyword, Model model) throws Exception {
		//乱码
		try {
			keyword = new String(keyword.getBytes("ISO-8859-1"),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		SearchResult result = searchItemService.queryItems(page, ROWS, keyword);
		model.addAttribute("query", keyword);
		model.addAttribute("totalPages", result.getTotalPages());
		model.addAttribute("recourdCount", result.getRecourdCount());
		model.addAttribute("page", result.getPage());
		model.addAttribute("itemList", result.getItems());
		return "search";
	}

}
