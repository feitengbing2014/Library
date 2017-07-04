package com.ddq.net.response.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by dongdaqing on 2017/7/4.
 * 目前大部分app的请求模式都是服务端返回一个result，
 * result里面包含了错误码，错误消息，以及数据字段三部分，
 * 这个注解就是用来标示错误码的，{@link Message}用来标识错误消息，
 * 可以给多个字段加{@link Message}标注，会取出非空的字段，
 * {@link Data}是用来标识数据字段，可以标识多个字段
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Code {
    /**
     * 一般请求都以0作为请求正常，如果不是以0为正常结果，改变code()的值
     * @return
     */
    int code() default 0;
}
