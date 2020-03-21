package com.ajay.ppmtool.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ajay.ppmtool.exceptions.ProjectIdException;
import com.ajay.ppmtool.exceptions.ProjectNotFoundException;
import com.ajay.ppmtool.model.BacklogProject;
import com.ajay.ppmtool.model.Project;
import com.ajay.ppmtool.model.User;
import com.ajay.ppmtool.repositories.BacklogRepository;
import com.ajay.ppmtool.repositories.ProjectRepository;
import com.ajay.ppmtool.repositories.UserRepository;

@Service
public class ProjectService {
	
	@Autowired
	private ProjectRepository projectRepository;
	
	@Autowired
	private BacklogRepository backlogRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	
	public Project saveOrUpdateProject(Project project, String username) {
		
		String identifier = project.getProjectIdentifier().toUpperCase();
		
		if(project.getId()!=null) {
			Project existingproject = projectRepository.findByProjectIdentifier(identifier);
			if(existingproject!=null && !existingproject.getProjectLeader().equals(username)) {
				throw new ProjectNotFoundException("Project not found in your account");
			}
			else if(existingproject==null) {
				throw new ProjectNotFoundException("Project with ID: '\"+project.getProjectIdentifier()+\"' cannot be updated because it doesn't exist");
			}
		}
		
		try {
			project.setProjectIdentifier(identifier);
			
			User user = userRepository.findByUsername(username);
			
			project.setUser(user);
			project.setProjectLeader(user.getUsername());
			
			if(project.getId()==null) {
				BacklogProject backlog = new BacklogProject();
				project.setBacklog(backlog);
				backlog.setProject(project);
				backlog.setProjectIdentifier(identifier);
			}
			
			if(project.getId()!=null) {
				project.setBacklog(backlogRepository.findByProjectIdentifier(identifier));
			}
			return projectRepository.save(project);
		}
		catch(Exception e) {
			throw new ProjectIdException("Project ID '"+project.getProjectIdentifier().toUpperCase()+"' already exists");
		}
		
	}
	
	public Project findProjectByIdentifier(String projectId,String username) {
		
		Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());
		if(project==null) {
			throw new ProjectIdException("Project'"+projectId+"' does not exist");
		}
		
		if(!project.getProjectLeader().equals(username)) {
			throw new ProjectNotFoundException("Project not found in your account");
		}
		return project;
	}
	
	public Iterable<Project> findAllProjects(String username){
		return projectRepository.findAllByProjectLeader(username);
	}
	
	public void deleteProject(String projectId,String username) {
		/*Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());
		
		if(project==null) {
			throw new ProjectIdException("Cannot delete project '"+projectId+"' as it does not exist");
		}*/
		
		projectRepository.delete(findProjectByIdentifier(projectId, username));
	}
	
	

}
