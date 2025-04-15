package ru.anger.CDRGen.service;

import ru.anger.CDRGen.entity.Record;

import java.util.List;

public interface MessageSender {
    void send(List<Record> cdr);
}
