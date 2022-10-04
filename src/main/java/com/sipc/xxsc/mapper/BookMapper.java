package com.sipc.xxsc.mapper;

import com.sipc.xxsc.pojo.domain.Book;

import java.util.List;

public interface BookMapper {
    Integer selectCount();

    List<Book> selectBooks();
}
