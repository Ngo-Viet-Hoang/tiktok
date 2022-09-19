package com.example.tiktok.entity;

import com.example.tiktok.entity.basic.BaseEntity;
import com.example.tiktok.entity.dto.PostDto;
import lombok.*;
import org.springframework.beans.BeanUtils;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "posts")
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String image;
    private String title;
    private String video;
    private String content;

    public Post(PostDto postDto){
        BeanUtils.copyProperties(postDto,this);
    }
}
