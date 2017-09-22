package cn.e3mall.search.listener;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import cn.e3mall.item.pojo.Item;
import cn.e3mall.pojo.E3Result;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.service.ItemService;
import freemarker.template.Configuration;
import freemarker.template.Template;

public class HtmlGenListener implements MessageListener{

	@Autowired
	private ItemService itemService;
	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;
	@Value("${HTMLGEN_DIR}")
	private String HTMLGEN_DIR;
	
	@Override
	public void onMessage(Message message) {
		FileWriter writer = null;
		try {
			TextMessage textmessage = (TextMessage)message;
			String text = textmessage.getText();
			if (StringUtils.isNotBlank(text)) {
				long id = Long.parseLong(text);
				//等待事务提交
				Thread.sleep(100);
				TbItem tbitem = itemService.getItemById(id);
				Item item = new Item(tbitem);
				//查询数据
				E3Result e3itemDesc = itemService.getItemDesc(id);
				TbItemDesc itemDesc = (TbItemDesc) e3itemDesc.getData();
				//将数据封装到map中
				HashMap<Object, Object> map = new HashMap<>();
				map.put("item", item);
				map.put("itemDesc", itemDesc);
				Configuration configuration = freeMarkerConfigurer.getConfiguration();
				Template template = configuration.getTemplate("item.ftl");
				//文件名为路径加id
				writer = new FileWriter(HTMLGEN_DIR + File.separator + id + ".html");
				//生成文件
				template.process(map, writer);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}


}
