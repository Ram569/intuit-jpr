package com.app.jpr.repository;

import com.app.jpr.model.Projects;
import com.app.jpr.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectsRepository extends JpaRepository<Projects, Integer> {
}
