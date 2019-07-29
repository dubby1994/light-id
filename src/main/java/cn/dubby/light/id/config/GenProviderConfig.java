package cn.dubby.light.id.config;

import java.util.Map;
import java.util.Objects;

/**
 * @author dubby
 * @date 2019/7/29 16:23
 */
public class GenProviderConfig {

    private int id;

    private String url;

    private String namespace;

    private Map<String, String> options;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public Map<String, String> getOptions() {
        return options;
    }

    public void setOptions(Map<String, String> options) {
        this.options = options;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GenProviderConfig genUnit = (GenProviderConfig) o;
        return id == genUnit.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
