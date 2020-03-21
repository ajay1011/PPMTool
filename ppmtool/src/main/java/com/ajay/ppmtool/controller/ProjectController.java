package com.ajay.ppmtool.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ajay.ppmtool.model.Project;
import com.ajay.ppmtool.services.MapValidationErrorService;
import com.ajay.ppmtool.services.ProjectService;

@RestController
@RequestMapping("/api/project")
@CrossOrigin
public class ProjectController {

	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private MapValidationErrorService mapvalidationErrorService;
	
	@PostMapping("")
	public ResponseEntity<?> createNewProject( @Valid @RequestBody Project project,BindingResult result,Principal principal){
		
		ResponseEntity<?> errorMap = mapvalidationErrorService.mapValidationService(result);
		
		if(errorMap!=null) {
			return errorMap;
		}
		
		Project project1 = projectService.saveOrUpdateProject(project,principal.getName());
		return new ResponseEntity<Project>(project1, HttpStatus.CREATED);
	}
	
	@GetMapping("/{projectId}")
	public ResponseEntity<?> getProjectById(@PathVariable String projectId,Principal principal){
		
		Project project = projectService.findProjectByIdentifier(projectId,principal.getName());
		return new ResponseEntity<Project>(project,HttpStatus.OK);
	}
	
	@GetMapping("/all")
	public Iterable<Project> getAllProjects(Principal principal){
		return projectService.findAllProjects(principal.getName());
	}
	
	@DeleteMapping("/{projectId}")
	public ResponseEntity<String> deleteProject(@PathVariable String projectId,Principal principal) {
		projectService.deleteProject(projectId,principal.getName());
		return new ResponseEntity<String>("Project with project id '"+projectId+"' was deleted successfully",HttpStatus.OK);
	}
	
	
	
}
