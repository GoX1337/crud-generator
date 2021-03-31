package org.gox.annotation.processor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Set;

@SupportedAnnotationTypes("javax.persistence.Entity")
@SupportedSourceVersion(SourceVersion.RELEASE_11)
public class CrudProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnv) {
        System.out.println("CrudProcessor processing..." + new Date());

        for (TypeElement annotation : set) {
            Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(annotation);
            annotatedElements.forEach(this::printClassElement);
        }

        System.out.println("CrudProcessor process ends.");
        return true;
    }

    public void printClassElement(Element element) {
        try {
            if (ElementKind.CLASS.equals(element.getKind())) {
                System.out.println(element);
                generateRepositoryClassFile(element);
                generateController(element);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void generateController(Element element) {
        MethodSpec main = MethodSpec.methodBuilder("main")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(void.class)
                .addParameter(String[].class, "args")
                .addStatement("$T.out.println($S)", System.class, "Hello, JavaPoet!")
                .build();

        TypeSpec helloWorld = TypeSpec.classBuilder("HelloWorld")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(main)
                .build();

        JavaFile javaFile = JavaFile.builder("com.example.helloworld", helloWorld)
                .build();

        try {
            javaFile.writeTo(Paths.get("target/generated-sources"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void generateRepositoryClassFile(Element element) throws ClassNotFoundException {
        String entityName = element.getSimpleName().toString();
        String packageName = element.asType().toString()
                .replace('.' + entityName, "")
                .replace("entity", "");

        TypeSpec repositoryInterface = TypeSpec.interfaceBuilder(entityName + "Repository")
                .addAnnotation(ClassName.get(Class.forName("org.springframework.stereotype.Repository")))
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(ClassName.get(Class.forName("org.springframework.data.repository.CrudRepository")))
                .build();

        JavaFile javaFile = JavaFile.builder(packageName, repositoryInterface)
                .build();

        writeJavaFile(javaFile);
    }

    private void writeJavaFile(JavaFile javaFile) {
        try {
            javaFile.writeTo(Paths.get("target/generated-sources"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
