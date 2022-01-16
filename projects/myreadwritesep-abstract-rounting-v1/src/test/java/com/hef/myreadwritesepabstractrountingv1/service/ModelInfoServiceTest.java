package com.hef.myreadwritesepabstractrountingv1.service;

import com.hef.myreadwritesepabstractrountingv1.MyreadwritesepAbstractRountingV1Application;
import com.hef.myreadwritesepabstractrountingv1.domain.ModelInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Date 2022/1/16
 * @Author lifei
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MyreadwritesepAbstractRountingV1Application.class)
public class ModelInfoServiceTest {

    @Resource
    private ModelInfoService modelInfoService;

    @Resource
    private ApplicationContext applicationContext;

    @Test
    public void findModelInfoTest() {
        List<ModelInfo> modelInfoList = modelInfoService.findModelInfoList();
        System.out.println(modelInfoList);
    }

    @Test
    public void findModelInfoByModelTypeTest() {
        ModelInfo modelInfo = modelInfoService.findModelInfoByModelType("dynamic-type");
        System.out.println(modelInfo);
    }
}
