package com.hef.myidsequence.service.impl;

import com.hef.myidsequence.dao.SequenceDao;
import com.hef.myidsequence.domain.ResponseResult;
import com.hef.myidsequence.domain.ResultStatusEnum;
import com.hef.myidsequence.domain.SequenceBO;
import com.hef.myidsequence.domain.SequenceScope;
import com.hef.myidsequence.domain.param.SequenceParam;
import com.hef.myidsequence.service.SequenceService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @Date 2021/12/29
 * @Author lifei
 */
@Service
@Transactional
public class SequenceServiceImpl implements SequenceService {

    @Resource
    private SequenceDao sequenceDao;

    @Override
    public ResponseResult<SequenceScope> findSequenceScopeResult(SequenceParam sequenceParam) {
        try {
            if (sequenceParam==null || StringUtils.isBlank(sequenceParam.getBusKey())) {
                throw new IllegalArgumentException("busKey is Empty");
            }
            // 查询beginID
            SequenceBO sequenceBO = sequenceDao.findSequenceBO(sequenceParam.getBusKey());
            if (sequenceBO==null || StringUtils.isBlank(sequenceBO.getBusKey())) {
                throw new RuntimeException("该业务不存在");
            }
            Long beginId = sequenceBO.getBeginId();
            Long endId = sequenceBO.getBeginId() + sequenceBO.getStep();
            SequenceBO clone = sequenceBO.clone();
            clone.setBeginId(endId+1);
            sequenceDao.updateSequenceBO(clone);
            return new ResponseResult<>(new SequenceScope.Builder()
                    .beginId(beginId)
                    .endId(endId)
                    .busKey(sequenceBO.getBusKey())
                    .builder(), ResultStatusEnum.SUCCESS);
        }catch (Exception e) {
            return new ResponseResult<>(null, ResultStatusEnum.FAIL.getStatus(), e.getMessage());
        }
    }
}
