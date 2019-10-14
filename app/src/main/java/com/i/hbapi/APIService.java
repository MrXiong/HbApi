package com.i.hbapi;

import com.github.fujianlian.klinechart.KLineEntity;
import com.i.hbapi.model.Symbol;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIService {

    //行情数据

    @GET("market/history/kline")
    Call<HttpResponse<List<KLineEntity>>> getKline(
            @Query("period") String period,
            @Query("size") int size,
            @Query("symbol") String symbol
    );

    @GET("v1/common/symbols")
    Call<HttpResponse<List<Symbol>>> getSymbols();
}
