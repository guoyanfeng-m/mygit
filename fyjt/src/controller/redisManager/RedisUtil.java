package controller.redisManager;

import java.util.ArrayList;
import java.util.List;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;



public class RedisUtil {
	/**
	 * 状态-等待下载
	 */
	public static final Integer STATUS_WAIT= 1;
	/**
	 * 状态-下载中
	 */
	public static final Integer STATUS_DOWNING = 2; 
	/**
	 * 状态-下载成功
	 */
	public static final Integer STATUS_SUCC = 3;
	/**
	 * 状态-下载失败
	 */
	public static final Integer STATUS_FAILD= 0;
	/**
	 * 状态-下载中断
	 */
	public static final Integer STATUS_INTERRUPT= 4;
	
	
	
	
	
	
	
    private static JedisPool jedisPool;//非切片连接池
    private static ShardedJedisPool shardedJedisPool;//切片连接池
    
    private RedisUtil(String ip) 
    { 
        initialPool(ip); 
        initialShardedPool(ip); 
    } 
    public static Jedis getJedis(String ip){
    	if(RedisUtil.jedisPool==null){
    		new RedisUtil(ip);
    	}
    	return RedisUtil.jedisPool.getResource();
    }
    public static ShardedJedis getShardedJedis(String ip){
    	if(RedisUtil.shardedJedisPool==null){
    		new RedisUtil(ip);
    	}
    	return RedisUtil.shardedJedisPool.getResource();
    }
    public static void returnJedis(Jedis jedis){
    	RedisUtil.jedisPool.returnResource(jedis);
    }
    public static void returnShardedJedis(ShardedJedis jedis){
    	RedisUtil.shardedJedisPool.returnResource(jedis);
    }
    /**
     * 初始化非切片池
     */
    private void initialPool(String ip) 
    { 
        // 池基本配置 
        JedisPoolConfig config = new JedisPoolConfig(); 
        config.setMaxActive(20); 
        config.setMaxIdle(10); 
        config.setMaxWait(1000l); 
        config.setTestOnBorrow(false); 
        
        RedisUtil.jedisPool = new JedisPool(config,ip,6379);
    }
    
    /** 
     * 初始化切片池 
     */ 
    private void initialShardedPool(String ip) 
    { 
        // 池基本配置 
        JedisPoolConfig config = new JedisPoolConfig(); 
        config.setMaxActive(20); 
        config.setMaxIdle(5); 
        config.setMaxWait(1000l); 
        config.setTestOnBorrow(false); 
        // slave链接 
        List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>(); 
        shards.add(new JedisShardInfo(ip, 6379, "master")); 

        // 构造池 
        RedisUtil.shardedJedisPool = new ShardedJedisPool(config, shards); 
    } 
}