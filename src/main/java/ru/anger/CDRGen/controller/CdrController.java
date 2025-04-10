package ru.anger.CDRGen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.anger.CDRGen.service.CdrGeneratorService;
import ru.anger.CDRGen.service.CdrService;

import java.util.Random;

@Controller
public class CdrController {

    @Autowired
    private CdrGeneratorService cdrGeneratorService;

    @Autowired
    private CdrService cdrService;

    Random random = new Random();

    @PostMapping("/generate")
    public ResponseEntity<String> generateCdrRecordsSetAmount(@RequestParam(name = "min") int minAmount,
                                                              @RequestParam(name = "max") int maxAmount) {
        cdrGeneratorService.generateRecords(minAmount, maxAmount);
        return ResponseEntity.ok("CDR records generated successfully.");
    }

    @PostMapping("/generate/random")
    public ResponseEntity<String> generateCdrRecordsRandomAmount() {
        cdrGeneratorService.generateRecords(0, random.nextInt(2000));
        return ResponseEntity.ok("CDR records generated successfully.");
    }

    @PostMapping("/deleteCdr")
    public ResponseEntity<String> deleteCdrTable() {
        cdrService.deleteCdrTable();
        return ResponseEntity.ok("CDR table truncated");
    }
}
