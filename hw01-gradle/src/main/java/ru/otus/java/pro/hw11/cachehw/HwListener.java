package ru.otus.java.pro.hw11.cachehw;

public interface HwListener<K, V> {

    void notify(K key, V value, String action);
}
