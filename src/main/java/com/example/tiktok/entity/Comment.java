package com.example.tiktok.entity;

import com.example.tiktok.entity.basic.BaseEntity;
import com.example.tiktok.entity.dto.CommentDto;
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
@Table(name = "comments")
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String comment;
    public Comment(CommentDto commentDto){
        BeanUtils.copyProperties(commentDto,this);
    }
}
