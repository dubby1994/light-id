package cn.dubby.light.id.test;

import cn.dubby.light.id.config.GeneratorConfig;
import cn.dubby.light.id.exception.LightInitException;
import cn.dubby.light.id.factory.ConfigFactory;
import cn.dubby.light.id.generator.LightIDGenerator;
import cn.dubby.light.id.generator.mysql.MySQLGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author dubby
 * @date 2019/7/29 19:40
 */
public class MySQLGeneratorTest {

    private static final Logger logger = LoggerFactory.getLogger(MySQLGeneratorTest.class);

    public static void main(String[] args) throws LightInitException, InterruptedException, SQLException {
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
                        if (id < 0) {
                            logger.info("id:{}", id);
                        }
                        //Thread.sleep(100);
                    } catch (Exception e) {
                        logger.error("id", e);
                    }
                }
            });
        }
        new CountDownLatch(1).await();
    }

}
