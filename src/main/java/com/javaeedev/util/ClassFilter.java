package com.javaeedev.util;

/**
 * A class filter.
 * 
 * @author Xuefeng
 */
public interface ClassFilter {

    /**
     * If accept the class passed as argument, return true, otherwize false.
     */
    boolean accept(Class<?> clazz);

}
