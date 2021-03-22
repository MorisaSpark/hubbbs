package com.hubbbs.user.controller;

import com.hubbbs.user.pojo.Reply;
import com.hubbbs.user.service.ReplyService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/hubbbs/reply")
public class ReplyController {

    @Autowired
    private ReplyService replyService;

    @Autowired
    private HttpServletRequest request;

    /**
     * 查询全部数据
     * @return
     */
//	@RequestMapping(method= RequestMethod.GET)
//	public Result findAll(){
//		return new Result(true,StatusCode.OK,"查询成功",replyService.findAll());
//	}

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable String id) {
        return new Result(true, StatusCode.OK, "查询成功", replyService.findById(id));
    }


    /**
     * 分页+多条件查询
     * @param searchMap 查询条件封装
     * @param page 页码
     * @param size 页大小
     * @return 分页结果
     */
	@RequestMapping(value="/search/{page}/{size}",method=RequestMethod.POST)
	public Result findSearch(@RequestBody Map searchMap , @PathVariable int page, @PathVariable int size){
		return  new Result(true,StatusCode.OK,"查询成功",  replyService.findSearch(searchMap, page, size));
	}

    /**
     * 根据条件查询
     * @param searchMap
     * @return
     */
//    @RequestMapping(value="/search",method = RequestMethod.POST)
//    public Result findSearch( @RequestBody Map searchMap){
//        return new Result(true,StatusCode.OK,"查询成功",replyService.findSearch(searchMap));
//    }

    /**
     * 根据条件查询
     *
     * @param postId
     * @return
     */
//    @RequestMapping(value="/search",method = RequestMethod.POST)
//    public Result findSearch( @RequestBody Map searchMap){
//        return new Result(true,StatusCode.OK,"查询成功",replyService.findSearch(searchMap));
//    }
    @RequestMapping(value = "/search/post/{postId}/{page}/{size}", method = RequestMethod.POST)
    public Result findSearchByPostId(@PathVariable String postId, @PathVariable int page, @PathVariable int size) {
        return new Result(true, StatusCode.OK, "查询成功", replyService.findByPostId(postId, page, size));
    }

    /**
     * 增加
     *
     * @param reply
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result add(@RequestBody Reply reply) {
        String userId = (String) request.getAttribute("userId");
        if(userId==null){
            return new Result(false,StatusCode.ERROR,"登录后才能使用哦");
        }
        reply.setUserId(userId);
        reply.setToId("0");
        replyService.add(reply);
        return new Result(true, StatusCode.OK, "增加成功");
    }

    /**
     * 修改
     *
     * @param reply
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Result update(@RequestBody Reply reply, @PathVariable String id) {
        reply.setId(id);
        replyService.update(reply);
        return new Result(true, StatusCode.OK, "修改成功");
    }

    /**
     * 删除
     *
     * @param id
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Result delete(@PathVariable String id) {
        replyService.deleteById(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    @RequestMapping(value = "/postIdWDefault/{postId}", method = RequestMethod.POST)
    public Result findByPostIdDefault(@PathVariable String postId) {
        return new Result(true, StatusCode.OK, "查询成功", replyService.findByPostId(postId));
    }

    @RequestMapping(value = "/searchFloorTh/{page}/{size}", method = RequestMethod.POST)
    public Result searchFloorTh(@RequestBody Map searchMap, @PathVariable int page, @PathVariable int size) {
        return new Result(true, StatusCode.OK, "查询成功", replyService.searchFloorTh((String) searchMap.get("postId"), page, size));
    }

    @RequestMapping(value = "/findRandom//{size}", method = RequestMethod.POST)
    public Result findRandom(@RequestBody Map searchMap, @PathVariable int size) {
        return new Result(true, StatusCode.OK, "查询成功", replyService.findRandom((String) searchMap.get("postId"), size));
    }
}
