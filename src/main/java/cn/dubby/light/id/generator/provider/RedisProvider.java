package cn.dubby.light.id.generator.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author dubby
 * @date 2019/7/29 17:51
 */
public class RedisProvider {

    private static final Logger logger = LoggerFactory.getLogger(RedisProvider.class);

    public RedisProvider(int id, JedisPool jedisPool, String key) {
        this.id = id;
        this.jedisPool = jedisPool;
        this.key = key;
    }

    private int id;

    private JedisPool jedisPool;

    private String key;

    public long provide() {
        try (Jedis jedis = jedisPool.getResource()) {
            long value = jedis.incr(key);
            return value << 10 | id;
        } catch (Exception e) {
            logger.error("provide, id:{}, key:{}", id, key);
            return -1;
        }
    }
}
