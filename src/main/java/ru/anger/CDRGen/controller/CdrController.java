package ru.anger.CDRGen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import ru.anger.CDRGen.service.CdrGeneratorService;

@Controller
public class CdrController {

    @Autowired
    private CdrGeneratorService cdrGeneratorService;

    @PostMapping("/generate")
    public ResponseEntity<String> generateCdrRecords() {
        cdrGeneratorService.generateRecords();
        return ResponseEntity.ok("CDR records generated successfully.");
    }
}
