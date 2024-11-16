package com.local.test.concurrency;

public class Datum {
    private int id;
    private int value;

    public Datum(int id, int value) {
        this.id = id;
        this.value = value;
    }

    public Datum(Datum datum) {
        this(datum.getId(), datum.getValue());
    }

    public int getId() {
        return id;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
