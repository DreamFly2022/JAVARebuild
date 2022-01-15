package com.hef.myreadwriteseparationv1.service;

import com.hef.myreadwriteseparationv1.MyReadWriteSeparationV1Application;
import com.hef.myreadwriteseparationv1.domain.ModelInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Date 2022/1/15
 * @Author lifei
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MyReadWriteSeparationV1Application.class)
public class ModelInfoServiceTest {

    @Resource
    private ModelInfoService modelInfoService;

    @Test
    public void updateModelInfoTest() {
        modelInfoService.updateModelInfo(new ModelInfo.Builder()
                .modelType("dynamic-type")
                .modelName("动态类型")
                .modelStatus(0).builder());
    }

    @Test
    public void findModelInfoListTest() {
        List<ModelInfo> modelInfoList = modelInfoService.findModelInfoList();
        System.out.println(modelInfoList);
    }
}
