package com.hubbbs.user.controller;

import com.hubbbs.user.pojo.Attenter;
import com.hubbbs.user.service.AttenterService;
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
@RequestMapping("/hubbbs/attenter")
public class AttenterController {

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
        return new Result(true, StatusCode.OK, "查询成功", attenterService.findAll());
    }

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable String id) {
        return new Result(true, StatusCode.OK, "查询成功", attenterService.findById(id));
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
        Page<Attenter> pageList = attenterService.findSearch(searchMap, page, size);
        return new Result(true, StatusCode.OK, "查询成功", new PageResult<Attenter>(pageList.getTotalElements(), pageList.getContent()));
    }

    /**
     * 根据条件查询
     *
     * @param searchMap
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public Result findSearch(@RequestBody Map searchMap) {
        return new Result(true, StatusCode.OK, "查询成功", attenterService.findSearch(searchMap));
    }

    /**
     * 增加
     *
     * @param attenter
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result add(@RequestBody Attenter attenter) {
        attenter.setFanId((String) request.getAttribute("userId"));
        if (attenter.getFanId().equals(attenter.getUserId())) {
            return new Result(false, StatusCode.ERROR, "不能关注自己");
        } else if (attenterService.findIsSelf(attenter.getUserId(), attenter.getFanId())) {
            return new Result(false, StatusCode.ERROR, "已关注了");
        }else{
            //        用redis
            attenterService.add(attenter);
            return new Result(true, StatusCode.OK, "增加成功");
        }
    }

    /**
     * 修改
     *
     * @param attenter
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Result update(@RequestBody Attenter attenter, @PathVariable String id) {
        attenter.setId(id);
        attenterService.update(attenter);
        return new Result(true, StatusCode.OK, "修改成功");
    }

    /**
     * 删除
     *
     * @param id
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Result delete(@PathVariable String id) {
        attenterService.deleteById(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public Result delete(@RequestBody Map map) {
        map.put("fanId", (String) request.getAttribute("userId"));
        attenterService.delete(map);
        return new Result(true, StatusCode.OK, "取关成功");
    }


    @RequestMapping(value = "/IsSelf/{userId}", method = RequestMethod.POST)
    public Result findIsSelf(@PathVariable String userId) {

        return new Result(true, StatusCode.OK, "查询成功", attenterService.findIsSelf(userId, (String) request.getAttribute("userId")));
    }

    /**
     * @return
     * @desc 查询粉丝与关注列表
     * @method searchByTypeSelf
     * @params [searchMap, page, size]
     * @author hubdir
     * @date 2019/5/5 23:55
     */
    @RequestMapping(value = "/type/{page}/{size}", method = RequestMethod.POST)
    public Result searchByTypeSelf(@RequestBody Map searchMap, @PathVariable int page, @PathVariable int size) {
        //1.从request中取出userId
        String userId = (String) request.getAttribute("userId");
        //2.从searchMap中取出value
        String er = (String) searchMap.get("value");
        //3.根据value与userId查到message
        Page<Map<String, Object>> pageList = attenterService.searchByTypeSelf(userId, er, page, size);
        return new Result(true, StatusCode.OK, "查询成功", new PageResult<Map<String, Object>>(pageList.getTotalElements(), pageList.getContent()));
    }
}
