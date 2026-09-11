package ru.otus.java.pro.hw08;

import ru.otus.java.pro.hw08.dataprocessor.FileSerializer;
import ru.otus.java.pro.hw08.dataprocessor.ProcessorAggregator;
import ru.otus.java.pro.hw08.dataprocessor.ResourcesFileLoader;

public class Demo {

    public static void main(String[] args) {
        var loader = new ResourcesFileLoader("inputData.json");
        var processor = new ProcessorAggregator();
        var serializer = new FileSerializer("outputData.json");

        var loadedMeasurements = loader.load();
        var aggregatedMeasurements = processor.process(loadedMeasurements);
        serializer.serialize(aggregatedMeasurements);

        System.out.println("Прочитано измерений: " + loadedMeasurements.size());
        System.out.println("Агрегированный результат: " + aggregatedMeasurements);
        System.out.println("Ответ записан в outputData.json");
    }
}
