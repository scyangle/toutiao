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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by Shichengyao on 1/10/17.
 */
@Service
public class QiniuService {
    private static final Logger logger = LoggerFactory.getLogger(QiniuService.class);
    //设置好账号的ACCESS_KEY和SECRET_KEY
    String ACCESS_KEY="Gv9AxSxtA85iMTkOw98k16WBMz7HvSflqBQAw_8h";
    String SECRET_KEY="fZ2KAqUZaNewiwx34J4u5_B5ZThw8kDvh_YyHrmK";
    //要上传的空间
    String bucketname = "image";
    private static String QINIU_IMAGE_DOMAIN = "http://ojkbh6krj.bkt.clouddn.com/";
    //密钥配置
    Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
    //第二种方式: 自动识别要上传的空间(bucket)的存储区域是华东、华北、华南。
    Zone z = Zone.autoZone();
    Configuration c = new Configuration(z);

    //创建上传对象
    UploadManager uploadManager = new UploadManager(c);

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

//    public void upload() throws IOException {
//        try {
//            //调用put方法上传
//            Response res = uploadManager.put(FilePath, key, getUpToken());
//            //打印返回的信息
//            System.out.println(res.bodyString());
//        } catch (QiniuException e) {
//            Response r = e.response;
//            // 请求失败时打印的异常的信息
//            System.out.println(r.toString());
//            try {
//                //响应的文本信息
//                System.out.println(r.bodyString());
//            } catch (QiniuException e1) {
//                //ignore
//            }
//        }
//    }
}

