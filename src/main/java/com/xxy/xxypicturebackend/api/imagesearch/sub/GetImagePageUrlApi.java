package com.xxy.xxypicturebackend.api.imagesearch.sub;


import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.xxy.xxypicturebackend.exception.BusinessException;
import com.xxy.xxypicturebackend.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 获取以图搜图页面 step1
 */
@Slf4j
public class GetImagePageUrlApi {
    public static String getImagePageUrl(String imageUrl) {
        // image: https%3A%2F%2Fwww.codefather.cn%2Flogo.png
        //tn: pc
        //from: pc
        //image_source: PC_UPLOAD_URL
        //sdkParams:
        // 1. 准备请求参数
        Map<String, Object> formData = new HashMap<>();
        formData.put("image", imageUrl);
        formData.put("tn", "pc");
        formData.put("from", "pc");
        formData.put("image_source", "PC_UPLOAD_URL");
        // 获取当前时间戳
        long uptime = System.currentTimeMillis();
        String url="https://graph.baidu.com/upload?uptime="+uptime;
        String acsToken ="1753503605657_1753507954403_vxHkmmhbC5DjDEIje6lBl6lehg1PNj59AvLHbTrihItY49cnnPuA/iZn0fXF4lpjQrjUtkda09yF/5P3/vLCdtVrYngkOxF7ztIgVYc5misp07oB4EmF5STaGosRLNr6EZYjGx6p/tqTkrh6oYMLs5sDtz1LgJL7eJjZi6Xbz92SZQT55vTKaVaLulKp15FkysNTwWpU7eHqT0SCQoUa9cxUTkuZYRi2d6ZSgf2e55z6d2LEea2daCh6Oip8lWeTX6b3wiM8PqmOXOwSnRzgLCy1zBwxPDl6b2+7/Ca467rhx23UgprzPtsIoPGUD2ljR6MPVJLODVwKBsPG/kB8IEJ6j/5gY9lHufNlcJ/AeoCkUrlEZgccIJGUM+tyFBJ6/oTWjnQFVu2cBzzhGopv/wWh85oVbAVMxnLmxGh8w4gU8iu9oADQmxinH+0jhcjA7NnClehb4uYOoDImeqzufSelbHzohIbpL+axGvaFY1ruUMpzKUzJvs8AlQjDArs+rS5Qb58IdTYKo5YiQeaFxA==";


        try {
            //发送请求
            HttpResponse httpResponse = HttpRequest.post(url)
                    .form(formData)
                    .header("Acs-token", acsToken)
                    .timeout(5000).execute();
            if (httpResponse.getStatus() != HttpStatus.HTTP_OK){
                throw new BusinessException(ErrorCode.OPERATION_ERROR,"接口调用失败");
            }

            //解析响应
            String body = httpResponse.body();
            Map<String,Object> result = JSONUtil.toBean(body, Map.class);

            //处理响应
            if (result==null || !Integer.valueOf(0).equals(result.get("status"))){
                throw new BusinessException(ErrorCode.OPERATION_ERROR,"接口调用失败");
            }
            Map<String,Object> data= (Map<String,Object>)result.get("data");

            //解码url
            String rawUrl = (String)data.get("url");
            String searchResultUrl = URLUtil.decode(rawUrl, StandardCharsets.UTF_8);
            if (StrUtil.isBlank(searchResultUrl)){
                throw new BusinessException(ErrorCode.OPERATION_ERROR,"未返回有效地址");
            }
            return searchResultUrl;
        }catch (Exception e ){
            throw  new RuntimeException(e);
        }
    }
    public static void main(String[] args) {
        // 测试以图搜图功能
        String imageUrl = "https://www.codefather.cn/logo.png";
        String searchResultUrl = getImagePageUrl(imageUrl);
        System.out.println("搜索成功，结果 URL：" + searchResultUrl);
    }
}
