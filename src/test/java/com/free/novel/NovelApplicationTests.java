package com.free.novel;

import com.free.novel.domain.Chapter;
import com.free.novel.mapper.NovelMapper;
import com.free.novel.util.ZLibUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NovelApplicationTests {
    @Resource
    private NovelMapper novelMapper;

    @Test
    public void contextLoads() throws UnsupportedEncodingException {
        List<Chapter> list =  novelMapper.getChapters(1);
        Chapter chapter = list.get(0);
        byte[] contentByte = ZLibUtils.decompress(chapter.getContent());
        System.out.println(contentByte.length);
        System.out.println(new String(contentByte,"GBK"));
        System.out.println(chapter);
    }

}
