package cn.dubby.light.id.config;

import cn.dubby.light.id.enums.GeneratorType;

import java.util.Set;

/**
 * @author dubby
 * @date 2019/7/29 16:19
 */
public class GeneratorConfig {

    private GeneratorType type;

    private Set<GenProviderConfig> providers;

    private int bufferSize;

    private int timeout;

    public int getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    public GeneratorType getType() {
        return type;
    }

    public void setType(GeneratorType type) {
        this.type = type;
    }

    public Set<GenProviderConfig> getProviders() {
        return providers;
    }

    public void setProviders(Set<GenProviderConfig> providers) {
        this.providers = providers;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}
