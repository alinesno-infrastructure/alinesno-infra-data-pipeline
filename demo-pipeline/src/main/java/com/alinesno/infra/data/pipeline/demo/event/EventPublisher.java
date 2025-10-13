package com.alinesno.infra.data.pipeline.demo.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * 简单的事件发布器（轻量级的 pub-sub）
 * - 支持 addListener/removeListener
 * - 支持 publish (同步) 与 publishAsync (异步)
 * - 内部使用 CopyOnWriteArrayList 保证并发安全
 */
public final class EventPublisher {
    private static final Logger logger = LoggerFactory.getLogger(EventPublisher.class);

    // 监听器集合，线程安全
    private static final List<Consumer<DataPipelineEvent>> listeners = new CopyOnWriteArrayList<>();

    // 异步执行器（可根据需要替换为共享线程池）
    private static final ExecutorService executor = Executors.newCachedThreadPool(r -> {
        Thread t = new Thread(r);
        t.setName("event-publisher-worker-" + t.getId());
        t.setDaemon(true);
        return t;
    });

    private EventPublisher() {
        // no instance
    }

    /**
     * 注册监听器
     * 示例： EventPublisher.addListener(evt -> { ... });
     */
    public static void addListener(Consumer<DataPipelineEvent> listener) {
        Objects.requireNonNull(listener, "listener must not be null");
        listeners.add(listener);
        logger.debug("Event listener added: {}", listener);
    }

    /**
     * 移除监听器
     */
    public static void removeListener(Consumer<DataPipelineEvent> listener) {
        listeners.remove(listener);
        logger.debug("Event listener removed: {}", listener);
    }

    /**
     * 清除所有监听器（谨慎调用）
     */
    public static void clearListeners() {
        listeners.clear();
        logger.debug("All event listeners cleared");
    }

    /**
     * 同步发布事件：在当前线程依次调用所有监听器。
     * 监听器异常会被捕获并记录，不影响其他监听器执行。
     */
    public static void publish(DataPipelineEvent event) {
        if (event == null) return;
        for (Consumer<DataPipelineEvent> listener : listeners) {
            try {
                listener.accept(event);
            } catch (Throwable t) {
                logger.error("Event listener threw exception for event {}: {}", event.getEventType(), t.getMessage(), t);
            }
        }
    }

    /**
     * 异步发布事件：使用线程池异步调用监听器（每个监听器在独立线程中执行）。
     * 注意：如果监听器对执行顺序有依赖，请使用同步 publish。
     */
    public static void publishAsync(DataPipelineEvent event) {
        if (event == null) return;
        for (Consumer<DataPipelineEvent> listener : listeners) {
            executor.submit(() -> {
                try {
                    listener.accept(event);
                } catch (Throwable t) {
                    logger.error("Async event listener threw exception for event {}: {}", event.getEventType(), t.getMessage(), t);
                }
            });
        }
    }

    /**
     * 关闭内部线程池（应用关闭时调用以释放资源）
     */
    public static void shutdown() {
        try {
            executor.shutdownNow();
        } catch (Throwable t) {
            logger.warn("Failed to shutdown event publisher executor: {}", t.getMessage(), t);
        }
    }
}