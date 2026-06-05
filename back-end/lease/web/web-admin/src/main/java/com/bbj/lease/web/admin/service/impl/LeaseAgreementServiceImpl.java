package com.bbj.lease.web.admin.service.impl;

import com.atguigu.lease.web.admin.mapper.*;
import com.bbj.lease.web.admin.mapper.*;
import com.bbj.lease.web.admin.service.LeaseAgreementService;
import com.bbj.lease.web.admin.vo.agreement.AgreementQueryVo;
import com.bbj.lease.web.admin.vo.agreement.AgreementVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bbj.lease.model.entity.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author liubo
 * @description 针对表【lease_agreement(租约信息表)】的数据库操作Service实现
 * @createDate 2023-07-24 15:48:00
 */
@Service
public class LeaseAgreementServiceImpl extends ServiceImpl<LeaseAgreementMapper, LeaseAgreement>
        implements LeaseAgreementService {

    @Autowired
    private LeaseAgreementMapper leaseAgreementMapper;

    @Autowired
    private ApartmentInfoMapper apartmentInfoMapper;

    @Autowired
    private RoomInfoMapper roomInfoMapper;

    @Autowired
    private PaymentTypeMapper paymentTypeMapper;

    @Autowired
    private LeaseTermMapper leaseTermMapper;

    @Override
    public IPage<AgreementVo> pageItem(Page<AgreementVo> page, AgreementQueryVo queryVo) {
        return leaseAgreementMapper.pageItem(page, queryVo);
    }

    @Override
    //这个查出来是租约的ID
    public AgreementVo getAgreementById(Long id) {
        //首先先查出来基本信息
        LeaseAgreement leaseAgreement = leaseAgreementMapper.selectById(id);


//不需要条件构造器  因为只需要byID就可以了  知道ID就行
//        Long apartmentId = leaseAgreement.getApartmentId();
//        LambdaQueryWrapper<ApartmentInfo> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(ApartmentInfo::getId, apartmentId);
//        List<ApartmentInfo> apartmentInfos = apartmentInfoMapper.selectList(queryWrapper);


        //查询公寓详细信息
        ApartmentInfo apartmentInfo = apartmentInfoMapper.selectById(leaseAgreement.getApartmentId());

        //签约房间信息
        RoomInfo roomInfo = roomInfoMapper.selectById(leaseAgreement.getRoomId());

        LeaseTerm leaseTerm = leaseTermMapper.selectById(leaseAgreement.getLeaseTermId());

        PaymentType paymentType = paymentTypeMapper.selectById(leaseAgreement.getPaymentTypeId());
        AgreementVo agreementVo = new AgreementVo();
        agreementVo.setRoomInfo(roomInfo);
        agreementVo.setApartmentInfo(apartmentInfo);
        agreementVo.setPaymentType(paymentType);
        agreementVo.setLeaseTerm(leaseTerm);

        //必须要手动设置新的实体类的属性复制继承的实体类的属性
                     //原始 ，到目标类
        BeanUtils.copyProperties(apartmentInfo,agreementVo);
        return agreementVo;

    }

}

//        LeaseAgreement leaseAgreement = leaseAgreementMapper.selectById(id);
//
//        ApartmentInfo apartmentInfo = apartmentInfoMapper.selectById(leaseAgreement.getApartmentId());
//
//        RoomInfo roomInfo = roomInfoMapper.selectById(leaseAgreement.getRoomId());
//
//        LeaseTerm leaseTerm = leaseTermMapper.selectById(leaseAgreement.getLeaseTermId());
//
//        PaymentType paymentType = paymentTypeMapper.selectById(leaseAgreement.getPaymentTypeId());
//
//        AgreementVo agreementVo = new AgreementVo();
//        BeanUtils.copyProperties(leaseAgreement, agreementVo);
//        agreementVo.setApartmentInfo(apartmentInfo);
//        agreementVo.setRoomInfo(roomInfo);
//        agreementVo.setPaymentType(paymentType);
//        agreementVo.setLeaseTerm(leaseTerm);
//
//        return agreementVo;
//}


