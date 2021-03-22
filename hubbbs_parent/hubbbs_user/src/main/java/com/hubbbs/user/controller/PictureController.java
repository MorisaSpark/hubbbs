package com.hubbbs.user.controller;

import com.hubbbs.user.pojo.PictureResult;
import com.hubbbs.user.service.UserService;
import com.hubbbs.user.utils.TencentCloudSaveUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import util.IdWorker;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: HB
 * Date: 2019/4/15
 *
 * @author HB
 * To change this template use File | Settings | File Templates.
 */
@RestController
@CrossOrigin
public class PictureController {

    @Autowired
    private TencentCloudSaveUtil tencentCloudSaveUtil;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private UserService userService;

    /**
     * @return 上传文件
     * @desc 描述
     * @method fileUpload
     * @params [file]
     * @author hubdir
     * @date 2019/4/24 19:21
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public Object fileUpload(@RequestParam("file") MultipartFile file) {
        PictureResult result = new PictureResult();
        ArrayList<String> dataList = new ArrayList<>();
        try {
            //1.上传文件至tx 返回位置
            String uploadPath = tencentCloudSaveUtil.upload(file.getInputStream(), "" + idWorker.nextId() + file.getOriginalFilename());
            dataList.add(uploadPath);
            result.setErrno(0);
            result.setData(dataList);
            return result;
        } catch (IOException e) {
            dataList.add("上传失败");
            result.setErrno(1);
            result.setData(dataList);
            return result;
        }
    }
}
