package com.example.tiktok.entity.dto;

import com.example.tiktok.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {
    private Long id;
    private String image;
    private String title;
    private String video;
    private String content;

    public PostDto(Post post){
        BeanUtils.copyProperties(post,this);
    }
}
