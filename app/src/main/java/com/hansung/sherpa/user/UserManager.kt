package com.hansung.sherpa.user

import android.util.Log
import com.google.gson.Gson
import com.hansung.sherpa.user.createuser.CreateUserRequest
import com.hansung.sherpa.user.createuser.CreateUserResponse
import com.hansung.sherpa.user.createuser.UserService
import com.hansung.sherpa.user.linkpermission.LinkPermissionResponse
import com.hansung.sherpa.user.linkpermission.LinkPermissionService
import com.hansung.sherpa.user.login.LoginRequest
import com.hansung.sherpa.user.login.LoginService
import com.hansung.sherpa.user.login.User1
import com.hansung.sherpa.user.login.UserResponse
import com.hansung.sherpa.user.relation.GetUserRelationService
import com.hansung.sherpa.user.relation.Relation
import com.hansung.sherpa.user.relation.RelationResponse
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
                        .postLoginService(request).execute()
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

    fun linkPermission(caregiverEmail: String):LinkPermissionResponse? {
        var result: LinkPermissionResponse? = null
        runBlocking {
            launch(Dispatchers.IO){
                try{
                    val response = Retrofit.Builder()
                        .baseUrl("http://13.209.212.166:8080/api/v1/user/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                        .create(LinkPermissionService::class.java)
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
                        .create(LoginService::class.java)
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

    fun getRelation(userId:Int): Relation {
        var result: RelationResponse? = null
        runBlocking {
            launch(Dispatchers.IO){
                try{
                    val response = Retrofit.Builder()
                        .baseUrl("http://13.209.212.166:8080/api/v1/userRelation/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                        .create(GetUserRelationService::class.java)
                        .getUserRelationService(userId).execute()
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