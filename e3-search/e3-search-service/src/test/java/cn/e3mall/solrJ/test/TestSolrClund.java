package cn.e3mall.solrJ.test;

import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.common.SolrInputDocument;

public class TestSolrClund {
	
	//@Test
	public void testName() throws Exception {
		CloudSolrServer solrServer = new CloudSolrServer("192.168.25.129:2181,192.168.25.129:2182,192.168.25.129:2183");
		solrServer.setDefaultCollection("collection2");
		SolrInputDocument doc = new SolrInputDocument();
		doc.addField("id", "01");
		doc.addField("item_sell_point", "中国人");
		doc.addField("item_price", 100L);
		solrServer.add(doc);
		solrServer.commit();
	}
	
	
	
}
