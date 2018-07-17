package org.xi.quick.webapi.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.xi.quick.webapi.model.GetVo;
import org.xi.quick.webapi.model.JsonResult;
import org.xi.quick.webapi.model.PostVo;

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
}
