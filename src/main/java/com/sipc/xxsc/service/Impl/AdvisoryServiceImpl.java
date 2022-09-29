package com.sipc.xxsc.service.Impl;

import com.github.pagehelper.PageHelper;
import com.sipc.xxsc.mapper.DoctorMapper;
import com.sipc.xxsc.pojo.dto.CommonResult;
import com.sipc.xxsc.pojo.dto.result.advisory.DoctorSummaryResult;
import com.sipc.xxsc.pojo.dto.result.advisory.GetDoctorDetailResult;
import com.sipc.xxsc.pojo.dto.result.advisory.GetDoctorsResult;
import com.sipc.xxsc.pojo.dto.resultEnum.ResultEnum;
import com.sipc.xxsc.pojo.po.advisory.DoctorDetailPo;
import com.sipc.xxsc.pojo.po.advisory.DoctorSummaryPo;
import com.sipc.xxsc.service.AdvisoryService;
import com.sipc.xxsc.util.CheckRole.CheckRole;
import com.sipc.xxsc.util.CheckRole.result.JWTCheckResult;
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
        PageHelper.startPage(page, 6);
        for (DoctorSummaryPo summaryPo : doctorMapper.selectDoctors())
            results.add(new DoctorSummaryResult(summaryPo));
        GetDoctorsResult result = new GetDoctorsResult();
        result.setDoctors(results);
        Object doctorPage = redisUtil.get("doctorPage");
        Integer pages;
        if (doctorPage instanceof Integer)
            pages = (Integer) doctorPage;
        else {
            Integer count = doctorMapper.selectCount();
            pages = count / 6 + (count % 6 == 0 ? 0 : 1);
            redisUtil.set("doctorPage", pages);
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
        return CommonResult.success(new GetDoctorDetailResult(po));
    }
}
