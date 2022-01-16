package com.hef.myreadwritesepabstractrountingv1.service;

import com.hef.myreadwritesepabstractrountingv1.domain.ModelInfo;

import java.util.List;

public interface ModelInfoService {

    List<ModelInfo> findModelInfoList();

    ModelInfo findModelInfoByModelType(String modelType);
}
