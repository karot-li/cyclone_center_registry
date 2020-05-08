package com.cyclone.independent.statement;

import com.cyclone.independent.enumeration.StatusCode;

/**
 * 服务注册实例
 */
public class RegisteredInstanceInfo {


    private String ip;//ip

    private String port;//端口

    private String hostname;//域名

    private String instanceName;//服务实例名称

    private String instanceId;//服务实例ID

    private int timeCycle;//时间周期

    private StatusCode statusCode;//状态码

    @Override
    public String toString() {
        return "RegisteredInstance{" +
                "ip='" + ip + '\'' +
                ", port='" + port + '\'' +
                ", hostname='" + hostname + '\'' +
                ", instanceName='" + instanceName + '\'' +
                ", instanceId='" + instanceId + '\'' +
                ", timeCycle=" + timeCycle +
                ", statusCode=" + statusCode +
                '}';
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public int getTimeCycle() {
        return timeCycle;
    }

    public void setTimeCycle(int timeCycle) {
        this.timeCycle = timeCycle;
    }

    public StatusCode getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(StatusCode statusCode) {
        this.statusCode = statusCode;
    }
}
