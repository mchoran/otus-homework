package ru.otus.java.pro.hw02;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {

        String[] words = {"One", "Two", "Three", "Four", "Five"};
        System.out.println("Исходный массив: " + Arrays.toString(words));

        swapWithCollections(words, 2, 3);
        System.out.println("После swap(2, 3): " + Arrays.toString(words));
        System.out.println();

        ArrayList<String> arrList = toArrayListStream(words);
        System.out.println("ArrayList из массива: " + arrList);
        System.out.println();

        String[] words2 = {"One", "Two", "Three", "Four", "Five", "Three",
                "Six", "Five", "Seven", "Five", "Eight", "Nine",
                "Two", "Three", "Ten", "One", "Two", "One"};

        System.out.println("Исходный массив (" + words2.length + " элементов):");
        System.out.println("  " + Arrays.toString(words2));
        System.out.println();

        Map<String, Integer> occurrences = countOccurrencesGroupingBy(words2);

        System.out.println("Уникальные слова (" + occurrences.size() + " шт.):");
        System.out.println("  " + occurrences.keySet());
        System.out.println();

        System.out.println("Количество вхождений каждого слова:");
        for (Map.Entry<String, Integer> entry : occurrences.entrySet()) {
            System.out.println("  '" + entry.getKey() + "' -> " + entry.getValue() + " раз(а)");
        }
        System.out.println();


        String mostFrequent = occurrences.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
        System.out.println("Самое частое слово: '" + mostFrequent + "'");

        List<String> uniqueOnly = occurrences.entrySet().stream()
                .filter(entry -> entry.getValue() == 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        System.out.println("Слова, встречающиеся только 1 раз: " + uniqueOnly);

        System.out.println("\nСлова, отсортированные по частоте (по убыванию):");
        occurrences.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .forEach(entry -> System.out.println("  " + entry.getKey() + " -> " + entry.getValue()));


        Integer[] numbers = {10, 20, 30, 40, 50};
        System.out.println("Integer массив до swap: " + Arrays.toString(numbers));
        swapWithCollections(numbers, 0, 4);
        System.out.println("После swap(0, 4): " + Arrays.toString(numbers));

        ArrayList<Integer> intList = toArrayListStream(numbers);
        System.out.println("ArrayList<Integer>: " + intList);

        Double[] doubles = {1.1, 2.2, 3.3, 4.4, 5.5};
        System.out.println("Double массив до swap: " + Arrays.toString(doubles));
        swapWithCollections(doubles, 1, 3);
        System.out.println("После swap(1, 3): " + Arrays.toString(doubles));
    }

    public static <T> void swapWithCollections(T[] array, int index1, int index2) {
        if (array == null) {
            throw new IllegalArgumentException("Массив не может быть null");
        }
        if (index1 < 0 || index1 >= array.length || index2 < 0 || index2 >= array.length) {
            throw new IllegalArgumentException("Индексы должны быть в пределах [0, " + (array.length - 1) + "]");
        }
        if (index1 == index2) {
            return;
        }

        List<T> list = Arrays.asList(array);
        Collections.swap(list, index1, index2);
    }

    public static <T> ArrayList<T> toArrayListStream(T[] arr) {
        if (arr == null) {
            return new ArrayList<>();
        }
        return Arrays.stream(arr).collect(Collectors.toCollection(ArrayList::new));
    }

    public static Map<String, Integer> countOccurrencesGroupingBy(String[] arr) {
        if (arr == null) {
            return new HashMap<>();
        }

        return Arrays.stream(arr)
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(word -> !word.isEmpty())
                .collect(Collectors.groupingBy(
                        word -> word,
                        Collectors.summingInt(e -> 1)
                ));
    }
}