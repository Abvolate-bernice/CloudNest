package com.bbj.lease.web.app.service;

import com.bbj.lease.model.entity.RoomInfo;
import com.bbj.lease.web.app.vo.room.RoomDetailVo;
import com.bbj.lease.web.app.vo.room.RoomItemVo;
import com.bbj.lease.web.app.vo.room.RoomQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author liubo
* @description 针对表【room_info(房间信息表)】的数据库操作Service
* @createDate 2023-07-26 11:12:39
*/
public interface RoomInfoService extends IService<RoomInfo> {
    IPage<RoomItemVo> pageItem(Page<RoomItemVo> page, RoomQueryVo queryVo);

    IPage<RoomItemVo> pageItemByApartmentId(Page<RoomItemVo> page, Long id);

    RoomDetailVo getDetailById(Long id);

}
