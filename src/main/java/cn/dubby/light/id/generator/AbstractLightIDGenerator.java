package cn.dubby.light.id.generator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TransferQueue;

/**
 * @author dubby
 * @date 2019/7/29 16:44
 */
public abstract class AbstractLightIDGenerator implements LightIDGenerator {

    private static final Logger logger = LoggerFactory.getLogger(AbstractLightIDGenerator.class);

    private TransferQueue<Long> transferQueue = new LinkedTransferQueue<>();

    private ScheduledExecutorService generatorThread = Executors.newScheduledThreadPool(1, r -> {
        Thread thread = new Thread(r);
        thread.setDaemon(true);
        thread.setName("lightGeneratorThread");
        return thread;
    });

    private int bufferSize;

    private int idlePercent;

    private int checkInterval;

    public AbstractLightIDGenerator(int bufferSize, int idlePercent, int checkInterval) {
        this.bufferSize = bufferSize;
        this.idlePercent = idlePercent;
        this.checkInterval = checkInterval;
    }

    protected void startAsyncGenerate() {
        try {
            generatorThread.scheduleWithFixedDelay(() -> {
                if (!needGenerate()) {
                    return;
                }
                fillCache();
            }, checkInterval, checkInterval, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            logger.error("asyncGenerate", e);
        }
    }

    private boolean needGenerate() {
        return bufferSize - transferQueue.size() > bufferSize / idlePercent;
    }

    protected void fillCache() {
        logger.info("transferQueue.size:{}", transferQueue.size());
        while (transferQueue.size() < bufferSize) {
            long[] ids = batchGenerate();
            for (long id : ids) {
                if (id > 0) {
                    transferQueue.offer(id);
                }
            }
        }
        logger.info("transferQueue.size:{}", transferQueue.size());
    }

    protected abstract long singleGenerate();

    protected abstract long[] batchGenerate();

    @Override
    public long nextID() {
        Long id = transferQueue.poll();
        if (id == null) {
            return singleGenerate();
        }
        return id;
    }


}
