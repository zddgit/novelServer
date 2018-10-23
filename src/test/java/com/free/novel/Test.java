package com.free.novel;

import com.free.novel.entity.Novel;

public class Test {
    public static void main(String[] args) {
        Novel novel = new Novel();
        novel.setId(1);
        novel.setName("圣墟");
        System.out.println(novel.toString());
        System.out.println(novel.hashCode());
    }
}
