package cn.e3mall.testjedis;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.e3mall.jedis.JedisClient;

public class TestJedis {

	//@Test
	public void testOne() throws Exception {
		ApplicationContext app = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-jedis.xml");
		JedisClient jedisClient = (JedisClient) app.getBean("jedisClient");
		jedisClient.set("a", "1");
	    String name = jedisClient.get("a");
	    System.out.println(name);
		
	}
}	
