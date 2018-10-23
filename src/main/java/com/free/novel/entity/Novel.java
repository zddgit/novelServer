package com.free.novel.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Novel {
    private int id;
    private String name;
    private String author;
    private String introduction;
    private int recentUpdateTime;


}
