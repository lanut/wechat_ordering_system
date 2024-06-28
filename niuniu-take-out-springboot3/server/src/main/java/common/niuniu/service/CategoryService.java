package common.niuniu.service;

import common.niuniu.dto.CategoryDTO;
import common.niuniu.dto.CategoryTypePageDTO;
import common.niuniu.entity.Category;
import common.niuniu.result.PageResult;

import java.util.List;

public interface CategoryService {
    void addCategory(CategoryDTO categoryDTO);

    PageResult getPageList(CategoryTypePageDTO categoryTypePageDTO);

    List<Category> getList(Integer type);

    Category getById(Integer id);
    void onOff(Integer id);

    void udpate(CategoryDTO categoryDTO);

    void delete(Integer id);

}
