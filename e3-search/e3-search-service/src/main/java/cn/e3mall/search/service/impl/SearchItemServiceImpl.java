package cn.e3mall.search.service.impl;

import java.io.IOException;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.pojo.E3Result;
import cn.e3mall.pojo.SearchItem;
import cn.e3mall.pojo.SearchResult;
import cn.e3mall.search.dao.SearchItemDao;
import cn.e3mall.search.mapper.SearchItemMapper;
import cn.e3mall.search.service.SearchItemService;

@Service
public class SearchItemServiceImpl implements SearchItemService {

	@Autowired
	private SearchItemMapper searchItemMapper;
	@Autowired
	private SolrServer solrServer;
	@Autowired
	private SearchItemDao searchItemDao;
	/**
	 * 将商品导入到索引库
	 */
	public E3Result searchItemImport() {
		try {
			List<SearchItem> list = searchItemMapper.findAll();
			for (SearchItem item : list) {
				SolrInputDocument doc = new SolrInputDocument();
				doc.addField("id", item.getId());
				doc.addField("item_title", item.getTitle());
				doc.addField("item_sell_point", item.getSell_point());
				doc.addField("item_price", item.getPrice());
				doc.addField("item_image", item.getImage());
				doc.addField("item_category_name", item.getCategory_name());
				solrServer.add(doc);
			}
			solrServer.commit();
			return E3Result.ok();
		} catch (Exception e) {
			return E3Result.build(500, "数据收入失败");
		}
		
	}
	
	/**
	 * 复杂查询
	 */
	public SearchResult queryItems(int page, int rows, String keyWord) {
		//封装查询结果
		SolrQuery query = new SolrQuery();
		query.setQuery(keyWord);
		//设置默认域
		query.set("df", "item_keywords");
		//设置高亮
		//1.开启高亮开关
		query.setHighlight(true);
		//2.添加高亮显示的域
		query.addHighlightField("item_title");
		//3.设置高亮的开头
		query.setHighlightSimplePre("<span style='color:red'>");
		//4.设置高亮的结尾
		query.setHighlightSimplePost("</span>");
		//设置分页
		int startPage = (page-1) * rows;
		query.setStart(startPage);
		query.setRows(rows);
		
		//查询
		SearchResult result = searchItemDao.queryItem(query);
		//计算总页数
		int totalPage = (int) ((result.getRecourdCount() + rows - 1) / rows);
		result.setTotalPages(totalPage);
		result.setPage(page);
		return result;
	}

	/**
	 * 根据id，查询商品
	 * @throws IOException 
	 * @throws SolrServerException 
	 */
	public E3Result saveSearchById(Long id) throws Exception {
		SearchItem item = searchItemMapper.findSearchItemByid(id);
		SolrInputDocument doc = new SolrInputDocument();
		doc.addField("id", item.getId());
		doc.addField("item_title", item.getTitle());
		doc.addField("item_sell_point", item.getSell_point());
		doc.addField("item_price", item.getPrice());
		doc.addField("item_image", item.getImage());
		doc.addField("item_category_name", item.getCategory_name());
		solrServer.add(doc);
		solrServer.commit();
		return E3Result.ok();
	}

	/**
	 * 根据商品逻辑id删除
	 */
	public E3Result delSearchItemById(Long id) throws Exception {
		solrServer.deleteById(String.valueOf(id));
		solrServer.commit();
		return E3Result.ok();
	}

}
