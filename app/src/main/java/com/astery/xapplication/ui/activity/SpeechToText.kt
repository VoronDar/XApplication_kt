package com.astery.xapplication.ui.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.speech.RecognizerIntent
import android.widget.EditText
import android.widget.Toast
import com.astery.xapplication.R
import com.astery.xapplication.ui.activity.interfaces.SearchUsable
import timber.log.Timber


class SpeechToText(val activity: MainActivity) {

    var fragment:SearchUsable? = null


    fun isRecognitionAvailable():Boolean {
        val manager: PackageManager = activity.packageManager
        val activities =
            manager.queryIntentActivities(Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0)
        return activities.isNotEmpty()
    }

    fun speak() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, javaClass.`package`!!.name)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH
        )
        activity.startActivityForResult(intent, VOICE_RECOGNITION_REQUEST)
    }

    fun acceptActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        Timber.d("recognition $data $requestCode $resultCode")
        if (requestCode == VOICE_RECOGNITION_REQUEST) {
            if (data != null) {
                Timber.d("$data")
                val matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                if (matches!!.isNotEmpty()) {
                    activity.getSearchText(matches[0])
                    fragment?.getSearchText(matches[0])
                }
            } else {
                Toast.makeText(activity, "распознавание не удалось", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object{

        const val FILTER_REQUEST = 41
        const val FILTER_RESULT_OK = 1
        const val FILTER_TYPE_PARAM = "type"
        const val VOICE_RECOGNITION_REQUEST = 111
    }

}

