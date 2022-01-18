package com.hef.myreadwritesepshardingspherejdbcv2.domain;

/**
 * @Date 2022/1/15
 * @Author lifei
 */
public class ModelInfo implements Cloneable{

    private Integer mid;
    private String modelType;
    private String modelName;
    private Integer modelStatus;

    public ModelInfo(){}
    private ModelInfo(Builder builder){
        this.mid = builder.mid;
        this.modelType = builder.modelType;
        this.modelName = builder.modelName;
        this.modelStatus = builder.modelStatus;
    }

    public static class Builder{
        private Integer mid;
        private String modelType;
        private String modelName;
        private Integer modelStatus;

        public Builder mid(Integer mid){this.mid=mid; return this;}
        public Builder modelType(String modelType){this.modelType=modelType; return this;}
        public Builder modelName(String modelName){this.modelName=modelName; return this;}
        public Builder modelStatus(Integer modelStatus){this.modelStatus=modelStatus; return this;}

        public ModelInfo builder() {
            return new ModelInfo(this);
        }
    }

    public Integer getMid() {
        return mid;
    }

    public void setMid(Integer mid) {
        this.mid = mid;
    }

    public String getModelType() {
        return modelType;
    }

    public void setModelType(String modelType) {
        this.modelType = modelType;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public Integer getModelStatus() {
        return modelStatus;
    }

    public void setModelStatus(Integer modelStatus) {
        this.modelStatus = modelStatus;
    }

    @Override
    public ModelInfo clone() {
        try {
            return (ModelInfo) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    @Override
    public String toString() {
        return "ModelInfo{" +
                "mid=" + mid +
                ", modelType='" + modelType + '\'' +
                ", modelName='" + modelName + '\'' +
                ", modelStatus=" + modelStatus +
                '}';
    }
}
