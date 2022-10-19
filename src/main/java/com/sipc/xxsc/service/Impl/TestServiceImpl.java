package com.sipc.xxsc.service.Impl;

import com.github.pagehelper.PageHelper;
import com.sipc.xxsc.mapper.TestMapper;
import com.sipc.xxsc.pojo.domain.Test;
import com.sipc.xxsc.pojo.dto.CommonResult;
import com.sipc.xxsc.pojo.dto.result.Pages;
import com.sipc.xxsc.pojo.dto.result.test.GetTestsResult;
import com.sipc.xxsc.pojo.dto.resultEnum.ResultEnum;
import com.sipc.xxsc.service.TestService;
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
public class TestServiceImpl implements TestService {
    @Resource
    RedisUtil redisUtil;
    @Resource
    TestMapper testMapper;
    /**
     * @param request 
     * @param response
     * @param page
     * @return
     */
    @Override
    public CommonResult<List<GetTestsResult>> getTests(HttpServletRequest request, HttpServletResponse response, Integer page) {
        // 鉴权
        CommonResult<JWTCheckResult> check = CheckRole.check(request, response);
        if (!Objects.equals(check.getCode(), ResultEnum.SUCCESS.getCode()))
            return CommonResult.fail(check.getCode(), check.getMessage());

        List<GetTestsResult> results = new ArrayList<>();
        PageHelper.startPage(page, RedisEnum.BOOKPAGES.getPageSize());
        for (Test selectTest : testMapper.selectTests()) {
            results.add(new GetTestsResult(selectTest));
        }
        return CommonResult.success(results);
    }

    /**
     * @param request 
     * @param response
     * @return
     */
    @Override
    public CommonResult<Pages> getTestPages(HttpServletRequest request, HttpServletResponse response) {
        // 鉴权
        CommonResult<JWTCheckResult> check = CheckRole.check(request, response);
        if (!Objects.equals(check.getCode(), ResultEnum.SUCCESS.getCode()))
            return CommonResult.fail(check.getCode(), check.getMessage());

        Object hollowPages = redisUtil.get(RedisEnum.TESTPAGES.getVarName());
        Integer pages;
        if (hollowPages instanceof Integer){
            pages = (Integer)hollowPages;
        } else {
            Integer count = testMapper.selectCount();
            pages = count / RedisEnum.TESTPAGES.getPageSize() + (count % RedisEnum.TESTPAGES.getPageSize() == 0 ? 0 : 1);
            redisUtil.set(RedisEnum.TESTPAGES.getVarName(), pages);
        }
        Pages result = new Pages();
        result.setPages(pages);
        return CommonResult.success(result);
    }
}
