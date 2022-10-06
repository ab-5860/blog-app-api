package com.blog.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blog.entities.Comment;
import com.blog.entities.Post;
import com.blog.exceptions.ResourceNotFoundException;
import com.blog.payloads.CommentDTO;

import com.blog.repositories.CommentRepo;
import com.blog.repositories.PostRepo;
import com.blog.services.CommentService;


@Service
public class CommentServiceImpl implements CommentService{


    @Autowired
    private PostRepo postRepo;

    @Autowired
    private CommentRepo commentRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CommentDTO createComment(CommentDTO commentDTO, Integer postId) {
        

        Post post = this.postRepo.findById(postId).orElseThrow(() -> new ResourceNotFoundException(
                                                "Post", "post Id", postId));
        
        Comment comment = this.modelMapper.map(commentDTO, Comment.class);
        
        comment.setPost(post);
        Comment savedComment = this.commentRepo.save(comment);

        return this.modelMapper.map(savedComment, CommentDTO.class);
    }

    @Override
    public void deleteComment(Integer commentId) {
        
        Comment com = this.commentRepo.findById(commentId).orElseThrow(()-> new ResourceNotFoundException(
                                        "Comment" , "comment Id", commentId));
                                        
        this.commentRepo.delete(com);
    }
    
}
