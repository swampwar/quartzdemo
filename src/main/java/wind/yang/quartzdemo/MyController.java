package wind.yang.quartzdemo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MyController {
    @RequestMapping("/hhh")
    public String hhh(){
        return "hhh";
    }
}
