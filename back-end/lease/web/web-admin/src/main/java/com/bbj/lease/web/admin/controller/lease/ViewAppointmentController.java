package com.bbj.lease.web.admin.controller.lease;


import com.bbj.lease.common.result.Result;
import com.bbj.lease.model.entity.ViewAppointment;
import com.bbj.lease.model.enums.AppointmentStatus;
import com.bbj.lease.web.admin.service.ViewAppointmentService;
import com.bbj.lease.web.admin.vo.appointment.AppointmentQueryVo;
import com.bbj.lease.web.admin.vo.appointment.AppointmentVo;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Tag(name = "预约看房管理")
@RequestMapping("/admin/appointment")
@RestController
public class ViewAppointmentController {

    @Autowired
    private ViewAppointmentService viewAppointmentService;

    @Operation(summary = "分页查询预约信息")
    @GetMapping("page")
    public Result<IPage<AppointmentVo>> page(@RequestParam long current, @RequestParam long size, AppointmentQueryVo queryVo) {
        //创建分页对象
        Page<ViewAppointment> page = new Page<>(current,size);
        IPage<AppointmentVo> iPage = viewAppointmentService.listPage(page, queryVo);
        return Result.ok(iPage);
    }

    @Operation(summary = "根据id更新预约状态")
    @PostMapping("updateStatusById")
    public Result updateStatusById(@RequestParam Long id, @RequestParam AppointmentStatus status) {
        LambdaUpdateWrapper<ViewAppointment> updateWrapper = new LambdaUpdateWrapper<>();

        //根据预约更新  不是公寓ID  是具体的一条数据的ID  然后设置状态
        updateWrapper.eq(ViewAppointment::getId,id)
                .set(ViewAppointment::getAppointmentStatus,status);
        viewAppointmentService.update(updateWrapper);
        return Result.ok();
    }

}
