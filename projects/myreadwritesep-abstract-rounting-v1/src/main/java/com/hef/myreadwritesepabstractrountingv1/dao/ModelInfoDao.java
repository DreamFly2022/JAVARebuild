package com.hef.myreadwritesepabstractrountingv1.dao;

import com.hef.myreadwritesepabstractrountingv1.domain.ModelInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ModelInfoDao {


    List<ModelInfo> findModelInfoList();

    ModelInfo findModelInfoByModelType(@Param("modelType") String modelType);
}
