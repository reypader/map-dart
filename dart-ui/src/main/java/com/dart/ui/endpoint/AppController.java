package com.dart.ui.endpoint;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by rpader on 5/2/16.
 */
@Controller
public class AppController {

    @RequestMapping(value = "/{[path:[^\\.]*}")
    public String forward() {
        return "forward:/";
    }
}
