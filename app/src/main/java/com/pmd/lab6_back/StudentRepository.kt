package com.pmd.lab6_back

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import java.io.FileOutputStream
import java.io.IOException

object StudentRepository {
    private const val JSON_FILE_NAME = "students.json"
    private val students = mutableListOf<Student>()

    fun loadStudents(context: Context) {
        students.clear()
        try {
            val jsonString = context.assets.open(JSON_FILE_NAME).bufferedReader().use { it.readText() }
            val jsonObject = JSONObject(jsonString)
            val jsonArray = jsonObject.getJSONArray("students")

            for (i in 0 until jsonArray.length()) {
                val studentJson = jsonArray.getJSONObject(i)
                students.add(
                    Student(
                        id = studentJson.getInt("id"),
                        name = studentJson.getString("name"),
                        age = studentJson.getInt("age"),
                        major = studentJson.getString("major"),
                        email = studentJson.getString("email")
                    )
                )
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun getStudents(): List<Student> = students

    fun addStudent(student: Student) {
        students.add(student)
    }

    fun updateStudent(student: Student) {
        val index = students.indexOfFirst { it.id == student.id }
        if (index != -1) {
            students[index] = student
        }
    }

    fun saveStudentsToJson(context: Context) {
        try {
            val jsonArray = JSONArray()
            for (student in students) {
                val jsonObject = JSONObject()
                jsonObject.put("id", student.id)
                jsonObject.put("name", student.name)
                jsonObject.put("age", student.age)
                jsonObject.put("major", student.major)
                jsonObject.put("email", student.email)
                jsonArray.put(jsonObject)
            }

            val jsonObject = JSONObject()
            jsonObject.put("students", jsonArray)

            val fileOutputStream: FileOutputStream = context.openFileOutput(JSON_FILE_NAME, Context.MODE_PRIVATE)
            fileOutputStream.write(jsonObject.toString().toByteArray())
            fileOutputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
