package com.scy.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by Shichengyao on 1/15/17.
 */
@Component
public class Qiuniu {
    @Value("${qiniu.bucketname}")
    private String bucketname;

    public String getBucketname() {
        return bucketname;
    }

    public void setBucketname(String bucketname) {
        this.bucketname = bucketname;
    }
}
