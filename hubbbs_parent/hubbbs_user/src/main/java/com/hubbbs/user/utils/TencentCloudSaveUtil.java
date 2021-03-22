package com.hubbbs.user.utils;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.*;
import com.qcloud.cos.region.Region;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by IntelliJ IDEA.
 * User: HB
 * Date: 2018/12/20
 *
 * @author HB
 * To change this template use File | Settings | File Templates.
 */
@Component
public class TencentCloudSaveUtil {

    private String accessKey = "AKIDoQox2sRq2MRHsEzH0cDZhbGk151ikMga";
    private String secretKey = "rZ3ZqthGbhcKrGRO6ZnriOp1zi6F6lgM";
    private String region_name = "ap-guangzhou";
    private String bucketName = "hub-1258341727";
    private String url_pre = "https://hub-1258341727.cos.ap-guangzhou.myqcloud.com/";
    private String pic_pre = "hubbbs/img/icon";
    // 1 初始化用户身份信息(secretId, secretKey)
    private COSCredentials cred = new BasicCOSCredentials(accessKey, secretKey);
    // 2 设置bucket的区域, COS地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
    // clientConfig中包含了设置region, https(默认http), 超时, 代理等set方法, 使用可参见源码或者接口文档FAQ中说明
    private ClientConfig clientConfig = new ClientConfig(new Region(region_name));

    public static void main(String[] args) throws IOException, InterruptedException {
//        System.out.println(new TencentCloudSaveUtil().upload("D:\\allWorkPlace\\Intellij\\portRules.json"));
        System.out.println(new TencentCloudSaveUtil().downloadToString("hubbbs/body/post_body/01/76/10057601.txt"));
    }

    public boolean upload(String filePath) throws IOException {
        String[] split = filePath.split("/");
        String fileName = split[split.length - 1];
        return upload(filePath, fileName);
    }

    public boolean upload(String filePath, String fileName) throws IOException {


        // 3 生成cos客户端
        COSClient cosClient = new COSClient(cred, clientConfig);
        // bucket的命名规则为{name}-{appid} ，此处填写的存储桶名称必须为此格式
        // 简单文件上传, 最大支持 5 GB, 适用于小文件上传, 建议 20M以下的文件使用该接口
        // 大文件上传请参照 API 文档高级 API 上传
        File localFile = new File(filePath);
        // 指定要上传到 COS 上对象键
        // 对象键（Key）是对象在存储桶中的唯一标识。例如，在对象的访问域名 `bucket1-1250000000.cos.ap-guangzhou.myqcloud.com/doc1/pic1.jpg` 中，对象键为 doc1/pic1.jpg, 详情参考 [对象键](https://cloud.tencent.com/document/product/436/13324)
        /*
         * 方法一
         * */
//        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName, localFile);

        /*
         * 方法二
         * */
        FileInputStream inputStream = new FileInputStream(localFile);
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(inputStream.available());
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName, inputStream, objectMetadata);

        PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);

        inputStream.close();
        // 关闭客户端(关闭后台线程)
        cosClient.shutdown();
        return true;
    }

    public String upload(InputStream inputStream, String fileName) throws IOException {

        // 3 生成cos客户端
        COSClient cosClient = new COSClient(cred, clientConfig);
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(inputStream.available());
        fileName = pic_pre + fileName;
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName, inputStream, objectMetadata);

        PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);

        inputStream.close();
        // 关闭客户端(关闭后台线程)
        cosClient.shutdown();
        return (url_pre + fileName);
    }

    public boolean delete(String fileName) {

        // 3 生成cos客户端

        COSClient cosClient = new COSClient(cred, clientConfig);
        cosClient.deleteObject(bucketName, fileName);

        // 关闭客户端(关闭后台线程)
        cosClient.shutdown();
        return true;
    }

    public boolean download(String fileName) {
        return download(fileName, fileName);
    }

    public boolean download(String fileName, String filePath) {

        // 3 生成cos客户端
        COSClient cosClient = new COSClient(cred, clientConfig);
        // 指定要下载到的本地路径
        File downFile = new File(filePath);
        // 指定要下载的文件所在的 bucket 和对象键
        GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, fileName);
        ObjectMetadata downObjectMeta = cosClient.getObject(getObjectRequest, downFile);
        // 关闭客户端(关闭后台线程)
        cosClient.shutdown();
        return true;
    }

    public String downloadToString(String fileName) throws InterruptedException, IOException {
        // 3 生成cos客户端
        COSClient cosClient = new COSClient(cred, clientConfig);
        GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, fileName);
        COSObject cosObject = cosClient.getObject(getObjectRequest);
        COSObjectInputStream cosObjectInput = cosObject.getObjectContent();

        byte[] bytes = new byte[1024];
        int len = 0;
        StringBuilder builder = new StringBuilder();
        while ((len = cosObjectInput.read(bytes)) > 0) {
            builder.append(new String(bytes, 0, len));
        }
        return builder.toString();
    }
}
