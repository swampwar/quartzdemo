package wind.yang.quartzdemo.service;

import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wind.yang.quartzdemo.dto.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class TriggerDetailService {
    @Autowired
    ExecProgService execProgService;

    @Autowired
    QuartzService quartzService;

    @Autowired
    JobGropService jobGropService;

    @Autowired
    JobService jobService;

    @Autowired
    JobDetailService jobDetailService;

    public TriggerJobInfo getTriggerJobInfo(String triggerGroup, String triggerName) {
        TriggerJobInfo triggerJobInfo = new TriggerJobInfo();
        JobResponse jobResponse = quartzService.readTrigger(triggerGroup, triggerName);
        List<ExecProg> execProgList = execProgService.findByTrigger(new TriggerKey(triggerName, triggerGroup));

        triggerJobInfo = triggerJobInfo.builder()
                .triggerGroup(jobResponse.getTriggerGroup())
                .triggerName1(jobResponse.getTriggerName())
                .triggerDescription(jobResponse.getDescription())
                .cronExpression(jobResponse.getCronExpression())
                .execProgInfoList(execProgList)
                .build();

        return triggerJobInfo;
    }

    public boolean insertTriggerDetail(TotalJobInfo totalJobInfo) {
        String insertType = totalJobInfo.getInsertType();
        boolean flag = true;

        String workDvsCd = totalJobInfo.getWorkDvsCd();
        if ("jobGroup".equals(insertType)) {
            if (totalJobInfo.getJobGroupId() == null || "".equals(totalJobInfo.getJobGroupId())) {
                totalJobInfo.setJobGroupId(jobGropService.makeJobGroupId(totalJobInfo.getWorkDvsCd()));
            }
            String triggerGroup = workDvsCd;
            String triggerName = totalJobInfo.getJobGroupId();
            String jobTime = totalJobInfo.getJobTime();

            TriggerKey triggerKey = new TriggerKey(triggerName, triggerGroup);

            try {
                TBIBM713 tbibm713 = execProgService.findProgByProgId(workDvsCd, totalJobInfo.getProgId());

                // Quartz Scheduler에 추가해준다.
                // 기존에 없는 trigger인 경우 추가해준다.
                if (quartzService.readTrigger(triggerGroup, triggerName) == null) {
                    JobRequest jobRequest = new JobRequest();
                    jobRequest = jobRequest.builder()
                            .triggerGroup(triggerGroup)
                            .triggerName(triggerName)
                            .cronExpression(jobTime)
                            .triggerDescription(tbibm713.getProgDesc())
                            .build();

                    log.debug("jobRequest : {}", jobRequest.toString());

                    quartzService.createJob(jobRequest);

                    //DB에 저장해준다
                    flag = insertJobGroup(totalJobInfo);
                }
                // 기존에 있는 trigger인 경우 update해준다.
                else {
                    quartzService.update(triggerKey, totalJobInfo.getJobTime());

                    /**TODO DB에 update하는 내용 추가필요*/
                }

            } catch (Exception e) {
                e.printStackTrace();
                flag = false;
            }
        }else if ("job".equals(totalJobInfo.getInsertType())) {
            if (totalJobInfo.getJobId() == null || "".equals(totalJobInfo.getJobId())) {
                // 신규 등록
                totalJobInfo.setJobId(jobService.makeJobId(totalJobInfo.getWorkDvsCd()));
                flag = insertJob(totalJobInfo);
            }else {
                /**TODO DB에 update하는 내용 추가필요*/
            }

        }else if ("jobDetail".equals(totalJobInfo.getInsertType())) {
            if (totalJobInfo.getHgrnJobId() != null && !"".equals(totalJobInfo.getHgrnJobId())) {
                totalJobInfo.setJobId(jobService.makeJobId(workDvsCd));
            }else {
                totalJobInfo.setHgrnJobId(null);
            }
            flag = insertJobDetail(totalJobInfo);
        }
        return flag;
    }

    public boolean insertJobGroup(TotalJobInfo totalJobInfo) throws Exception{
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");

        TBIBM710 tbibm710 = new TBIBM710(totalJobInfo.getWorkDvsCd(), totalJobInfo.getJobGroupId(), totalJobInfo.getProgId(),
                totalJobInfo.getJobTime(), (totalJobInfo.getJobSeq().equals("")) ? 0 : Integer.parseInt(totalJobInfo.getJobSeq()),
                totalJobInfo.getSttDt().replaceAll("-", ""), totalJobInfo.getEndDt().replaceAll("-", ""),
                totalJobInfo.getUseYn(), "quartz scheduler", format.format(date), "quartz scheduler", format.format(date));

        boolean flag = jobGropService.insertJobGroup(tbibm710);

        return flag;
    }

    public boolean insertJob(TotalJobInfo totalJobInfo) {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");

                TBIBM711 tbibm711 = new TBIBM711(totalJobInfo.getWorkDvsCd(), totalJobInfo.getJobGroupId(), totalJobInfo.getJobId(),
                totalJobInfo.getProgId(), totalJobInfo.getJobName(), (totalJobInfo.getJobSeq().equals("")) ? 0 : Integer.parseInt(totalJobInfo.getJobSeq()),
                totalJobInfo.getSttDt().replaceAll("-", ""), totalJobInfo.getEndDt().replaceAll("-", ""),
                totalJobInfo.getUseYn(), "quartz scheduler", format.format(date), "quartz scheduler", format.format(date));

        boolean flag = jobService.insertJob(tbibm711);

        return flag;
    }

    public boolean insertJobDetail(TotalJobInfo totalJobInfo) {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");

        TBIBM712 tbibm712 = new TBIBM712(totalJobInfo.getWorkDvsCd(), totalJobInfo.getJobId(), totalJobInfo.getHgrnJobId(),
                totalJobInfo.getProgId(), (totalJobInfo.getJobSeq().equals("")) ? 0 : Integer.parseInt(totalJobInfo.getJobSeq()), totalJobInfo.getJobProdDt(),
                totalJobInfo.getSttDt().replaceAll("-", ""), totalJobInfo.getEndDt().replaceAll("-", ""),
                totalJobInfo.getUseYn(), "quartz scheduler", format.format(date), "quartz scheduler", format.format(date));

        boolean flag = jobDetailService.insertJob(tbibm712);

        return flag;
    }

}
