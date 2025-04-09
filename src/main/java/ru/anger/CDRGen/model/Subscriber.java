package ru.anger.CDRGen.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "Subscriber")
public class Subscriber {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "msisdn")
    private String msisdn;

    Subscriber() {}

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
