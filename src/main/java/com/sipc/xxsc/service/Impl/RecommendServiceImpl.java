package com.sipc.xxsc.service.Impl;

import com.github.pagehelper.PageHelper;
import com.sipc.xxsc.mapper.BookMapper;
import com.sipc.xxsc.pojo.domain.Book;
import com.sipc.xxsc.pojo.dto.CommonResult;
import com.sipc.xxsc.pojo.dto.result.Pages;
import com.sipc.xxsc.pojo.dto.result.recommend.BooksResult;
import com.sipc.xxsc.pojo.dto.resultEnum.ResultEnum;
import com.sipc.xxsc.service.RecommendService;
import com.sipc.xxsc.util.CheckRole.CheckRole;
import com.sipc.xxsc.util.CheckRole.result.JWTCheckResult;
import com.sipc.xxsc.util.redis.RedisEnum;
import com.sipc.xxsc.util.redis.RedisUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class RecommendServiceImpl implements RecommendService {
    @Resource
    BookMapper bookMapper;
    @Resource
    RedisUtil redisUtil;
    /**
     * @return 书籍推荐页数
     */
    @Override
    public CommonResult<Pages> getBookPages(HttpServletRequest request, HttpServletResponse response) {
        // 鉴权
        CommonResult<JWTCheckResult> check = CheckRole.check(request, response);
        if (!Objects.equals(check.getCode(), ResultEnum.SUCCESS.getCode()))
            return CommonResult.fail(check.getCode(), check.getMessage());

        Object hollowPages = redisUtil.get(RedisEnum.BOOKPAGES.getVarName());
        Integer pages;
        if (hollowPages instanceof Integer){
            pages = (Integer)hollowPages;
        } else {
            Integer count = bookMapper.selectCount();
            pages = count / RedisEnum.BOOKPAGES.getPageSize() + (count % RedisEnum.BOOKPAGES.getPageSize() == 0 ? 0 : 1);
            redisUtil.set(RedisEnum.BOOKPAGES.getVarName(), pages);
        }
        Pages result = new Pages();
        result.setPages(pages);
        return CommonResult.success(result);
    }

    /**
     * @param page 书籍推荐页数
     * @return 书籍
     */
    @Override
    public CommonResult<List<BooksResult>> getBooks(HttpServletRequest request, HttpServletResponse response, Integer page) {
        CommonResult<JWTCheckResult> check = CheckRole.check(request, response);
        if (!Objects.equals(check.getCode(), ResultEnum.SUCCESS.getCode()))
            return CommonResult.fail(check.getCode(), check.getMessage());

        List<BooksResult> results = new ArrayList<>();
        PageHelper.startPage(page, RedisEnum.BOOKPAGES.getPageSize());
        for (Book selectBook : bookMapper.selectBooks()) {
            results.add(new BooksResult(selectBook));
        }
        return CommonResult.success(results);
    }

    /**
     * @param keyword 关键词
     * @return 搜索结果
     */
    @Override
    public CommonResult<List<BooksResult>> searchBooks(HttpServletRequest request, HttpServletResponse response, String keyword) {
        CommonResult<JWTCheckResult> check = CheckRole.check(request, response);
        if (!Objects.equals(check.getCode(), ResultEnum.SUCCESS.getCode()))
            return CommonResult.fail(check.getCode(), check.getMessage());

        List<BooksResult> results = new ArrayList<>();
        for (Book selectBook : bookMapper.searchBooks(keyword)) {
            results.add(new BooksResult(selectBook));
        }
        return CommonResult.success(results);
    }
}
