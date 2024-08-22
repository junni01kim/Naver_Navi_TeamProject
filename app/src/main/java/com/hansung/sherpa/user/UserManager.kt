package com.hansung.sherpa.user

import android.util.Log
import com.google.gson.Gson
import com.hansung.sherpa.user.user.Relation
import com.hansung.sherpa.user.user.User1
import com.hansung.sherpa.user.updateFcm.UpdateFcmRequest
import com.hansung.sherpa.user.updateFcm.UpdateFcmResponse
import com.hansung.sherpa.user.updateFcm.UpdateFcmService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okio.IOException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val nncBackendUserUrl = "http://13.209.212.166:8080/api/v1/user/"
val nncBackendRelationUrl = "http://13.209.212.166:8080/api/v1/userRelation/"

class UserManager {
    /**
     * 계정을 생성하는 함수
     */
    fun create(request: CreateUserRequest): CreateUserResponse {
        var result: CreateUserResponse? = null
        runBlocking {
            launch(Dispatchers.IO){
                try{
                    val response = Retrofit.Builder()
                        .baseUrl(nncBackendUserUrl)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                        .create(UserService::class.java)
                        .postCreateService(request).execute()
                    val jsonString = response.body()?.string()?:"response is null"

                    // 반환 실패에 대한 에러처리
                    if(jsonString == "response is null") {
                        result = CreateUserResponse(404, "'reponse.body()' is null")
                        Log.e("API Log:response(Null)", "UserManager.create: ${result?.message}")
                    }
                    else {
                        result = Gson().fromJson(
                            jsonString,
                            CreateUserResponse::class.java
                        )
                    }
                } catch(e:IOException){
                    Log.e("API Log: IOException", "UserManager.create: ${e.message}(e.message)")
                }
            }
        }
        Log.i("API Log: Success", "create 함수 실행 성공 ${result?.message}")
        return result?: CreateUserResponse(500, "에러 원인을 찾을 수 없음")
    }

    /**
     * 계정 로그인(사용자 인증)하는 함수
     */
    fun login(email:String, password:String): UserResponse {
        val loginRequest = LoginRequest(email,password)
        var result: UserResponse? = null
        runBlocking {
            launch(Dispatchers.IO){
                try{
                    val response = Retrofit.Builder()
                        .baseUrl(nncBackendUserUrl)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                        .create(UserService::class.java)
                        .postLoginService(loginRequest).execute()
                    val jsonString = response.body()?.string()?:"response is null"

                    // 반환 실패에 대한 에러처리
                    if(jsonString == "response is null") {
                        result = UserResponse(404, "'reponse.body()' is null")
                        Log.e("API Log:response(Null)", "UserManager.login: ${result?.message}")
                    }
                    else {
                        result = Gson().fromJson(
                            jsonString,
                            UserResponse::class.java
                        )
                    }
                } catch (e:IOException){
                    Log.e("API Log: IOException", "UserManager.login: ${e.message}(e.message)")
                }
            }
        }
        Log.i("API Log: Success", "login 함수 실행 성공 ${result?.message}")
        return result?: UserResponse(500, "에러 원인을 찾을 수 없음")
    }

    /**
     * 사용자 정보를 얻는 함수
     */
    fun getUser(userId:Int): UserResponse {
        var result: UserResponse? = null
        runBlocking {
            launch(Dispatchers.IO){
                try{
                    val response = Retrofit.Builder()
                        .baseUrl(nncBackendUserUrl)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                        .create(UserService::class.java)
                        .getUser(userId).execute()
                    val jsonString = response.body()?.string()?:"response is null"

                    // 반환 실패에 대한 에러처리
                    if(jsonString == "response is null") {
                        result = UserResponse(404, "'reponse.body()' is null")
                        Log.e("API Log:response(Null)", "UserManager.getUser: ${result?.message}")
                    }
                    else {
                        result = Gson().fromJson(
                            jsonString,
                            UserResponse::class.java
                        )
                    }
                }catch(e: java.io.IOException){
                    Log.e("API Log: IOException", "UserManager.getUser: ${e.message}(e.message)")
                }
            }
        }
        Log.i("API Log: Success", "getUser 함수 실행 성공 ${result?.message}")
        return result?: UserResponse(500, "에러 원인을 찾을 수 없음")
    }

    /**
     * 보호자 인증을 요청하는 함수
     */
    fun linkPermission(caregiverEmail: String):LinkPermissionResponse {
        var result: LinkPermissionResponse? = null
        runBlocking {
            launch(Dispatchers.IO){
                try{
                    val response = Retrofit.Builder()
                        .baseUrl(nncBackendUserUrl)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                        .create(UserService::class.java)
                        .getLinkPermissionService(caregiverEmail).execute()
                    //TODO: 잘못된 Email로 요청할 때 에러처리 해야한다.
                    val jsonString = response.body()?.string()?:"response is null"

                    // 반환 실패에 대한 에러처리
                    if(jsonString == "response is null") {
                        result = LinkPermissionResponse(404, "'reponse.body()' is null")
                        Log.e("API Log:response(Null)", "UserManager.linkPermission: ${result?.message}")
                    }
                    else {
                        result = Gson().fromJson(
                            jsonString,
                            LinkPermissionResponse::class.java
                        )
                    }
                } catch(e:IOException){
                    Log.e("API Log: IOException", "UserManager.linkPermission: ${e.message}(e.message)")
                }
            }
        }
        Log.i("API Log: Success", "linkPermission 함수 실행 성공 ${result?.message}")
        return result?: LinkPermissionResponse(500, "에러 원인을 찾을 수 없음")
    }

    /**
     * 사용자와 보호자의 관계를 얻는 함수
     */
    fun getRelation(userId:Int): RelationResponse {
        var result: RelationResponse? = null
        runBlocking {
            launch(Dispatchers.IO){
                try{
                    val response = Retrofit.Builder()
                        .baseUrl(nncBackendRelationUrl)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                        .create(UserService::class.java)
                        .getRelationService(userId).execute()
                    val jsonString = response.body()?.string()?:"response is null"

                    // 반환 실패에 대한 에러처리
                    if(jsonString == "response is null") {
                        result = RelationResponse(404, "'reponse.body()' is null")
                        Log.e("API Log:response(Null)", "UserManager.getRelation: ${result?.message}")
                    }
                    else {
                        result = Gson().fromJson(
                            jsonString,
                            RelationResponse::class.java
                        )
                    }
                } catch(e: java.io.IOException){
                    Log.e("API Log: IOException", "UserManager.getRelation: ${e.message}(e.message)")
                }
            }
        }
        Log.i("API Log: Success", "getRelation 함수 실행 성공 ${result?.message}")
        return result?:RelationResponse(500, "에러 원인을 찾을 수 없음")
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
                        .baseUrl(nncBackendUserUrl)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                        .create(UpdateFcmService::class.java)
                        .patchUpdateFcmService(updateFcmRequest).execute()
                    val jsonString = response.body()?.string()?:"response is null"

                    // 반환 실패에 대한 에러처리
                    if(jsonString == "response is null") {
                        result = UpdateFcmResponse(404, "'reponse.body()' is null")
                        Log.e("API Log:response(Null)", "UserManager.updateFcm: ${result?.message}")
                    }
                    else {
                        result = Gson().fromJson(
                            jsonString,
                            UpdateFcmResponse::class.java
                        )
                    }
                } catch (e:IOException){
                    Log.e("API Log: IOException", "UserManager.updateFcm: ${e.message}(e.message)")
                }
            }
        }
        Log.i("API Log: Success", "updateFcm 함수 실행 성공 ${result?.message}")
    }

}