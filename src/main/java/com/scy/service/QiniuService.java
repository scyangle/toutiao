package com.scy.service;

import com.alibaba.fastjson.JSONObject;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.scy.utils.ToutiaoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;


@Service
public class QiniuService implements InitializingBean{
    private static final Logger logger = LoggerFactory.getLogger(QiniuService.class);
    //设置好账号的ACCESS_KEY和SECRET_KEY
    @Value("${qiniu.AccessKey}")
    private  String ACCESS_KEY;
    @Value("${qiniu.SecretKey}")
    private  String SECRET_KEY;
    //要上传的空间
    @Value("${qiniu.bucketname}")
    private  String bucketname;
    @Value("${qiniu.imageDomain}")
    private  String QINIU_IMAGE_DOMAIN;
    //密钥配置
    Auth auth =null;
    //第二种方式: 自动识别要上传的空间(bucket)的存储区域是华东、华北、华南。
    Zone z =null;
    Configuration c=null;
    //创建上传对象
    UploadManager uploadManager=null;
    //简单上传，使用默认策略，只需要设置上传的空间名就可以了
    public String getUpToken() {
        return auth.uploadToken(bucketname);
    }

    public String saveImage(MultipartFile file) {
        try {
            int dotPos = file.getOriginalFilename().lastIndexOf(".");
            if (dotPos < 0) {
                return null;
            }
            String fileExt = file.getOriginalFilename().substring(dotPos + 1);
            if (!ToutiaoUtils.isFileAllowed(fileExt)) {
                return null;
            }
            String fileName = UUID.randomUUID().toString().replaceAll("-", "") + fileExt;

            Response res = uploadManager.put(file.getBytes(), fileName, getUpToken());
            if (res.isOK() && res.isJson()) {
                return QINIU_IMAGE_DOMAIN + JSONObject.parseObject(res.bodyString()).getString("key");
            }else{
                logger.error("七牛云异常:" + res.bodyString());
                return null;
            }
        } catch (IOException e) {
            logger.info("七牛云上传失败",e);
            return null;
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        z = Zone.autoZone();
        c = new Configuration(z);
        uploadManager = new UploadManager(c);
    }
}

