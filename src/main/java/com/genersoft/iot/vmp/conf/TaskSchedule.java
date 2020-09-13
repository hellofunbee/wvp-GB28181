package com.genersoft.iot.vmp.conf;


import com.genersoft.iot.vmp.common.VideoManagerConstants;
import com.genersoft.iot.vmp.gb28181.bean.Device;
import com.genersoft.iot.vmp.utils.redis.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;


@Component
@EnableScheduling
public class TaskSchedule {

    @Autowired
    private RedisUtil redis;

    //每三个小时清空一下设备缓存数据
    @Scheduled(cron = "0 0 0 * * ?")
    public void removeAllDeVice(){
        List<Object> deviceIdList = redis.keys(VideoManagerConstants.CACHEKEY_PREFIX+"*");
        for (int i = 0; i < deviceIdList.size(); i++) {
            redis.remove((String)deviceIdList.get(i));
            System.out.println("移除设备：" + deviceIdList.get(i));
        }
    }

}
