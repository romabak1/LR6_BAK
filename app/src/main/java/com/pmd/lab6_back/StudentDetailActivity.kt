package com.pmd.lab6_back

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class StudentDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_detail)

        val studentId = intent.getIntExtra("studentId", -1)
        val student = StudentRepository.getStudents().find { it.id == studentId }

        if (student != null) {
            // Відображення даних у полях редагування
            val etName = findViewById<EditText>(R.id.etStudentName)
            val etMajor = findViewById<EditText>(R.id.etStudentMajor)
            val etAge = findViewById<EditText>(R.id.etStudentAge)
            val etEmail = findViewById<EditText>(R.id.etStudentEmail)

            etName.setText(student.name)
            etMajor.setText(student.major)
            etAge.setText(student.age.toString())
            etEmail.setText(student.email)

            // Обробка кнопки "Зберегти зміни"
            findViewById<Button>(R.id.btnSaveChanges).setOnClickListener {
                val updatedStudent = student.copy(
                    name = etName.text.toString(),
                    major = etMajor.text.toString(),
                    age = etAge.text.toString().toIntOrNull() ?: student.age,
                    email = etEmail.text.toString()
                )

                StudentRepository.updateStudent(updatedStudent)
                StudentRepository.saveStudentsToJson(this)
                Toast.makeText(this, "Дані оновлено", Toast.LENGTH_SHORT).show()
                finish() // Повернення до списку
            }
        } else {
            Toast.makeText(this, "Студента не знайдено", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
