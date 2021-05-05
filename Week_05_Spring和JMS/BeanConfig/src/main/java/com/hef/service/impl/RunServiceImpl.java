package com.hef.service.impl;

import com.hef.service.RunService;
import org.springframework.stereotype.Component;

/**
 * @author lifei
 * @since 2020/11/18
 */
@Component
public class RunServiceImpl implements RunService {
    @Override
    public void running() {
        System.out.println("begin run...");
    }
}
