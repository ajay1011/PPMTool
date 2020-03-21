package com.ajay.ppmtool.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ajay.ppmtool.model.BacklogProject;

@Repository
public interface BacklogRepository extends CrudRepository<BacklogProject, Long> {

	BacklogProject findByProjectIdentifier(String Identifier);
}
