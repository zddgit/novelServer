package com.free.novel.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {
    private Integer id;
    private String nick;
    private String pwd;
    private Integer lastLoginTime;
    private String email;
    private String phone;
    private String deviceID;
    private Integer registerTime;
    private Integer goldenBean;
    private Integer expireDate;


}
