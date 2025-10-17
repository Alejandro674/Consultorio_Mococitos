package com.desarrollo.repositories;

import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

/**
 * @author ivanLuna
 */
@Transactional
public class DesarrolloRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements DesarrolloJpa<T, ID> {

    public DesarrolloRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super( entityInformation, entityManager);
    }

}
