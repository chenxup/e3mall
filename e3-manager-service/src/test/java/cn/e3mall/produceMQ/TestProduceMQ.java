package cn.e3mall.produceMQ;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

public class TestProduceMQ {
	@Test
	public void testName() throws Exception {
		ApplicationContext app = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-acitveMQ.xml");
		
		JmsTemplate jmsTemplate = app.getBean(JmsTemplate.class);
		Destination destination = (Destination) app.getBean("queueDestination");
		
		jmsTemplate.send(destination, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage massage = session.createTextMessage("发布了一条队列消息");
				return massage;
			}
		});
	}
}
