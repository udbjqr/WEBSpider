/*==============================================================*/
/* DBMS name:      PostgreSQL 9.x                               */
/* Created on:     2017/5/25 11:53:27                           */
/*==============================================================*/


create sequence seq_cleanup_data
start 200;

create sequence seq_spider_record
start 200;

create sequence seq_web_address
start 200;

create sequence seq_data
start 200;

create sequence seq_original_data
start 200;

/*==============================================================*/
/* Table: acquire_original_data                                 */
/*==============================================================*/
create table acquire_original_data (
   id                   integer              not null default nextval('seq_data'),
   create_time          TIMESTAMP            null default now(),
   absolute_url         varchar(2000)        null,
   content              text                 null,
   is_work_out          integer              null default '0',
   web_address_id       integer              not null,
   constraint PK_ACQUIRE_ORIGINAL_DATA primary key (id)
);

comment on table acquire_original_data is
'保存所有原始获取数据信息。';

comment on column acquire_original_data.content is
'从网页上获取到的内容，包括整个body段';

comment on column acquire_original_data.is_work_out is
'0：未处理
1：已处理';

comment on column acquire_original_data.web_address_id is
'对应获取位置的网址';

/*==============================================================*/
/* Table: audit_data                                            */
/*==============================================================*/
create table audit_data (
   id                   integer              not null default nextval('seq_original_data'),
   audit_data           jsonb                not null,
   create_time          TIMESTAMP            not null default now(),
   is_upload            integer              not null default '0',
   constraint PK_AUDIT_DATA primary key (id)
);

comment on table audit_data is
'保存经过编辑修改后最终确定的数据';

comment on column audit_data.audit_data is
'保存审核后准备上传数据。';

comment on column audit_data.is_upload is
'0:未上传
1：已上传';

/*==============================================================*/
/* Table: city                                                  */
/*==============================================================*/
create table city (
   id                   integer              not null,
   name                 varchar(300)         not null,
   province_id          integer              null,
   constraint PK_CITY primary key (id)
);

/*==============================================================*/
/* Table: clean_up_data                                         */
/*==============================================================*/
create table clean_up_data (
   id                   integer              not null default nextval('seq_original_data'),
   clean_up_data        jsonb                not null,
   create_time          TIMESTAMP            not null default now(),
   is_audit             integer              not null default '0',
   constraint PK_CLEAN_UP_DATA primary key (id)
);

comment on table clean_up_data is
'保存经过整理后的数据';

comment on column clean_up_data.clean_up_data is
'保存整理后得以的数据，以及相应网址相关联数据';

comment on column clean_up_data.is_audit is
'0:未审核
1：已审核';

/*==============================================================*/
/* Table: county                                                */
/*==============================================================*/
create table county (
   id                   integer              not null,
   name                 varchar(300)         not null,
   city_id              integer              null,
   constraint PK_COUNTY primary key (id)
);

/*==============================================================*/
/* Table: open_tenders_data                                     */
/*==============================================================*/
create table open_tenders_data (
   id                   integer              not null,
   data                 jsonb                not null,
   create_time          TIMESTAMP            not null default now(),
   money                numeric(18,2)         null,
   tenderee             varchar(800)         null,
   location             integer              null,
   is_push              integer              not null default '0',
   constraint PK_OPEN_TENDERS_DATA primary key (id)
);

comment on table open_tenders_data is
'此为服务端表，保存客户端上传的招标信息';

comment on column open_tenders_data.data is
'保存上传的完整数据';

comment on column open_tenders_data.is_push is
'0:未推送
1：已推送';

/*==============================================================*/
/* Table: province                                              */
/*==============================================================*/
create table province (
   id                   integer              not null,
   name                 varchar(300)         not null,
   constraint PK_PROVINCE primary key (id)
);

/*==============================================================*/
/* Table: spider_record                                         */
/*==============================================================*/
create table spider_record (
   id                   integer              not null default nextval('seq_spider_record'),
   create_time          TIMESTAMP            not null default now(),
   get_url              varchar(8000)        not null,
   is_succeed           integer              not null default '0',
   record               jsonb                not null,
   constraint PK_SPIDER_RECORD primary key (id)
);

comment on table spider_record is
'保存每次抓取的记录';

comment on column spider_record.is_succeed is
'0:未成功
1：成功';

comment on column spider_record.record is
'记录此次抓取情况。
未成功记录返回错误信息
成功记录结果集';

/*==============================================================*/
/* Table: user_authorization                                    */
/*==============================================================*/
create table user_authorization (
   Column_1             integer              not null,
   Column_2             varchar(200)         null,
   Column_3             jsonb                null,
   constraint PK_USER_AUTHORIZATION primary key (Column_1)
);

comment on table user_authorization is
'记录已经授权用户的信息';

comment on column user_authorization.Column_3 is
'以一个json格式保存允许用户访问的结构。方便扩展';

/*==============================================================*/
/* Table: web_address                                           */
/*==============================================================*/
create table web_address (
   id                   integer              not null default nextval('seq_web_address'),
   name                 VARCHAR(200)         not null,
   url                  varchar(1000)        not null,
   regular_model        varchar(1000)        null,
   flag                 INTEGER              null,
   parallelism          jsonb                null,
   type                 integer              null,
   constraint PK_WEB_ADDRESS primary key (id)
);

comment on table web_address is
'此表保存需要抓取的URL首页地址';

comment on column web_address.regular_model is
'保存当前网页内详细页面的模型地址
此为一个正则表达式，以确定相关详细地址如何获取';

comment on column web_address.flag is
'0:不可用
1:可用';

comment on column web_address.parallelism is
'指定获取内容与原内容对应，通过此值区分不同网站获取内容的方式
';

alter table acquire_original_data
   add constraint FK_ACQUIRE__REFERENCE_WEB_ADDR foreign key (web_address_id)
references web_address (id)
on delete restrict on update restrict;

alter table audit_data
   add constraint FK_AUDIT_DA_REFERENCE_CLEAN_UP foreign key (id)
references clean_up_data (id)
on delete restrict on update restrict;

alter table clean_up_data
   add constraint FK_CLEAN_UP_REFERENCE_ACQUIRE_ foreign key (id)
references acquire_original_data (id)
on delete restrict on update restrict;

