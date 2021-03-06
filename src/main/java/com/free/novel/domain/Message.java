package com.free.novel.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Message {
    private Integer id;
    private Integer feedbackUserId;
    private Integer replyUserId;
    private Integer read;
    private Integer feedbackTime;
    private Integer replyTime;
    private String  feedback;
    private String  reply;
}
