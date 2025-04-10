package ru.anger.CDRGen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import ru.anger.CDRGen.repositories.SubscribersRepository;
import ru.anger.CDRGen.service.SubscribersService;

@Controller
public class SubscriberController {

    @Autowired
    private SubscribersRepository subscribersRepository;

    @Autowired
    private SubscribersService subscribersService;

    @PostMapping("/fillSubscribers")
    public ResponseEntity<String> deleteCdrTable() {
        subscribersService.fillSubscribers();
        return ResponseEntity.ok("Subscriber table filled!");
    }
}