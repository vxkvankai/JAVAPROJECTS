package com.d3.helpers;

import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

@Slf4j
public class AnnotationHelper {

    private AnnotationHelper() {
    }

    /**
     * Allows altering of an annotation on the class level by reflection magic
     *
     * Usage: create a class implementing the Annotation to pass in as annotationValue
     *
     * @param classToLookFor Class on which to edit the annotation
     * @param annotationToAlter Annotation type to alter
     * @param annotationValue Annotation to change to
     */
    public static void alterAnnotationOnClass(Class classToLookFor, Class<? extends Annotation> annotationToAlter, Annotation annotationValue) {
        final String annotationsStr = "annotations";
        final String annotationDataStr = "annotationData";

        try {
            // Thanks to Simonkay in testers.io for this

            // In JDK8 Class has a private method called annotationData().
            // We first need to invoke it to obtain a reference to AnnotationData class which is a private class
            Method method = Class.class.getDeclaredMethod(annotationDataStr, (Class<?>) null);
            method.setAccessible(true);

            // Since AnnotationData is a private class we cannot create a direct reference to it. We will have to
            // manage with just Object
            Object annotationData = method.invoke(classToLookFor, (Object) null);

            // We now look for the map called "annotations" within AnnotationData object.
            Field annotations = annotationData.getClass().getDeclaredField(annotationsStr);
            annotations.setAccessible(true);

            @SuppressWarnings("unchecked")
            Map<Class<? extends Annotation>, Annotation> map = (Map<Class<? extends Annotation>, Annotation>) annotations.get(annotationData);

            map.put(annotationToAlter, annotationValue);
        } catch (IllegalAccessException | NoSuchFieldException | InvocationTargetException | NoSuchMethodException e) {
            log.error("Error altering Annotation", e);
        }
    }

    /**
     * Allows altering of an annotation on the method level by reflection magic
     *
     * Usage: create a class implementing the Annotation to pass in as annotationValue
     *
     * @param methodToLookFor Method to change the annotation of
     * @param annotationToAlter Annotation type to alter
     * @param annotationValue Annotation to change to
     */
    public static void alterAnnotationOnMethod(Method methodToLookFor, Class<? extends Annotation> annotationToAlter, Annotation annotationValue) {
        final String annotationDataStr = "declaredAnnotations";

        try {
            Field annotations = methodToLookFor.getClass().getSuperclass().getDeclaredField(annotationDataStr);
            annotations.setAccessible(true);

            @SuppressWarnings("unchecked")
            Map<Class<? extends Annotation>, Annotation> map = (Map<Class<? extends Annotation>, Annotation>) annotations.get(methodToLookFor);

            map.put(annotationToAlter, annotationValue);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            log.error("Error altering Annotation", e);
        }
    }
}
