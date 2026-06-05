package com.bbj.lease.web.admin.custom.converter;

import com.bbj.lease.model.enums.BaseEnum;
import org.jetbrains.annotations.Nullable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.stereotype.Component;

@Component
public class StringToBaseEnumConverterFactory implements ConverterFactory<String, BaseEnum> {


    //自定义转换器，前端传过来的数据自动转换为Service 接收到的数据类型
    @Override
    public <T extends BaseEnum> Converter<String, T> getConverter(Class<T> targetType) {
        return new Converter<String, T>() {
            @Nullable
            @Override
            public T convert(String code) {
                //？？？？
                for (T enumConstant : targetType.getEnumConstants()) {
                    if(enumConstant.getCode().toString().equals(code)){
                        return enumConstant;
                    }

                }
                throw  new IllegalArgumentException("code"+code+"非法");
            }
        };
    }
}
