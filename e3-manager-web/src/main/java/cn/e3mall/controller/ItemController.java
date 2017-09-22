package cn.e3mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.pojo.E3Result;
import cn.e3mall.pojo.EasyUiDataGuridPage;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.service.ItemService;

/**
 * 商品controller
 * @author HOC
 *
 */
/**
 * @author HOC
 *
 */
/**
 * @author HOC
 *
 */
@Controller
public class ItemController {

	@Autowired
	private ItemService itemService;
	
	/**
	 * 根据id查询
	 * @param id
	 * @return
	 */
	@RequestMapping("/item/{id}")
	@ResponseBody
	public TbItem getItemById(@PathVariable Long id){
		return itemService.getItemById(id);
	}
	
	
	/**
	 * 分页查询
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/item/list")
	@ResponseBody
	public EasyUiDataGuridPage getItemList(int page, int rows){
		return itemService.getItemList(page, rows);
	}
	
	
	/**
	 * 商品保存
	 */
	@RequestMapping(value="item/save", method=RequestMethod.POST)
	@ResponseBody
	public E3Result saveItem(TbItem item, String desc) {
		E3Result result = itemService.saveItem(item, desc);
		return result;
	}
	
	/**
	 * 商品删除
	 */
	@RequestMapping("/rest/item/delete")
	@ResponseBody
	public E3Result deleteItem(String ids) {
		E3Result result = itemService.DeleteItem(ids);
		return result;
	}
	
	/**
	 * 获得商品描述
	 */
	@RequestMapping("/rest/item/query/item/desc/{id}")
	@ResponseBody
	public E3Result getItemDesc(@PathVariable Long id) {
		E3Result result = itemService.getItemDesc(id);
		return result;
	}
	
	/**
	 * 更新商品
	 */
	@RequestMapping("/rest/item/update")
	@ResponseBody
	public E3Result getItemDesc(TbItem item, String desc) {
		E3Result result = itemService.updateDesc(item, desc);
		return result;
	}
	
	
	
	
}
