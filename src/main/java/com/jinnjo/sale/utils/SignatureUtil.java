package com.jinnjo.sale.utils;

import com.jinnjo.base.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.*;


@Component
@Slf4j
public class SignatureUtil {

    private static final String secretKey = "7713393c8c1948e85220ee56e2b29052";

    public static String signParams(Object vo) {
        Field[] fields = vo.getClass().getDeclaredFields();
        List<String> values = new ArrayList<>();
        for (Field field : fields) {
            field.setAccessible(true);
            if (StringUtil.isEmpty(field.getName()) || "sign".equals(field.getName())|| "loginPush".equals(field.getName()) ||  "isAll".equals(field.getName()) ) {
                continue;
            }
            try {
                if (field.get(vo) != null) {
                    values.add(field.get(vo).toString());
                }
            } catch (Exception ignored) {

            }
        }
        Collections.sort(values);
        StringBuilder sb = new StringBuilder();
        for (String str : values) {
            if (StringUtil.isEmpty(str)) {
                continue;
            }
            sb.append(str);
        }
        sb.append(secretKey);
        log.info("sb.toString()========="+sb.toString());
        return DigestUtils.md5Hex(sb.toString());
    }

}
