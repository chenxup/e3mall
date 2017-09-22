package cn.e3mall.cart.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.service.ItemService;
import cn.e3mall.utils.CookieUtils;
import cn.e3mall.utils.JsonUtils;

/**
 * 购物车
 * @author HOC
 *
 */
@Controller
public class CartController {
	
	@Autowired
	private ItemService itemService;
	@Autowired
	private CartService cartService;
	
	@Value("${CART_COOKIE}")
	private String CART_COOKIE;
	@Value("${COOKIE_MAXAGE}")
	private int COOKIE_MAXAGE;
	

	/**
	 * 添加购物车
	 * @param request
	 * @param response
	 * @param id
	 * @param num
	 * @return
	 */
	@RequestMapping("/cart/add/{id}")
	public String addCart(HttpServletRequest request, HttpServletResponse response, @PathVariable long id, @RequestParam(defaultValue="1") int num) {
		//获得cookie中的商品信息
		List<TbItem> cartList = getCartList(request);
		boolean flag = true;
		//查看购物车中是否有商品了
		for (TbItem tbItem : cartList) {
			if (id == tbItem.getId().longValue()) {
				flag = false;
				//将原来的商品数量改变
				tbItem.setNum(tbItem.getNum() + num);
				break;
			}
		}
		//没有商品，将商品信息查出
		if (flag) {
			TbItem item = itemService.getItemById(id);
			//设置商品数量
			item.setNum(num);
			//取第一张图片
			item.setImage(item.getImage().split(",")[0]);
			cartList.add(item);
		}
		
		//查看用户是否登录
		TbUser user = (TbUser) request.getAttribute("logUser");
		if (user != null) {
			//将cookie中的cart与redis同步
			cartService.addCart(user.getId(), cartList);
			//将cookie删除
			CookieUtils.deleteCookie(request, response, "cart");
			return "cartSuccess";
		}
		
		//写回cookie
		CookieUtils.setCookie(request, response, CART_COOKIE, JsonUtils.objectToJson(cartList), COOKIE_MAXAGE, true);
		return "cartSuccess";
	}
	
	/**
	 * 查找所有购物车，跳转到购物车页面
	 * @param request
	 * @param response 
	 * @return
	 */
	@RequestMapping("/cart/cart")
	public String toCart(HttpServletRequest request, Model model, HttpServletResponse response) {
		List<TbItem> cartList = getCartList(request);
		//查看用户是否登录
		TbUser user = (TbUser) request.getAttribute("logUser");
		if (user != null) {
			//将cookie中的cart与redis同步
			cartService.addCart(user.getId(), cartList);
			//将cookie删除
			CookieUtils.deleteCookie(request, response, "cart");
			//然后从redis中取数据
			cartList = cartService.findCart(user.getId());
		} 
		model.addAttribute("cartList", cartList);
		return "cart";
	}
	
	/**
	 * 更新购物车
	 * @param id
	 * @param num
	 * @param request
	 * @param response
	 */
	@RequestMapping("/cart/update/num/{id}/{num}")
	@ResponseBody
	public void updateCart(@PathVariable long id, @PathVariable int num, HttpServletRequest request, HttpServletResponse response) {
		//查看用户是否登录
		TbUser user = (TbUser) request.getAttribute("logUser");
		if (user != null) {
			cartService.updateById(String.valueOf(user.getId()), String.valueOf(id), num);
			return;
		} 
		List<TbItem> cartList = getCartList(request);
		for (TbItem tbItem : cartList) {
			if (tbItem.getId().longValue() == id) {
				tbItem.setNum(num);
				break;
			}
		}
		//写回cookie
		CookieUtils.setCookie(request, response, CART_COOKIE, JsonUtils.objectToJson(cartList), COOKIE_MAXAGE, true);
		return;
	}
	
	@RequestMapping("/cart/delete/{id}")
	public String delCart(@PathVariable long id, HttpServletRequest request, HttpServletResponse response) {
		//查看用户是否登录
		TbUser user = (TbUser) request.getAttribute("logUser");
		if (user != null) {
			cartService.delById(String.valueOf(user.getId()), String.valueOf(id));
			return "redirect:/cart/cart.html";
		} 
		
		List<TbItem> cartList = getCartList(request);
		for (TbItem tbItem : cartList) {
			if (tbItem.getId().longValue() == id) {
				cartList.remove(tbItem);
				break;
			}
		}
		CookieUtils.setCookie(request, response, CART_COOKIE, JsonUtils.objectToJson(cartList), COOKIE_MAXAGE, true);
		return "redirect:/cart/cart.html";
	}
	
	
	
	/**
	 * 从cookie中获得购物车信息
	 * @param request
	 * @return
	 */
	public List<TbItem> getCartList(HttpServletRequest request) {
		String strCart = CookieUtils.getCookieValue(request, CART_COOKIE, true);
		if (StringUtils.isBlank(strCart)) {
			return new ArrayList<>();
		}
		return JsonUtils.jsonToList(strCart, TbItem.class);
	}
	
	
	
	
	
}
