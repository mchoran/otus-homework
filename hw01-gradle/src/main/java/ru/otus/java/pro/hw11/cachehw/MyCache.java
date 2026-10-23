package ru.otus.java.pro.hw11.cachehw;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class MyCache<K, V> implements HwCache<K, V> {

    private final Map<K, V> cache = new WeakHashMap<>();
    private final List<HwListener<K, V>> listeners = new ArrayList<>();

    @Override
    public void put(K key, V value) {
        synchronized (cache) {
            cache.put(key, value);
        }
        notifyListeners(key, value, "put");
    }

    @Override
    public void remove(K key) {
        V removed;
        synchronized (cache) {
            removed = cache.remove(key);
        }
        notifyListeners(key, removed, "remove");
    }

    @Override
    public V get(K key) {
        synchronized (cache) {
            return cache.get(key);
        }
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        synchronized (listeners) {
            listeners.remove(listener);
        }
    }

    public int size() {
        synchronized (cache) {
            return cache.size();
        }
    }

    private void notifyListeners(K key, V value, String action) {
        List<HwListener<K, V>> snapshot;
        synchronized (listeners) {
            snapshot = List.copyOf(listeners);
        }
        snapshot.forEach(listener -> listener.notify(key, value, action));
    }
}
