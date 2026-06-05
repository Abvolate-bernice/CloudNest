package com.bbj.lease.common.minio;

import kotlin.reflect.KProperty1;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix = "minio")
public class MinioProperties {
    //驼峰映射自动映射属性名必须和yml中映射后的一样
    private String endpoint;

    private String accessKey;

    private String secretKey;

    private String bucketName;

    //minio:
    //  endpoint: http://192.168.14.101:9000
    //  access-key: minioadmin
    //  secret-key: minioadmin
    //  bucket-name: lease

}
