package cn.e3mall.cart.service;

import java.util.List;

import cn.e3mall.pojo.E3Result;
import cn.e3mall.pojo.TbItem;

public interface CartService {
	E3Result addCart(Long id, List<TbItem> list);
	List<TbItem> findCart(Long id);
	E3Result updateById(String id, String cid, int num);
    E3Result delById(String id, String cid);
    E3Result delAll(String uid);
}
