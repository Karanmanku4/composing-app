package com.example.composingapp.views.commands

import android.widget.Toast
import com.example.composingapp.utils.interfaces.ui.Command
import com.example.composingapp.utils.music.BarObserver
import com.example.composingapp.utils.music.Music
import com.example.composingapp.utils.music.Note
import com.example.composingapp.utils.music.NoteTable
import com.example.composingapp.viewmodels.ScoreViewModel
import com.example.composingapp.viewmodels.ViewModelHelper.mutation
import com.example.composingapp.views.NoteView
import com.example.composingapp.views.ScoreLineView

class ChangeNoteCommand(
        private val scoreLineView: ScoreLineView,
        private val scoreViewModel: ScoreViewModel,
        private val newNote: Note,
        private val notifyScoreObserversWhenExecuted: Boolean = false
) : Command {

    override fun execute() {
        scoreLineView.findClickedChild()?.let {
            if (it is NoteView) {
                val oldNote = it.notePositionDict.note
                val barObserver = it.barViewGroup.barObserver
                val index = it.barViewGroup.noteViewList.indexOf(it)

                // Replace the note differently depending on whether the newNote was loaded with
                // a rest or a regular Note
                when {
                    newNote.pitchClass == Music.PitchClass.REST -> {
                        updateNoteInScore(NoteTable.get(newNote.noteLength), barObserver, index)
                    }
                    oldNote.pitchClass == Music.PitchClass.REST -> {
                        updateNoteInScore(NoteTable.get(newNote.pitchClass, newNote.octave, newNote.noteLength),
                                barObserver, index)
                    }
                    else -> {
                        updateNoteInScore(NoteTable.get(oldNote.pitchClass, oldNote.octave, newNote.noteLength),
                                barObserver, index)
                    }
                }
            }
        }
    }

    /**
     * Replaces a target note in the ScoreViewModel's ScoreObservable to note
     *
     * @param note the replacement Note
     * @param targetBarObserver BarObserver of the target Note
     * @param targetIndex Int representing the index of the target Note
     *
     * Side effects: Mutates the ScoreViewModel's LiveData; notifies the ViewModel's observers
     */
    private fun updateNoteInScore(note: Note, targetBarObserver: BarObserver, targetIndex: Int) {
        // Mutate data without forcing a reset for any observers. This prevents any clicked item from
        // resetting
        scoreViewModel.scoreObservableMutableLiveData.value?.barObserverList
                ?.find { barObserver -> barObserver == targetBarObserver }
                .apply { this?.replaceNoteAt(targetIndex, note) }

        if (notifyScoreObserversWhenExecuted) {
            scoreViewModel.scoreObservableMutableLiveData.mutation { }
        }
    }


    override fun undo() {
        Toast.makeText(scoreLineView.context, "Not yet implemented", Toast.LENGTH_SHORT).show()
    }
}