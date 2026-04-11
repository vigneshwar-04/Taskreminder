package com.example.taskreminder.service;

import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;

@Service
public class CsvService {

    public void generateCSV() {

        try {
            FileWriter writer = new FileWriter("tasks.csv");

            writer.append("Task Name,Status\n");
            writer.append("Task1,Pending\n");
            writer.append("Task2,Completed\n");

            writer.flush();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}