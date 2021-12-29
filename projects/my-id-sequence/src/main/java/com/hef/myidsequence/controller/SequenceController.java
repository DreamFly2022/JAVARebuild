package com.hef.myidsequence.controller;

import com.hef.myidsequence.domain.ResponseResult;
import com.hef.myidsequence.domain.SequenceScope;
import com.hef.myidsequence.domain.param.SequenceParam;
import com.hef.myidsequence.service.SequenceService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Date 2021/12/29
 * @Author lifei
 */
@RestController
@RequestMapping(value = "/sequenceController")
public class SequenceController {


    @Resource
    private SequenceService sequenceService;

    /**
     * 获取 一个可用的id范围
     * @param sequenceParam
     * @return
     */
    @RequestMapping(value = "/findSequenceScopeResult", method = RequestMethod.POST)
    public ResponseResult<SequenceScope> findSequenceScopeResult(@RequestBody SequenceParam sequenceParam) {
        return sequenceService.findSequenceScopeResult(sequenceParam);
    }

}
