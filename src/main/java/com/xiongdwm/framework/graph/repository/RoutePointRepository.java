package com.xiongdwm.framework.graph.repository;

import com.xiongdwm.framework.graph.entities.P;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoutePointRepository extends Neo4jRepository<P,String> {

}
