package com.cyclone.independent.statement;

/**
 * 注册声明
 */
public interface RegisteredStatement {

    /**
     * 注册实例名称
     * @return
     */
    String getRegisteredName();

    /**
     * 服务注册url
     * @return
     */
    String getRegisteredUrl();

    /**
     * 服务剔除时间
     * @return
     */
    long getEliminationTime();

}
