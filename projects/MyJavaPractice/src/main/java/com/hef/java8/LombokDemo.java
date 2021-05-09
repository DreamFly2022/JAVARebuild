package com.hef.java8;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.java.Log;

/**
 * @Date 2021/5/9
 * @Author lifei
 */
@Data
@ToString
@Builder
@Log
public class LombokDemo {

    private String name;

    public void test() {
        log.info("this is log");
    }
}
