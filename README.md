# 쿼츠 데모 프로젝트

## 사용 기술
- SpringBoot + Quartz + Postgresql + Mybatis + Thymeleaf

## 프로젝트 실행 단계
### 사전준비
1. IDE는 Self

2. 로컬PC내 Docker + Postgresql 설정  
Docker 설치 및 Postgresql 이미지를 다운로드 후 아래 스크립트를 이용하여 기동

```shell script
# QScheduler Docker
docker volume create QSchedulerData # volume 생성

docker run -d -p 5432:5432 \
 -e POSTGRES_PASSWORD=1234 -e POSTGRES_USER=cns -e POSTGRES_DB=QSchedulerDB \
 --name QSchedulerCtn \
 -v QSchedulerData:/var/lib/postgresql/data \
 postgres

docker exec -i -t QSchedulerCtn bash

# Postgresql Container
psql -U cns -d QSchedulerDB # CLI 접속
```

### 프로젝트 실행
1. 프로젝트 import
2. application.yml 설정파일 수정
   - spring.datasource 설정이 위 DB설정과 일치하는지 확인
   - quartzdemo.shell 이하 설정을 본인로컬PC에 맞게 수정
3. 실행 프로그램(TB_EXEC_PROG) 초기데이터 적재
   - INSERT INTO tb_exec_prog (trigger_group, trigger_name, seq, program_name) VALUES ('MGT','MGT_TRIGGER1','1','trigger1.sh');
4. QuartzdemoApplication.java 파일 Run

