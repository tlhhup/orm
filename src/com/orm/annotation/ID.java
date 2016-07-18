package com.orm.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ID {

	/**
	 * id列的名称
	 * @return
	 */
	String name();
	
	/**
	 * 标示主键是否为自增
	 * @return
	 */
	boolean isAutoIncrement() default false;
	
}
