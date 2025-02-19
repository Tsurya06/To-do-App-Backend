package com.login.task.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.login.task.modal.Comment;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @PostMapping("/task/{taskId}")
    public ResponseEntity<Comment> addComment(
        @PathVariable Long taskId,
        @RequestBody String content
    ){
        return null;
    }

    @GetMapping("/task/{taskId}")
    public ResponseEntity<List<Comment>> getTaskComments(@PathVariable Long taskId){
        return null;
    }
} 