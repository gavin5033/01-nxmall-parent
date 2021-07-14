package com.nx.provider.service.util;

import com.nx.api.service.ItemCatService;
import com.nx.api.service.ItemCatServiceUtil;
import com.nx.pojo.TbItemCat;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@DubboService(timeout = 50000)
public class ItemCatServiceImplUtil implements ItemCatServiceUtil {
    @Autowired
    private ItemCatService itemCatService;


    //对当前节点的子节点进行赋值
    public List<TbItemCat> selectItemCatByPid(Long pid) {
        List<TbItemCat> catList = itemCatService.selectItemCatByPid(pid);
        for (TbItemCat tbItemCat : catList) {
            if(tbItemCat.getIsParent()){
                tbItemCat.setChildren(selectItemCatByPid(tbItemCat.getId()));
            }
        }

        return catList;
        
    }
}
