package com.chaplygin.task_manager;

import org.springframework.boot.SpringApplication;

public class TestTaskManagerApplication {

	public static void main(String[] args) {
		SpringApplication.from(TaskManagerApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
