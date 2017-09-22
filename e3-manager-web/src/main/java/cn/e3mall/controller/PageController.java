package cn.e3mall.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class PageController {

	@RequestMapping("/")
	public String showIndex() {
		return "index";
	}
	
	@RequestMapping(value="/{page}", method=RequestMethod.GET)
	public String showPage(@PathVariable String page) {
		return page;
	}
}
