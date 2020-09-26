package com.george.pubsub.thiroros.service.impl;

import com.george.pubsub.thiroros.service.Thiroros;
import com.george.pubsub.thiroros.util.ChordUtils;
import com.george.pubsub.thiroros.util.DistributedNode;
import com.george.pubsub.thiroros.util.ThirorosResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;

public class ThirorosImpl implements Thiroros {

    private Logger logger = LoggerFactory.getLogger(ThirorosImpl.class);
    private LinkedList<DistributedNode> distributedNodes = new LinkedList<>();

    @Override
    public ThirorosResponse join(DistributedNode distributedNode) {

        ThirorosResponse thirorosResponse = new ThirorosResponse();
        try {
            int id = ChordUtils.computeId(distributedNode.getRemoteAddress().toString());
            distributedNode.setId(id);
            thirorosResponse.setNodeId(id);
            logger.info("got join request from node {}", distributedNode);

            if (distributedNodes.isEmpty()) {
                distributedNodes.add(distributedNode);
                thirorosResponse.setThirorosResponse(ThirorosResponse.Response.OK);
                thirorosResponse.setPreviousNode(null);
            } else {
                // find previous node
                int index = 0;
                for (DistributedNode node : distributedNodes) {
                    if (distributedNode.getId() <= node.getId()) {
                        break;
                    }
                    index++;
                }
                if (distributedNode.getId() == distributedNodes.get(index).getId()) {
                    thirorosResponse.setThirorosResponse(ThirorosResponse.Response.OK);
                    logger.info("node {} already joined", distributedNode);
                    return thirorosResponse;
                }
                if (!checkRange(index, distributedNode)) {
                    thirorosResponse.setThirorosResponse(ThirorosResponse.Response.REJECTED_SMALL_RANGE);
                    logger.info("node {} rejected [small range]", distributedNode);
                    return thirorosResponse;
                }
                thirorosResponse.setThirorosResponse(ThirorosResponse.Response.OK);
                if (index == distributedNodes.size()) {
                    distributedNodes.addLast(distributedNode);
                } else {
                    distributedNodes.add(index, distributedNode);
                }
                if (index == 0) {
                    thirorosResponse.setPreviousNode(distributedNodes.getLast());
                } else {
                    thirorosResponse.setPreviousNode(distributedNodes.get(index - 1));
                }
            }
            logger.info("node {} joined chord", distributedNode);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            logger.info("node {} join failed, error {}", distributedNode, e.getLocalizedMessage());
            thirorosResponse.setThirorosResponse(ThirorosResponse.Response.REJECTED_INVALID_ID);
        }
        return thirorosResponse;
    }

    @Override
    public ThirorosResponse leave(DistributedNode distributedNode) {
        logger.info("got leave request from node {}", distributedNode);
        ThirorosResponse thirorosResponse = new ThirorosResponse();
        int index = 0;
        for (DistributedNode node : distributedNodes) {
            if (distributedNode.getId() == node.getId()) {
                distributedNodes.remove(index);
                thirorosResponse.setThirorosResponse(ThirorosResponse.Response.OK);
                logger.info("node {} removed from chord", distributedNode);
                return thirorosResponse;
            }
            index++;
        }
        logger.info("node {} not found", distributedNode);
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
