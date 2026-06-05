package com.bbj.lease.web.admin.mapper;

import com.bbj.lease.model.entity.LeaseAgreement;
import com.bbj.lease.web.admin.vo.agreement.AgreementQueryVo;
import com.bbj.lease.web.admin.vo.agreement.AgreementVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
* @author liubo
* @description 针对表【lease_agreement(租约信息表)】的数据库操作Mapper
* @createDate 2023-07-24 15:48:00
* @Entity com.atguigu.lease.model.LeaseAgreement
*/
public interface LeaseAgreementMapper extends BaseMapper<LeaseAgreement> {

    //后面开发无需关注分页逻辑 只需要编写纯生SQL就行  结果要匹配AgreementVo实体类型

    IPage<AgreementVo> pageItem(Page<AgreementVo> page, AgreementQueryVo queryVo);

}




