package ru.anger.CDRGen.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import ru.anger.CDRGen.model.Record;
import ru.anger.CDRGen.model.Subscriber;
import ru.anger.CDRGen.repositories.SubscribersRepository;
import ru.anger.CDRGen.repositories.RecordRepository;

import java.time.LocalDateTime;

import java.util.*;

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
    private final List<Record> cdrBuffer = new ArrayList<>();

    private LocalDateTime currentStartTime = LocalDateTime.of(2024, 1, 1, 0, 0);
    private LocalDateTime endOfYear = currentStartTime.plusYears(1);

    private final Map<Subscriber, LocalDateTime> busySubscriber = new HashMap<>();

    public void generateRecords(int min, int max) {
        List<Subscriber> subscribers = subscriberRepository.findAll();
        checkAndResetYear();

        //кол-во необходимых записей
        int targetCount = min + random.nextInt(max - min + 1);
        int generated = 0;

        while (generated < targetCount && currentStartTime.isBefore(endOfYear)) {
            System.out.println("-----------------------------------GENERATING NEW RECORDS-----------------------------------");

            //считаем время для очередной записи
            LocalDateTime startTime = currentStartTime;
            int duration = random.nextInt(3600);
            LocalDateTime endTime = startTime.plusSeconds(duration);
            LocalDateTime midnight = startTime.toLocalDate().plusDays(1).atStartOfDay();

            //перед созданием новой записи рефрешим занятых пользователей
            updateBusySubscribers();
            //случайные неодинаковые незанятые пользователи
            Subscriber caller = getRandomSubscriber(subscribers, null);
            Subscriber receiver = getRandomSubscriber(subscribers, caller);
            String callType = random.nextBoolean() ? "01" : "02";

            //проверка на ночной звонок: делим на 2 разные записи если два разных звонка
            // Можно сделать рефактор чтобы возвращался List<Record> из createRandomRecord, не уверен нужно ли.
            if (endTime.isAfter(midnight)) {
                Record record1 = createRandomRecord(callType, caller, receiver, startTime, midnight);
                Record record2 = createRandomRecord(callType, caller, receiver, midnight, endTime);
                recordRepository.save(record1);
                recordRepository.save(record2);
                cdrBuffer.add(record1);
                checkAndSendCdr();
                cdrBuffer.add(record2);
                generated += 2;
                updateCurrentTime(record2);
                currentStartTime = record2.getStartTime().plusSeconds(random.nextInt(3000));
            } else {
                Record record = createRandomRecord(callType, caller, receiver, startTime, endTime);
                recordRepository.save(record);
                cdrBuffer.add(record);
                generated += 1;
                updateCurrentTime(record);
                currentStartTime = record.getStartTime().plusSeconds(random.nextInt(3000));
            }
            //необходимые чеки
            checkAndResetYear();
            checkAndSendCdr();
        }
    }

    private Record createRandomRecord(String callType, Subscriber caller, Subscriber receiver,
                                      LocalDateTime startTime, LocalDateTime endTime) {
        //создаем записи
        Record record = new Record();
        record.setCallType(callType);
        record.setCaller(caller.getMsisdn());
        record.setReceiver(receiver.getMsisdn());
        record.setStartTime(startTime);
        record.setEndTime(endTime);

        //добавляем занятых пользователей
        busySubscriber.put(caller, endTime);
        busySubscriber.put(receiver, endTime);
        return record;
    }

    private void updateBusySubscribers() {
        busySubscriber.entrySet().removeIf(entry -> entry.getValue().isBefore(currentStartTime));
    }

    private void checkAndSendCdr() {
        if (cdrBuffer.size() >= 10) {
            sendCdr(createCdr());
        }
    }

    private List<Record> createCdr() {
        List<Record> cdr = new ArrayList<>();
        for (int i = 0; i < 10 && !cdrBuffer.isEmpty(); i++) {
            cdr.add(cdrBuffer.remove(cdrBuffer.size() - 1));
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
        Subscriber subscriber;
        do {
            subscriber = subscribers.get(random.nextInt(subscribers.size()));
        } while (subscriber.equals(exclude) || busySubscriber.containsKey(subscriber));
        return subscriber;
    }

    private void checkAndResetYear() {
        if (currentStartTime.isAfter(endOfYear)) {
            currentStartTime = currentStartTime.plusYears(1)
                    .withMonth(1).withDayOfMonth(1).withHour(0).withMinute(0);
            endOfYear = currentStartTime.plusYears(1);
        }
    }

    private void sendCdr(List<Record> cdr) {
            System.out.println("Sending this records:");

            cdr.forEach(record -> {
                System.out.printf("ID: %-5d | Type: %-5s | From: %-12s | To: %-12s | Start: %-20s | End: %-20s%n",
                        record.getId(),
                        record.getCallType(),
                        record.getCaller(),
                        record.getReceiver(),
                        record.getStartTime(),
                        record.getEndTime());
            });

            // Отправляем CDR в RabbitMQ
            //rabbitTemplate.convertAndSend(queueName, cdr);;
    }
}
