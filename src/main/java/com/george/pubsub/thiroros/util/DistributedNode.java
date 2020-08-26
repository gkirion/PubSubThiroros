package com.george.pubsub.thiroros.util;

import java.util.Objects;

public class DistributedNode {

    private int id;
    private RemoteAddress remoteAddress;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public RemoteAddress getRemoteAddress() {
        return remoteAddress;
    }

    public void setRemoteAddress(RemoteAddress remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DistributedNode that = (DistributedNode) o;
        return id == that.id &&
                Objects.equals(remoteAddress, that.remoteAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, remoteAddress);
    }

    @Override
    public String toString() {
        return "DistributedNode{" +
                "id=" + id +
                ", remoteAddress=" + remoteAddress +
                '}';
    }

}
