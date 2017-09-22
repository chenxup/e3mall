package cn.e3mall.content.service;

import java.util.List;

import cn.e3mall.pojo.E3Result;
import cn.e3mall.pojo.EasyUiTreeData;

public interface CategoryService {
	List<EasyUiTreeData> getCategoryByParentId(Long parentId);
	E3Result save(Long parentId, String text);
	void delete(Long id);
}
