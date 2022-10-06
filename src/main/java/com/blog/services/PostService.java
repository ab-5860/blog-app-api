package com.blog.services;

import java.util.List;

import com.blog.payloads.PostDTO;
import com.blog.payloads.PostResponse;

public interface PostService {
    
    //create

    PostDTO createPost(PostDTO postDTO, Integer postId, Integer categoryId);

    //update

    PostDTO updatePost(PostDTO postDTO, Integer postId);

    // delete

    void deletePost(Integer postId);

    //get all posts
    PostResponse getAllPosts(Integer pageNumber, Integer pageSize,String sortBy,String sortDir);

    //get single post
    PostDTO getPostById(Integer postId);

    //get all posts by Category
    List<PostDTO> getAllPostsByCategory(Integer categoryId);

    // get all posts by Users
    List<PostDTO> getAllPostsByUser(Integer userId);

    //serach posts
    List<PostDTO> searchPosts(String keyword);
}
