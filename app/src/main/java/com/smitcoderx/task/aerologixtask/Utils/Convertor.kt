package com.smitcoderx.task.aerologixtask.Utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.smitcoderx.task.aerologixtask.Model.Data
import com.smitcoderx.task.aerologixtask.Model.Education
import com.smitcoderx.task.aerologixtask.Model.Job

class Convertor {

    @TypeConverter
    fun toDataList(list: String): List<Data> {
        val type = object : TypeToken<List<Data>>() {}.type
        return Gson().fromJson(list, type)
    }


    @TypeConverter
    fun toJobList(list: String): List<Job> {
        val type = object : TypeToken<List<Job>>() {}.type
        return Gson().fromJson(list, type)
    }

    @TypeConverter
    fun toEduList(list: String): List<Education> {
        val type = object : TypeToken<List<Education>>() {}.type
        return Gson().fromJson(list, type)
    }

    @TypeConverter
    fun toDataJson(data: List<Data>): String {
        val type = object : TypeToken<List<Data>>() {}.type
        return Gson().toJson(data, type)
    }

    @TypeConverter
    fun toJobJson(data: List<Job>): String {
        val type = object : TypeToken<List<Job>>() {}.type
        return Gson().toJson(data, type)
    }

    @TypeConverter
    fun toEduJson(data: List<Education>): String {
        val type = object : TypeToken<List<Education>>() {}.type
        return Gson().toJson(data, type)
    }


}