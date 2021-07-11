package com.nx.pojo;

import java.util.List;

/**
 * @author by 张益豪
 * @Classname EasyUIDatagrid
 * @Description TODO
 * @Date 2021/7/11 16:07
 */
public class EasyUIDatagrid {
    private Long total;
    private List<?> rows;

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<?> getRows() {
        return rows;
    }

    public void setRows(List<?> rows) {
        this.rows = rows;
    }
}
