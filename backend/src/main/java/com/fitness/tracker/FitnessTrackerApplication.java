package com.fitness.tracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 健身数据追踪系统启动类
 */
@SpringBootApplication
public class FitnessTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(FitnessTrackerApplication.class, args);
        System.out.println("========================================");
        System.out.println("  Fitness Tracker AI Backend Started  ");
        System.out.println("  API: http://localhost:8080/api      ");
        System.out.println("========================================");
    }
}
