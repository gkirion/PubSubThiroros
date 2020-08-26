package com.george.pubsub.thiroros.service.impl;

import com.george.pubsub.thiroros.service.Thiroros;
import com.george.pubsub.thiroros.util.ChordUtils;
import com.george.pubsub.thiroros.util.DistributedNode;
import com.george.pubsub.thiroros.util.ThirorosResponse;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;

public class ThirorosImpl implements Thiroros {

    private LinkedList<DistributedNode> distributedNodes = new LinkedList<>();

    @Override
    public ThirorosResponse join(DistributedNode distributedNode) {
        ThirorosResponse thirorosResponse = new ThirorosResponse();

        try {
            int id = ChordUtils.computeId(distributedNode);
            distributedNode.setId(id);

            if (distributedNodes.isEmpty()) {
                distributedNodes.add(distributedNode);
                thirorosResponse.setThirorosResponse(ThirorosResponse.Response.OK);
                thirorosResponse.setDistributedNodes(distributedNodes);
            } else {
                int index = 0;
                for (DistributedNode node : distributedNodes) {
                    if (distributedNode.getId() < node.getId()) {
                        break;
                    }
                    index++;
                }
                if (!checkRange(index, distributedNode)) {
                    thirorosResponse.setThirorosResponse(ThirorosResponse.Response.REJECTED_SMALL_RANGE);
                    return thirorosResponse;
                }
                thirorosResponse.setThirorosResponse(ThirorosResponse.Response.OK);
                if (index == distributedNodes.size()) {
                    distributedNodes.addLast(distributedNode);
                } else {
                    distributedNodes.add(index, distributedNode);
                }
                thirorosResponse.setDistributedNodes(distributedNodes);
            }
            return thirorosResponse;
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
            thirorosResponse.setThirorosResponse(ThirorosResponse.Response.REJECTED_INVALID_ID);
        }
        return thirorosResponse;
    }

    @Override
    public ThirorosResponse leave(DistributedNode distributedNode) {
        ThirorosResponse thirorosResponse = new ThirorosResponse();
        int index = 0;
        for (DistributedNode node : distributedNodes) {
            if (distributedNode.getId() == node.getId()) {
                distributedNodes.remove(index);
                thirorosResponse.setThirorosResponse(ThirorosResponse.Response.OK);
                return thirorosResponse;
            }
            index++;
        }
        thirorosResponse.setThirorosResponse(ThirorosResponse.Response.REJECTED_NOT_FOUND);
        return thirorosResponse;
    }

    @Override
    public List<DistributedNode> update() {
        return distributedNodes;
    }

    boolean checkRange(int index, DistributedNode distributedNode) {
        long previousNodeRange = 0;
        if (index == 0) {
            previousNodeRange = Integer.MAX_VALUE - distributedNodes.getLast().getId() + distributedNode.getId() + 1;
        } else {
            previousNodeRange = distributedNode.getId() - distributedNodes.get(index - 1).getId();
        }
        long nextNodeRange = 0;
        if (index == distributedNodes.size()) {
            nextNodeRange = Integer.MAX_VALUE - distributedNode.getId() + distributedNodes.getFirst().getId() + 1;
        } else {
            nextNodeRange = distributedNodes.get(index).getId() - distributedNode.getId();
        }
        if (previousNodeRange > 1 && nextNodeRange > 1) {
            return true;
        }
        return false;
    }

}
