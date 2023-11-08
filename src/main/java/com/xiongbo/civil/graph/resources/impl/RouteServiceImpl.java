package com.xiongbo.civil.graph.resources.impl;

import com.xiongbo.civil.graph.entities.P;
import com.xiongbo.civil.graph.entities.relationship.R;
import com.xiongbo.civil.graph.repository.RoutePointRepository;
import com.xiongbo.civil.graph.resources.RouteService;
import com.xiongbo.civil.support.orm.entity.EdgePrinciple;
import com.xiongbo.civil.support.orm.helper.AbstractCypherHelper;
import com.xiongbo.civil.support.orm.helper.provider.CypherHelper;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

@Service
public class RouteServiceImpl implements RouteService {
    @Resource
    private RoutePointRepository routePointRepository;
    @Resource
    private Neo4jClient client;


    @Override
    public String createOrUpateRoutePoint(P p) {
        return routePointRepository.save(p).getName();
    }


    @Override
    public String createFiber(String startName, String endName, String fiberName, double length) {
        R r = new R();
        r.setUid(2L);
        r.setName(fiberName);
        r.setLength(length);
        //查询startName对应的RoutePoint findById
        Optional<P> startOptional = routePointRepository.findById(startName);
        Optional<P> endOptional = routePointRepository.findById(endName);
        P start=null;
        P end=null;
        if(startOptional.isPresent())start=startOptional.get();
        if(endOptional.isPresent()){
            end=endOptional.get();
//            fiber.setEnd(end);
        }
        if(start==null||end==null)return "start or end is null";
//        start.getFiber().add(fiber);
//        routePointRepository.save(start);
//        int a=fiberRepository.createDirectedRelationship(start,end,fiber);
        AbstractCypherHelper<R> fiberRepository=new CypherHelper<>(client);
//        boolean a=fiberRepository.createRelationship(start,end,fiber,AbstractCypherHelper.RelationshipDirection.DIRECTED);
        EdgePrinciple principle=new EdgePrinciple(R.class, P.class);
        principle.matchEdge("name",fiberName).matchEdge("length",length).matchNodeEndAt("name",endName).matchNodeStartAt("name",startName).matchEdge("name",fiberName);
        fiberRepository.retrieveRelationshipByPrinciple(principle);
//        client.query(a).run();
        System.out.println(r);
        return "";
    }
}
