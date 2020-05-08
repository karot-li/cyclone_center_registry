package com.cyclone.integration.event;

import com.cyclone.independent.statement.RegisteredInstanceInfo;
import org.springframework.context.ApplicationEvent;

/**
 * 制造注册信息监听事件
 */
public class RegisterInstanceEvent extends ApplicationEvent {

    //注册实例信息
    private RegisteredInstanceInfo registeredInstanceInfo;

    private long timeCycle;

    private boolean isSync;

    public RegisterInstanceEvent(Object source, RegisteredInstanceInfo registeredInstanceInfo, long timeCycle, boolean isSync) {
        super(source);
        this.registeredInstanceInfo = registeredInstanceInfo;
        this.timeCycle = timeCycle;
        this.isSync = isSync;
    }
}
