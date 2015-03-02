package com.sqlajt_orajt;

import java.util.HashMap;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

public class SoundProvider {
	public static final int START = R.raw.millionaire_game_start;
	public static final int CORRECT = R.raw.millionaire_correct;
	public static final int WRONG = R.raw.millionaire_wrong;

	private static SoundPool soundPool;

	private static HashMap<Integer, Integer> soundPoolMap;
	private static final float volume = 0.6f;
	public static void enableSound(Context context) {
		soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 100);
		soundPoolMap = new HashMap<Integer, Integer>(3);
		soundPoolMap.put(START,
				soundPool.load(context, R.raw.millionaire_game_start, 1));
		soundPoolMap.put(CORRECT,
				soundPool.load(context, R.raw.millionaire_correct, 2));
		soundPoolMap.put(WRONG,
				soundPool.load(context, R.raw.millionaire_wrong, 3));
	}

	public void start(Context context) {
		if(soundPool == null || soundPoolMap == null)
			enableSound(context);
		//soundPool.play(soundPoolMap.get(1), volume, volume, 1, 0, 1f);
		MediaPlayer mp = MediaPlayer.create(context, 1); 
		
		mp.start();
	}

	public void fail(Context context) {
		if(soundPool == null || soundPoolMap == null)
			enableSound(context);
		soundPool.play(soundPoolMap.get(3), volume, volume, 1, 0, 1f);
	}

	public void correct(Context context) {
		if(soundPool == null || soundPoolMap == null)
			enableSound(context);
		soundPool.play(soundPoolMap.get(2), volume, volume, 1, 0, 1f);
	}

	public static void select_A() {

	}

	public static void select_B() {

	}

	public static void select_C() {

	}

	public static void select_D() {

	}

}
