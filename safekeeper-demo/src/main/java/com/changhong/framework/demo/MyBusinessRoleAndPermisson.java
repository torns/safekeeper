package com.changhong.framework.demo;

import com.changhong.framework.common.model.SafeKeeperAuthorizationCallBack;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MyBusinessRoleAndPermisson implements SafeKeeperAuthorizationCallBack {
    private Map<String, Map<String,List>> roleData=new HashMap<>();
    private Map<String, Map<String,List>> perData=new HashMap<>();

    public MyBusinessRoleAndPermisson(){

        List<String> roles1=new ArrayList<>();
        roles1.add("admin");
        roles1.add("user");

        List<String> roles2=new ArrayList<>();
        roles2.add("manager");
        roles2.add("view");

        List<String> roles3=new ArrayList<>();
        roles3.add("view");

        Map<String,List> roleMap=new HashMap<>();
        roleMap.put("1",roles1);
        roleMap.put("2",roles2);
        roleMap.put("3",roles3);


        roleData.put("app",roleMap);
        roleData.put("h5",roleMap);
        roleData.put("wx",roleMap);


        List<String> per1=new ArrayList<>();
        per1.add("*");
        List<String> per2=new ArrayList<>();
        per2.add("add");
        per2.add("del");
        per2.add("update");
        List<String> per3=new ArrayList<>();
        per3.add("view");
        per3.add("download");
        Map<String,List> perMap=new HashMap<>();
        perMap.put("1",per1);
        perMap.put("2",per2);
        perMap.put("3",per3);
        perData.put("app",perMap);
        perData.put("h5",perMap);
        perData.put("wx",perMap);
    }


    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        System.out.println("框架回调getPermissionList loginId:"+loginId+" loginType:"+loginType);
        return perData.get(loginType).get(loginId);
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        System.out.println("框架回调getRoleList loginId:"+loginId+" loginType:"+loginType);
        return roleData.get(loginType).get(loginId);
    }
}
