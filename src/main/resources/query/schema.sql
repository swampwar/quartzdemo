create table TB_EXEC_PROG
(
    TRIGGER_GROUP varchar,
    TRIGGER_NAME varchar,
    SEQ int,
    PROGRAM_NAME varchar,
    constraint TB_EXEC_PROG_pk
        unique (TRIGGER_GROUP, TRIGGER_NAME, SEQ)
);
