package com.nx.manager.impl;

import com.nx.api.service.ItemService;
import com.nx.manager.service.ItemManagerService;
import com.nx.pojo.EasyUIDatagrid;
import com.nx.pojo.TbItem;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author by 张益豪
 * @Classname ItemManagerServiceImpl
 * @Description TODO
 * @Date 2021/7/11 17:20
 */
@Service
public class ItemManagerServiceImpl implements ItemManagerService {
    @DubboReference
    private ItemService itemService;
    @Override
    public EasyUIDatagrid getItemsByPage(int page, int rows) {
        List<TbItem> tbItems = itemService.selectItemByPage(page, rows);
        Long aLong = itemService.countItem();
        EasyUIDatagrid easyUIDatagrid = new EasyUIDatagrid();
        easyUIDatagrid.setTotal(aLong);
        easyUIDatagrid.setRows(tbItems);
        return easyUIDatagrid;
    }
}
