package cn.e3mall.search.mapper;

import java.util.List;

import cn.e3mall.pojo.SearchItem;

public interface SearchItemMapper {
	List<SearchItem> findAll();
	SearchItem findSearchItemByid(Long id);
	void deleteSearchItemById(Long id);
}
