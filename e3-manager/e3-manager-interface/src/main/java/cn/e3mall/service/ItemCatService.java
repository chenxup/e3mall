package cn.e3mall.service;

import java.util.List;

import cn.e3mall.pojo.EasyUiTreeData;

public interface ItemCatService {
	List<EasyUiTreeData> getItemCatByParentId(Long parentId);
}
