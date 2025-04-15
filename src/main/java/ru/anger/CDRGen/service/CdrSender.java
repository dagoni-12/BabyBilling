package ru.anger.CDRGen.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import ru.anger.CDRGen.entity.Record;

import java.util.List;

@Service
public class CdrSender implements MessageSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${exchange.name}")
    private String exchangeName;

    @Override
    public void send(List<Record> cdr) {
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
        rabbitTemplate.convertAndSend(exchangeName, "message.cdr", cdr);
    }
}
