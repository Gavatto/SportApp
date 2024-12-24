package com.example.sportapp


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.ArrayAdapter
import android.widget.ListView
import com.example.sportapp.R


class ExercisesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercises)

        // Масив вправ і описів
        val exercises = mapOf(
            "Присідання" to "Станьте прямо, ноги на ширині плечей. Повільно опускайте таз до рівня колін, потім поверніться в початкове положення.",
            "Віджимання" to "Ляжте на підлогу, руки на ширині плечей. Повільно опускайтесь до підлоги і піднімайтесь.",
            "Планка" to "Утримуйте тіло у прямій лінії, спираючись на лікті та носки. Напружуйте м'язи преса.",
            "Скручування" to "Ляжте на спину, ноги зігнуті. Піднімайте верхню частину тіла, скорочуючи м'язи преса.",
            "Випади" to "Станьте прямо, зробіть крок уперед і зігніть обидві ноги до утворення прямого кута. Поверніться в початкове положення."
        )

        // Конвертація у список для відображення
        val exerciseNames = exercises.keys.toList()
        val exerciseDetails = exercises.values.toList()

        // Зв'язок з ListView
        val listView = findViewById<ListView>(R.id.exerciseListView)
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            exerciseNames
        )
        listView.adapter = adapter

        // Відображення опису вправ при виборі
        listView.setOnItemClickListener { _, _, position, _ ->
            val exerciseName = exerciseNames[position]
            val exerciseDescription = exerciseDetails[position]
            val dialog = ExerciseDetailsDialogFragment(exerciseName, exerciseDescription)
            dialog.show(supportFragmentManager, "exerciseDetails")
        }
    }
}
