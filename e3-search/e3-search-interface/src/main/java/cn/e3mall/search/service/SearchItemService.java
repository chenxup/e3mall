package cn.e3mall.search.service;

import cn.e3mall.pojo.E3Result;
import cn.e3mall.pojo.SearchResult;

public interface SearchItemService {
	E3Result searchItemImport();
	SearchResult queryItems(int page, int rows, String keyWord);
	E3Result saveSearchById(Long id) throws Exception;
	E3Result delSearchItemById(Long id) throws Exception;
}
