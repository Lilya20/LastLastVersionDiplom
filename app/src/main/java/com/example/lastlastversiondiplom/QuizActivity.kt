package com.example.lastlastversiondiplom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.lastlastversiondiplom.db.Painting
import com.example.lastlastversiondiplom.db.PaintingDatabase
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import kotlin.random.Random
import kotlinx.coroutines.*


class QuizActivity : AppCompatActivity() {

    private lateinit var painting: Painting
    private lateinit var answerOptions: MutableList<String>
    private lateinit var questionText: TextView
    private lateinit var questionImage: ImageView
    private lateinit var option1: RadioButton
    private lateinit var option2: RadioButton
    private lateinit var option3: RadioButton
    private lateinit var option4: RadioButton
    private lateinit var submitButton: Button
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        // Находим все View по их id
        questionText = findViewById(R.id.question_text)
        questionImage = findViewById(R.id.question_image)
        option1 = findViewById(R.id.option1)
        option2 = findViewById(R.id.option2)
        option3 = findViewById(R.id.option3)
        option4 = findViewById(R.id.option4)
        submitButton = findViewById(R.id.submit_answer_button)
        progressBar = findViewById(R.id.progress_bar)

        // Показываем индикатор загрузки
        progressBar.visibility = View.VISIBLE

        // Получаем случайную картину из базы данных
        GlobalScope.launch(Dispatchers.Main) {
            painting = getRandomPaintingFromDatabase()

            // Получаем четыре случайных варианта ответа, включая правильный
            answerOptions = mutableListOf(painting.author)
            while (answerOptions.size < 4) {
                val randomOption = getRandomAuthorFromDatabase()
                if (!answerOptions.contains(randomOption)) {
                    answerOptions.add(randomOption)
                }
            }

            // Случайным образом перемешиваем варианты ответов
            answerOptions.shuffle()

            // Загружаем изображение картины в ImageView с помощью библиотеки Picasso
            val file = withContext(Dispatchers.IO) {
                File.createTempFile("image", ".jpg", applicationContext.cacheDir)
            }
            withContext(Dispatchers.IO) {
                file.writeBytes(painting.image ?: return@withContext)
            }
            Picasso.get().load(file).into(questionImage)

            // Устанавливаем текст вопроса и вариантов ответов
            questionText.text = getString(R.string.question_template, painting.title)
            option1.text = answerOptions[0]
            option2.text = answerOptions[1]
            option3.text = answerOptions[2]
            option4.text = answerOptions[3]

            // Скрываем индикатор загрузки
            progressBar.visibility = View.GONE
        }

        // Устанавливаем обработчик клика на кнопку
        submitButton.setOnClickListener { checkAnswer() }
    }

    private suspend fun getRandomPaintingFromDatabase(): Painting {
        return withContext(Dispatchers.IO) {
            val paintings =
                PaintingDatabase.getInstance(this@QuizActivity).paintingDao().getAllPaintings()
            if (isActive && paintings.isNotEmpty()) {
                paintings[Random.nextInt(paintings.size)]
            } else {
                Painting()
            }
        }
    }

    private suspend fun getRandomAuthorFromDatabase(): String {
        return withContext(Dispatchers.IO) {
            val authors = PaintingDatabase.getInstance(this@QuizActivity).paintingDao().getAllAuthors()
            if (isActive && authors.isNotEmpty()) {
                authors[Random.nextInt(authors.size)]
            } else {
                ""
            }
        }
    }

    private fun checkAnswer() {
        // Находим выбранный вариант ответа
        val answerOptions = findViewById<RadioGroup>(R.id.answer_options)
        val selectedOption = findViewById<RadioButton>(answerOptions.checkedRadioButtonId)

        // Запускаем корутин для проверки ответа
        GlobalScope.launch {
            // Проверяем, является ли выбранный вариант ответа правильным
            if (selectedOption.text == painting.author) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@QuizActivity, R.string.correct_answer_message, Toast.LENGTH_SHORT).show()
                }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@QuizActivity, R.string.incorrect_answer_message, Toast.LENGTH_SHORT).show()
                }
            }
            // Задерживаем закрытие активити на 2 секунды
            delay(2000)
            // Закрываем активити
            withContext(Dispatchers.Main) {
                finish()
            }
        }
    }

}



