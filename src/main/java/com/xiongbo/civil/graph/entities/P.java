package com.xiongbo.civil.graph.entities;


import com.xiongbo.civil.graph.entities.relationship.R;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import javax.validation.constraints.NotNull;
import java.util.List;


@Node
public class P {
    @Id
    @NotNull
    private String name;
    private double x;
    private double y;
    private P.Type type;
    @Relationship(type = "R", direction = Relationship.Direction.OUTGOING)  //FIBER is the relationship type
    private List<R> r;

    enum Type {
        TYPE1,
        TYPE2,
        TYPE3
    }

    //getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public List<R> getR() {
        return r;
    }

    public void setR(List<R> r) {
        this.r = r;
    }
}
