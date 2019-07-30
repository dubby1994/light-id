package cn.dubby.light.id.test;

import cn.dubby.light.id.config.GeneratorConfig;
import cn.dubby.light.id.exception.LightInitException;
import cn.dubby.light.id.factory.ConfigFactory;
import cn.dubby.light.id.generator.LightIDGenerator;
import cn.dubby.light.id.generator.mysql.MySQLGenerator;
import cn.dubby.light.id.generator.redis.RedisGenerator;
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
public class MySQLGeneratorTest {

    private static final Logger logger = LoggerFactory.getLogger(MySQLGeneratorTest.class);

    public static void main(String[] args) throws LightInitException, URISyntaxException, InterruptedException {
        ConfigFactory configFactory = new ConfigFactory();
        GeneratorConfig generatorConfig = configFactory.getGeneratorConfig();
        LightIDGenerator lightGenerator = new MySQLGenerator(generatorConfig);

        int threadNum = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(threadNum);
        for (int i = 0; i < threadNum; ++i) {
            executorService.submit(() -> {
                while (true) {
                    try {
                        long id = lightGenerator.nextID();
                        logger.info("id:{}", id);
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        logger.error("id", e);
                    }
                }
            });
        }
        new CountDownLatch(1).await();
    }

}
