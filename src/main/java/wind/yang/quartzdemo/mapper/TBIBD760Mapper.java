package wind.yang.quartzdemo.mapper;

import wind.yang.quartzdemo.dto.TBIBD760;

import java.util.List;


public interface TBIBD760Mapper {
    void insertExecHistory(TBIBD760 tbibd760);

    void updateExecHistory(TBIBD760 tbibd760);

    int checkJobHistory(TBIBD760 tbibd760);

    TBIBD760 checkJobHistoryMaster(TBIBD760 master);

    List<TBIBD760> findDetailAll(String startDate, String endDate, String settmWorkDvsCd, String settmJobGroupId);

    TBIBD760 findLastExecHistory(TBIBD760 tbibd760);

    List<TBIBD760> findByExecHistory(TBIBD760 searchParam);

//    TBIBD760 findByExecHistory(TBIBD760 tbibd760);

    TBIBD760 findLastStartExecHistory(TBIBD760 tbibd760);
}
