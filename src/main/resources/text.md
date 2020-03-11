# 쿼츠 생성 테이블
```text
SELECT * FROM QRTZ_BLOB_TRIGGERS;
SELECT * FROM QRTZ_CALENDARS;
SELECT * FROM QRTZ_CRON_TRIGGERS;
SELECT * FROM QRTZ_FIRED_TRIGGERS; 
SELECT * FROM QRTZ_JOB_DETAILS;
SELECT * FROM QRTZ_LOCKS;
SELECT * FROM QRTZ_PAUSED_TRIGGER_GRPS;
SELECT * FROM QRTZ_SCHEDULER_STATE;
SELECT * FROM QRTZ_SIMPLE_TRIGGERS;
SELECT * FROM QRTZ_SIMPROP_TRIGGERS; 
SELECT * FROM QRTZ_TRIGGERS; 
```

# 기능
## 완료
- Scheduler CRUD API
  - Job 등록 : POST /scheduler/job
  - Job 조회 : GET /scheduler/jobs
  - Job 변경(일시정지) : PUT /scheduler/job/pause
  - Job 변경(재시작) : PUT /scheduler/job/resume
  - Job 삭제 : DELETE /scheduler/job
- Scheduler Job 실행

## 미완료
- Scheduler Job 실행 부가기능
  - 실행중인 Job 강제종료
  - 실행된 Job 재실행
- View
  - 전체 Job(Trigger) 현황 대쉬보드
    - 실행예정, 실행중, 실행완료 상태확인
    - 실행완료 Job 로그 제공
  - Scheduler CRUD API 요청 및 응답

# 짤짤이 TODO
- Trigger별 실행 쉘스크립트를 DB로 관리 -> 현재는 Map으로 메모리에 저장
- 쿼츠 기본 테이블 구성을 커스터마이징할 수 있는지 확인 -> 현재는 쿼츠가 자동으로 테이블 생성
- 아래 내용을 참고해서 컨트롤러 메서드 파라미터 밸리데이션
  http://dolszewski.com/spring/how-to-bind-requestparam-to-object/

# 짤짤이 정리
- 트리거의 상태는 실행중이 없다. 평상시는 NORMAL(WATING), PAUSE시 PAUSED
  NONE, NORMAL, PAUSED, COMPLETE, ERROR, BLOCKED
- DefaultExcutor는 비동기로 Job을 실행한다. 데몬형태로 실행하는 DaemonExecutor가 있다.
- WatchDog에 의해 실행중인 Shell이 종료될 때는 143 에러코드를 출력한다.
  Exit Code 143: Indicates failure as container received SIGTERM







