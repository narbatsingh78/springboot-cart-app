package com.springbootcartapp.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.springbootcartapp.entities.UploadTask;
import com.springbootcartapp.entities.User;
import com.springbootcartapp.repository.UploadTaskRepository;
import com.springbootcartapp.repository.UserRepository;

@RestController
@CrossOrigin
public class SseController {
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UploadTaskRepository uploadTaskRepository;

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    @GetMapping("/sse/progress/{receivedTaskId}")
    public SseEmitter subscribe(@PathVariable String receivedTaskId, HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("X-Accel-Buffering", "no");

        SseEmitter emitter = new SseEmitter(0L);
        emitters.put(receivedTaskId, emitter);

        emitter.onCompletion(() -> emitters.remove(receivedTaskId));
        emitter.onTimeout(() -> emitters.remove(receivedTaskId));

        try {
            emitter.send(SseEmitter.event().name("init").data("Subscribed to " + receivedTaskId));
        } catch (IOException e) {
            emitter.completeWithError(e);
        }

        return emitter;
    }

    
    @GetMapping("/my-tasks")
    public List<UploadTask> getUserUploads(Authentication authentication) {
        String username = authentication.getName(); // Spring Security username
        Optional<User> user = userRepository.findByUserEmail(username);
        return uploadTaskRepository.findByUserOrderByStartedAtDesc(user);
    }

    public void sendProgress(String taskId, int inserted, int total, String status) {
        System.out.println("Sending progress update for taskId: " + taskId);
        System.out.println("Emitters map contains: " + emitters.keySet());

        SseEmitter emitter = emitters.get(taskId);
        if (emitter != null) {
            try {
                Map<String, Object> payload = Map.of(
                    "taskId", taskId,
                    "inserted", inserted,
                    "total", total,
                    "status", status
                );
                emitter.send(SseEmitter.event().name("progress").data(payload));
                if (status.equals("completed")) {
                    emitter.complete();
                }
            } catch (IOException e) {
                emitter.completeWithError(e);
            }
        } else {
            System.out.println("No emitter found for taskId: " + taskId);
        }
    }

}

