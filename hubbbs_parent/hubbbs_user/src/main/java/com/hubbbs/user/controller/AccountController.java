package com.hubbbs.user.controller;

import com.hubbbs.user.pojo.Account;
import com.hubbbs.user.pojo.User;
import com.hubbbs.user.service.AccountService;
import com.hubbbs.user.service.UserService;
import com.hubbbs.user.utils.StringUtils;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 控制器层
 *
 * @author Administrator
 */
@RestController
@CrossOrigin
@RequestMapping("/hubbbs/account")
public class AccountController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private UserService userService;
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 查询全部数据
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll() {
        return new Result(true, StatusCode.OK, "查询成功", accountService.findAll());
    }

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable String id) {
        return new Result(true, StatusCode.OK, "查询成功", accountService.findById(id));
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
        Page<Account> pageList = accountService.findSearch(searchMap, page, size);
        return new Result(true, StatusCode.OK, "查询成功", new PageResult<Account>(pageList.getTotalElements(), pageList.getContent()));
    }

    /**
     * 根据条件查询
     *
     * @param searchMap
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public Result findSearch(@RequestBody Map searchMap) {
        return new Result(true, StatusCode.OK, "查询成功", accountService.findSearch(searchMap));
    }

    /**
     * 增加
     *
     * @param account
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result add(@RequestBody Account account) {
        accountService.add(account);
        return new Result(true, StatusCode.OK, "增加成功");
    }

    /**
     * 修改
     *
     * @param account
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Result update(@RequestBody Account account, @PathVariable String id) {
        account.setId(id);
        accountService.update(account);
        return new Result(true, StatusCode.OK, "修改成功");
    }

    /**
     * 删除
     *
     * @param id
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Result delete(@PathVariable String id) {
        accountService.deleteById(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    /**
     * @return
     * @desc 获取验证码
     * @method sendSms
     * @params [username]
     * @author hubdir
     * @date 2019/5/4 13:46
     */
    @RequestMapping(value = "/sendSMS/{username}", method = RequestMethod.POST)
    public Result sendSms(@PathVariable String username) {
        if (!StringUtils.regexUsername(username)) {
            return new Result(false, StatusCode.ERROR, "手机号有误");
        }
        if (accountService.findByUsername(username) != null) {
            return new Result(false, StatusCode.ERROR, "该手机号已注册");
        }
        accountService.sendSms(username);
        return new Result(true, StatusCode.OK, "发送成功");
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Result login(@RequestBody Account account) {
        account = accountService.login(account);
        if (account == null || account.getIsBan() != 0) {
            return new Result(false, StatusCode.LOGINERROR, "登录失败");
        }
        User user = userService.findById(account.getUserId());
        String token = jwtUtil.createJWT(account.getId(), account.getUsername(), account.getRole(), user.getId());
        HashMap<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("roles", account.getRole());
        map.put("nickname", user.getNickname());
        map.put("icon", user.getIcon());
        map.put("userId", user.getId());
        userService.loginRecord(user);

        return new Result(true, StatusCode.OK, "登录成功", map);
    }

    @RequestMapping(value = "/register/{code}", method = RequestMethod.POST)
    public Result register(@PathVariable String code, @RequestBody Account account) {
        //获取缓存中验证码
        String checkCodeRedis = redisTemplate.opsForValue().get("check_" + account.getUsername());
        System.out.println("check_ + account.getMobile()===" + "check_" + account.getUsername());
        System.out.println("checkCodeRedis===" + checkCodeRedis);
        System.out.println("code===" + code);
        //正确则注册
        if (checkCodeRedis == null) {
            return new Result(false, StatusCode.ERROR, "请先获取手机验证码");
        }
        if (!checkCodeRedis.equals(code)) {
            return new Result(false, StatusCode.ERROR, "请先输入正确验证码");
        }
        if (accountService.findByUsername(account.getUsername()) != null) {
            return new Result(false, StatusCode.ERROR, "该手机号已注册");
        }

        accountService.add(account);

        return new Result(true, StatusCode.OK, "注册成功");
    }

    @RequestMapping(value = "/self", method = RequestMethod.POST)
    public Result getUserDetailSelf() {
        String userId = (String) request.getAttribute("userId");
        if (userId == null) {
            return new Result(false, StatusCode.ERROR, "登录后才能使用哦");
        }
        return new Result(true, StatusCode.OK, "查询成功", accountService.findByIdForChange(userId));
    }

    @RequestMapping(value = "/password", method = RequestMethod.PUT)
    public Result updatePassword(@RequestBody Map map) {
        String accountUsername = (String) request.getAttribute("accountUsername");
        if (!accountService.updatePassword(accountUsername, map)) {
            return new Result(false, StatusCode.ERROR, "操作失败");
        }

        return new Result(true, StatusCode.OK, "操作成功");
    }

    @RequestMapping(value = "/username/{code}", method = RequestMethod.PUT)
    public Result updateUsername(@RequestBody Map map, @PathVariable String code) {
        String accountUsername = (String) request.getAttribute("accountUsername");
        String checkCodeRedis = redisTemplate.opsForValue().get("check_" + accountUsername);
        //正确则注册
        if (checkCodeRedis == null) {
            return new Result(false, StatusCode.ERROR, "请先获取手机验证码");
        }
        if (!checkCodeRedis.equals(code)) {
            return new Result(false, StatusCode.ERROR, "请先输入正确验证码");
        }

        if (!accountService.updateUsername(accountUsername, (String) map.get("accountFormUsername"))) {
            return new Result(false, StatusCode.ERROR, "操作失败");
        }
        return new Result(true, StatusCode.OK, "操作成功");
    }

    /**
     * @return
     * @desc 后台管理登录
     * @method loginAdmin
     * @params [account]
     * @author hubdir
     * @date 2019/4/25 19:16
     */
    @RequestMapping(value = "/login/admin", method = RequestMethod.POST)
    public Result loginAdmin(@RequestBody Account account) {
//        response.setHeader("Access-Control-Allow-Origin","*");
        account = accountService.login(account);
        if (account == null || !"admin".equals(account.getRole())) {
            return new Result(false, StatusCode.LOGINERROR, "登录失败");
        }
        User user = userService.findById(account.getUserId());
        String token = jwtUtil.createJWT(account.getId(), account.getUsername(), account.getRole(), user.getId());
        HashMap<String, String> map = new HashMap<>();
        map.put("avatar", user.getIcon());
        map.put("name", user.getNickname());
        map.put("token", token);
        return new Result(true, StatusCode.OK, "登录成功", map);
    }

    /**
     * @return
     * @desc 返回用户信息
     * @method userInfo
     * @params []
     * @author hubdir
     * @date 2019/4/25 19:15
     */
    @RequestMapping(value = "/user/info", method = RequestMethod.GET)
    public Result userInfo() {
        String userId = (String) request.getAttribute("userId");
        Map<String, Object> result = userService.findInfoById(userId);
        HashMap<String, Object> map = new HashMap<>();
        String roles = (String) result.get("roles");
        ArrayList<String> strs = new ArrayList<>();
        strs.add(roles);
        map.putAll(result);
        map.put("avatar", result.get("icon"));
        map.put("name", result.get("nickname"));
        map.put("role", strs);
        map.put("roles", strs);
        return new Result(true, StatusCode.OK, "获取成功", map);
    }

    /**
     * @return Result
     * @desc 退出登录，设置jwt过期时间
     * @method userLogout
     * @params [roles]
     * @author hubdir
     * @date 2019/4/26 12:34
     */
    @RequestMapping(value = "/user/logout/{roles}", method = RequestMethod.POST)
    public Result userLogout(@PathVariable String roles) {
//        1.获取到token
//        2.根据token获取权限认证claims
        try {
            String token = (String) request.getAttribute("claims_" + roles);
            Claims claims = jwtUtil.parseJWT(token);
            // 3.设置claims的过期时间为当前时间
            claims.setExpiration(new Date());
            return new Result(true, StatusCode.OK, "退出成功", token);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "错误");
        }
    }


}
