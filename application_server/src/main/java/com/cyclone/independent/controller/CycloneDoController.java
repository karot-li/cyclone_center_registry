package com.cyclone.independent.controller;


import com.cyclone.independent.annotation.CycloneController;
import com.cyclone.independent.annotation.CycloneRequestMapping;
import com.cyclone.independent.annotation.CycloneResponseBody;
import com.cyclone.independent.entity.Leaser;
import com.cyclone.independent.entity.Result;
import com.cyclone.independent.register.ServiceRegistry;
import com.cyclone.independent.statement.RegisteredInstanceInfo;
import com.cyclone.independent.statement.RegisteredStatement;

@CycloneController
@CycloneRequestMapping("/nba")
public class CycloneDoController {

    //注册器
    private ServiceRegistry serviceRegistry;
    //注册声明
    private RegisteredStatement registeredStatement;

    public CycloneDoController(ServiceRegistry serviceRegistry, RegisteredStatement registeredStatement) {
        this.serviceRegistry = serviceRegistry;
        this.registeredStatement = registeredStatement;
    }

    /**
     * 注册
     * @param registeredInstanceInfo
     * @param isSync
     * @return
     */
    @CycloneRequestMapping("/registry.do")
    @CycloneResponseBody
    public Result doRegistry(RegisteredInstanceInfo registeredInstanceInfo, String isSync) {
        if (registeredInstanceInfo==null) {
            return new Result(false,"注册信息无效",Result.ERROR_CODE);
        }else if (isNull(registeredInstanceInfo.getHostname())) {
            return new Result(false,"hostname不能为空",Result.ERROR_CODE);
        }else if (isNull(registeredInstanceInfo.getInstanceId())) {
            return new Result(false,"id不能为空",Result.ERROR_CODE);
        }else if (isNull(registeredInstanceInfo.getInstanceName())) {
            return new Result(false,"实例名称不能为空",Result.ERROR_CODE);
        }else if (isNull(registeredInstanceInfo.getIp())) {
            return new Result(false,"ip不能为空",Result.ERROR_CODE);
        }else if (isNull(registeredInstanceInfo.getPort())){
            return new Result(false,"端口不能为空",Result.ERROR_CODE);
        }
        //默认时间周期90s
        long timeCycle = Leaser.DEFAULT_LIMIT_TIME;
        if (registeredInstanceInfo.getTimeCycle()>0){
            timeCycle = registeredInstanceInfo.getTimeCycle();
        }
        serviceRegistry.register(registeredInstanceInfo, (int) timeCycle,isSync==null?false:"true".equals(isSync));
        return new Result(true,Result.SUCCESS_CODE,"success");

    }

    /**
     * 续约
     */

    /**
     *剔除
     */

    public boolean isNull(String str) {
        return str==null||str.isEmpty();
    }

    @CycloneRequestMapping("/mvp.do")
    @CycloneResponseBody
    public String mvp() {
        return "What can I say, Mamba out";
    }

    @CycloneRequestMapping("/team.do")
    @CycloneResponseBody
    public String champion(String teamName, int playerNum) {
        System.out.println(teamName+": "+playerNum);
        return teamName;
    }

}
