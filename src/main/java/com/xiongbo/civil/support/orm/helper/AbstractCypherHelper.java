package com.xiongbo.civil.support.orm.helper;


import com.xiongbo.civil.support.orm.entity.iInterfaces.DynamicPrinciple;
import org.springframework.data.neo4j.core.Neo4jClient;

import java.util.*;

/**
 * @author xiong
 * @version 1.0.0
 * @neo4jVersion 4.4.7-community
 * @since 2021/05/31 16:00
 * @param <T> Relationship Entity class
 */
public abstract class AbstractCypherHelper<T>{
    public enum RelationshipDirection {
        DIRECTED,
        UNDIRECTED;
    }
    public enum OperationType{
        CREATE,
        UPDATE,
        DELETE,
        RETRIEVE;
    }

    private final Neo4jClient neo4jClient;


    public AbstractCypherHelper(Neo4jClient neo4jClient) {
        this.neo4jClient = neo4jClient;
    }

    public abstract boolean createRelationship(Object start, Object end, T relationship, RelationshipDirection direction);

    public abstract int updateRelationship(Object start, Object end, T relationship);

    public abstract List<T> retrieveRelationshipByPrinciple(DynamicPrinciple principle);


    protected void query(String cypher) {
        Collection<Map<String, Object>> o=neo4jClient.query(cypher).fetch().all();
        System.out.println(new ArrayList<>(o));
    }
}