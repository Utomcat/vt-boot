package com.ranyk.vt.boot.example.single.datasource;

import com.ranyk.vt.boot.base.response.PageResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * CLASS_NAME: VtBootExampleSingleDatasourceApplicationTests.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 单数据源 - 样例测试启动类
 * @date: 2026-02-06
 */
@SpringBootTest
class VtBootExampleSingleDatasourceApplicationTests {

    /**
     * 测试单元测试是否成功
     */
    @Test
    void contextLoads() {
        System.out.println("Hello World - Single Datasource");
    }

    @Test
    void test0(){
        PageResponse.builder().build();
    }

}
