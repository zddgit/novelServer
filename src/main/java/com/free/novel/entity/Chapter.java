package com.free.novel.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Chapter {
    private int id;
    private int novelId;
    private String content;
    private String title;

}
