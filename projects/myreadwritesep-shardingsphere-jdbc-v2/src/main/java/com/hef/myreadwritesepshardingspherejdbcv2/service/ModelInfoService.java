package com.hef.myreadwritesepshardingspherejdbcv2.service;


import com.hef.myreadwritesepshardingspherejdbcv2.domain.ModelInfo;

import java.util.List;

public interface ModelInfoService {

    List<ModelInfo> findModelInfoList();

    ModelInfo findModelInfoByModelType(String modelType);

    List<String> showDataBases();
}
