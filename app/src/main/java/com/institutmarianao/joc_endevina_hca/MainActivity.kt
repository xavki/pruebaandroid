package com.institutmarianao.joc_endevina_hca

import android.content.pm.ActivityInfo
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var imageViews: Array<ImageView>
    private lateinit var playButton: Button
    private var mediaPlayer: MediaPlayer? = null
    private var correctSoundKey: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        imageViews = arrayOf(
            findViewById(R.id.imageView1),
            findViewById(R.id.imageView2),
            findViewById(R.id.imageView3)
        )
        playButton = findViewById(R.id.playButton)

        assignRandomImages()
        playButton.setOnClickListener { playRandomSound() }

        imageViews.forEachIndexed { index, imageView ->
            imageView.setOnClickListener { checkAnswer(index) }
        }
    }



    private fun playRandomSound() {
        val sounds = getAllResources("raw")
        val selectedEntry = imageViews.random().tag as String
        correctSoundKey = selectedEntry

        val soundId = sounds[selectedEntry] ?: return

        mediaPlayer?.release()
        mediaPlayer = MediaPlayer.create(this, soundId)
        mediaPlayer?.start()
    }

    private fun checkAnswer(selectedIndex: Int) {
        val selectedKey = imageViews[selectedIndex].tag as String

        if (selectedKey == correctSoundKey) {
            playFeedbackSound(R.raw.win)
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show()
            assignRandomImages()
            playRandomSound()
        } else {
            playFeedbackSound(R.raw.fail)
            Toast.makeText(this, "Try again!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun playFeedbackSound(soundRes: Int) {
        val feedbackPlayer = MediaPlayer.create(this, soundRes)
        feedbackPlayer.start()
    }

    private fun getAllResources(type: String): Map<String, Int> {
        val res = resources
        val packageName = packageName

        val names = listOf(
            "butterflies", "classic", "dueling_dragons", "juiced", "lantern", "party_time",
            "reaper", "splash", "sub_zero", "carbonator", "siesmic", "flame_rain",
            "rocket_mancer", "air_strike"
        )

        val resMap = mutableMapOf<String, Int>()

        for (name in names) {
            val resId = res.getIdentifier(name, type, packageName)
            if (resId != 0) {
                resMap[name] = resId
            }
        }
        return resMap
    }
}
