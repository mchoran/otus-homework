package ru.otus.java.pro.hw13.multiconfig;

import ru.otus.java.pro.hw13.appcontainer.api.AppComponent;
import ru.otus.java.pro.hw13.appcontainer.api.AppComponentsContainerConfig;
import ru.otus.java.pro.hw13.services.EquationPreparer;
import ru.otus.java.pro.hw13.services.GameProcessor;
import ru.otus.java.pro.hw13.services.GameProcessorImpl;
import ru.otus.java.pro.hw13.services.IOService;
import ru.otus.java.pro.hw13.services.PlayerService;
import ru.otus.java.pro.hw13.services.PlayerServiceImpl;

@AppComponentsContainerConfig(order = 2)
public class AppConfig2 {

    @AppComponent(order = 1, name = "playerService")
    public PlayerService playerService(IOService ioService) {
        return new PlayerServiceImpl(ioService);
    }

    @AppComponent(order = 2, name = "gameProcessor")
    public GameProcessor gameProcessor(IOService ioService,
                                       PlayerService playerService,
                                       EquationPreparer equationPreparer) {
        return new GameProcessorImpl(ioService, equationPreparer, playerService);
    }
}
