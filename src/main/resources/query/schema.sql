create table TB_EXEC_PROG
(
    TRIGGER_GROUP varchar,
    TRIGGER_NAME varchar,
    SEQ int,
    PROGRAM_NAME varchar,
    constraint TB_EXEC_PROG_pk
        primary key (TRIGGER_GROUP, TRIGGER_NAME, SEQ)
);

comment on table TB_EXEC_PROG is '실행프로그램';

create table tb_exec_history
(
    job_stt_dtm     varchar,
    job_end_dtm     varchar,
    trigger_group   varchar,
    trigger_name    varchar,
    job_group       varchar,
    job_name        varchar,
    job_exec_sta_cd varchar,
    exec_prog_name  varchar,
    job_exec_rslt   varchar,
    constraint tb_exec_history_pk
        primary key (job_stt_dtm, trigger_group, trigger_name, job_group, job_name)
);

comment on table tb_exec_history is '작업이력';

create table TB_TRIGGER
(
    TRIGGER_GROUP varchar,
    TRIGGER_NAME varchar,
    TRIGGER_TYPE varchar,
    constraint TB_TRIGGER_pk
        primary key (TRIGGER_GROUP, TRIGGER_NAME)
);

comment on table tb_trigger is '트리거';

