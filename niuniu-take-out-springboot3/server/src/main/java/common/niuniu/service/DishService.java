package common.niuniu.service;

import common.niuniu.dto.DishDTO;
import common.niuniu.dto.DishPageDTO;
import common.niuniu.entity.Dish;
import common.niuniu.result.PageResult;
import common.niuniu.vo.DishVO;

import java.util.List;

public interface DishService {
    void addDishWithFlavor(DishDTO dishDTO);

    PageResult getPageList(DishPageDTO dishPageDTO);

    DishVO getDishWithFlavorById(Integer id);

    void updateDishWithFlavor(DishDTO dishDTO);

    void deleteBatch(List<Integer> ids);

    void onOff(Integer id);

    List<DishVO> getDishesWithFlavorById(Dish dish);
}
