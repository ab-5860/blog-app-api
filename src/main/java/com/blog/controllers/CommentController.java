package com.blog.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.blog.payloads.ApiResponse;
import com.blog.payloads.CommentDTO;
import com.blog.services.CommentService;

@RestController
@RequestMapping("/api/")
public class CommentController {
    
    @Autowired
    private CommentService commentService;

    @PostMapping("/post/{postId}/comments")
    public ResponseEntity<CommentDTO> createComment(
        @RequestBody CommentDTO comment, @PathVariable Integer postId)
    {
        CommentDTO createdComment = this.commentService.createComment(comment, postId);
        return new ResponseEntity<CommentDTO>(createdComment,HttpStatus.OK);
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<ApiResponse> deleteComment(@PathVariable Integer commentId)
    {
        this.commentService.deleteComment(commentId);

        return new ResponseEntity<ApiResponse>(new ApiResponse("Comment deletd successfully",true),
                                HttpStatus.OK);
    }

}
