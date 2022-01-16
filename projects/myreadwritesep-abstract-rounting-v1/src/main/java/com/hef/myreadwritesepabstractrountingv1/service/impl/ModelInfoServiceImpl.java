package com.hef.myreadwritesepabstractrountingv1.service.impl;

import com.hef.myreadwritesepabstractrountingv1.conf.CurDataSource;
import com.hef.myreadwritesepabstractrountingv1.conf.DataSourceKey;
import com.hef.myreadwritesepabstractrountingv1.dao.ModelInfoDao;
import com.hef.myreadwritesepabstractrountingv1.domain.ModelInfo;
import com.hef.myreadwritesepabstractrountingv1.service.ModelInfoService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Date 2022/1/16
 * @Author lifei
 */
@Service
public class ModelInfoServiceImpl implements ModelInfoService {


    @Resource
    private ModelInfoDao modelInfoDao;

    @CurDataSource(name = DataSourceKey.READ01_KEY)
    @Override
    public List<ModelInfo> findModelInfoList() {
        List<ModelInfo> result = new ArrayList<>();
        List<ModelInfo> modelInfoList = modelInfoDao.findModelInfoList();
        if (CollectionUtils.isNotEmpty(modelInfoList)) {
            result.addAll(modelInfoList);
        }
        return result;
    }

    @CurDataSource(name = DataSourceKey.READ02_KEY)
    @Override
    public ModelInfo findModelInfoByModelType(String modelType) {
        if (StringUtils.isBlank(modelType)) return null;
        ModelInfo modelInfo = modelInfoDao.findModelInfoByModelType(modelType);
        if (modelInfo==null) return null;
        return modelInfo.clone();
    }

}
