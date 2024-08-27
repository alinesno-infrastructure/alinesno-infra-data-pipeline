package com.alinesno.infra.data.pipeline.service.impl;

import com.alinesno.infra.common.core.service.impl.IBaseServiceImpl;
import com.alinesno.infra.common.core.utils.StringUtils;
import com.alinesno.infra.data.pipeline.api.TreeSelectDto;
import com.alinesno.infra.data.pipeline.entity.JobCatalogEntity;
import com.alinesno.infra.data.pipeline.mapper.JobCatalogMapper;
import com.alinesno.infra.data.pipeline.service.IJobCatalogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Prompt指令类型Service业务层处理
 *
 * @author luoxiaodong
 * @version 1.0.0
 */
@Slf4j
@Service
public class JobCatalogServiceImpl extends IBaseServiceImpl<JobCatalogEntity, JobCatalogMapper> implements IJobCatalogService {

    @Override
    public List<JobCatalogEntity> selectCatalogList(JobCatalogEntity promptCatalog) {

        List<JobCatalogEntity> list = list() ;

        if(list == null || list.isEmpty()){

            list = new ArrayList<>() ;

            // 默认有一个选项是父类
            JobCatalogEntity parent = new JobCatalogEntity() ;
            parent.setName("父类对象");
            parent.setId(0L);

            list.add(parent) ;
        }

        return list ;

    }

    @Override
    public void insertCatalog(JobCatalogEntity entity) {

        JobCatalogEntity info = this.getById(entity.getParentId());
        if(info != null){
            entity.setAncestors(info.getAncestors() + "," + entity.getParentId());
        }

        this.save(entity) ;
    }

    @Override
    public List<TreeSelectDto> selectCatalogTreeList() {

        List<JobCatalogEntity> deptTrees = buildDeptTree(list());
        return deptTrees.stream().map(TreeSelectDto::new).collect(Collectors.toList());
    }

    /**
     * 构建前端所需要树结构
     *
     * @param prompts 部门列表
     * @return 树结构列表
     */
    public List<JobCatalogEntity> buildDeptTree(List<JobCatalogEntity> prompts) {

        List<JobCatalogEntity> returnList = new ArrayList<>();
        List<Long> tempList = prompts.stream().map(JobCatalogEntity::getId).toList();

        for (JobCatalogEntity dept : prompts) {
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(dept.getParentId())) {
                recursionFn(prompts, dept);
                returnList.add(dept);
            }
        }

        if (returnList.isEmpty()) {
            returnList = prompts;
        }
        return returnList;
    }

    /**
     * 递归列表
     */
    private void recursionFn(List<JobCatalogEntity> list, JobCatalogEntity t) {
        // 得到子节点列表
        List<JobCatalogEntity> childList = getChildList(list, t);
        t.setChildren(childList);
        for (JobCatalogEntity tChild : childList) {
            if (hasChild(list, tChild))
            {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<JobCatalogEntity> getChildList(List<JobCatalogEntity> list, JobCatalogEntity t) {
        List<JobCatalogEntity> tlist = new ArrayList<>();
        for (JobCatalogEntity n : list) {
            if (StringUtils.isNotNull(n.getParentId()) && n.getParentId().longValue() == t.getId().longValue()) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<JobCatalogEntity> list, JobCatalogEntity t) {
        return !getChildList(list, t).isEmpty();
    }
}
