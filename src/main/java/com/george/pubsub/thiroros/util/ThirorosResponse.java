package com.george.pubsub.thiroros.util;

import java.util.Objects;

public class ThirorosResponse {

    public enum Response {OK, REJECTED_SMALL_RANGE, REJECTED_INVALID_ID, REJECTED_NOT_FOUND}

    private int nodeId;
    private Response thirorosResponse;
    private DistributedNode previousNode;

    public int getNodeId() {
        return nodeId;
    }

    public void setNodeId(int nodeId) {
        this.nodeId = nodeId;
    }

    public Response getThirorosResponse() {
        return thirorosResponse;
    }

    public void setThirorosResponse(Response thirorosResponse) {
        this.thirorosResponse = thirorosResponse;
    }

    public DistributedNode getPreviousNode() {
        return previousNode;
    }

    public void setPreviousNode(DistributedNode previousNode) {
        this.previousNode = previousNode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ThirorosResponse that = (ThirorosResponse) o;
        return nodeId == that.nodeId &&
                thirorosResponse == that.thirorosResponse &&
                Objects.equals(previousNode, that.previousNode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nodeId, thirorosResponse, previousNode);
    }

    @Override
    public String toString() {
        return "ThirorosResponse{" +
                "nodeId=" + nodeId +
                ", thirorosResponse=" + thirorosResponse +
                ", previousNode=" + previousNode +
                '}';
    }

}
