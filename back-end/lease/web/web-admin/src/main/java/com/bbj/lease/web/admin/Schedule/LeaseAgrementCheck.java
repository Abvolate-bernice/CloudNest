package com.bbj.lease.web.admin.Schedule;

import com.bbj.lease.model.entity.LeaseAgreement;
import com.bbj.lease.model.enums.LeaseStatus;
import com.bbj.lease.web.admin.service.LeaseAgreementService;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class LeaseAgrementCheck {
    @Autowired
   private LeaseAgreementService  leaseAgreementService;
    @Scheduled(cron = "0 0 0 * * *")   //设置定时在每天的0.00     秒 分 时 日 月 周 （0和7 都代表周日）  把过期的租约状态设置为过期
    public void leaseAgrementExpiredCheck(){
        LambdaUpdateWrapper<LeaseAgreement> wrapper = new LambdaUpdateWrapper<LeaseAgreement>()
                .le(LeaseAgreement::getLeaseEndDate, new Date())
                .in(LeaseAgreement::getStatus, LeaseStatus.SIGNED, LeaseStatus.WITHDRAWN)
                .set(LeaseAgreement::getStatus,LeaseStatus.EXPIRED);
        leaseAgreementService.update(wrapper);
    }
}
