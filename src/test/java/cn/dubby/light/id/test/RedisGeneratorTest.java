package cn.dubby.light.id.test;

import cn.dubby.light.id.config.GeneratorConfig;
import cn.dubby.light.id.exception.LightInitException;
import cn.dubby.light.id.factory.ConfigFactory;
import cn.dubby.light.id.generator.LightIDGenerator;
import cn.dubby.light.id.generator.redis.RedisGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author dubby
 * @date 2019/7/29 19:40
 */
public class RedisGeneratorTest {

    private static final Logger logger = LoggerFactory.getLogger(RedisGeneratorTest.class);

    public static void main(String[] args) throws LightInitException, InterruptedException, IOException, URISyntaxException {
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(ConfigFactory.class.getClassLoader().getResourceAsStream("light_redis.json")));
        String json = reader.lines().reduce((a, b) -> a + b).orElseThrow(() -> new IllegalStateException("load config error"));
        reader.close();
        ConfigFactory configFactory = new ConfigFactory(json);
        GeneratorConfig generatorConfig = configFactory.getGeneratorConfig();
        LightIDGenerator lightGenerator = new RedisGenerator(generatorConfig);

        int threadNum = 20;
        ExecutorService executorService = Executors.newFixedThreadPool(threadNum);
        for (int i = 0; i < threadNum; ++i) {
            executorService.submit(() -> {
                while (true) {
                    long id = lightGenerator.nextID();
                    logger.info("id:{}", id);
                    //Thread.sleep(1);
                }
            });
        }
        new CountDownLatch(1).await();
    }

}
