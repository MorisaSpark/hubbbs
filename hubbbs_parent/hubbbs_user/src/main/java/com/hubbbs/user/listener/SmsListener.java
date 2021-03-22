package com.hubbbs.user.listener;


import com.hubbbs.user.utils.IndustrySMS;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: HB
 * Date: 2018/12/3
 *
 * @author HB
 * To change this template use File | Settings | File Templates.
 */
@RabbitListener(queues = "sms")
@Component
public class SmsListener {

    @Autowired
    private IndustrySMS industrySMS;

    @RabbitHandler
    public void getMsg(HashMap message) {
        String mobile = (String) message.get("mobile");
        System.out.println("手机号===" + mobile);
        String checkCode = (String) message.get("checkCode");
        System.out.println("验证码===" + checkCode);
//        String context = "【图从科技】您的短信验证码为" + checkCode + ",请在5分钟内使用。";
        String context = "【图从科技】尊敬的用户，您的验证码为" + checkCode;

        industrySMS.execute(mobile, checkCode, context);
    }
}



