package cn.e3mall.solrJ.test;

import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.junit.Test;

public class TestSolrQuery {
	@Test
	public void testName() throws Exception {
		SolrServer solrServer = new HttpSolrServer("http://192.168.25.129:8080/solr");
		//创建查询对象
		SolrQuery query = new SolrQuery();
		//设置查询的语法,两种写法
		query.set("q", "item_keywords:阿尔卡特");
		//query.setQuery("阿尔卡特");
		
		//设置默认域
		query.set("df","item_keywords");
		
		//高亮
		//1.开启高亮开关
		query.setHighlight(true);
		//2.添加高亮显示的域
		query.addHighlightField("item_title");
		//3.设置高亮的开头
		query.setHighlightSimplePre("<span style='color:red'>");
		//4.设置高亮的结尾
		query.setHighlightSimplePost("</span>");
		
		//分页
		query.setStart(1);
		query.setRows(4);
		
		QueryResponse response = solrServer.query(query);
		//得到所有的文档集合
		SolrDocumentList results = response.getResults();
		
		/*
		 * 得到所有的高亮域集合
		 * 大Map: key-id value-map
		 * 小Map: key-域   value-值
		 */
		
		Map<String, Map<String, List<String>>> map = response.getHighlighting();
		//查询的总记录数
		long count = results.getNumFound();
		System.out.println("count======" + count);
		
		//遍历打印document文档
		for (SolrDocument doc : results) {
			System.out.println(doc.get("id"));
			System.out.println(doc.get("item_title"));
			System.out.println(doc.get("item_sell_point"));
			System.out.println(doc.get("item_price"));
			System.out.println(doc.get("item_image"));
			System.out.println(doc.get("item_category_name"));
			System.out.println("============");
			
			List<String> list = map.get(doc.get("id")).get("item_title");
			if (list != null && list.size() > 0) {
				System.out.println(list.get(0));
			}
			System.out.println("============");
		}
		
		
		solrServer.commit();
		
	}
}
