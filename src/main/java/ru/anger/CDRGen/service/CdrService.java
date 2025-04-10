package ru.anger.CDRGen.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.anger.CDRGen.repositories.RecordRepository;

@Service
public class CdrService {
    @Autowired
    private RecordRepository recordRepository;

    public void deleteCdrTable() {
        recordRepository.deleteAll();
    }
}
