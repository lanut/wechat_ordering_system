show databases;

create database niuniu_take_out;
use niuniu_take_out;

-- 1、后台管理员工
create table employee
(
    id          int primary key auto_increment,
    name        varchar(64)        not null default '员工' comment '员工姓名',
    account     varchar(64) unique not null comment '账号',
    password    varchar(255)       not null comment '密码',
    phone       varchar(16)        null comment '手机号',
    age         int                null comment '年龄',
    gender      tinyint            null comment '性别',
    pic         longtext           null comment '头像',
    status      tinyint            not null default 0 comment '状态 0禁用 1启用',
    create_user int                not null comment '创建人', -- 可能是自己注册，也可能是管理员帮忙操作
    update_user int                not null comment '修改人', -- 存的是user的id
    create_time datetime           not null default now() comment '创建时间',
    update_time datetime           not null default now() comment '更新时间'
);
drop table employee;
-- update employee set account = 'cyh' where id = 4;

-- 2、分类：包括菜品分类和套餐分类，即小程序侧边栏
create table category
(
    id          int primary key auto_increment,
    name        varchar(64) unique not null comment '分类名称',
    type        tinyint            not null comment '类型   1 菜品分类 2 套餐分类',     -- 1表示菜品分类，2表示套餐分类
    sort        int                not null comment '顺序',                             -- 分类等级，1的优先级最高，asc升序
    status      tinyint            not null default 1 comment '分类状态 0:禁用，1:启用', -- 1启用，0禁用
    create_user int                not null comment '创建人',                           -- 存的是user的id
    update_user int                not null comment '修改人',
    create_time datetime           not null default now() comment '创建时间',
    update_time datetime           not null default now() comment '更新时间'
);
drop table category;

-- 3、菜品
create table dish
(
    id          int primary key auto_increment,
    name        varchar(64) unique not null comment '菜品名称',
    pic         longtext           null comment '图片',
    detail      varchar(255)       not null comment '描述',
    price       decimal(10, 2)     not null comment '价格',
    status      tinyint            not null default 1, -- 1启用，0禁用
    category_id int                not null,           -- fk 关联的菜品分类
    create_user int                not null,           -- 存的是user的id
    update_user int                not null,
    create_time datetime           not null default now(),
    update_time datetime           not null default now(),
    constraint fk_dish_category foreign key (category_id) references category (id) on update cascade on delete cascade
);
drop table dish;

-- 4、菜品口味
create table dish_flavor
(
    id      int primary key auto_increment,
    name    varchar(64)  not null, -- eg. 温度
    list    varchar(255) not null, -- eg. ['热','温','冷']
    dish_id int          not null,
    constraint fk_flavor_dish foreign key (dish_id) references dish (id) on update cascade on delete cascade
);

-- 5、套餐
create table setmeal
(
    id          int primary key auto_increment,
    name        varchar(64) unique not null comment '套餐名称',
    pic         longtext           null comment '图片',
    detail      varchar(255)       not null comment '描述',
    price       decimal(10, 2)     not null comment '价格',
    status      tinyint            not null default 1, -- 1启用，0禁用
    category_id int                not null,           -- fk 关联的套餐分类
    create_user int                not null,
    update_user int                not null,
    create_time datetime           not null default now(),
    update_time datetime           not null default now(),
    constraint fk_setmeal_category foreign key (category_id) references category (id) on update cascade on delete cascade
);
drop table setmeal;

-- 6、菜品套餐中间表(因为菜品套餐是 多对多 关系)
create table setmeal_dish
(
    id         int primary key auto_increment,
    name       varchar(64)    not null comment '套餐名称',
    price      decimal(10, 2) not null comment '价格',
    copies     int            not null comment '份数',
    dish_id    int            not null, -- fk 中间表的外键
    setmeal_id int            not null, -- fk 中间表的外键
    constraint fk_between_dish foreign key (dish_id) references dish (id) on update cascade on delete cascade,
    constraint fk_between_setmeal foreign key (setmeal_id) references setmeal (id) on update cascade on delete cascade
);
drop table setmeal_dish;


-- 7、微信小程序用户表
create table user
(
    id          int primary key auto_increment,
    name        varchar(64) null,
    openid      varchar(45) not null,
    phone       varchar(11) null,
    gender      tinyint     null,
    id_number   varchar(18) null,
    pic         longtext    null,
    create_time datetime    not null
);
drop table user;

-- 8、订单明细表
create table order_detail
(
    id          int            not null auto_increment comment '主键',
    name        varchar(32)             default null comment '名字',
    pic         longtext                default null comment '图片',
    order_id    int            not null comment '订单id',
    dish_id     int                     default null comment '菜品id',
    setmeal_id  int                     default null comment '套餐id',
    dish_flavor varchar(50)             default null comment '口味',
    number      int            not null default '1' comment '数量',
    amount      decimal(10, 2) not null comment '金额',
    primary key (id)
) comment ='订单明细表';
drop table if exists order_detail;

-- 9、订单表
create table orders
(
    id                      int            not null auto_increment comment '主键',
    number                  varchar(50)             default null comment '订单号',
    status                  int            not null default '1' comment '订单状态 1待付款 2待接单 3已接单 4派送中 5已完成 6已取消 7退款',
    user_id                 int            not null comment '下单用户',
    address_book_id         int            not null comment '地址id',
    order_time              datetime       not null comment '下单时间',
    checkout_time           datetime                default null comment '结账时间',
    pay_method              int            not null default '1' comment '支付方式 1微信,2支付宝',
    pay_status              tinyint        not null default '0' comment '支付状态 0未支付 1已支付 2退款',
    amount                  decimal(10, 2) not null comment '实收金额',
    remark                  varchar(100)            default null comment '备注',
    phone                   varchar(11)             default null comment '手机号',
    address                 varchar(255)            default null comment '地址',
    user_name               varchar(32)             default null comment '用户名称',
    consignee               varchar(32)             default null comment '收货人',
    cancel_reason           varchar(255)            default null comment '订单取消原因',
    rejection_reason        varchar(255)            default null comment '订单拒绝原因',
    cancel_time             datetime                default null comment '订单取消时间',
    estimated_delivery_time datetime                default null comment '预计送达时间',
    delivery_status         tinyint(1)     not null default '1' comment '配送状态  1立即送出  0选择具体时间',
    delivery_time           datetime                default null comment '送达时间',
    pack_amount             int                     default null comment '打包费',
    tableware_number        int                     default null comment '餐具数量',
    tableware_status        tinyint(1)     not null default '1' comment '餐具数量状态  1按餐量提供  0选择具体数量',
    primary key (id)
) comment ='订单表';
drop table if exists orders;

-- 10、购物车
create table cart
(
    id          int            not null auto_increment comment '主键',
    name        varchar(32)    null comment '商品名称',
    pic         longtext       null comment '图片',
    user_id     int            not null comment '主键',
    dish_id     int            null comment '菜品id',
    setmeal_id  int            null comment '套餐id',
    dish_flavor varchar(50)    null comment '口味',
    number      int            not null default '1' comment '数量',
    amount      decimal(10, 2) not null comment '金额',
    create_time datetime       null comment '创建时间',
    primary key (id)
) comment ='购物车';
drop table if exists cart;

-- 11、地址簿
create table address_book
(
    id            int          not null auto_increment comment '主键',
    user_id       int          not null comment '用户id',
    consignee     varchar(50)  null comment '收货人',
    gender        tinyint(1)   null comment '性别',
    phone         varchar(11)  not null comment '手机号',
    province_code varchar(12)  null comment '省级区划编号',
    province_name varchar(32)  null comment '省级名称',
    city_code     varchar(12)  null comment '市级区划编号',
    city_name     varchar(32)  null comment '市级名称',
    district_code varchar(12)  null comment '区级区划编号',
    district_name varchar(32)  null comment '区级名称',
    detail        varchar(200) null comment '详细地址',
    label         varchar(100) null comment '标签',
    is_default    tinyint(1)   not null default '0' comment '默认 0 否 1是',
    primary key (id)
) comment ='地址簿';
drop table if exists address_book;


