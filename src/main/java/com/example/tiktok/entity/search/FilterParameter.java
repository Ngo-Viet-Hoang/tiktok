package com.example.tiktok.entity.search;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FilterParameter {
    private String keyword;
    private int page;
    private int limit;
    private String accountId;
    private int status;
}
