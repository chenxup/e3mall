package cn.e3mall.order.service;

import cn.e3mall.order.vo.OrderVo;
import cn.e3mall.pojo.E3Result;

public interface OrderService {
	E3Result create(OrderVo orderVo);
}
