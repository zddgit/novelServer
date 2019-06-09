package com.free.novel.entity;

public class RedisKey {
    private static final String tagZsetTemplet = "tag:%s";
    private static final String novelHashTemplet = "novel:%s";
    private static final String novelTitlesHashTemplet = "noveltitles:%s";
    private static final String novelcontentsHashTemplet = "novelcontents:%s";
    private static final String novelChapterIdsZsetTemplet = "novelchapterids:%s";

    // 按照类别存储的按照点击数排序的小说
    public static String getTagKey(String tagId) {
        return String.format(tagZsetTemplet, tagId);
    }

    // 具体小说概要信息
    public static String getNovelKey(String novelid) {
        return String.format(novelHashTemplet, novelid);
    }

    // 具体小说章节内容
    public static String getNovelContents(String novelid) {
        return String.format(novelcontentsHashTemplet, novelid);
    }

    // 具体小说章节id 排序
    public static String getNovelChapterIds(String novelid) {
        return String.format(novelChapterIdsZsetTemplet, novelid);
    }

    // 具体小说章节标题
    public static String getNovelTitles(String novelid) {
        return String.format(novelTitlesHashTemplet, novelid);
    }
}
