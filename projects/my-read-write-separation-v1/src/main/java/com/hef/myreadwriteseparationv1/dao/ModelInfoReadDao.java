package com.hef.myreadwriteseparationv1.dao;

import com.hef.myreadwriteseparationv1.domain.ModelInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ModelInfoReadDao {

    /**
     * 查询模块
     * @param modelType
     * @return
     */
    ModelInfo findModelInfoByModelType(@Param("modelType") String modelType);

    /**
     * 查询模块列表
     * @return
     */
    List<ModelInfo> findModelInfoList();

}
