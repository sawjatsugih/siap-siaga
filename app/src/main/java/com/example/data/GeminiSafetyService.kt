package com.example.data

import android.util.Log
import com.example.BuildConfig
import com.example.model.DisasterType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.concurrent.TimeUnit

data class SafetyStep(
    val stepNumber: Int,
    val title: String,
    val description: String,
    val iconEmoji: String
)

data class SafetyTipsResult(
    val steps: List<SafetyStep>,
    val isAiGenerated: Boolean,
    val modelName: String = "gemini-3.5-flash",
    val errorMessage: String? = null
)

object GeminiSafetyService {
    private const val TAG = "GeminiSafetyService"
    private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent"

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    suspend fun getSafetySteps(disaster: DisasterType): SafetyTipsResult = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Exception) {
            ""
        }

        // If key is missing or dummy placeholder, return authoritative default safety protocol
        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            Log.d(TAG, "Gemini API key is not configured; using offline safety protocol")
            return@withContext SafetyTipsResult(
                steps = getDefaultSafetySteps(disaster),
                isAiGenerated = false
            )
        }

        val prompt = """
            Kamu adalah instruktur mitigasi dan tanggap darurat bencana profesional BNPB.
            Berikan tepat 3 langkah keselamatan paling krusial dan mendesak ("What to do") saat terjadi bencana: ${disaster.title} (${disaster.emergencySubtitle}).
            
            Keluarkan jawaban HANYA dalam format JSON valid tanpa teks pembuka atau penutup dengan struktur:
            {
              "steps": [
                {
                  "title": "Judul langkah (3-5 kata)",
                  "description": "Instruksi tindakan keselamatan yang jelas dan praktis (1-2 kalimat)"
                },
                {
                  "title": "Judul langkah kedua",
                  "description": "Instruksi tindakan kedua"
                },
                {
                  "title": "Judul langkah ketiga",
                  "description": "Instruksi tindakan ketiga"
                }
              ]
            }
        """.trimIndent()

        try {
            val jsonPayload = JSONObject().apply {
                val contentsArray = org.json.JSONArray().apply {
                    val contentObj = JSONObject().apply {
                        val partsArray = org.json.JSONArray().apply {
                            put(JSONObject().apply { put("text", prompt) })
                        }
                        put("parts", partsArray)
                    }
                    put(contentObj)
                }
                put("contents", contentsArray)

                val generationConfig = JSONObject().apply {
                    put("responseMimeType", "application/json")
                    put("temperature", 0.4)
                }
                put("generationConfig", generationConfig)
            }

            val requestBody = jsonPayload.toString().toRequestBody("application/json".toMediaType())
            val request = Request.Builder()
                .url("$BASE_URL?key=$apiKey")
                .post(requestBody)
                .build()

            val response = okHttpClient.newCall(request).execute()
            if (!response.isSuccessful) {
                val errBody = response.body?.string() ?: "HTTP ${response.code}"
                Log.e(TAG, "Gemini API failed: $errBody")
                return@withContext SafetyTipsResult(
                    steps = getDefaultSafetySteps(disaster),
                    isAiGenerated = false,
                    errorMessage = "Gagal memuat dari AI (${response.code}). Menggunakan panduan standar."
                )
            }

            val responseString = response.body?.string() ?: ""
            val parsedResult = parseSafetyStepsJson(responseString, disaster)
            if (parsedResult.isNotEmpty()) {
                return@withContext SafetyTipsResult(
                    steps = parsedResult,
                    isAiGenerated = true
                )
            } else {
                return@withContext SafetyTipsResult(
                    steps = getDefaultSafetySteps(disaster),
                    isAiGenerated = false
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception calling Gemini API: ${e.message}", e)
            return@withContext SafetyTipsResult(
                steps = getDefaultSafetySteps(disaster),
                isAiGenerated = false,
                errorMessage = e.message
            )
        }
    }

    private fun parseSafetyStepsJson(rawJson: String, disaster: DisasterType): List<SafetyStep> {
        try {
            val root = JSONObject(rawJson)
            val candidates = root.optJSONArray("candidates")
            val firstCandidate = candidates?.optJSONObject(0)
            val content = firstCandidate?.optJSONObject("content")
            val parts = content?.optJSONArray("parts")
            val text = parts?.optJSONObject(0)?.optString("text") ?: return emptyList()

            // The model returns the json string inside text
            val stepsJson = JSONObject(text.trim().removePrefix("```json").removePrefix("```").removeSuffix("```").trim())
            val stepsArray = stepsJson.optJSONArray("steps") ?: return emptyList()

            val list = mutableListOf<SafetyStep>()
            val emojis = when (disaster) {
                DisasterType.GEMPA_BUMI -> listOf("🛡️", "🪟", "🚪")
                DisasterType.BANJIR -> listOf("⚡", "🏔️", "🌊")
                DisasterType.TANAH_LONGSOR -> listOf("🏃", "🛡️", "⚠️")
                DisasterType.KEBAKARAN -> listOf("💨", "🚪", "🧯")
                DisasterType.TSUNAMI -> listOf("🏃", "🏔️", "📻")
                DisasterType.KEKERINGAN -> listOf("💧", "🌱", "☀️")
                DisasterType.ANGIN_TOPAN -> listOf("🏠", "🪟", "🛡️")
                DisasterType.GUNUNG_BERAPI -> listOf("😷", "👓", "🚪")
                DisasterType.ABRASI -> listOf("🌱", "🧱", "🏖️")
                DisasterType.PERUBAHAN_IKLIM -> listOf("💡", "🚲", "🌳")
                DisasterType.KONFLIK_SOSIAL -> listOf("🏠", "📱", "🤝")
            }

            for (i in 0 until minOf(stepsArray.length(), 3)) {
                val stepObj = stepsArray.getJSONObject(i)
                val title = stepObj.optString("title", "Langkah Keselamatan ${i + 1}")
                val description = stepObj.optString("description", "")
                list.add(
                    SafetyStep(
                        stepNumber = i + 1,
                        title = title,
                        description = description,
                        iconEmoji = emojis.getOrElse(i) { "⚠️" }
                    )
                )
            }
            return list
        } catch (e: Exception) {
            Log.e(TAG, "Error parsing JSON from Gemini: ${e.message}")
            return emptyList()
        }
    }

    fun getDefaultSafetySteps(disaster: DisasterType): List<SafetyStep> {
        return when (disaster) {
            DisasterType.GEMPA_BUMI -> listOf(
                SafetyStep(
                    stepNumber = 1,
                    title = "Drop, Cover, and Hold On",
                    description = "Segera berlutut di lantai, lindungi kepala dan leher, lalu berlindung di bawah meja yang kokoh hingga guncangan mereda.",
                    iconEmoji = "🛡️"
                ),
                SafetyStep(
                    stepNumber = 2,
                    title = "Jauhi Kaca dan Benda Gantung",
                    description = "Menjauhlah dari jendela kaca, cermin, lemari tinggi, dan lampu gantung yang rawan runtuh atau pecah menimpa Anda.",
                    iconEmoji = "🪟"
                ),
                SafetyStep(
                    stepNumber = 3,
                    title = "Evakuasi via Tangga Darurat",
                    description = "Setelah guncangan berhenti, segera keluar menuju titik kumpul terbuka menggunakan tangga darurat. Jangan pernah memakai lift.",
                    iconEmoji = "🚪"
                )
            )
            DisasterType.BANJIR -> listOf(
                SafetyStep(
                    stepNumber = 1,
                    title = "Putus Aliran Listrik dan Gas",
                    description = "Segera matikan sakelar MCB utama dan cabut regulator tabung gas untuk mencegah korsleting mematikan atau ledakan gas.",
                    iconEmoji = "⚡"
                ),
                SafetyStep(
                    stepNumber = 2,
                    title = "Evakuasi ke Tempat Lebih Tinggi",
                    description = "Bawa tas siaga bencana dan segera bergerak ke lantai atas atau posko pengungsian di dataran tinggi sebelum arus deras menutup akses.",
                    iconEmoji = "🏔️"
                ),
                SafetyStep(
                    stepNumber = 3,
                    title = "Jangan Menerobos Arus Air",
                    description = "Arus air setinggi 15-30 cm berarus kencang dapat merobohkan orang dewasa dan menghanyutkan kendaraan. Hindari genangan berbahaya.",
                    iconEmoji = "🌊"
                )
            )
            DisasterType.TANAH_LONGSOR -> listOf(
                SafetyStep(
                    stepNumber = 1,
                    title = "Lari Tegak Lurus Jalur Longsor",
                    description = "Jika terdengar gemuruh lereng, segera lari menyamping (tegak lurus) menjauhi arah luncuran tanah dan lumpur.",
                    iconEmoji = "🏃"
                ),
                SafetyStep(
                    stepNumber = 2,
                    title = "Lindungi Kepala dan Gulung Tubuh",
                    description = "Jika terjebak runtuhan, segera meringkuk seperti bola dan lindungi kepala serta leher dengan kedua tangan rapat.",
                    iconEmoji = "🛡️"
                ),
                SafetyStep(
                    stepNumber = 3,
                    title = "Waspadai Bahaya Longsor Susulan",
                    description = "Tetap berada di zona aman dan jangan sekali-kali mendekati tebing longsor sampai tim ahli geologi menyatakan stabil.",
                    iconEmoji = "⚠️"
                )
            )
            DisasterType.KEBAKARAN -> listOf(
                SafetyStep(
                    stepNumber = 1,
                    title = "Merangkak Rendah di Bawah Asap",
                    description = "Asap panas dan gas beracun naik ke atas. Merangkaklah dengan kepala 30 cm dari lantai dan tutupi hidung dengan kain basah.",
                    iconEmoji = "💨"
                ),
                SafetyStep(
                    stepNumber = 2,
                    title = "Raba Pintu Sebelum Membuka",
                    description = "Gunakan punggung tangan untuk menyentuh pegangan dan permukaan pintu. Jika terasa panas, cari jalur evakuasi lain.",
                    iconEmoji = "🚪"
                ),
                SafetyStep(
                    stepNumber = 3,
                    title = "Stop, Drop, and Roll Jika Terbakar",
                    description = "Jika pakaian terbakar, jangan berlari. Berhenti, jatuhkan diri ke lantai, lalu berguling-guling untuk memadamkan api, hubungi 113.",
                    iconEmoji = "🧯"
                )
            )
            DisasterType.TSUNAMI -> listOf(
                SafetyStep(
                    stepNumber = 1,
                    title = "Segera Lari ke Tempat Tinggi",
                    description = "Bila air laut surut drastis usai gempa kuat, lari secepat mungkin ke perbukitan atau bangunan beton kokoh setinggi minimal 20 meter.",
                    iconEmoji = "🏃"
                ),
                SafetyStep(
                    stepNumber = 2,
                    title = "Jauhi Pantai dan Muara Sungai",
                    description = "Gelombang tsunami melesat ratusan km/jam dan menyusup jauh ke muara sungai. Jangan pernah menonton gelombang dari bibir pantai.",
                    iconEmoji = "🏖️"
                ),
                SafetyStep(
                    stepNumber = 3,
                    title = "Tunggu Arahan Resmi BMKG/BNPB",
                    description = "Gelombang tsunami datang berulang dalam beberapa jam. Tetap di dataran tinggi sampai peringatan resmi BMKG dicabut.",
                    iconEmoji = "📻"
                )
            )
            DisasterType.KEKERINGAN -> listOf(
                SafetyStep(
                    stepNumber = 1,
                    title = "Prioritaskan Air untuk Kebutuhan Pokok",
                    description = "Gunakan cadangan air bersih secara ketat untuk konsumsi minum, memasak, dan kebersihan diri paling krusial.",
                    iconEmoji = "💧"
                ),
                SafetyStep(
                    stepNumber = 2,
                    title = "Simpan Air Bersih Tertutup Rapat",
                    description = "Tampung air dalam wadah bersih tertutup untuk mencegah kontaminasi kotoran dan sarang nyamuk demam berdarah.",
                    iconEmoji = "🪣"
                ),
                SafetyStep(
                    stepNumber = 3,
                    title = "Gunakan Air Daur Ulang Non-Konsumsi",
                    description = "Gunakan sisa air cuci beras atau sayuran untuk menyiram tanaman di pagi atau sore hari agar tidak menguap cepat.",
                    iconEmoji = "🌱"
                )
            )
            DisasterType.ANGIN_TOPAN -> listOf(
                SafetyStep(
                    stepNumber = 1,
                    title = "Masuk Ruangan Tengah Tanpa Jendela",
                    description = "Segera berlindung di ruangan paling tengah di lantai dasar (seperti kamar mandi atau lorong) berdinding semen kokoh.",
                    iconEmoji = "🏠"
                ),
                SafetyStep(
                    stepNumber = 2,
                    title = "Jauhi Kaca dan Papan Reklame",
                    description = "Hindari jendela kaca lebar, pintu kaca, serta pohon rindang atau tiang listrik yang mudah roboh terhempas angin.",
                    iconEmoji = "🪟"
                ),
                SafetyStep(
                    stepNumber = 3,
                    title = "Lindungi Kepala dengan Bantal/Matras",
                    description = "Gunakan kasur, bantal tebal, atau helm untuk melindungi kepala dan tengkuk dari reruntuhan genteng atau plafon.",
                    iconEmoji = "🛡️"
                )
            )
            DisasterType.GUNUNG_BERAPI -> listOf(
                SafetyStep(
                    stepNumber = 1,
                    title = "Pakai Masker dan Kacamata Goggles",
                    description = "Kenakan masker N95 atau kain basah serta kacamata tertutup untuk mencegah silika abu vulkanik mengikis paru dan kornea.",
                    iconEmoji = "😷"
                ),
                SafetyStep(
                    stepNumber = 2,
                    title = "Tutup Rapat Celah Rumah",
                    description = "Tutup rapat ventilasi, pintu, dan jendela dengan kain basah untuk mencegah debu abu vulkanik beracun masuk ke dalam hunian.",
                    iconEmoji = "🚪"
                ),
                SafetyStep(
                    stepNumber = 3,
                    title = "Jauhi Daerah Aliran Sungai",
                    description = "Hindari lembah sungai di kaki gunung karena ancaman banjir lahar dingin yang dapat menyapu material ratusan ton saat hujan.",
                    iconEmoji = "🏔️"
                )
            )
            DisasterType.ABRASI -> listOf(
                SafetyStep(
                    stepNumber = 1,
                    title = "Jauhi Bibir Tebing Pantai Retak",
                    description = "Jangan berdiri atau beraktivitas di dekat tepi tebing pesisir yang terkikis karena struktur tanah bawah rentan amblas tiba-tiba.",
                    iconEmoji = "⚠️"
                ),
                SafetyStep(
                    stepNumber = 2,
                    title = "Tanam Mangrove & Pelindung Pantai",
                    description = "Tanam bibit pohon bakau dan cemara laut bersama komunitas untuk meredam kekuatan gelombang pasang pengikis pantai.",
                    iconEmoji = "🌱"
                ),
                SafetyStep(
                    stepNumber = 3,
                    title = "Relokasi Hunian dari Garis Pasang",
                    description = "Patuhi garis sempadan pantai dan pindahkan barang berharga jika gelombang pasang telah mencapai fondasi bangunan.",
                    iconEmoji = "🏖️"
                )
            )
            DisasterType.PERUBAHAN_IKLIM -> listOf(
                SafetyStep(
                    stepNumber = 1,
                    title = "Hemat Konsumsi Listrik & Bahan Bakar",
                    description = "Matikan alat elektronik yang tidak dipakai dan gunakan transportasi aktif atau publik untuk meminimalkan jejak karbon.",
                    iconEmoji = "💡"
                ),
                SafetyStep(
                    stepNumber = 2,
                    title = "Reboisasi dan Penghijauan Mandiri",
                    description = "Tanam minimal satu pohon peneduh di pekarangan rumah untuk menyerap CO2 dan meredam hawa panas pulau perkotaan.",
                    iconEmoji = "🌳"
                ),
                SafetyStep(
                    stepNumber = 3,
                    title = "Kelola Sampah dan Stop Pembakaran",
                    description = "Pisahkan sampah organik untuk kompos dan hindari membakar sampah plastik yang melepaskan gas rumah kaca berbahaya.",
                    iconEmoji = "♻️"
                )
            )
            DisasterType.KONFLIK_SOSIAL -> listOf(
                SafetyStep(
                    stepNumber = 1,
                    title = "Kunci Pintu Rumah dan Tetap di Dalam",
                    description = "Tetap berada di dalam hunian yang aman, kunci pintu dan jendela, serta hindari kerumunan massa yang memanas di jalan.",
                    iconEmoji = "🏠"
                ),
                SafetyStep(
                    stepNumber = 2,
                    title = "Saring Berita dan Tolak Provokasi",
                    description = "Jangan menyebarkan kabar burung atau video provokatif yang belum terverifikasi ke media sosial agar tidak memicu kepanikan.",
                    iconEmoji = "📱"
                ),
                SafetyStep(
                    stepNumber = 3,
                    title = "Utamakan Dialog dan Lapor Aparat",
                    description = "Jika terjadi perselisihan antarkelompok, kedepankan mediasi damai dan laporkan potensi kekerasan kepada kepolisian terdekat.",
                    iconEmoji = "🤝"
                )
            )
        }
    }
}
