package com.spacetime.config.auth;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spacetime.dto.ProjectResponseDto;
import com.spacetime.model.Project;
import com.spacetime.model.User;
import com.spacetime.repository.ProjectRepository;
import com.spacetime.repository.UserRepository;

import lombok.RequiredArgsConstructor;



@Service
@RequiredArgsConstructor
public class ProjectService {
	private final ProjectRepository projectRepository;
	private final UserRepository userRepository;
	
	@Transactional
	public Long save(final Project params) throws Exception {
		Project entity = projectRepository.save(params);
		return entity.getId();
	}
	
	public List<ProjectResponseDto> findAll() {
		Sort sort = Sort.by(Direction.DESC, "id", "updateTime" );
		List<Project> list = projectRepository.findAll(sort);
		return list.stream().map(ProjectResponseDto::new).collect(Collectors.toList());
	}
	
	
	public Long update(final Long id, final Project params) throws Exception {
		Project project = projectRepository.findById(id).orElse(params);
		project.update(params.getTitle(), params.getContent(), params.getExpireTime(), params.getPrWriter(), params.getPrMember());
		return id;
	}
	
}
