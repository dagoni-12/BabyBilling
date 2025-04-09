package ru.anger.CDRGen.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import ru.anger.CDRGen.model.Record;
import ru.anger.CDRGen.model.Subscriber;
import ru.anger.CDRGen.repositories.SubscribersRepository;
import ru.anger.CDRGen.repositories.RecordRepository;

import java.time.LocalDateTime;

import java.util.*;
import java.util.concurrent.*;

@Service
public class CdrGeneratorService {

    @Autowired
    private SubscribersRepository subscriberRepository;
    @Autowired
    private RecordRepository recordRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate = new RabbitTemplate();
    @Value("${queue.name}")
    private String queueName;

    private final Random random = new Random();
    private final Queue<Record> cdrBuffer = new ConcurrentLinkedQueue<>();

    private LocalDateTime currentStartTime = LocalDateTime.of(2024, 1, 1, 0, 0);
    private LocalDateTime endOfYear = currentStartTime.plusYears(1);

    private final Object generationLock = new Object();

    @Async
    public void generateRecords(int min, int max) {
        List<Subscriber> subscribers = subscriberRepository.findAll();
        checkAndResetYear();

        int targetCount = min + random.nextInt(max - min + 1);
        System.out.println(targetCount);
        int generated = 0;

        while (generated < targetCount && currentStartTime.isBefore(endOfYear)) {
            System.out.println("-----------------------------------GENERATING NEW RECORDS-----------------------------------");

            LocalDateTime startTime = currentStartTime;
            int duration = random.nextInt(3600);
            LocalDateTime endTime = startTime.plusSeconds(duration);

            LocalDateTime midnight = startTime.toLocalDate().plusDays(1).atStartOfDay();

            if (endTime.isAfter(midnight)) {
                Record record1 = createRandomRecord(subscribers, startTime, midnight);
                Record record2 = createRandomRecord(subscribers, midnight, endTime);
                recordRepository.save(record1);
                recordRepository.save(record2);
                cdrBuffer.add(record1);
                checkAndSendCdr();
                cdrBuffer.add(record2);
                generated += 2;
                updateCurrentTime(record2);
                currentStartTime = record2.getStartTime().plusSeconds(random.nextInt(3000));
            } else {
                Record record = createRandomRecord(subscribers, startTime, endTime);
                recordRepository.save(record);
                cdrBuffer.add(record);
                generated += 1;
                updateCurrentTime(record);
                currentStartTime = record.getStartTime().plusSeconds(random.nextInt(3000));
            }
            checkAndResetYear();
            checkAndSendCdr();
        }
        sendRemainingCdrs();
    }

    private Record createRandomRecord(List<Subscriber> subscribers, LocalDateTime startTime, LocalDateTime endTime) {
        Subscriber caller = getRandomSubscriber(subscribers, null);
        Subscriber receiver = getRandomSubscriber(subscribers, caller);

        Record record = new Record();
        record.setCallType(random.nextBoolean() ? "01" : "02");
        record.setCaller(caller.getMsisdn());
        record.setReceiver(receiver.getMsisdn());
        record.setStartTime(startTime);
        record.setEndTime(endTime);
        return record;
    }

    private void checkAndSendCdr() {
        if (cdrBuffer.size() >= 10) {
            sendCdr(createCdr());
        }
    }

    private void sendRemainingCdrs() {
        if (!cdrBuffer.isEmpty()) {
            sendCdr(createCdr());
        }
    }

    private List<Record> createCdr() {
        List<Record> cdr = new ArrayList<>();
        for (int i = 0; i < 10 && !cdrBuffer.isEmpty(); i++) {
            cdr.add(cdrBuffer.poll());
        }
        return cdr;
    }

    private void updateCurrentTime(Record record) {
        LocalDateTime lastEndTime = record.getEndTime();
        currentStartTime = lastEndTime.plusSeconds(random.nextInt(3600));

        if (currentStartTime.isAfter(endOfYear)) {
            currentStartTime = endOfYear;
        }
    }

    private Subscriber getRandomSubscriber(List<Subscriber> subscribers, Subscriber exclude) {
        Subscriber receiver;
        do {
            receiver = subscribers.get(random.nextInt(subscribers.size()));
        } while (receiver.equals(exclude));
        return receiver;
    }

    private void checkAndResetYear() {
        if (currentStartTime.isAfter(endOfYear)) {
            currentStartTime = currentStartTime.plusYears(1)
                    .withMonth(1).withDayOfMonth(1).withHour(0).withMinute(0);
            endOfYear = currentStartTime.plusYears(1);
        }
    }

    private void sendCdr(List<Record> cdr) {
//        CompletableFuture.runAsync(() -> {
//            System.out.println("Sending this records:");
//
//            cdr.forEach(record -> {
//                System.out.printf("ID: %-5d | Type: %-5s | From: %-12s | To: %-12s | Start: %-20s | End: %-20s%n",
//                        record.getId(),
//                        record.getCallType(),
//                        record.getCaller(),
//                        record.getReceiver(),
//                        record.getStartTime(),
//                        record.getEndTime());;
//            });
//
//            // Отправляем CDR в RabbitMQ
//            //rabbitTemplate.convertAndSend(queueName, cdr);
//        });
            System.out.println("Sending this records:");

            cdr.forEach(record -> {
                System.out.printf("ID: %-5d | Type: %-5s | From: %-12s | To: %-12s | Start: %-20s | End: %-20s%n",
                        record.getId(),
                        record.getCallType(),
                        record.getCaller(),
                        record.getReceiver(),
                        record.getStartTime(),
                        record.getEndTime());;
            });

            // Отправляем CDR в RabbitMQ
            //rabbitTemplate.convertAndSend(queueName, cdr);;
    }
}
