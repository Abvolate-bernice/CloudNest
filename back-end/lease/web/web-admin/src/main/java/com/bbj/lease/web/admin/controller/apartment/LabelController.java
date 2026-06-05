package com.bbj.lease.web.admin.controller.apartment;


import com.bbj.lease.common.result.Result;
import com.bbj.lease.model.entity.LabelInfo;
import com.bbj.lease.model.enums.ItemType;
import com.bbj.lease.web.admin.service.LabelInfoService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "标签管理")
@RestController
@RequestMapping("/admin/label")
public class LabelController {
    @Autowired
    private LabelInfoService service;

    @Operation(summary = "（根据类型）查询标签列表")
    @GetMapping("list")

    //请求参数是枚举类型的type  并设定为不是必须的
    //返回值类型是List<LabelInfo>
    public Result<List<LabelInfo>> labelList(@RequestParam(required = false) ItemType type) {
        //指定实现类
        LambdaQueryWrapper<LabelInfo> wrapper = new LambdaQueryWrapper<LabelInfo>().
                eq(type!=null, LabelInfo::getType,type);  //LambdaQueryWapper 必须指定泛型，这样才能确定引用的是哪一张表，  LabelInfo::getType 找到表中的字段， type//传入的要比较的值
        List<LabelInfo> list = service.list(wrapper);
        return Result.ok(list);
    }

    @Operation(summary = "新增或修改标签信息")
    @PostMapping("saveOrUpdate")
    public Result saveOrUpdateLabel(@RequestBody LabelInfo labelInfo) {
        service.saveOrUpdate(labelInfo);

        return Result.ok();
    }

    @Operation(summary = "根据id删除标签信息")
    @DeleteMapping("deleteById")
    public Result deleteLabelById(@RequestParam Long id) {
        service.removeById(id);
        return Result.ok();
    }
}
