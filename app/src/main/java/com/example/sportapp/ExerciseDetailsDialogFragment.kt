package com.example.sportapp


import android.app.Dialog
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.sportapp.R


class ExerciseDetailsDialogFragment(
    private val title: String,
    private val description: String
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater

        // Інфлейт діалогового вигляду
        val view = inflater.inflate(R.layout.dialog_exercise_details, null)
        view.findViewById<TextView>(R.id.exerciseTitleTextView).text = title
        view.findViewById<TextView>(R.id.exerciseDescriptionTextView).text = description

        builder.setView(view)
            .setPositiveButton("Закрити") { dialog, _ ->
                dialog.dismiss()
            }
        return builder.create()
    }
}
