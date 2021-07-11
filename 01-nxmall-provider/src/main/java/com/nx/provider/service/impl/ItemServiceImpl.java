package com.nx.provider.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nx.api.service.ItemService;
import com.nx.mapper.TbItemMapper;
import com.nx.pojo.TbItem;
import com.nx.pojo.TbItemExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.dubbo.config.annotation.*;

import java.util.List;

/**
 * @author by 张益豪
 * @Classname ItemServiceImpl
 * @Description TODO
 * @Date 2021/7/11 16:12
 */
@DubboService
public class ItemServiceImpl implements ItemService {
    @Autowired
    private TbItemMapper tbItemMapper;
    @Override
    public List<TbItem> selectItemByPage(int page, int rows) {
        PageHelper.startPage(page, rows);
        List<TbItem> tbItems = tbItemMapper.selectByExample(new TbItemExample());
        PageInfo<TbItem> pageInfo = new PageInfo<>(tbItems);
        return  pageInfo.getList();
    }

    @Override
    public Long countItem() {
        return tbItemMapper.countByExample(new TbItemExample());
    }
}
