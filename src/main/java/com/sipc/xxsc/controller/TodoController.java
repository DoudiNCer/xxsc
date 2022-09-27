package com.sipc.xxsc.controller;

import com.sipc.xxsc.pojo.dto.CommonResult;
import com.sipc.xxsc.pojo.dto.param.todo.PostTodoParam;
import com.sipc.xxsc.pojo.dto.param.todo.PutTodoParam;
import com.sipc.xxsc.pojo.dto.result.NoData;
import com.sipc.xxsc.pojo.dto.result.todo.TodoDetailResult;
import com.sipc.xxsc.service.TodoService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/tdmd")
public class TodoController {
    @Resource
    HttpServletRequest request;
    @Resource
    HttpServletResponse response;
    @Resource
    TodoService todoService;


    @GetMapping("/todo")
    public CommonResult<List<TodoDetailResult>> getTodo(){
        return todoService.getTodo(request, response);
    }

    @PutMapping("/todo")
    public CommonResult<NoData> putTodo(@Validated @RequestBody PutTodoParam param){
        return todoService.putTodo(request, response, param);
    }

    @PostMapping("/todo")
    public CommonResult<NoData> postTodo(@Validated @RequestBody PostTodoParam param){
        return todoService.postTodo(request, response, param);
    }
}
