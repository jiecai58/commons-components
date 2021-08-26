package com.util.coordinate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 常用地图转换工具类  提供了百度坐标（BD09）、国测局坐标（火星坐标，GCJ02）、和WGS84坐标系之间的转换
 * WGS84坐标系：即地球坐标系，国际上通用的坐标系   GPS坐标（Google Earth使用、或者GPS模块） 谷歌地图用此坐标
 * BD09坐标系：即百度坐标系，GCJ02坐标系经加密后的坐标系     百度坐标  百度地图API
 * GCJ02坐标系：即火星坐标系，WGS84坐标系经加密后的坐标系    腾讯搜搜地图API  阿里云地图API 高德MapABC地图API Google Map
 * @author caijie
 * @since 2021年08月11日13:35:16
 */
public class MapUtils {


    public static final double PI = 3.1415926535897932384626433832795;
    public static final double x_pi = PI * 3000.0 / 180.0;
    //  a: 卫星椭球坐标投影到平面地图坐标系的投影因子。
    private static final double a = 6378245.0;
    //  ee: 椭球的偏心率。
    private static final double ee = 0.00669342162296594323;

    /**
     * 百度坐标（BD09）转 GCJ02
     *
     * @param lon 百度经度
     * @param lat 百度纬度
     * @return GCJ02 坐标：[经度，纬度]
     */
    public static double[] transformBD09ToGCJ02(double lon, double lat) {
        double x = lon - 0.0065, y = lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
        double gcjLon = z * Math.cos(theta);
        double gcjLat = z * Math.sin(theta);
        return new double[]{gcjLon, gcjLat};
    }
    
    /**
     * GCJ-02坐标转百度BD-09坐标
     *
     * @param lon GCJ02 经度
     * @param lat GCJ02 纬度
     * @return 百度坐标：[经度，纬度]
     */
    public static double[] transformGCJ02ToBD09(double lon, double lat) {
        double z = Math.sqrt(lon * lon + lat * lat) + 0.00002 * Math.sin(lat * x_pi);
        double theta = Math.atan2(lat, lon) + 0.000003 * Math.cos(lon * x_pi);
        double bdLon = z * Math.cos(theta) + 0.0065;
        double bdLat = z * Math.sin(theta) + 0.006;
        return new double[]{bdLon, bdLat};
    }

    /**
     * GCJ02 转 WGS84
     *
     * @param lon 经度
     * @param lat 纬度
     * @return WGS84坐标：[经度，纬度]
     */
    public static double[] transformGCJ02ToWGS84(double lon, double lat) {
        if (outOfChina(lon, lat)) {
            return new double[]{lon, lat};
        }
        double[] wg = delta(lat, lon);
        wg[0] = lon - wg[0];
        wg[1] = lat - wg[1];
        return wg;
    }

    public static double[] delta(double lat, double lon) {
        double dLat = transformLat(lon - 105.0, lat - 35.0);
        double dlon = transformlon(lon - 105.0, lat - 35.0);
        double radLat = lat / 180.0 * PI;
        double magic = Math.sin(radLat);
        magic = 1 - ee * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * PI);
        dlon = (dlon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * PI);
        return new double[]{dlon, dLat};
    }

    /**
     * WGS84 坐标 转 GCJ02
     *
     * @param lon 经度
     * @param lat 纬度
     * @return GCJ02 坐标：[经度，纬度]
     */
    public static double[] transformWGS84ToGCJ02(double lon, double lat) {
        if (outOfChina(lon, lat)) {
            return new double[]{lon, lat};
        } else {

            double[] d = delta(lat, lon);
            double mglon = lon + d[0];
            double mgLat = lat + d[1];
            return new double[]{mglon, mgLat};
        }
    }
    
    /**
     * 百度坐标BD09 转 WGS84
     *
     * @param lon 经度
     * @param lat 纬度
     * @return WGS84 坐标：[经度，纬度]
     */
    public static double[] transformBD09ToWGS84(double lon, double lat) {
        double[] lonLat = transformBD09ToGCJ02(lon, lat);

        return transformGCJ02ToWGS84(lonLat[0], lonLat[1]);
    }

    /**
     * WGS84 转 百度坐标BD09
     *
     * @param lon 经度
     * @param lat 纬度
     * @return BD09 坐标：[经度，纬度]
     */
    public static double[] transformWGS84ToBD09(double lon, double lat) {
        double[] lonLat = transformWGS84ToGCJ02(lon, lat);

        return transformGCJ02ToBD09(lonLat[0], lonLat[1]);
    }

    /**
     *
     * @param lon x
     * @param lat y
     * @return double
     */
    private static double transformLat(double lon, double lat) {
        double ret = -100.0 + 2.0 * lon + 3.0 * lat + 0.2 * lat * lat + 0.1 * lon * lat + 0.2 * Math.sqrt(Math.abs(lon));
        ret += (20.0 * Math.sin(6.0 * lon * PI) + 20.0 * Math.sin(2.0 * lon * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(lat * PI) + 40.0 * Math.sin(lat / 3.0 * PI)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(lat / 12.0 * PI) + 320 * Math.sin(lat * PI / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    private static double transformlon(double lon, double lat) {
        double ret = 300.0 + lon + 2.0 * lat + 0.1 * lon * lon + 0.1 * lon * lat + 0.1 * Math.sqrt(Math.abs(lon));
        ret += (20.0 * Math.sin(6.0 * lon * PI) + 20.0 * Math.sin(2.0 * lon * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(lon * PI) + 40.0 * Math.sin(lon / 3.0 * PI)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(lon / 12.0 * PI) + 300.0 * Math.sin(lon / 30.0 * PI)) * 2.0 / 3.0;
        return ret;
    }

    /**
     * 判断坐标是否不在中国内
     *
     * @param lon 经度
     * @param lat 纬度
     * @return 坐标是否在国内
     */
    public static boolean outOfChina(double lon, double lat) {
        if (lon < 72.004 || lon > 137.8347){
            return true;
        }

        if (lat < 0.8293 || lat > 55.8271){
            return true;
        }
        return false;
    }





















    public static final double r2d = 57.2957795131;
    public static final double rearth = 6371006.84;

    /**
     * wgs84坐标转上海城市坐标
     * @param lat 维度
     * @param lon 经度
     * @return
     */
    public static Map<String, Double> wgs84Tosh(double lat, double lon) {
        double tolat = (31 + (14.0 + 7.55996 / 60.0) / 60.0) / r2d;
        double tolon = (121.0 + (28.0 + 1.80651 / 60.0) / 60) / r2d;

        double frlat = lat / r2d;
        double frlon = lon / r2d;
        double clatt = Math.cos(frlat);
        double clatf = Math.cos(tolat);
        double slatt = Math.sin(frlat);
        double slatf = Math.sin(tolat);
        double dlon = frlon - tolon;
        double cdlon = Math.cos(dlon);
        double sdlon = Math.sin(dlon);
        double cdist = slatf * slatt + clatf * clatt * cdlon;
        double temp = (clatt * sdlon) * (clatt * sdlon) + (clatf * slatt - slatf * clatt * cdlon) * (clatf * slatt - slatf * clatt * cdlon);
        double sdist = Math.sqrt(Math.abs(temp));

        double gcdist = 0.0;

        if ((Math.abs(sdist) > 1e-7) || (Math.abs(cdist) > 1e-7)) {
            gcdist = Math.atan2(sdist, cdist);
        }
        double sbrg = sdlon * clatt;
        double cbrg = (clatf * slatt - slatf * clatt * cdlon);

        if ((Math.abs(sbrg) > 1e-7) || (Math.abs(cbrg) > 1e-7)) {
            temp = Math.atan2(sbrg, cbrg);
            while (temp < 0) {
                temp = temp + 2 * PI;
            }
        }

        double hor = gcdist * rearth;
        double xx = hor * Math.sin(temp);
        double yy = hor * Math.cos(temp);

        Map<String,Double> model = new HashMap<String,Double>();

        model.put("lat", xx);
        model.put("lon", yy);

        return model;
    }


    public static final double m_pNorthMove = -3457000.0;
    public static final double m_pSHa = 6378245.0;
    public static final double m_pSHf = 298.3;
    public static final double m_pWGS84a = 6371006.84;
    public static final double m_pWGS84f = 298.25722356300003;
    /**
     * 上海城市坐标转WGS84坐标
     * @param East 经(东部)
     * @param North 纬(北部)
     * @param InH  内部高度(默认可以用 0.0)
     * @return
     */
    public static Map<String, Double> ShToWGS84(double East, double North, double InH) {

        North = North - m_pNorthMove;
        List<Double> a = AntiGaussProjectionConst(m_pSHa, m_pSHf, East, North);
        double rB = a.get(0);
        double rl = a.get(1);
        double m_pCenterL = DMSToDegree(121.0, 28.0, 0.0);
        double dL = RadianToDegree(rl) + m_pCenterL;
        double tB = rB;
        double tL = DegreeToRadian(dL);
        double tH = InH;

        ArrayList<Double> m_pPara = new ArrayList<Double>();
        m_pPara.add(-39.208938);
        m_pPara.add(65.046547);
        m_pPara.add(49.410739);
        m_pPara.add(SecondToRadian(6.125483));
        m_pPara.add(SecondToRadian(-1.281548));
        m_pPara.add(SecondToRadian(-0.861599));
        m_pPara.add(2.916036 * 1e-6);
        List<Double> b = LBH7ParameterSelf(tL, tB, tH, m_pSHa, 1.0 / m_pSHf, m_pPara.get(0),
                m_pPara.get(1), m_pPara.get(2), m_pPara.get(3),
                m_pPara.get(4), m_pPara.get(5), m_pPara.get(6),
                m_pWGS84a, 1.0 / m_pWGS84f);


        ArrayList<Double> a1 = RadianToDMS(b.get(0));
        ArrayList<Double> a2 = RadianToDMS(b.get(1));

        double b1 = a1.get(0) + a1.get(1) / 60 + a1.get(2) / 3600;
        double b2 = a2.get(0) + a2.get(1) / 60 + a2.get(2) / 3600;

        /*百度偏移*/
        /*谷歌偏移*/
        b1 = b1 + 0.000935;
        b2 = b2 + 0.002651;

        Map<String,Double> model = new HashMap<String,Double>();

        model.put("lat", b1);
        model.put("lon", b2);

        return model;
    }








    public static final double MPD = 60.0;
    public static final double SPD = 3600.0;
    public static final double SPM = 60.0;
    public static ArrayList<Double> RadianToDMS(Double radian) {
        Boolean isNegative;

        double degree = 0.0;
        double minute = 0.0;
        double second = 0.0;

        isNegative = false;
        if (radian < 0.0) {
            isNegative = false;
            radian = Math.abs(radian);
        } else {
            isNegative = false;
            degree = radian * DPR;
            minute = (degree - Math.floor(degree)) * MPD;

            degree = Math.floor(degree);
            second = (minute - Math.floor(minute)) * SPM;
            minute = Math.floor(minute);

            if (isNegative) {
                degree = -degree;
                minute = -minute;
                second = -second;
            }
        }

        ArrayList<Double> datalist = new ArrayList<Double>();

        datalist.add(degree);
        datalist.add(minute);
        datalist.add(second);

        return datalist;
    }
    public static ArrayList<Double> LBH7ParameterSelf(double Ls, double Bs, double Hs, double fA, double fF, double dX,
                                                      double dY, double dZ, double ex, double ey, double ez, double m, double at, double ft) {
        double Xs, Ys, Zs, Xt, Yt, Zt, Lt, Bt, Ht;
        ArrayList<Double> datalist = new ArrayList<Double>();

        ArrayList<Double> a = LBHToXYZ(fA, 1.0 / fF, Ls, Bs, Hs);

        Xs = a.get(0);
        Ys = a.get(1);
        Zs = a.get(2);

        ArrayList<Double> b = XYZ7Parameter(Xs, Ys, Zs, dX, dY, dZ, ex, ey, ez, m);

        Xt = b.get(0);
        Yt = b.get(1);
        Zt = b.get(2);

        ArrayList<Double> c = XYZToLBHBowring(at, 1.0 / ft, Xt, Yt, Zt);

        Lt = c.get(0);
        Bt = c.get(1);
        Ht = c.get(2);

        datalist.add(Lt);
        datalist.add(Bt);
        datalist.add(Ht);

        return datalist;
    }

    public static final double EQUALDE = 0.00000000000001;
    public static ArrayList<Double> AntiGaussProjectionConst(double curra, double currinvf, double East, double North) {
        double currf, currb, curre12, curre22, curre14, curre16, curre18, currAp, currBp, currCp, currDp, currEp;
        double A2, A4, A6, A8, currB2, currB4, currB6, currB8, phi, Bf, Nf, tf, cosBf, etaf2;
        double B, l;

        ArrayList<Double> datalist = new ArrayList<Double>();

        if ((Math.abs(East) < EQUALDE) && (Math.abs(North) < EQUALDE)) {
            B = 0.0;
            l = 0.0;
        }

        currf = 1 / currinvf;
        currb = curra * (1 - currf);
        curre12 = (curra * curra - currb * currb) / (curra * curra);
        curre22 = (curra * curra - currb * currb) / (currb * currb);
        curre14 = curre12 * curre12;
        curre16 = curre14 * curre12;
        curre18 = curre14 * curre14;

        currAp = 1 + 3.0 / 4.0 * curre12 + 45.0 / 64.0 * curre14 + 175.0 / 256.0 * curre16 + 11025.0 / 16384.0 * curre18;
        currBp = 3.0 / 4.0 * curre12 + 15.0 / 16.0 * curre14 + 525.0 / 512.0 * curre16 + 2205.0 / 2048.0 * curre18;
        currCp = 15.0 / 64.0 * curre14 + 105.0 / 256.0 * curre16 + 2205.0 / 4096.0 * curre18;
        currDp = 35.0 / 512.0 * curre16 + 315.0 / 2048.0 * curre18;
        currEp = 315.0 / 16384.0 * curre18;
        A2 = currBp / (2 * currAp);
        A4 = -currCp / (4 * currAp);
        A6 = currDp / (6 * currAp);
        A8 = -currEp / (8 * currAp);

        currB2 = A2 - A2 * A4 - A4 * A6 - 0.5 * A2 * A2 * A2 - A2 * A4 * A4 + 0.5 * A2 * A2 * A6 - 18.3 * A2 * A2 * A2 * A4;
        currB4 = A4 + A2 * A2 - 2.0 * A2 * A6 - 4.0 * A2 * A2 * A4 - 1.3 * A2 * A2 * A2 * A2;
        currB6 = A6 + 3.0 * A2 * A4 - 3.0 * A2 * A8 + 1.5 * A2 * A2 * A2 - 4.5 * A2 * A4 * A4 - 9.0 * A2 * A2 * A6 - 12.5 * A2 * A2 * A2 * A4;
        currB8 = A8 + 2.0 * A4 * A4 + 4.0 * A2 * A6 + 8.0 * A2 * A2 * A4 + 2.7 * A2 * A2 * A2 * A2;

        phi = North / (curra * (1 - curre12) * currAp);
        Bf = phi + currB2 * Math.sin(2 * phi) + currB4 * Math.sin(4 * phi) + currB6 * Math.sin(6 * phi) + currB8 * Math.sin(8 * phi);

        if (Math.abs(Math.abs(Bf) - PI / 2.0) < EQUALDE) {
            B = Bf;
            l = 0.0;

            datalist.add(B);
            datalist.add(l);

            return datalist;
        }

        Nf = curra / Math.sqrt(1 - curre12 * Math.sin(Bf) * Math.sin(Bf));
        tf = Math.tan(Bf);
        cosBf = Math.cos(Bf);
        etaf2 = curre22 * cosBf * cosBf;

        B = Bf + tf * (-1 - etaf2) * East * East / (2 * Nf * Nf)
                + tf * (5 + 3 * tf * tf + 6 * etaf2 - 6 * tf * tf * etaf2 - 3 * etaf2 * etaf2 - 9 * tf * tf * etaf2 * etaf2) * East * East * East * East / (24 * Nf * Nf * Nf * Nf)
                + tf * (-61 - 90 * tf * tf - 45 * tf * tf * tf * tf - 107 * etaf2 + 162 * tf * tf * etaf2 + 45 * tf * tf * tf * tf * etaf2) * East * East * East * East * East * East / (720 * Nf * Nf * Nf * Nf * Nf * Nf);
        l = East / (Nf * cosBf)
                + (-1 - 2 * tf * tf - etaf2) * East * East * East / (6 * Nf * Nf * Nf * cosBf)
                + (5 + 28 * tf * tf + 24 * tf * tf * tf * tf + 6 * etaf2 + 8 * tf * tf * etaf2) * East * East * East * East * East / (120 * Nf * Nf * Nf * Nf * Nf * cosBf);

        datalist.add(B);
        datalist.add(l);

        return datalist;

    }
    public static final double DPM = 0.016666666666666666666666666666667;
    public static final double DPS = 0.00027777777777777777777777777777778;
    public static double DMSToDegree(double degree, double minute, double second) {
        Boolean isNegative;

        if ((degree < 0.0) || (minute < 0.0) || (second < 0.0)) {
            isNegative = true;
            degree = Math.abs(degree);
            minute = Math.abs(minute);
            second = Math.abs(second);
        } else {
            isNegative = false;
        }
        degree = degree + minute * DPM + second * DPS;

        if (isNegative) {
            return -degree;
        } else {
            return degree;
        }
    }

    public static final double DPR = 57.295779513082320876798154814105;
    public static final double RPD = 0.017453292519943295769236907684886;
    public static final double RPS = 0.0000048481368110953599358991410235795;
    public static double RadianToDegree(double radian) {
        return radian * DPR;
    }
    public static double DegreeToRadian(double degree) {
        return degree * RPD;
    }
    public static double SecondToRadian(double second) {
        return second * RPS;
    }

    public static ArrayList<Double> XYZToLBHBowring(double curra, double currinvf, double X, double Y, double Z) {
        double L, B, H;
        double Rxy, f, e12, e22, tanu, cosu, sinu, temp;
        double sinB;
        ArrayList<Double> datalist = new ArrayList<Double>();

        if ((X == 0) && (Y == 0)) {
            if (Z < 0) {
                L = 0.0;
                B = -PI / 2;
                H = -(Z + curra * (1 - 1 / currinvf));
            } else if (Z > 0) {
                L = 0.0;
                B = PI / 2;
                H = Z - curra * (1 - 1 / currinvf);
            } else {
                L = 0.0;
                B = 0.0;
                H = -curra;
            }
        }

        Rxy = Math.sqrt(X * X + Y * Y);
        //Get L
        L = Math.acos(X / Rxy);
        if (Y < 0){ L = -L;}
        //Get B
        f = 1 / currinvf;
        e12 = (2 - f) * f;
        e22 = e12 / (1 - e12);
        tanu = Z * Math.sqrt(1 + e22) / Rxy;
        cosu = 1 / Math.sqrt(1 + tanu * tanu);
        sinu = tanu * cosu;
        temp = Rxy - curra * e12 * cosu * cosu * cosu;
        if (temp == 0) {
            if (Z < 0){
                B = -PI / 2;
            } else {
                B = PI / 2;
            }
        } else{
                B = Math.atan((Z + curra * (1 - f) * e22 * sinu * sinu * sinu) / temp);
            }
        //Get H
        sinB = Math.sin(B);
        if (Math.abs(B) < 4.8e-10) {
            H = Rxy / Math.cos(B) - curra / Math.sqrt(1 - e12 * sinB * sinB);
        }else {
            H = Z / sinB - curra / Math.sqrt(1 - e12 * sinB * sinB) * (1 - e12);
        }
        datalist.add(L);
        datalist.add(B);
        datalist.add(H);

        return datalist;
    }

    public static ArrayList<Double> LBHToXYZ(double curra, double currinvf, double L, double B, double H) {
        double e12, N, X, Y, Z;
        ArrayList<Double> datalist = new ArrayList<Double>();

        e12 = (2.0 - 1.0 / currinvf) / currinvf;
        N = curra / Math.sqrt(1 - e12 * Math.sin(B) * Math.sin(B));
        X = (N + H) * Math.cos(B) * Math.cos(L);
        Y = (N + H) * Math.cos(B) * Math.sin(L);
        Z = (N * (1 - e12) + H) * Math.sin(B);

        datalist.add(X);
        datalist.add(Y);
        datalist.add(Z);

        return datalist;
    }

    public static ArrayList<Double> XYZ7Parameter(double Xs, double Ys, double Zs, double dX, double dY, double dZ, double ex,
                                                  double ey, double ez, double m) {
        double Xt, Yt, Zt;
        ArrayList<Double> datalist = new ArrayList<Double>();

        Xt = Xs * (1 + m) + Ys * ez - Zs * ey + dX;
        Yt = Ys * (1 + m) - Xs * ez + Zs * ex + dY;
        Zt = Zs * (1 + m) + Xs * ey - Ys * ex + dZ;

        datalist.add(Xt);
        datalist.add(Yt);
        datalist.add(Zt);

        return datalist;
    }


}
