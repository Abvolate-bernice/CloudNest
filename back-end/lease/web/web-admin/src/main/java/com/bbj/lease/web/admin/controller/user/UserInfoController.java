package com.bbj.lease.web.admin.controller.user;


import com.bbj.lease.common.result.Result;
import com.bbj.lease.model.entity.UserInfo;
import com.bbj.lease.model.enums.BaseStatus;
import com.bbj.lease.web.admin.service.UserInfoService;
import com.bbj.lease.web.admin.vo.user.UserInfoQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "用户信息管理")
@RestController
@RequestMapping("/admin/user")
public class UserInfoController {
    @Autowired
    private UserInfoService userInfoService;

    @Operation(summary = "分页查询用户信息")
    @GetMapping("page")
    public Result<IPage<UserInfo>> pageUserInfo(@RequestParam long current, @RequestParam long size, UserInfoQueryVo queryVo) {

        Page<UserInfo> page = new Page<>(current, size);
        LambdaQueryWrapper<UserInfo> wrapper = new LambdaQueryWrapper<>();
//                .eq(UserInfo::getPhone, queryVo.getPhone())   手机号应该按照模糊匹配 ，并且又要有个boolean 类型的参数，要先进行判断
//                .or()
//                .eq(UserInfo::getStatus, queryVo.getStatus());   同样的状态也需要加，因为也有可能不选


        wrapper.like(queryVo.getPhone()!=null,UserInfo::getPhone, queryVo.getPhone())  //不为空再加这个条件
                .eq(queryVo.getStatus()!=null,UserInfo::getStatus, queryVo.getStatus());//不为空再加这个条件
        Page<UserInfo> iPage = userInfoService.page(page, wrapper);
        return Result.ok(iPage);
    }

    @Operation(summary = "根据用户id更新账号状态")
    @PostMapping("updateStatusById")
    public Result updateStatusById(@RequestParam Long id, @RequestParam BaseStatus status) {
        LambdaUpdateWrapper<UserInfo> wrapper = new LambdaUpdateWrapper<UserInfo>()
                .eq(UserInfo::getId, id)
                .set(UserInfo::getStatus, status);
        userInfoService.update(wrapper);

        return Result.ok();
    }
}
