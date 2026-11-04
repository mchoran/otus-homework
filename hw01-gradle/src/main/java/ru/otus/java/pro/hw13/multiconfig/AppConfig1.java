package ru.otus.java.pro.hw13.multiconfig;

import ru.otus.java.pro.hw13.appcontainer.api.AppComponent;
import ru.otus.java.pro.hw13.appcontainer.api.AppComponentsContainerConfig;
import ru.otus.java.pro.hw13.services.EquationPreparer;
import ru.otus.java.pro.hw13.services.EquationPreparerImpl;
import ru.otus.java.pro.hw13.services.IOService;
import ru.otus.java.pro.hw13.services.IOServiceStreams;

@AppComponentsContainerConfig(order = 1)
public class AppConfig1 {

    @AppComponent(order = 0, name = "equationPreparer")
    public EquationPreparer equationPreparer() {
        return new EquationPreparerImpl();
    }

    @AppComponent(order = 0, name = "ioService")
    public IOService ioService() {
        return new IOServiceStreams(System.out, System.in);
    }
}
