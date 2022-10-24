package com.sipc.xxsc.service.Impl;

import com.github.pagehelper.PageHelper;
import com.sipc.xxsc.mapper.AdvisoryMapper;
import com.sipc.xxsc.mapper.DoctorMapper;
import com.sipc.xxsc.pojo.domain.Advisory;
import com.sipc.xxsc.pojo.dto.CommonResult;
import com.sipc.xxsc.pojo.dto.param.advisory.ReserveParam;
import com.sipc.xxsc.pojo.dto.result.advisory.*;
import com.sipc.xxsc.pojo.dto.resultEnum.ResultEnum;
import com.sipc.xxsc.pojo.po.advisory.DoctorDetailPo;
import com.sipc.xxsc.pojo.po.advisory.DoctorSummaryPo;
import com.sipc.xxsc.service.AdvisoryService;
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
public class AdvisoryServiceImpl implements AdvisoryService {
    @Resource
    DoctorMapper doctorMapper;
    @Resource
    AdvisoryMapper advisoryMapper;
    @Resource
    RedisUtil redisUtil;
    /**
     * @param page 页数
     * @return 医生列表
     */
    @Override
    public CommonResult<GetDoctorsResult> getDoctors(HttpServletRequest request, HttpServletResponse response, Integer page) {
        // 鉴权
        CommonResult<JWTCheckResult> check = CheckRole.check(request, response);
        if (!Objects.equals(check.getCode(), ResultEnum.SUCCESS.getCode()))
            return CommonResult.fail(check.getCode(), check.getMessage());
        List<DoctorSummaryResult> results = new ArrayList<>();
        PageHelper.startPage(page, RedisEnum.DOCTORPAGES.getPageSize());
        for (DoctorSummaryPo summaryPo : doctorMapper.selectDoctors())
            results.add(new DoctorSummaryResult(summaryPo));
        GetDoctorsResult result = new GetDoctorsResult();
        result.setDoctors(results);
        Object doctorPage = redisUtil.get(RedisEnum.DOCTORPAGES.getVarName());
        Integer pages;
        if (doctorPage instanceof Integer)
            pages = (Integer) doctorPage;
        else {
            Integer count = doctorMapper.selectCount();
            pages = count / RedisEnum.DOCTORPAGES.getPageSize() + (count % RedisEnum.DOCTORPAGES.getPageSize() == 0 ? 0 : 1);
            redisUtil.set(RedisEnum.DOCTORPAGES.getVarName(), pages);
        }
        result.setPages(pages);
        return CommonResult.success(result);
    }

    /**
     * @param id 医生ID
     * @return 医生详细信息
     */
    @Override
    public CommonResult<GetDoctorDetailResult> getDoctor(HttpServletRequest request, HttpServletResponse response, Integer id) {
        // 鉴权
        CommonResult<JWTCheckResult> check = CheckRole.check(request, response);
        if (!Objects.equals(check.getCode(), ResultEnum.SUCCESS.getCode()))
            return CommonResult.fail(check.getCode(), check.getMessage());
        DoctorDetailPo po = doctorMapper.selectById(id);
        if (po == null)
            return CommonResult.fail("医生不存在");
        return CommonResult.success(new GetDoctorDetailResult(po, TimeUtils.getAge(po.getBirthday())));
    }

    /**
     */
    @Override
    public CommonResult<AdvisoryReserveResult> reserve(HttpServletRequest request, HttpServletResponse response, ReserveParam param) {
        // 鉴权
        CommonResult<JWTCheckResult> check = CheckRole.check(request, response);
        if (!Objects.equals(check.getCode(), ResultEnum.SUCCESS.getCode()))
            return CommonResult.fail(check.getCode(), check.getMessage());
        if (check.getData().getIsDoctor())
            return CommonResult.userAuthError("医生哒咩捣乱");
        DoctorDetailPo doctorDetailPo = doctorMapper.selectById(param.getDoctorId());
        if (doctorDetailPo == null)
            return CommonResult.fail("医生不存在");
        Advisory advisory = advisoryMapper.selectByUserIdAndDoctorId(check.getData().getUserId(), param.getDoctorId());
        if (advisory == null) {
            advisory = new Advisory();
            advisory.setDoctorId(param.getDoctorId());
            advisory.setUserId(check.getData().getUserId());
            advisoryMapper.insert(advisory);
        }
        AdvisoryReserveResult result = new AdvisoryReserveResult();
        result.setAdvisoryId(advisory.getId());
        return CommonResult.success(result);
    }

    /**
     */
    @Override
    public CommonResult<List<getAdvisoryReserveResult>> getAdvisoryReserve(HttpServletRequest request, HttpServletResponse response) {
        // 鉴权
        CommonResult<JWTCheckResult> check = CheckRole.check(request, response);
        if (!Objects.equals(check.getCode(), ResultEnum.SUCCESS.getCode()))
            return CommonResult.fail(check.getCode(), check.getMessage());
        List<Advisory> advisories;
        if (check.getData().getIsDoctor())
            advisories = advisoryMapper.selectByDoctorId(check.getData().getUserId());
        else
            advisories = advisoryMapper.selectByUserId(check.getData().getUserId());
        List<getAdvisoryReserveResult> results = new ArrayList<>();
        for ( Advisory advisory : advisories)
            results.add(new getAdvisoryReserveResult(advisory));
        return CommonResult.success(results);
    }
}
