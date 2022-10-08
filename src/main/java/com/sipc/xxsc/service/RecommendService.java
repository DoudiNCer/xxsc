package com.sipc.xxsc.service;

import com.sipc.xxsc.pojo.dto.CommonResult;
import com.sipc.xxsc.pojo.dto.result.Pages;
import com.sipc.xxsc.pojo.dto.result.recommend.BooksResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface RecommendService {
    CommonResult<Pages> getBookPages(HttpServletRequest request, HttpServletResponse response);

    CommonResult<List<BooksResult>> getBooks(HttpServletRequest request, HttpServletResponse response, Integer page);

    CommonResult<List<BooksResult>> searchBooks(HttpServletRequest request, HttpServletResponse response, String keyword);
}
