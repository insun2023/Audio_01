import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.IOException

class RecordActivity : ComponentActivity() {
    private var mediaRecorder: MediaRecorder? = null
    private var isRecording by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface {
                    RecordScreen()
                }
            }
        }
    }

    @Composable
    fun RecordScreen() {
        val context = LocalContext.current

        Column {
            Button(
                onClick = {
                    if (isRecording) {
                        stopRecording()
                    } else {
                        if (checkPermission()) {
                            startRecording()
                        } else {
                            requestPermission()
                        }
                    }
                    isRecording = !isRecording
                }
            ) {
                Text(text = if (isRecording) "Stop Recording" else "Start Recording")
            }
        }
    }

    private fun startRecording() {
        mediaRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            setOutputFile(getOutputFilePath())
            try {
                prepare()
                start()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun stopRecording() {
        mediaRecorder?.apply {
            stop()
            release()
        }
        mediaRecorder = null
    }

    private fun checkPermission(): Boolean {
        val permission = Manifest.permission.RECORD_AUDIO
        return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, permission)
    }

    private fun requestPermission() {
        val permission = Manifest.permission.RECORD_AUDIO
        ActivityCompat.requestPermissions(this, arrayOf(permission), 0)
    }

    private fun getOutputFilePath(): String {
        // Define your output file path here
        return ""
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaRecorder?.release()
    }
}
