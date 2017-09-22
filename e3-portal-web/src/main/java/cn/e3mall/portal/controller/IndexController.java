package cn.e3mall.portal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.e3mall.content.service.ContentService;
import cn.e3mall.pojo.TbContent;

@Controller
public class IndexController {
	
	@Autowired
	private ContentService contentService;
	
	@Value("${LUNBO_ID}")
	private Long LUNBO_ID;
	
	/**
	 * 访问路径就写到端口号就能访问到
	 * 原因：由于在tomcat访问路径设置为/所以不用写项目名，
	 * 那么，tomcat会自动赵index.html的页面，但是我们在springmvc中配置了*.html，所以请求会到这里来。
	 * 然后就能跳转到index.jsp了。
	 * @return
	 */
	@RequestMapping("/index.html")
	public String repIndex(Model model) {
		//添加大广告商品信息
		List<TbContent> adlist = contentService.findContentById(LUNBO_ID);
		model.addAttribute("ad1List", adlist);
		return "index";
	}
}
