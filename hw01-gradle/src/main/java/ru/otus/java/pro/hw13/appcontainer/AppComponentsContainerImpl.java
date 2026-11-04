package ru.otus.java.pro.hw13.appcontainer;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import ru.otus.java.pro.hw13.appcontainer.api.AppComponent;
import ru.otus.java.pro.hw13.appcontainer.api.AppComponentsContainer;
import ru.otus.java.pro.hw13.appcontainer.api.AppComponentsContainerConfig;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        this(new Class<?>[]{initialConfigClass});
    }

    public AppComponentsContainerImpl(Class<?>... initialConfigClasses) {
        if (initialConfigClasses == null || initialConfigClasses.length == 0) {
            throw new IllegalArgumentException("Config classes are required");
        }
        Arrays.stream(initialConfigClasses)
                .sorted(Comparator.comparingInt(this::configOrder))
                .forEach(this::processConfig);
    }

    public AppComponentsContainerImpl(String packageName) {
        this(findConfigClasses(packageName));
    }

    private static Class<?>[] findConfigClasses(String packageName) {
        if (packageName == null || packageName.isBlank()) {
            throw new IllegalArgumentException("Package name is required");
        }
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader == null) {
            classLoader = AppComponentsContainerImpl.class.getClassLoader();
        }
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setClassLoaders(new ClassLoader[]{classLoader})
                .setUrls(ClasspathHelper.forPackage(packageName, classLoader))
                .filterInputsBy(new FilterBuilder().includePackage(packageName))
                .setScanners(Scanners.TypesAnnotated));
        Class<?>[] configClasses = reflections.getTypesAnnotatedWith(AppComponentsContainerConfig.class)
                .toArray(Class<?>[]::new);
        if (configClasses.length == 0) {
            throw new IllegalArgumentException("No config classes found in package " + packageName);
        }
        return configClasses;
    }

    private int configOrder(Class<?> configClass) {
        checkConfigClass(configClass);
        return configClass.getAnnotation(AppComponentsContainerConfig.class).order();
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);
        Object configInstance = createConfigInstance(configClass);
        Arrays.stream(configClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(AppComponent.class))
                .sorted(Comparator.comparingInt(method -> method.getAnnotation(AppComponent.class).order()))
                .forEach(method -> registerComponent(configInstance, method));
    }

    private Object createConfigInstance(Class<?> configClass) {
        try {
            var constructor = configClass.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Cannot instantiate config " + configClass.getName(), e);
        }
    }

    private void registerComponent(Object configInstance, Method method) {
        Object[] args = Arrays.stream(method.getParameterTypes())
                .map(this::getAppComponent)
                .toArray();
        try {
            method.setAccessible(true);
            Object component = method.invoke(configInstance, args);
            if (component == null) {
                throw new IllegalStateException("Component method returned null: " + method.getName());
            }
            String name = method.getAnnotation(AppComponent.class).name();
            if (appComponentsByName.containsKey(name)) {
                throw new IllegalArgumentException("Duplicate component name: " + name);
            }
            appComponentsByName.put(name, component);
            appComponents.add(component);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Failed to create component " + method.getName(), e);
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof RuntimeException runtimeException) {
                throw runtimeException;
            }
            throw new IllegalStateException("Failed to create component " + method.getName(), e);
        }
    }

    private void checkConfigClass(Class<?> configClass) {
        if (configClass == null || !configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            String name = configClass == null ? "null" : configClass.getName();
            throw new IllegalArgumentException(String.format("Given class is not config %s", name));
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        List<Object> found = appComponents.stream()
                .filter(component -> componentClass.isAssignableFrom(component.getClass()))
                .toList();
        if (found.size() != 1) {
            throw new IllegalArgumentException(String.format(
                    "Expected exactly one component of type %s, found %d",
                    componentClass.getName(),
                    found.size()));
        }
        return componentClass.cast(found.get(0));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C> C getAppComponent(String componentName) {
        Object component = appComponentsByName.get(componentName);
        if (component == null) {
            throw new IllegalArgumentException("Component not found: " + componentName);
        }
        return (C) component;
    }
}
