package com.hef.myreadwritesepshardingspherejdbcv2.dao;

import com.hef.myreadwritesepshardingspherejdbcv2.domain.ModelInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ModelInfoDao {


    List<ModelInfo> findModelInfoList();

    ModelInfo findModelInfoByModelType(@Param("modelType") String modelType);

    List<String> showDataBases();
}
