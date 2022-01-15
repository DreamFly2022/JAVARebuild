package com.hef.myreadwriteseparationv1.dao.dao01;

import com.hef.myreadwriteseparationv1.domain.ModelInfo;
import org.springframework.stereotype.Repository;

@Repository(value = "modelInfoWriteDao")
public interface ModelInfoWriteDao {

    void updateModelInfo(ModelInfo modelInfo);

    void saveModelInfo(ModelInfo modelInfo);

}
