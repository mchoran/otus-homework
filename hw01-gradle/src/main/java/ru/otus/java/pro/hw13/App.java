package ru.otus.java.pro.hw13;

import ru.otus.java.pro.hw13.appcontainer.AppComponentsContainerImpl;
import ru.otus.java.pro.hw13.appcontainer.api.AppComponentsContainer;
import ru.otus.java.pro.hw13.config.AppConfig;
import ru.otus.java.pro.hw13.services.GameProcessor;
import ru.otus.java.pro.hw13.services.GameProcessorImpl;


public class App {

    public static void main(String[] args) throws Exception {
        // Опциональные варианты
        //AppComponentsContainer container = new AppComponentsContainerImpl(
        //        ru.otus.java.pro.hw13.multiconfig.AppConfig1.class,
        //        ru.otus.java.pro.hw13.multiconfig.AppConfig2.class);

        //AppComponentsContainer container = new AppComponentsContainerImpl("ru.otus.java.pro.hw13.config");
        //AppComponentsContainer container = new AppComponentsContainerImpl("ru.otus.java.pro.hw13.multiconfig");

        AppComponentsContainer container = new AppComponentsContainerImpl(AppConfig.class);

        GameProcessor gameProcessor = container.getAppComponent(GameProcessor.class);
        //GameProcessor gameProcessor = container.getAppComponent(GameProcessorImpl.class);
        //GameProcessor gameProcessor = container.getAppComponent("gameProcessor");

        gameProcessor.startGame();
    }
}
