package musicconstants;

import java.util.Hashtable;
import java.util.Objects;

public class MidiNoteDict {
    private static final String TAG = "MidiNoteDict";
    private final int startingMidiNum = 21;   // start at midi number 21 (the first key on an 88-key piano)
    private final int endingMidiNum = 108;    // end at midi number 108 (the last key on an 88-key piano)
    private Hashtable<Integer, Tone> midiNoteDict;

    public MidiNoteDict() {
        buildMidiNoteDict();
    }

    private void buildMidiNoteDict() {
        // The dictionary is built only using sharps and naturals, ignoring enharmonic equivalents
        // Create an array of PitchClass constants to iterate over so we can build midiNoteDict
        // The 1st element in pitchClasses must match the pitch class of the startingMidiNum
        final PitchClass[] pitchClasses = {
                PitchClass.A_NATURAL, PitchClass.A_SHARP, PitchClass.B_NATURAL, PitchClass.C_NATURAL,
                PitchClass.C_SHARP, PitchClass.D_NATURAL, PitchClass.D_SHARP, PitchClass.E_NATURAL,
                PitchClass.F_NATURAL, PitchClass.F_SHARP, PitchClass.G_NATURAL, PitchClass.G_SHARP};

        midiNoteDict = new Hashtable<Integer, Tone>();  // initialize the dictionary

        // Prepare for the loop:
        int currentPitchClassIdx = 0; // Index variable points to the current pitch class in pitchClasses
        PitchClass currentPitchClass;   // variable to store the current pitch class
        int currentOctave = 0; // Variable to hold the current octave in which we are in

        for (int midiNum = startingMidiNum; midiNum <= endingMidiNum; midiNum++) {
            currentPitchClass = pitchClasses[currentPitchClassIdx]; // get the current pitch class
            // Anytime we reach the pitch class C natural, we have moved up an octave
            if (currentPitchClass == PitchClass.C_NATURAL) {
                currentOctave++;
            }

            // Use the midi number as the key, and a tone object containing both the pitch class and octave
            midiNoteDict.put(midiNum, new Tone(currentPitchClass, currentOctave));

            // Move to the next pitch class in the pitchClasses array
            currentPitchClassIdx++;
            // if we have moved the index out of the array, reset it back to 0
            if (currentPitchClassIdx > pitchClasses.length - 1) {
                currentPitchClassIdx = 0;
            }
        }
    }

    public Tone getTone(int midiNum) {
        return Objects.requireNonNull(midiNoteDict.get(midiNum));
    }

    public PitchClass getPitchClass(int midiNum) {
        return getTone(midiNum).getPitchClass();
    }

    public int getOctave(int midiNum) {
        return getTone(midiNum).getOctave();
    }
}