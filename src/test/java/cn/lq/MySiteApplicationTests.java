package cn.lq;

import cn.lq.common.domain.po.es.ContentEsPO;
import cn.lq.manager.ContentManager;
import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MySiteApplicationTests {
    @Resource
    private ContentManager contentManager;

    @Test
    public void test() {
        ContentEsPO contentEsPO = new ContentEsPO();
        contentEsPO.setContent("content");
        contentEsPO.setType("type");
        contentEsPO.setAllowComment(1);
        contentEsPO.setAllowFeed(1);
        contentEsPO.setCategories("cate");
        long id = contentManager.insert(contentEsPO);
        System.out.println("id = " + id);
        ContentEsPO esPO = contentManager.queryForObject(id);
        String jsonString = JSON.toJSONString(esPO);
        System.out.println("jsonString = " + jsonString);
        ContentEsPO updatePO = new ContentEsPO();
        updatePO.setId(id);
        updatePO.setContent("第一次更新文档");
        contentManager.update(updatePO);
        ContentEsPO afterUpdate = contentManager.queryForObject(id);
        System.out.println("afterUpdate = " + JSON.toJSONString(afterUpdate));

    }

}
