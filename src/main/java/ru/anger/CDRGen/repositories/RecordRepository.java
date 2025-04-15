package ru.anger.CDRGen.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.anger.CDRGen.entity.Record;

@Repository
public interface RecordRepository extends JpaRepository<Record, Integer> {
}