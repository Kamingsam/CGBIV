package com.jt.config;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.JedisShardInfo;
//标识配置类
import redis.clients.jedis.ShardedJedis;
@Configuration
@PropertySource("classpath:/properties/redis.properties")
public class RedisConfig {
//	@Value("${redis.node}")
//	private String redisNode;
//	@Value("${redis.shards}")
//	private String redisShards;

	//单台redis
//	@Bean	//标识实例化对象的类型
//	@Scope("prototype")	//对象的多例
//	public Jedis jedis() {
//		String[] nodeArray = redisNode.split(":");
//		String host = nodeArray[0];
//		int port = Integer.parseInt(nodeArray[1]);
//		return new Jedis(host, port);
//	}


//	@Bean
//	@Scope("prototype")
//	public ShardedJedis shardedJedis() {//redis分片
//		//ip:port,ip:port,ip:port
//		List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
//
//		String[] nodeArray = redisShards.split(",");
//		for (String node : nodeArray) {
//			String[] nodeArr = node.split(":");
//			String host = nodeArr[0];
//			int port = Integer.parseInt(nodeArr[1]);
//			//每循环一次,添加一个node节点对象到list集合中
//			shards.add(new JedisShardInfo(host, port));
//		}
//
//		return new ShardedJedis(shards);
//	}
//
//	@Value("${redis.sentinel}")
//	private String sentinel;	
//	/**
//	 * 3.整合redis的哨兵
//	 * 创建哨兵的池对象.
//	 */
//	@Bean
//	public JedisSentinelPool jedisSentinelPool() {
//		Set<String> sentinels = new HashSet<>();
//		sentinels.add(sentinel);
//		return new JedisSentinelPool("mymaster", sentinels);
//	}
//	//2.动态获取池中的jedis对象
//	//问题说明:如何在方法中,动态获取bean对象.
//	//知识点说明: 
//	//    1.Spring @Bean注解工作时,如果发现方法有参数列表.则会自动的注入.
//	//    2.@Qualifier 利用名称,实现对象的动态赋值.
//
//	//sentinelJedis:jedis对象
//	@Bean
//	@Scope("prototype")//设置为多例,用户什么时候使用,什么时候创建对象
//	public Jedis sentinelJedis(JedisSentinelPool jedisSentinelPool) {
//		Jedis jedis = jedisSentinelPool.getResource();
//		return jedis;
//	}
	@Value("${redis.clusters}")
	private String clusterNodes;
	
	@Bean
	@Scope("prototype")
	public JedisCluster jedisCluster() {
		Set<HostAndPort> nodes = new HashSet<>();
		String[] nodesArray = clusterNodes.split(",");
		for (String n : nodesArray) {
			String host = n.split(":")[0];
			int port = Integer.parseInt(n.split(":")[1]);			
			nodes.add(new HostAndPort(host, port));
		}
		return new JedisCluster(nodes);
	}
	
	
	
	
}
