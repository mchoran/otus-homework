package ru.otus.java.pro.hw09.crm.model;

import ru.otus.java.pro.hw09.mapper.Id;

import java.util.Objects;

public class Manager {

    @Id
    private Long no;
    private String label;

    public Manager(Long no, String label) {
        this.no = no;
        this.label = label;
    }

    public Manager(String label) {
        this(null, label);
    }

    public Long getNo() {
        return no;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Manager manager)) {
            return false;
        }
        return Objects.equals(no, manager.no) && Objects.equals(label, manager.label);
    }

    @Override
    public int hashCode() {
        return Objects.hash(no, label);
    }

    @Override
    public String toString() {
        return "Manager{no=" + no + ", label='" + label + "'}";
    }
}
