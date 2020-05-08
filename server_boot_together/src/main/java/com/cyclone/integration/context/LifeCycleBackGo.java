package com.cyclone.integration.context;

import com.cyclone.independent.register.ServiceRegistry;
import com.cyclone.independent.statement.RegisteredStatement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.SmartLifecycle;

import java.util.Timer;
import java.util.TimerTask;

public class LifeCycleBackGo implements SmartLifecycle {

    @Autowired
    RegisteredStatement registeredStatement;

    @Autowired
    ServiceRegistry serviceRegistry;

    private boolean isRunning = false;

    //是否执行start方法
    @Override
    public boolean isAutoStartup() {
        return true;
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }

    /**
     * 开启一个独立线程, 周期性检查, 执行服务剔除逻辑
     */
    @Override
    public void start() {

        new Thread(()->{
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    serviceRegistry.eliminate();
                }
            },registeredStatement.getEliminationTime(),registeredStatement.getEliminationTime());

        }).start();

    }

    @Override
    public void stop(Runnable callback) {
        callback.run();
        isRunning = false;
    }

    @Override
    public int getPhase() {
        return 0;
    }



    @Override
    public void stop() {
        isRunning=false;
    }

}
