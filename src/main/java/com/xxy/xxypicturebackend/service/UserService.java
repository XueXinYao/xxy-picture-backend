package com.xxy.xxypicturebackend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xxy.xxypicturebackend.mdoel.dto.user.UserQueryRequest;
import com.xxy.xxypicturebackend.mdoel.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xxy.xxypicturebackend.vo.LoginUserVO;
import com.xxy.xxypicturebackend.vo.UserVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author xuexinyao
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2025-06-10 21:14:50
*/
public interface UserService extends IService<User> {

    long userRegister(String userAccount, String userPassword, String checkPassword);

    String getEncryptPassword(String password);

    /**Add commentMore actions
     * 用户登录
     *
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @param request
     * @return 脱敏后的用户信息
     */
    LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request);



    /**
     * 获取当前用户
     * */
    User  getLoginUser(HttpServletRequest request);

    /**
     * 获得脱敏登录用户信息
     * */
    LoginUserVO getLoginUserVO(User user);


    /**
     * 获得脱敏用户信息
     * */
    UserVO getUserVO(User user);


    /**
     * 获得脱敏用户信息列表
     * */
    List<UserVO> getUserVOList(List<User> userList);
    /**
     * 用户注销
     * @param request
     * @return
     */
    boolean userLogout(HttpServletRequest request);


    QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest);
}
