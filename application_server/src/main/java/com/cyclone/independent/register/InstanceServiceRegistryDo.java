package com.cyclone.independent.register;


import com.cyclone.independent.entity.Leaser;
import com.cyclone.independent.enumeration.StatusCode;
import com.cyclone.independent.statement.RegisteredInstanceInfo;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 服务治理
 */
public class InstanceServiceRegistryDo implements ServiceRegistry {

    private Map<String, Map<String, Leaser<RegisteredInstanceInfo>>> registers = new ConcurrentHashMap<>();


    @Override
    public void register(RegisteredInstanceInfo registeredInstanceInfo, int timeLimit, boolean isSync) {
        Map<String, Leaser<RegisteredInstanceInfo>> serverInstance = registers.get(registeredInstanceInfo.getHostname());
        if (serverInstance == null) {
            serverInstance = new HashMap<>();
            Leaser<RegisteredInstanceInfo> leaser = new Leaser<>();
            registeredInstanceInfo.setStatusCode(StatusCode.UP);
            leaser.setObj(registeredInstanceInfo);
            leaser.setTimeCycle(registeredInstanceInfo.getTimeCycle());
            leaser.heartBeatRenew();
            serverInstance.put(registeredInstanceInfo.getInstanceId(),leaser);
            registers.put(registeredInstanceInfo.getHostname(),serverInstance);
        }
        System.out.println("服务注册: " + registeredInstanceInfo);
    }

    @Override
    public void heartBeatRenew(String serverName, String instanceId, boolean isSync) {
        Map<String, Leaser<RegisteredInstanceInfo>> serverInstance = registers.get(serverName);
        if (serverInstance!=null) {
            Leaser<RegisteredInstanceInfo> leaser = serverInstance.get(instanceId);
            if (leaser!=null) {
                leaser.heartBeatRenew();
            }
        }
        System.out.println("心跳续约");
    }

    @Override
    public void getDown(String serverName, String instanceId, boolean isSync) {
        Map<String, Leaser<RegisteredInstanceInfo>> serverInstance = registers.get(serverName);
        if (serverInstance!=null) {
            Leaser<RegisteredInstanceInfo> leaser = serverInstance.get(instanceId);
            if (leaser!=null) {
                RegisteredInstanceInfo registeredInstanceInfo = leaser.getObj();
                registeredInstanceInfo.setStatusCode(StatusCode.DOWN);
            }
        }
        System.out.println("服务下架");
    }

    @Override
    public void eliminate() {
        if (registers!=null&&registers.size()>0) {
            for (String serverName : registers.keySet()) {
                Map<String, Leaser<RegisteredInstanceInfo>> serverInstance = registers.get(serverName);
                Iterator<String> iterator = serverInstance.keySet().iterator();
                while (iterator.hasNext()) {
                    if (serverInstance.get(iterator.next()).isOverdue()){
                        iterator.remove();
                    }
                }

            }

        }
        System.out.println("服务剔除");
    }
}
