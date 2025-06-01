package com.springbootcartapp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springbootcartapp.entities.UploadTask;
import com.springbootcartapp.entities.User;

public interface UploadTaskRepository extends JpaRepository<UploadTask, String> {

	List<UploadTask> findByUserOrderByStartedAtDesc(Optional<User> user);
}

