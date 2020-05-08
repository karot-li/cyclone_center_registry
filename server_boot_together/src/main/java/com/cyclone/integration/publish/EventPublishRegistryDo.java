package com.cyclone.integration.publish;

import com.cyclone.independent.register.ClusterSynchronizationDo;
import com.cyclone.independent.statement.RegisteredInstanceInfo;
import com.cyclone.integration.event.RegisterInstanceEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;

/**
 * 责任链
 * 发布注册监听事件
 */
public class EventPublishRegistryDo extends ClusterSynchronizationDo {


    @Autowired
    private ApplicationContext applicationContext;

    private void publishEvent(RegisteredInstanceInfo registeredInstanceInfo, int timeLimit, boolean isSync) {
        appPublish(new RegisterInstanceEvent(this,registeredInstanceInfo,timeLimit,isSync));
    }

    private void appPublish(ApplicationEvent applicationEvent) {
        applicationContext.publishEvent(applicationEvent);
    }



    @Override
    public void register(RegisteredInstanceInfo registeredInstanceInfo, int timeLimit, boolean isSync) {
        publishEvent(registeredInstanceInfo,timeLimit,isSync);
        super.register(registeredInstanceInfo, timeLimit, isSync);
    }



    //心跳续约
    @Override
    public void heartBeatRenew(String serverName, String instanceId, boolean isSync) {
        super.heartBeatRenew(serverName, instanceId, isSync);
    }

    //服务下架
    @Override
    public void getDown(String serverName, String instanceId, boolean isSync) {
        super.getDown(serverName, instanceId, isSync);
    }

    //服务剔除
    @Override
    public void eliminate() {
        super.eliminate();
    }
}
