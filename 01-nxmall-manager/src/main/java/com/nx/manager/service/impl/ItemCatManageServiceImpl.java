package com.nx.manager.service.impl;/*
 @Author sunjiuxiang
 @Package com.nx.manage.service.impl
 @CreateTime 2020/8/18
*/

import com.nx.api.service.ItemCatService;
import com.nx.manager.service.ItemCatManageService;
import com.nx.pojo.EasyUIDatagrid;
import com.nx.pojo.EasyUITree;
import com.nx.pojo.TbItemCat;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemCatManageServiceImpl implements ItemCatManageService {
    @DubboReference
    private ItemCatService itemCatService;
    //根据父节点id查询对应的所有子节点
    @Override
    public List<EasyUITree> selectItemCatByPid(Long pid) {
        List<TbItemCat> tbItemCats = itemCatService.selectItemCatByPid(pid);
        List<EasyUITree> trees = new ArrayList<>();

        for (TbItemCat tbItemCat : tbItemCats) {
            EasyUITree tree = new EasyUITree();
            tree.setId(tbItemCat.getId());
            tree.setText(tbItemCat.getName());
            tree.setState(tbItemCat.getIsParent()==true?"closed":"open");

            trees.add(tree);

        }
        return trees;
    }

    @Override
    public EasyUIDatagrid findItemCatByPid(Long pid) {
        EasyUIDatagrid datagrid = new EasyUIDatagrid();
        List<TbItemCat> tbItemCats = itemCatService.selectItemCatByPid(pid);
        Long count = itemCatService.count(pid);
        datagrid.setRows(tbItemCats);
        datagrid.setTotal(count);
        return datagrid;
    }
}
