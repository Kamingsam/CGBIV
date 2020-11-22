package com.jt.test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.params.SetParams;

public class TestRedis {
	//利用redis自己的API暂时不需要和Spring整合.
	//报错信息:程序链接不通.  
	// 1.关闭防火墙   service iptables stop
	// 2.检查redis.conf中的配置文件  ip绑定注释,关闭保护模式,后台启动
	// 3.redis-server redis.conf
	@Test
	public void testString() {
		String host="192.168.88.129";
		Integer port=6379;
		Jedis jedis = new Jedis(host, port);
		//redis中的操作命令就是调用的方法
		String res = jedis.set("1910", "CGB1910");
		System.out.println("结果为:"+res);
		String value = jedis.get("1910");
		System.out.println("获取的value为:"+value);
	}
	
	Jedis jedis = null;

	@BeforeEach
	public void init() {
		String host="192.168.88.129";
		Integer port=6379;
		jedis = new Jedis(host, port);
	}

	/**
	 * 	业务说明:
	 * 	 如果redis中已经存在了这个key,则不允许赋值.
	 * 	 redis方法介绍:
	 * 		1.利用exists判断实现赋值.
	 * 		2.利用setnx方式进行判断
	 */
	@Test
	public void test02() {
		//1.判断一个该key是否存在.
		if(!jedis.exists("1910")) {
			jedis.set("1910", "测试赋值是否成功!!!");
		}
		System.out.println(jedis.get("1910"));
	}
	
	@Test
	public void test03() {
		//如果key不存在,则执行set操作,否则跳过.
		Long res = jedis.setnx("1910", "测试赋值");
		System.out.println(res);
		System.out.println(jedis.get("1910"));
	}
	
	/**
	 * 为key添加超时时间
	 * @throws InterruptedException 
	 * 原子性操作: 要求赋值和超时时间的设定同时完成.
	 */
	@Test
	public void test04() throws Exception{
		jedis.setnx("1910", "超时时间的测试");
		jedis.expire("1910", 20);	//添加超时时间
		//暂时不是原子性操作.
		Thread.sleep(3000);
		Long seconds =jedis.ttl("1910");
		System.out.println("剩余存活时间:"+seconds);
	}
	
	//实现原子性操作
	@Test
	public void test05() throws InterruptedException {
		//要么同时成功,要么同时失败.
		jedis.setex("1910", 20, "测试原子性");
		Thread.sleep(2000);
		System.out.println(jedis.ttl("1910"));
	}
	/**
	 * 要求: 进行操作和添加超时时间同时完成.并且不允许修改已有数据.
	 * 问题说明:
	 * 		setnx:可以实现不修改已有数据.
	 * 		setex:可以实现赋值和超时时间操作.
	 * 		但是上述操作不能同时完成.要求时原子性的
	 * 
	 *  String XX = "xx";  完成赋值.不管之间
  		String NX = "nx";  如果存在,不赋值.
  		String PX = "px";  超时时间单位毫秒
  		String EX = "ex";  超时时间单位秒
	 * @throws InterruptedException 
	 */
	@Test
	public void test06() throws InterruptedException {
		SetParams params = new SetParams();
		params.nx().ex(5);
		//满足赋值和超时的原子性操作.
		jedis.set("1910", "String类型的终极测试", params);
		Thread.sleep(5000);
		System.out.println(jedis.get("1910"));
	} 
	
//	@Test
//	public void test07() {
//		System.out.println(2<<3);
//	}
	
	@Test
	public void testShards01() {
		List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
		shards.add(new JedisShardInfo("192.168.88.129", 6379));
		shards.add(new JedisShardInfo("192.168.88.129", 6380));
		shards.add(new JedisShardInfo("192.168.88.129", 6381));
		ShardedJedis shardedJedis = new ShardedJedis(shards);
		
		shardedJedis.set("1910", "分片测试1");
		System.out.println(shardedJedis.get("1910"));
		
	}
	@Test
	public void testSentinel01() {
		Set<String> sentinels = new HashSet<>();
		sentinels.add("192.168.88.129:26379");//哨兵配置信息
		JedisSentinelPool sentinelPool = new JedisSentinelPool("mymaster", sentinels);
		Jedis jedis = sentinelPool.getResource();
		jedis.set("1910", "哨兵测试");
		System.out.println(jedis.get("1910"));
	}
	
	@Test
	public void testCluster01() {
		Set<HostAndPort> nodes = new HashSet<>();
		nodes.add(new HostAndPort("192.168.88.129", 7000));
		nodes.add(new HostAndPort("192.168.88.129", 7001));
		nodes.add(new HostAndPort("192.168.88.129", 7002));
		nodes.add(new HostAndPort("192.168.88.129", 7003));
		nodes.add(new HostAndPort("192.168.88.129", 7004));
		nodes.add(new HostAndPort("192.168.88.129", 7005));
		JedisCluster jedisCluster = new JedisCluster(nodes);
		
		jedisCluster.set("1910", "Cluster redis集群测试");
		System.out.println(jedisCluster.get("1910"));
	}
}
