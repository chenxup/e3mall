package cn.e3mall.search.listener;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import cn.e3mall.search.service.SearchItemService;

public class ItemAddMessageListener implements MessageListener {

	@Autowired
	private SearchItemService searchItemService;

	@Override
	public void onMessage(Message message) {
		try {
			TextMessage textMessage = (TextMessage) message;
			String text = textMessage.getText();
			Thread.sleep(100);
			if (StringUtils.isNotBlank(text)) {
				text = textMessage.getText();
				Long id = Long.valueOf(text);
				searchItemService.saveSearchById(id);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
