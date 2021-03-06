package com.free.novel.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Chapter {
    private int chapterId;
    private int novelId;
    private String title;
    private String source;
    private byte[] content;
    private String content_str;
}
