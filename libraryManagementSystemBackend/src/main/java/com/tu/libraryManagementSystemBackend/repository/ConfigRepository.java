package com.tu.libraryManagementSystemBackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tu.libraryManagementSystemBackend.model.Config;
import java.util.Optional;
import java.util.UUID;

public interface ConfigRepository extends JpaRepository<Config, UUID> {
    Optional<Config> findByKey(String key);
}
