package com.example.tiktok.service;

import com.example.tiktok.entity.Comment;
import com.example.tiktok.entity.Post;
import com.example.tiktok.entity.dto.CommentDto;
import com.example.tiktok.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    final CommentRepository commentRepository;

    public Comment save(Comment comment){
        return commentRepository.save(comment);
    }
    public Optional<Comment> findById(Long id){
        return commentRepository.findById(id);
    }
    public void deleteById(Long id){
        commentRepository.deleteById(id);
    }
    public List<Comment> findAll(){
        return commentRepository.findAll();
    }


    public Comment create(CommentDto commentDto){
     Comment comment = new Comment(commentDto);
     return commentRepository.save(comment);
    }
    public Comment update(CommentDto comment){
        Optional<Comment> optionalComment = commentRepository.findById(comment.getId());
        if(!optionalComment.isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Comment is not found");
        }
        Comment exitComment = optionalComment.get();
        exitComment.setComment(comment.getComment());
        return commentRepository.save(exitComment);
    }
}
