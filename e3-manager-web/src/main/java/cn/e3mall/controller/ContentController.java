package cn.e3mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.content.service.ContentService;
import cn.e3mall.pojo.E3Result;
import cn.e3mall.pojo.EasyUiDataGuridPage;
import cn.e3mall.pojo.TbContent;

@Controller
public class ContentController {
	
	@Autowired
	private ContentService contentService;
	
	/**
	 * 保存
	 */
	@RequestMapping("/content/save")
	@ResponseBody
	public E3Result save(TbContent content) {
		contentService.save(content);
		return E3Result.ok();
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/content/query/list")
	@ResponseBody
	public EasyUiDataGuridPage pageQuery(int page, int rows) {
		EasyUiDataGuridPage data = contentService.pageQuery(page, rows);
		return data;
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/content/delete")
	@ResponseBody
	public void delete(String ids) {
		contentService.deleteByIds(ids);
	}
}
