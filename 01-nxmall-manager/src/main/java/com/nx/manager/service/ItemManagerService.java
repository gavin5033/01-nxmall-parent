package com.nx.manager.service;

import com.nx.pojo.EasyUIDatagrid;

/**
 * @author by 张益豪
 * @Classname ItemManagerService
 * @Description TODO
 * @Date 2021/7/11 17:18
 */
public interface ItemManagerService {
    EasyUIDatagrid getItemsByPage(int page,int rows);
}
