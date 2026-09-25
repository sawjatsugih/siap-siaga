package com.example.data

import com.example.model.DisasterType

data class QuizOption(
    val label: String,
    val text: String,
    val isCorrect: Boolean
)

data class QuizQuestion(
    val id: String,
    val disaster: DisasterType,
    val situation: String,
    val options: List<QuizOption>,
    val explanation: String,
    val timeLimitSeconds: Int = 15
)

object DisasterQuizBank {

    val ALL_QUESTIONS = listOf(
        // Gempa Bumi
        QuizQuestion(
            id = "q_gempa_1",
            disaster = DisasterType.GEMPA_BUMI,
            situation = "Saat Anda berada di dalam gedung bertingkat dan terjadi gempa bumi kuat, apa tindakan pertama yang paling tepat?",
            options = listOf(
                QuizOption("A", "Segera berlari menuju lift terdekat agar cepat keluar", false),
                QuizOption("B", "Lakukan DROP, COVER, HOLD ON di bawah meja kokoh dan lindungi kepala", true),
                QuizOption("C", "Berdiri di dekat jendela kaca untuk memantau situasi luar", false),
                QuizOption("D", "Berteriak histeris dan memecahkan kaca ventilasi", false)
            ),
            explanation = "Rumus 3B (Berlutut, Berlindung, Bertahan) adalah standar resmi BNPB untuk mencegah cedera fatal akibat runtuhan plafon atau material bangunan."
        ),
        QuizQuestion(
            id = "q_gempa_2",
            disaster = DisasterType.GEMPA_BUMI,
            situation = "Setelah guncangan gempa bumi utama berhenti, apa hal krusial yang wajib segera dilakukan di dalam rumah?",
            options = listOf(
                QuizOption("A", "Nyalakan korek api untuk memeriksa kebocoran tabung gas", false),
                QuizOption("B", "Langsung tidur kembali jika tidak ada dinding yang retak", false),
                QuizOption("C", "Matikan saklar MCB listrik utama dan regulator gas guna mencegah kebakaran", true),
                QuizOption("D", "Gunakan telepon rumah terus-menerus untuk mengobrol", false)
            ),
            explanation = "Kebakaran pasca-gempa akibat korsleting listrik dan kebocoran pipa gas sering kali menimbulkan korban jiwa lebih banyak daripada reruntuhan awal."
        ),

        // Tsunami
        QuizQuestion(
            id = "q_tsunami_1",
            disaster = DisasterType.TSUNAMI,
            situation = "Anda merasakan gempa kuat di pesisir pantai dan melihat air laut surut sangat jauh secara mendadak. Tindakan apa yang wajib diambil?",
            options = listOf(
                QuizOption("A", "Turun ke pantai untuk memunguti ikan yang terdampar", false),
                QuizOption("B", "Segera lari menuju bukit atau tempat tinggi minimal 20 meter tanpa menunggu sirine", true),
                QuizOption("C", "Berlindung di dalam mobil di pinggir pantai", false),
                QuizOption("D", "Mengambil kamera dan merekam video laut surut dari bibir pantai", false)
            ),
            explanation = "Surutnya air laut secara ekstrem adalah tanda alamiah utama tsunami besar. Evakuasi mandiri ke tempat tinggi harus dilakukan seketika!"
        ),
        QuizQuestion(
            id = "q_tsunami_2",
            disaster = DisasterType.TSUNAMI,
            situation = "Gelombang tsunami pertama telah menghantam dan mulai surut kembali ke laut. Apakah aman untuk langsung kembali ke pemukiman pantai?",
            options = listOf(
                QuizOption("A", "Aman, karena tsunami hanya datang satu kali saja", false),
                QuizOption("B", "Aman, asalkan menggunakan perahu karet penyelamat", false),
                QuizOption("C", "Tidak aman! Gelombang tsunami biasanya datang beruntun dan gelombang berikutnya bisa lebih tinggi", true),
                QuizOption("D", "Aman jika sudah lewat waktu 5 menit", false)
            ),
            explanation = "Tsunami merupakan rangkaian gelombang (*wave train*). Gelombang kedua atau ketiga sering kali jauh lebih dahsyat daripada gelombang awal."
        ),

        // Banjir
        QuizQuestion(
            id = "q_banjir_1",
            disaster = DisasterType.BANJIR,
            situation = "Air banjir luapan sungai mulai menggenangi jalanan setinggi lutut. Apa bahaya terbesar jika Anda nekat berjalan menerobos arus air?",
            options = listOf(
                QuizOption("A", "Hanya celana yang basah dan kotor", false),
                QuizOption("B", "Arus air setinggi 15-20 cm mampu menjatuhkan orang dewasa serta menyembunyikan lubang gorong-gorong terbuka", true),
                QuizOption("C", "Air banjir selalu jernih dan steril dari kuman penyakit", false),
                QuizOption("D", "Tidak ada bahaya sama sekali jika berjalan berdua", false)
            ),
            explanation = "Air keruh menyembunyikan bahaya jebolnya got terbuka, pecahan kaca tajam, dan kabel listrik putus yang masih beraliran listrik."
        ),

        // Kebakaran
        QuizQuestion(
            id = "q_kebakaran_1",
            disaster = DisasterType.KEBAKARAN,
            situation = "Ruangan dipenuhi asap hitam tebal akibat kebakaran gedung. Bagaimana cara terbaik untuk keluar menuju pintu darurat?",
            options = listOf(
                QuizOption("A", "Berjalan tegak sambil menghirup napas dalam-dalam", false),
                QuizOption("B", "Merangkak rendah dekat lantai dan tutup hidung/mulut dengan kain basah", true),
                QuizOption("C", "Bersembunyi di dalam lemari pakaian kayu", false),
                QuizOption("D", "Membuka seluruh jendela agar api semakin membesar", false)
            ),
            explanation = "Asap beracun dan gas karbon monoksida naik ke atas karena panas. Lapisan udara bersih tersisa berada 30-60 cm di atas permukaan lantai."
        ),

        // Gunung Berapi
        QuizQuestion(
            id = "q_gunung_1",
            disaster = DisasterType.GUNUNG_BERAPI,
            situation = "Gunung berapi meletus dan menyemburkan hujan abu vulkanik pekat. Alat pelindung diri apa yang paling esensial untuk digunakan?",
            options = listOf(
                QuizOption("A", "Topi koboi dan kacamata hitam biasa", false),
                QuizOption("B", "Masker N95 / kain basah untuk pernapasan dan kacamata goggle rapat", true),
                QuizOption("C", "Hanya memakai payung kertas", false),
                QuizOption("D", "Tidak perlu pelindung karena abu vulkanik itu organik", false)
            ),
            explanation = "Abu vulkanik mengandung partikel silika tajam menyerupai pecahan kaca kecil yang dapat merusak alveolus paru-paru dan melukai kornea mata."
        ),

        // Angin Topan
        QuizQuestion(
            id = "q_topan_1",
            disaster = DisasterType.ANGIN_TOPAN,
            situation = "Angin puting beliung dahsyat terlihat mendekati pemukiman. Di manakah posisi teraman jika berada di dalam rumah?",
            options = listOf(
                QuizOption("A", "Di dekat jendela kaca besar lantai dua", false),
                QuizOption("B", "Ruangan paling dalam di lantai dasar tanpa jendela, seperti kamar mandi atau lorong bawah tangga", true),
                QuizOption("C", "Di atap genteng rumah untuk mengamati arah angin", false),
                QuizOption("D", "Di dalam garasi mobil dengan pintu terbuka lebar", false)
            ),
            explanation = "Dinding tengah lantai dasar memberikan perlindungan maksimal dari bahaya proyektil puing-puing seng dan kayu yang diterbangkan angin kencang."
        ),

        // Tanah Longsor
        QuizQuestion(
            id = "q_longsor_1",
            disaster = DisasterType.TANAH_LONGSOR,
            situation = "Setelah hujan deras berhari-hari di lereng bukit, Anda mendengar suara gemuruh tanah bergetar dan pohon mulai miring. Apa langkah evakuasi Anda?",
            options = listOf(
                QuizOption("A", "Lari menyusuri lembah mengikuti arah aliran material longsor", false),
                QuizOption("B", "Lari menjauh ke arah samping tegak lurus dari jalur runtuhan menuju tanah stabil", true),
                QuizOption("C", "Mendekati tebing untuk mengecek volume tanah yang retak", false),
                QuizOption("D", "Masuk ke dalam gorong-gorong air bawah lereng", false)
            ),
            explanation = "Material longsor meluncur deras ke arah bawah lembah. Menyelamatkan diri harus dilakukan ke arah samping (*lateral*) dari arah luncuran."
        ),

        // Kekeringan
        QuizQuestion(
            id = "q_kekeringan_1",
            disaster = DisasterType.KEKERINGAN,
            situation = "Dalam menghadapi ancaman kekeringan panjang dan krisis air bersih, strategi mitigasi hemat air apa yang paling dianjurkan?",
            options = listOf(
                QuizOption("A", "Menggunakan air mengalir deras untuk mencuci kendaraan setiap hari", false),
                QuizOption("B", "Pemanenan air hujan (rainwater harvesting) dan daur ulang greywater untuk tanaman", true),
                QuizOption("C", "Menguras sumur warga hingga kering tanpa batas", false),
                QuizOption("D", "Membakar lahan kering agar tanah cepat gembur", false)
            ),
            explanation = "Pemanenan air hujan dan penghematan air tanah merupakan mitigasi struktural berbasis konservasi sumber daya air yang berkelanjutan."
        ),

        // Abrasi Pantai
        QuizQuestion(
            id = "q_abrasi_1",
            disaster = DisasterType.ABRASI,
            situation = "Apa solusi mitigasi jangka panjang berbasis alam (*nature-based solution*) terbaik untuk menahan erosi abrasi pantai?",
            options = listOf(
                QuizOption("A", "Menebang seluruh hutan bakau di muara sungai", false),
                QuizOption("B", "Menambang pasir pantai secara besar-besaran", false),
                QuizOption("C", "Penanaman kembali sabuk hijau hutan mangrove (*greenbelt*) dan terumbu karang", true),
                QuizOption("D", "Membuang sampah plastik ke bibir pantai", false)
            ),
            explanation = "Akar mangrove yang rapat meredam energi hempasan ombak laut hingga 60% dan mengikat sedimen pasir dari kikisan abrasi."
        ),

        // Konflik Sosial
        QuizQuestion(
            id = "q_konflik_1",
            disaster = DisasterType.KONFLIK_SOSIAL,
            situation = "Jika terjadi kerusuhan atau bentrok massa di ruang publik, tindakan mitigasi keselamatan diri yang tepat adalah:",
            options = listOf(
                QuizOption("A", "Mendekat ke barisan depan untuk memprovokasi massa", false),
                QuizOption("B", "Ikut menyebarkan foto dan kabar hoaks yang belum terverifikasi ke grup medsos", false),
                QuizOption("C", "Segera cari tempat aman di gedung tertutup, hindari kerumunan, dan patuhi arahan aparat", true),
                QuizOption("D", "Menolak dievakuasi oleh petugas penyelamat", false)
            ),
            explanation = "Prinsip utama mitigasi konflik adalah menjauh dari episentrum massa, tidak mudah terhasut provokasi disinformasi, dan mengamankan keluarga."
        ),

        // Perubahan Iklim
        QuizQuestion(
            id = "q_iklim_1",
            disaster = DisasterType.PERUBAHAN_IKLIM,
            situation = "Sebagai bagian dari mitigasi perubahan iklim global, kebiasaan harian apa yang paling berdampak positif mengurangi jejak karbon?",
            options = listOf(
                QuizOption("A", "Membiarkan AC dan peralatan listrik menyala 24 jam di ruangan kosong", false),
                QuizOption("B", "Pengurangan sampah plastik sekali pakai, hemat energi listrik, dan penghijauan pohon", true),
                QuizOption("C", "Membakar tumpukan sampah plastik di pekarangan rumah", false),
                QuizOption("D", "Menebang pohon peneduh di tepi jalan", false)
            ),
            explanation = "Efisiensi konsumsi energi, pengelolaan sampah sirkular, dan reboisasi mengurangi emisi gas rumah kaca yang memicu pemanasan global."
        )
    )

    fun getQuestions(count: Int = 5): List<QuizQuestion> {
        val selected = ALL_QUESTIONS.shuffled().take(count.coerceAtMost(ALL_QUESTIONS.size))
        val optionLabels = listOf("A", "B", "C", "D")
        return selected.map { question ->
            val shuffledOptions = question.options.shuffled().mapIndexed { index, option ->
                option.copy(label = optionLabels.getOrElse(index) { ('A' + index).toString() })
            }
            question.copy(options = shuffledOptions)
        }
    }
}
