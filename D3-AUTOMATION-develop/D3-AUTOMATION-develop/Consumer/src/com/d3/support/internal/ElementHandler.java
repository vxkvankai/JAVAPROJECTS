package com.d3.support.internal;

import static com.d3.support.internal.ImplementedByProcessor.getWrapperClass;

import com.d3.exceptions.D3FactoryException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ElementHandler implements InvocationHandler {
    private final ElementLocator locator;
    private final Class<?> wrappingType;

    public <T> ElementHandler(Class<T> interfaceType, ElementLocator locator) {
        this.locator = locator;
        if (!Element.class.isAssignableFrom(interfaceType)) {
            throw new D3FactoryException("interface not assignable to Element.");
        }

        this.wrappingType = getWrapperClass(interfaceType);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] objects) throws Throwable {
        WebElement element = locator.findElement();

        if ("getWrappedElement".equals(method.getName())) {
            return element;
        }

        Constructor cons = wrappingType.getConstructor(WebElement.class);
        Object thing = cons.newInstance(element);
        try {
            return method.invoke(wrappingType.cast(thing), objects);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }
}
