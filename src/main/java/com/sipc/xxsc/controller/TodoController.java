package com.sipc.xxsc.controller;

import com.sipc.xxsc.pojo.dto.CommonResult;
import com.sipc.xxsc.pojo.dto.param.mood.PostMoodParam;
import com.sipc.xxsc.pojo.dto.param.mood.PutMoodParam;
import com.sipc.xxsc.pojo.dto.param.todo.PostTodoParam;
import com.sipc.xxsc.pojo.dto.param.todo.PutTodoParam;
import com.sipc.xxsc.pojo.dto.result.NoData;
import com.sipc.xxsc.pojo.dto.result.mood.MoodDetail;
import com.sipc.xxsc.pojo.dto.result.todo.TodoDetail;
import com.sipc.xxsc.service.TodoService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/tdmd")
public class TodoController {
    @Resource
    HttpServletRequest request;
    @Resource
    HttpServletResponse response;
    @Resource
    TodoService todoService;


    @GetMapping("/mood")
    public CommonResult<TodoDetail> getTodo(){
        return todoService.getTodo(request, response);
    }

    @PutMapping("/mood")
    public CommonResult<NoData> putTodo(@Validated @RequestBody PutTodoParam param){
        return todoService.putTodo(request, response, param);
    }

    @PostMapping("/mood")
    public CommonResult<NoData> postTodo(@Validated @RequestBody PostTodoParam param){
        return todoService.postTodo(request, response, param);
    }
}
