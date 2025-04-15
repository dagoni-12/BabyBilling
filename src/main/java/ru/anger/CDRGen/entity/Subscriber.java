package ru.anger.CDRGen.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Subscriber")
public class Subscriber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String msisdn;

    public Subscriber() {
    }

    public Subscriber(String msisdn) {
        this.msisdn = msisdn;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }
}
