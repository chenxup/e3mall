package cn.e3mall.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.content.service.CategoryService;
import cn.e3mall.pojo.E3Result;
import cn.e3mall.pojo.EasyUiTreeData;

/**
 * 内容分类
 * @author HOC
 *
 */
@Controller
public class CategoryController {

	@Autowired
	private CategoryService categoryService;
	
	/**
	 * 根据父节点，查找分类
	 */
	@RequestMapping("/content/category/list{id}")
	@ResponseBody
	public List<EasyUiTreeData> getCategoryByParentId(@RequestParam(value="id", defaultValue="0") Long parentId) {
		List<EasyUiTreeData> list = categoryService.getCategoryByParentId(parentId);
		return list;
	}
	
	/**
	 * 添加新节点
	 */
	@RequestMapping(value="/content/category/create", method=RequestMethod.POST)
	@ResponseBody
	public E3Result save(Long parentId, String name) {
		E3Result result = categoryService.save(parentId, name);
		return result;
	}
	
	/**
	 * 删除节点
	 */
	@RequestMapping(value="/content/category/delete", method=RequestMethod.POST)
	@ResponseBody
	public void delete(Long id) {
		categoryService.delete(id);
	}
	
}
