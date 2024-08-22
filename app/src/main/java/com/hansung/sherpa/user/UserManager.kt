package com.hansung.sherpa.user

import android.util.Log
import com.google.gson.Gson
import com.hansung.sherpa.user.`class`.Relation
import com.hansung.sherpa.user.`class`.User1
import com.hansung.sherpa.user.updateFcm.UpdateFcmRequest
import com.hansung.sherpa.user.updateFcm.UpdateFcmResponse
import com.hansung.sherpa.user.updateFcm.UpdateFcmService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okio.IOException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class UserManager {
    /**
     * 계정을 생성하는 함수
     */
    fun create(request: CreateUserRequest): CreateUserResponse? {
        var result: CreateUserResponse? = null
        runBlocking {
            launch(Dispatchers.IO){
                try{
                    val response = Retrofit.Builder()
                        .baseUrl("http://13.209.212.166:8080/api/v1/user/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                        .create(UserService::class.java)
                        .postCreateService(request).execute()
                    result = Gson().fromJson(response.body()!!.string(), CreateUserResponse::class.java)
                } catch(e:IOException){
                    Log.d("explain", "onFailure: 실패")
                    Log.d("explain", "message: ${e.message}")
                }
            }
        }
        Log.d("FCMLog", "create 함수 실행 성공 ${result?.code}")
        return result
    }

    /**
     * 계정 로그인(사용자 인증)하는 함수
     */
    fun login(email:String, password:String): UserResponse? {
        val loginRequest = LoginRequest(email,password)
        var result: UserResponse? = null
        runBlocking {
            launch(Dispatchers.IO){
                try{
                    val response = Retrofit.Builder()
                        .baseUrl("http://13.209.212.166:8080/api/v1/user/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                        .create(UserService::class.java)
                        .postLoginService(loginRequest).execute()
                    result = Gson().fromJson(response.body()!!.string(), UserResponse::class.java)
                } catch (e:IOException){
                    Log.d("explain", "onFailure: 실패")
                    Log.d("explain", "message: ${e.message}")
                }
            }
        }
        Log.d("FCMLog", "login 함수 실행 성공 ${result?.data?.userId}")
        return result
    }

    /**
     * 사용자 정보를 얻는 함수
     */
    fun updateFcm() {
        val updateFcmRequest = UpdateFcmRequest()
        var result: UpdateFcmResponse? = null
        runBlocking {
            launch(Dispatchers.IO) {
                try{
                    val response = Retrofit.Builder()
                        .baseUrl("http://13.209.212.166:8080/api/v1/fcm1/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                        .create(UpdateFcmService::class.java)
                        .patchUpdateFcmService(updateFcmRequest).execute()
                    result = Gson().fromJson(response.body()!!.string(), UpdateFcmResponse::class.java)
                } catch (e:IOException){
                    Log.d("explain", "onFailure: 실패")
                    Log.d("explain", "message: ${e.message}")
                }
            }
        }
    }

    /**
     * 보호자 인증을 요청하는 함수
     */
    fun linkPermission(caregiverEmail: String):LinkPermissionResponse? {
        var result: LinkPermissionResponse? = null
        runBlocking {
            launch(Dispatchers.IO){
                try{
                    val response = Retrofit.Builder()
                        .baseUrl("http://13.209.212.166:8080/api/v1/user/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                        .create(UserService::class.java)
                        .getLinkPermissionService(caregiverEmail).execute()
                    //TODO: 잘못된 Email로 요청할 때 에러처리 해야한다.
                    result = Gson().fromJson(response.body()!!.string(), LinkPermissionResponse::class.java)
                } catch(e:IOException){
                    Log.d("explain", "onFailure: 실패")
                    Log.d("explain", "message: ${e.message}")
                }
            }
        }
        Log.d("FCMLog", "create 함수 실행 성공 ${result?.code}")
        return result
    }

    /**
     * 사용자 정보를 얻는 함수
     */
    fun getUser(userId:Int): User1 {
        var result: UserResponse? = null
        runBlocking {
            launch(Dispatchers.IO){
                try{
                    val response = Retrofit.Builder()
                        .baseUrl("http://13.209.212.166:8080/api/v1/user/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                        .create(UserService::class.java)
                        .getUser(userId).execute()
                    result = Gson().fromJson(response.body()!!.string(), UserResponse::class.java)
                }catch(e: java.io.IOException){
                    Log.d("explain", "onFailure: 실패")
                    Log.d("explain", "message: ${e.message}")
                }
            }
        }
        return result?.data!!
    }

    /**
     * 사용자와 보호자의 관계를 얻는 함수
     */
    fun getRelation(userId:Int): Relation {
        var result: RelationResponse? = null
        runBlocking {
            launch(Dispatchers.IO){
                try{
                    val response = Retrofit.Builder()
                        .baseUrl("http://13.209.212.166:8080/api/v1/userRelation/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                        .create(UserService::class.java)
                        .getRelationService(userId).execute()
                    result = Gson().fromJson(response.body()!!.string(), RelationResponse::class.java)
                } catch(e: java.io.IOException){
                    Log.d("explain", "onFailure: 실패")
                    Log.d("explain", "message: ${e.message}")
                }
            }
        }
        Log.d("FCMLog", "getRelation 함수 실행 성공 ${result?.code}")
        return result?.data!!
    }
}