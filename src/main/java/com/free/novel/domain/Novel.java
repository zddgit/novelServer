package com.free.novel.domain;

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
    private int recentChapterUpdateId;
    private int sourceid;
    private int tagid;
    private byte[] cover;


}
