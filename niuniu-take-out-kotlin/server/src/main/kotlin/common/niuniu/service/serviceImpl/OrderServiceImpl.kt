package common.niuniu.service.serviceImpl

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import com.github.pagehelper.Page
import com.github.pagehelper.PageHelper
import common.niuniu.annotation.Slf4j.Companion.log
import common.niuniu.constant.MessageConstant
import common.niuniu.context.BaseContext.currentId
import common.niuniu.dto.*
import common.niuniu.exception.AddressBookBusinessException
import common.niuniu.exception.OrderBusinessException
import common.niuniu.exception.ShoppingCartBusinessException
import common.niuniu.mapper.*
import common.niuniu.po.Cart
import common.niuniu.po.Order
import common.niuniu.po.OrderDetail
import common.niuniu.result.PageResult
import common.niuniu.service.OrderService
import common.niuniu.utils.HttpClientUtil.doGet
import common.niuniu.utils.WeChatPayUtil
import common.niuniu.vo.OrderPaymentVO
import common.niuniu.vo.OrderStatisticsVO
import common.niuniu.vo.OrderSubmitVO
import common.niuniu.vo.OrderVO
import common.niuniu.websocket.WebSocketServer
import lombok.extern.slf4j.Slf4j
import org.springframework.beans.BeanUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.util.CollectionUtils
import java.time.LocalDateTime
import java.util.stream.Collectors

@Service
@Slf4j
class OrderServiceImpl : OrderService {
    @Autowired
    private val orderMapper: OrderMapper? = null

    @Autowired
    private val orderDetailMapper: OrderDetailMapper? = null

    @Autowired
    private val cartMapper: CartMapper? = null

    @Autowired
    private val addressBookMapper: AddressBookMapper? = null

    @Autowired
    private val userMapper: UserMapper? = null
    private var order: Order? = null

    @Autowired
    private val webSocketServer: WebSocketServer? = null

    // 这个Value是annotation注解的包，不是lombok的！
    @Value("\${niuniu.shop.address}")
    private lateinit var shopAddress: String

    @Value("\${niuniu.baidu.ak}")
    private lateinit var ak: String

    /**
     * 用户下单
     *
     * @param orderSubmitDTO
     * @return
     */
    override fun submit(orderSubmitDTO: OrderSubmitDTO?): OrderSubmitVO? {
        // 1、查询校验地址情况
        val addressBook = addressBookMapper!!.getById(orderSubmitDTO!!.addressId)
            ?: throw AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL)
        // 不能超出配送范围
        // checkOutOfRange(addressBook.getCityName() + addressBook.getDistrictName() + addressBook.getDetail());
        // 2、查询校验购物车情况
        val userId = currentId
        val cart = Cart()
        cart.userId = userId
        val cartList = cartMapper!!.list(cart)
        if (cartList == null || cartList.isEmpty()) {
            throw ShoppingCartBusinessException(MessageConstant.CART_IS_NULL)
        }
        // 3、构建订单数据
        val order = Order()
        BeanUtils.copyProperties(orderSubmitDTO, order)
        order.addressBookId = orderSubmitDTO.addressId
        order.phone = addressBook.phone
        order.address = addressBook.detail
        order.consignee = addressBook.consignee
        // 利用时间戳来生成当前订单的编号
        order.number = System.currentTimeMillis().toString()
        order.userId = userId
        order.status = Order.PENDING_PAYMENT // 刚下单提交，此时是待付款状态
        order.payStatus = Order.UN_PAID // 未支付
        order.orderTime = LocalDateTime.now()
        this.order = order
        // 4、向订单表插入1条数据
        orderMapper!!.insert(order)
        // 订单明细数据
        val orderDetailList: MutableList<OrderDetail?> = ArrayList()
        // 遍历购物车中所有的商品，逐个加到订单明细表
        for (c in cartList) {
            val orderDetail = OrderDetail()
            BeanUtils.copyProperties(c, orderDetail)
            orderDetail.orderId = order.id
            orderDetailList.add(orderDetail)
        }
        // 5、向明细表插入n条数据
        orderDetailMapper!!.insertBatch(orderDetailList)
        // 6、清理购物车中的数据
        cartMapper.delete(userId)
        // 7、封装返回结果
        val orderSubmitVO = OrderSubmitVO.builder()
            .id(order.id)
            .orderNumber(order.number)
            .orderAmount(order.amount)
            .orderTime(order.orderTime)
            .build()
        return orderSubmitVO
    }

    /**
     * 当前用户未支付订单数量
     *
     * @return
     */
    override fun unPayOrderCount(): Int? {
        val userId = currentId
        return orderMapper!!.getUnPayCount(userId)
    }

    /**
     * 根据id查询订单详情
     *
     * @param id
     * @return
     */
    override fun getById(id: Int?): OrderVO? {
        val order = orderMapper!!.getById(id)
        val orderDetailList = orderDetailMapper!!.getById(id)
        val orderVO = OrderVO()
        BeanUtils.copyProperties(order!!, orderVO)
        orderVO.orderDetailList = orderDetailList
        return orderVO
    }

    /**
     * 用户端条件分页查询历史订单
     *
     * @param page
     * @param pageSize
     * @param status
     * @return
     */
    override fun userPage(page: Int, pageSize: Int, status: Int?): PageResult? {
        PageHelper.startPage<Any>(page, pageSize)
        // 要根据当前用户和状态条件来查询订单，因此设计OrderPageDTO来封装信息
        val orderPageDTO = OrderPageDTO()
        orderPageDTO.userId = currentId
        orderPageDTO.status = status
        val orderPage = orderMapper!!.page(orderPageDTO)
        // 查到所有订单orderPage后，封装成OrderVO列表返回
        val list: MutableList<OrderVO?> = ArrayList()
        // 其实就是将每个订单都加上订单详情OrderDetail
        if (orderPage != null && orderPage.total > 0) {
            for (order in orderPage) {
                val orderId = order!!.id // 订单id
                // 查询订单明细
                val orderDetails = orderDetailMapper!!.getById(orderId)
                val orderVO = OrderVO()
                BeanUtils.copyProperties(order, orderVO)
                orderVO.orderDetailList = orderDetails
                list.add(orderVO)
            }
        }
        return PageResult(orderPage!!.total, list)
    }

    /**
     * 用户根据订单id取消订单
     *
     * @param id
     */
    @Throws(Exception::class)
    override fun userCancelById(id: Int?) {
        // 根据id查询订单
        val ordersDB = orderMapper!!.getById(id) ?: throw OrderBusinessException(MessageConstant.ORDER_NOT_FOUND)
        // 校验订单是否存在
        // 订单状态 1待付款 2待接单 3已接单 4派送中 5已完成 6已取消  前两个状态才能直接退款，否则要联系商家
        if (ordersDB.status > 2) {
            throw OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR)
        }
        val order = Order()
        order.id = ordersDB.id

        // 订单处于待接单状态下取消，需要进行退款
        if (ordersDB.status == Order.TO_BE_CONFIRMED) {
            // 调用微信支付退款接口（refund有异常需要抛出处理）
            // 用不了
//            weChatPayUtil.refund(
//                    ordersDB.getNumber(), //商户订单号
//                    ordersDB.getNumber(), //商户退款单号
//                    new BigDecimal(0.01),//退款金额，单位 元
//                    new BigDecimal(0.01));//原订单金额

            // 支付状态修改为 退款

            order.payStatus = Order.REFUND
        }

        // 更新订单状态、取消原因、取消时间
        order.status = Order.CANCELLED
        order.cancelReason = "用户取消"
        order.cancelTime = LocalDateTime.now()
        orderMapper.update(order)
    }

    /**
     * 根据订单id再来一单
     *
     * @param id
     */
    override fun reOrder(id: Int?) {
        val userId = currentId
        // 1、先拿到这个订单id的所有菜品
        val detailList = orderDetailMapper!!.getById(id)
        // 2、将订单详情对象转换为购物车对象
//        List<Cart> cartList = new ArrayList<>();
//        for (OrderDetail orderDetail : detailList){
//            Cart cart = new Cart();
//            BeanUtils.copyProperties(orderDetail, cart, "id");
//            cart.setUserId(userId);
//            cart.setCreateTime(LocalDateTime.now());
//            cartList.add(cart);
//        }
        val cartList = detailList!!.stream().map<Cart?> { x: OrderDetail? ->
            val cart = Cart()
            // 将原订单详情里面的菜品信息重新复制到购物车对象中
            BeanUtils.copyProperties(x!!, cart, "id")
            cart.userId = userId
            cart.createTime = LocalDateTime.now()
            cart
        }.toList()
        // 3、将购物车对象批量添加到数据库
        cartMapper!!.insertBatch(cartList)
    }

    /**
     * 用户支付订单
     *
     * @param orderPaymentDTO
     * @return
     */
    override fun payment(orderPaymentDTO: OrderPaymentDTO?): OrderPaymentVO? {
        // 当前登录用户id
        val userId = currentId
        val user = userMapper!!.getById(userId)
        // 调用微信支付接口，生成预支付交易单
        // 暂时不做，而是把 weChatUtils 里相关的参数设置好，让后续代码不出问题
        val jsonObject = JSONObject()
        jsonObject["code"] = "ORDERPAID"
        // 抽取 paySuccess 的代码：不搞支付，修改订单状态后直接更新数据库，并返回给前端
        val vo = jsonObject.toJavaObject(OrderPaymentVO::class.java)
        vo.packageStr = jsonObject.getString("package")
        val OrderPaidStatus = Order.PAID // 支付状态，已支付
        val OrderStatus = Order.TO_BE_CONFIRMED // 订单状态，待接单
        val checkOutTime = LocalDateTime.now() // 更新支付时间
        orderMapper!!.updateStatus(OrderStatus, OrderPaidStatus, checkOutTime, order!!.id)

        // 由于跳过了微信支付，因此没有通过微信来调用paySuccess方法，所以把里面的消息提醒方法抽出来放到这里！
        // 通过websocket向客户端浏览器推送消息 type orderId content
        val map: MutableMap<String, Any> = mutableMapOf()
        map["type"] = 1 // 消息类型，1表示来单提醒（2表示客户催单）
        map["orderId"] = order!!.id
        map["content"] = "订单号：" + order!!.number
        val json = JSON.toJSONString(map)
        log.info("发给商家端：{}", map)
        webSocketServer!!.sendToAllClient(json)

        return vo
    }

    /**
     * 条件分页查询订单信息
     *
     * @param orderPageDTO
     * @return
     */
    override fun conditionSearch(orderPageDTO: OrderPageDTO?): PageResult? {
        PageHelper.startPage<Any>(orderPageDTO!!.page, orderPageDTO.pageSize)
        val orders = orderMapper!!.page(orderPageDTO)
        // 部分订单状态，需要额外返回订单菜品信息，将Orders转化为OrderVO
        val orderVOList = getOrderVOList(orders)
        return PageResult(orders!!.total, orderVOList)
    }

    /**
     * 不同状态订单数量统计
     *
     * @return
     */
    override fun statistics(): OrderStatisticsVO? {
        // 根据状态，分别查询出待接单、已接单/待派送、派送中的订单数量
        val toBeConfirmed = orderMapper!!.countByStatus(Order.TO_BE_CONFIRMED)
        val confirmed = orderMapper.countByStatus(Order.CONFIRMED)
        val deliveryInProgress = orderMapper.countByStatus(Order.DELIVERY_IN_PROGRESS)
        // 封装成VO返回
        return OrderStatisticsVO.builder()
            .toBeConfirmed(toBeConfirmed)
            .confirmed(confirmed)
            .deliveryInProgress(deliveryInProgress)
            .build()
    }

    /**
     * 接单
     *
     * @param orderConfirmDTO
     */
    override fun confirm(orderConfirmDTO: OrderConfirmDTO?) {
        val order = Order.builder()
            .id(orderConfirmDTO!!.id)
            .status(Order.CONFIRMED)
            .build()
        orderMapper!!.update(order)
    }

    /**
     * 拒单
     *
     * @param orderRejectionDTO
     */
    override fun reject(orderRejectionDTO: OrderRejectionDTO?) {
        val orderId = orderRejectionDTO!!.id
        val orderDB = orderMapper!!.getById(orderId)
        val order = Order()
        // 订单只有存在且状态为2（待接单）才可以拒单
        if (orderDB == null || orderDB.status != Order.TO_BE_CONFIRMED) {
            throw OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR)
        }
        // 拒单需要退款，根据订单id更新订单状态、拒单原因、取消时间
        order.id = orderDB.id
        // 调用微信支付退款接口，但关于微信的接口都用不了，支付状态修改为 退款 就行
        order.payStatus = Order.REFUND
        order.status = Order.CANCELLED
        order.rejectionReason = orderRejectionDTO.rejectionReason
        order.cancelTime = LocalDateTime.now()
        orderMapper.update(order)
    }

    /**
     * 取消订单
     *
     * @param orderCancelDTO
     */
    override fun cancel(orderCancelDTO: OrderCancelDTO?) {
        val orderId = orderCancelDTO!!.id
        val orderDB = orderMapper!!.getById(orderId)
        val order = Order()
        // 取消订单需要退款，根据订单id更新订单状态、取消原因、取消时间
        order.id = orderDB!!.id
        // 调用微信支付退款接口，但关于微信的接口都用不了，支付状态修改为 退款 就行
        order.payStatus = Order.REFUND
        order.status = Order.CANCELLED
        order.cancelReason = orderCancelDTO.cancelReason
        order.cancelTime = LocalDateTime.now()
        orderMapper.update(order)
    }

    /**
     * 根据id派送订单
     *
     * @param id
     */
    override fun delivery(id: Int?) {
        val orderDB = orderMapper!!.getById(id)
        // 订单存在 且 状态为3已接单，才能进行派送操作
        if (orderDB == null || orderDB.status != Order.CONFIRMED) {
            throw OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR)
        }
        val order = Order()
        order.id = orderDB.id
        order.status = Order.DELIVERY_IN_PROGRESS
        orderMapper.update(order)
    }

    /**
     * 完成订单
     *
     * @param id
     */
    override fun complete(id: Int?) {
        val orderDB = orderMapper!!.getById(id)
        // 订单存在 且 状态为4派送中，才能进行完成操作
        if (orderDB == null || orderDB.status != Order.DELIVERY_IN_PROGRESS) {
            throw OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR)
        }
        val order = Order()
        order.id = orderDB.id
        order.status = Order.COMPLETED
        order.deliveryTime = LocalDateTime.now() // 设置订单完成时间
        orderMapper.update(order)
    }

    /**
     * 用户催单
     * @param id
     */
    override fun reminder(id: Int) {
        val orderDB = orderMapper!!.getById(id) ?: throw OrderBusinessException(MessageConstant.ORDER_NOT_FOUND)
        // 订单不存在
        // 通过websocket向客户端浏览器推送消息 type orderId content
        val map: MutableMap<String, Any> = mutableMapOf()
        map["type"] = 2 // 消息类型，2表示客户催单（1表示来单提醒）
        map["orderId"] = id
        map["content"] = "订单号：" + orderDB.number
        val json = JSON.toJSONString(map)
        log.info("发给商家端啊！：{}", map)
        webSocketServer!!.sendToAllClient(json)
    }

    /**
     * 抽出page.getResult()的内容，其中的订单菜品需要有详情信息
     *
     * @param page
     * @return
     */
    private fun getOrderVOList(page: Page<Order?>?): List<OrderVO?> {
        // 需要返回订单菜品信息，自定义OrderVO响应结果
        val orderVOList: MutableList<OrderVO?> = ArrayList()
        val ordersList = page!!.result
        if (!CollectionUtils.isEmpty(ordersList)) {
            for (orders in ordersList) {
                // 将共同字段复制到OrderVO
                val orderVO = OrderVO()
                BeanUtils.copyProperties(orders!!, orderVO)
                val orderDishes = getOrderDishesStr(orders)
                // 将订单菜品信息封装到orderVO中，并添加到orderVOList
                orderVO.orderDishes = orderDishes
                orderVOList.add(orderVO)
            }
        }
        return orderVOList
    }

    /**
     * 根据订单id获取菜品信息字符串
     *
     * @param order
     * @return
     */
    private fun getOrderDishesStr(order: Order?): String {
        // 查询订单菜品详情信息（订单中的菜品和数量）
        val orderDetailList = orderDetailMapper!!.getById(order!!.id)
        // 将每一条订单菜品信息拼接为字符串（格式：宫保鸡丁*3;）
        val orderDishList = orderDetailList!!.stream().map { x: OrderDetail? ->
            val orderDish = x!!.name + "*" + x.number + ";"
            orderDish
        }.collect(Collectors.toList())
        // 将该订单对应的所有菜品信息拼接在一起
        return java.lang.String.join("", orderDishList)
    }

    /**
     * 检查客户的收货地址是否超出配送范围
     *
     * @param address
     */
    private fun checkOutOfRange(address: String) {
        val map: MutableMap<String, String> = mutableMapOf()
        map["address"] = shopAddress
        map["output"] = "json"
        map["ak"] = ak

        // 获取店铺的经纬度坐标
        val shopCoordinate = doGet("https://api.map.baidu.com/geocoding/v3", map)

        var jsonObject = JSON.parseObject(shopCoordinate)
        if (jsonObject.getString("status") != "0") {
            throw OrderBusinessException("店铺地址解析失败")
        }

        // 数据解析
        var location = jsonObject.getJSONObject("result").getJSONObject("location")
        var lat = location.getString("lat")
        var lng = location.getString("lng")
        // 店铺经纬度坐标
        val shopLngLat = "$lat,$lng"

        map["address"] = address

        // 获取用户收货地址的经纬度坐标
        val userCoordinate = doGet("https://api.map.baidu.com/geocoding/v3", map)

        jsonObject = JSON.parseObject(userCoordinate)
        if (jsonObject.getString("status") != "0") {
            throw OrderBusinessException("收货地址解析失败")
        }

        // 数据解析
        location = jsonObject.getJSONObject("result").getJSONObject("location")
        lat = location.getString("lat")
        lng = location.getString("lng")
        // 用户收货地址经纬度坐标
        val userLngLat = "$lat,$lng"

        map["origin"] = shopLngLat
        map["destination"] = userLngLat
        map["steps_info"] = "0"

        // 路线规划
        val json = doGet("https://api.map.baidu.com/directionlite/v1/driving", map)

        jsonObject = JSON.parseObject(json)
        if (jsonObject.getString("status") != "0") {
            throw OrderBusinessException("配送路线规划失败")
        }

        // 数据解析
        val result = jsonObject.getJSONObject("result")
        val jsonArray = result["routes"] as JSONArray?
        val distance = (jsonArray!![0] as JSONObject)["distance"] as Int?

        if (distance!! > 5000) {
            //配送距离超过5000米
            throw OrderBusinessException("超出配送范围")
        }
    }
}
