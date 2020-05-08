package com.cyclone.integration.config;

import com.cyclone.independent.container.InstanceContain;
import com.cyclone.independent.controller.CycloneDoController;
import com.cyclone.independent.register.ServiceRegistry;
import com.cyclone.independent.servlet.CycloneServlet;
import com.cyclone.independent.statement.RegisteredStatement;
import com.cyclone.integration.container.InstanceContainImpl;
import com.cyclone.integration.context.LifeCycleBackGo;
import com.cyclone.integration.publish.EventPublishRegistryDo;
import com.cyclone.integration.statementimpl.RegisteredStatementImpl;
import com.cyclone.integration.tag.CanEnableTag;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.servlet.Servlet;
import java.util.Arrays;

@Configuration//cglib
//指定启动标记
@ConditionalOnBean(CanEnableTag.class)
@EnableConfigurationProperties(RegisterConfigProperties.class)
@Import(LifeCycleBackGo.class)
public class AppStartConfig {

    @Bean
    public CycloneDoController cycloneDoController(ServiceRegistry serviceRegistry,RegisteredStatement registeredStatement) {
        return new CycloneDoController(serviceRegistry, registeredStatement);
    }

    /**
     * 注册信息声明
     * yml文件配置信息
     * @return
     */
    @Bean
    public RegisteredStatement registeredStatement() {
        return new RegisteredStatementImpl();
    }


    /**
     *
     * 创建servlet实例
     * @param instanceContain
     * @return
     */
    @Bean
    public ServletRegistrationBean servletRegistrationBean(InstanceContain instanceContain) {
        ServletRegistrationBean<Servlet> servletRegistrationBean = new ServletRegistrationBean<>();
        CycloneServlet cycloneServlet = new CycloneServlet(instanceContain);
        servletRegistrationBean.setServlet(cycloneServlet);
        servletRegistrationBean.setLoadOnStartup(1);
        servletRegistrationBean.setUrlMappings(Arrays.asList("*.do"));
        return servletRegistrationBean;
    }

    /**
     * 存放controller实例容器, 目的是不用每次调用controller都去实例化
     * @return
     */
    @Bean
    public InstanceContain instanceContain(CycloneDoController cycloneDoController){
        InstanceContainImpl instanceContainImpl = new InstanceContainImpl();
        instanceContainImpl.putObj(cycloneDoController);
        return instanceContainImpl;
    }

    /**
     * 完整注册器, 包括发布注册事件
     * @return
     */
    @Bean
    public EventPublishRegistryDo eventPublishRegistryDo() {
        return new EventPublishRegistryDo();
    }

}
