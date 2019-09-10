package com.jinnjo.sale.utils;

import com.jinnjo.base.util.BeanUtils;
import com.jinnjo.base.util.StringUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;


public class QueryParamsUtil {

    public static String getBenginDate(String filterDate){
        List<String> result = getDateArray(filterDate);
        if(BeanUtils.isEmpty(result)){
            return "";
        }
        return result.get(0);
    }


    public static String getEndDate(String filterDate){
        List<String> result = getDateArray(filterDate);
        if(BeanUtils.isEmpty(result)){
            return "";
        }
        return result.get(1);
    }


    private static List<String> getDateArray(String filterDate){
        if(StringUtil.isEmpty(filterDate)){
            return null;
        }
        List<String> result = new ArrayList<>();
        int indexCount = StringUtils.indexOf(filterDate,",");
        String beginDate = StringUtils.substring(filterDate,0,indexCount);
        String endDate = StringUtils.substring(filterDate,indexCount+1,filterDate.length());
        result.add(beginDate);
        result.add(endDate);
        return result;
    }
}
