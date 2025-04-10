package ru.anger.CDRGen.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.anger.CDRGen.model.Subscriber;
import ru.anger.CDRGen.repositories.RecordRepository;
import ru.anger.CDRGen.repositories.SubscribersRepository;

@Service
public class SubscribersService {
    @Autowired
    private SubscribersRepository subscribersRepository;

    public void fillSubscribers() {
        subscribersRepository.save(new Subscriber("+79111234567"));
        subscribersRepository.save(new Subscriber("+79111234568"));
        subscribersRepository.save(new Subscriber("+79111234569"));
        subscribersRepository.save(new Subscriber("+79111234570"));
        subscribersRepository.save(new Subscriber("+79111234571"));
        subscribersRepository.save(new Subscriber("+79111234572"));
        subscribersRepository.save(new Subscriber("+79111234573"));
        subscribersRepository.save(new Subscriber("+79111234574"));
        subscribersRepository.save(new Subscriber("+79111234575"));
        subscribersRepository.save(new Subscriber("+79111234576"));
        subscribersRepository.save(new Subscriber("+79111234577"));
        subscribersRepository.save(new Subscriber("+79111234578"));
        subscribersRepository.save(new Subscriber("+79111234579"));
        subscribersRepository.save(new Subscriber("+79111234580"));
        subscribersRepository.save(new Subscriber("+79111234581"));
    }
}
