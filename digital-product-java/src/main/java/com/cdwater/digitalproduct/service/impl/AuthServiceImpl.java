package com.cdwater.digitalproduct.service.impl;

import com.cdwater.digitalproduct.common.constants.RedisMark;
import com.cdwater.digitalproduct.common.constants.RoleType;
import com.cdwater.digitalproduct.common.enums.ReturnMsg;
import com.cdwater.digitalproduct.common.exception.BusinessException;
import com.cdwater.digitalproduct.common.utils.RedisUtil;
import com.cdwater.digitalproduct.common.utils.RegexUtil;
import com.cdwater.digitalproduct.common.utils.ThreadUtil;
import com.cdwater.digitalproduct.model.auth.*;
import com.cdwater.digitalproduct.entity.Admin;
import com.cdwater.digitalproduct.entity.DigitalShop;
import com.cdwater.digitalproduct.entity.User;
import com.cdwater.digitalproduct.config.properties.JwtProperties;
import com.cdwater.digitalproduct.service.*;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class AuthServiceImpl implements AuthService {

    @Resource
    private GenericAuthService genericAuthService;
    @Resource
    private JwtProperties jwtProperties;
    @Resource
    private RedisUtil redisUtil;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        //校验输入
        if (RegexUtil.usernameNotValid(loginRequest.getUsername()) ||
                RegexUtil.passwordNotValid(loginRequest.getPassword())) {
            throw new BusinessException(ReturnMsg.ILLEGAL_INPUT);
        }

        //获取登录角色
        Integer role = loginRequest.getRole();

        LoginResponse loginResponse;
        //根据角色登录
        if (RoleType.isAdmin(role)) {
            loginResponse = genericAuthService.login(loginRequest, Admin.class, jwtProperties.getAdminExpiration());
        } else if (RoleType.isDigitalShop(role)) {
            loginResponse = genericAuthService.login(loginRequest, DigitalShop.class, jwtProperties.getShopExpiration());
        } else if (RoleType.isUser(role)) {
            loginResponse = genericAuthService.login(loginRequest, User.class, jwtProperties.getUserExpiration());
        } else {
            throw new BusinessException(ReturnMsg.ILLEGAL_INPUT);
        }

        return loginResponse;
    }

    @Override
    public String sendCode(CodeRequest codeRequest) {
        //校验输入
        if (RegexUtil.phoneNotValid(codeRequest.getPhone())) {
            throw new BusinessException(ReturnMsg.ILLEGAL_INPUT);
        }

        //生成验证码
        String code = RandomStringUtils.randomNumeric(6);

        //保存验证码到redis
        String key = RedisMark.CODE_PREFIX + codeRequest.getPhone();
        redisUtil.setStr(key, code, RedisMark.CODE_TTL, TimeUnit.SECONDS, true);
        //发送验证码（模拟）,TODO后续可接入短信接口
        return code;
    }

    @Override
    public LoginResponse phoneLogin(LoginRequest loginRequest) {
        //校验输入
        if (RegexUtil.phoneNotValid(loginRequest.getPhone()) ||
                RegexUtil.verifyCodeNotValid(loginRequest.getCode())) {
            throw new BusinessException(ReturnMsg.ILLEGAL_INPUT);
        }

        //获取登录角色
        Integer role = loginRequest.getRole();

        LoginResponse loginResponse;
        //根据角色登录
        if (RoleType.isAdmin(role)) {
            loginResponse = genericAuthService.phoneLogin(loginRequest, Admin.class, jwtProperties.getAdminExpiration());
        } else if (RoleType.isDigitalShop(role)) {
            loginResponse = genericAuthService.phoneLogin(loginRequest, DigitalShop.class, jwtProperties.getShopExpiration());
        } else if (RoleType.isUser(role)) {
            loginResponse = genericAuthService.phoneLogin(loginRequest, User.class, jwtProperties.getUserExpiration());
        } else {
            throw new BusinessException(ReturnMsg.ILLEGAL_INPUT);
        }

        return loginResponse;
    }

    @Override
    public void register(RegisterRequest registerRequest) {
        //校验输入
        if (RegexUtil.usernameNotValid(registerRequest.getUsername()) ||
                RegexUtil.passwordNotValid(registerRequest.getPassword()) ||
                RegexUtil.passwordNotComplex(registerRequest.getPassword()) ||
                !StringUtils.equals(registerRequest.getPassword(), registerRequest.getConfirmPassword())) {
            throw new BusinessException(ReturnMsg.ILLEGAL_INPUT);
        }

        //获取登录角色
        Integer role = registerRequest.getRole();

        //根据角色注册
        if (RoleType.isDigitalShop(role)) {
            genericAuthService.register(registerRequest, DigitalShop.class);
        } else if (RoleType.isUser(role)) {
            genericAuthService.register(registerRequest, User.class);
        } else {
            throw new BusinessException(ReturnMsg.ILLEGAL_INPUT);
        }
    }

    @Override
    public void forget(ForgetRequest forgetRequest) {
        //校验输入
        if (RegexUtil.phoneNotValid(forgetRequest.getPhone()) ||
                RegexUtil.verifyCodeNotValid(forgetRequest.getCode()) ||
                RegexUtil.passwordNotValid(forgetRequest.getNewPassword()) ||
                RegexUtil.passwordNotComplex(forgetRequest.getNewPassword()) ||
                !StringUtils.equals(forgetRequest.getNewPassword(), forgetRequest.getConfirmNewPassword())) {
            throw new BusinessException(ReturnMsg.ILLEGAL_INPUT);
        }

        //获取登录角色
        Integer role = forgetRequest.getRole();

        //根据角色找回密码
        if (RoleType.isAdmin(role)) {
            genericAuthService.forget(forgetRequest, Admin.class);
        } else if (RoleType.isDigitalShop(role)) {
            genericAuthService.forget(forgetRequest, DigitalShop.class);
        } else if (RoleType.isUser(role)) {
            genericAuthService.forget(forgetRequest, User.class);
        } else {
            throw new BusinessException(ReturnMsg.ILLEGAL_INPUT);
        }
    }

    @Override
    public void change(ChangeRequest changeRequest) {
        //校验输入
        if (RegexUtil.passwordNotValid(changeRequest.getOldPassword()) ||
                RegexUtil.passwordNotValid(changeRequest.getNewPassword()) ||
                RegexUtil.passwordNotComplex(changeRequest.getNewPassword()) ||
                !StringUtils.equals(changeRequest.getNewPassword(), changeRequest.getConfirmNewPassword())) {
            throw new BusinessException(ReturnMsg.ILLEGAL_INPUT);
        }

        //获取登录角色
        Integer role = changeRequest.getRole();

        //根据角色修改密码
        if (RoleType.isAdmin(role)) {
            genericAuthService.change(changeRequest, Admin.class);
        } else if (RoleType.isDigitalShop(role)) {
            genericAuthService.change(changeRequest, DigitalShop.class);
        } else if (RoleType.isUser(role)) {
            genericAuthService.change(changeRequest, User.class);
        } else {
            throw new BusinessException(ReturnMsg.ILLEGAL_INPUT);
        }
    }

    @Override
    public void logout(HttpServletRequest request) {
        //获取token
        String token = request.getHeader(jwtProperties.getTokenName());
        //清理个人信息缓存
        String profileKey = RedisMark.PROFILE_PREFIX + token;
        redisUtil.delete(profileKey);

        //token移出白名单
        String tokenKey = RedisMark.TOKEN_PREFIX + token;
        redisUtil.delete(tokenKey);

        //清除ThreadLocal
        ThreadUtil.removeId();
        ThreadUtil.removeRole();
    }
}
