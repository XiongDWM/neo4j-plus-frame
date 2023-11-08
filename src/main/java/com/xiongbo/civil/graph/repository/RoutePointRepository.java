package com.xiongbo.civil.graph.repository;

import com.xiongbo.civil.graph.entities.P;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoutePointRepository extends Neo4jRepository<P,String> {

}
