package com.george.pubsub.thiroros.service;

import com.george.pubsub.thiroros.util.DistributedNode;
import com.george.pubsub.thiroros.util.ThirorosResponse;

import java.util.List;

public interface Thiroros {

    public ThirorosResponse join(DistributedNode distributedNode);
    public ThirorosResponse leave(DistributedNode distributedNode);
    public List<DistributedNode> update();

}
