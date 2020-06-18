package com.lzh.Pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {

    private Integer id;

    @NotNull(message = "名字不能为空")
    @Size(min = 1,max = 10,message = "姓名长度必须为1到10")
    private String username;

    @NotNull(message = "密码不能为空")
    @Size(min = 1,max = 10,message = "密码长度必须为1到10")
    private String password;
}
