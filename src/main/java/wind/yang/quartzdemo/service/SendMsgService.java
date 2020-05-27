package wind.yang.quartzdemo.service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import wind.yang.quartzdemo.dto.PushMessage;

@Slf4j
@Service
public class SendMsgService {

    @Value("${slack.url.mgt}")
    private String sendUrlMgt;

    @Value("${slack.url.settm}")
    private String sendUrlSettm;

//    @Value("${fcm.key-path}")
//    private String FIREBASE_KEY_PATH;

    @Autowired
    private RestTemplate restTemplate;

    // FCM 전송 INIT
//    @PostConstruct
//    public void init(){
//        try {
//            FirebaseOptions options = new FirebaseOptions.Builder()
//                    .setCredentials(GoogleCredentials.fromStream(new ClassPathResource(FIREBASE_KEY_PATH).getInputStream())).build();
//            if (FirebaseApp.getApps().isEmpty()) {
//                FirebaseApp.initializeApp(options);
//                log.info("Firebase Init Successs");
//            }
//        } catch (IOException e) {
//            log.error("FireBase Init Error : " + e.getMessage());
//        }
//    }
//
//    public void notifyWebPush(final PushMessage pushMessage) throws InterruptedException, ExecutionException {
//        Message message = Message.builder()
//                .setToken("")
//                .setWebpushConfig(WebpushConfig.builder().putHeader("ttl", "300")
//                        .setNotification(new WebpushNotification(pushMessage.toSimpleSlackTitle(),
//                                pushMessage.toSimpleSlackMessage()))
//                        .build())
//                .build();
//
//        String response = FirebaseMessaging.getInstance().sendAsync(message).get();
//        log.info("Sent message: " + response);
//    }

    // SLACK 메세지 전송
    // 추가적인 포맷 작성 가능
    // https://api.slack.com/tools/block-kit-builder?mode=message&blocks=%5B%7B%22type%22%3A%22section%22%2C%22block_id%22%3A%22section789%22%2C%22fields%22%3A%5B%7B%22type%22%3A%22mrkdwn%22%2C%22text%22%3A%22*Average%20Rating*%5Cn1.0%22%7D%5D%7D%2C%7B%22type%22%3A%22divider%22%7D%2C%7B%22type%22%3A%22context%22%2C%22elements%22%3A%5B%7B%22type%22%3A%22mrkdwn%22%2C%22text%22%3A%22Last%20updated%3A%20Jan%201%2C%202019%22%7D%5D%7D%5D
    // attachment 기능 추가
    public boolean notifySlack(PushMessage message) {
        log.debug("Notify[channel: {}, message: {}]", message.getChannel(), message);
        String sendUrl = "";
        try {
            if(message.getTriggerGroup().equals("MGT"))
                sendUrl = sendUrlMgt;
            else if(message.getTriggerGroup().equals("STL"))
                sendUrl = sendUrlSettm;
            else if(message.getTriggerGroup().equals("BUS"))
                sendUrl = sendUrlMgt;

            System.out.println("sendUrl : " + sendUrl);
            restTemplate.postForEntity(sendUrl, message.getMessageEntity(), String.class);
            return true;
        } catch (Exception e) {
            log.error("Exception:", e);
            return false;
        }
    }

}
