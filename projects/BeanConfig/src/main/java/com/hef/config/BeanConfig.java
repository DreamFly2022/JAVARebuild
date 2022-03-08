package com.hef.config;

import com.hef.domain.Car;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lifei
 * @since 2020/11/18
 */
@Configuration
public class BeanConfig {

    @Bean
    public Car car(){
        return new Car.Builder()
                .name("Dream Car")
                .color("Red")
                .price(99.99).builder();
    }

}
