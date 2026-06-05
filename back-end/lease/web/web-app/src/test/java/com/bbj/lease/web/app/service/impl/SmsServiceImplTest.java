package com.bbj.lease.web.app.service.impl;

import com.bbj.lease.web.app.service.SmsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SmsServiceImplTest {

    @Autowired
    private SmsService service;

    @Test
    void sendSms() {
        service.sendSms("17701326512", "123456");
    }

}