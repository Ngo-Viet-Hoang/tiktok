package com.example.tiktok.restapi;

import com.example.tiktok.entity.Post;
import com.example.tiktok.entity.dto.PostDto;
import com.example.tiktok.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/posts")
public class PostRestApi {
    final PostService postService;
    @PostMapping
    public ResponseEntity<?> create(@RequestBody PostDto post) {
        try {
            return ResponseEntity.ok(postService.create(post));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Action fails.");
        }
    }
    @PutMapping
    public ResponseEntity<?> update(@RequestBody PostDto post) {
        try {

            Optional<Post> optionalPost = postService.findById(post.getId());
            if (!optionalPost.isPresent()) {
                return ResponseEntity.badRequest().body("Post not found");
            }
            Post existPost = optionalPost.get();
            postService.update(post);
            return ResponseEntity.ok( new PostDto(existPost));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Action fails.");
        }
    }
    @GetMapping("{id}")
    public ResponseEntity<?> getDetail(@PathVariable Long id) {
        try {
            Optional<Post> optionalPost = postService.findById(id);
            if (!optionalPost.isPresent()) {
                return ResponseEntity.badRequest().body("Post not found");
            }
            return ResponseEntity.ok( new PostDto(optionalPost.get()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Action fails");
        }
    }
        @DeleteMapping( "{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        if (!postService.findById(id).isPresent()) {
            ResponseEntity.badRequest().build();
        }
        postService.deletedById(id);
        return ResponseEntity.ok().build();
    }
    @GetMapping
//    public ResponseEntity<?>  getList(){
//        return ResponseEntity.ok(postService.findAll());
//    }
    public ResponseEntity<?> getList(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "limit",defaultValue = "10") int limit){
        try {
            Pageable pageable = PageRequest.of(page -1, limit, Sort.by("createdAt").descending());
            return ResponseEntity.ok(postService.findAll(pageable).map(PostDto::new));
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Errors");
        }
    }
}
