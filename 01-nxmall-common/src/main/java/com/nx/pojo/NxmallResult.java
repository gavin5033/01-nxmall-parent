package com.nx.pojo;

/**
 * @author by 张益豪
 * @Classname NxmallResult
 * @Description TODO
 * @Date 2021/7/14 22:11
 */
public class NxmallResult {
    private int status;
    private Object data;
    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
