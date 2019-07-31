package cn.dubby.light.id.generator.redis;

import cn.dubby.light.id.generator.IDProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

/**
 * @author dubby
 * @date 2019/7/29 17:51
 */
public class RedisProvider extends IDProvider {

    private static final Logger logger = LoggerFactory.getLogger(RedisProvider.class);

    public RedisProvider(int id, Jedis jedis, String key) {
        this.id = id;
        this.jedis = jedis;
        this.key = key;
    }

    private int id;

    private Jedis jedis;

    private String key;

    @Override
    public long increaseByDataSource() {
        try {
            return jedis.incr(key);
        } catch (Exception e) {
            logger.error("provide, id:{}, key:{}", id, key);
            return -1;
        }
    }
}
