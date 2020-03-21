package com.ajay.ppmtool.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ajay.ppmtool.exceptions.ProjectNotFoundException;
import com.ajay.ppmtool.model.BacklogProject;
import com.ajay.ppmtool.model.Project;
import com.ajay.ppmtool.model.ProjectTask;
import com.ajay.ppmtool.repositories.BacklogRepository;
import com.ajay.ppmtool.repositories.ProjectRepository;
import com.ajay.ppmtool.repositories.ProjectTaskRepository;

@Service
public class ProjectTaskService {

	@Autowired
	private BacklogRepository backlogRepository;
	
	@Autowired
	private ProjectTaskRepository projectTaskRepository;
	
	@Autowired
	private ProjectRepository projectRepository;
	
	@Autowired
	private ProjectService projectService;
	
	public ProjectTask addProjectTask(String projectIdentifier,ProjectTask projectTask,String username) {
		
		// handle exceptions when project not found
		
	
			//PTs to be added to specific project, project != null, backlog exists
			//BacklogProject backlog = backlogRepository.findByProjectIdentifier(projectIdentifier);
			BacklogProject backlog = projectService.findProjectByIdentifier(projectIdentifier, username).getBacklog();
			// set the backlog to pt
			projectTask.setBacklog(backlog);
			// we want our project task sequence to be like, IDPro-1,IDPRO-2...
			
			Integer backLogSequence = backlog.getPTSequence();
			// update the bl sequence
			backLogSequence++;
			
			backlog.setPTSequence(backLogSequence);
			
			// add sequence tp project task
			projectTask.setProjectSequence(projectIdentifier+"-"+backLogSequence);
			projectTask.setProjectIdentifier(projectIdentifier);
			
			// initial priority when priority is null
			
			if(projectTask.getPriority()==null||projectTask.getPriority()==0) { // in the future we need to handle projectTask.getPriority()==0
				projectTask.setPriority(3);
			}
			//initial status when status is null
			if(projectTask.getStatus()==null || projectTask.getStatus()=="") {
				projectTask.setStatus("TO_DO");
			}
			
			return projectTaskRepository.save(projectTask);
		
		
	}

	public List<ProjectTask> findBacklogById(String id,String username) {
		// TODO Auto-generated method stub
		
		/*Project project = projectRepository.findByProjectIdentifier(id);
		
		if(project==null) {
			throw new ProjectNotFoundException("Project with id: '"+id+"' doesn't exist");
		}*/
		projectService.findProjectByIdentifier(id, username);
		return projectTaskRepository.findByProjectIdentifierOrderByPriority(id);
	}
	
	public ProjectTask findPTByProjectSequence(String backlog_id,String pt_id,String username) {
		// make sure we are extracting from existing backlog
		/*BacklogProject backlog = backlogRepository.findByProjectIdentifier(backlog_id);
		
		if(backlog == null) {
			throw new ProjectNotFoundException("Project with id '"+backlog_id+"' doesn't exist");
		}*/
		projectService.findProjectByIdentifier(backlog_id, username);
		
		// make sure the project task exists
		
		ProjectTask projectTask = projectTaskRepository.findByProjectSequence(pt_id);
		
		if(projectTask==null) {
			throw new ProjectNotFoundException("Project task with id '"+pt_id+"' doesn't exist");
		}
		
		// make sure we are extracting PT from correct backlog
		if(!projectTask.getProjectIdentifier().equals(backlog_id)) {
			throw new ProjectNotFoundException("Project task with id '"+pt_id+"' doesn't exist in project: "+backlog_id);
		}
		
		return projectTask;
	}
	
	public ProjectTask updateByProjectSequence(ProjectTask updatedTask,String backlog_id,String pt_id,String username) {
		
		ProjectTask projectTask = findPTByProjectSequence(backlog_id, pt_id,username);
		
		projectTask = updatedTask;
		
		return projectTaskRepository.save(projectTask);
	}
	
	public void deletePTByProjectSequence(String backlog_id,String pt_id,String username) {
		
		ProjectTask projectTask = findPTByProjectSequence(backlog_id, pt_id,username);
		projectTaskRepository.delete(projectTask);
	}
	
}
