package com.jinnjo.sale.utils;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 功能描述
 *
 * @author subingbing
 * 2018/8/23
 */
@Component
public class LinkTools {
    /**
     * 取字段_links
     * @return
     */
    public static String getLinks(Map map) {
        if (null != map && map.size() > 0) {
            Map links = (Map) map.get("_links");
            if (null != links) {
                Map self = (Map) links.get("self");
                if (null != self) {
                    if(null != self.get("href")){
                        return self.get("href").toString();
                    }
                }
            }
        }
        return null;
    }

    /**
     * 取字段_links下的content
     * @return
     */
    public static String getContent(Map map) {
        if (null != map && map.size() > 0) {
            List links = (List) map.get("links");
            if (null != links) {
                Map res = (Map)links.get(1);
                return (String) res.get("href");
            }
        }
        return null;
    }

}
