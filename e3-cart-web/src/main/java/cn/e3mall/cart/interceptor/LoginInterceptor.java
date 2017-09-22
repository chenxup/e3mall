package cn.e3mall.cart.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import cn.e3mall.pojo.E3Result;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.UserService;
import cn.e3mall.utils.CookieUtils;

public class LoginInterceptor implements HandlerInterceptor{

	@Autowired
	private UserService userService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object obj) throws Exception {
		//获得token中的信息
		String token = CookieUtils.getCookieValue(request, "token");
		if (StringUtils.isBlank(token)) {
			return true;
		}
		//调用sso服务,判断是否登录
		E3Result userByToken = userService.getUserByToken(token);
		if (userByToken.getStatus() != 200) {
			return true;
		}
		TbUser user = (TbUser) userByToken.getData();
		//将用户信息保存在request中
		request.setAttribute("logUser", user);
		return true;
	}
	
	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
		// TODO Auto-generated method stub
		
	}


}
