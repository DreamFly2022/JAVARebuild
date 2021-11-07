package com.hef.dbsql.mysql;

/**
 * @Date 2021/8/1
 * @Author lifei
 */
public class MySQLEngineItem {

    private Long tid;
    private String name;
    private Double num;
    private String phoneType;
    private String phoneVersion;

    public MySQLEngineItem(){}
    private MySQLEngineItem(Builder builder){
        this.name = builder.name;
        this.num = builder.num;
        this.phoneType=builder.phoneType;
        this.phoneVersion = builder.phoneVersion;
    }

    public static class Builder{
        private String name;
        private Double num;
        private String phoneType;
        private String phoneVersion;

        public Builder name(String name) {this.name = name; return this;}
        public Builder num(Double num) {this.num = num; return this;}
        public Builder phoneType(String phoneType) {this.phoneType = phoneType; return this;}
        public Builder phoneVersion(String phoneVersion) {this.phoneVersion = phoneVersion; return this;}

        public MySQLEngineItem builder() {
            return new MySQLEngineItem(this);
        }
    }

    public Long getTid() {
        return tid;
    }

    public void setTid(Long tid) {
        this.tid = tid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getNum() {
        return num;
    }

    public void setNum(Double num) {
        this.num = num;
    }

    public String getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(String phoneType) {
        this.phoneType = phoneType;
    }

    public String getPhoneVersion() {
        return phoneVersion;
    }

    public void setPhoneVersion(String phoneVersion) {
        this.phoneVersion = phoneVersion;
    }

    @Override
    public String toString() {
        return "MySQLEngineItem{" +
                "tid=" + tid +
                ", name='" + name + '\'' +
                ", num='" + num + '\'' +
                ", phoneType='" + phoneType + '\'' +
                ", phoneVersion='" + phoneVersion + '\'' +
                '}';
    }
}
