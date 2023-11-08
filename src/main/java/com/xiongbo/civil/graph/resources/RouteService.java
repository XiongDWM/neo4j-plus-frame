package com.xiongbo.civil.graph.resources;

import com.xiongbo.civil.graph.entities.P;
import org.springframework.stereotype.Service;

@Service
public interface RouteService {
    String createOrUpateRoutePoint(P p);

    String createFiber(String startName, String endName, String fiberName, double length);
}
