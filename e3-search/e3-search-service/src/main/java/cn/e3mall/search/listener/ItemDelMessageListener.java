package cn.e3mall.search.listener;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import cn.e3mall.search.service.SearchItemService;

public class ItemDelMessageListener implements MessageListener{

	@Autowired
	private SearchItemService SearchItemService;
	
	@Override
	public void onMessage(Message message) {
		try {
			TextMessage textMessage = (TextMessage)message;
			String text = textMessage.getText();
			if (StringUtils.isNotBlank(text)) {
				String[] str = text.split(",");
				for (String id : str) {
					SearchItemService.delSearchItemById(Long.parseLong(id));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
