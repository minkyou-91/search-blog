package com.searchblog.api.adpater.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QueryRepository extends JpaRepository<QueryEntity, Long> {
    Optional<QueryEntity> findByQueryText(String queryText);
}
