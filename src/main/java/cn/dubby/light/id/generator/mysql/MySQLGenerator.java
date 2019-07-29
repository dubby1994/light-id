package cn.dubby.light.id.generator.mysql;

import cn.dubby.light.id.config.GenProviderConfig;
import cn.dubby.light.id.config.GeneratorConfig;
import cn.dubby.light.id.generator.AbstractLightIDGenerator;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;
import java.sql.Connection;

public class MySQLGenerator extends AbstractLightIDGenerator {

    private static final Logger logger = LoggerFactory.getLogger(MySQLGenerator.class);

    private MySQLProvider[] providers;

    public MySQLGenerator(GeneratorConfig config) throws URISyntaxException {
        super(config.getBufferSize(), config.getIdlePercent(), config.getCheckInterval());
        providers = new MySQLProvider[config.getProviders().size()];
        int index = 0;
        for (GenProviderConfig c : config.getProviders()) {
            ConnPoolFactory connPoolFactory = new ConnPoolFactory(c.getUrl(), c.getOptions().get("username"), c.getOptions().get("password"), config.getTimeout());
            GenericObjectPool<Connection> connPoll = new GenericObjectPool<Connection>(connPoolFactory);
            MySQLProvider provider = new MySQLProvider(c.getId(), connPoll, c.getNamespace());
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
            for (MySQLProvider provider : providers) {
                long id = provider.provide();
                if (id > 0) {
                    transferQueue.offer(id);
                }
            }
        }
        logger.info("transferQueue.size:{}", transferQueue.size());
    }
}
