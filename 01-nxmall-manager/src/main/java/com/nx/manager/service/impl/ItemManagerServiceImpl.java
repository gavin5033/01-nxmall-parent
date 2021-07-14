package com.nx.manager.service.impl;

import com.nx.api.service.ItemDescService;
import com.nx.api.service.ItemParamItemService;
import com.nx.api.service.ItemService;
import com.nx.manager.service.ItemManagerService;
import com.nx.pojo.*;
import com.nx.utils.FastDFSUtil;
import com.nx.utils.IDUtil;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author by 张益豪
 * @Classname ItemManagerServiceImpl
 * @Description TODO
 * @Date 2021/7/11 17:20
 */
@Service
public class ItemManagerServiceImpl implements ItemManagerService {
    @Value("${custom.fastdfs.nginx}")
    private String nginxUrl;

    @Value("")
    private String sorlUrl;

    @DubboReference
    private ItemService itemService;
    @DubboReference
    private ItemDescService itemDescService;
    @DubboReference
    private ItemParamItemService itemParamItemService;
    //显示商品数据并分页
    @Override
    public EasyUIDatagrid selectAllItemsByPage(int page, int rows) {
        List<TbItem> tbItems = itemService.selectItemsByPage(page, rows);
        Long count = itemService.countItem();

        EasyUIDatagrid datagrid = new EasyUIDatagrid();
        datagrid.setTotal(count);
        datagrid.setRows(tbItems);

        return datagrid;
    }


    //根据商品id修改商品状态
    @Override
    @Transactional(rollbackFor = Exception.class)
    public NxmallResult updateStatusById(String ids, String status) {
        NxmallResult er = new NxmallResult();
        String[] idsStr = ids.split(",");
        int index = 0;
        for (String idStr : idsStr) {
            long id = Long.parseLong(idStr);
            index += itemService.updateItemStatusById(id,status);
        }
        if(index == idsStr.length){
            er.setStatus(200);
        }
        return er;
    }


    //文件上传
    @Override
    public Map<String, Object> fileUpload(MultipartFile imgFile) {
        Map<String, Object> map = new HashMap<>();
        try {
            String filename = imgFile.getOriginalFilename();
            String[] results = FastDFSUtil.uploadFile(imgFile.getInputStream(), filename);
            String url = nginxUrl+results[0]+"/"+results[1];
            map.put("error",0);
            map.put("url",url);
        } catch (IOException e) {
            e.printStackTrace();
            map.put("error",1);
            map.put("message","上传失败！");
        }
        return map;
    }


    //新增 TODO 分布式事务，保证三个rpc调用在同一个事务内
    @Override
    @Transactional(rollbackFor = Exception.class)
    public NxmallResult insertItem(TbItem tbItem, String desc, String itemParams) {
        NxmallResult er = new NxmallResult();
        Date date = new Date();
        long itemId = IDUtil.nextId(); //自增？？ 保持唯一

        //新增商品
        tbItem.setId(itemId);
        tbItem.setCreated(date);
        tbItem.setUpdated(date);
        tbItem.setStatus((byte)1);
        int index = 0;
        index += itemService.insertItem(tbItem);
        if(index>0){
            //新增商品描述
            TbItemDesc tbItemDesc = new TbItemDesc();
            tbItemDesc.setItemId(itemId);
            tbItemDesc.setItemDesc(desc);
            tbItemDesc.setCreated(date);
            tbItemDesc.setUpdated(date);
            index += itemDescService.insertItemDesc(tbItemDesc);

            //新增商品规格参数详情表 前台配合
            TbItemParamItem tbItemParamItem = new TbItemParamItem();
            tbItemParamItem.setItemId(itemId);
            tbItemParamItem.setCreated(date);
            tbItemParamItem.setUpdated(date);
            tbItemParamItem.setParamData(itemParams);
            index += itemParamItemService.insertItemParamItem(tbItemParamItem);

            if(index ==3){
                er.setStatus(200);
            }
        }else {
            throw  new RuntimeException("哈哈哈哈，假事务");
        }


        //跟新索引库用MQ
//        HttpClientUtil.doGet(sorlUrl+itemId);
        //TODO 将itemId入MQ，在search工程中监听MQ，逻辑为ItemSearchServiceImpl.storeSolrIndex
        return er;
    }
}
