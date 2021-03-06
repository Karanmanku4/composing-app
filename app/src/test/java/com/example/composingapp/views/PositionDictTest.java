package com.example.composingapp.views;

import com.example.composingapp.utils.music.Music;
import com.example.composingapp.utils.music.Note;
import com.example.composingapp.utils.music.NoteTable;
import com.example.composingapp.utils.music.Tone;
import com.example.composingapp.utils.music.ToneTable;
import com.example.composingapp.views.viewtools.positiondict.ScorePositionDict;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
@DisplayName("When PositionDict is created ")
class PositionDictTest {
    ScorePositionDict notePositionDict;
    float barHeight;
    Music.Clef clef;

    @BeforeEach
    void init() {
        barHeight = 100;
        clef = Music.Clef.TREBLE_CLEF;
        notePositionDict = new ScorePositionDict(barHeight, clef);
    }

    @Nested
    @DisplayName("toneToYMap")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class ToneToYMap {
        HashMap<Tone, Float> toneToYMap;

        @BeforeEach
        void initToneToYMap() {
            toneToYMap = notePositionDict.getToneToYMap();
        }


        @DisplayName("has accidental tones in the same position as unaccented ones")
        @ParameterizedTest
        @MethodSource("provideAccidentalTones")
        void testAccidentalTones(Tone unaccTone, Tone accTone) {
            assertEquals(toneToYMap.get(unaccTone), toneToYMap.get(accTone));
        }

        /**
         * Produces the arguments for testAccidentalTones()
         *
         * @return stream of arguments for testAccidentalTones()
         */
        Stream<Arguments> provideAccidentalTones() {
            Music.PitchClass[] unaccPitchClasses = {Music.PitchClass.A_NATURAL,
                    Music.PitchClass.C_NATURAL, Music.PitchClass.D_NATURAL,
                    Music.PitchClass.F_NATURAL, Music.PitchClass.G_NATURAL};
            Music.PitchClass[] accPitchClasses = {Music.PitchClass.A_SHARP,
                    Music.PitchClass.C_SHARP, Music.PitchClass.D_SHARP,
                    Music.PitchClass.F_SHARP, Music.PitchClass.G_SHARP};

            int octave = 4; // essential octave in any score (contains middle C)
            Arguments[] arguments = new Arguments[accPitchClasses.length];
            for (int i = 0; i < accPitchClasses.length; i++) {
                arguments[i] =
                        Arguments.of(ToneTable.get(unaccPitchClasses[i], octave),
                                ToneTable.get(accPitchClasses[i], octave));
            }
            return Arrays.stream(arguments);
        }


        @DisplayName("is bounded within the bar height")
        @Test
        void testMinAndMaxOfNoteYPositions() {
            assertAll(
                    () -> assertEquals(0, Collections.min(toneToYMap.values())),
                    () -> assertEquals(barHeight, Collections.max(toneToYMap.values()))
            );
        }

        @DisplayName("produces y-values for notes, since they are also tones")
        @Test
        void testNotesAsInput() {
            // C4 - Middle C is included in every staff
            Note middleCNote = NoteTable.get(Music.PitchClass.C_NATURAL, 4,
                    Music.NoteLength.QUARTER_NOTE);
            assertDoesNotThrow(() ->toneToYMap.get(middleCNote));
        }
    }

    @Nested
    @DisplayName("toneToBarlineYMap")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class ToneToBarLineYMap {

        @DisplayName("has barlines on the right tones for each clef")
        @ParameterizedTest
        @MethodSource("provideBarLineTones")
        void testCorrectTonesForClef(Music.Clef clef, Tone[] clefTones) {
            notePositionDict = new ScorePositionDict(barHeight, clef);
            HashMap<Tone, Float> toneToBarlineYMap = notePositionDict.getToneToBarlineYMap();
            for (Tone tone : clefTones) {
                assertAll(
                        () -> assertTrue(toneToBarlineYMap.containsKey(tone))
                );
            }
        }


        /**
         * Provides arguments for testCorrectTonesForClef
         *
         * @return stream of arguments: (Music.Staff, Tone[])
         */
        Stream<Arguments> provideBarLineTones() {
            Tone[] trebleTones = {ToneTable.get(Music.PitchClass.E_NATURAL, 4),
                    ToneTable.get(Music.PitchClass.G_NATURAL, 4),
                    ToneTable.get(Music.PitchClass.B_NATURAL, 4),
                    ToneTable.get(Music.PitchClass.D_NATURAL, 5),
                    ToneTable.get(Music.PitchClass.F_NATURAL, 5)};

            Tone[] bassTones = {ToneTable.get(Music.PitchClass.G_NATURAL, 2),
                    ToneTable.get(Music.PitchClass.B_NATURAL, 2),
                    ToneTable.get(Music.PitchClass.D_NATURAL, 3),
                    ToneTable.get(Music.PitchClass.F_NATURAL, 3),
                    ToneTable.get(Music.PitchClass.A_NATURAL, 3)};
            return Stream.of(
                    Arguments.of(Music.Clef.TREBLE_CLEF, trebleTones),
                    Arguments.of(Music.Clef.BASS_CLEF, bassTones));
        }
    }

    @Nested
    @DisplayName("yToToneMap")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class YToToneMap {
        HashMap<Float, Tone> yToToneMap;

        @BeforeEach
        void initYToToneMap() {
            yToToneMap = notePositionDict.getYToToneMap();
        }

        @DisplayName("only contains natural notes")
        @Test
        void verifyOnlyNaturalNotes() {
            for (Tone tone : yToToneMap.values()) {
                assertSame(tone.getPitchClass().getAccidental(), Music.PitchClass.Accidental.NATURAL);
            }
        }
    }
}
