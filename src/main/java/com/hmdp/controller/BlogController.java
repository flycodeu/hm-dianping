package com.hmdp.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hmdp.dto.Result;
import com.hmdp.dto.UserDTO;
import com.hmdp.entity.Blog;
import com.hmdp.entity.User;
import com.hmdp.service.IBlogService;
import com.hmdp.service.IUserService;
import com.hmdp.utils.SystemConstants;
import com.hmdp.utils.UserHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.websocket.server.PathParam;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@RestController
@RequestMapping( "/blog" )
public class BlogController {

    @Resource
    private IBlogService blogService;
    @Resource
    private IUserService userService;

    @PostMapping
    public Result saveBlog(@RequestBody Blog blog) {
        return blogService.saveBlog(blog);
    }

    @PutMapping( "/like/{id}" )
    public Result likeBlog(@PathVariable( "id" ) Long id) {
        // 修改点赞数量
        // blogService.update().setSql("liked = liked + 1").eq("id", id).update();
        blogService.likeBlog(id);
        return Result.ok();
    }


    @GetMapping( "/of/me" )
    public Result queryMyBlog(@RequestParam( value = "current", defaultValue = "1" ) Integer current) {
        // 获取登录用户
        UserDTO user = UserHolder.getUser();
        // 根据用户查询
        Page<Blog> page = blogService.query()
                .eq("user_id", user.getId()).page(new Page<>(current, SystemConstants.MAX_PAGE_SIZE));
        // 获取当前页数据
        List<Blog> records = page.getRecords();
        return Result.ok(records);
    }

    @GetMapping( "/hot" )
    public Result queryHotBlog(@RequestParam( value = "current", defaultValue = "1" ) Integer current) {
        return blogService.queryHotBlog(current);
    }

    /**
     * 查询单个笔记信息
     *
     * @param id
     * @return
     */
    @GetMapping( "/{id}" )
    public Result queryBlog(@PathVariable( "id" ) Long id) {
        return blogService.queryBlogById(id);
    }


    @GetMapping( "likes/{id}" )
    public Result queryBlogLikes(@PathVariable( "id" ) Long id) {
        return blogService.queryBlogLikes(id);
    }

    @GetMapping( "/of/user" )
    public Result queryBlogByUserId(@RequestParam( value = "current", defaultValue = "1" ) Integer current,
                                    @RequestParam( "id" ) Long id) {
        Page<Blog> page = blogService.query().eq("user_id", id).page(new Page<>(current, SystemConstants.MAX_PAGE_SIZE));
        List<Blog> records = page.getRecords();
        return Result.ok(records);
    }

    @GetMapping( "/of/follow" )
    public Result queryFollowBlog(@RequestParam( value = "lastId" ) Long max, @RequestParam( value = "offset", defaultValue = "0" ) Integer offset) {
        return blogService.queryBlogOfFollow(max, offset);
    }
}
