import android.util.Log
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

const val API_IP = "192.168.4.2"

fun sendXYZtoPython(roll: Float, throttle: Float, pitch: Float) {
    val client = OkHttpClient()
    val json = """{"roll":$roll, "throttle":$throttle, "pitch":$pitch}"""
    val body = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

    val request = Request.Builder()
        .url("http://$API_IP:5000/update")
        .post(body)
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: java.io.IOException) {
            Log.e("HTTP", "Hata: ${e.message}")
        }
        override fun onResponse(call: Call, response: Response) {
            val bodyStr = response.body?.string()
            Log.d("HTTP", "Başarılı cevap: $bodyStr")
        }
    })
}

fun sendCommandToPython(command: String) {
    val client = OkHttpClient()
    val json = """{"mode":"$command"}"""
    val body = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

    val request = Request.Builder()
        .url("http://$API_IP:5000/command")
        .post(body)
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: java.io.IOException) {
            Log.e("HTTP", "Komut gönderilemedi: ${e.message}")
        }
        override fun onResponse(call: Call, response: Response) {
            val bodyStr = response.body?.string()
            Log.d("HTTP", "Komut cevabı: $bodyStr")
        }
    })
}
