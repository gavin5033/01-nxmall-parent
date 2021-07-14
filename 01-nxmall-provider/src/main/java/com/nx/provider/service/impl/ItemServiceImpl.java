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

import java.util.Date;
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

    //显示当前页面商品详情
    @Override
    public List<TbItem> selectItemsByPage(int page, int rows) {
        //1.开启分页插件
        PageHelper.startPage(page,rows);
        //2.查询商品数据
        List<TbItem> items = tbItemMapper.selectByExample(new TbItemExample());
        PageInfo<TbItem> pageInfo = new PageInfo<>(items);

        return pageInfo.getList();
    }

    //查询商品总记录数
    @Override
    public Long countItem() {

        return tbItemMapper.countByExample(new TbItemExample());
    }

    //根据商品id修改商品状态
    @Override
    public int updateItemStatusById(Long id,String status) {
        TbItem tbItem = new TbItem();
        Date date = new Date();
        tbItem.setId(id);
        if(status.equals("reshelf")){
            tbItem.setStatus((byte)1);
        }else if(status.equals("instock")){
            tbItem.setStatus((byte)2);
        }else{
            tbItem.setStatus((byte)3);
        }
        tbItem.setUpdated(date);

        return tbItemMapper.updateByPrimaryKeySelective(tbItem);
    }


    //新增
    @Override
    public int insertItem(TbItem tbItem) {

        return tbItemMapper.insertSelective(tbItem);
    }


    //查询所有状态正常的商品
    @Override
    public List<TbItem> selectAllItems() {
        TbItemExample example = new TbItemExample();
        example.createCriteria().andStatusEqualTo((byte)1);

        return tbItemMapper.selectByExample(example);
    }


    //根据商品id查询对应的商品详情
    @Override
    public TbItem selectItemById(Long id) {
        TbItemExample example = new TbItemExample();
        example.createCriteria().andStatusEqualTo((byte)1).andIdEqualTo(id);

        List<TbItem> tbItems = tbItemMapper.selectByExample(example);
        return tbItems!=null&&tbItems.size()>0?tbItems.get(0):null;
    }


    //根据商品id修改商品库存
    @Override
    public int updateNumById(Long id, int num,Date date) {
        TbItem tbItem = tbItemMapper.selectByPrimaryKey(id);
        tbItem.setNum(tbItem.getNum() - num);
        tbItem.setUpdated(date);
        return tbItemMapper.updateByPrimaryKeySelective(tbItem);
    }
}
