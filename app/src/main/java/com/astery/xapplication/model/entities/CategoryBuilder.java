package com.astery.xapplication.model.entities;

/*
import java.util.ArrayList;
import java.util.List;

public class CategoryBuilder {
    private final List<com.astery.xapplication.pojo.Category> list;

    public CategoryBuilder(List<com.astery.xapplication.pojo.Category> list) {
        this.list = list;
    }
    public com.astery.xapplication.pojo.Category build(){
        com.astery.xapplication.pojo.Category category = new com.astery.xapplication.pojo.Category("", null, null, null, null);
        category.setChildren(new ArrayList<>());
        for (com.astery.xapplication.pojo.Category c: list){
            if (c.getParentId() == null)
                category.getChildren().add(c);
        }
        if (category.getChildren() == null)
            return null;
        else
            for (com.astery.xapplication.pojo.Category c: category.getChildren()){
                newStep(c);
            }
            return category;
    }

    private void newStep(com.astery.xapplication.pojo.Category category){
        category.setChildren(new ArrayList<>());
        for (com.astery.xapplication.pojo.Category c: list){
            if (c.getParentId() == null)
                continue;
            if (c.getParentId().equals(category.getId())) {
                category.getChildren().add(c);
                newStep(c);
            }
        }
    }
}

 */
