package ru.anger.CDRGen.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
public class Record implements Serializable{
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "callType")
    private String callType;

    @Column(name = "caller")
    private String caller;

    @Column(name = "receiver")
    private String receiver;

    @Column(name = "startTime")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime startTime;

    @Column(name = "endTime")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime endTime;

    public Record() {}

    public Record(String callType, String caller, String receiver, LocalDateTime startTime, LocalDateTime endTime) {
        this.callType = callType;
        this.caller = caller;
        this.receiver = receiver;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getCaller() {
        return caller;
    }

    public void setCaller(String caller) {
        this.caller = caller;
    }

    public String getCallType() {
        return callType;
    }

    public void setCallType(String callType) {
        this.callType = callType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Record{" +
                "id=" + id +
                ", callType='" + callType + '\'' +
                ", caller='" + caller + '\'' +
                ", receiver='" + receiver + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
