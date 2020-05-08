package com.cyclone.independent.container;

public interface InstanceContain {

    void putObj(Object controller);

    Object getObj(Class<?> clazz);
}
