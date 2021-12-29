package com.hef.myidsequence.domain;

/**
 * @Date 2021/12/29
 * @Author lifei
 */
public class SequenceBO implements Cloneable{

    private String busKey;
    private Long beginId;
    private Integer step;

    public String getBusKey() {
        return busKey;
    }

    public void setBusKey(String busKey) {
        this.busKey = busKey;
    }

    public Long getBeginId() {
        return beginId;
    }

    public void setBeginId(Long beginId) {
        this.beginId = beginId;
    }

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

    @Override
    public SequenceBO clone()  {
        try {
            return (SequenceBO) super.clone();
        }catch (Exception e) {
            throw new AssertionError(e);
        }
    }
}
