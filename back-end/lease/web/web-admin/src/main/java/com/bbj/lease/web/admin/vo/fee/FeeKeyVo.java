package com.bbj.lease.web.admin.vo.fee;

import com.bbj.lease.model.entity.FeeKey;
import com.bbj.lease.model.entity.FeeValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;


@Data
public class FeeKeyVo extends FeeKey {
//FeeKeyVo  名称、杂费值列表、ID
    @Schema(description = "杂费value列表")
    private List<FeeValue> feeValueList;
}
