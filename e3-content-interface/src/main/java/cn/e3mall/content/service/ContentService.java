package cn.e3mall.content.service;

import java.util.List;

import cn.e3mall.pojo.E3Result;
import cn.e3mall.pojo.EasyUiDataGuridPage;
import cn.e3mall.pojo.TbContent;

public interface ContentService {
	E3Result save(TbContent content);
	EasyUiDataGuridPage pageQuery(int page, int rows);
	void deleteByIds(String ids);
	List<TbContent> findContentById(Long id);
}
