package com.parth.controller;

import com.parth.model.Submission;
import com.parth.model.UserDto;
import com.parth.service.SubmissionService;
import com.parth.service.TaskService;
import com.parth.service.UserService;
import com.parth.serviceImpl.SubmissionServiceImple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/submissions")
public class SubmissionController {

    @Autowired
    private SubmissionService submissionService;

    @Autowired
    private UserService userService;

    @Autowired
   private TaskService taskService;

    @PostMapping()
    public ResponseEntity<Submission> submitTask(
            @RequestParam Long task_id,
            @RequestParam String github_link,
            @RequestHeader("Authorization") String jwt
    )throws  Exception{
        UserDto user = userService.getUserProfile(jwt);
        Submission submission = submissionService.submitTask(task_id,github_link,user.getId(), jwt);
        return  new ResponseEntity<>(submission, HttpStatus.CREATED);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Submission> getSubmissionById(
            @PathVariable Long id,
            @RequestHeader("Authorization") String jwt
    )throws  Exception{
        UserDto user = userService.getUserProfile(jwt);
        Submission submission = submissionService.getTaskSubmissionById(id);
        return  new ResponseEntity<>(submission, HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<List<Submission>> getAllSubmission(
            @RequestHeader("Authorization") String jwt
    )throws  Exception{
        UserDto user = userService.getUserProfile(jwt);
        List<Submission> submission = submissionService.getAllTaskSubmission();
        return  new ResponseEntity<>(submission, HttpStatus.OK);
    }

    @GetMapping("/task/{taskId}")
    public ResponseEntity<List<Submission>> getAllTasksSubmission(
            @PathVariable Long taskId,
            @RequestHeader("Authorization") String jwt
    )throws  Exception{
        UserDto user = userService.getUserProfile(jwt);
        List<Submission> submission = submissionService.getTaskSubmissionsByTaskId(taskId);
        return  new ResponseEntity<>(submission, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Submission> AcceptOrDeclineSubmission(
            @PathVariable Long id,
            @RequestParam("status") String status,
            @RequestHeader("Authorization") String jwt
    )throws  Exception{
        UserDto user = userService.getUserProfile(jwt);
        Submission submission = submissionService.acceptDeclineSubmission(id,status);
        return  new ResponseEntity<>(submission, HttpStatus.OK);
    }





}
