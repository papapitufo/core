package com.control.core.repository;

import com.control.core.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    
    Optional<Permission> findByName(String name);
    
    List<Permission> findByCategory(String category);
    
    @Query("SELECT p FROM Permission p ORDER BY p.category, p.name")
    List<Permission> findAllOrderByCategory();
    
    @Query("SELECT DISTINCT p.category FROM Permission p ORDER BY p.category")
    List<String> findAllCategories();
    
    @Query("SELECT p FROM Permission p WHERE p.name IN :names")
    List<Permission> findByNameIn(@Param("names") List<String> names);
    
    @Query("SELECT p FROM Permission p LEFT JOIN FETCH p.roles WHERE p.id = :id")
    Optional<Permission> findByIdWithRoles(@Param("id") Long id);
    
    @Query("SELECT p FROM Permission p LEFT JOIN FETCH p.users WHERE p.id = :id")
    Optional<Permission> findByIdWithUsers(@Param("id") Long id);
    
    boolean existsByName(String name);
}
