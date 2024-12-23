package com.pmd.lab6_back

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var lvStudents: ListView
    private lateinit var btnAddStudent: Button
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Завантаження студентів із JSON
        StudentRepository.loadStudents(this)

        // Ініціалізація компонентів
        lvStudents = findViewById(R.id.lvStudents)
        btnAddStudent = findViewById(R.id.btnAddStudent)

        // Адаптер для списку
        adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            StudentRepository.getStudents().map { it.name }
        )
        lvStudents.adapter = adapter

        // Обробка кліку на елемент
        lvStudents.setOnItemClickListener { _, _, position, _ ->
            val selectedStudent = StudentRepository.getStudents()[position]
            val intent = Intent(this, StudentDetailActivity::class.java).apply {
                putExtra("studentId", selectedStudent.id)
            }
            startActivity(intent)
        }

        // Обробка кнопки "Додати студента"
        btnAddStudent.setOnClickListener {
            val newStudent = Student(
                id = StudentRepository.getStudents().size + 1,
                name = "Новий Студент",
                age = 18,
                major = "Нова спеціальність",
                email = "new.student@example.com"
            )
            StudentRepository.addStudent(newStudent)
            updateList()
        }
    }

    override fun onResume() {
        super.onResume()
        updateList()
    }

    private fun updateList() {
        val updatedStudents = StudentRepository.getStudents()
        adapter.clear()
        adapter.addAll(updatedStudents.map { it.name })
        adapter.notifyDataSetChanged()
    }
}
