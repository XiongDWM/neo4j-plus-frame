package com.xiongdwm.framework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.xiongbo.civil.relational.repository")
@EnableNeo4jRepositories(basePackages = {"com.xiongbo.civil.graph.repository","com.xiongbo.civil.graph.repository.relationship"})
@EntityScan(basePackages = {"com.xiongbo.civil.relational.entities","com.xiongbo.civil.graph.entities"})
public class Neo4jPlusFrameApplication {
	public static void main(String[] args) {
		SpringApplication.run(Neo4jPlusFrameApplication.class, args);
	}
}
