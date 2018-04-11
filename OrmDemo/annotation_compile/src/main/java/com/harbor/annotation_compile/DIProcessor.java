package com.harbor.annotation_compile;

import com.google.auto.service.AutoService;
import com.harbor.annotation.DIActivity;
import com.harbor.annotation.DIVIew;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * Created by chenwenfeng on 2018/4/11.
 * Description:
 */

@AutoService(Processor.class)
public class DIProcessor extends AbstractProcessor {

    private Elements elementUtils;
    private int index = 1;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elementUtils  = processingEnv.getElementUtils();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(DIActivity.class.getCanonicalName());
    }

    private String getPkgName(TypeElement typeElement){
        String pkg = elementUtils.getPackageOf(typeElement).getQualifiedName().toString();
        return pkg;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        System.out.print("DIProcessor process() " +index);

        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(DIActivity.class);
        if(elements != null && !elements.isEmpty()){
            for(Element element : elements){
                TypeElement typeElement = (TypeElement) element;
                List<? extends Element> members = elementUtils.getAllMembers(typeElement);
                MethodSpec.Builder bindMethodSpecBuilder = MethodSpec.methodBuilder("bindView")
                        .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                        .returns(TypeName.VOID)
                        .addParameter(ClassName.get(typeElement.asType()), "activity");

                for(Element item : members){
                    DIVIew divIew = item.getAnnotation(DIVIew.class);
                    if(divIew == null){
                        continue;
                    }
                    bindMethodSpecBuilder.addStatement(String.format(
                            "activity.%s = (%s) activity.findViewById(%s)", item.getSimpleName(), ClassName.get(item.asType()), divIew.value()));
                }

                TypeSpec typeSpec = TypeSpec.classBuilder("DI" + element.getSimpleName())
                        .superclass(TypeName.get(typeElement.asType()))
                        .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                        .addMethod(bindMethodSpecBuilder.build())
                        .build();

                JavaFile javaFile = JavaFile.builder(getPkgName(typeElement), typeSpec).build();

                try {
                    javaFile.writeTo(processingEnv.getFiler());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        index++;
        return true;
    }
}
