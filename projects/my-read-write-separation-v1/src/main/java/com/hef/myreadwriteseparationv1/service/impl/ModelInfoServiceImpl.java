package com.hef.myreadwriteseparationv1.service.impl;

import com.hef.myreadwriteseparationv1.dao.dao01.ModelInfoWriteDao;
import com.hef.myreadwriteseparationv1.dao.dao02.ModelInfoRead01Dao;
import com.hef.myreadwriteseparationv1.domain.ModelInfo;
import com.hef.myreadwriteseparationv1.service.ModelInfoService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @Date 2022/1/15
 * @Author lifei
 */
@Service
public class ModelInfoServiceImpl implements ModelInfoService {

    @Resource
    private ModelInfoDaoConf modelInfoDaoConf;

    @Resource(name = "modelInfoWriteDao")
    private ModelInfoWriteDao modelInfoWriteDao;

    @Override
    public void updateModelInfo(ModelInfo modelInfo) {
        try {
            if (modelInfo==null || StringUtils.isBlank(modelInfo.getModelType())) {
                throw new IllegalArgumentException("modelType 不能为空");
            }
            ModelInfo item = modelInfoDaoConf.modelInfoReadDao().findModelInfoByModelType(modelInfo.getModelType());
            if (item!=null && Objects.nonNull(item.getMid())) {
                modelInfo.setMid(item.getMid());
                modelInfoWriteDao.updateModelInfo(modelInfo);
            }else {
                modelInfoWriteDao.saveModelInfo(modelInfo);
            }
        }catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public List<ModelInfo> findModelInfoList() {
        List<ModelInfo> result = new ArrayList<>();
        List<ModelInfo> modelInfoList = modelInfoDaoConf.modelInfoReadDao().findModelInfoList();
        if (CollectionUtils.isNotEmpty(modelInfoList)) {
            result.addAll(modelInfoList);
        }
        return result;
    }

}
