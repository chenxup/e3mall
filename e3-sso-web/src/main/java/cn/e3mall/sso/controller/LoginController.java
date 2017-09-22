package cn.e3mall.sso.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.pojo.E3Result;
import cn.e3mall.sso.service.UserService;
import cn.e3mall.utils.CookieUtils;

/**
 * 登录
 * @author HOC
 *
 */
@Controller
public class LoginController {
	
	@Autowired
	private UserService userService;
	@Value("${TOKEN}")
	private String TOKEN;
	
	/**
	 * 挑战登录页面
	 * @return
	 */
	@RequestMapping("page/login")
	public String showLogin(String redirectURL, Model model) {
		model.addAttribute("redirect", redirectURL);
		return "login";
	}
	
	/**
	 * 登录
	 * @param username
	 * @param password
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/user/login")
	@ResponseBody
	public E3Result	login(String username, String password, HttpServletRequest request, HttpServletResponse response) {
		E3Result result = userService.login(username, password);
		if (result.getStatus() != 200) {
			return result;
		}
		
		//写回cookie
		String sessionId = (String) result.getData();
		CookieUtils.setCookie(request, response, TOKEN, sessionId);
		return result;
	}
}
