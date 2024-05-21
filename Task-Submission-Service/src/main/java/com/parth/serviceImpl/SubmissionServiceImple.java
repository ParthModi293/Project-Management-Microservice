package com.parth.serviceImpl;

import com.parth.model.Submission;
import com.parth.model.TaskDto;
import com.parth.repository.SubmissionRepository;
import com.parth.service.SubmissionService;
import com.parth.service.TaskService;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SubmissionServiceImple implements SubmissionService {


    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private TaskService taskService;

    int attempts=0;


    @Override
//    @CircuitBreaker(name = "taskBreaker", fallbackMethod = "taskBreakerFallBackMethod")
    @Retry(name = "taskBreaker", fallbackMethod = "submitTaskFallback")
    public Submission submitTask(Long taskId, String githubLink, Long userId, String jwt) throws Exception {
        System.out.println("Attempts: "+ ++attempts);

        TaskDto task = taskService.getTaskById(taskId,jwt);
        if(task!=null){


            Submission submission = new Submission();

            submission.setTaskId(taskId);
            submission.setUserId(userId);
            submission.setGithubLink(githubLink);
            submission.setSubmissionTime(LocalDateTime.now());

            return submissionRepository.save(submission);


        }
        else {
            throw  new Exception("Task not found with taskId "+taskId);

        }


    }

//    public String submitTaskFallback(Long taskId, String githubLink, Long userId, String jwt, Throwable t) {
//        System.out.println("Fallback called due to: " + t.getMessage());
//        Submission fallbackSubmission = new Submission();
//        fallbackSubmission.setTaskId(taskId);
//        fallbackSubmission.setUserId(userId);
//        fallbackSubmission.setGithubLink(githubLink);
//        fallbackSubmission.setSubmissionTime(LocalDateTime.now());
//        fallbackSubmission.setStatus("Failed");
//       fallbackSubmission.setErrorMessage("Task submission failed due to: " + t.getMessage());
//
//        List<String> l = new ArrayList<>();
//        l.add("Hello");
//       return "gello" ;
//    }


    @Override
    public Submission getTaskSubmissionById(Long submissionId) throws Exception {
        return submissionRepository.findById(submissionId).orElseThrow(()-> new Exception("submission not found with id"+submissionId));
    }

    @Override
    public List<Submission> getAllTaskSubmission() {

        return submissionRepository.findAll();
    }

    @Override
    public List<Submission> getTaskSubmissionsByTaskId(Long taskId) {
        return submissionRepository.findByTaskId(taskId);
    }

    @Override
    public Submission acceptDeclineSubmission(long id, String status) throws Exception {

        Submission submission = getTaskSubmissionById(id);
        submission.setStatus(status);
        if(status.equals("ACCEPT")){
            taskService.completeTask(submission.getTaskId());
        }
        return submissionRepository.save(submission);
    }
}
