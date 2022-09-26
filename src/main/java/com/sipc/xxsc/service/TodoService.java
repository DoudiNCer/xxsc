package com.sipc.xxsc.service;

import com.sipc.xxsc.pojo.dto.CommonResult;
import com.sipc.xxsc.pojo.dto.param.todo.PostTodoParam;
import com.sipc.xxsc.pojo.dto.param.todo.PutTodoParam;
import com.sipc.xxsc.pojo.dto.result.NoData;
import com.sipc.xxsc.pojo.dto.result.todo.TodoDetail;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface TodoService {
    CommonResult getTodo(HttpServletRequest request, HttpServletResponse response);

    CommonResult<NoData> putTodo(HttpServletRequest request, HttpServletResponse response, PutTodoParam param);

    CommonResult<NoData> postTodo(HttpServletRequest request, HttpServletResponse response, PostTodoParam param);
}
