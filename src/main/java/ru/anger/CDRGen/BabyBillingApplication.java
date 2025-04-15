package ru.anger.CDRGen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BabyBillingApplication {
//    @Autowired
//    private RabbitAdmin rabbitAdmin;
//
//    @Autowired
//    private Queue queue;
//
//    @PostConstruct
//    public void declareQueue() {
//        rabbitAdmin.declareQueue(queue);
//    }


    public static void main(String[] args) {
        //System.out.println(org.hibernate.Version.getVersionString());
        SpringApplication.run(BabyBillingApplication.class, args);
        System.out.println(org.hibernate.Version.getVersionString());
        //SpringApplication.run(BRTApplication.class, args);
    }
}
