package com.changhong.framework.demo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class UserVO {
    private Integer id;
    private String username;
    private String password;
}
