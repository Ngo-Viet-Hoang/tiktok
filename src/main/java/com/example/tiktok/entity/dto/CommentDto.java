package com.example.tiktok.entity.dto;

import com.example.tiktok.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private Long id;
    private String comment;

    public CommentDto(Comment comment){
        BeanUtils.copyProperties(comment,this);
    }
}
