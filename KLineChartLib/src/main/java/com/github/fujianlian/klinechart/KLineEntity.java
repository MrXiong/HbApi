package com.github.fujianlian.klinechart;

import com.github.fujianlian.klinechart.entity.IKLine;
import com.github.fujianlian.klinechart.utils.TimeUtils;

import java.math.BigDecimal;

/**
 * K线实体
 * Created by tifezh on 2016/5/16.
 */
public class KLineEntity implements IKLine {

    public String getDate() {
        return TimeUtils.getTime(id* 1000L, TimeUtils.DATE_FORMAT_SIM);
    }

    @Override
    public float getOpenPrice() {
        return open.setScale(4, BigDecimal.ROUND_DOWN).floatValue();
    }

    @Override
    public float getHighPrice() {
        return high.setScale(4, BigDecimal.ROUND_DOWN).floatValue();
    }

    @Override
    public float getLowPrice() {
        return low.setScale(4, BigDecimal.ROUND_DOWN).floatValue();
    }

    @Override
    public float getClosePrice() {
        return close.setScale(4, BigDecimal.ROUND_DOWN).floatValue();
    }

    @Override
    public float getMA5Price() {
        return MA5Price;
    }

    @Override
    public float getMA10Price() {
        return MA10Price;
    }

    @Override
    public float getMA20Price() {
        return MA20Price;
    }

    @Override
    public float getMA30Price() {
        return MA30Price;
    }

    @Override
    public float getMA60Price() {
        return MA60Price;
    }

    @Override
    public float getDea() {
        return dea;
    }

    @Override
    public float getDif() {
        return dif;
    }

    @Override
    public float getMacd() {
        return macd;
    }

    @Override
    public float getK() {
        return k;
    }

    @Override
    public float getD() {
        return d;
    }

    @Override
    public float getJ() {
        return j;
    }

    @Override
    public float getR() {
        return r;
    }

    @Override
    public float getRsi() {
        return rsi;
    }

    @Override
    public float getUp() {
        return up;
    }

    @Override
    public float getMb() {
        return mb;
    }

    @Override
    public float getDn() {
        return dn;
    }

    @Override
    public float getVolume() {
        return Volume;
    }

    @Override
    public float getMA5Volume() {
        return MA5Volume;
    }

    @Override
    public float getMA10Volume() {
        return MA10Volume;
    }

    public String Date;
    public float Open;
    public float High;
    public float Low;
    public float Close;
    public float Volume;

    public float MA5Price;

    public float MA10Price;

    public float MA20Price;

    public float MA30Price;

    public float MA60Price;

    public float dea;

    public float dif;

    public float macd;

    public float k;

    public float d;

    public float j;

    public float r;

    public float rsi;

    public float up;

    public float mb;

    public float dn;

    public float MA5Volume;

    public float MA10Volume;


    //新增
    private long id;
    private BigDecimal open;
    private BigDecimal close;
    private BigDecimal low;
    private BigDecimal high;
    private BigDecimal amount;
    private BigDecimal vol;
    private BigDecimal count;


}
