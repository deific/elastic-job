package com.myzh.dpc.console.utils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;

public class SignUtil {
    private static final String signKey = "TNtAuyUnudvD7l2U3i4zTw";
    private static final String KEY_SIGN = "sign";
    private static final String KEY_TIME = "time";
    private static final Set<String> KeySet = new HashSet<String>() {{
        add("action");
        add("source");
        add("frduID");
        add("bizID");
        add("friendId");
        add("merchantId");
        add("streetId");
        add("areaId");
        add("couponId");
        add("time");
    }};

    public static String signUrl(String url) {
        if (StringUtils.isBlank(url)) return null;

        String time = DateUtil.unixTimestamp().toString();
        TreeMap<String, String> params = url2map(url);
        params.put(KEY_TIME, time);
        return "&time=" + time + "&sign=" + sign(params);
    }

    public static boolean verifySign(String url) {
        return verifySign(url2map(url));
    }

    public static boolean verifySign(TreeMap<String, String> map) {
        return sign(map).equals(map.get(KEY_SIGN));
    }

    private static String sign(TreeMap<String, String> params) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (!KeySet.contains(entry.getKey())) continue;
            sb.append(entry.getKey()).append("=").append(entry.getValue());
        }
        sb.append(signKey);

        return DigestUtil.md5(sb.toString());
    }

    public static TreeMap<String, String> queryString2Map(String query) {
        if (query.contains("8900eeea==d52680f46c54cf4cab6e2280cffd279709b820e63cfe67eb74a7")) {
            query = query.replace("8900eeea==d52680f46c54cf4cab6e2280cffd279709b820e63cfe67eb74a7", "8900eeea0101d52680f46c54cf4cab6e2280cffd279709b820e63cfe67eb74a7");
        }

        TreeMap<String, String> map = new TreeMap<>();
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            String[] entry = pair.split("=");
            if (entry.length == 2)
                map.put(entry[0], entry[1]);
            else if (entry.length == 1)
                map.put(entry[0], "");
        }
        return map;
    }

    private static TreeMap<String, String> url2map(String url) {
        String query = url.substring(url.indexOf("?") + 1);
        return queryString2Map(query);
    }
}
