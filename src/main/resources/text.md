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
- 실행이력 DB저장
  - 잡 실행결과 코드 : 시작(PROGRESS), 정상종료(COMPLETE), 에러(ERROR)
  - 트리거유형 코드 : 일반(NORMAL), 강제실행(FORCE)
- 테이블 구성
  - 트리거 : TB_TRIGGERS(트리거그룹, 트리거이름, 트리거유형)
  - 실행프로그램 : TB_EXEC_PROG(트리거그룹, 트리거이름, 시퀀스, 프로그램이름)
  - 작업이력 : TB_EXEC_HISTORY(작업시작, 작업종료, 트리거그룹, 트리거이름, 잡그룹, 잡이름, 작업상태코드, 실행프로그램, 실행결과)

## 미완료
- Scheduler Job 실행 부가기능
  - 실행중인 Job 강제종료
  - 실행된 Job 재실행 = 스케쥴링된 시간과 별개로 강제실행(긴급실행)
  - Job Chaining 실행(개별 Job의 실행 결과에 따라서 분기된 후속 처리)
- 불규칙한 수행일정을 위한 캘린더 스케쥴링 기능
- View GUI
  - 전체 Job(Trigger) 현황 대쉬보드
    - 실행예정, 실행중, 실행완료 상태확인
    - 실행완료 Job 로그 제공
  - Scheduler CRUD API 요청 및 응답
---------------------------------------------------------
- 사용자 관리, 권한제어 등
- Trigger CRUD에 대한 이력저장


# 짤짤이 TODO
- 쿼츠 기본 테이블 구성을 커스터마이징할 수 있는지 확인 -> 현재는 쿼츠가 자동으로 테이블 생성
- 아래 내용을 참고해서 컨트롤러 메서드 파라미터 밸리데이션
  http://dolszewski.com/spring/how-to-bind-requestparam-to-object/

# 짤짤이 정리
- 트리거의 상태는 실행중이 없다. 평상시는 NORMAL(WATING), PAUSE시 PAUSED
  NONE, NORMAL, PAUSED, COMPLETE, ERROR, BLOCKED
- DefaultExcutor는 비동기로 Job을 실행한다. 데몬형태로 실행하는 DaemonExecutor가 있다.
- WatchDog에 의해 실행중인 Shell이 종료될 때는 143 에러코드를 출력한다.
  Exit Code 143: Indicates failure as container received SIGTERM
- Job에서 발생하는 에러는 JobExecutionException으로 핸들링 할 수 있다.
- Job 실행순서 : QuartzSchedulerThread -> JobRunShell ->
- 실행순서 : triggerFired -> vetoJobExecution -> jobToBeExecuted -> Job실행 -> jobWasExecuted -> triggerComplete
- 잡이 싱글톤으로 생성되는지? 매번 새로운 객체가 생성되는지?
- 트리거를 스케쥴링된 시간 외에 강제로 실행하는 방법은 없는것으로 보인다.
