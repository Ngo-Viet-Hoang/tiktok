package com.example.tiktok.restapi;

import com.example.tiktok.entity.Account;
import com.example.tiktok.entity.Post;
import com.example.tiktok.entity.Respondata;
import com.example.tiktok.entity.dto.PostDto;
import com.example.tiktok.repository.AccountRepository;
import com.example.tiktok.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/posts")
public class PostRestApi {
    final PostService postService;


    @Autowired
    AccountRepository accountRepository;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody PostDto post, Authentication principal) {
        try {

            System.out.printf("aaa: " + principal.getName());
            String adminId = principal.getName();

            Optional<Account> op = accountRepository.findAccountByUsername(adminId);


            if (!op.isPresent()){

            }
                Account account = op.get();

            return ResponseEntity.ok(new Respondata(postService.create(post,account.getId()),adminId));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Action fails.");
        }
    }
    @PutMapping
    public ResponseEntity<?> update(@RequestBody PostDto post, Authentication principal) {
        try {
            String adminId = principal.getName();

            Optional<Account> op = accountRepository.findAccountByUsername(adminId);


            if (!op.isPresent()){

            }
            Account account = op.get();
            return ResponseEntity.ok(new Respondata(postService.update(post,account.getId()),adminId));
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
    public ResponseEntity<?>  getList(){

        return ResponseEntity.ok(postService.findAll());
    }
//    public ResponseEntity<?> getList(
//            @RequestParam(value = "page", defaultValue = "1") int page,
//            @RequestParam(value = "limit",defaultValue = "10") int limit){
//        try {
//            Pageable pageable = PageRequest.of(page -1, limit, Sort.by("createdAt").descending());
//            return ResponseEntity.ok(postService.findAll(pageable).map(PostDto::new));
//        }catch (Exception e){
//            return ResponseEntity.badRequest().body("Errors");
//        }
//    }
}
