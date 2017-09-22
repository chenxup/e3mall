package cn.e3mall.order.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.order.service.OrderService;
import cn.e3mall.order.vo.OrderVo;
import cn.e3mall.pojo.E3Result;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbUser;

@Controller
public class OrderController {

	@Autowired
	private CartService cartService;
	@Autowired
	private OrderService orderService;
	
	/**
	 * 跳转订单页面
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("order/order-cart")
	public String showOrderCart(HttpServletRequest request,Model model) {
		//从requet中取出用户
		TbUser user = (TbUser) request.getAttribute("logUser");
		
		//从redis中获取购物车数据
		List<TbItem> cartList = cartService.findCart(user.getId());
		model.addAttribute("cartList", cartList);
		return "order-cart";
	}
	
	@RequestMapping("/order/create")
	public String toSuccess(OrderVo orderVo, HttpServletRequest request, Model model){
		//封装参数
		TbUser user = (TbUser) request.getAttribute("logUser");
		orderVo.setUserId(user.getId());
		
		//调用业务生辰订单
		E3Result result = orderService.create(orderVo);
		//将购物车清除
		cartService.delAll(user.getId() + "");
		
		//将参数返回页面
		model.addAttribute("orderId", result.getData());
		model.addAttribute("payment", orderVo.getPayment());
		return "success";
	}
	
	
}
