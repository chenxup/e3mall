package cn.e3mall.testJedis;

import java.util.HashSet;
import java.util.Set;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

public class TestJedis {
	
	/**
	 * 单机版
	 * @throws Exception
	 */
	//@Test
	public void testOne() throws Exception {
		JedisPool jedisPool = new JedisPool("192.168.25.128", 6379);
		Jedis jedis = jedisPool.getResource();
		jedis.set("user", "小明");
		String name = jedis.get("user");
		System.out.println(name);
		jedisPool.close();
	}
	
	/**
	 * 集群版
	 * @throws Exception
	 */
	//@Test
	public void testCluster() throws Exception {
		Set<HostAndPort> nodes = new HashSet<HostAndPort>();
		nodes.add(new HostAndPort("192.168.25.128", 7001));
		nodes.add(new HostAndPort("192.168.25.128", 7002));
		nodes.add(new HostAndPort("192.168.25.128", 7003));
		nodes.add(new HostAndPort("192.168.25.128", 7004));
		nodes.add(new HostAndPort("192.168.25.128", 7005));
		nodes.add(new HostAndPort("192.168.25.128", 7006));
		JedisCluster jedis = new JedisCluster(nodes );
		jedis.set("age", "10");
		String age = jedis.get("age");
		System.out.println(age);
		jedis.close();
	}
}
