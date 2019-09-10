package com.jinnjo.sale.utils;


import com.jinnjo.base.util.DateFormatUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ParaFormatUtil {

    public static Map<String, String> findObjListByFilter(String filter) {
        Map<String, String> paramsMap = new HashMap<>();
       if(StringUtils.isNotEmpty(filter)){
           String[] filters = filter.split("\\|");
           for (String filter1 : filters) {
               String[] keyValues = filter1.split("::");
               if (keyValues.length == 2) {
                   paramsMap.put(keyValues[0], keyValues[1]);
               }
           }
           System.out.println(paramsMap.toString());
       }
        return paramsMap;
    }

    public  static Date convertDateStr2Date(String dateStr){
        Date date=null;
        if(StringUtils.isNotEmpty(dateStr)){
            try {
                date = DateFormatUtil.parse(dateStr,"yyyy-MM-dd+HH:mm:ss");
            } catch (ParseException e) {
                log.info(dateStr+"转换yyyy-MM-dd+HH:mm:ss出错");
            }
            try {
                if(null==date){
                    date = DateFormatUtil.parse(dateStr,"yyyy-MM-dd HH:mm:ss");
                }
            } catch (ParseException e) {
                log.info(dateStr+"转换yyyy-MM dd+HH:mm:ss出错");
            }
        }
        return date;
    }
}
