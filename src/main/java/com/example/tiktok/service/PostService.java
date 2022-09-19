package com.example.tiktok.service;

import com.example.tiktok.entity.Post;
import com.example.tiktok.entity.dto.PostDto;
import com.example.tiktok.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    final PostRepository postRepository;
    public Post save(Post post){
        return postRepository.save(post);
    }
    public List<Post> findAll(){
        return postRepository.findAll();
    }
    public Optional<Post> findById(Long id){
        return postRepository.findById(id);
    }
    public void deletedById(Long id) {
        postRepository.deleteById(id);
    }
    public Post create(PostDto postDto){
        if(null == postDto.getImage() || postDto.getImage().equals("")){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Image is not null");
        }
        if(null == postDto.getContent() || postDto.getContent().equals("")){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Content is not null");
        }
        if(null == postDto.getTitle() || postDto.getTitle().equals("")){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Title is not null");
        }
//        if(null == postDto.getVideo() || postDto.getVideo().equals("")){
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Video is not null");
//        }
        Post post = new  Post(postDto);
        return postRepository.save(post);
    }
    public Post update(PostDto post){
        Optional<Post> optionalPost = postRepository.findById(post.getId());
        if(!optionalPost.isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Post is not found");
        }
        Post exitPost = optionalPost.get();
        exitPost.setId(post.getId());
        exitPost.setImage(post.getImage());
        exitPost.setTitle(post.getTitle());
//        exitPost.setVideo(post.getVideo());
        exitPost.setContent(post.getContent());
        exitPost.setUpdatedAt(LocalDateTime.now());
        return  postRepository.save(exitPost);
     }
}
