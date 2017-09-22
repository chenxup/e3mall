package cn.e3mall.order.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.e3mall.jedis.JedisClient;
import cn.e3mall.mapper.TbOrderItemMapper;
import cn.e3mall.mapper.TbOrderMapper;
import cn.e3mall.mapper.TbOrderShippingMapper;
import cn.e3mall.order.service.OrderService;
import cn.e3mall.order.vo.OrderVo;
import cn.e3mall.pojo.E3Result;
import cn.e3mall.pojo.TbOrderItem;
import cn.e3mall.pojo.TbOrderShipping;

@Service
public class OrderServiceImpl implements OrderService{

	@Autowired
	private TbOrderMapper orderMapper;
	@Autowired
	private TbOrderItemMapper orderItemMapper;
	@Autowired
	private TbOrderShippingMapper orderShippingMapper;
	
	@Autowired
	private JedisClient jedisClient;
	@Value("${REDIS_ORDER_PRE}")
	private String REDIS_ORDER_PRE;
	@Value("${REDIS_DEFAULT_PRE}")
	private String REDIS_DEFAULT_PRE;
	@Value("${REDIS_ORDERITEM_PRE}")
	private String REDIS_ORDERITEM_PRE;
	
	/**
	 * 生成订单
	 */
	public E3Result create(OrderVo orderVo) {
		//从redis中获取订单id
		Boolean falg = jedisClient.exists(REDIS_ORDER_PRE);
		if (!falg) {
			//第一次设置jediclient默认值
			jedisClient.set(REDIS_ORDER_PRE, REDIS_DEFAULT_PRE);
		}
		Long orderId = jedisClient.incr(REDIS_ORDER_PRE);
		orderVo.setOrderId(String.valueOf(orderId));
		//补全属性
		orderVo.setStatus(1);
		orderVo.setCreateTime(new Date());
		orderVo.setUpdateTime(new Date());
		//保存订单
		orderMapper.insert(orderVo);
		
		//订单详情
		//id，可以随意设置，只要不重复,不能超过数据库设置的值，这里直接从redis中取
		List<TbOrderItem> orderItems = orderVo.getOrderItems();
		for (TbOrderItem orderItem : orderItems){
			Long orderItemId = jedisClient.incr("REDIS_ORDERITEM_PRE");
			orderItem.setId(String.valueOf(orderItemId));
			//补全属性
			orderItem.setOrderId(orderId + "");
			orderItemMapper.insert(orderItem);
		}
		
		//订单物流
		//id与订单一致
		TbOrderShipping orderShipping = orderVo.getOrderShipping();
		orderShipping.setOrderId(String.valueOf(orderId));
		orderShipping.setCreated(new Date());
		orderShipping.setUpdated(new Date());
		orderShippingMapper.insert(orderShipping);
		
		//将商品id返回
		return E3Result.ok(orderId);
	}

}
