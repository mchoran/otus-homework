package ru.otus.java.pro.hw12.dto;

import ru.otus.java.pro.hw10.crm.model.Address;
import ru.otus.java.pro.hw10.crm.model.Client;
import ru.otus.java.pro.hw10.crm.model.Phone;

import java.util.List;

public record ClientDto(Long id, String name, String address, List<String> phones) {

    public static ClientDto from(Client client) {
        String street = client.getAddress() == null ? "" : client.getAddress().getStreet();
        List<String> numbers = client.getPhones().stream()
                .map(Phone::getNumber)
                .toList();
        return new ClientDto(client.getId(), client.getName(), street, numbers);
    }

    public Client toClient() {
        if (name == null || name.isBlank() || address == null || address.isBlank()) {
            throw new IllegalArgumentException("Имя и адрес обязательны");
        }
        List<Phone> phoneList = phones == null
                ? List.of()
                : phones.stream()
                .filter(number -> number != null && !number.isBlank())
                .map(number -> new Phone(number.trim()))
                .toList();
        return new Client(name.trim(), new Address(address.trim()), phoneList);
    }
}
