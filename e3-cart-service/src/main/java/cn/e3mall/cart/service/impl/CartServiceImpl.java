package cn.e3mall.cart.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.jedis.JedisClient;
import cn.e3mall.pojo.E3Result;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.utils.JsonUtils;

@Service
public class CartServiceImpl implements CartService{

	@Autowired
	private JedisClient jedisClient;
	@Value("${REIDS_CART_KEY}")
	private String REIDS_CART_KEY;
	
	/**
	 * 将cookie中的商品信息与redis同步
	 */
	public E3Result addCart(Long id, List<TbItem> list) {
		//遍历
		for (TbItem tbItem : list) {
			addCartByCid(id, tbItem);
		}
		return E3Result.ok();
	}
	
	/**
	 * 将单个商品放入redis中
	 * @param id
	 * @param item
	 * @return
	 */
	public E3Result addCartByCid(Long id, TbItem item){
		//先判断jedis中是否有数据
		Boolean flag = jedisClient.exists(REIDS_CART_KEY + ":" + id);
		String hget = jedisClient.hget(REIDS_CART_KEY + ":" + id, String.valueOf(item.getId()));
		if (flag && hget != null) {
			//有数据,将原来的取出，再放进去
			TbItem ritem = JsonUtils.jsonToPojo(hget, TbItem.class);
			ritem.setNum(ritem.getNum() + item.getNum());
			jedisClient.hset(REIDS_CART_KEY + ":" + id, String.valueOf(item.getId()), JsonUtils.objectToJson(ritem));
		} else {
			//没有数据，直接放入
			jedisClient.hset(REIDS_CART_KEY + ":" + id, String.valueOf(item.getId()), JsonUtils.objectToJson(item));
		}
		
		return E3Result.ok();
	}
	
	public List<TbItem> findCart(Long id) {
		List<String> list = jedisClient.hval(REIDS_CART_KEY + ":" + id);
		//将里面你的String转成item
		List<TbItem> itemList = new ArrayList<>();
		for (String json : list) {
			TbItem item = JsonUtils.jsonToPojo(json, TbItem.class);
			itemList.add(item);
		}
		return itemList;
	}

	/**
	 * 更新
	 */
	public E3Result updateById(String id, String cid, int num) {
		String jitem = jedisClient.hget(REIDS_CART_KEY + ":" + id, cid);
		TbItem item = JsonUtils.jsonToPojo(jitem, TbItem.class);
		item.setNum(num);
		jedisClient.hset(REIDS_CART_KEY + ":" + id, cid, JsonUtils.objectToJson(item));
		return E3Result.ok();
	}

	/**
	 * 删除
	 */
	public E3Result delById(String id, String cid) {
		jedisClient.hdel(REIDS_CART_KEY + ":" + id, cid);
		return E3Result.ok();
	}

	/**
	 * 删除所有购物车
	 */
	public E3Result delAll(String uid) {
		jedisClient.del(REIDS_CART_KEY + ":" + uid);
		return E3Result.ok();
	}
	
	
	

}
