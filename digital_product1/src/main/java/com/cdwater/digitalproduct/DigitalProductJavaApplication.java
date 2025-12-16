package com.cdwater.digitalproduct;

import org.dromara.x.file.storage.spring.EnableFileStorage;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableFileStorage
@EnableScheduling
@MapperScan("com.cdwater.digitalproduct.mapper")
public class DigitalProductJavaApplication {

    public static void main(String[] args) {
        SpringApplication.run(DigitalProductJavaApplication.class, args);
    }

}
