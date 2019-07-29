package cn.dubby.light.id.factory;

import cn.dubby.light.id.config.GeneratorConfig;
import cn.dubby.light.id.exception.LightInitException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author dubby
 * @date 2019/7/29 16:25
 */
public class ConfigFactory {

    private static final String DEFAULT_CONFIG_FILE = "light.json";

    private GeneratorConfig generatorConfig;

    public ConfigFactory(String json) throws LightInitException {
        try {
            init(json);
        } catch (IOException e) {
            throw new LightInitException(e);
        }
    }

    public ConfigFactory() throws LightInitException {
        try (BufferedReader reader =
                     new BufferedReader(new InputStreamReader(ConfigFactory.class.getClassLoader().getResourceAsStream(DEFAULT_CONFIG_FILE)))) {
            String json = reader.lines().reduce((a, b) -> a + b).orElseThrow(() -> new IllegalStateException("load config error"));
            init(json);
        } catch (Exception e) {
            throw new LightInitException(e);
        }
    }

    private void init(String json) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        generatorConfig = objectMapper.readValue(json, GeneratorConfig.class);
    }

    public GeneratorConfig getGeneratorConfig() {
        return generatorConfig;
    }
}
