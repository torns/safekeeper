package com.changhong.framework.demo;

import com.changhong.framework.common.annotations.Condition;
import com.changhong.framework.common.annotations.SafeKeeperHasPermission;
import com.changhong.framework.common.annotations.SafeKeeperHasRole;
import com.changhong.framework.common.model.SafeKeeperTokenInfo;
import com.changhong.framework.core.SafeKeeper;
import com.changhong.framework.plugin.web.Result;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class DemoController {

    private String name="admin";
    private String password="admin";

    @RequestMapping("/m")
    public String m(){
        return "框架放行了，可以玩了，hello world";
    }

    @RequestMapping("/login")
    public Result login(@RequestBody UserVO user,HttpServletRequest request){
        //这个里面是校验登录的逻辑，校验通过后就可以进行安全框架的维度登录。
        System.out.println(user);
        if(!StringUtils.isEmpty(user)){
            if(user.getUsername().equals(name) && user.getPassword().equals(password)){
                SafeKeeper.safeLogic(request.getHeader("loginType")).login(user.getId());
                return Result.succeedWith(
                        SafeKeeper.safeLogic(request.getHeader("loginType")).getTokenInfo()
                        ,1000,"登录成功");
            }
        }
        return Result.failedWith(null,1001,"用户名密码错误");
    }

    @RequestMapping("/f")
    public Result f(){
        return Result.failedWith(null,1000,"f()方法可以访问了");
    }


    @RequestMapping("/add")
    @SafeKeeperHasRole(roles={"admin"},type = "app")
    @SafeKeeperHasPermission(permissions = {"model:furetue:add"},type = "app")
    public Result add(){
        return Result.failedWith(null,1000,"add()方法可以访问了");
    }

    @RequestMapping("/del")
    @SafeKeeperHasRole(roles={"manager"},type = "app")
    public Result del(){
        return Result.failedWith(null,1000,"del()方法可以访问了");
    }

    @RequestMapping("/update")
    @SafeKeeperHasRole(roles={"manager"},type = "app")
    public Result update(){
        return Result.failedWith(null,1000,"update()方法可以访问了");
    }

    @RequestMapping("/view")
    @SafeKeeperHasRole(roles={"view"},type = "app")
    public Result view(){
        return Result.failedWith(null,1000,"view()方法可以访问了");
    }
}
