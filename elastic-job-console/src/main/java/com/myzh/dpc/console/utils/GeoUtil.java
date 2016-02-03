package com.myzh.dpc.console.utils;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;


public class GeoUtil {

    private static final double PI = 3.14159265;
    private static final double EARTH_RADIUS = 6378137;
    private static final double RAD = Math.PI / 180.0;




    public static void main(String[] args) {
        int distance = 1000;

        double lon = 116.348511;
        double lat = 39.929028;

        double[] point = GeoUtil.getEastInnerSquareCenterPoint(lon, lat, distance);
        System.out.println("result:" + point[1] + "," + point[0] + "  distance=" + GeoUtil.getDistance(lon, lat, point[1], point[0]));

//        Map<String, double[]> squarePoint = GeoUtil.getOuterSquareAroundPoint(lon, lat, distance);
//        System.out.println("正方形： " );
//        Iterator iterator= squarePoint.keySet().iterator();
//        String key = "";
//        while(iterator.hasNext()) {
//            key = (String) iterator.next();
//            double[] point = squarePoint.get(key);
//            double distance1 = GeoUtil.getDistance(116.348511,39.929028,point[1],point[0]);
//            System.out.println(key + ":" + point[1] + "," + point[0] + "  distance=" + distance1);
//        }



    }
    //@see http://snipperize.todayclose.com/snippet/php/SQL-Query-to-Find-All-Retailers-Within-a-Given-Radius-of-a-Latitude-and-Longitude--65095/
    //The circumference of the earth is 24,901 miles.
    //24,901/360 = 69.17 miles / degree
    /**
     * @param raidus 单位米
     * return minLat,minLng,maxLat,maxLng
     */
    public static double[] getAround(double lat,double lon,int radius){


        Double longitude = lon;
        Double latitude = lat;

        Double degree = (24901*1609)/360.0;
        double radiusMile = radius;

        Double dpmLat = 1/degree;
        Double radiusLat = dpmLat*radiusMile;
        Double minLat = latitude - radiusLat;
        Double maxLat = latitude + radiusLat;

        Double mpdLng = degree*Math.cos(latitude * (PI/180));
        Double dpmLng = 1 / mpdLng;
        Double radiusLng = dpmLng*radiusMile;
        Double minLng = longitude - radiusLng;
        Double maxLng = longitude + radiusLng;
        //System.out.println("["+minLat+","+minLng+","+maxLat+","+maxLng+"]");
        return new double[]{minLat,minLng,maxLat,maxLng};
    }

    /**
     * 计算经纬度点对应外切正方形4个点的坐标
     *
     * @param longitude
     * @param latitude
     * @param distance
     * @return
     */
    public static Map<String, double[]> getOuterSquareAroundPoint(double longitude,
                                                                  double latitude, double distance) {
        Map<String, double[]> squareMap = new HashMap<String, double[]>();
        // 计算经度弧度,从弧度转换为角度
        double dLongitude = 2 * (Math.asin(Math.sin(distance
                / (2 * EARTH_RADIUS))
                / Math.cos(Math.toRadians(latitude))));
        dLongitude = Math.toDegrees(dLongitude);
        // 计算纬度角度
        double dLatitude = distance / EARTH_RADIUS;
        dLatitude = Math.toDegrees(dLatitude);

        // 正方形
        double[] leftTopPoint = { latitude + dLatitude, longitude - dLongitude };
        double[] rightTopPoint = { latitude + dLatitude, longitude + dLongitude };
        double[] leftBottomPoint = { latitude - dLatitude,
                longitude - dLongitude };
        double[] rightBottomPoint = { latitude - dLatitude,
                longitude + dLongitude };
        squareMap.put("leftTopPoint", leftTopPoint);
        squareMap.put("rightTopPoint", rightTopPoint);
        squareMap.put("leftBottomPoint", leftBottomPoint);
        squareMap.put("rightBottomPoint", rightBottomPoint);
        return squareMap;
    }

    /**
     * 计算经纬度点对应内切正方形4个点的坐标
     *
     * @param longitude
     * @param latitude
     * @param distance
     * @return
     */
    public static Map<String, double[]> getInterSquareAroundPoint(double longitude,
                                                                  double latitude, double distance) {
        double interDistance = distance / 2;
        return getOuterSquareAroundPoint(longitude, latitude, distance);
    }

    /**
     * 计算经纬度点和距离计算对应外切西侧正方形中心点坐标
     *
     * @param longitude
     * @param latitude
     * @param distance
     * @return
     */
    public static double[] getWestOuterSquareCenterPoint(double longitude,
                                                         double latitude, double distance) {
        // 计算经度弧度,从弧度转换为角度
        double dLongitude = 2 * (Math.asin(Math.sin(distance
                / (2 * EARTH_RADIUS))
                / Math.cos(Math.toRadians(latitude))));
        dLongitude = Math.toDegrees(dLongitude);

        // 正方形
        double[] leftOuterSquareCenterPoint = {latitude, longitude - dLongitude * 2};
        return leftOuterSquareCenterPoint;
    }
    /**
     * 计算经纬度点和距离计算对应外切东侧正方形中心点坐标
     *
     * @param longitude
     * @param latitude
     * @param distance
     * @return
     */
    public static double[] getEastOuterSquareCenterPoint(double longitude,
                                                         double latitude, double distance) {
        // 计算经度弧度,从弧度转换为角度
        double dLongitude = 2 * (Math.asin(Math.sin(distance
                / (2 * EARTH_RADIUS))
                / Math.cos(Math.toRadians(latitude))));
        dLongitude = Math.toDegrees(dLongitude);

        // 正方形
        double[] leftOuterSquareCenterPoint = {latitude, longitude + dLongitude * 2};
        return leftOuterSquareCenterPoint;
    }

    /**
     * 计算经纬度点和距离计算对应外切北侧正方形中心点坐标
     *
     * @param longitude
     * @param latitude
     * @param distance
     * @return
     */
    public static double[] getNorthOuterSquareCenterPoint(double longitude,
                                                          double latitude, double distance) {
        // 计算纬度角度
        double dLatitude = distance / EARTH_RADIUS;
        dLatitude = Math.toDegrees(dLatitude);

        // 正方形
        double[] leftOuterSquareCenterPoint = {latitude + dLatitude * 2, longitude};
        return leftOuterSquareCenterPoint;
    }
    /**
     * 计算经纬度点和距离计算对应外切南侧正方形中心点坐标
     *
     * @param longitude
     * @param latitude
     * @param distance
     * @return
     */
    public static double[] getSouthOuterSquareCenterPoint(double longitude,
                                                          double latitude, double distance) {
        // 计算纬度角度
        double dLatitude = distance / EARTH_RADIUS;
        dLatitude = Math.toDegrees(dLatitude);

        // 正方形
        double[] leftOuterSquareCenterPoint = {latitude - dLatitude * 2, longitude};
        return leftOuterSquareCenterPoint;
    }
    /**
     * 计算经纬度点和距离计算对应内切西侧正方形中心点坐标
     *
     * @param longitude
     * @param latitude
     * @param distance
     * @return
     */
    public static double[] getWestInnerSquareCenterPoint(double longitude,
                                                         double latitude, double distance) {
        return getWestOuterSquareCenterPoint(longitude, latitude, distance / 2);
    }
    /**
     * 计算经纬度点和距离计算对应内切东侧正方形中心点坐标
     *
     * @param longitude
     * @param latitude
     * @param distance
     * @return
     */
    public static double[] getEastInnerSquareCenterPoint(double longitude,
                                                         double latitude, double distance) {
        return getEastOuterSquareCenterPoint(longitude, latitude, distance / 2);
    }
    /**
     * 计算经纬度点和距离计算对应内切北侧正方形中心点坐标
     *
     * @param longitude
     * @param latitude
     * @param distance
     * @return
     */
    public static double[] getNorthInnerSquareCenterPoint(double longitude,
                                                          double latitude, double distance) {
        return getNorthOuterSquareCenterPoint(longitude, latitude, distance / 2);
    }
    /**
     * 计算经纬度点和距离计算对应内切南侧正方形中心点坐标
     *
     * @param longitude
     * @param latitude
     * @param distance
     * @return
     */
    public static double[] getSouthInnerSquareCenterPoint(double longitude,
                                                          double latitude, double distance) {
        return getSouthOuterSquareCenterPoint(longitude, latitude, distance / 2);
    }
    /**
     * 转化为弧度(rad)
     * */
    private static double rad(double d)
    {
        return d * Math.PI / 180.0;
    }
    /**
     * 基于googleMap中的算法得到两经纬度之间的距离,计算精度与谷歌地图的距离精度差不多，相差范围在0.2米以下
     * @param lon1 第一点的精度
     * @param lat1 第一点的纬度
     * @param lon2 第二点的精度
     * @param lat3 第二点的纬度
     * @return 返回的距离，单位m
     * */
    public static double getDistance(double lon1,double lat1,double lon2, double lat2)
    {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lon1) - rad(lon2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2)+Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }

    public static class GeoHash {

        private static int numberBits = 6 * 5;
        final static char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
                '9', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'm', 'n', 'p',
                'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };

        final static HashMap<Character, Integer> lookup = new HashMap<Character, Integer>();
        static {
            int i = 0;
            for (char c : digits)
                lookup.put(c, i++);
        }

        public double[] decode(String geoHash) {
            StringBuilder buffer = new StringBuilder();
            for (char c : geoHash.toCharArray()) {

                int i = lookup.get(c) + 32;
                buffer.append( Integer.toString(i, 2).substring(1) );
            }

            BitSet longitudeSet = new BitSet();
            BitSet latitudeSet = new BitSet();

            //even bits
            int j =0;
            for (int i=0; i< numberBits*2;i+=2) {
                boolean isSet = false;
                if ( i < buffer.length() )
                    isSet = buffer.charAt(i) == '1';
                longitudeSet.set(j++, isSet);
            }

            //odd bits
            j=0;
            for (int i=1; i< numberBits*2;i+=2) {
                boolean isSet = false;
                if ( i < buffer.length() )
                    isSet = buffer.charAt(i) == '1';
                latitudeSet.set(j++, isSet);
            }
            //中国地理坐标：东经73°至东经135°，北纬4°至北纬53°
            double lon = decode(longitudeSet, 70, 140);
            double lat = decode(latitudeSet, 0, 60);

            return new double[] {lat, lon};
        }

        public static double decode(BitSet bs, double floor, double ceiling) {
            double mid = 0;
            for (int i=0; i<bs.length(); i++) {
                mid = (floor + ceiling) / 2;
                if (bs.get(i))
                    floor = mid;
                else
                    ceiling = mid;
            }
            return mid;
        }


        public static String encode(double lat, double lon) {
            BitSet latitudeSet = getBits(lat, 0, 60);
            BitSet longitudeSet = getBits(lon, 70, 140);
            StringBuilder buffer = new StringBuilder();
            for (int i = 0; i < numberBits; i++) {
                buffer.append( (longitudeSet.get(i))?'1':'0');
                buffer.append( (latitudeSet.get(i))?'1':'0');
            }
            return base32(Long.parseLong(buffer.toString(), 2));
        }

        private static BitSet getBits(double lat, double floor, double ceiling) {
            BitSet buffer = new BitSet(numberBits);
            for (int i = 0; i < numberBits; i++) {
                double mid = (floor + ceiling) / 2;
                if (lat >= mid) {
                    buffer.set(i);
                    floor = mid;
                } else {
                    ceiling = mid;
                }
            }
            return buffer;
        }

        public static String base32(long i) {
            char[] buf = new char[65];
            int charPos = 64;
            boolean negative = (i < 0);
            if (!negative)
                i = -i;
            while (i <= -32) {
                buf[charPos--] = digits[(int) (-(i % 32))];
                i /= 32;
            }
            buf[charPos] = digits[(int) (-i)];

            if (negative)
                buf[--charPos] = '-';
            return new String(buf, charPos, (65 - charPos));
        }

    }
}


