package com.cyclone.independent.register;


import com.cyclone.independent.statement.RegisteredInstanceInfo;

/**
 * 服务注册
 */
public interface ServiceRegistry {

    /**
     * 服务注册
     */
    void register(RegisteredInstanceInfo registeredInstanceInfo, int timeLimit, boolean isSync);
    /**
     * 心跳续约
     */
    void heartBeatRenew(String serverName, String instanceId, boolean isSync);
    /**
     * 服务下架
     */
    void getDown(String serverName, String instanceId, boolean isSync);
    /**
     * 服务剔除
     */
    void eliminate();
}
