package com.xiongdwm.framework.graph.entities.relationship;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.xiongdwm.framework.graph.entities.P;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.TargetNode;

import javax.validation.constraints.NotNull;


public class R {
    @NotNull
    @Id
    private Long uid;
    @NotNull
    private String name;
    private double length;
    @TargetNode
    @NotNull
    @JsonFilter("end")
    private P end;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public P getEnd() {
        return end;
    }

    public void setEnd(P end) {
        this.end = end;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }
}
