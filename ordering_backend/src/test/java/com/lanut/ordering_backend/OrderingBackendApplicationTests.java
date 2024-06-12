package com.lanut.ordering_backend;

import com.lanut.ordering_backend.entity.RestBean;
import com.lanut.ordering_backend.entity.dto.Dish;
import com.lanut.ordering_backend.web.JavaTestController;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class OrderingBackendApplicationTests {
	private static final Logger log = LoggerFactory.getLogger(OrderingBackendApplicationTests.class);

	@Test
	// 测试javaIndex方法
	void contextLoads() {
		 JavaTestController javaTestController = new JavaTestController();
		 assertEquals("Hello World Java", javaTestController.javaIndex());
		 log.info("javaIndex方法测试通过");
	}

	@Test
	void temp() {
		val success = RestBean.success("success");
		val fail = RestBean.failure(301, "fail");
		System.out.println(success.asJsonString());
		System.out.println(fail.asJsonString());
	}

}
