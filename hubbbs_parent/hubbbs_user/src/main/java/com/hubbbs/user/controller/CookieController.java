package com.hubbbs.user.controller;

import com.hubbbs.user.pojo.Cookie;
import com.hubbbs.user.pojo.User;
import com.hubbbs.user.service.CookieService;
import com.hubbbs.user.service.UserService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 控制器层
 *
 * @author Administrator
 */
@RestController
@CrossOrigin
@RequestMapping("/hubbbs/cookie")
public class CookieController {

    @Autowired
    private CookieService cookieService;
    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest request;

    /**
     * 查询全部数据
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll() {
        return new Result(true, StatusCode.OK, "查询成功", cookieService.findAll());
    }

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable String id) {
        return new Result(true, StatusCode.OK, "查询成功", cookieService.findById(id));
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
        Page<Cookie> pageList = cookieService.findSearch(searchMap, page, size);
        return new Result(true, StatusCode.OK, "查询成功", new PageResult<Cookie>(pageList.getTotalElements(), pageList.getContent()));
    }

    /**
     * 根据条件查询
     *
     * @param searchMap
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public Result findSearch(@RequestBody Map searchMap) {
        return new Result(true, StatusCode.OK, "查询成功", cookieService.findSearch(searchMap));
    }

    /**
     * 增加
     *
     * @param cookie
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result add(@RequestBody Cookie cookie) {
        String userId = (String) request.getAttribute("userId");
        cookie.setUserId(userId);
        User user = userService.findById(userId);
        if(user ==null){
            return new Result(false, StatusCode.ERROR, "请登录");
        }
        if(user.getCookieNum()<1){
            return new Result(false, StatusCode.ERROR, "饼干数量不足");
        }
        cookieService.add(cookie);
        return new Result(true, StatusCode.OK, "喂食成功");
    }

    /**
     * 修改
     *
     * @param cookie
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Result update(@RequestBody Cookie cookie, @PathVariable String id) {
        cookie.setId(id);
        cookieService.update(cookie);
        return new Result(true, StatusCode.OK, "修改成功");
    }

    /**
     * 删除
     *
     * @param id
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Result delete(@PathVariable String id) {
        cookieService.deleteById(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }

}
