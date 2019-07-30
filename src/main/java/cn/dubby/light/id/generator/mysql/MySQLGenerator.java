package cn.dubby.light.id.generator.mysql;

import cn.dubby.light.id.config.GenProviderConfig;
import cn.dubby.light.id.config.GeneratorConfig;
import cn.dubby.light.id.generator.AbstractLightIDGenerator;
import cn.dubby.light.id.generator.IDProvider;

import java.sql.SQLException;

public class MySQLGenerator extends AbstractLightIDGenerator {

    private MySQLProvider[] providers;

    public MySQLGenerator(GeneratorConfig config) throws SQLException {
        super(config.getBufferSize(), config.getIdleSize(), config.getCheckInterval());
        providers = new MySQLProvider[config.getProviders().size()];
        int index = 0;
        for (GenProviderConfig c : config.getProviders()) {
            MySQLProvider provider = new MySQLProvider(c);
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
