package com.nx.manager.service.impl;/*
 @Author sunjiuxiang
 @Package com.nx.manage.service.impl
 @CreateTime 2020/8/19
*/

import com.nx.api.service.ContentCatService;
import com.nx.manager.service.ContentCatManageService;
import com.nx.pojo.EasyUITree;
import com.nx.pojo.NxmallResult;
import com.nx.pojo.TbContentCategory;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ContentCatManageServiceImpl implements ContentCatManageService {
    @DubboReference
    private ContentCatService contentCatService;

    //根据父节点id查询所有子节点
    @Override
    public List<EasyUITree> selectCatsByPid(Long pid) {
        List<TbContentCategory> list = contentCatService.selectContentCatsByPid(pid);
        List<EasyUITree> trees = new ArrayList<>();
        for (TbContentCategory contentCategory : list) {
            EasyUITree tree = new EasyUITree();
            tree.setId(contentCategory.getId());
            tree.setText(contentCategory.getName());
            tree.setState(contentCategory.getIsParent()==true?"closed":"open");

            trees.add(tree);
        }
        return trees;
    }


    //新增
    @Override
    @Transactional(rollbackFor = Exception.class)
    public NxmallResult insertContentCat(TbContentCategory tbContentCategory) {
        NxmallResult er = new NxmallResult();
        //1.查询，同一父节点下不能有相同名称其他子节点
        List<TbContentCategory> categoryList = contentCatService.selectContentCatsByPid(tbContentCategory.getParentId());
        for (TbContentCategory contentCategory : categoryList) {
            if(contentCategory.getName().equals(tbContentCategory.getName())){
                er.setStatus(400);
                return er;

            }
        }

        //2.新增
        Date date = new Date();
        tbContentCategory.setCreated(date);
        tbContentCategory.setUpdated(date);
        tbContentCategory.setIsParent(false);
        tbContentCategory.setSortOrder(1);
        tbContentCategory.setStatus(1);
        Long id = contentCatService.insertContentCat(tbContentCategory);
        tbContentCategory.setId(id);


        if(id != null){
            //3.修改
            //查询当前父节点详情
            TbContentCategory parentCat = contentCatService.selectContentCatById(tbContentCategory.getParentId());
            //如果当前节点不是父节点，则修改isParent为true,否则不用修改
            if(!parentCat.getIsParent()){
                //修改isParent为true
                parentCat.setId(tbContentCategory.getParentId());
                parentCat.setIsParent(true);
                parentCat.setUpdated(date);
                int result = contentCatService.updateContentCat(parentCat);
                if(result >0){
                    er.setStatus(200);

                }
            }
            er.setData(tbContentCategory);
            er.setStatus(200);

        }
        return er;
    }


    //重命名
    @Override
    public NxmallResult updateContentCat(TbContentCategory tbContentCategory) {
        NxmallResult er = new NxmallResult();

        //1.查询：根据当前要重命名的节点id查询对应的内容分类详情
        TbContentCategory currentContent = contentCatService.selectContentCatById(tbContentCategory.getId());
        //2.查询：根据当前节点的pid以及当前修改的名字进行查询，查看父节点下是否有相同名字的节点
        TbContentCategory otherContent = new TbContentCategory();
        otherContent.setParentId(currentContent.getParentId());
        otherContent.setName(tbContentCategory.getName());
        List<TbContentCategory> categoryList = contentCatService.selectContentCatByCondition(otherContent);

        //如果当前父节点下有其他同名的子节点
        if(categoryList != null && categoryList.size()>0){
            er.setStatus(400);
        }else{
            //3.修改
            Date date = new Date();
            tbContentCategory.setUpdated(date);
            int index = contentCatService.updateContentCat(tbContentCategory);
            if(index >0){
                er.setStatus(200);
            }
        }


        return er;
    }


    //删除
    @Override
    @Transactional(rollbackFor = Exception.class)
    public NxmallResult deleteContentCat(TbContentCategory tbContentCategory) {
        NxmallResult er = new NxmallResult();
        Date date = new Date();
        //1.修改：根据页面传过来的id修改对应的对象的status为2
        tbContentCategory.setStatus(2);
        tbContentCategory.setUpdated(date);
        int index = contentCatService.updateContentCat(tbContentCategory);
        if(index >0){
            //查询当前节点的详情
            TbContentCategory category = contentCatService.selectContentCatById(tbContentCategory.getId());

            //2.查询：根据当前节点的pid查询当前父节点下是否还有status为1的其他子节点
            TbContentCategory otherContent = new TbContentCategory();
            otherContent.setParentId(category.getParentId());
            otherContent.setStatus(1);
            List<TbContentCategory> list = contentCatService.selectContentCatByPidAndStatus(otherContent);
            //3.修改父节点isParent为false
            if(list ==null || list.size()==0){
                TbContentCategory parent = new TbContentCategory();
                parent.setId(category.getParentId());
                parent.setIsParent(false);
                parent.setUpdated(date);
                int result = contentCatService.updateContentCat(parent);
                if(result >0){
                    er.setStatus(200);
                }

            }
            er.setStatus(200);
        }
        return er;
    }
}
