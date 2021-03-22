package com.hubbbs.user.controller;

import com.hubbbs.user.pojo.User;
import com.hubbbs.user.service.AttenterService;
import com.hubbbs.user.service.UserService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 控制器层
 *
 * @author Administrator
 */
@RestController
@CrossOrigin
@RequestMapping("/hubbbs/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AttenterService attenterService;

    @Autowired
    private HttpServletRequest request;

    /**
     * 查询全部数据
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll() {
        return new Result(true, StatusCode.OK, "查询成功", userService.findAll());
    }

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable String id) {
        String fanId = (String) request.getAttribute("userId");
        Boolean isAtter = attenterService.findIsSelf(id, fanId);
        User user = userService.findById(id);
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("user", user);
        resultMap.put("isAtter", isAtter);
        return new Result(true, StatusCode.OK, "查询成功", resultMap);
    }

    @RequestMapping(value = "/self", method = RequestMethod.POST)
    public Result getUserDetailSelf() {
        String userId = (String) request.getAttribute("userId");
        return new Result(true, StatusCode.OK, "查询成功", userService.findById(userId));
    }

    @RequestMapping(value = "/self", method = RequestMethod.PUT)
    public Result updateSelf(@RequestBody User user) {
        String userId = (String) request.getAttribute("userId");
        User byId = userService.findById(userId);
        byId.setSummary(user.getSummary());
        byId.setNickname(user.getNickname());
        byId.setCollege(user.getCollege());
        byId.setMajor(user.getMajor());
        byId.setSex(user.getSex());
        userService.update(byId);
        return new Result(true, StatusCode.OK, "修改成功");
    }


    /**
     * 分页+多条件查询
     *
     * @param searchMap 查询条件封装
     * @param page      页码
     * @param size      页大小
     * @return 分页结果
     */
    @RequestMapping(value = "/search/{page}/{size}", method = RequestMethod.POST)
    public Result findSearch(@RequestBody Map searchMap, @PathVariable int page, @PathVariable int size) {
        Page<User> pageList = userService.findSearch(searchMap, page, size);
        return new Result(true, StatusCode.OK, "查询成功", new PageResult<User>(pageList.getTotalElements(), pageList.getContent()));
    }

    /**
     * 根据条件查询
     *
     * @param searchMap
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public Result findSearch(@RequestBody Map searchMap) {
        return new Result(true, StatusCode.OK, "查询成功", userService.findSearch(searchMap));
    }

    /**
     * 增加
     *
     * @param user
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result add(@RequestBody User user) {
        userService.add(user);
        return new Result(true, StatusCode.OK, "增加成功");
    }

    /**
     * 修改
     *
     * @param user
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Result update(@RequestBody User user, @PathVariable String id) {
        user.setId(id);
        userService.update(user);
        return new Result(true, StatusCode.OK, "修改成功");
    }

    /**
     * 删除
     *
     * @param id
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Result delete(@PathVariable String id) {
        userService.deleteById(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    @RequestMapping(value = "/saveSelf", method = RequestMethod.PUT)
    public Result saveSelf(@RequestBody User user) {
        //2.更新数据库中icon
        String userId = (String) request.getAttribute("userId");

        User byId = userService.findById(userId);
        byId.setIcon(user.getIcon());
        userService.update(byId);
        return new Result(true, StatusCode.OK, "更新成功");
    }

}
