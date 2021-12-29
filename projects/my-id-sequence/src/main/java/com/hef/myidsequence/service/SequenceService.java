package com.hef.myidsequence.service;

import com.hef.myidsequence.domain.ResponseResult;
import com.hef.myidsequence.domain.SequenceScope;
import com.hef.myidsequence.domain.param.SequenceParam;

/**
 * @Date 2021/12/29
 * @Author lifei
 */
public interface SequenceService {

    /**
     * 查询序列范围
     * @param sequenceParam
     * @return
     */
    ResponseResult<SequenceScope> findSequenceScopeResult(SequenceParam sequenceParam);
}
