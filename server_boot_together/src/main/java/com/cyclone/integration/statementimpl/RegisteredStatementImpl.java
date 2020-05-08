package com.cyclone.integration.statementimpl;

import com.cyclone.independent.statement.RegisteredStatement;
import com.cyclone.integration.config.RegisterConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 获取yml文件配置信息
 */
public class RegisteredStatementImpl implements RegisteredStatement {

    @Autowired
    RegisterConfigProperties registerConfigProperties;

    @Override
    public String getRegisteredName() {
        return registerConfigProperties.getHostname();
    }

    @Override
    public String getRegisteredUrl() {
        return registerConfigProperties.getDefaultZone();
    }

    @Override
    public long getEliminationTime() {
        return registerConfigProperties.getEliminationTime();
    }
}
