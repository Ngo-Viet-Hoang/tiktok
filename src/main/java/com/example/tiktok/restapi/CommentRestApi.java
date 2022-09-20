package com.example.tiktok.restapi;

import com.example.tiktok.entity.Comment;
import com.example.tiktok.entity.Post;
import com.example.tiktok.entity.dto.CommentDto;
import com.example.tiktok.entity.dto.PostDto;
import com.example.tiktok.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/v1/comments")
public class CommentRestApi {
    final CommentService commentService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CommentDto comment){
        try {
            return ResponseEntity.ok(commentService.create(comment));
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Errors");
        }
    }
    @GetMapping
    public ResponseEntity<?>  getList(){
        return ResponseEntity.ok(commentService.findAll());
    }
    @GetMapping("{id}")
    public ResponseEntity<?> getDetail(@PathVariable Long id) {
        try {
            Optional<Comment> optionalComment = commentService.findById(id);
            if (!optionalComment.isPresent()) {
                return ResponseEntity.badRequest().body("Post not found");
            }
            return ResponseEntity.ok( new CommentDto(optionalComment.get()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Action fails");
        }
    }
    @DeleteMapping( "{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        if (!commentService.findById(id).isPresent()) {
            ResponseEntity.badRequest().build();
        }
        commentService.deleteById(id);
        return ResponseEntity.ok().build();
    }
    @PutMapping
    public ResponseEntity<?> update(@RequestBody CommentDto comment) {
        try {

            Optional<Comment> optionalComment = commentService.findById(comment.getId());
            if (!optionalComment.isPresent()) {
                return ResponseEntity.badRequest().body("Post not found");
            }
            Comment existComment = optionalComment.get();
            commentService.update(comment);
            return ResponseEntity.ok( new CommentDto(existComment));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Action fails.");
        }
    }
}
