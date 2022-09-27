package com.sipc.xxsc.service.Impl;

import com.sipc.xxsc.mapper.TodoMapper;
import com.sipc.xxsc.pojo.domain.Todo;
import com.sipc.xxsc.pojo.dto.CommonResult;
import com.sipc.xxsc.pojo.dto.param.todo.PostTodoParam;
import com.sipc.xxsc.pojo.dto.param.todo.PutTodoParam;
import com.sipc.xxsc.pojo.dto.result.NoData;
import com.sipc.xxsc.pojo.dto.result.todo.TodoDetailResult;
import com.sipc.xxsc.pojo.dto.resultEnum.ResultEnum;
import com.sipc.xxsc.service.TodoService;
import com.sipc.xxsc.util.CheckRole.CheckRole;
import com.sipc.xxsc.util.CheckRole.result.JWTCheckResult;
import com.sipc.xxsc.util.TimeUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class TodoServiceImpl implements TodoService {
    @Resource
    TodoMapper todoMapper;
    /**
     * @apiNote 获取当天的Todo
     * @return 当天的Todo
     */
    @Override
    public CommonResult<List<TodoDetailResult>> getTodo(HttpServletRequest request, HttpServletResponse response) {
        // 鉴权
        CommonResult<JWTCheckResult> check = CheckRole.check(request, response);
        if (!Objects.equals(check.getCode(), ResultEnum.SUCCESS.getCode()))
            return CommonResult.fail(check.getCode(), check.getMessage());
        List<TodoDetailResult> results = new ArrayList<>();
        for (Todo todo : todoMapper.selectTodayTodosByUserId(check.getData().getUserId())) {
            TodoDetailResult todoDetailResult = new TodoDetailResult();
            todoDetailResult.setId(todo.getId());
            todoDetailResult.setFinish(todo.getFinish());
            todoDetailResult.setTodo(todo.getTodo());
            results.add(todoDetailResult);
        }
        return CommonResult.success(results);
    }

    /**
     * @param param 要创建的 Todo 的内容、用户信息
     * @return 插入结果
     */
    @Override
    public CommonResult<NoData> putTodo(HttpServletRequest request, HttpServletResponse response, PutTodoParam param) {
        // 鉴权
        CommonResult<JWTCheckResult> check = CheckRole.check(request, response);
        if (!Objects.equals(check.getCode(), ResultEnum.SUCCESS.getCode()))
            return CommonResult.fail(check.getCode(), check.getMessage());
        Todo todo = new Todo();
        todo.setTodo(param.getTodo());
        todo.setUserId(check.getData().getUserId());
        todo.setDate(TimeUtils.getNow());
        todoMapper.insert(todo);
        return CommonResult.success();
    }

    /**
     * @param param 要更新状态的Todo的ID、更新后的状态
     * @return 更新结果
     */
    @Override
    public CommonResult<NoData> postTodo(HttpServletRequest request, HttpServletResponse response, PostTodoParam param) {
        // 鉴权
        CommonResult<JWTCheckResult> check = CheckRole.check(request, response);
        if (!Objects.equals(check.getCode(), ResultEnum.SUCCESS.getCode()))
            return CommonResult.fail(check.getCode(), check.getMessage());
        Todo oldTodo = todoMapper.selectTodayTodosById(param.getId());
        if (oldTodo == null)
            return CommonResult.fail("Todo不存在");
        if (!Objects.equals(oldTodo.getUserId(), check.getData().getUserId()))
            return CommonResult.fail("Todo与当前用户不匹配");
        if (!Objects.equals(oldTodo.getFinish(), param.getFinish()))
            todoMapper.updateFinishStatusById(param.getId(), param.getFinish());
        return CommonResult.success();
    }
}
