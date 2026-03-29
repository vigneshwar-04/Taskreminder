package com.example.taskreminder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class TaskreminderApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskreminderApplication.class, args);
	}

}