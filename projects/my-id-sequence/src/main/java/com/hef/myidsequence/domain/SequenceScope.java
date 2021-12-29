package com.hef.myidsequence.domain;

/**
 * @Date 2021/12/29
 * @Author lifei
 */
public class SequenceScope {

    private String busKey;
    private Long beginId;
    private Long endId;

    public SequenceScope(){}
    private SequenceScope(Builder builder){
        this.busKey = builder.busKey;
        this.beginId = builder.beginId;
        this.endId = builder.endId;
    }

    public static class Builder{
        private String busKey;
        private Long beginId;
        private Long endId;

        public Builder busKey(String busKey) {this.busKey=busKey; return this;}
        public Builder beginId(Long beginId) {this.beginId=beginId; return this;}
        public Builder endId(Long endId) {this.endId=endId; return this;}

        public SequenceScope builder() {
            return new SequenceScope(this);
        }
    }

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

    public Long getEndId() {
        return endId;
    }

    public void setEndId(Long endId) {
        this.endId = endId;
    }

    @Override
    public String toString() {
        return "SequenceScope{" +
                "busKey='" + busKey + '\'' +
                ", beginId=" + beginId +
                ", endId=" + endId +
                '}';
    }
}
