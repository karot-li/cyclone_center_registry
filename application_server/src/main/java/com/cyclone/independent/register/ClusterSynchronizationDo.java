package com.cyclone.independent.register;

import com.cyclone.independent.statement.RegisteredInstanceInfo;

/**
 * 集群同步
 */
public class ClusterSynchronizationDo extends InstanceServiceRegistryDo {


    @Override
    public void register(RegisteredInstanceInfo registeredInstanceInfo, int timeLimit, boolean isSync) {
        super.register(registeredInstanceInfo, timeLimit, isSync);
    }

    /**
     * 同步节点
     */
    public void synchronous() {

    }
}
