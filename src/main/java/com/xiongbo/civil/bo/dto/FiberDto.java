package com.xiongbo.civil.bo.dto;

public class FiberDto {
    private String startName;
    private String endName;
    private Long uid;
    private String fiberName;
    private double length;

    public String getStartName() {
        return startName;
    }

    public void setStartName(String startName) {
        this.startName = startName;
    }

    public String getEndName() {
        return endName;
    }

    public void setEndName(String endName) {
        this.endName = endName;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getFiberName() {
        return fiberName;
    }

    public void setFiberName(String fiberName) {
        this.fiberName = fiberName;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }
}
