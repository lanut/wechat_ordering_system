package common.niuniu.service;

import common.niuniu.dto.SetmealDTO;
import common.niuniu.dto.SetmealPageDTO;
import common.niuniu.entity.Setmeal;
import common.niuniu.result.PageResult;
import common.niuniu.vo.DishItemVO;
import common.niuniu.vo.SetmealVO;
import common.niuniu.vo.SetmealWithPicVO;

import java.util.List;

public interface SetmealService {
    void addSetmeal(SetmealDTO setmealDTO);

    PageResult getPageList(SetmealPageDTO setmealPageDTO);

    SetmealVO getSetmealById(Integer id);

    void onOff(Integer id);

    void update(SetmealDTO setmealDTO);

    void deleteBatch(List<Integer> ids);

    List<Setmeal> getList(Integer categoryId);

    List<DishItemVO> getSetmealDishesById(Integer id);

    SetmealWithPicVO getSetmealWithPic(Integer id);
}
