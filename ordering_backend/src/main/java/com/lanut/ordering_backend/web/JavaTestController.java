package com.lanut.ordering_backend.web;

import com.lanut.ordering_backend.entity.RestBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class JavaTestController {

    @SuppressWarnings("SameReturnValue")
    @ResponseBody
    @GetMapping("/Java")
    public String javaIndex() {
        return RestBean.success("Hello World Java").asJsonString();
    }

    @SuppressWarnings("SameReturnValue")
    @ResponseBody
    @GetMapping("/test")
    public String test(){
        Logger logger = LoggerFactory.getLogger(TestController.class);
        logger.info("用户访问了一次测试数据");
        return RestBean.success("用户访问了一次测试数据").asJsonString();
    }
}
