package com.jinnjo.sale.utils;

import com.jinnjo.sale.domain.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class UserUtil {
    public static UserVo getUser(){
        Authentication auth = getCurrentUserAuthentication();
        UserVo user = null;
        if(null != auth){
            user = new UserVo();
            auth =  ((OAuth2Authentication) auth).getUserAuthentication();
            Map json = (Map) auth.getDetails();
            if(null != json && null != json.get("id")){
                user.setMobile(json.get("id").toString());
                user.setUserName(json.get("id").toString());
            }
            if(null != json){
                Map userMap = (Map) json.get("attributes");
                if(null != userMap && null != userMap.get("user_id")){
                    user.setUserId(Long.parseLong(userMap.get("user_id").toString()));
                }
                if(null != userMap && null != userMap.get("name")){
                    user.setName(userMap.get("name").toString());
                }
                if(null != userMap && null != userMap.get("roles")){
                    user.setRoles(userMap.get("roles").toString());
                }
            }
        }
        if(null==user){
            throw new ConstraintViolationException("用户信息获取失败！", new HashSet<>());
        }
        return user;
    }

    public static Long getCurrentUserId(){
        return getUser().getUserId();
    }


    public static List<String> getAuthorities(){
         List<String> authorities = new ArrayList<>();
         Authentication authentication = getCurrentUserAuthentication();
         if(null==authentication){
             log.error("用户信息不存在，默认返回空权限");
             return authorities;
         }
         try {
             authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
         }catch (Exception e){
             log.error("获取权限出错，默认返回空权限",e);
         }
         return authorities;
    }

    private static Authentication getCurrentUserAuthentication(){
        return SecurityContextHolder.getContext().getAuthentication();
    }

}
