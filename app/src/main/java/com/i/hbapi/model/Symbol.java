package com.i.hbapi.model;

import com.google.gson.annotations.SerializedName;

public class Symbol {

    /**
     * base-currency : cnn
     * quote-currency : btc
     * price-precision : 10
     * amount-precision : 2
     * symbol-partition : innovation
     * symbol : cnnbtc
     * state : online
     * value-precision : 8
     * min-order-amt : 100
     * max-order-amt : 100000000
     * min-order-value : 1.0E-4
     */

    @SerializedName("base-currency")
    private String basecurrency;
    @SerializedName("quote-currency")
    private String quotecurrency;
    @SerializedName("price-precision")
    private int priceprecision;
    @SerializedName("amount-precision")
    private int amountprecision;
    @SerializedName("symbol-partition")
    private String symbolpartition;
    private String symbol;
    private String state;
    @SerializedName("value-precision")
    private int valueprecision;
    @SerializedName("min-order-amt")
    private double minorderamt;
    @SerializedName("max-order-amt")
    private int maxorderamt;
    @SerializedName("min-order-value")
    private double minordervalue;

    public String getBasecurrency() {
        return basecurrency;
    }

    public void setBasecurrency(String basecurrency) {
        this.basecurrency = basecurrency;
    }

    public String getQuotecurrency() {
        return quotecurrency;
    }

    public void setQuotecurrency(String quotecurrency) {
        this.quotecurrency = quotecurrency;
    }

    public int getPriceprecision() {
        return priceprecision;
    }

    public void setPriceprecision(int priceprecision) {
        this.priceprecision = priceprecision;
    }

    public int getAmountprecision() {
        return amountprecision;
    }

    public void setAmountprecision(int amountprecision) {
        this.amountprecision = amountprecision;
    }

    public String getSymbolpartition() {
        return symbolpartition;
    }

    public void setSymbolpartition(String symbolpartition) {
        this.symbolpartition = symbolpartition;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getValueprecision() {
        return valueprecision;
    }

    public void setValueprecision(int valueprecision) {
        this.valueprecision = valueprecision;
    }

    public double getMinorderamt() {
        return minorderamt;
    }

    public void setMinorderamt(double minorderamt) {
        this.minorderamt = minorderamt;
    }

    public int getMaxorderamt() {
        return maxorderamt;
    }

    public void setMaxorderamt(int maxorderamt) {
        this.maxorderamt = maxorderamt;
    }

    public double getMinordervalue() {
        return minordervalue;
    }

    public void setMinordervalue(double minordervalue) {
        this.minordervalue = minordervalue;
    }
}
