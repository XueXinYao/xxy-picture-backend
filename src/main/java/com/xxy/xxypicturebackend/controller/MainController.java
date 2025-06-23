package com.xxy.xxypicturebackend.controller;


import com.xxy.xxypicturebackend.common.BaseResponse;
import com.xxy.xxypicturebackend.common.ResultUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class MainController {
    @GetMapping("/health")
    public BaseResponse<String> hello() {
        return ResultUtils.success("health");
    }
}
