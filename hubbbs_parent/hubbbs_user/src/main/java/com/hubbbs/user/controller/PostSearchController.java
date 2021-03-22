package com.hubbbs.user.controller;

import com.hubbbs.user.pojo.PostSearch;
import com.hubbbs.user.service.PostSearchService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

/**
 * Created by IntelliJ IDEA.
 * User: HB
 * Date: 2019/4/3
 *
 * @author HB
 * To change this template use File | Settings | File Templates.
 */
@RestController
@CrossOrigin
@RequestMapping("/hubbbs/postsearch")
public class PostSearchController {
    @Autowired
    private PostSearchService postSearchService;

    @RequestMapping(method = RequestMethod.POST)
    public Result save(@RequestBody PostSearch postSearch) {
        postSearchService.add(postSearch);
        return new Result(true, StatusCode.OK, "操作成功");
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{keywords}/{page}/{size}")
    public Result findByKeywords(@PathVariable String keywords, @PathVariable int page, @PathVariable int size) {
        Page<PostSearch> postSearchPage = postSearchService.findByKeyboards(keywords, page, size);
        return new Result(true, StatusCode.OK, "搜索成功", new PageResult<>(postSearchPage.getTotalElements(), postSearchPage.getContent()));
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/deleteAll")
    public Result deleteAll() {
        postSearchService.deleteAll();
        return new Result(true, StatusCode.OK, "全部删除成功");
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{postSearchId}")
    public Result update(@RequestBody PostSearch postSearch, @PathVariable String postSearchId) {
        postSearch.setId(postSearchId);
        postSearchService.add(postSearch);
        return new Result(true, StatusCode.OK, "修改成功");
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{postSearchId}")
    public Result findById(@PathVariable String postSearchId) {
        return new Result(true, StatusCode.OK, "修改成功", postSearchService.findById(postSearchId));
    }
}
