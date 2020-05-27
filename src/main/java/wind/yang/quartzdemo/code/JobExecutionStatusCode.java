package wind.yang.quartzdemo.code;

public enum JobExecutionStatusCode {
    READY("준비", "실행 준비완료"),
    DUPLICATE("중복실행", "트리거 중복실행으로 실행되지 않음"),
    START("시작", "실행 시작"),
    SUCCESS("완료", "실행 완료 :)"),
    ERROR("에러", "실행중 에러발생"),
    SKIP("생략", "실행 생략");

    private final String name;
    private final String msg;

    JobExecutionStatusCode(String name, String msg) {
        this.name = name;
        this.msg = msg;
    }

    public String getName() {
        return name;
    }

    public String getMsg() {
        return msg;
    }
}
