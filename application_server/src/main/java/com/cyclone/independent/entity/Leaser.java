package com.cyclone.independent.entity;

public class Leaser<T> {

    T obj;

    public final static long DEFAULT_LIMIT_TIME = 90;

    //最后活跃时间
    private long eventualActiveTime;
    //时间周期
    private long timeCycle;
    //被剔除时间
    private long excludeTime;

    //心跳续约
    public void heartBeatRenew() {
        eventualActiveTime = System.currentTimeMillis() + timeCycle;
    }

    //判断是否过期
    public boolean isOverdue() {
        return excludeTime > 0 || eventualActiveTime < System.currentTimeMillis() - timeCycle;
    }

    public T getObj() {
        return obj;
    }

    public void setObj(T obj) {
        this.obj = obj;
    }

    public long getEventualActiveTime() {
        return eventualActiveTime;
    }

    public void setEventualActiveTime(long eventualActiveTime) {
        this.eventualActiveTime = eventualActiveTime;
    }

    public long getTimeCycle() {
        return timeCycle;
    }

    public void setTimeCycle(long timeCycle) {
        this.timeCycle = timeCycle;
    }

    public long getExcludeTime() {
        return excludeTime;
    }

    public void setExcludeTime(long excludeTime) {
        this.excludeTime = excludeTime;
    }
}
