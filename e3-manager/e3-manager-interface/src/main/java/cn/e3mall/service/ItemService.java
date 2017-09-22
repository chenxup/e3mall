package cn.e3mall.service;

import cn.e3mall.pojo.E3Result;
import cn.e3mall.pojo.EasyUiDataGuridPage;
import cn.e3mall.pojo.TbItem;

public interface ItemService {
	TbItem getItemById(Long id);
	EasyUiDataGuridPage getItemList(int page, int rows);
	E3Result saveItem(TbItem item, String desc);
	E3Result DeleteItem(String ids);
	E3Result getItemDesc(Long id);
	E3Result updateDesc(TbItem item, String desc);
}
