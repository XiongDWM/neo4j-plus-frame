package com.xiongdwm.framework.graph.rest;

import com.xiongdwm.framework.graph.entities.P;
import com.xiongdwm.framework.graph.resources.RouteService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class RouteController {
    @Resource
    private RouteService routeService;


    @RequestMapping("/route/createPoint")
    public String createRoutePoint(P p){
        return routeService.createOrUpateRoutePoint(p);
    }

    @RequestMapping("/route/createFiber")
    public String createFiber(String startName,String endName,String fiberName,double length){
        return routeService.createFiber(startName,endName,fiberName,length);
    }
}
