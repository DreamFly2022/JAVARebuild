package com.hef.rpcfx.demo.provider;

import com.hef.rpcfx.api.RpcfxRequest;
import com.hef.rpcfx.api.RpcfxResponse;
import com.hef.rpcfx.server.RpcfxInvoker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Date 2022/2/15
 * @Author lifei
 */
@SpringBootApplication
@RestController
public class RpcfxServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RpcfxServerApplication.class, args);
    }

    @Resource
    private RpcfxInvoker rpcfxInvoker;

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public RpcfxResponse call(@RequestBody RpcfxRequest rpcfxRequest) {
        RpcfxResponse rpcfxResponse = rpcfxInvoker.invoker(rpcfxRequest);
        return rpcfxResponse;
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test() {
        return "success";
    }
}
