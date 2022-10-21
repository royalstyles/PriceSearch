package com.jhpj.pricesearch.ui.movie;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitInterface {
    //http://www.kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/searchDailyBoxOfficeList.json?key=bc7dc8d95109b9f51ad46cff5c60c14c&targetDt=20210102
    @GET("http://kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/searchDailyBoxOfficeList.json")
    Call<Result> getBoxOffice(@Query("key") String key, @Query("targetDt") String targetDt);
}
