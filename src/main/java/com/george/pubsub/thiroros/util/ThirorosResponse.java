package com.george.pubsub.thiroros.util;

import java.util.List;
import java.util.Objects;

public class ThirorosResponse {

    public enum Response {OK, REJECTED_SMALL_RANGE, REJECTED_INVALID_ID, REJECTED_NOT_FOUND}

    private Response thirorosResponse;
    private List<DistributedNode> distributedNodes;

    public Response getThirorosResponse() {
        return thirorosResponse;
    }

    public void setThirorosResponse(Response thirorosResponse) {
        this.thirorosResponse = thirorosResponse;
    }

    public List<DistributedNode> getDistributedNodes() {
        return distributedNodes;
    }

    public void setDistributedNodes(List<DistributedNode> distributedNodes) {
        this.distributedNodes = distributedNodes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ThirorosResponse that = (ThirorosResponse) o;
        return thirorosResponse == that.thirorosResponse &&
                Objects.equals(distributedNodes, that.distributedNodes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(thirorosResponse, distributedNodes);
    }

    @Override
    public String toString() {
        return "ThirorosResponse{" +
                "thirorosResponse=" + thirorosResponse +
                ", distributedNodes=" + distributedNodes +
                '}';
    }

}
