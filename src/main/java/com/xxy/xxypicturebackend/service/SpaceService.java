package com.xxy.xxypicturebackend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xxy.xxypicturebackend.mdoel.dto.space.SpaceAddRequest;
import com.xxy.xxypicturebackend.mdoel.dto.space.SpaceQueryRequest;
import com.xxy.xxypicturebackend.mdoel.entity.Space;
import com.xxy.xxypicturebackend.mdoel.entity.User;
import com.xxy.xxypicturebackend.vo.PictureVO;
import com.xxy.xxypicturebackend.vo.SpaceVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author xuexinyao
* @description 针对表【space(空间)】的数据库操作Service
* @createDate 2025-07-21 16:32:12
*/
public interface SpaceService extends IService<Space> {

    /**
     * 创建空间
     * @param space
     * @param loginUser
     * @return
     */
    long  addSpace(SpaceAddRequest spaceAddRequest , User loginUser);

    /**
     * 校验
     * @param space
     * @param add  是否为创建时校验
     */
    void validspace(Space space ,boolean add);

    /**
     *  获取查询条件
     * @param spaceQueryRequest
     * @return
     */
    QueryWrapper<Space> getQueryWrapper(SpaceQueryRequest spaceQueryRequest);

    /**
     * 获取分页数据
     *
     * @param spacePage
     * @param request
     * @return
     */
    Page<SpaceVO> getSpaceVOPage(Page<Space> spacePage, HttpServletRequest request);

    /**
     * 获取数据(单条)
     * @param space
     * @param request
     * @return
     */
    SpaceVO getSpaceVO(Space space, HttpServletRequest request);

     void  fillSpaceBySpaceLevel(Space space);
}
