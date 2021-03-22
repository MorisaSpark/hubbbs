package com.hubbbs.user.controller;

import com.hubbbs.user.pojo.RelTag;
import com.hubbbs.user.service.RelTagService;
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
@RequestMapping("/hubbbs/reltag")
public class RelTagController {

    @Autowired
    private RelTagService relTagService;

    @Autowired
    private HttpServletRequest request;

    /**
     * 查询全部数据
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll() {
        return new Result(true, StatusCode.OK, "查询成功", relTagService.findAll());
    }

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable String id) {
        return new Result(true, StatusCode.OK, "查询成功", relTagService.findById(id));
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
        Page<RelTag> pageList = relTagService.findSearch(searchMap, page, size);
        return new Result(true, StatusCode.OK, "查询成功", new PageResult<RelTag>(pageList.getTotalElements(), pageList.getContent()));
    }

    /**
     * 根据条件查询
     *
     * @param searchMap
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public Result findSearch(@RequestBody Map searchMap) {
        return new Result(true, StatusCode.OK, "查询成功", relTagService.findSearch(searchMap));
    }

    /**
     * 增加
     *
     * @param reltag
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result add(@RequestBody RelTag reltag) {
        relTagService.add(reltag);
        return new Result(true, StatusCode.OK, "增加成功");
    }

    /**
     * 修改
     *
     * @param reltag
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Result update(@RequestBody RelTag reltag, @PathVariable String id) {
        reltag.setId(id);
        relTagService.update(reltag);
        return new Result(true, StatusCode.OK, "修改成功");
    }

    /**
     * 删除
     *
     * @param id
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Result delete(@PathVariable String id) {
        relTagService.deleteById(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    @RequestMapping(value = "/deleteInPost/{postId}/{newTagName}", method = RequestMethod.DELETE)
    public Result deleteInPost(@PathVariable String postId, @PathVariable String newTagName) {
        if (request.getAttribute("userId") == null) {
            return new Result(false, StatusCode.ERROR, "登录后才能使用哦");
        }
        relTagService.deleteByPostIdAndSummary(postId, newTagName);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    @RequestMapping(value = "/saveInPost/{postId}/{newTagName}", method = RequestMethod.POST)
    public Result saveInPost(@PathVariable String postId, @PathVariable String newTagName) {
        if (request.getAttribute("userId") == null) {
            return new Result(false, StatusCode.ERROR, "登录后才能使用哦");
        }
        RelTag reltag = new RelTag()
                .setPostId(postId)
                .setSummary(newTagName);
        relTagService.add(reltag);
        return new Result(true, StatusCode.OK, "增加成功");
    }

    @RequestMapping(value = "/findByPostId/{postId}", method = RequestMethod.POST)
    public Result findByPostId(@PathVariable String postId) {
        HashMap whereMap = new HashMap<String, Object>();
        whereMap.put("postId", postId);
        return new Result(true, StatusCode.OK, "增加成功", relTagService.findSearch(whereMap));
    }

}
