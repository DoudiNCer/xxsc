package com.sipc.xxsc.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sipc.xxsc.mapper.MoodMapper;
import com.sipc.xxsc.pojo.domain.Mood;
import com.sipc.xxsc.pojo.dto.CommonResult;
import com.sipc.xxsc.pojo.dto.param.mood.PostMoodParam;
import com.sipc.xxsc.pojo.dto.param.mood.PutMoodParam;
import com.sipc.xxsc.pojo.dto.result.NoData;
import com.sipc.xxsc.pojo.dto.result.mood.MoodDetail;
import com.sipc.xxsc.pojo.dto.result.mood.MoodSummary;
import com.sipc.xxsc.pojo.dto.resultEnum.ResultEnum;
import com.sipc.xxsc.service.MoodService;
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
public class MoodServiceImpl implements MoodService {
    @Resource
    MoodMapper moodMapper;
    /**
     * @apiNote 根据用户的Moods
     * @param page 页数
     * @return Moods的集合
     */
    @Override
    public CommonResult getMoods(HttpServletRequest request, HttpServletResponse response, Integer page) {
        // 鉴权
        CommonResult<JWTCheckResult> check = CheckRole.check(request, response);
        if (!Objects.equals(check.getCode(), ResultEnum.SUCCESS.getCode()))
            return CommonResult.fail(check.getCode(), check.getMessage());
        List<MoodSummary> results = new ArrayList<>();
        PageHelper.startPage(page, 7);
        List<Mood> moods = moodMapper.selectByUserId(check.getData().getUserId());
        PageInfo<Mood> pageInfo = new PageInfo<>(moods);
        for (Mood ms : pageInfo.getList()) {
            MoodSummary moodSummary = new MoodSummary();
            moodSummary.setMood(ms.getMood());
            moodSummary.setId(ms.getId());
            moodSummary.setMessage(ms.getMessage());
            results.add(moodSummary);
        }
        return CommonResult.success(results);
    }

    /**
     * @param id Mood ID
     * @return Mood 的详细信息
     */
    @Override
    public CommonResult<MoodDetail> getMood(HttpServletRequest request, HttpServletResponse response, Integer id) {
        // 鉴权
        CommonResult<JWTCheckResult> check = CheckRole.check(request, response);
        if (!Objects.equals(check.getCode(), ResultEnum.SUCCESS.getCode()))
            return CommonResult.fail(check.getCode(), check.getMessage());
        Mood mood = moodMapper.selectById(id);
        if (mood == null)
            return CommonResult.fail("Mood不存在");
        if (!Objects.equals(mood.getUserId(), check.getData().getUserId()))
            return CommonResult.userResourceException("Mood 与用户不匹配");
        MoodDetail result = new MoodDetail();
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
}
