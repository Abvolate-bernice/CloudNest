package com.bbj.lease.web.admin.service.impl;

import com.bbj.lease.common.exception.RoomIsNotNull;
import com.bbj.lease.common.result.ResultCodeEnum;
import com.bbj.lease.model.entity.*;
import com.bbj.lease.model.enums.ItemType;
import com.atguigu.lease.web.admin.mapper.*;
import com.atguigu.lease.web.admin.service.*;
import com.bbj.lease.web.admin.mapper.*;
import com.bbj.lease.web.admin.service.*;
import com.bbj.lease.web.admin.vo.apartment.ApartmentDetailVo;
import com.bbj.lease.web.admin.vo.apartment.ApartmentItemVo;
import com.bbj.lease.web.admin.vo.apartment.ApartmentQueryVo;
import com.bbj.lease.web.admin.vo.apartment.ApartmentSubmitVo;
import com.bbj.lease.web.admin.vo.fee.FeeValueVo;
import com.bbj.lease.web.admin.vo.graph.GraphVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liubo
 * @description 针对表【apartment_info(公寓信息表)】的数据库操作Service实现
 * @createDate 2023-07-24 15:48:00
 */
@Service
//通用service 要指定Mapper 和实体类
public class ApartmentInfoServiceImpl extends ServiceImpl<ApartmentInfoMapper, ApartmentInfo>
        implements ApartmentInfoService {

    //这里注入service是因为后面可以进行批量增加操作，比Mapper方便一点
    @Autowired
    private GraphInfoService graphInfoService;
    @Autowired
    private ApartmentLabelService apartmentLabelService;
    @Autowired
    private ApartmentFacilityService apartmentFacilityService;
    @Autowired
    private ApartmentFeeValueService apartmentFeeValueService;
    @Autowired
    private LabelInfoService labelInfoService;
    @Autowired
    private ApartmentInfoMapper apartmentInfoMapper;
    @Autowired
    private GraphInfoMapper graphInfoMapper;
    @Autowired
    private  LabelInfoMapper labelInfoMapper;
    @Autowired
    private FacilityInfoMapper facilityInfoMapper;
    @Autowired
    private FeeValueMapper feeValueMapper;
    @Autowired
    private RoomInfoMapper roomInfoMapper;

    @Override
    //保存或更新ApartmentSubmitVo
    public void saveOrUpdateApartment(ApartmentSubmitVo apartmentSubmitVo) {
        Boolean isIdNotNull = apartmentSubmitVo.getId() != null;
        //基础信息更新或插入
        super.saveOrUpdate(apartmentSubmitVo);
        //ID不是空

        if (isIdNotNull) {
            //如果不为空更新操作，那么要先删除

            //1.图片删除  图片ID是要删除公寓的ID，图片属性为公寓
            LambdaQueryWrapper<GraphInfo> GraphInfoQueryWrapper = new LambdaQueryWrapper<GraphInfo>()
                    .eq(GraphInfo::getItemId, apartmentSubmitVo.getId())
                    .eq(GraphInfo::getItemType, ItemType.APARTMENT);
            graphInfoService.remove(GraphInfoQueryWrapper);
            //2.标签删除
            //应该使用标签公寓关联表 ，这里的LabelInfo 知识基础的，没有公寓信息
            LambdaQueryWrapper<ApartmentLabel> labelInfoQueryWrapper = new LambdaQueryWrapper<ApartmentLabel>()
                    .eq(ApartmentLabel::getId, apartmentSubmitVo.getId());
            apartmentLabelService.remove(labelInfoQueryWrapper);

            //3.配套删除
            LambdaQueryWrapper<ApartmentFacility> apartmentFacilityQueryWrapper = new LambdaQueryWrapper<ApartmentFacility>();
            apartmentFacilityQueryWrapper.eq(ApartmentFacility::getApartmentId, apartmentSubmitVo.getId());
            apartmentFacilityService.remove(apartmentFacilityQueryWrapper);

            //4.删除杂费列表
            LambdaQueryWrapper<ApartmentFeeValue> feeQueryWrapper = new LambdaQueryWrapper<>();
            feeQueryWrapper.eq(ApartmentFeeValue::getApartmentId, apartmentSubmitVo.getId());
            apartmentFeeValueService.remove(feeQueryWrapper);
        }

        //ID为空进行增加操作
        //1. 插入新的图片列表
        List<GraphVo> graphVoList = apartmentSubmitVo.getGraphVoList();
        //拿到的实体类集合不为空才进行下面操作
        //传过来的是GraphVo这个实体类 但是批处理需要传入一个GraphInfo实体  少另一个图片所属ID，和类型
        if (!CollectionUtils.isEmpty(graphVoList)) {
            ArrayList<GraphInfo> graphInfoList = new ArrayList<>();
            for (GraphVo graphVo : graphVoList) {
                GraphInfo graphInfo = new GraphInfo();
                graphInfo.setUrl(graphVo.getName());
                graphInfo.setName(graphVo.getName());
                graphInfo.setItemId(apartmentSubmitVo.getId());
                graphInfo.setItemType(ItemType.APARTMENT);
                graphInfoList.add(graphInfo);

            }
            graphInfoService.saveBatch(graphInfoList);
        }


        //2.插入标签
        List<Long> labelIds = apartmentSubmitVo.getLabelIds();
        if (!CollectionUtils.isEmpty(labelIds)) {
            ArrayList<ApartmentLabel> labelInfoList = new ArrayList<ApartmentLabel>();
            for (Long labelId : labelIds) {
                //应该修改的是公寓标签关联表 -----labelInfo 是所有的标签
                ApartmentLabel apartmentLabel = new ApartmentLabel();
                apartmentLabel.setLabelId(labelId);
                apartmentLabel.setApartmentId(apartmentSubmitVo.getId());
                labelInfoList.add(apartmentLabel);
            }

            //需要转换为LabelInfo的List  type、name
            apartmentLabelService.saveBatch(labelInfoList);
        }

        //3. 插入配套
        List<Long> facilityIds = apartmentSubmitVo.getFacilityInfoIds();
        if (!CollectionUtils.isEmpty(facilityIds)) {
            ArrayList<ApartmentFacility> facilityList = new ArrayList<>();
            for (Long facilityId : facilityIds) {
                ApartmentFacility apartmentFacility = new ApartmentFacility();
                apartmentFacility.setApartmentId(apartmentSubmitVo.getId());
                apartmentFacility.setFacilityId(facilityId);
                facilityList.add(apartmentFacility);
            }
            apartmentFacilityService.saveBatch(facilityList);
        }
        //4.插入杂费值

        List<Long> feeValueIds = apartmentSubmitVo.getFeeValueIds();
        if (!CollectionUtils.isEmpty(feeValueIds)) {
            ArrayList<ApartmentFeeValue> apartmentFeeValueList = new ArrayList<>();
            for (Long feeValueId : feeValueIds) {
                ApartmentFeeValue apartmentFeeValue = new ApartmentFeeValue();
                apartmentFeeValue.setApartmentId(apartmentSubmitVo.getId());
                apartmentFeeValue.setFeeValueId(feeValueId);
                apartmentFeeValueList.add(apartmentFeeValue);
            }
            apartmentFeeValueService.saveBatch(apartmentFeeValueList);
        }
    }


    //实现分页查询
    @Override
    public IPage<ApartmentItemVo> pageItem(Page<ApartmentItemVo> page, ApartmentQueryVo queryVo) {

        //使用Mybatis 插件mapper无需关注分页逻辑了，只需要定义查询mapper SQL语句
        IPage<ApartmentItemVo> pageItem = apartmentInfoMapper.pageItem(page, queryVo);

        return pageItem;
    }
//分页查询是根据mapper多表jion查询的，是一个list
// 但是detail查询不用  id传过来是一个公寓
    @Override
    public ApartmentDetailVo getDetailById(Long id) {



        ApartmentInfo apartmentInfo =apartmentInfoMapper.selectById(id);
        //基本信息


        //1.图片
        List<GraphVo> graphVos = graphInfoMapper.selectListByItemTypeAndId(ItemType.APARTMENT,id);

        //2.标签   获得标签列表
        List<LabelInfo> labelInfos = labelInfoMapper.selectLabelListById(id);

        //3.配套
        List<FacilityInfo> facilityInfos = facilityInfoMapper.selectByApartementID(id);
        //4.杂费值
        List<FeeValueVo> feeValueVos = feeValueMapper.selectListById(id);

        //整合 成一个完整对象
        ApartmentDetailVo apartmentDetailVo = new ApartmentDetailVo();
        BeanUtils.copyProperties(apartmentInfo,apartmentDetailVo);
        apartmentDetailVo.setGraphVoList(graphVos);
        apartmentDetailVo.setLabelInfoList(labelInfos);
        apartmentDetailVo.setFacilityInfoList(facilityInfos);
        apartmentDetailVo.setFeeValueVoList(feeValueVos);


        return apartmentDetailVo;
    }



    @Override
    public void deletedByIds(Long id) {

        //获取房间数
        LambdaQueryWrapper<RoomInfo> roomInfoWrapper = new LambdaQueryWrapper<>();
        roomInfoWrapper.eq(RoomInfo::getApartmentId,id);
        Long count = roomInfoMapper.selectCount(roomInfoWrapper);

        if(count >0){
            //房间数量不为0 执行警告信息  执行异常
            throw   new RoomIsNotNull(ResultCodeEnum.DELETE_ERROR);
        }
        super.removeById(id);
        //执行删除操作
        //1.图片删除  图片ID是要删除公寓的ID，图片属性为公寓
        LambdaQueryWrapper<GraphInfo> GraphInfoQueryWrapper = new LambdaQueryWrapper<GraphInfo>()
                .eq(GraphInfo::getItemId, id)
                .eq(GraphInfo::getItemType, ItemType.APARTMENT);
        graphInfoService.remove(GraphInfoQueryWrapper);
        //2.标签删除
        //应该使用标签公寓关联表 ，这里的LabelInfo 知识基础的，没有公寓信息
        LambdaQueryWrapper<ApartmentLabel> labelInfoQueryWrapper = new LambdaQueryWrapper<ApartmentLabel>()
                .eq(ApartmentLabel::getId, id);
        apartmentLabelService.remove(labelInfoQueryWrapper);

        //3.配套删除
        LambdaQueryWrapper<ApartmentFacility> apartmentFacilityQueryWrapper = new LambdaQueryWrapper<ApartmentFacility>();
        apartmentFacilityQueryWrapper.eq(ApartmentFacility::getApartmentId, id);
        apartmentFacilityService.remove(apartmentFacilityQueryWrapper);

        //4.删除杂费列表
        LambdaQueryWrapper<ApartmentFeeValue> feeQueryWrapper = new LambdaQueryWrapper<>();
        feeQueryWrapper.eq(ApartmentFeeValue::getApartmentId, id);
        apartmentFeeValueService.remove(feeQueryWrapper);
    }
}



//    @Autowired
//    private GraphInfoService graphInfoService;
//
//    @Autowired
//    private ApartmentFacilityService facilityService;
//
//    @Autowired
//    private ApartmentLabelService labelService;
//
//    @Autowired
//    private ApartmentFeeValueService feeValueService;
//
//    @Autowired
//    private ApartmentInfoMapper apartmentInfoMapper;
//
//    @Autowired
//    private GraphInfoMapper graphInfoMapper;
//
//    @Autowired
//    private LabelInfoMapper labelInfoMapper;
//
//    @Autowired
//    private FacilityInfoMapper facilityInfoMapper;
//
//    @Autowired
//    private FeeValueMapper feeValueMapper;
//
//    @Autowired
//    private RoomInfoMapper roomInfoMapper;
//
//    @Override
//    public void saveOrUpdateApartment(ApartmentSubmitVo apartmentSubmitVo) {
//        boolean isUpdate = apartmentSubmitVo.getId() != null;
//        super.saveOrUpdate(apartmentSubmitVo);
//
//        if (isUpdate) {
//            //1.删除图片列表
//            LambdaQueryWrapper<GraphInfo> graphQueryWrapper = new LambdaQueryWrapper<>();
//            graphQueryWrapper.eq(GraphInfo::getItemType, ItemType.APARTMENT);
//            graphQueryWrapper.eq(GraphInfo::getItemId, apartmentSubmitVo.getId());
//            graphInfoService.remove(graphQueryWrapper);
//
//            //2.删除配套列表
//            LambdaQueryWrapper<ApartmentFacility> facilityQueryWrapper = new LambdaQueryWrapper<>();
//            facilityQueryWrapper.eq(ApartmentFacility::getApartmentId, apartmentSubmitVo.getId());
//            facilityService.remove(facilityQueryWrapper);
//
//            //3.删除标签列表
//            LambdaQueryWrapper<ApartmentLabel> labelQueryWrapper = new LambdaQueryWrapper<>();
//            labelQueryWrapper.eq(ApartmentLabel::getApartmentId, apartmentSubmitVo.getId());
//            labelService.remove(labelQueryWrapper);
//
//            //4.删除杂费列表
//            LambdaQueryWrapper<ApartmentFeeValue> feeQueryWrapper = new LambdaQueryWrapper<>();
//            feeQueryWrapper.eq(ApartmentFeeValue::getApartmentId, apartmentSubmitVo.getId());
//            feeValueService.remove(feeQueryWrapper);
//        }
//
//        //1.插入新的图片列表
//        List<GraphVo> graphVoList = apartmentSubmitVo.getGraphVoList();
//        if (!CollectionUtils.isEmpty(graphVoList)) {
//            ArrayList<GraphInfo> graphInfoList = new ArrayList<>();
//            for (GraphVo graphVo : graphVoList) {
//                GraphInfo graphInfo = new GraphInfo();
//                graphInfo.setName(graphVo.getName());
//                graphInfo.setUrl(graphVo.getUrl());
//                graphInfo.setItemType(ItemType.APARTMENT);
//                graphInfo.setItemId(apartmentSubmitVo.getId());
//
//                graphInfoList.add(graphInfo);
//            }
//            graphInfoService.saveBatch(graphInfoList);
//        }
//
//        //2.插入新的配套列表
//        List<Long> facilityIds = apartmentSubmitVo.getFacilityInfoIds();
//        if (!CollectionUtils.isEmpty(facilityIds)) {
//            ArrayList<ApartmentFacility> facilityList = new ArrayList<>();
//            for (Long facilityId : facilityIds) {
//                ApartmentFacility apartmentFacility = new ApartmentFacility();
//                apartmentFacility.setApartmentId(apartmentSubmitVo.getId());
//                apartmentFacility.setFacilityId(facilityId);
//                facilityList.add(apartmentFacility);
//            }
//            facilityService.saveBatch(facilityList);
//        }
//
//        //3.插入新的标签列表
//        List<Long> labelIds = apartmentSubmitVo.getLabelIds();
//        if (!CollectionUtils.isEmpty(labelIds)) {
//            List<ApartmentLabel> apartmentLabelList = new ArrayList<>();
//            for (Long labelId : labelIds) {
//                ApartmentLabel apartmentLabel = new ApartmentLabel();
//                apartmentLabel.setApartmentId(apartmentSubmitVo.getId());
//                apartmentLabel.setLabelId(labelId);
//                apartmentLabelList.add(apartmentLabel);
//            }
//            labelService.saveBatch(apartmentLabelList);
//        }
//
//        //4.插入新的杂费列表
//        List<Long> feeValueIds = apartmentSubmitVo.getFeeValueIds();
//        if (!CollectionUtils.isEmpty(feeValueIds)) {
//            ArrayList<ApartmentFeeValue> apartmentFeeValueList = new ArrayList<>();
//            for (Long feeValueId : feeValueIds) {
//                ApartmentFeeValue apartmentFeeValue = new ApartmentFeeValue();
//                apartmentFeeValue.setApartmentId(apartmentSubmitVo.getId());
//                apartmentFeeValue.setFeeValueId(feeValueId);
//                apartmentFeeValueList.add(apartmentFeeValue);
//            }
//            feeValueService.saveBatch(apartmentFeeValueList);
//        }
//
//    }
//
//    @Override
//    public IPage<ApartmentItemVo> pageItem(Page<ApartmentItemVo> page, ApartmentQueryVo queryVo) {
//        return apartmentInfoMapper.pageItem(page, queryVo);
//    }
//
//    @Override
//    public ApartmentDetailVo getDetailById(Long id) {
//        //1.公寓信息
//        ApartmentInfo apartmentInfo = apartmentInfoMapper.selectById(id);
//
//        //2.图片
//        List<GraphVo> graphVoList = graphInfoMapper.selectListByItemTypeAndId(ItemType.APARTMENT, id);
//
//        //3.标签
//        List<LabelInfo> labelInfoList = labelInfoMapper.selectListByApartmentId(id);
//
//        //4.配套
//        List<FacilityInfo> facilityInfoList = facilityInfoMapper.selectListByApartmentId(id);
//
//        //5.杂费
//        List<FeeValueVo> feeValueVoList = feeValueMapper.selectListByApartmentId(id);
//
//        //6.组装结果
//        ApartmentDetailVo apartmentDetailVo = new ApartmentDetailVo();
//        BeanUtils.copyProperties(apartmentInfo, apartmentDetailVo);
//        apartmentDetailVo.setGraphVoList(graphVoList);
//        apartmentDetailVo.setLabelInfoList(labelInfoList);
//        apartmentDetailVo.setFacilityInfoList(facilityInfoList);
//        apartmentDetailVo.setFeeValueVoList(feeValueVoList);
//
//        return apartmentDetailVo;
//
//    }
//
//    @Override
//    public void removeApartmentById(Long id) {
//
//        LambdaQueryWrapper<RoomInfo> roomQueryWrapper = new LambdaQueryWrapper<>();
//        roomQueryWrapper.eq(RoomInfo::getApartmentId, id);
//        Long count = roomInfoMapper.selectCount(roomQueryWrapper);
//
//            //终止删除操作，并且返回提示信息
//        if (count > 0) {
//            throw new LeaseException(ResultCodeEnum.DELETE_ERROR);
//        }
//
//        super.removeById(id);
//
//        //1.删除图片列表
//        LambdaQueryWrapper<GraphInfo> graphQueryWrapper = new LambdaQueryWrapper<>();
//        graphQueryWrapper.eq(GraphInfo::getItemType, ItemType.APARTMENT);
//        graphQueryWrapper.eq(GraphInfo::getItemId, id);
//        graphInfoService.remove(graphQueryWrapper);
//
//        //2.删除配套列表
//        LambdaQueryWrapper<ApartmentFacility> facilityQueryWrapper = new LambdaQueryWrapper<>();
//        facilityQueryWrapper.eq(ApartmentFacility::getApartmentId, id);
//        facilityService.remove(facilityQueryWrapper);
//
//        //3.删除标签列表
//        LambdaQueryWrapper<ApartmentLabel> labelQueryWrapper = new LambdaQueryWrapper<>();
//        labelQueryWrapper.eq(ApartmentLabel::getApartmentId, id);
//        labelService.remove(labelQueryWrapper);
//
//        //4.删除杂费列表
//        LambdaQueryWrapper<ApartmentFeeValue> feeQueryWrapper = new LambdaQueryWrapper<>();
//        feeQueryWrapper.eq(ApartmentFeeValue::getApartmentId, id);
//        feeValueService.remove(feeQueryWrapper);
//    }





