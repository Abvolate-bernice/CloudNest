package com.bbj.lease.web.admin.controller.system;


import com.bbj.lease.common.result.Result;
import com.bbj.lease.model.entity.SystemUser;
import com.bbj.lease.model.enums.BaseStatus;
import com.bbj.lease.web.admin.service.SystemUserService;
import com.bbj.lease.web.admin.vo.system.user.SystemUserItemVo;
import com.bbj.lease.web.admin.vo.system.user.SystemUserQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Tag(name = "后台用户信息管理")
@RestController
@RequestMapping("/admin/system/user")
public class SystemUserController {
    @Autowired
    private SystemUserService service;

    @Operation(summary = "根据条件分页查询后台用户列表")
    @GetMapping("page")
    public Result<IPage<SystemUserItemVo>> page(@RequestParam long current, @RequestParam long size, SystemUserQueryVo queryVo) {
        Page<SystemUserItemVo> Page = new Page<>(current, size);
        IPage<SystemUserItemVo> pagedItem = service.pageItem(Page, queryVo);
        return Result.ok(pagedItem);
    }

    @Operation(summary = "根据ID查询后台用户信息")
    @GetMapping("getById")
    public Result<SystemUserItemVo> getById(@RequestParam Long id) {
        SystemUserItemVo detailById = service.getDetailById(id);
        return Result.ok(detailById);
    }

    @Operation(summary = "保存或更新后台用户信息")
    @PostMapping("saveOrUpdate")
    public Result saveOrUpdate(@RequestBody SystemUser systemUser) {

        //不能吧明文的用户密码保存到数据库中  使用单向函数进行加密  常用的单项函数::MD5 sha-256

        //Mybatis-plus 提供的更新方法（就是通用Service Mapper） 如果实体中的字段为null，默认情况下
        //最终生成的update语句中不会包含这个字段

        //如果想修改在YML中配置属性 update-strategy :   或者局部配置在@TableField 中设置updateStrategy 的属性值
        if(systemUser.getPassword()!=null){
            String string = DigestUtils.md5Hex(systemUser.getPassword());
            systemUser.setPassword(string);
        }
        service.saveOrUpdate(systemUser);
        return Result.ok();
    }

    @Operation(summary = "判断后台用户名是否可用")
    @GetMapping("isUserNameAvailable")
    public Result<Boolean> isUsernameExists(@RequestParam String username) {
        LambdaQueryWrapper<SystemUser> wrapper = new LambdaQueryWrapper<SystemUser>()
                .eq(SystemUser::getUsername, username);
        long count = service.count(wrapper);
        //查出来相同名字的数据为0 则为TRUE

        return Result.ok(count==0);
    }

    @DeleteMapping("deleteById")
    @Operation(summary = "根据ID删除后台用户信息")
    public Result removeById(@RequestParam Long id) {
        service.removeById(id);
        return Result.ok();
    }

    @Operation(summary = "根据ID修改后台用户状态")
    @PostMapping("updateStatusByUserId")
    public Result updateStatusByUserId(@RequestParam Long id, @RequestParam BaseStatus status) {
        LambdaUpdateWrapper<SystemUser> wrapper = new LambdaUpdateWrapper<SystemUser>()
                .eq(SystemUser::getId, id)
                .set(SystemUser::getStatus, status);
        service.update(wrapper);
        return Result.ok();
    }
}
