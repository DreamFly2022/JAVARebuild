package com.hef.nio.gateway.rounter;

import java.util.List;

public interface HttpEndpointRouter {

    String router(List<String> endpoints);
}
