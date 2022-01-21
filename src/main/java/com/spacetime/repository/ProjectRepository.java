package com.spacetime.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.spacetime.model.Project;
import com.spacetime.model.User;

public interface ProjectRepository extends JpaRepository<Project, Long>{
	public Project findByTitle(String title);
	public Project findById(long id);

}
