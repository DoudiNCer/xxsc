package com.sipc.xxsc.mapper;

import com.sipc.xxsc.pojo.domain.Todo;

import java.util.List;

public interface TodoMapper {

    List<Todo> selectTodayTodosByUserId(Integer userId);

    int insert(Todo todo);

    Todo selectTodayTodosById(Integer id);

    int updateFinishStatusById(Integer id, Integer finish);
}
