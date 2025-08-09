package com.control.core.security.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom annotation for marking methods that require specific permissions.
 * This annotation should be used in combination with method-level security.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequirePermission {
    
    /**
     * The permission name required to access the method
     */
    String value();
    
    /**
     * Custom error message when access is denied
     */
    String message() default "Access denied";
}
