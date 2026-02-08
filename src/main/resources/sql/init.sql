create table user_info_t (
 id bigint unsigned not null AUTO_INCREMENT comment '用户id',
 `name` varchar(20) not null comment '用户姓名',
 create_time datetime DEFAULT CURRENT_TIMESTAMP comment '创建时间',
 update_time datetime DEFAULT CURRENT_TIMESTAMP ON update current_TIMESTAMP comment '更新时间',
 state tinyint(1) DEFAULT 1 COMMENT '状态，0：退团，1：在团',
 PRIMARY KEY (`id`)
);

create table user_battle_score_t (
 id bigint unsigned not null AUTO_INCREMENT comment '主键id',
 user_id bigint unsigned not null comment '用户id',
 `name` varchar(20) not null comment '用户姓名',
 battle_date date not null comment '团战日期',
 score tinyint(1) not null,
 complete_attack tinyint(1) DEFAULT 3 COMMENT '是否出完刀，1：未出刀，2：未出完刀，3：满刀',
 create_time datetime DEFAULT CURRENT_TIMESTAMP comment '创建时间',
 update_time datetime DEFAULT CURRENT_TIMESTAMP ON update current_TIMESTAMP comment '更新时间',
 PRIMARY KEY (`id`)
);

create table team_battle_record_t (
 id bigint unsigned not null AUTO_INCREMENT comment '主键id',
 enemy_team_name varchar(20) not null comment '对方团名',
 battle_date date not null comment '团战日期',
 state tinyint(1) DEFAULT 1 COMMENT '状态，0：失败，1：胜利',
 team_rank smallint not null COMMENT '团排名',
 create_time datetime DEFAULT CURRENT_TIMESTAMP comment '创建时间',
 update_time datetime DEFAULT CURRENT_TIMESTAMP ON update current_TIMESTAMP comment '更新时间',
 PRIMARY KEY (`id`)
)

create table menu_item_t (
 id bigint unsigned not null AUTO_INCREMENT comment '菜单id',
 `name` varchar(20) not null comment '菜单名',
 parent_id bigint unsigned comment '父菜单id',
 icon varchar(20) comment '图标名',
 front_path varchar(50) comment '图标名',
 level tinyint(1) not null comment '层级',
 state tinyint(1) DEFAULT 1 COMMENT '状态，0：删除，1：正常',
 create_time datetime DEFAULT CURRENT_TIMESTAMP comment '创建时间',
 update_time datetime DEFAULT CURRENT_TIMESTAMP ON update current_TIMESTAMP comment '更新时间',
 PRIMARY KEY (`id`)
);