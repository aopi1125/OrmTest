package com.harbor.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by chenwenfeng on 2018/4/11.
 * Description:
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface DIActivity {
}
