package com.ndz.wheatmall.controller.index;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.ndz.wheatmall.annotation.NoApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson2.JSON;
import com.ndz.wheatmall.dto.index.GreetDTO;
import com.ndz.wheatmall.vo.index.GreetVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class HomeController {

    /**
     * NoApiResponse 注解表示不自动包装
     */
    @GetMapping("/hello")
    @NoApiResponse
    public String hello() {
        log.debug("debug");
        log.info("info");
        log.warn("warn");
        log.error("error");

        return "HelloWorld";
    }

    /**
     * 返回将会自动包装
     */
    @GetMapping("/greet")
    public GreetVO greet() {
        GreetVO greetVO = new GreetVO();
        greetVO.setMsg("Hello World");
        greetVO.setTime(LocalDateTime.now());
        greetVO.setDate(LocalDate.now());
        // 模拟异常
        Integer.valueOf("qwqweqw");
        return greetVO;
    }

    @PostMapping("/say")
    public void say(@RequestBody GreetDTO dto) {
        // 1673677631531 --- 13位毫秒级时间戳
        // curl  -H "Content-type: application/json" -X POST -d '{"date":"2023-01-14","time":"2023-01-14 13:48:19"}' localhost:8347/say
        System.out.println(JSON.toJSONString(dto));
    }




}
