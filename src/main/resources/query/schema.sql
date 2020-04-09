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
    use_yn varchar
);

alter table tb_trigger_group owner to cns;

comment on table tb_trigger_group is '트리거그룹';


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