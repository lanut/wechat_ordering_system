package com.lanut.ordering_backend.service.impl;

import com.lanut.ordering_backend.entity.Feedback;
import com.lanut.ordering_backend.mapper.FeedbackMapper;
import com.lanut.ordering_backend.service.IFeedbackService;
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
public class FeedbackServiceImpl extends ServiceImpl<FeedbackMapper, Feedback> implements IFeedbackService {

}
