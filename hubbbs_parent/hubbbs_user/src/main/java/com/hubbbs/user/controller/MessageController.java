package com.hubbbs.user.controller;

import com.hubbbs.user.pojo.Message;
import com.hubbbs.user.service.MessageService;
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
@RequestMapping("/hubbbs/message")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private HttpServletRequest request;

    /**
     * 查询全部数据
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll() {
        return new Result(true, StatusCode.OK, "查询成功", messageService.findAll());
    }

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable String id) {
        return new Result(true, StatusCode.OK, "查询成功", messageService.findById(id));
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
        Page<Message> pageList = messageService.findSearch(searchMap, page, size);
        return new Result(true, StatusCode.OK, "查询成功", new PageResult<Message>(pageList.getTotalElements(), pageList.getContent()));
    }

    /**
     * 根据条件查询
     *
     * @param searchMap
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public Result findSearch(@RequestBody Map searchMap) {
        return new Result(true, StatusCode.OK, "查询成功", messageService.findSearch(searchMap));
    }

    /**
     * 增加
     *
     * @param message
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result add(@RequestBody Message message) {
        message.setSenderId((String) request.getAttribute("userId"));
        messageService.add(message);
        return new Result(true, StatusCode.OK, "增加成功");
    }

    /**
     * 修改
     *
     * @param message
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Result update(@RequestBody Message message, @PathVariable String id) {
        message.setId(id);
        messageService.update(message);
        return new Result(true, StatusCode.OK, "修改成功");
    }

    /**
     * 删除
     *
     * @param id
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Result delete(@PathVariable String id) {
        messageService.deleteById(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    /**
     * @return
     * @desc 查询发送和接收的信息
     * @method searchByTypeSelf
     * @params [searchMap, page, size]
     * @author hubdir
     * @date 2019/5/5 0:02
     */
    @RequestMapping(value = "/type/{page}/{size}", method = RequestMethod.POST)
    public Result searchByTypeSelf(@RequestBody Map searchMap, @PathVariable int page, @PathVariable int size) {
        //1.从request中取出userId
        String userId = (String) request.getAttribute("userId");
        //2.从searchMap中取出value
        String er = (String) searchMap.get("value");
        //3.根据value与userId查到message
        Page<Map<String, Object>> pageList = messageService.searchByTypeSelf(userId, er, page, size);
        return new Result(true, StatusCode.OK, "查询成功", new PageResult<Map<String, Object>>(pageList.getTotalElements(), pageList.getContent()));
    }

}
