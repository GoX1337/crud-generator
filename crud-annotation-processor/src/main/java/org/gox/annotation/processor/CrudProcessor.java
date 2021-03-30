package org.gox.annotation.processor;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
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
        if(ElementKind.CLASS.equals(element.getKind())) {
            System.out.println(element);
            element.getEnclosedElements().forEach(this::printAttributeElement);
        }

        JavaFileObject builderFile = null;
        try {
            builderFile = processingEnv.getFiler().createSourceFile("Hey");
            try (PrintWriter out = new PrintWriter(builderFile.openWriter())) {
                out.println("public class Hey { }");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void printAttributeElement(Element element){
        if(ElementKind.FIELD.equals(element.getKind())) {
            System.out.println("\t- " + element);
        }
    }
}
