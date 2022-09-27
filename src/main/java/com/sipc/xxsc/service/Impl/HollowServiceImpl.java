package com.sipc.xxsc.service.Impl;

import com.github.pagehelper.PageHelper;
import com.sipc.xxsc.mapper.CommentMapper;
import com.sipc.xxsc.mapper.HollowMapper;
import com.sipc.xxsc.pojo.domain.Comment;
import com.sipc.xxsc.pojo.domain.Hollow;
import com.sipc.xxsc.pojo.dto.CommonResult;
import com.sipc.xxsc.pojo.dto.param.hollow.PutCommentsParam;
import com.sipc.xxsc.pojo.dto.param.hollow.PutHollowParam;
import com.sipc.xxsc.pojo.dto.result.NoData;
import com.sipc.xxsc.pojo.dto.result.Pages;
import com.sipc.xxsc.pojo.dto.result.hollow.CommentResult;
import com.sipc.xxsc.pojo.dto.result.hollow.HollowResult;
import com.sipc.xxsc.pojo.dto.resultEnum.ResultEnum;
import com.sipc.xxsc.pojo.po.CommentPo;
import com.sipc.xxsc.pojo.po.HollowPo;
import com.sipc.xxsc.service.HollowService;
import com.sipc.xxsc.util.CheckRole.CheckRole;
import com.sipc.xxsc.util.CheckRole.result.JWTCheckResult;
import com.sipc.xxsc.util.TimeUtils;
import com.sipc.xxsc.util.redis.RedisUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class HollowServiceImpl implements HollowService {
    @Resource
    RedisUtil redisUtil;
    @Resource
    HollowMapper hollowMapper;
    @Resource
    CommentMapper commentMapper;
    /**
     * @return HollowResult 的页数
     */
    @Override
    public CommonResult<Pages> getHollowPages(HttpServletRequest request, HttpServletResponse response) {
        // 鉴权
        CommonResult<JWTCheckResult> check = CheckRole.check(request, response);
        if (!Objects.equals(check.getCode(), ResultEnum.SUCCESS.getCode()))
            return CommonResult.fail(check.getCode(), check.getMessage());
        Object hollowPages = redisUtil.get("hollowPages");
        Integer pages;
        if (hollowPages instanceof Integer){
            pages = (Integer)hollowPages;
        } else {
            Integer count = hollowMapper.selectCount();
            pages = (count % 5 == 0) ? count : count + 1;
            redisUtil.set("hollowPages", pages);
        }
        Pages result = new Pages();
        result.setPages(pages);
        return CommonResult.success(result);
    }

    /**
     * @param param 故事内容
     * @return 提交结果
     */
    @Override
    public CommonResult<NoData> putHollow(HttpServletRequest request, HttpServletResponse response, PutHollowParam param) {
        // 鉴权
        CommonResult<JWTCheckResult> check = CheckRole.check(request, response);
        if (!Objects.equals(check.getCode(), ResultEnum.SUCCESS.getCode()))
            return CommonResult.fail(check.getCode(), check.getMessage());
        Hollow hollow = new Hollow();
        hollow.setUserId(check.getData().getUserId());
        hollow.setStory(param.getStory());
        hollow.setTime(TimeUtils.getNow());
        hollowMapper.insert(hollow);
        if (redisUtil.exists("hollowPages"))
            redisUtil.remove("hollowPages");
        return CommonResult.success();
    }

    /**
     * @param param 评论内容
     * @return 评论结果
     */
    @Override
    public CommonResult<NoData> putComment(HttpServletRequest request, HttpServletResponse response, PutCommentsParam param) {
        // 鉴权
        CommonResult<JWTCheckResult> check = CheckRole.check(request, response);
        if (!Objects.equals(check.getCode(), ResultEnum.SUCCESS.getCode()))
            return CommonResult.fail(check.getCode(), check.getMessage());
        Comment comment = new Comment();
        comment.setUserId(check.getData().getUserId());
        comment.setTime(TimeUtils.getNow());
        comment.setComment(param.getComment());
        comment.setStoryId(param.getId());
        commentMapper.insert(comment);
        return CommonResult.success();
    }

    /**
     * @param page 页数
     * @return 树洞故事
     */
    @Override
    public CommonResult<List<HollowResult>> getHollows(HttpServletRequest request, HttpServletResponse response, Integer page) {        // 鉴权
        CommonResult<JWTCheckResult> check = CheckRole.check(request, response);
        if (!Objects.equals(check.getCode(), ResultEnum.SUCCESS.getCode()))
            return CommonResult.fail(check.getCode(), check.getMessage());
        PageHelper.startPage(page, 5);
        List<HollowPo> hollowPos = hollowMapper.selectHollows();
        List<HollowResult> results = new ArrayList<>();
        for (HollowPo hollowPo : hollowPos) {
            HollowResult result = new HollowResult();
            result.setId(hollowPo.getId());
            result.setComments(hollowPo.getComments());
            result.setStory(hollowPo.getStory());
            result.setUser(hollowPo.getUser());
            result.setTime(TimeUtils.EasyRead(hollowPo.getTime()));
            results.add(result);
        }
        return CommonResult.success(results);
    }

    /**
     * @param page 页数
     * @return 评论信息
     */
    @Override
    public CommonResult<List<CommentResult>> getComments(HttpServletRequest request, HttpServletResponse response, Integer page, Integer storyId) {
        // 鉴权
        CommonResult<JWTCheckResult> check = CheckRole.check(request, response);
        if (!Objects.equals(check.getCode(), ResultEnum.SUCCESS.getCode()))
            return CommonResult.fail(check.getCode(), check.getMessage());
        PageHelper.startPage(page, 10);
        List<CommentPo> commentPos = commentMapper.selectByStoryId(page, storyId);
        List<CommentResult> results = new ArrayList<>();
        for (CommentPo commentPo : commentPos){
            CommentResult result = new CommentResult();
            result.setComment(commentPo.getComment());
            result.setTime(TimeUtils.EasyRead(commentPo.getTime()));
            result.setId(commentPo.getId());
            result.setUser(commentPo.getUser());
            results.add(result);
        }
        return CommonResult.success(results);
    }
}
