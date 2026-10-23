package ru.otus.java.pro.hw12.helpers;

import org.eclipse.jetty.util.resource.Resource;

import java.net.URL;

public final class FileSystemHelper {

    private FileSystemHelper() {
    }

    public static Resource classpathResource(String resourceName) {
        URL url = FileSystemHelper.class.getClassLoader().getResource(resourceName);
        if (url == null) {
            throw new IllegalStateException("Resource not found: " + resourceName);
        }
        return Resource.newResource(url);
    }
}
