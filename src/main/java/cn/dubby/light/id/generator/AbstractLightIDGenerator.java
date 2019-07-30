package cn.dubby.light.id.generator;

import cn.dubby.light.id.exception.LightIDGenerateException;
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

    protected TransferQueue<Long> transferQueue = new LinkedTransferQueue<>();

    private ScheduledExecutorService generatorThread = Executors.newScheduledThreadPool(2, r -> {
        Thread thread = new Thread(r);
        thread.setDaemon(true);
        thread.setName("lightGeneratorThread");
        return thread;
    });

    protected volatile int bufferSize;

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

    protected abstract void fillCache();

    @Override
    public long nextID() {
        Long id = transferQueue.poll();
        if (id == null) {
            bufferSize = bufferSize * 2;
            id = transferQueue.poll();
            if (id == null) {
                throw new LightIDGenerateException();
            }
            return id;
        }
        return id;
    }


}
