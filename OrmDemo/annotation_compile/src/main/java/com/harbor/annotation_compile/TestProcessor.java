package com.harbor.annotation_compile;

import com.google.auto.service.AutoService;
import com.harbor.annotation.Test;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

@AutoService(Processor.class)
public class TestProcessor extends AbstractProcessor{

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(Test.class.getCanonicalName());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        //main静态方法
        MethodSpec main = MethodSpec.methodBuilder("main")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(void.class)
                .addParameter(String[].class, "args")
                .addStatement("$T.out.println($S)", System.class, "Hello, Harbor to JavaPoet..")
                .build();

        //final HelloJavaPoet.java类
        TypeSpec hello = TypeSpec.classBuilder("HelloJavaPoet")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(main)
                .build();

        JavaFile javaFile = JavaFile.builder("com.harbor.hello", hello)
                .build();

        try {
            javaFile.writeTo(processingEnv.getFiler());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
