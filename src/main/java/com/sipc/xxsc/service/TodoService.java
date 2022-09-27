package com.sipc.xxsc.service;

import com.sipc.xxsc.pojo.dto.CommonResult;
import com.sipc.xxsc.pojo.dto.param.todo.PostTodoParam;
import com.sipc.xxsc.pojo.dto.param.todo.PutTodoParam;
import com.sipc.xxsc.pojo.dto.result.NoData;
import com.sipc.xxsc.pojo.dto.result.todo.TodoDetailResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface TodoService {
    CommonResult<List<TodoDetailResult>> getTodo(HttpServletRequest request, HttpServletResponse response);

    CommonResult<NoData> putTodo(HttpServletRequest request, HttpServletResponse response, PutTodoParam param);

    CommonResult<NoData> postTodo(HttpServletRequest request, HttpServletResponse response, PostTodoParam param);
}
