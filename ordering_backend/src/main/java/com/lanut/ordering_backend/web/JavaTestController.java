package com.lanut.ordering_backend.web;

import com.lanut.ordering_backend.entity.RestBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JavaTestController {

    @SuppressWarnings("SameReturnValue")
    @GetMapping("/Java")
    public String javaIndex() {
        return RestBean.success("Hello World Java").asJsonString();
    }

    @SuppressWarnings("SameReturnValue")
    @GetMapping("/test")
    public String test(){
        Logger logger = LoggerFactory.getLogger(TestController.class);
        logger.info("用户访问了一次测试数据");
        return RestBean.success("用户访问了一次测试数据").asJsonString();
    }
}
