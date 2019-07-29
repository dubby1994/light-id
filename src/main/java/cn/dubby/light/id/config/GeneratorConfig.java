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

    private int bufferSize = 1000;

    private int timeout = 20;

    private int idlePercent = 10;

    private int checkInterval = 10;

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

    public int getIdlePercent() {
        return idlePercent;
    }

    public void setIdlePercent(int idlePercent) {
        this.idlePercent = idlePercent;
    }

    public int getCheckInterval() {
        return checkInterval;
    }

    public void setCheckInterval(int checkInterval) {
        this.checkInterval = checkInterval;
    }
}
