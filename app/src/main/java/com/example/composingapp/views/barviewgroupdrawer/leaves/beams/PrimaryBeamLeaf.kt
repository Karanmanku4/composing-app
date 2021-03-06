package com.example.composingapp.views.barviewgroupdrawer.leaves.beams

import android.graphics.Paint
import com.example.composingapp.utils.Line
import com.example.composingapp.views.NoteView
import com.example.composingapp.views.barviewgroupdrawer.leaves.StemLeaf
import com.example.composingapp.views.viewtools.ViewConstants.STEM_WIDTH


/**
 *  Constructor
 *
 *  @param noteViews List of NoteViews to beam together with this PrimaryBeamLeaf
 *  @param stemDirection StemDirection of the noteViews
 *  @param paint Paint to calculate x coordinate of the stems of noteViews
 *  @param yTranslation Float - vertical shift to apply to line
 *  @param line Line representing the mapping of this PrimaryBeamLeaf to the Canvas
 */
class PrimaryBeamLeaf(
        noteViews: List<NoteView>,
        stemDirection: StemLeaf.StemDirection,
        paint: Paint,
        yTranslation: Float = 0F,
        line: Line,
) : BeamLeaf() {
    override val beamLine: Line = line.moveVertically(
            if (stemDirection == StemLeaf.StemDirection.POINTS_UP) yTranslation
            else -yTranslation)

    private val firstStemX = BeamHelper.getStemX(noteViews.first(), paint, stemDirection)
    private val lastStemX = BeamHelper.getStemX(noteViews.last(), paint, stemDirection)
    override val startX: Float = firstStemX + STEM_WIDTH / 2

    override val endX: Float = lastStemX - STEM_WIDTH / 2
    override val startY: Float = beamLine.yAt(startX)
    override val endY: Float = beamLine.yAt(endX)


    companion object {
//        private const val TAG = "PrimaryBeamLeaf"
    }
}