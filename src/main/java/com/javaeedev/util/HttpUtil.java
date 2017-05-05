package com.javaeedev.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public final class HttpUtil {

    /**
     * Set cache time in seconds.
     */
    public static void setCacheTime(HttpServletResponse response, int cacheSeconds) {
        if(cacheSeconds<=0) {
            response.setHeader("Cache-Control", "no-cache");
        }
        else {
            response.setHeader("Cache-Control", "max-age=" + cacheSeconds);
            response.setDateHeader("Expires", System.currentTimeMillis() + cacheSeconds * 1000L);
        }
    }

    /**
     * Get full url including query string if any.
     */
    public static String getURL(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String queryString = request.getQueryString();
        if(queryString==null)
            return request.getRequestURI();
        return uri + "?" + queryString;
    }

    /**
     * Get Integer parameter from request. If specified parameter name 
     * is not found, the default value will be returned.
     */
    public static int getInt(HttpServletRequest request, String paramName, int defaultValue) {
        String s = request.getParameter(paramName);
        if(s==null || s.equals(""))
            return defaultValue;
        return Integer.parseInt(s);
    }

    /**
     * Get Integer parameter from request. If specified parameter name 
     * is not found, an Exception will be thrown.
     */
    public static int getInt(HttpServletRequest request, String paramName) {
        String s = request.getParameter(paramName);
        return Integer.parseInt(s);
    }

    /**
     * Get String parameter from request. If specified parameter name 
     * is not found, the default value will be returned.
     */
    public static String getString(HttpServletRequest request, String paramName, String defaultValue) {
        String s = request.getParameter(paramName);
        if(s==null || s.equals(""))
            return defaultValue;
        return s;
    }

    /**
     * Get String parameter from request. If specified parameter name 
     * is not found or empty, an Exception will be thrown.
     */
    public static String getString(HttpServletRequest request, String paramName) {
        String s = request.getParameter(paramName);
        if(s==null || s.equals(""))
            throw new NullPointerException("Null parameter: " + paramName);
        return s;
    }

    /**
     * Get Boolean parameter from request. If specified parameter name 
     * is not found, an Exception will be thrown.
     */
    public static boolean getBoolean(HttpServletRequest request, String paramName) {
        String s = request.getParameter(paramName);
        return Boolean.parseBoolean(s);
    }

    /**
     * Get Boolean parameter from request. If specified parameter name 
     * is not found, the default value will be returned.
     */
    public static boolean getBoolean(HttpServletRequest request, String paramName, boolean defaultValue) {
        String s = request.getParameter(paramName);
        if(s==null || s.equals(""))
            return defaultValue;
        return Boolean.parseBoolean(s);
    }

    /**
     * Get Long parameter from request. If specified parameter name 
     * is not found, an Exception will be thrown.
     */
    public static long getLong(HttpServletRequest request, String paramName) {
        String s = request.getParameter(paramName);
        return Long.parseLong(s);
    }

    /**
     * Get Long parameter from request. If specified parameter name 
     * is not found, the default value will be returned.
     */
    public static long getLong(HttpServletRequest request, String paramName, long defaultValue) {
        String s = request.getParameter(paramName);
        if(s==null || s.equals(""))
            return defaultValue;
        return Long.parseLong(s);
    }

    /**
     * Get float parameter from request. If specified parameter name 
     * is not found, an Exception will be thrown.
     */
    public static float getFloat(HttpServletRequest request, String paramName) {
        String s = request.getParameter(paramName);
        return Float.parseFloat(s);
    }

    @SuppressWarnings("unchecked")
    private static Map<Class, List<InvokeSetter>> cache = new HashMap<Class, List<InvokeSetter>>();

    /**
     * Bind data to a FormBean. Example: If found a parameter named "age", 
     * the object's setAge() method will be invoked if this method exists. 
     * If a setXxx() method exists but no corresponding parameter, this 
     * setXxx() method will never be invoked.<br/>
     * <b>NOTE:</b> only public setXxx() method can be invoked successfully.
     */
    @SuppressWarnings("unchecked")
    public static void bindForm(HttpServletRequest request, Object form) {
        Class c = form.getClass();
        List<InvokeSetter> invokeSetterList = cache.get(c);
        if(invokeSetterList==null) {
            // find all public set methods
            invokeSetterList = new ArrayList<InvokeSetter>();
            Method[] methods = c.getMethods();
            for(Method method : methods) {
                if(isSetter(method)) {
                    InvokeSetter setter = InvokeSetter.createInvokeSetter(
                            method,
                            getSetterName(method)
                    );
                    if(setter!=null) {
                        invokeSetterList.add(setter);
                    }
                }
            }
            cache.put(c, invokeSetterList);
        }
        // invoke each setter:
        for(InvokeSetter setter : invokeSetterList) {
            String value = request.getParameter(setter.getProperty());
            if(value!=null)
                setter.invoke(form, value);
        }
    }

    private static boolean isSetter(Method method) {
        String name = method.getName();
        return name.startsWith("set") && name.length()>3
            && method.getParameterTypes().length==1
            && method.getReturnType().equals(void.class);
    }

    private static String getSetterName(Method method) {
        String name = method.getName();
        char c = name.charAt(3);
        return Character.toLowerCase(c) + name.substring(4);
    }

}

class InvokeSetter {

    @SuppressWarnings("unchecked")
    private Class argument;
    private Method method;
    private String property;

    @SuppressWarnings("unchecked")
    private static Set<Class> supportedTypes = new HashSet<Class>();

    static {
        supportedTypes.add(String.class);
        supportedTypes.add(Date.class);
        supportedTypes.add(boolean.class);
        supportedTypes.add(Boolean.class);
        supportedTypes.add(int.class);
        supportedTypes.add(Integer.class);
        supportedTypes.add(long.class);
        supportedTypes.add(Long.class);
        supportedTypes.add(char.class);
        supportedTypes.add(Character.class);
        supportedTypes.add(short.class);
        supportedTypes.add(Short.class);
        supportedTypes.add(float.class);
        supportedTypes.add(Float.class);
        supportedTypes.add(double.class);
        supportedTypes.add(Double.class);
    }

    private InvokeSetter() {}

    public String getProperty() {
        return property;
    }

    @SuppressWarnings("unchecked")
    public static InvokeSetter createInvokeSetter(Method method, String property) {
        if(!supportedTypes.contains(method.getParameterTypes()[0]))
            return null;
        InvokeSetter setter = new InvokeSetter();
        setter.method = method;
        setter.argument = method.getParameterTypes()[0];
        setter.property = property;
        return setter;
    }

    public void invoke(Object target, String value) {
        try {
            Object arg = parseString(value);
            method.invoke(target, arg);
        }
        catch(NumberFormatException e) {
            /* do nothing */
        }
        catch(IllegalArgumentException e) {
            e.printStackTrace();
        }
        catch(IllegalAccessException e) {
            e.printStackTrace();
        }
        catch(InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private Object parseString(String value) {
        if(argument.equals(String.class))
            return value;
        // for non-string type:
        if(argument.equals(int.class) || argument.equals(Integer.class))
            return Integer.valueOf(value);
        if(argument.equals(boolean.class) || argument.equals(Boolean.class))
            return Boolean.valueOf(value);
        if(argument.equals(long.class) || argument.equals(Long.class))
            return Long.valueOf(value);
        if(argument.equals(float.class) || argument.equals(Float.class))
            return Float.valueOf(value);
        if(argument.equals(double.class) || argument.equals(Double.class))
            return Double.valueOf(value);
        if(argument.equals(short.class) || argument.equals(Short.class))
            return Short.valueOf(value);
        if(argument.equals(char.class) || argument.equals(Character.class))
            return value.length()==0 ? null : Character.valueOf(value.charAt(0));
        if(argument.equals(Date.class)) {
            Date d = DateUtil.parseDateTime(value);
            if(d==null)
                d = DateUtil.parseDate(value);
            return d;
        }
        return null;
    }
}
