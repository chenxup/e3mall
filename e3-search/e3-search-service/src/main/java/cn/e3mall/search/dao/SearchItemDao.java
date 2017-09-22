package cn.e3mall.search.dao;

import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.e3mall.pojo.SearchItem;
import cn.e3mall.pojo.SearchResult;

@Repository
public class SearchItemDao {
	
	@Autowired
	private SolrServer solrServer;
	
	public SearchResult queryItem(SolrQuery query) {
		try {
			SearchResult searchResult = new SearchResult();
			QueryResponse result = solrServer.query(query);
			SolrDocumentList results = result.getResults();
			//查询总记录数
			long numFound = results.getNumFound();
			searchResult.setRecourdCount(numFound);
			List<SearchItem> items = searchResult.getItems();
			
			//高亮结果
			Map<String, Map<String, List<String>>> highlighting = result.getHighlighting();
			String title = "";
			for (SolrDocument doc : results) {
				SearchItem item = new SearchItem();
				item.setId(doc.get("id") + "");
				item.setCategory_name((String) doc.get("item_category_name"));
				item.setImage((String) doc.get("item_image"));
				item.setPrice(doc.get("item_price")+"");
				item.setSell_point((String) doc.get("item_sell_point"));
				//将title高亮显示
				title = (String) doc.get("item_title");
				List<String> list = highlighting.get(doc.get("id")).get("item_title");
				if (list != null && list.size() > 0) {
					title = list.get(0);
				}
				item.setTitle(title);
				items.add(item);
			}
			
			return searchResult;
			
		} catch (SolrServerException e) {
			e.printStackTrace();
		}
		return null;
	}
}
