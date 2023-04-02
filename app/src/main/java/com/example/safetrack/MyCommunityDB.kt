package com.example.safetrack

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ContactModel::class], version = 1, exportSchema = false)
public abstract class MyCommunityDB :RoomDatabase(){


    abstract fun contactDao():ContactDao



    companion object{

        @Volatile   //Thread safe
        private var INSTANCE:MyCommunityDB? = null

        fun getDatabase(context: Context):MyCommunityDB{

            INSTANCE?.let {
                return it
            }

            return synchronized(MyCommunityDB::class.java){
                val instance = Room.databaseBuilder(context.applicationContext,MyCommunityDB::class.java,"My Community DB").build()

                INSTANCE = instance

                 instance
            }


        }
    }
}