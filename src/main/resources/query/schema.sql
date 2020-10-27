create table tb_exec_history
(
    trigger_stt_dtm varchar not null,
    trigger_group varchar not null,
    trigger_name varchar not null,
    job_group varchar not null,
    job_name varchar not null,
    exec_prog_seq integer not null,
    exec_prog_name varchar,
    job_stt_dtm varchar,
    job_end_dtm varchar,
    job_exec_sta_cd varchar,
    job_exec_rslt varchar,
    exec_param1 varchar,
    exec_param2 varchar,
    exec_param3 varchar,
    exec_cmd varchar,
    summary varchar,
    constraint tb_exec_history_pk
        primary key (trigger_stt_dtm, trigger_group, trigger_name, job_group, job_name, exec_prog_seq)
);

comment on table tb_exec_history is '작업이력';

alter table tb_exec_history owner to cns;

create table tb_exec_prog
(
    trigger_group varchar not null,
    trigger_name varchar not null,
    seq integer not null,
    program_name varchar,
    summery varchar,
    discription varchar,
    exec_param1 varchar,
    exec_param2 varchar,
    exec_param3 varchar,
    summary varchar,
    description varchar,
    constraint tb_exec_prog_pk
        primary key (trigger_group, trigger_name, seq)
);

comment on table tb_exec_prog is '실행프로그램';

alter table tb_exec_prog owner to cns;

create table tb_trigger_group
(
    trigger_group varchar
        constraint tb_trigger_group_pk
            primary key,
    description varchar,
    use_yn varchar,
    rgt_dtm varchar(14),
    udt_dtm varchar(14)
);

alter table tb_trigger_group owner to cns;

comment on table tb_trigger_group is '트리거그룹';

create table TBFZD999
(
    JOB_VER_ID varchar(14)
        constraint TBFZD999_pk
        primary key,
    JOB_GROUP varchar(19),
    FULL_PATH varchar(200),
    STATUS_CD varchar(1),
    EXEC_PARAM1 varchar(100),
    EXEC_PARAM2 varchar(100),
    EXEC_PARAM3 varchar(100),
    RGT_DTM varchar(14),
    RGT_ID varchar(50),
    UDT_DTM varchar(14),
    UDT_ID varchar(50)
)

create table TBFZM990
(
    WORK_DVS_CD varchar(5) not null,
    JOB_GROUP_ID varchar(20) not null,
    PROG_ID varchar(20) not null,
    WORK_TIME varchar(100),
    WORK_SEQ varchar(3),
    STT_DT varchar(14),
    END_DT varchar(14),
    USE_YN varchar(1),
    REGIST_ID varchar(50),
    REGIST_DTIME varchar(14),
    UPDATE_ID varchar(50),
    UPDATE_DTIME varchar(14),
    constraint TBFZM990_pk
    primary key (WORK_DVS_CD, JOB_GROUP_ID, PROG_ID)
)

create table TBFZD991
(
    WORK_DVS_CD varchar(5) not null,
    JOB_GROUP_ID varchar(20) not null,
    JOB_ID varchar(20) not null,
    PROG_ID varchar(20) not null,
    JOB_NAME varchar(50),
    JOB_SEQ varchar(3),
    STT_DT varchar(14),
    END_DT varchar(14),
    USE_YN varchar(1),
    REGIST_ID varchar(50),
    REGIST_DTIME varchar(14),
    UPDATE_ID varchar(50),
    UPDATE_DTIME varchar(14),
    constraint TBFZD991_pk
    primary key (WORK_DVS_CD, JOB_GROUP_ID, JOB_ID, PROG_ID)
)

create table TBFZD992
(
    WORK_DVS_CD varchar(5) not null,
    JOB_ID varchar(20) not null,
    PROG_ID varchar(20) not null,
    JOB_SEQ varchar(3),
    HGRN_JOB_ID varchar(20),
    WORK_PROC_DT varchar(14),
    STT_DT varchar(14),
    END_DT varchar(14),
    USE_YN varchar(1),
    REGIST_ID varchar(50),
    REGIST_DTIME varchar(14),
    UPDATE_ID varchar(50),
    UPDATE_DTIME varchar(14),
    constraint TBFZD992_pk
    primary key (WORK_DVS_CD, JOB_ID, PROG_ID)
)

create table TBFZD993
(
    WORK_DVS_CD varchar(5) not null,
    PROG_ID varchar(20) not null,
    PROG_DVS_CD varchar(3),
    PROG_NM varchar(20),
    PROG_DESC varchar(14),
    PROG_PATH varchar(14),
    END_DT varchar(14),
    USE_YN varchar(1),
    REGIST_ID varchar(50),
    REGIST_DTIME varchar(14),
    UPDATE_ID varchar(50),
    UPDATE_DTIME varchar(14),
    constraint TBFZD993_pk
    primary key (WORK_DVS_CD, PROG_ID)
)

create table TBIBM714
(
    SETTM_WORK_DVS_CD varchar(5) not null,
    PRST_ID varchar(14) not null,
    PRST_DESC varchar(4000),
    USE_YN varchar(1),
    REGIST_ID varchar(20),
    REGIST_DTIME varchar(14),
    UPDATE_ID varchar(20),
    UPDATE_DTIME varchar(14),
    constraint TBIBM714_pk
    primary key (SETTM_WORK_DVS_CD, PRST_ID)
)

create table TBIBM715
(
    SETTM_WORK_DVS_CD varchar(5) not null,
    PRST_ID varchar(14) not null,
    PRMT_ID varchar(14) not null,
    SETTM_JOB_ID varchar(14),
    PROG_ID varchar(14),
    PRMT_VAL varchar(50),
    PRMT_DESC varchar(4000),
    PRMT_SEQ int default 0,
    USE_YN varchar(1),
    REGIST_ID varchar (20),
    REGIST_DTIME varchar(14),
    UPDATE_ID varchar(20),
    UPDATE_DTIME varchar(14),
    constraint TBIBM715_pk
    primary key (SETTM_WORK_DVS_CD, PRST_ID, PRMT_ID)
)

create table TBIBD760
(
    SETTM_WORK_DVS_CD varchar(5) not null,
    SETTM_JOB_GROUP_ID varchar(14) not null,
    SETTM_JOB_ID varchar(14) not null,
    PROG_ID varchar(14) not null,
    SYST_AREA_CLASS_CD varchar(5) not null,
    WORK_DT varchar(8) not null,
    WORK_SEQ int default 0,
    WORK_STT_DTIME varchar(14),
    WORK_END_DTIME varchar(14),
    WORK_RESULT_CD varchar(2),
    WORK_DESC varchar(4000),
    REGIST_ID varchar (20),
    REGIST_DTIME varchar(14),
    UPDATE_ID varchar(20),
    UPDATE_DTIME varchar(14),
    constraint TBIBD760_pk
    primary key (SETTM_WORK_DVS_CD, SETTM_JOB_GROUP_ID, SETTM_JOB_ID,
    PROG_ID, SYST_AREA_CLASS_CD, WORK_DT)
)






INSERT INTO tb_exec_prog
    (trigger_group, trigger_name, seq, program_name, exec_param1, exec_param2, exec_param3)
VALUES
    ('MGT','MGT_TRIGGER1',1,'trigger1.sh','param1','param2','param3');

INSERT INTO tb_exec_prog
    (trigger_group, trigger_name, seq, program_name, exec_param1, exec_param2, exec_param3)
VALUES
    ('MGT','MGT_TRIGGER1',2,'trigger2.sh','param4','param5','param6');

INSERT INTO tb_exec_prog
    (trigger_group, trigger_name, seq, program_name, exec_param1, exec_param2, exec_param3)
VALUES
    ('MGT','MGT_TRIGGER1',3,'trigger3.sh','param7','param8','param9');

INSERT INTO tb_exec_prog
(trigger_group, trigger_name, seq, program_name, exec_param1, exec_param2, exec_param3)
VALUES
('MGT','MGT_TRIGGER2',1,'trigger4.sh','param1','param2','param3');

INSERT INTO tb_exec_prog
(trigger_group, trigger_name, seq, program_name, exec_param1, exec_param2, exec_param3)
VALUES
('MGT','MGT_TRIGGER2',2,'trigger5.sh','param4','param5','param6');

INSERT INTO tb_exec_prog
(trigger_group, trigger_name, seq, program_name, exec_param1, exec_param2, exec_param3)
VALUES
('MGT','MGT_TRIGGER2',3,'trigger6.sh','param7','param8','param9');