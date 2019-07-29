package cn.dubby.light.id.generator;

import cn.dubby.light.id.config.GenProviderConfig;
import cn.dubby.light.id.config.GeneratorConfig;
import cn.dubby.light.id.generator.provider.RedisProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPool;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author dubby
 * @date 2019/7/29 16:43
 */
public class RedisGenerator extends AbstractGenerator {

    private static final Logger logger = LoggerFactory.getLogger(RedisGenerator.class);

    private RedisProvider[] providers;

    public RedisGenerator(GeneratorConfig config) throws URISyntaxException {
        super(config.getBufferSize());
        providers = new RedisProvider[config.getProviders().size()];
        int index = 0;
        for (GenProviderConfig c : config.getProviders()) {
            JedisPool jedisPool = new JedisPool(new URI(c.getUrl()), config.getTimeout());
            RedisProvider provider = new RedisProvider(c.getId(), jedisPool, c.getNamespace());
            providers[index] = provider;
            ++index;
        }
        doGenerate();
        asyncGenerate();
    }

    @Override
    protected long singleGenerate() {
        int index = ThreadLocalRandom.current().nextInt(0, providers.length);
        long id = providers[index].provide();
        //logger.debug("redis singleGenerate:{}", id);
        return id;
    }

    @Override
    protected long[] batchGenerate() {
        long[] ids = new long[providers.length];
        for (int i = 0; i < providers.length; ++i) {
            ids[i] = providers[i].provide();
        }
        //logger.debug("redis batchGenerate:{}", ids);
        return ids;
    }

}
