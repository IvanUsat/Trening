package com.jm.online_store;

import com.jm.online_store.model.TaskSettings;
import com.jm.online_store.repository.TaskSettingsRepository;
import com.sun.source.util.TaskListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
@SpringBootApplication
public class OnlineStoreApplication {


    public static void main(String[] args) {
        SpringApplication.run(OnlineStoreApplication.class, args);
    }

}