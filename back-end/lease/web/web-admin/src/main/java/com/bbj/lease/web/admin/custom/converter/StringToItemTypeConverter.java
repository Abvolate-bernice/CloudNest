package com.bbj.lease.web.admin.custom.converter;

import com.bbj.lease.model.enums.ItemType;
import org.jetbrains.annotations.Nullable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToItemTypeConverter implements Converter<String,ItemType> {

    @Nullable
    @Override
    public ItemType convert(String code) {
        for (ItemType value : ItemType.values()) {
            if(value.getCode().toString().equals(code)){
                return value;
            }
        }
        throw new IllegalArgumentException("code"+code+"非法");
    }
}

