package com.bbj.lease.web.admin.service.impl;

import com.bbj.lease.common.minio.MinioProperties;
import com.bbj.lease.web.admin.service.FileService;
import io.minio.*;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private MinioClient minioClient;

    @Autowired
    private MinioProperties properties;

    @Override
    public String upload(MultipartFile file) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {

        //1.配置好了Minio，可以注入MinioClient客户端直接使用
            //先判断桶是否存在，不存在创建桶
            boolean bucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(properties.getBucketName()).build());
            if (!bucketExists) {

                //桶不存在，创建桶
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(properties.getBucketName()).build());
                //设置桶的权限——所有人可读所有桶中的内容
                minioClient.setBucketPolicy(SetBucketPolicyArgs.builder().  //给哪个桶设置，config设置什么内容（JSON）
                        bucket(properties.getBucketName())
                        .config(createBucketPolicyConfig(properties.getBucketName())).build());
            }
            String filename = new SimpleDateFormat("yyyyMMdd").format(new Date()) + "/" + UUID.randomUUID() + "-" + file.getOriginalFilename();
            minioClient.putObject(PutObjectArgs.builder().contentType(file.getContentType())  //上传文件必须指定  上传到哪一个桶 --》文件名  --》输入流
                    .bucket(properties.getBucketName())
                    .object(filename)
                    .contentType(file.getContentType())   //设置Meta元数据的ContentType 保持和原来一样，否则的话回事一个Stream 类型浏览器访问URL会直接下载而不是直接展示
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .build());
            return String.join("/", properties.getEndpoint(), properties.getBucketName(), filename);
    }


//        try {
//            boolean bucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(properties.getBucketName()).build());
//            if (!bucketExists){
//                minioClient.makeBucket(MakeBucketArgs.builder().bucket(properties.getBucketName()).build());
//                minioClient.setBucketPolicy(SetBucketPolicyArgs.builder().bucket(properties.getBucketName()).config(createBucketPolicyConfig(properties.getBucketName())).build());
//            }
//
//            String filename = new SimpleDateFormat("yyyyMMdd").format(new Date()) + "/" + UUID.randomUUID() + "-" + file.getOriginalFilename();
//            minioClient.putObject(PutObjectArgs.builder().contentType(file.getContentType()).bucket(properties.getBucketName()).object(filename).stream(file.getInputStream(),file.getSize(),-1).build());
//
//            return String.join("/", properties.getEndpoint(),properties.getBucketName(),filename);
//
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
private String createBucketPolicyConfig(String bucketName) {

    return """
            {
              "Statement" : [ {
                "Action" : "s3:GetObject",
                "Effect" : "Allow",
                "Principal" : "*",
                "Resource" : "arn:aws:s3:::%s/*"
              } ],
              "Version" : "2012-10-17"
            }
            """.formatted(bucketName);

    }
}



