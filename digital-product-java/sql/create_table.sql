# 创建数据库
CREATE DATABASE IF NOT EXISTS digital_product
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

# 切换数据库
USE digital_product;

# 管理员表
create table admin
(
    id          int unsigned auto_increment comment '主键'
        primary key,
    nickname    varchar(32)                          not null comment '昵称',
    avatar      varchar(255)                         not null comment '头像',
    username    varchar(32)                          not null comment '账号',
    password    varchar(255)                         not null comment '密码',
    role        tinyint                              not null comment '角色：0-管理员，1-店家，2-用户',
    phone       varchar(11)                          null comment '电话',
    email       varchar(254)                         null comment '邮箱',
    create_time datetime   default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime   default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '修改时间',
    is_deleted  tinyint(1) default 0                 not null comment '逻辑删除标记：0-未删除，1-已删除',
    constraint email
        unique (email),
    constraint phone
        unique (phone),
    constraint username
        unique (username)
)
    comment '管理员表';


# 店铺表
create table digital_shop
(
    id               int unsigned auto_increment comment '主键'
        primary key,
    nickname         varchar(32)                          not null comment '昵称',
    avatar           varchar(255)                         not null comment '头像',
    username         varchar(32)                          not null comment '账号',
    password         varchar(255)                         not null comment '密码',
    role             tinyint                              not null comment '角色：0-管理员，1-店家，2-用户',
    phone            varchar(11)                          null comment '电话',
    email            varchar(254)                         null comment '邮箱',
    province_code    char(6)                              null comment '省级区划编号',
    city_code        char(6)                              null comment '市级区划编号',
    district_code    char(6)                              null comment '区级区划编号',
    detail_address   varchar(255)                         null comment '详细地址',
    introduce        text                                 null comment '介绍',
    practice_license varchar(255)                         null comment '营业执照',
    principal_name   varchar(32)                          null comment '负责人姓名',
    principal_no     char(18)                             null comment '负责人身份证号',
    audit_status     tinyint                              not null comment '审核状态：0-待提交，1-待审核，2-审核通过，3-审核不通过',
    create_time      datetime   default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time      datetime   default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '修改时间',
    is_deleted       tinyint(1) default 0                 not null comment '逻辑删除标记：0-未删除，1-已删除',
    constraint email
        unique (email),
    constraint phone
        unique (phone),
    constraint username
        unique (username)
)
    comment '店铺表';


# 用户表
create table user
(
    id          int unsigned auto_increment comment '主键'
        primary key,
    nickname    varchar(32)                              not null comment '昵称',
    avatar      varchar(255)                             not null comment '头像',
    username    varchar(32)                              not null comment '账号',
    password    varchar(255)                             not null comment '密码',
    role        tinyint                                  not null comment '角色：0-管理员，1-店家，2-用户',
    phone       varchar(11)                              null comment '电话',
    email       varchar(254)                             null comment '邮箱',
    balance     decimal(10, 2) default 0.00              null comment '余额',
    create_time datetime       default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime       default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '修改时间',
    is_deleted  tinyint(1)     default 0                 not null comment '逻辑删除标记：0-未删除，1-已删除',
    constraint email
        unique (email),
    constraint phone
        unique (phone),
    constraint username
        unique (username)
)
    comment '用户表';


# 数码产品类型表
create table product_type
(
    id   int unsigned auto_increment comment '主键'
        primary key,
    name varchar(32) not null comment '类型名称',
    constraint name
        unique (name)
)
    comment '数码产品类型表';


# 数码产品表
create table product
(
    id          int unsigned auto_increment comment '主键'
        primary key,
    shop_id     int unsigned      not null comment '关联店铺id',
    type_id     int unsigned      not null comment '关联数码产品类型id',
    name        varchar(32)       not null comment '产品名称',
    used        tinyint           not null comment '产品品质：0-全新，1-二手',
    img         varchar(255)      not null comment '产品图片',
    price       decimal(10, 2)    not null comment '产品价格',
    store       int               not null comment '剩余数量',
    introduce   text              null comment '产品介绍',
    content     longtext          null comment '产品详情',
    sale_status tinyint           not null comment '售卖状态：0-已售罄，1-售卖中，2-未上架',
    recommend   tinyint default 0 null comment '是否推荐：0-未推荐，1-推荐中'
)
    comment '数码产品表';


# 数码产品订单表
create table product_orders
(
    id             int unsigned auto_increment comment '主键'
        primary key,
    order_no       bigint                             not null comment '订单号',
    order_status   tinyint                            null comment '订单状态：0-待付款，1-待接单，2-派送中，3-已送达，4-已完成，5-已取消',
    order_time     datetime default CURRENT_TIMESTAMP not null comment '下单时间',
    user_id        int unsigned                       not null comment '关联用户id',
    shop_id        int unsigned                       not null comment '关联店铺id',
    product_id     int unsigned                       not null comment '关联产品id',
    product_name   varchar(32)                        null comment '产品名称，冗余',
    product_img    varchar(255)                       null comment '产品图片，冗余',
    product_price  decimal(10, 2)                     null comment '产品价格，冗余',
    address_id     int unsigned                       not null comment '关联地址id',
    consignee      varchar(32)                        null comment '收货人，冗余',
    phone_number   varchar(11)                        null comment '收货电话，冗余',
    province_code  char(6)                            null comment '省级区划编号，冗余',
    city_code      char(6)                            null comment '市级区划编号，冗余',
    district_code  char(6)                            null comment '区级区划编号，冗余',
    detail_address varchar(255)                       null comment '详细地址，冗余',
    constraint order_no
        unique (order_no)
)
    comment '数码产品订单表';


# 数码配件类型表
create table accessory_type
(
    id   int unsigned auto_increment comment '主键'
        primary key,
    name varchar(32) not null comment '类型名称',
    constraint name
        unique (name)
)
    comment '数码配件类型表';


# 数码配件表
create table accessory
(
    id          int unsigned auto_increment comment '主键'
        primary key,
    shop_id     int unsigned   not null comment '关联店铺id',
    type_id     int unsigned   not null comment '关联数码配件类型id',
    name        varchar(32)    not null comment '配件名称',
    img         varchar(255)   not null comment '配件图片',
    price       decimal(10, 2) not null comment '配件价格',
    store       int            not null comment '剩余数量',
    introduce   text           null comment '配件简介',
    sale_volume int default 0  null comment '销量',
    sale_status tinyint        not null comment '售卖状态：0-已售罄，1-售卖中，2-未上架'
)
    comment '数码配件表';


# 数码配件订单表
create table accessory_orders
(
    id              int unsigned auto_increment comment '主键'
        primary key,
    order_no        bigint                             not null comment '订单号',
    order_status    tinyint                            null comment '订单状态：0-待付款，1-待接单，2-派送中，3-已送达，4-已完成，5-已取消',
    order_time      datetime default CURRENT_TIMESTAMP not null comment '下单时间',
    quantity        int                                not null comment '购买数量',
    total_price     decimal(10, 2)                     not null comment '购买总价',
    user_id         int unsigned                       not null comment '关联用户id',
    shop_id         int unsigned                       not null comment '关联店铺id',
    accessory_id    int unsigned                       not null comment '关联数码配件id',
    accessory_name  varchar(32)                        null comment '配件名称，冗余',
    accessory_img   varchar(255)                       null comment '配件图片，冗余',
    accessory_price decimal(10, 2)                     null comment '配件价格，冗余',
    address_id      int unsigned                       not null comment '关联地址id',
    consignee       varchar(32)                        null comment '收货人，冗余',
    phone_number    varchar(11)                        null comment '收货电话，冗余',
    province_code   char(6)                            null comment '省级区划编号，冗余',
    city_code       char(6)                            null comment '市级区划编号，冗余',
    district_code   char(6)                            null comment '区级区划编号，冗余',
    detail_address  varchar(255)                       null comment '详细地址，冗余',
    constraint order_no
        unique (order_no)
)
    comment '数码配件订单表';


# 收藏表
create table collect
(
    id         int unsigned auto_increment comment '主键'
        primary key,
    user_id    int unsigned not null comment '关联用户id',
    product_id int unsigned not null comment '关联数码产品id'
)
    comment '收藏表';


# 购物车表
create table cart
(
    id           int unsigned auto_increment comment '主键'
        primary key,
    user_id      int unsigned  not null comment '关联用户id',
    quantity     int default 1 null comment '数码配件数量',
    accessory_id int unsigned  not null comment '关联数码配件id'
)
    comment '购物车表';


# 用户地址表
create table address
(
    id             int unsigned auto_increment comment '主键'
        primary key,
    user_id        int unsigned      not null comment '关联用户id',
    consignee      varchar(32)       not null comment '收货人',
    phone_number   varchar(11)       not null comment '收货电话',
    province_code  char(6)           not null comment '省级区划编号',
    city_code      char(6)           not null comment '市级区划编号',
    district_code  char(6)           not null comment '区级区划编号',
    detail_address varchar(255)      not null comment '详细地址',
    tag            varchar(32)       null comment '标签',
    is_default     tinyint default 0 null comment '是否作为默认地址：0-非默认，1-默认'
)
    comment '用户地址表';


# 轮播图表
create table slideshow
(
    id         int unsigned auto_increment comment '主键'
        primary key,
    product_id int unsigned not null comment '关联数码产品id',
    shop_id    int unsigned not null comment '关联店铺id',
    img        varchar(255) not null comment '轮播展示图'
)
    comment '轮播图表';


# 系统公告表
create table notice
(
    id           int unsigned auto_increment comment '主键'
        primary key,
    title        varchar(64)                        not null comment '公告标题',
    content      text                               not null comment '公告内容',
    release_time datetime default CURRENT_TIMESTAMP not null comment '发布时间'
)
    comment '系统公告表';