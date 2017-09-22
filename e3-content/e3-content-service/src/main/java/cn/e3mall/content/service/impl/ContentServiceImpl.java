package cn.e3mall.content.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.e3mall.content.service.ContentService;
import cn.e3mall.jedis.JedisClient;
import cn.e3mall.mapper.TbContentMapper;
import cn.e3mall.pojo.E3Result;
import cn.e3mall.pojo.EasyUiDataGuridPage;
import cn.e3mall.pojo.TbContent;
import cn.e3mall.pojo.TbContentExample;
import cn.e3mall.pojo.TbContentExample.Criteria;
import cn.e3mall.utils.JsonUtils;

@Service
public class ContentServiceImpl implements ContentService {

	@Autowired
	private TbContentMapper contentMapper;
	
	@Autowired
	private JedisClient jedisClient;
	
	@Value("${CONTENT_KEY}")
	private String CONTENT_KEY;
	
	/**
	 * 保存内容
	 */
	public E3Result save(TbContent content) {
		//补全内容
		content.setUpdated(new Date());
		content.setCreated(new Date());
		contentMapper.insert(content);
		return E3Result.ok();
	}

	/**
	 * 分页查询
	 */
	public EasyUiDataGuridPage pageQuery(int page, int rows) {
		PageHelper pageHelper = new PageHelper();
		pageHelper.startPage(page, rows);
		TbContentExample example = new TbContentExample();
		List<TbContent> contentList = contentMapper.selectByExample(example);
		PageInfo<TbContent> pageInfo = new PageInfo<>(contentList);
		EasyUiDataGuridPage data = new EasyUiDataGuridPage();
		data.setTotal(pageInfo.getTotal());
		data.setRows(pageInfo.getList());
		
		return data;
	}

	/**
	 * 删除多个id
	 */
	public void deleteByIds(String ids) {
		String[] strIds = ids.split(",");
		for (String id : strIds) {
			contentMapper.deleteByPrimaryKey(Long.valueOf(id));
		}
	}

	/**
	 *根据分类id查询
	 */
	public List<TbContent> findContentById(Long id) {
		try {
			//从redis中查询
			String contentStr = jedisClient.hget(CONTENT_KEY, String.valueOf(id));
			if (StringUtils.isNotBlank(contentStr)){
				return JsonUtils.jsonToList(contentStr, TbContent.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		TbContentExample example = new TbContentExample();
		Criteria createCriteria = example.createCriteria();
		createCriteria.andCategoryIdEqualTo(id);
		List<TbContent> list = contentMapper.selectByExampleWithBLOBs(example);
		
		//将查询的结果放入redis中
		try {
			jedisClient.hset(CONTENT_KEY, String.valueOf(id), JsonUtils.objectToJson(list));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}

}
