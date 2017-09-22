package cn.e3mall.service.impl;

import java.util.Date;
import java.util.List;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.alibaba.druid.support.json.JSONUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.e3mall.jedis.JedisClient;
import cn.e3mall.mapper.TbItemDescMapper;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.E3Result;
import cn.e3mall.pojo.EasyUiDataGuridPage;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.pojo.TbItemDescExample;
import cn.e3mall.pojo.TbItemExample;
import cn.e3mall.pojo.TbItemExample.Criteria;
import cn.e3mall.service.ItemService;
import cn.e3mall.utils.IDUtils;
import cn.e3mall.utils.JsonUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisClusterCommand;

/**
 * 商品service
 * @author HOC
 *
 */
@Service
public class ItemServiceImpl implements ItemService {
	
	@Autowired
	private TbItemMapper itemMapper;
	@Autowired
	private TbItemDescMapper itemDescMapper;
	
	//redis
	@Autowired
	private JedisClient JedisClient;
	@Value("${ITEM_INFO_PRE}")
	private String ITEM_INFO_PRE;
	@Value("${ITEM_INFO_EXPIRE}")
	private int ITEM_INFO_EXPIRE;
	
	//发布topic消息
	@Autowired
	private JmsTemplate jmsTemplate;
	//增加商品
	@Autowired
	@Qualifier("topicDestination-add")
	private Destination destinationAdd;
	//删除商品
	@Autowired
	@Qualifier("topicDestination-delete")
	private Destination destinationDel;
	
	/**
	 * 根据id查询
	 */
	public TbItem getItemById(Long id) {
		
		try {
			//从缓存中取数据
			String value = JedisClient.get("ITEM_INFO_PRE:" + String.valueOf(id) + "BASE");
			if (StringUtils.isNotBlank(value)) {
				return JsonUtils.jsonToPojo(value, TbItem.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		TbItem item = itemMapper.selectByPrimaryKey(id);
		//放入缓存
		try {
			JedisClient.set("ITEM_INFO_PRE:" + String.valueOf(id) + "BASE", JsonUtils.objectToJson(item));
			//设计过期时间
			JedisClient.expire("ITEM_INFO_PRE:" + String.valueOf(id) + "BASE", ITEM_INFO_EXPIRE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return item;
	}
	
	/**
	 * 分页查询
	 */
	public EasyUiDataGuridPage getItemList(int page, int rows){
		PageHelper pageHelper = new PageHelper();
		pageHelper.startPage(page, rows);
		
		TbItemExample example = new TbItemExample();
		List<TbItem> list = itemMapper.selectByExample(example);
		
		//封装参数
		EasyUiDataGuridPage result = new EasyUiDataGuridPage();
		result.setRows(list);
		result.setTotal(new PageInfo<>(list).getTotal());
		
		return result;
	}

	/**
	 * 保存商品
	 */
	public E3Result saveItem(TbItem item, String desc) {
		//生成id，有当前时间毫秒值加上两位随机数
		final long id = IDUtils.genItemId();
		item.setId(id);
		//status: 1-正常， 2-下架，  3-删除
		item.setStatus((byte)1);
		item.setCreated(new Date());
		item.setUpdated(new Date());
	
		//创建商品描述
		TbItemDesc itemDesc = new TbItemDesc();
		//id和商品一样
		itemDesc.setItemId(id);
		itemDesc.setItemDesc(desc);
		itemDesc.setCreated(new Date());
		itemDesc.setUpdated(new Date());
		
		//保存
		itemMapper.insert(item);
		itemDescMapper.insert(itemDesc);
		
		//同步索引库数据
		jmsTemplate.send(destinationAdd, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage message = session.createTextMessage(String.valueOf(id));
				return message;
			}
		});
	
		return E3Result.ok();
	}

	/**
	 * 商品逻辑删除
	 */
	public E3Result DeleteItem(final String ids) {
		String[] strId = ids.split(",");
		for (String id : strId) {
			TbItemExample example = new TbItemExample();
			Criteria criteria = example.createCriteria();
			criteria.andIdEqualTo(Long.valueOf(id));
			//删除商品
			TbItem item = new TbItem();
			item.setStatus((byte)3);
			itemMapper.updateByExampleSelective(item, example);
		}
		
		//同步索引库数据
		jmsTemplate.send(destinationDel, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage message = session.createTextMessage(ids);
				return message;
			}
		});
		
		return E3Result.ok();
	}

	/**
	 * 获得商品描述
	 */
	public E3Result getItemDesc(Long id) {
		try {
			//从缓存中取数据
			String value = JedisClient.get("ITEM_INFO_PRE:" + String.valueOf(id) + "DESC");
			if (StringUtils.isNotBlank(value)) {
				TbItemDesc jsonToPojo = JsonUtils.jsonToPojo(value, TbItemDesc.class);
				return E3Result.ok(jsonToPojo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		TbItemDesc itemDesc = itemDescMapper.selectByPrimaryKey(id);
		//放入缓存
		try {
			JedisClient.set("ITEM_INFO_PRE:" + String.valueOf(id) + "DESC", JsonUtils.objectToJson(itemDesc));
			//设计过期时间
			JedisClient.expire("ITEM_INFO_PRE:" + String.valueOf(id) + "DESC", ITEM_INFO_EXPIRE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return E3Result.ok(itemDesc);
	}

	/**
	 * 编辑商品
	 */
	public E3Result updateDesc(TbItem item, String desc) {
		//获得商品id
		Long id = item.getId();
		//更新商品
		item.setId(null);
		TbItemExample example = new TbItemExample();
		Criteria criteria = example.createCriteria();
		criteria.andIdEqualTo(id);
		itemMapper.updateByExampleSelective(item, example );
		
		//更新商品描述
		TbItemDesc itemdesc = new TbItemDesc();
		itemdesc.setItemDesc(desc);
		TbItemDescExample descEmp = new TbItemDescExample();
		cn.e3mall.pojo.TbItemDescExample.Criteria createCriteria = descEmp.createCriteria();
		createCriteria.andItemIdEqualTo(id);
		itemDescMapper.updateByExampleSelective(itemdesc, descEmp);
		return E3Result.ok();
	}
	
}
