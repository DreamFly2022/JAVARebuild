package com.hef.myreadwriteseparationv1.service.impl;

import com.hef.myreadwriteseparationv1.dao.ModelInfoReadDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @Date 2022/1/15
 * @Author lifei
 */
@Configuration
public class ModelInfoDaoConf {

    @Resource(name = "modelInfoRead01Dao")
    private ModelInfoReadDao modelInfoRead01Dao;

    @Resource(name = "modelInfoRead02Dao")
    private ModelInfoReadDao modelInfoRead02Dao;

    private Random random = new Random();

    @Bean
    public List<ModelInfoReadDao> modelInfoReadDaoList() {
        List<ModelInfoReadDao> modelInfoReadDaoList = new ArrayList<>(2);
        modelInfoReadDaoList.add(modelInfoRead01Dao);
        modelInfoReadDaoList.add(modelInfoRead02Dao);
        return modelInfoReadDaoList;
    }

    /**
     * 随机查询一个 dao
     * @return
     */
    public ModelInfoReadDao modelInfoReadDao() {
        List<ModelInfoReadDao> modelInfoReadDaoList = modelInfoReadDaoList();
        int i = random.nextInt(modelInfoReadDaoList.size());
        return modelInfoReadDaoList.get(i);
    }
}
