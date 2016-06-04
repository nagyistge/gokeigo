package com.id11236662.gokeigo.util;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.Locale;

/**
 * This helper class is for calling the TTS API.
 * Tutorials: http://code.tutsplus.com/tutorials/use-text-to-speech-on-android-to-read-out-incoming-messages--cms-22524
 * http://www.tutorialspoint.com/android/android_text_to_speech.htm
 */

public class Speaker implements TextToSpeech.OnInitListener {

    private TextToSpeech mTts;
    private boolean mIsReady = false;

    public Speaker(Context context) {
        mTts = new TextToSpeech(context, this);
    }

    /**
     * Called to signal the completion of the TextToSpeech engine initialization.
     *
     * @param status {@link TextToSpeech#SUCCESS} or {@link TextToSpeech#ERROR}.
     */
    @Override
    public void onInit(int status) {
        Log.d(Constants.TAG, "Speaker.onInit");
        if (status == TextToSpeech.SUCCESS) {
            Log.d(Constants.TAG, "Speaker.onInit - SUCCESS");

            // Change to Japanese locale so it doesn't matter where the device is at.

            mTts.setLanguage(Locale.JAPANESE);
            mIsReady = true;
        } else {
            Log.d(Constants.TAG, "Speaker.onInit - FAIL");
            mIsReady = false;
        }
    }

    public void speak(String text) {

        // Speak only if the TTS is ready and the user has allowed speech.

        if (mIsReady) {
            mTts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    /**
     * Free up resource when the TTS engine is no longer needed.
     */

    public void destroy() {
        mTts.shutdown();
    }
}

