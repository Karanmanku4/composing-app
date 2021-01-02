package com.example.composingapp.views.viewtools.noteviewdrawer

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import com.example.composingapp.utils.interfaces.*
import com.example.composingapp.utils.music.Music
import com.example.composingapp.views.viewtools.ViewConstants.STEM_WIDTH
import com.example.composingapp.views.viewtools.accidentals.SharpLeaf
import com.example.composingapp.views.viewtools.barviewgroupdrawer.BarViewGroupDrawer
import com.example.composingapp.views.viewtools.noteviewdrawer.composites.NoteComposite
import com.example.composingapp.views.viewtools.noteviewdrawer.composites.RestComposite
import com.example.composingapp.views.viewtools.noteviewdrawer.leaves.StemLeaf
import com.example.composingapp.views.viewtools.positiondict.NotePositionDict

class NoteViewDrawer(private val notePositionDict: NotePositionDict) : CompositeDrawer {
    private var drawers = mutableListOf<ComponentDrawer>()
    private val observers = mutableListOf<Observer>()
    private val paint = Paint().apply {
        color = Color.BLACK
        isAntiAlias = true
        isDither = true
        strokeWidth = STEM_WIDTH.toFloat()
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
    }

    init {
        resetWith(notePositionDict)
//        Log.d(TAG, "Drawer for ${notePositionDict.note.pitchClass}, ${notePositionDict.note.octave}, " +
//                "${notePositionDict.note.noteLength} ")
    }

    /**
     *  Resets all drawers with the information provided from a given NotePositionDict
     *
     *  @param notePositionDict NotePositionDict containing coordinate information for the note
     */
    fun resetWith(notePositionDict: NotePositionDict) {
        drawers.clear()
        if (notePositionDict.note.pitchClass == Music.PitchClass.REST) {
            add(RestComposite(notePositionDict, paint))
        } else {
            add(NoteComposite(notePositionDict, paint))
        }
    }

    override fun draw(canvas: Canvas?) {
        drawers.map {it.draw(canvas)}
    }

    override fun add(drawerComponent: ComponentDrawer) {
        if (drawerComponent is StemLeaf) {
            drawers.removeAll { it is StemLeaf }
        }
        drawers.add(drawerComponent)
    }

    override fun remove(drawerComponent: ComponentDrawer) {
        drawers.remove(drawerComponent)
    }

    companion object {
        private const val TAG = "NoteViewDrawer"
    }
}