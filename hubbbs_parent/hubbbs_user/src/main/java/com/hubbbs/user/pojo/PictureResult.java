package com.hubbbs.user.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: HB
 * Date: 2019/4/24
 *
 * @author HB
 * To change this template use File | Settings | File Templates.
 */
@Data
@SuppressWarnings("unused")
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class PictureResult {
    private Integer errno;
    private List data;
    //{
    //    // errno 即错误代码，0 表示没有错误。
    //    //       如果有错误，errno != 0，可通过下文中的监听函数 fail 拿到该错误码进行自定义处理
    //    "errno": 0,
    //
    //    // data 是一个数组，返回若干图片的线上地址
    //    "data": [
    //        "图片1地址",
    //        "图片2地址",
    //        "……"
    //    ]
    //}
}
