package com.hef.myreadwriteseparationv1.service;

import com.hef.myreadwriteseparationv1.domain.ModelInfo;

import java.util.List;

public interface ModelInfoService {

    void updateModelInfo(ModelInfo modelInfo);

    List<ModelInfo> findModelInfoList();
}
