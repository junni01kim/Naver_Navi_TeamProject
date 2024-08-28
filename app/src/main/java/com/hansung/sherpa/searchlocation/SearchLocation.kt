package com.hansung.sherpa.searchlocation

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.hansung.sherpa.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/**
 * Search API 응답 처리 후 콜백 인터페이스
 * @property onSuccess 검색 성공 시 결과 리턴 @Nullable
 * @property onFailure 검색 실패 시 오류 리턴
 */
interface SearchLoactionCallBack{
    fun onSuccess(response : SearchLocationResponse?)
    fun onFailure(message : String)
}

/**
 * 장소 검색 시 여러 결과 리턴하는 클래스
 * @since 2024-05-26
 * @author 6-keem
 *
 */
class SearchLocation {
        // Request Headers 따로 구성
    private val interceptor = OkHttpClient().newBuilder()
        .addInterceptor(HeaderInterceptor())
        .build()

    private val gson: Gson = GsonBuilder()
        .registerTypeAdapter(SearchLocationResponse::class.java, SearchLocationDeserializer())
        .create()

    private val retrofitService = Retrofit.Builder()
            .baseUrl("https://openapi.naver.com")
            .client(interceptor)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(SearchLocationService::class.java)

    /**
     * 장소 입력 시 여러 개의 검색 결과 리턴
     *
     * @param locationString 검색할 장소
     * @param searchLoactionCallBack Response 받으면 호출 부분으로 Callback
     */
    fun search(locationString : String, searchLoactionCallBack : SearchLoactionCallBack){
        // UTF-8 Encoding ※ Uri.encode() 메서드 사용 시 오류
        val byteArray = locationString.toByteArray()
        val encoded = String(byteArray, Charsets.UTF_8)

        retrofitService.searchLocation(query = encoded, display = 5)
            .enqueue(object : Callback<SearchLocationResponse> {
                override fun onResponse(
                    call: Call<SearchLocationResponse>,
                    response: Response<SearchLocationResponse>
                ) {
                    if(response.isSuccessful){
                        searchLoactionCallBack.onSuccess(response.body())
                    }
                }
                override fun onFailure(call: Call<SearchLocationResponse>, t: Throwable) {
                    searchLoactionCallBack.onFailure(t.message.toString())
                }
            })
    }

    /**
     * Search API의 Request Header 구성하는 클래스
     *
     */
    private class HeaderInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
            val request = chain.request()
                .newBuilder()
                .addHeader("Accept", "*/*")
                .addHeader("X-Naver-Client-Id", BuildConfig.SEARCH_API_CLIENT_ID)
                .addHeader("X-Naver-Client-Secret",BuildConfig.SEARCH_API_CLIENT_SECRET)
                .build()
            return chain.proceed(request)
        }
    }
}