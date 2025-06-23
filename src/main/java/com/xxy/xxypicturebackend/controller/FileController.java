package com.xxy.xxypicturebackend.controller;

import com.qcloud.cos.model.COSObject;
import com.qcloud.cos.model.COSObjectInputStream;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.utils.IOUtils;
import com.xxy.xxypicturebackend.annoatation.AuthCheck;
import com.xxy.xxypicturebackend.common.BaseResponse;
import com.xxy.xxypicturebackend.common.ResultUtils;
import com.xxy.xxypicturebackend.constant.UserConstant;
import com.xxy.xxypicturebackend.exception.BusinessException;
import com.xxy.xxypicturebackend.exception.ErrorCode;
import com.xxy.xxypicturebackend.manager.CosManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/file")
@Slf4j
public class FileController {
    @Resource
    private CosManager cosManager;

    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @PostMapping("/test/upload")
    public BaseResponse<String> uploadFile(@RequestPart("file") MultipartFile file) {

       //文件目录
        String filename = file.getOriginalFilename();
        String filepath = String.format("test/%s", filename);

        File tempFile = null;
        try {
            //上传文件
            tempFile = File.createTempFile(filepath, null);
            file.transferTo(tempFile); // 使用正确的变量名 file
            cosManager.putObject(filepath, tempFile);
            //返回可返回的地址
            return ResultUtils.success(filepath);
        } catch (IOException e) {
            log.error("file upload fail");
            throw new RuntimeException(e);
        }finally {
            if (tempFile != null) {
                boolean delete = tempFile.delete();
                if (!delete) {
                    log.error("file delete fail");
                }
            }
        }
    }

    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @GetMapping("/test/download")
    public void downloadFile(String filepath, HttpServletResponse response) throws IOException {

        COSObjectInputStream cosObjectInput=null;
        try {
            COSObject cosObject = cosManager.getObject(filepath, null);
            cosObjectInput=cosObject.getObjectContent();
            // 设置响应头
            response.setContentType("application/octet-stream;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + filepath);
            byte[] bytes = IOUtils.toByteArray(cosObjectInput);
            //写入响应   输入流读，输出流写
            response.getOutputStream().write(bytes);
            response.getOutputStream().flush();
        } catch (IOException e) {

            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"下载失败");
        }finally {
            if (cosObjectInput != null){
                //关闭流
                cosObjectInput.close();
            }
        }
    }
}

