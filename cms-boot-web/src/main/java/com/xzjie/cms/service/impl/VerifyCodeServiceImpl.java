package com.xzjie.cms.service.impl;

import com.xzjie.cms.core.event.EmailEvent;
import com.xzjie.cms.core.service.AbstractService;
import com.xzjie.cms.core.utils.MapUtils;
import com.xzjie.cms.enums.VerifyCodeScenes;
import com.xzjie.cms.enums.VerifyCodeType;
import com.xzjie.cms.model.VerifyCode;
import com.xzjie.cms.repository.VerifyCodeRepository;
import com.xzjie.cms.service.VerifyCodeService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class VerifyCodeServiceImpl extends AbstractService<VerifyCode, VerifyCodeRepository> implements VerifyCodeService {
    @Value("${code.expiration}")
    private Integer expiration;

    @Autowired
    private Configuration configuration;
    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public boolean update(VerifyCode obj) {
        return false;
    }

    @Override
    public VerifyCode save(String target, String value, VerifyCodeScenes scenes, VerifyCodeType type, String message) {
        VerifyCode model = new VerifyCode();
        model.setCreateDate(LocalDateTime.now());
        model.setMessage(message);
        model.setType(type);
        model.setScenes(scenes);
        model.setValue(value);
        model.setTarget(target);
        model.setState(1);
        return baseRepository.save(model);
    }

    @Override
    public void sendMail(String email) {
        String code = RandomStringUtils.randomNumeric(6);
        String subject = VerifyCodeScenes.EMAIL_CHANGE.getName();
        String content = null;
        try {
            Template template = configuration.getTemplate("email/reset_email.html");
            StringWriter result = new StringWriter();
            template.process(MapUtils.create().set("code", code), result);
            content = result.toString(); // FreeMarkerTemplateUtils.processTemplateIntoString(template,)
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }

        VerifyCode verifyCode = this.save(email, code, VerifyCodeScenes.EMAIL_CHANGE, VerifyCodeType.EMAIL, null);

        applicationContext.publishEvent(EmailEvent.EmailEventBuilder
                .builder()
                .setSubject(subject)
                .setContent(content)
                .setTo(email)
                .build());
//        timedDestruction(verifyCode);
    }

    @Override
    public boolean validated(String code, String email, VerifyCodeScenes scenes, VerifyCodeType type) {

        VerifyCode verifyCode = baseRepository.findByTargetAndAndScenesAndType(email, scenes, type);
        if (verifyCode == null || !verifyCode.getValue().equals(code)) {
            return false;
        }
        verifyCode.setState(0);
        baseRepository.save(verifyCode);
        return true;
    }

    @Override
    public List<VerifyCode> getVerifyCodeExpiration() {
        return baseRepository.findVerifyCode(expiration);
    }

    /**
     * 定时任务，指定分钟后改变验证码状态
     *
     * @param verifyCode 验证码
     */
    private void timedDestruction(VerifyCode verifyCode) {
        //以下示例为程序调用结束继续运行
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        try {
            executorService.schedule(() -> {
                verifyCode.setState(0);
                baseRepository.save(verifyCode);
            }, expiration * 60 * 1000L, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
