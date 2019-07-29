package cn.dubby.light.id.test;

import cn.dubby.light.id.config.GeneratorConfig;
import cn.dubby.light.id.exception.LightInitException;
import cn.dubby.light.id.factory.ConfigFactory;
import cn.dubby.light.id.generator.LightGenerator;
import cn.dubby.light.id.generator.RedisGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    public static void main(String[] args) throws LightInitException, URISyntaxException, InterruptedException {
        ConfigFactory configFactory = new ConfigFactory();
        GeneratorConfig generatorConfig = configFactory.getGeneratorConfig();
        LightGenerator lightGenerator = new RedisGenerator(generatorConfig);

        int threadNum = 20;
        ExecutorService executorService = Executors.newFixedThreadPool(threadNum);
        for (int i = 0; i < threadNum; ++i) {
            executorService.submit(() -> {
                while (true) {
                    long id = lightGenerator.nextID();
                    if (id < 0) {
                        logger.error("error {}", id);
                    }
                    Thread.sleep(1);
                }
            });
        }
        new CountDownLatch(1).await();
    }

}
