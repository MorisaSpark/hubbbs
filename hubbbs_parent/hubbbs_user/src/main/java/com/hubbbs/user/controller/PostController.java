package com.hubbbs.user.controller;

import com.hubbbs.user.pojo.Post;
import com.hubbbs.user.pojo.RelTag;
import com.hubbbs.user.pojo.Type;
import com.hubbbs.user.service.*;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 控制器层
 *
 * @author Administrator
 */
@RestController
@CrossOrigin
@RequestMapping("/hubbbs/post")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private UserService userService;

    @Autowired
    private RelTagService relTagService;

    @Autowired
    private TypeService typeService;
    @Autowired
    private RelCollectionService relCollectionService;

    /**
     * 查询全部数据
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll() {
        return new Result(true, StatusCode.OK, "查询成功", postService.findAll());
    }

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable String id, HttpServletRequest request) {
//        request用来更新redis中的点击量
        return new Result(true, StatusCode.OK, "查询成功", postService.findById(id, request.getRemoteUser()));
    }

    @RequestMapping(value = "/detail/{postId}", method = RequestMethod.GET)
    public Result findByIdDetail(@PathVariable String postId) {
        //
        String userId = (String) request.getAttribute("userId");
        Post post = postService.findById(postId);
        if (post.getIsBan() == 1) {
            return new Result(false, StatusCode.ERROR, "文章不可查看");
        }
        return new Result(true, StatusCode.OK, "查询成功", postService.findByIdDetailRedis(postId, userId));
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
        Page<Post> pageList = postService.findSearch(searchMap, page, size);
        return new Result(true, StatusCode.OK, "查询成功", new PageResult<>(pageList.getTotalElements(), pageList.getContent()));
    }

    @RequestMapping(value = "/listSummary/{page}/{size}", method = RequestMethod.POST)
    public Result getListSummary(@RequestBody Map type, @PathVariable int page, @PathVariable int size) {
        String typeId = (String) type.get("typeId");
        String orderBy = (String) type.get("orderBy");
        Page<Map<String, Object>> pageList = postService.getListSummary(page, size, typeId, orderBy);
        return new Result(true, StatusCode.OK, "查询成功", new PageResult<>(pageList.getTotalElements(), pageList.getContent()));
    }

    /**
     * 根据条件查询
     *
     * @param searchMap
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public Result findSearch(@RequestBody Map searchMap) {
        return new Result(true, StatusCode.OK, "查询成功", postService.findSearch(searchMap));
    }

    /**
     * 增加
     *
     * @param post
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result add(@RequestBody Post post) {
        postService.add(post);
        return new Result(true, StatusCode.OK, "增加成功");
    }

    /**
     * @return entity.Result
     * @desc 发帖的接口
     * @method saveSelf
     * @params [post]
     * @author hubdir
     * @date 2019/5/6 21:11
     */
    @RequestMapping(value = "/saveSelf", method = RequestMethod.POST)
    public Result saveSelf(@RequestBody Post post) {
        postService.add(post, (String) request.getAttribute("userId"));
        return new Result(true, StatusCode.OK, "增加成功");
    }

    /**
     * 修改
     *
     * @param post
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Result update(@RequestBody Post post, @PathVariable String id) {
        post.setId(id);
        postService.update(post);
        return new Result(true, StatusCode.OK, "修改成功");
    }

    /**
     * 删除
     *
     * @param id
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Result delete(@PathVariable String id) {
        Post post = postService.findById(id);
        String userId = (String) request.getAttribute("userId");
        String admin = (String) request.getAttribute("claims_admin");
        //删除文章要么是作者本人，要么是admin
        if (userId.equals(post.getUserId()) || admin != null) {
            postService.deleteById(id);
//            User user = userService.findById(userId);
//            user.setPostNum(user.getPostNum() - 1);
            typeService.updatePostNumById(post.getId(), "-1");
//            userService.update(user);
            return new Result(true, StatusCode.OK, "删除成功");
        } else {
            return new Result(false, StatusCode.ERROR, "权限不足");
        }
    }

    @RequestMapping(value = "/sumClickNum/{userId}", method = RequestMethod.POST)
    public Result findSumClickNumByUserId(@PathVariable String userId) {
        return new Result(true, StatusCode.OK, "查询成功", postService.findSumClickNumByUserId(userId));
    }

    @RequestMapping(value = "/Flowers/{page}/{size}", method = RequestMethod.POST)
    public Result searchByFlowers(@RequestBody Map map, @PathVariable Integer page, @PathVariable Integer size) {
        Page<Post> pageList = postService.searchByFlowers((String) map.get("userId"), (String) map.get("orderBy"), page, size);
        return new Result(true, StatusCode.OK, "查询成功", new PageResult<>(pageList.getTotalElements(), pageList.getContent()));
    }

    @RequestMapping(value = "/searchByUserIdSelf/{page}/{size}", method = RequestMethod.POST)
    public Result searchByUserIdSelf(@PathVariable Integer page, @PathVariable Integer size) {
        String userId = (String) request.getAttribute("userId");
        HashMap map = new HashMap<String, String>();
        map.put("userId", userId);
        Page<Post> pageList = postService.findSearch(map, page, size);
        return new Result(true, StatusCode.OK, "查询成功", new PageResult<>(pageList.getTotalElements(), pageList.getContent()));
    }

    @RequestMapping(value = "/findSumClickNumByUserIdSelf", method = RequestMethod.POST)
    public Result findSumClickNumByUserIdSelf() {
        String userId = (String) request.getAttribute("userId");
        return new Result(true, StatusCode.OK, "查询成功", postService.findSumClickNumByUserId(userId));
    }

    @RequestMapping(value = "/findByTag/{postId}/{page}/{size}", method = RequestMethod.GET)
    public Result findByTag(@PathVariable String postId, @PathVariable Integer page, @PathVariable Integer size) {
        List<String> summaries = relTagService.findSummaryByPostId(postId);
        if (summaries.size() == 0) {
            return new Result(true, StatusCode.OK, "无相关标签");
        }
        List<RelTag> relTagList = relTagService.findBySummaryIn(summaries);

        return new Result(true, StatusCode.OK, "成功获取相关文章", postService.findByPostInTag(relTagList, page, size));
    }

    @RequestMapping(value = "/findByType/{postId}/{page}/{size}", method = RequestMethod.GET)
    public Result findByType(@PathVariable String postId, @PathVariable Integer page, @PathVariable Integer size) {
        Post post = postService.findById(postId);
        Type type = typeService.findById(post.getTypeId());
        Page<Map<String, Object>> pageList = postService.findByTypeId(type.getId(), page, size);
        return new Result(true, StatusCode.OK, "成功获取相关文章", new PageResult<>(pageList.getTotalElements(), pageList.getContent()));
    }

    /**
     * @return
     * @desc 通过发表、收藏的文章的排序方式查询
     * @method findByUserIdType
     * @params [type, orderBy, page, size]
     * @author hubdir
     * @date 2019/5/6 1:16
     */
    @RequestMapping(value = "/findByUserIdType/{type}/{orderBy}/{page}/{size}", method = RequestMethod.GET)
    public Result findByUserIdType(@PathVariable String type, @PathVariable String orderBy, @PathVariable Integer page, @PathVariable Integer size) {
        String userId = (String) request.getAttribute("userId");
        if ("post".equals(type)) {
            Page<Map<String, Object>> pageList = postService.findPartByUserId(userId, page, size, orderBy);
            return new Result(true, StatusCode.OK, "成功查找发表文章", new PageResult<>(pageList.getTotalElements(), pageList.getContent()));
        } else if ("collection".equals(type)) {
            Page<Map<String, Object>> pageList = relCollectionService.findByUserIdWithPost(userId, page, size, orderBy);
            return new Result(true, StatusCode.OK, "成功查找收藏文章", new PageResult<>(pageList.getTotalElements(), pageList.getContent()));
        } else {
            return new Result(false, StatusCode.ERROR, "查询失败");
        }
    }
}
