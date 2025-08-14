package com.xxy.xxypicturebackend.manager.websocket;

import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.xxy.xxypicturebackend.manager.auth.SpaceUserAuthManager;
import com.xxy.xxypicturebackend.manager.auth.model.SpaceUserPermissionConstant;
import com.xxy.xxypicturebackend.mdoel.entity.Picture;
import com.xxy.xxypicturebackend.mdoel.entity.Space;
import com.xxy.xxypicturebackend.mdoel.entity.User;
import com.xxy.xxypicturebackend.mdoel.enums.SpaceLevelEnum;
import com.xxy.xxypicturebackend.mdoel.enums.SpaceTypeEnum;
import com.xxy.xxypicturebackend.service.PictureService;
import com.xxy.xxypicturebackend.service.SpaceService;
import com.xxy.xxypicturebackend.service.SpaceUserService;
import com.xxy.xxypicturebackend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * websocket 握手拦截器 建立链接前校验
 */
@Slf4j
@Component
public class WsHandshakeInterceptor implements HandshakeInterceptor {
    @Resource
    private UserService userService;
    @Resource
    private PictureService pictureService;
    @Resource
    private SpaceService spaceService;
    @Resource
    private SpaceUserAuthManager spaceUserAuthManager;

    /**
     *
     * @param request
     * @param response
     * @param wsHandler
     * @param attributes  给websocket会话属性
     * @return
     * @throws Exception
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {

        //获取登录用户
        if (request instanceof ServletServerHttpRequest) {
            HttpServletRequest httpServletRequest = ((ServletServerHttpRequest) request).getServletRequest();
            //获取登录用户
            String pictureId = httpServletRequest.getParameter("pictureId");
            if (StringUtils.isBlank(pictureId)) {
                log.error("pictureId is blank拒绝握手");
                return false;
            }
            User loginUser = userService.getLoginUser(httpServletRequest);
            if (ObjUtil.isEmpty(loginUser)) {
                log.error("loginUser is empty拒绝握手");
                return false;
            }
            //校验当前用户是否编辑权限
            Picture picture = pictureService.getById(Long.valueOf(pictureId));
            if (ObjUtil.isEmpty(picture)) {
                log.error("picture is empty拒绝握手");
                return false;
            }
            Long spaceId = picture.getSpaceId();
            Space space = null;
            if (ObjUtil.isNotEmpty(spaceId)) {
                space = spaceService.getById(spaceId);
                if (ObjUtil.isEmpty(space)) {
                    log.error("space is empty拒绝握手");
                    return false;
                }
                if (space.getSpaceType()!= SpaceTypeEnum.TEAM.getValue()){
                    log.error("不是团队空间拒绝握手");
                    return false;
                }
            }
            List<String> permissionList = spaceUserAuthManager.getPermissionList(space, loginUser);
            if (!permissionList.contains(SpaceUserPermissionConstant.PICTURE_EDIT)){
                log.error("没有编辑权限拒绝握手");
                return false;
            }
            //设置登录信息到websocket会话中
            attributes.put("user",loginUser);
            attributes.put("userId",loginUser.getId());
            attributes.put("pictureId",Long.valueOf(pictureId));
        }
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}
