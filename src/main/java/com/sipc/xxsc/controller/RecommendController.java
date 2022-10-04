package com.sipc.xxsc.controller;

import com.sipc.xxsc.pojo.dto.CommonResult;
import com.sipc.xxsc.pojo.dto.result.Pages;
import com.sipc.xxsc.pojo.dto.result.recommend.BooksResult;
import com.sipc.xxsc.service.RecommendService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/recommend")
public class RecommendController {
    @Resource
    HttpServletRequest request;
    @Resource
    HttpServletResponse response;
    @Resource
    RecommendService recommendService;
    @GetMapping("/bookPages")
    public CommonResult<Pages> getBookPages(){
        return recommendService.getBookPages(request, response);
    }

    @GetMapping("/books")
    public CommonResult<List<BooksResult>> getBookPages(
            @RequestParam(name = "page", defaultValue = "1")Integer page
    ){
        return recommendService.getBooks(request, response, page);
    }
}
