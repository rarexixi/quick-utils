package org.xi.quick.webapi.controller;

import org.springframework.web.bind.annotation.*;
import org.xi.quick.utils.image.CaptchaUtils;
import org.xi.quick.webapi.model.GetVo;
import org.xi.quick.webapi.model.JsonResult;
import org.xi.quick.webapi.model.PostVo;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class HomeController {

    @RequestMapping(value = "get", method = RequestMethod.GET)
    public JsonResult<String> get(GetVo model) {
        return new JsonResult<>(model.getName());
    }

    @RequestMapping(value = "post", method = RequestMethod.POST)
    public JsonResult<String> post(PostVo model) {
        return new JsonResult<>(model.getName());
    }


    @RequestMapping("/captcha.jpg")
    public void getCaptchaImage(HttpServletResponse response,
                                @RequestParam(value = "width", defaultValue = "150") Integer width,
                                @RequestParam(value = "height", defaultValue = "40") Integer height) throws IOException {

        response.setContentType("image/jpeg");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        CaptchaUtils.outputCaptchaImage(width, height, response.getOutputStream(), 6);
    }
}
