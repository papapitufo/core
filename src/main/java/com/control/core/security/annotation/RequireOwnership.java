package com.control.core.security.annotation;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for checking if user is owner of a resource or has admin role
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("@authorizationService.isOwnerOrAdmin(authentication, #userId)")
public @interface RequireOwnership {
    String userIdParam() default "userId";
}
