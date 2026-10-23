package ru.otus.java.pro.hw11.cachehw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class HWCacheDemo {

    private static final Logger logger = LoggerFactory.getLogger(HWCacheDemo.class);

    public static void main(String[] args) throws InterruptedException {
        new HWCacheDemo().demo();
    }

    private void demo() throws InterruptedException {
        HwCache<String, Integer> cache = new MyCache<>();

        @SuppressWarnings("java:S1604")
        HwListener<String, Integer> listener = new HwListener<String, Integer>() {
            @Override
            public void notify(String key, Integer value, String action) {
                logger.info("key:{}, value:{}, action: {}", key, value, action);
            }
        };

        cache.addListener(listener);
        cache.put("1", 1);

        logger.info("getValue:{}", cache.get("1"));
        cache.remove("1");
        cache.removeListener(listener);

        demonstrateEviction();
    }

    private void demonstrateEviction() throws InterruptedException {
        MyCache<Object, byte[]> cache = new MyCache<>();
        List<Object> keys = new ArrayList<>();
        for (int i = 0; i < 200; i++) {
            Object key = new Object();
            keys.add(key);
            cache.put(key, new byte[256 * 1024]);
        }
        logger.info("cache size while keys are referenced: {}", cache.size());

        keys.clear();
        System.gc();
        Thread.sleep(200);
        logger.info("cache size after GC: {}", cache.size());
    }
}
