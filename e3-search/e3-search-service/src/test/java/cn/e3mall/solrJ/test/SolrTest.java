package cn.e3mall.solrJ.test;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;

public class SolrTest {
	//@Test
	public void testName() throws Exception {
		SolrServer solrServer = new HttpSolrServer("http://192.168.25.129:8080/solr");
		SolrInputDocument doc = new SolrInputDocument();
		doc.addField("id", "01");
		doc.addField("item_sell_point", "中国人");
		doc.addField("item_price", 100L);
		doc.addField("item_image", "httP://");
		doc.addField("item_category_name", "图书");
		solrServer.add(doc);
		solrServer.commit();
	}
}
