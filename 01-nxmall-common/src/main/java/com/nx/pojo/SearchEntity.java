package com.nx.pojo;

/**
 * @author by 张益豪
 * @Classname SearchEntity
 * @Description TODO
 * @Date 2021/7/14 22:12
 */
public class SearchEntity extends TbItem{
    private String[] images;
    private Boolean enough;

    public Boolean getEnough() {
        return enough;
    }

    public void setEnough(Boolean enough) {
        this.enough = enough;
    }

    public String[] getImages() {
        return images;
    }

    public void setImages(String[] images) {
        this.images = images;
    }
}
