package com.hubbbs.user.utils;

import com.alibaba.fastjson.JSON;
import com.hubbbs.user.utils.common.Config;
import com.hubbbs.user.utils.common.HttpUtil;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: HB
 * Date: 2018/6/28
 *
 * @author HB
 * To change this template use File | Settings | File Templates.
 */
@Component
public class IndustrySMS {

    private String operation = "/industrySMS/sendSMS";

    private String accountSid = Config.ACCOUNT_SID;
    private String to;
    private String smsContent;

    /**
     * 验证码通知短信
     */
    public Map execute(String to) {
        int v = (int) ((Math.random() * 9 + 1) * 100000);
        this.to = to;
        this.smsContent = "【图从科技】尊敬的用户，您的验证码为" + v;


        System.out.println("smsContent==" + smsContent);
        String tmpSmsContent = null;
        try {
            tmpSmsContent = URLEncoder.encode(smsContent, "UTF-8");
        } catch (Exception e) {

        }
        String url = Config.BASE_URL + operation;
        String body = "accountSid=" + accountSid + "&to=" + to + "&smsContent=" + tmpSmsContent
                + HttpUtil.createCommonParam();

        // 提交请求
        String post = HttpUtil.post(url, body);

        /*
        * 返回map
         格式
         {
             "respDesc":"成功",
             "smsId":"913945fec0204b1e94baa75a5c013f59",
             "failCount":"1",
             "failList":[{
                 "respDesc":"匹配到黑名单",
                 "phone":"13896543210",
                 "respCode":"00111"
             }],
             "respCode":"00000"
         }
         * */
        Map parse = (Map) JSON.parse(post);
        parse.put("tel", to);
        parse.put("checkCode", v);
        return parse;
//        return "1";
    }

    /**
     * 验证码通知短信
     */
    public Map execute(String mobile, String checkCode, String context) {
        this.to = mobile;
        this.smsContent = context;


        System.out.println("smsContent==" + smsContent);
        String tmpSmsContent = null;
        try {
            tmpSmsContent = URLEncoder.encode(smsContent, "UTF-8");
        } catch (Exception e) {

        }
        String url = Config.BASE_URL + operation;
        String body = "accountSid=" + accountSid + "&to=" + to + "&smsContent=" + tmpSmsContent
                + HttpUtil.createCommonParam();

        // 提交请求
        String post = HttpUtil.post(url, body);

        /*
        * 返回map
         格式
         {
             "respDesc":"成功",
             "smsId":"913945fec0204b1e94baa75a5c013f59",
             "failCount":"1",
             "failList":[{
                 "respDesc":"匹配到黑名单",
                 "phone":"13896543210",
                 "respCode":"00111"
             }],
             "respCode":"00000"
         }
         * */
        Map parse = (Map) JSON.parse(post);
        parse.put("mobile", to);
        parse.put("checkCode", checkCode);
        return parse;
    }

}
