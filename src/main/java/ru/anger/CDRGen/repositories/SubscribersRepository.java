package ru.anger.CDRGen.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.anger.CDRGen.entity.Subscriber;

@Repository
public interface SubscribersRepository extends JpaRepository<Subscriber, Long> {
}