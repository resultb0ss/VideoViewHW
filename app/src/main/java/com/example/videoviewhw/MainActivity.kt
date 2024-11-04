package com.example.videoviewhw

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.MediaController
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.videoviewhw.databinding.ActivityMainBinding

val GALLERY_REQUEST = 300

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private val videoList = listOf(
        R.raw.one,
        R.raw.two,
        R.raw.three
    )
    private var videoIndex = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mediaController = MediaController(this)
        mediaController.setAnchorView(mediaController)

        val offlineUri = Uri.parse("android.resource://$packageName/${videoList[videoIndex]}")
        binding.videoView.setMediaController(mediaController)
        binding.videoView.setVideoURI(offlineUri)
        binding.videoView.requestFocus()
        binding.videoView.start()

//        val path = ""
//        binding.videoView.setVideoPath(path)

        binding.nextButton.setOnClickListener{
            if (binding.videoView.isPlaying){
                binding.videoView.stopPlayback()
                if (videoIndex < videoList.size){
                    videoIndex += 1
                    videoPlayIndex(videoIndex)
                }
                else {
                    Toast.makeText(this, "Видео больше нет в папке",
                        Toast.LENGTH_LONG).show()
                }
                Log.d("@@@", "VideoIndex = $videoIndex  LastIndex = ${videoList.lastIndex}")


            }
        }

        binding.previousButton.setOnClickListener{
            if (binding.videoView.isPlaying){
                binding.videoView.stopPlayback()
                if (videoIndex < 0){
                    Toast.makeText(this, "Видео больше нет в папке",
                        Toast.LENGTH_LONG).show()
                } else if (videoIndex == 0) {
                    videoPlayFirst()
                }
                else {
                    videoIndex -= 1
                    videoPlayIndex(videoIndex)
                }

            }
        }

        binding.FolderBtn.setOnClickListener {
            val videoPickerIntent = Intent(Intent.ACTION_PICK)
            videoPickerIntent.type = "video/*"
            startActivityForResult(videoPickerIntent,GALLERY_REQUEST)
        }



    }

    fun videoPlayIndex(videoIndex: Int){
        val offlineUri = Uri.parse("android.resource://$packageName/${videoList[videoIndex]}")
        binding.videoView.setVideoURI(offlineUri)
        binding.videoView.requestFocus()
        binding.videoView.start()
    }


    fun videoPlayFirst(){
        val offlineUri = Uri.parse("android.resource://$packageName/${videoList[0]}")
        binding.videoView.setVideoURI(offlineUri)
        binding.videoView.requestFocus()
        binding.videoView.start()
    }

    fun videoPlayLast(){
        val offlineUri =
            Uri.parse("android.resource://$packageName/${videoList[videoList.lastIndex]}")
        binding.videoView.setVideoURI(offlineUri)
        binding.videoView.requestFocus()
        binding.videoView.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            GALLERY_REQUEST -> if(resultCode == RESULT_OK){
                val videoUri = data?.data
                if (binding.videoView.isPlaying){
                    binding.videoView.stopPlayback()
                    binding.videoView.setVideoURI(videoUri)
                    binding.videoView.requestFocus()
                    binding.videoView.start()
                } else {
                    binding.videoView.setVideoURI(videoUri)
                    binding.videoView.requestFocus()
                    binding.videoView.start()
                }

            } else {
                videoPlayIndex(0)
            }

        }
    }
}