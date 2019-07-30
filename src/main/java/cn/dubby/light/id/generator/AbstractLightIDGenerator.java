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

    private TransferQueue<Long> transferQueue = new LinkedTransferQueue<>();

    private ScheduledExecutorService generatorThread = Executors.newSingleThreadScheduledExecutor(r -> {
        Thread thread = new Thread(r);
        thread.setDaemon(true);
        thread.setName("lightGeneratorThread");
        return thread;
    });

    private int bufferSize;

    private int idleSize;

    private int checkInterval;

    protected abstract IDProvider[] getIDProvider();

    public AbstractLightIDGenerator(int bufferSize, int idleSize, int checkInterval) {
        this.bufferSize = bufferSize;
        this.idleSize = idleSize;
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
        return bufferSize - transferQueue.size() >= idleSize;
    }

    private void fillCache() {
        while (transferQueue.size() < bufferSize) {
            for (IDProvider provider : getIDProvider()) {
                // TODO: 2019/7/30 理论上每个下游数据源都是独立的，所以这里改成并发请求
                // TODO: 2019/7/30 对于下游数据源不可用，这里可以添加健康监测，对于故障节点可以跳过
                long id = provider.provide();
                if (id > 0) {
                    transferQueue.offer(id);
                    logger.info("transferQueue.size:{}", transferQueue.size());
                }
            }
        }
    }

    @Override
    public long nextID() {
        try {
            return transferQueue.take();
        } catch (InterruptedException e) {
            throw new LightIDGenerateException();
        }
    }

}
