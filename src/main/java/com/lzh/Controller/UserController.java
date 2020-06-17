package com.lzh.Controller;

import com.lzh.Pojo.User;
import com.lzh.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import javax.validation.ValidationException;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Validated
@RestController
public class UserController {

    @Autowired
    UserService userService;


    /*@Qualifier("defaultValidator")
    @Autowired
    private Validator validator;*/



    //查询数据库的所有信息
    @GetMapping("/userList")
    public List<User> userList(){
        System.out.println("------");
        List<User> users = userService.queryUserList();
        System.out.println(users);
        return users;

    }

    @PostMapping("/addUser")
    public String addUser(@RequestBody @Valid User user,BindingResult bindingResult){
        validData(bindingResult);

        if(userService.addUser(user)!=null){
            return "创建成功";
        }else{
            return "创建失败";
        }

    }

    @PostMapping("/updateUser")
    public String updateUser(@RequestBody @Valid User user,BindingResult bindingResult){
        validData(bindingResult);
        //System.out.println(user.getId());
        //System.out.println(userService.queryUserById(user.getId()));
        if(userService.queryUserById(user.getId())!=null){
            userService.updateUser(user);
            return "更新成功";
        }else{
            return "更新失败";
        }
    }

    @GetMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable("id") int id){

        if(userService.queryUserById(id)!=null){
            userService.deleteUser(id);
            return "删除成功";
        }else{
            return "用户不存在";
        }
    }


    @GetMapping("/queryUserById/{id}")
    public Object queryUserById(@PathVariable("id") int id){

        if(userService.queryUserById(id)!=null){
            return userService.queryUserById(id);
        }else{
            return "用户不存在";
        }
    }

    @GetMapping("/getUser")
    public String getUser(@NotNull(message = "username 不能为空")
                            @Size(max=5, message = "长度不能大于5")
                                       String username) {
        return "name: " + username;
    }

    private void validData(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuffer sb = new StringBuffer();
            for (ObjectError error : bindingResult.getAllErrors()) {
                sb.append(error.getDefaultMessage());
            }
            throw new ValidationException(sb.toString());
        }
    }

}
