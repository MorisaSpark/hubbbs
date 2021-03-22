package com.hubbbs.user.controller;

import com.hubbbs.user.scheduler.RedisScheduledTasks;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by IntelliJ IDEA.
 * User: HB
 * Date: 2019/4/23
 *
 * @author HB
 * To change this template use File | Settings | File Templates.
 */
@RestController
@CrossOrigin
@RequestMapping("/test")
public class TestController {
    @Autowired
    private RedisScheduledTasks redisScheduledTasks;

    @RequestMapping(value = "/persistClickNumToMysqlDB", method = RequestMethod.GET)
    public Result persistClickNumToMysqlDB() {
        redisScheduledTasks.persistClickNumToMysqlDB();
        return new Result(true, StatusCode.OK, "定时任务测试成功");
    }

    @RequestMapping(value = "/persistCollectionNumToMysqlDB", method = RequestMethod.GET)
    public Result persistCollectionNumToMysqlDB() {
        redisScheduledTasks.persistCollectionNumToMysqlDB();
        return new Result(true, StatusCode.OK, "定时任务测试成功");
    }
    @RequestMapping(value = "/prepareTestData", method = RequestMethod.GET)
    public Result prepareTestData() {
        redisScheduledTasks.prepareTestData();
        return new Result(true, StatusCode.OK, "定时任务测试数据生成成功");
    }
}
