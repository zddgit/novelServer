package com.free.novel.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Dictionary {
    private int id;
    private String name;
    private String type;
    private int status;
}
