package com.nx.manager.service;

import com.nx.pojo.EasyUIDatagrid;
import com.nx.pojo.NxmallResult;
import com.nx.pojo.TbItem;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @author by 张益豪
 * @Classname ItemManagerService
 * @Description TODO
 * @Date 2021/7/11 17:18
 */
public interface ItemManagerService {
    //显示商品数据并分页
    EasyUIDatagrid selectAllItemsByPage(int page,int rows);

    //根据商品id修改商品状态
    NxmallResult updateStatusById(String ids, String status);

    //文件上传
    Map<String,Object> fileUpload(MultipartFile imgFile);

    //新增
    NxmallResult insertItem(TbItem tbItem, String desc, String itemParams);
}
