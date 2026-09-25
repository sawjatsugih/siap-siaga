package com.example.model

import androidx.compose.ui.graphics.Color
import java.util.Random

data class EmergencyScenario(
    val question: String,
    val options: List<ScenarioOption>
)

data class ScenarioOption(
    val text: String,
    val isCorrect: Boolean,
    val explanation: String
)

enum class DisasterType(
    val id: String,
    val title: String,
    val defaultAlertTitle: String,
    val emoji: String,
    val primaryColor: Color,
    val secondaryColor: Color,
    val softColor: Color,
    val soundDescription: String,
    val emergencySubtitle: String,
    val quickActionTitle: String,
    val responseScenario: EmergencyScenario,
    val mitigationTips: List<String>
) {
    GEMPA_BUMI(
        id = "gempa",
        title = "Gempa Bumi",
        defaultAlertTitle = "GEMPA BUMI 7.2 SR!",
        emoji = "🏚️",
        primaryColor = Color(0xFFE65100), // Vibrant Amber Orange
        secondaryColor = Color(0xFFFFB300),
        softColor = Color(0xFFFFF3E0),
        soundDescription = "Gemuruh tektonik sub-bass bumi merekah, patahan dinding & sirine evakuasi!",
        emergencySubtitle = "Guncangan hebat terasa! Tanah merekah dan reruntuhan berjatuhan!",
        quickActionTitle = "TINDAKAN PERTAMA SAAT GEMPA",
        responseScenario = EmergencyScenario(
            question = "Guncangan hebat terasa dan lampu padam! Apa langkah keselamatan pertamamu?",
            options = listOf(
                ScenarioOption(
                    text = "DROP, COVER, HOLD ON! Merunduk di bawah meja kokoh & lindungi kepala",
                    isCorrect = true,
                    explanation = "Tepat sekali! Merunduk, berlindung di bawah perabot kokoh, dan berpegangan melindungi kepala dari runtuhan material."
                ),
                ScenarioOption(
                    text = "Segera berlari menuju balkon atau jendela untuk melihat situasi",
                    isCorrect = false,
                    explanation = "Sangat berbahaya! Jendela kaca rentan pecah dan balkon bisa runtuh seketika saat gempa berlangsung."
                ),
                ScenarioOption(
                    text = "Gunakan lift lantai atas agar cepat sampai ke lobi",
                    isCorrect = false,
                    explanation = "Jangan pernah memakai lift saat gempa! Lift bisa macet akibat listrik padam atau jalur rel elevator bengkok."
                )
            )
        ),
        mitigationTips = listOf(
            "Rumus 3B BNPB: Berlutut/Merunduk (Drop), Berlindung (Cover), dan Bertahan (Hold On).",
            "Jauhi kaca jendela, cermin, lemari tinggi, dan instalasi gantung.",
            "Jika di luar ruangan, segera cari tanah lapang yang jauh dari gedung dan tiang listrik.",
            "Setelah gempa reda, matikan kompor gas dan MCB listrik utama guna cegah kebakaran."
        )
    ),

    BANJIR(
        id = "banjir",
        title = "Banjir Bandang",
        defaultAlertTitle = "BANJIR BANDANG MELUAP!",
        emoji = "🌊",
        primaryColor = Color(0xFF0288D1), // Bright Ocean Blue
        secondaryColor = Color(0xFF4FC3F7),
        softColor = Color(0xFFE1F5FE),
        soundDescription = "Deru arus air bah bergelombang deras & sirine bahaya meluap!",
        emergencySubtitle = "Air bah meluap cepat! Arus deras menenggelamkan pemukiman!",
        quickActionTitle = "TINDAKAN EVAKUASI BANJIR",
        responseScenario = EmergencyScenario(
            question = "Air bah mulai masuk ke dalam rumah dengan cepat. Apa yang harus kamu lakukan pertama kali?",
            options = listOf(
                ScenarioOption(
                    text = "Matikan saklar MCB listrik utama & cabut regulator gas",
                    isCorrect = true,
                    explanation = "Benar! Mematikan MCB listrik mencegah sengatan listrik fatal di dalam air, dan melepas gas mencegah ledakan."
                ),
                ScenarioOption(
                    text = "Nyalakan pompa air dan coba menguras air sendiri",
                    isCorrect = false,
                    explanation = "Sangat berisiko tersetrum listrik saat menyalakan alat elektronik saat air sedang meluap."
                ),
                ScenarioOption(
                    text = "Berenang menerobos arus banjir di jalan raya",
                    isCorrect = false,
                    explanation = "Arus air setinggi 15 cm saja sudah mampu menyeret orang dewasa dan menyembunyikan selokan terbuka."
                )
            )
        ),
        mitigationTips = listOf(
            "Segera putus aliran listrik dari MCB utama dan amankan dokumen berharga dalam tas tahan air.",
            "Evakuasi ke tempat yang lebih tinggi sebelum jalur jalan tertutup air bah.",
            "Jangan pernah bermain atau berenang di genangan air banjir karena risiko infeksi bakteri dan sengatan listrik.",
            "Simpan nomor darurat BPBD dan siapkan tas siaga bencana (makanan instan, senter, P3K)."
        )
    ),

    TANAH_LONGSOR(
        id = "longsor",
        title = "Tanah Longsor",
        defaultAlertTitle = "TEBING LONGSOR RUNTUH!",
        emoji = "⛰️",
        primaryColor = Color(0xFF795548), // Warm Earth Brown
        secondaryColor = Color(0xFFBCAAA4),
        softColor = Color(0xFFEFEBE9),
        soundDescription = "Dentuman batu-batu raksasa saling beradu & gemeretak runtuhan tebing!",
        emergencySubtitle = "Lereng tebing runtuh! Massa tanah dan bebatuan meluncur deras!",
        quickActionTitle = "EVAKUASI CEPAT LONGSOR",
        responseScenario = EmergencyScenario(
            question = "Terdengar gemuruh tanah dan pohon-pohon di bukit mulai miring. Ke arah mana kamu harus lari?",
            options = listOf(
                ScenarioOption(
                    text = "Lari ke arah samping (tegak lurus) dari jalur luncuran longsor",
                    isCorrect = true,
                    explanation = "Tepat! Berlari menyamping (tegak lurus arah luncuran) adalah cara terbaik untuk keluar dari jalur runtuhan material."
                ),
                ScenarioOption(
                    text = "Lari lurus ke bawah searah dengan luncuran tanah",
                    isCorrect = false,
                    explanation = "Kecepatan longsoran tanah dapat mencapai puluhan km/jam dan akan menyusul orang yang berlari searah."
                ),
                ScenarioOption(
                    text = "Bersembunyi di balik dinding rumah kayu",
                    isCorrect = false,
                    explanation = "Beban massa tanah dan batu dapat meratakan bangunan biasa dengan mudah."
                )
            )
        ),
        mitigationTips = listOf(
            "Segera lari menyamping (tegak lurus jalur luncuran) saat mendengar suara gemuruh lereng.",
            "Jangan mendirikan pemukiman di bawah lereng terjal atau bantaran sungai bertebing curam.",
            "Jika tidak sempat lari, segera gulung tubuh seperti bola untuk melindungi kepala dan dada.",
            "Waspadai retakan tanah baru dan rembesan air keruh di lereng bukit pasca hujan lebat."
        )
    ),

    KEBAKARAN(
        id = "kebakaran",
        title = "Kebakaran",
        defaultAlertTitle = "KEBAKARAN BESAR BERKOBAR!",
        emoji = "🔥",
        primaryColor = Color(0xFFD32F2F), // Fiery Red
        secondaryColor = Color(0xFFFF5252),
        softColor = Color(0xFFFFEBEE),
        soundDescription = "Deru lidah api berkobar membakar, letupan kayu & alarm kebakaran melengking!",
        emergencySubtitle = "Api menjalar tak terkendali! Asap tebal mengepung ruangan!",
        quickActionTitle = "PENYELAMATAN DIRI DARI API",
        responseScenario = EmergencyScenario(
            question = "Ruangan dipenuhi asap hitam tebal dan pintu keluar terlihat jauh. Cara evakuasi terbaik adalah?",
            options = listOf(
                ScenarioOption(
                    text = "Merangkak rendah di lantai sambil menutup hidung dengan kain basah",
                    isCorrect = true,
                    explanation = "Sempurna! Asap panas dan gas beracun membubung ke atas. Udara bersih tersisa sekitar 30 cm di atas lantai."
                ),
                ScenarioOption(
                    text = "Berlari sambil bernapas dalam-dalam agar tidak sesak",
                    isCorrect = false,
                    explanation = "Menghirup asap beracun saat berlari dapat menyebabkan pingsan seketika dalam hitungan detik."
                ),
                ScenarioOption(
                    text = "Bersembunyi di dalam lemari atau di bawah tempat tidur",
                    isCorrect = false,
                    explanation = "Bersembunyi memperlambat proses evakuasi dan menyulitkan petugas pemadam kebakaran untuk menemukanmu."
                )
            )
        ),
        mitigationTips = listOf(
            "Merangkak rendah di bawah kepulan asap dan tutup mulut serta hidung dengan kain basah.",
            "Raba daun pintu dengan punggung tangan; bila terasa panas, jangan dibuka karena ada kobaran api di baliknya.",
            "Jika baju terbakar, lakukan STOP, DROP, & ROLL (Berhenti, Jatuhkan diri, Berguling di lantai).",
            "Sedia APAR di rumah dan ketahui nomor darurat pemadam kebakaran (113)."
        )
    ),

    TSUNAMI(
        id = "tsunami",
        title = "Tsunami",
        defaultAlertTitle = "PERINGATAN DINI TSUNAMI!",
        emoji = "🌊⚡",
        primaryColor = Color(0xFF00695C), // Teal Deep Ocean
        secondaryColor = Color(0xFF26A69A),
        softColor = Color(0xFFE0F2F1),
        soundDescription = "Gemuruh raksasa samudra menghantam daratan & sirine peringatan dini BMKG!",
        emergencySubtitle = "Air laut surut tiba-tiba! Gelombang raksasa mendekat ke pantai!",
        quickActionTitle = "EVAKUASI MANDIRI TSUNAMI",
        responseScenario = EmergencyScenario(
            question = "Setelah gempa kuat di pesisir, kamu melihat air laut surut drastis dan ikan terdampar. Apa tindakanmu?",
            options = listOf(
                ScenarioOption(
                    text = "Segera lari secepat mungkin ke tempat tinggi (>20 meter) atau bukit",
                    isCorrect = true,
                    explanation = "Benar sekali! Air laut surut cepat adalah tanda pasti gelombang tsunami dahsyat akan menerjang dalam hitungan menit."
                ),
                ScenarioOption(
                    text = "Turun ke pantai untuk mengambil ikan yang terdampar",
                    isCorrect = false,
                    explanation = "Fatal! Banyak korban tsunami terjebak karena tertarik mengambil ikan sebelum gelombang raksasa datang."
                ),
                ScenarioOption(
                    text = "Menaiki mobil dan menunggu aba-aba resmi dari pengeras suara",
                    isCorrect = false,
                    explanation = "Jalan pesisir rawan macet parah. Gunakan evakuasi jalan kaki atau berlari menuju perbukitan tinggi terdekat."
                )
            )
        ),
        mitigationTips = listOf(
            "Prinsip 20-20-20: Jika gempa terasa 20 detik, evakuasi dalam 20 menit ke ketinggian 20 meter.",
            "Jangan pernah menonton gelombang tsunami dari pinggir pantai atau jembatan.",
            "Gelombang tsunami biasanya datang lebih dari satu kali; gelombang kedua dan ketiga sering kali jauh lebih besar.",
            "Kenali jalur evakuasi tsunami terdekat di wilayah pesisir tempat tinggalmu."
        )
    ),

    KEKERINGAN(
        id = "kekeringan",
        title = "Kekeringan",
        defaultAlertTitle = "KRISIS KEKERINGAN EKSTRIM!",
        emoji = "☀️🏜️",
        primaryColor = Color(0xFFF57F17), // Sun Golden Amber
        secondaryColor = Color(0xFFFFD54F),
        softColor = Color(0xFFFFFDE7),
        soundDescription = "Deru angin kering terik, gesekan tanah tandus pecah & dengung panas matahari!",
        emergencySubtitle = "Sumber air mengering! Cuaca terik berkepanjangan melanda tanah pertanian!",
        quickActionTitle = "PENGELOLAAN KRISIS AIR",
        responseScenario = EmergencyScenario(
            question = "Krisis kekeringan melanda dan debit sumur menurun drastis. Prioritas penggunaan air yang benar adalah?",
            options = listOf(
                ScenarioOption(
                    text = "Prioritaskan untuk minum, memasak, dan kebersihan esensial",
                    isCorrect = true,
                    explanation = "Tepat! Hidrasi tubuh dan kebutuhan sanitasi dasar adalah prioritas mutlak untuk mencegah dehidrasi dan wabah penyakit."
                ),
                ScenarioOption(
                    text = "Gunakan air sumur untuk mencuci kendaraan dan halaman setiap hari",
                    isCorrect = false,
                    explanation = "Pemborosan air untuk hal non-esensial akan mempercepat kekeringan total sumber air bersih."
                ),
                ScenarioOption(
                    text = "Menimbun air sebanyak-banyaknya di wadah terbuka tanpa penutup",
                    isCorrect = false,
                    explanation = "Wadah air terbuka di musim kering menjadi tempat berkembang biak nyamuk demam berdarah (Aedes aegypti)."
                )
            )
        ),
        mitigationTips = listOf(
            "Terapkan perilaku hemat air: matikan keran saat menyikat gigi dan gunakan air bekas cucian untuk menyiram tanaman.",
            "Bangun sumur resapan dan biopori di pekarangan rumah untuk mengisi cadangan air tanah saat musim hujan.",
            "Tutup rapat tempat penampungan air agar tidak menjadi sarang nyamuk vektor penyakit.",
            "Gunakan mulsa organik pada tanah tanaman untuk menjaga kelembapan tanah lebih lama."
        )
    ),

    ANGIN_TOPAN(
        id = "topan",
        title = "Angin Topan",
        defaultAlertTitle = "BADAI ANGIN TOPAN MENERJANG!",
        emoji = "🌪️",
        primaryColor = Color(0xFF3949AB), // Indigo Storm
        secondaryColor = Color(0xFF7986CB),
        softColor = Color(0xFFE8EAF6),
        soundDescription = "Deru pusaran angin kencang berputar, atap seng berterbangan & desing badai dahsyat!",
        emergencySubtitle = "Pusaran angin dahsyat berkecepatan tinggi menghancurkan pepohonan dan bangunan!",
        quickActionTitle = "PERLINDUNGAN DARI ANGIN TOPAN",
        responseScenario = EmergencyScenario(
            question = "Pusaran angin kencang mendekat dan atap rumah mulai bergoyang. Di mana tempat berlindung paling aman?",
            options = listOf(
                ScenarioOption(
                    text = "Masuk ke ruangan paling tengah tanpa jendela di lantai dasar (seperti kamar mandi)",
                    isCorrect = true,
                    explanation = "Benar! Bagian tengah rumah berdinding bata tebal tanpa jendela melindungi dari serpihan terbang berkecepatan tinggi."
                ),
                ScenarioOption(
                    text = "Berdiri di dekat jendela kaca besar untuk merekam video badai",
                    isCorrect = false,
                    explanation = "Kaca jendela bisa pecah berkeping-keping akibat tekanan angin dan serpihan benda terbang yang menembus ke dalam."
                ),
                ScenarioOption(
                    text = "Berlindung di bawah pohon besar yang rindang di pekarangan",
                    isCorrect = false,
                    explanation = "Pohon besar berisiko tinggi tumbang atau tersambar petir menimpa orang di bawahnya."
                )
            )
        ),
        mitigationTips = listOf(
            "Berlindunglah di ruangan tengah lantai dasar tanpa jendela (seperti lorong atau kamar mandi).",
            "Jauhi pohon besar, papan reklame, tiang listrik, dan atap kanopi yang mudah terhempas.",
            "Pangkas dahan-dahan pohon besar di sekitar rumah secara berkala sebelum musim angin kencang tiba.",
            "Kunci semua pintu dan jendela, serta pasang lakban silang (X) pada kaca untuk meminimalkan pecahan tajam."
        )
    ),

    GUNUNG_BERAPI(
        id = "gunung_api",
        title = "Gunung Berapi",
        defaultAlertTitle = "ERUPSI GUNUNG API MELETUS!",
        emoji = "🌋",
        primaryColor = Color(0xFFBF360C), // Deep Lava Rust
        secondaryColor = Color(0xFFFF7043),
        softColor = Color(0xFFFBE9E7),
        soundDescription = "Dentuman ledakan vulkanik magmatik, gemuruh lahar panas & hujan abu belerang!",
        emergencySubtitle = "Gunung api erupsi memuntahkan awan panas, lava pijar, dan hujan abu pekat!",
        quickActionTitle = "PANDUAN KESELAMATAN ERUPSI",
        responseScenario = EmergencyScenario(
            question = "Erupsi terjadi dan hujan abu vulkanik pekat mulai turun. Apa perlindungan diri yang wajib dipakai?",
            options = listOf(
                ScenarioOption(
                    text = "Gunakan masker/kain basah, kacamata pelindung, dan pakaian tertutup",
                    isCorrect = true,
                    explanation = "Tepat! Abu vulkanik mengandung pecahan silika tajam yang merusak paru-paru dan kornea mata jika terhirup atau tergores."
                ),
                ScenarioOption(
                    text = "Menggunakan lensa kontak agar pandangan tetap jernih",
                    isCorrect = false,
                    explanation = "Jangan pernah memakai lensa kontak saat hujan abu! Butiran abu halus bisa terjepit di balik lensa dan merusak kornea."
                ),
                ScenarioOption(
                    text = "Mendekati aliran sungai untuk mencuci abu dari badan",
                    isCorrect = false,
                    explanation = "Lembah sungai di kaki gunung merupakan jalur utama lahar dingin yang sangat berbahaya dan mematikan."
                )
            )
        ),
        mitigationTips = listOf(
            "Pakai masker N95 atau kain basah untuk melindungi saluran pernapasan dari partikel silika abu vulkanik.",
            "Kenakan pakaian lengan panjang, celana panjang, dan kacamata pelindung (goggles).",
            "Patuhi zona radius bahaya KRB (Kawasan Rawan Bencana) yang ditetapkan PVMBG/BNPB.",
            "Hindari lembah dan daerah aliran sungai karena ancaman bahaya lahar dingin saat hujan lebat."
        )
    ),

    ABRASI(
        id = "abrasi",
        title = "Abrasi Pantai",
        defaultAlertTitle = "GARIS PANTAI TERKIKIS ABRASI!",
        emoji = "🏖️🌊",
        primaryColor = Color(0xFF00838F), // Deep Cyan Tide
        secondaryColor = Color(0xFF4DD0E1),
        softColor = Color(0xFFE0F7FA),
        soundDescription = "Hantaman ombak pasang konstan menghancurkan dinding pantai & reruntuhan tebing pesisir!",
        emergencySubtitle = "Gelombang pasang laut terus mengikis pesisir! Daratan terancam amblas ke laut!",
        quickActionTitle = "MITIGASI ABRASI PESISIR",
        responseScenario = EmergencyScenario(
            question = "Garis pantai di desamu makin menyusut dan mendekati pemukiman. Solusi ekologis jangka panjang terbaik adalah?",
            options = listOf(
                ScenarioOption(
                    text = "Menanam sabuk hijau hutan bakau (mangrove) dan terumbu karang",
                    isCorrect = true,
                    explanation = "Sempurna! Akar mangrove yang rapat menyerap energi ombak hingga 60% dan mengendapkan sedimen tanah secara alami."
                ),
                ScenarioOption(
                    text = "Menambang pasir pantai untuk dijual sebagai bahan bangunan",
                    isCorrect = false,
                    explanation = "Penambangan pasir pantai adalah penyebab utama abrasi makin parah karena menghilangkan pelindung alami pantai."
                ),
                ScenarioOption(
                    text = "Menebang pohon cemara udang pesisir agar pemandangan laut terbuka",
                    isCorrect = false,
                    explanation = "Menebang vegetasi pantai justru mempercepat kikisan tanah saat dihantam gelombang badai."
                )
            )
        ),
        mitigationTips = listOf(
            "Tanam dan lestarikan hutan mangrove serta vegetasi pesisir (cemara laut) sebagai peredam alami ombak.",
            "Bangun pemecah gelombang (breakwater) atau tanggul rip-rap batu di titik kritis pengikisan.",
            "Tegakkan peraturan zonasi sempadan pantai; jangan mendirikan bangunan permanen di bibir pasang tertinggi.",
            "Hindari kegiatan penambangan pasir pantai liar dan perusakan terumbu karang."
        )
    ),

    PERUBAHAN_IKLIM(
        id = "iklim",
        title = "Perubahan Iklim",
        defaultAlertTitle = "KRISIS ANOMALI IKLIM GLOBAL!",
        emoji = "🌍🌡️",
        primaryColor = Color(0xFF2E7D32), // Forest Eco Green
        secondaryColor = Color(0xFF81C784),
        softColor = Color(0xFFE8F5E9),
        soundDescription = "Frekuensi anomali cuaca ekstrim, alarm pemanasan global & deru angin kutub mencair!",
        emergencySubtitle = "Suhu bumi meningkat drastis! Cuaca ekstrim tidak menentu melanda dunia!",
        quickActionTitle = "AKSI NYATA CEGAH IKLIM",
        responseScenario = EmergencyScenario(
            question = "Sebagai individu, aksi sederhana sehari-hari yang paling efektif menekan emisi gas rumah kaca adalah?",
            options = listOf(
                ScenarioOption(
                    text = "Hemat listrik, kurangi sampah makanan, dan gunakan transportasi umum atau jalan kaki",
                    isCorrect = true,
                    explanation = "Tepat sekali! Menghemat energi, beralih ke mobilitas rendah emisi, dan mengomposkan sisa makanan langsung menekan jejak karbon."
                ),
                ScenarioOption(
                    text = "Membakar sampah plastik di halaman belakang setiap sore",
                    isCorrect = false,
                    explanation = "Membakar sampah plastik melepaskan racun dioksin dan gas karbon dioksida tinggi yang mempercepat pemanasan global."
                ),
                ScenarioOption(
                    text = "Menyalakan pendingin ruangan (AC) suhu 16°C seharian saat rumah kosong",
                    isCorrect = false,
                    explanation = "Penggunaan listrik berlebih dari pembangkit fosil membuang energi dan memperbanyak emisi karbon dioksida."
                )
            )
        ),
        mitigationTips = listOf(
            "Lakukan gerakan 3R (Reduce, Reuse, Recycle) dan kurangi konsumsi barang plastik sekali pakai.",
            "Hemat konsumsi listrik dengan mencabut colokan yang tidak digunakan dan matikan lampu saat siang.",
            "Gencarkan penanaman pohon di pekarangan rumah untuk menyerap CO2 dan menyejukkan lingkungan sekitar.",
            "Dukung gaya hidup ramah lingkungan dengan memprioritaskan jalan kaki, bersepeda, atau transportasi umum."
        )
    ),

    KONFLIK_SOSIAL(
        id = "konflik",
        title = "Konflik Sosial",
        defaultAlertTitle = "PERINGATAN KERUSUHAN SOSIAL!",
        emoji = "🛡️🤝",
        primaryColor = Color(0xFF6A1B9A), // Royal Deep Purple
        secondaryColor = Color(0xFFBA68C8),
        softColor = Color(0xFFF3E5F5),
        soundDescription = "Klaxon peringatan aparat, riuh kegaduhan massa & seruan perdamaian darurat!",
        emergencySubtitle = "Ketegangan kelompok memuncak! Potensi bentrokan massa di ruang publik!",
        quickActionTitle = "KESELAMATAN & RESOLUSI DAMAI",
        responseScenario = EmergencyScenario(
            question = "Terjadi bentrokan massa di jalan raya depan lingkunganmu. Sikap yang paling bijak dan aman adalah?",
            options = listOf(
                ScenarioOption(
                    text = "Tetap berada di dalam rumah yang terkunci rapat dan jangan ikut terprovokasi",
                    isCorrect = true,
                    explanation = "Benar! Menghindari kerumunan massa bentrok menyelamatkanmu dari lemparan batu dan tuduhan salah sasaran aparat."
                ),
                ScenarioOption(
                    text = "Keluar ikut melempar batu untuk membela kelompok tertentu",
                    isCorrect = false,
                    explanation = "Ikut terlibat dalam kerusuhan memperparah konflik, melanggar hukum, dan membahayakan keselamatan jiwa sendiri."
                ),
                ScenarioOption(
                    text = "Menyebarkan video kekerasan tanpa sensor dan narasi panas ke grup WhatsApp",
                    isCorrect = false,
                    explanation = "Menyebarkan konten provokatif menyulut kepanikan publik dan memperluas eskalasi dendam antarkelompok."
                )
            )
        ),
        mitigationTips = listOf(
            "Tetap berada di tempat aman, kunci pintu dan jendela, serta hindari titik konsentrasi massa.",
            "Saring sebelum sharing: verifikasi kebenaran informasi dan tolak segala bentuk ujaran kebencian atau hoax.",
            "Kedepankan musyawarah, dialog damai, dan mediasi tokoh masyarakat jika ada perselisihan warga.",
            "Hubungi aparat kepolisian terdekat jika melihat tanda-tanda ancaman kekerasan fisik."
        )
    );

    companion object {
        val ALL: List<DisasterType> = entries

        private val random = Random()

        /**
         * Generates a random realistic earthquake magnitude between 5.1 and 8.8 SR.
         */
        fun getRandomMagnitude(): Double {
            return (51 + random.nextInt(38)) / 10.0 // e.g. 5.1 to 8.8
        }

        /**
         * Returns a dynamic alert title for Gempa Bumi with randomized magnitude and depth.
         */
        fun getDynamicAlertTitle(disaster: DisasterType, magnitude: Double = getRandomMagnitude()): String {
            return if (disaster == GEMPA_BUMI) {
                "GEMPA BUMI %.1f SR!".format(magnitude)
            } else {
                disaster.defaultAlertTitle
            }
        }
    }
}
