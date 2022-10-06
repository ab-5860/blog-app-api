package com.blog.services.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.blog.entities.Category;
import com.blog.entities.Post;
import com.blog.entities.User;
import com.blog.exceptions.ResourceNotFoundException;
import com.blog.payloads.PostDTO;
import com.blog.payloads.PostResponse;
import com.blog.repositories.CategoryRepo;
import com.blog.repositories.PostRepo;
import com.blog.repositories.UserRepo;
import com.blog.services.PostService;

@Service
public class PostServiceImpl implements PostService{

    @Autowired
    private PostRepo postRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CategoryRepo categoryRepo;

    @Override
    public PostDTO createPost(PostDTO postDTO,Integer userId, Integer categoryId) {
        
        User user = this.userRepo.findById(userId).orElseThrow(()-> new ResourceNotFoundException(
                                                                "User ", "user Id", userId));
        Category category = this.categoryRepo.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException(
                                                                "Category", "category Id", categoryId));

        Post post = this.modelMapper.map(postDTO, Post.class);
        post.setImageName("default.png");
        post.setAddedDate(new Date());
        post.setUser(user);
        post.setCategory(category);

        Post createdPost = this.postRepo.save(post);
        return this.modelMapper.map(createdPost, PostDTO.class);
    }

    @Override
    public PostDTO updatePost(PostDTO postDTO, Integer postId) {
        
        Post post = this.postRepo.findById(postId).orElseThrow(()-> new ResourceNotFoundException(
                                        "Post", "post Id", postId));   
        
        post.setTitle(postDTO.getTitle());
        post.setContent(postDTO.getContent());
        post.setImageName(postDTO.getImageName());


        Post updatedPost = this.postRepo.save(post);
        return this.modelMapper.map(updatedPost, PostDTO.class);
    }

    @Override
    public void deletePost(Integer postId) {
        
        Post post = this.postRepo.findById(postId).orElseThrow(()-> new ResourceNotFoundException(
                                    "Post", "post Id", postId));
        
        this.postRepo.delete(post);
        
    }

    @Override
    public PostResponse getAllPosts(Integer pageNumber, Integer pageSize,String sortBy,String sortDir) {


        Sort sort = null;
        if(sortDir.equalsIgnoreCase("asc"))
        {
            sort = Sort.by(sortBy).ascending();
        }else{
            sort = Sort.by(sortBy).descending();
        }
        Pageable p = PageRequest.of(pageNumber, pageSize,sort);//paging and sorting

        Page<Post> pagePost = this.postRepo.findAll(p);
        List<Post> allPosts = pagePost.getContent();

        List<PostDTO> postDTOs = allPosts.stream().map((post) -> this.modelMapper.map(post, PostDTO.class))
                                                .collect(Collectors.toList());
        
        
        PostResponse postResponse = new PostResponse();
        postResponse.setContent(postDTOs);
        postResponse.setPageNumber(pagePost.getNumber());
        postResponse.setPageSize(pagePost.getSize());
        postResponse.setTotalElements(pagePost.getTotalElements());
        postResponse.setTotalPages(pagePost.getTotalPages());
        postResponse.setLastPage(pagePost.isLast());

        return postResponse;
    }

    @Override
    public PostDTO getPostById(Integer postId) {
        
        Post post = this.postRepo.findById(postId).orElseThrow(() -> new ResourceNotFoundException(
                                                        "Post", "post Id", postId));
        
        return this.modelMapper.map(post, PostDTO.class);
    }

    @Override
    public List<PostDTO> getAllPostsByCategory(Integer categoryId) {
        
        Category cat = this.categoryRepo.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException(
                                            "Category", "category Id", categoryId));
        List<Post> posts = this.postRepo.findByCategory(cat);
        List<PostDTO> postDtos = posts.stream().map((post -> this.modelMapper.map(post, PostDTO.class))).collect(Collectors.toList());

        return postDtos;

    }

    @Override
    public List<PostDTO> getAllPostsByUser(Integer userId) {
        
        User user = this.userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException(
                                                    "User","user Id", userId));

        List<Post> posts = this.postRepo.findByUser(user);
        List<PostDTO> postDtos = posts.stream().map((post -> this.modelMapper.map(post, PostDTO.class))).collect(Collectors.toList());
        return postDtos;
    }

    @Override
    public List<PostDTO> searchPosts(String keyword) {
        
        List<Post> posts = this.postRepo.findByTitleContaining(keyword);
        List<PostDTO> postDTOs = posts.stream().map((post) -> this.modelMapper.map(post, PostDTO.class)).collect(Collectors.toList());

        return postDTOs;
    }
    
}
