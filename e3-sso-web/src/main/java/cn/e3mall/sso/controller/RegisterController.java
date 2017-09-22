package cn.e3mall.sso.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.pojo.E3Result;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.UserService;

/**
 * 注册
 * @author HOC
 *
 */

@Controller
public class RegisterController {

	@Autowired
	private UserService userService;
	
	/**
	 * 跳转注册页面
	 * @return
	 */
	@RequestMapping("/page/register")
	public String showPage() {
		return "register";
	}
	
	/**
	 * Ajax验证信息
	 * @param info
	 * @param type
	 * @return
	 */
	@RequestMapping("/user/check/{info}/{type}")
	@ResponseBody
	public E3Result checkData(@PathVariable String info, @PathVariable int type) {
		return userService.checkData(info, type);
	}
	
	/**
	 * 注册
	 * @param user
	 * @return
	 */
	@RequestMapping("/user/register")
	@ResponseBody
	public E3Result register(TbUser user) {
		return userService.register(user);
	}
	
}
