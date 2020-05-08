package com.cyclone.integration.annotation;

import com.cyclone.integration.tag.CanEnableTag;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import(CanEnableTag.class)
public @interface EnableCycloneServer {

}
