package com.lanut.ordering_backend.service.impl;

import com.lanut.ordering_backend.entity.Order;
import com.lanut.ordering_backend.mapper.OrderMapper;
import com.lanut.ordering_backend.service.IOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lanut
 * @since 2024-06-04
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

}
