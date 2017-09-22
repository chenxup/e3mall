package cn.e3mall.content.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.content.service.CategoryService;
import cn.e3mall.mapper.TbContentCategoryMapper;
import cn.e3mall.pojo.E3Result;
import cn.e3mall.pojo.EasyUiTreeData;
import cn.e3mall.pojo.TbContentCategory;
import cn.e3mall.pojo.TbContentCategoryExample;
import cn.e3mall.pojo.TbContentCategoryExample.Criteria;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private TbContentCategoryMapper categoryMapper;
	
	/**
	 * 根据父节点查询内容分类
	 */
	public List<EasyUiTreeData> getCategoryByParentId(Long parentId) {
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		List<TbContentCategory> categoryList = categoryMapper.selectByExample(example);
		//将结果组装
		List<EasyUiTreeData> list = new ArrayList<EasyUiTreeData>();
		for (TbContentCategory category : categoryList) {
			EasyUiTreeData data = new EasyUiTreeData();
			data.setId(category.getId());
			data.setText(category.getName());
			data.setState(category.getIsParent() ? "closed" : "open");
			list.add(data);
		}
		
		return list;
	}

	/**
	 * 保存商品分类
	 */
	public E3Result save(Long parentId, String text) {
		//保存分类
		TbContentCategory category= new TbContentCategory();
		category.setCreated(new Date());
		category.setIsParent(false);
		category.setName(text);
		category.setParentId(parentId);
		category.setSortOrder(1);
		//状态：1正常，0删除
		category.setStatus(1);
		category.setUpdated(new Date());
		categoryMapper.insert(category);
		
		//如果父节点是叶子节点，将它的isparent更改
		TbContentCategory parentCategory = categoryMapper.selectByPrimaryKey(parentId);
		if (!parentCategory.getIsParent()) {
			parentCategory.setIsParent(true);
		}
		categoryMapper.updateByPrimaryKey(parentCategory);
		return E3Result.ok(category);
	}

	/**
	 * 根据id删除
	 */
	public void delete(Long id) {
		TbContentCategory category = categoryMapper.selectByPrimaryKey(id);
		//判断父节点是否还有叶子节点，如果没有，改变isparent
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(category.getParentId());
		//删除
		categoryMapper.deleteByPrimaryKey(id);
		int count = categoryMapper.countByExample(example);
		if (count == 0) {
			TbContentCategory contentCategory = new TbContentCategory();
			contentCategory.setId(category.getParentId());
			contentCategory.setIsParent(false);
			categoryMapper.updateByPrimaryKeySelective(contentCategory);
		}
	}

}
