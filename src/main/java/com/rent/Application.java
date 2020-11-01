package com.rent;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@MapperScan("com.rent.dao")
@MapperScan({"com.rent.dao", "com.rent.Base.organ.dao"})
//@EntityScan("com.rent")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
