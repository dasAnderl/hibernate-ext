package com.anderl.hibernate.ext.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by ga2unte on 8.9.2014.
 */
public class ReflectionHelper {

    private ReflectionHelper() {
    }

    private static Logger log = LoggerFactory.getLogger(ReflectionHelper.class);

    public static <T> List<T> invokeGettersByReturnType(Class<T> returnType, Object object) {
        List<T> list = new ArrayList<>();
        if (object == null || returnType == null) {
            throw new AssertionError(String.format("one arg is null returnType=%s, object=%s", returnType, object));
        }
        for (Method method : object.getClass().getMethods()) {
            if (method.getName().startsWith("get")
                    && method.getReturnType().equals(returnType)
                    && object.getClass() != method.getClass()) {
                try {
                    Object result = method.invoke(object);
                    if (result != null) {
                        list.add((T)result);
                    }
                } catch (IllegalAccessException e) {
                    log.warn("{} not contains any getter with returntype {}", object.getClass(), returnType);
                } catch (InvocationTargetException e) {
                    log.warn("{} not contains any getter with returntype {}", object.getClass(), returnType);
                }
            }
        }
        return list;
    }

    public static Class getGenericInterfaceType(Class clazz, int index) {
        return (Class<?>) ((ParameterizedTypeImpl) clazz.getGenericInterfaces()[index]).getActualTypeArguments()[0];
    }

    public static boolean fieldExistsRecursive(Class clazz, String fieldPath) throws NoSuchFieldException{
        Class currentClass = clazz;
        List<String> fieldNames = Arrays.asList(fieldPath.split("\\."));
        for(String fieldName : fieldNames)    {
            Field field = ReflectionUtils.findField(currentClass, fieldName);
            if (field != null) {
                currentClass = field.getType();
                if (Collection.class.isAssignableFrom(currentClass)) {
                    currentClass = (Class) ((ParameterizedTypeImpl)field.getGenericType()).getActualTypeArguments()[0];
                }
            } else throw new NoSuchFieldException(String.format("%s is not a field on %s. complete path was %s on %s)", fieldName, currentClass.getSimpleName(), fieldPath, clazz.getSimpleName()));
        }
        return true;
    }
}
