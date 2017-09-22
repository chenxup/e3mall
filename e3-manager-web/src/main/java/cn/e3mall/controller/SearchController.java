package cn.e3mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.pojo.E3Result;
import cn.e3mall.search.service.SearchItemService;

@Controller
public class SearchController {

	@Autowired
	private SearchItemService SearchItemService;
	
	
	@RequestMapping("/index/item/import")
	@ResponseBody
	public E3Result findAllSearchItem() {
		return SearchItemService.searchItemImport();
	}
	
}
