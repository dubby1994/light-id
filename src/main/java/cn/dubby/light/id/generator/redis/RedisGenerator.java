package cn.dubby.light.id.generator.redis;

import cn.dubby.light.id.config.GenProviderConfig;
import cn.dubby.light.id.config.GeneratorConfig;
import cn.dubby.light.id.generator.AbstractLightIDGenerator;
import cn.dubby.light.id.generator.IDProvider;
import redis.clients.jedis.Jedis;

/**
 * @author dubby
 * @date 2019/7/29 16:43
 */
public class RedisGenerator extends AbstractLightIDGenerator {

    private RedisProvider[] providers;

    public RedisGenerator(GeneratorConfig config) {
        super(config.getBufferSize(), config.getIdleSize(), config.getCheckInterval());
        providers = new RedisProvider[config.getProviders().size()];
        int index = 0;
        for (GenProviderConfig c : config.getProviders()) {
            RedisProvider provider = new RedisProvider(c.getId(), new Jedis(c.getUrl()), c.getNamespace());
            providers[index] = provider;
            ++index;
        }
        startAsyncGenerate();
    }

    @Override
    protected IDProvider[] getIDProvider() {
        return providers;
    }
}
