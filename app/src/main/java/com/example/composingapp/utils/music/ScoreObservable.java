package com.example.composingapp.utils.music;

import com.example.composingapp.utils.interfaces.Observable;
import com.example.composingapp.utils.interfaces.Observer;

import java.util.ArrayList;

public class ScoreObservable implements Observable {
    private static final String TAG = "Staff";
    private ArrayList<BarObserver> mBarObserverList;
    private Music.Clef mClef;
    private Music.NoteLength mBeatUnit;
    private int mBeatsPerBar;

    public ScoreObservable(Music.Clef mClef, Music.NoteLength mBeatUnit, int mBeatsPerBar) {
        this.mClef = mClef;
        this.mBeatUnit = mBeatUnit;
        this.mBeatsPerBar = mBeatsPerBar;
        mBarObserverList = new ArrayList<>();
    }

    public Music.Clef getClef() {
        return mClef;
    }

    public void setClef(Music.Clef mClef) {
        this.mClef = mClef;
    }

    public Music.NoteLength getBeatUnit() {
        return mBeatUnit;
    }

    public void setBeatUnit(Music.NoteLength mBeatUnit) {
        this.mBeatUnit = mBeatUnit;
    }

    public int getBeatsPerBar() {
        return mBeatsPerBar;
    }

    public void setBeatsPerBar(int mBeatsPerBar) {
        this.mBeatsPerBar = mBeatsPerBar;
    }

    @Override
    public void addObserver(Observer barObserver) {
        mBarObserverList.add((BarObserver) barObserver);
    }

    @Override
    public void removeObserver(Observer barObserver) {
        mBarObserverList.remove(barObserver);
    }

    @Override
    public void updateObservers() {
        for (BarObserver barObserver : mBarObserverList) {
            barObserver.update();
        }
    }
}
