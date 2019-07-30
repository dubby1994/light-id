package cn.dubby.light.id.generator.redis;

import cn.dubby.light.id.config.GenProviderConfig;
import cn.dubby.light.id.config.GeneratorConfig;
import cn.dubby.light.id.generator.AbstractLightIDGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPool;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author dubby
 * @date 2019/7/29 16:43
 */
public class RedisGenerator extends AbstractLightIDGenerator {

    private static final Logger logger = LoggerFactory.getLogger(RedisGenerator.class);

    private RedisProvider[] providers;

    public RedisGenerator(GeneratorConfig config) throws URISyntaxException {
        super(config.getBufferSize(), config.getIdleSize(), config.getCheckInterval());
        providers = new RedisProvider[config.getProviders().size()];
        int index = 0;
        for (GenProviderConfig c : config.getProviders()) {
            JedisPool jedisPool = new JedisPool(new URI(c.getUrl()), config.getTimeout());
            RedisProvider provider = new RedisProvider(c.getId(), jedisPool, c.getNamespace());
            providers[index] = provider;
            ++index;
        }
        fillCache();
        startAsyncGenerate();
    }

    @Override
    protected void fillCache() {
        logger.info("transferQueue.size:{}", transferQueue.size());
        while (transferQueue.size() < bufferSize) {
            for (RedisProvider provider : providers) {
                long id = provider.provide();
                if (id > 0) {
                    transferQueue.offer(id);
                }
            }
        }
        logger.info("transferQueue.size:{}", transferQueue.size());
    }
}
