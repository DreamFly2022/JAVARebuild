package com.hef.myidsequence.dao;

import com.hef.myidsequence.domain.SequenceBO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SequenceDao {

    /**
     * 查询一个序列范围
     * @param busKey
     * @return
     */
    SequenceBO findSequenceBO(@Param("busKey") String busKey);

    /**
     * 更新开始序列
     * @param sequenceBO
     */
    void updateSequenceBO(SequenceBO sequenceBO);
}
