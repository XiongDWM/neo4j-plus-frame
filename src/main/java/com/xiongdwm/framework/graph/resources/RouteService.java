package com.xiongdwm.framework.graph.resources;

import com.xiongdwm.framework.graph.entities.P;
import org.springframework.stereotype.Service;

@Service
public interface RouteService {
    String createOrUpateRoutePoint(P p);

    String createFiber(String startName, String endName, String fiberName, double length);
}
