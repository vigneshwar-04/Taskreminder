package com.example.taskreminder.controller;

import com.example.taskreminder.service.CsvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/csv")
public class CSVController {

    @Autowired
    private CsvService csvService;

    @GetMapping("/export")
    public String exportCSV() {
        csvService.generateCSV();
        return "CSV Generated!";
    }
}