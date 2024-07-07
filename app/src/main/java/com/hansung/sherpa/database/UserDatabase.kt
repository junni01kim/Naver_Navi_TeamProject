package com.hansung.sherpa.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * 사용자 데이터 저장 Database by Room
 * UserDatabase.createInstance(context) 초기 호출 이후 UserDatabase.getInstance()로 접근
 * UserDatabase.getInstance().caregiverDao()로 caregiverDao return
 * caregiverDao 통해 table record 조회, 수정 , 삭제 가능
 * @author 6-keem
 * @since 2024-07-05
 *
 */
@Database(entities = [CaregiverData::class], version = 1, exportSchema =  false)
abstract class UserDatabase : RoomDatabase(){
    abstract fun caregiverDao() : CaregiverDao
    companion object {
        private lateinit var userDatabase: UserDatabase

        /**
         * @return UserDatabase
         * @param context
         */
        fun createInstance(context : Context) : UserDatabase {
            if(!this::userDatabase.isInitialized){
                userDatabase = Room.databaseBuilder(context.applicationContext, UserDatabase::class.java, "user_data.db")
                    .setJournalMode(JournalMode.TRUNCATE)
                    .allowMainThreadQueries()
                    .build()
                userDatabase.inTransaction()
            }
            return userDatabase
        }

        /**
         * createInstance 호출이 선행 되어야 함
         * @return UserDatabase
         * @throws NullPointerException
         */
        fun getInstance() : UserDatabase {
            if(!this::userDatabase.isInitialized)
                throw NullPointerException()
            return userDatabase
        }
    }
}