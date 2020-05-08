package com.cyclone.integration.container;

import com.cyclone.independent.container.InstanceContain;

import java.util.HashMap;
import java.util.Map;

public class InstanceContainImpl implements InstanceContain {

    private Map<Class,Object> controllerMap;

    public InstanceContainImpl() {
        controllerMap = new HashMap<>();
    }


    public void putObj(Object controller) {
        Object obj = controllerMap.get(controller.getClass());
        if (obj==null) {
            controllerMap.put(controller.getClass(),controller);
        }
    }

    @Override
    public Object getObj(Class<?> clazz) {
        return controllerMap.get(clazz);
    }
}
