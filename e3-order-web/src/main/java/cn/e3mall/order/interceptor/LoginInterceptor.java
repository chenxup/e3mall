package cn.e3mall.order.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.pojo.E3Result;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.UserService;
import cn.e3mall.utils.CookieUtils;
import cn.e3mall.utils.JsonUtils;

public class LoginInterceptor implements HandlerInterceptor {
	
	@Autowired
	private UserService userService;
	@Autowired
	private CartService cartService;
	
	@Value("${TOKEN}")
	private String TOKEN;
	@Value("${CART_COOKIE}")
	private String CART_COOKIE;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object obj) throws Exception {
		//判断用户是否登录
		String token = CookieUtils.getCookieValue(request, TOKEN);
		if (token == null) {
			//获得这次请求的页面
			StringBuffer url = request.getRequestURL();
			//跳转登录页面,并将要请求的页面传过去
			response.sendRedirect("Http://localhost:8088/page/login.html?redirectURL=" + url);
			return false;
		}
		//判断用户是否过期
		E3Result result = userService.getUserByToken(token);
		if (result.getStatus() != 200) {
			//过期
			//获得这次请求的页面
			StringBuffer url = request.getRequestURL();
			//跳转登录页面,并将要请求的页面传过去
			response.sendRedirect("Http://localhost:8088/page/login.html?redirectURL=" + url);
			return false;
		}
		
		//将用户放到request中
		TbUser user = (TbUser) result.getData();
		request.setAttribute("logUser", user);
		//并且将购物车中的数据进行同步
		String cart = CookieUtils.getCookieValue(request, CART_COOKIE, true);
		if (StringUtils.isNotBlank(cart)) {
			cartService.addCart(user.getId(), JsonUtils.jsonToList(cart, TbItem.class));
		}
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
