package cn.e3mall.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.mapper.TbItemCatMapper;
import cn.e3mall.pojo.EasyUiTreeData;
import cn.e3mall.pojo.TbItemCat;
import cn.e3mall.pojo.TbItemCatExample;
import cn.e3mall.pojo.TbItemCatExample.Criteria;
import cn.e3mall.service.ItemCatService;

@Service
public class ItemCatServiceImpl implements ItemCatService {

	@Autowired
	private TbItemCatMapper itemCatMapper;
	
	/**
	 * 根据父节点查询
	 */
	public List<EasyUiTreeData> getItemCatByParentId(Long parentId) {
		TbItemCatExample example = new TbItemCatExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		List<TbItemCat> itemCatList = itemCatMapper.selectByExample(example);
		
		List<EasyUiTreeData> listData = new ArrayList<EasyUiTreeData>(); 
		//将结果封装
		for (TbItemCat tbItemCat : itemCatList) {
			EasyUiTreeData data = new EasyUiTreeData();
			data.setId(tbItemCat.getId());
			data.setText(tbItemCat.getName());
			data.setState(tbItemCat.getIsParent() ? "closed" : "open");
			listData.add(data);
		}
		
		return listData;
	}

}
