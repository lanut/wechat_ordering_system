package common.niuniu.service;

import common.niuniu.vo.BusinessDataVO;
import common.niuniu.vo.DishOverViewVO;
import common.niuniu.vo.OrderOverViewVO;
import common.niuniu.vo.SetmealOverViewVO;

import java.time.LocalDateTime;

public interface WorkSpaceService {
    BusinessDataVO getBusinessData(LocalDateTime begin, LocalDateTime end);

    OrderOverViewVO getOrderOverView();

    DishOverViewVO getDishOverView();

    SetmealOverViewVO getSetmealOverView();
}
