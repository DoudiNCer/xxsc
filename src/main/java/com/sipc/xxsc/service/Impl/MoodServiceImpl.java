package com.sipc.xxsc.service.Impl;

import com.github.pagehelper.PageHelper;
import com.sipc.xxsc.mapper.MoodMapper;
import com.sipc.xxsc.pojo.domain.Mood;
import com.sipc.xxsc.pojo.dto.CommonResult;
import com.sipc.xxsc.pojo.dto.param.mood.PostMoodParam;
import com.sipc.xxsc.pojo.dto.param.mood.PutMoodParam;
import com.sipc.xxsc.pojo.dto.result.NoData;
import com.sipc.xxsc.pojo.dto.result.Pages;
import com.sipc.xxsc.pojo.dto.result.mood.MoodDetailResult;
import com.sipc.xxsc.pojo.dto.result.mood.MoodSummaryResult;
import com.sipc.xxsc.pojo.dto.resultEnum.ResultEnum;
import com.sipc.xxsc.service.MoodService;
import com.sipc.xxsc.util.CheckRole.CheckRole;
import com.sipc.xxsc.util.CheckRole.result.JWTCheckResult;
import com.sipc.xxsc.util.TimeUtils;
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
public class MoodServiceImpl implements MoodService {
    @Resource
    MoodMapper moodMapper;
    @Resource
    RedisUtil redisUtil;
    /**
     * @apiNote 根据用户的Moods
     * @param page 页数
     * @return Moods的集合
     */
    @Override
    public CommonResult<List<MoodSummaryResult>> getMoods(HttpServletRequest request, HttpServletResponse response, Integer page) {
        // 鉴权
        CommonResult<JWTCheckResult> check = CheckRole.check(request, response);
        if (!Objects.equals(check.getCode(), ResultEnum.SUCCESS.getCode()))
            return CommonResult.fail(check.getCode(), check.getMessage());
        List<MoodSummaryResult> results = new ArrayList<>();
        PageHelper.startPage(page, RedisEnum.MOODPAGES.getPageSize());
        List<Mood> moods = moodMapper.selectByUserId(check.getData().getUserId());
        for (Mood ms : moods) {
            MoodSummaryResult moodSummaryResult = new MoodSummaryResult();
            moodSummaryResult.setMood(ms.getMood());
            moodSummaryResult.setId(ms.getId());
            moodSummaryResult.setDate(TimeUtils.formatDateTime(ms.getDate()));
            results.add(moodSummaryResult);
        }
        return CommonResult.success(results);
    }

    /**
     * @param id Mood ID
     * @return Mood 的详细信息
     */
    @Override
    public CommonResult<MoodDetailResult> getMood(HttpServletRequest request, HttpServletResponse response, Integer id) {
        // 鉴权
        CommonResult<JWTCheckResult> check = CheckRole.check(request, response);
        if (!Objects.equals(check.getCode(), ResultEnum.SUCCESS.getCode()))
            return CommonResult.fail(check.getCode(), check.getMessage());
        Mood mood = moodMapper.selectById(id);
        if (mood == null)
            return CommonResult.fail("Mood不存在");
        if (!Objects.equals(mood.getUserId(), check.getData().getUserId()))
            return CommonResult.userResourceException("Mood 与用户不匹配");
        MoodDetailResult result = new MoodDetailResult();
        result.setMood(mood.getMood());
        result.setMessage(mood.getMessage());
        result.setDate(TimeUtils.formatDateTime(mood.getDate()));
        return CommonResult.success(result);
    }

    /**
     * @param param 心情与评价
     * @return 提交结果
     */
    @Override
    public CommonResult<NoData> putMood(HttpServletRequest request, HttpServletResponse response, PutMoodParam param) {
        // 鉴权
        CommonResult<JWTCheckResult> check = CheckRole.check(request, response);
        if (!Objects.equals(check.getCode(), ResultEnum.SUCCESS.getCode()))
            return CommonResult.fail(check.getCode(), check.getMessage());
        if (param.getMessage().length() > 100)
            return CommonResult.fail("message过长");
        Mood mood = new Mood();
        mood.setMood(param.getMood());
        mood.setUserId(check.getData().getUserId());
        mood.setMessage(param.getMessage());
        mood.setDate(TimeUtils.getNow());
        moodMapper.insert(mood);
        return CommonResult.success();
    }

    /**
     * @param param 要更新的Mood的内容
     * @return 更新结果
     */
    @Override
    public CommonResult<NoData> postMood(HttpServletRequest request, HttpServletResponse response, PostMoodParam param) {
        // 鉴权
        CommonResult<JWTCheckResult> check = CheckRole.check(request, response);
        if (!Objects.equals(check.getCode(), ResultEnum.SUCCESS.getCode()))
            return CommonResult.fail(check.getCode(), check.getMessage());
        Mood nMood = moodMapper.selectById(param.getId());
        if (nMood == null)
            return CommonResult.fail("ID对应Mood不存在");
        if (!Objects.equals(nMood.getUserId(), check.getData().getUserId()))
            return CommonResult.userResourceException("Mood ID 与用户不匹配");
        Mood mood = new Mood();
        mood.setId(param.getId());
        mood.setMood(param.getMood());
        mood.setMessage(param.getMessage());
        moodMapper.updateById(mood);
        return CommonResult.success();
    }

    /**
     * @return Mood页数
     */
    @Override
    public CommonResult<Pages> getMoodPages(HttpServletRequest request, HttpServletResponse response) {
        // 鉴权
        CommonResult<JWTCheckResult> check = CheckRole.check(request, response);
        if (!Objects.equals(check.getCode(), ResultEnum.SUCCESS.getCode()))
            return CommonResult.fail(check.getCode(), check.getMessage());
        Integer count = moodMapper.selectCount(check.getData().getUserId());
        Integer pages = count / RedisEnum.MOODPAGES.getPageSize() + (count % RedisEnum.MOODPAGES.getPageSize() == 0 ? 0 : 1);
        Pages result = new Pages();
        result.setPages(pages);
        return CommonResult.success(result);
    }

    /**
     * @return 今日Mood
     */
    @Override
    public CommonResult<MoodDetailResult> getTodayMood(HttpServletRequest request, HttpServletResponse response) {
        // 鉴权
        CommonResult<JWTCheckResult> check = CheckRole.check(request, response);
        if (!Objects.equals(check.getCode(), ResultEnum.SUCCESS.getCode()))
            return CommonResult.fail(check.getCode(), check.getMessage());
        Mood mood = moodMapper.selectAfterTime(TimeUtils.getNow());
        if (mood == null)
            return CommonResult.success(null);
        else{
            MoodDetailResult result = new MoodDetailResult();
            result.setMood(mood.getMood());
            result.setMessage(mood.getMessage());
            result.setDate(TimeUtils.formatDateTime(mood.getDate()));
            return CommonResult.success(result);
        }
    }
}
