package com.example.data

import com.example.model.DisasterType

data class DrillQuestion(
    val id: String,
    val disaster: DisasterType,
    val situation: String,
    val options: List<DrillOption>,
    val explanation: String
)

data class DrillOption(
    val label: String,
    val text: String,
    val isCorrect: Boolean
)

object DisasterDrillBank {

    // 15 comprehensive, BNPB-aligned questions for each of the 11 disasters (Total: 165 questions)
    val ALL_QUESTIONS: Map<DisasterType, List<DrillQuestion>> = mapOf(
        // ================= 1. GEMPA BUMI (15 SOAL) =================
        DisasterType.GEMPA_BUMI to listOf(
            DrillQuestion(
                id = "gempa_1", disaster = DisasterType.GEMPA_BUMI,
                situation = "Guncangan gempa bumi dahsyat mendadak mengguncang ruangan tempat Anda berada. Tindakan penyelamatan awal apa yang paling tepat?",
                options = listOf(
                    DrillOption("A", "Segera berlari menuju balkon atau jendela luar", false),
                    DrillOption("B", "Lakukan DROP, COVER, HOLD ON: Merunduk, berlindung di bawah meja kokoh, dan berpegangan erat", true),
                    DrillOption("C", "Gunakan lift darurat agar cepat sampai ke lobi gedung", false),
                    DrillOption("D", "Berdiri tepat di bawah lampu gantung besar di tengah ruangan", false)
                ),
                explanation = "Rumus 3B (Berlutut, Berlindung, Bertahan) adalah protokol keselamatan utama untuk melindungi organ vital kepala dan leher dari reruntuhan plafon."
            ),
            DrillQuestion(
                id = "gempa_2", disaster = DisasterType.GEMPA_BUMI,
                situation = "Bila tidak terdapat meja kokoh di sekitar Anda saat gempa berlangsung, di manakah posisi perlindungan terbaik?",
                options = listOf(
                    DrillOption("A", "Berdiri merapat di samping lemari kaca besar", false),
                    DrillOption("B", "Merapat ke sudut dinding bagian dalam, berlutut, dan lindungi kepala memakai bantal atau kedua tangan", true),
                    DrillOption("C", "Tidur telentang di lantai tanpa penutup kepala", false),
                    DrillOption("D", "Berdiri di depan pintu kaca geser", false)
                ),
                explanation = "Sudut dinding bagian dalam memiliki integritas struktural terkuat. Lindungi kepala dari pecahan plester langit-langit."
            ),
            DrillQuestion(
                id = "gempa_3", disaster = DisasterType.GEMPA_BUMI,
                situation = "Anda sedang mengemudikan kendaraan roda empat saat gempa bumi berkekuatan tinggi terjadi. Tindakan apa yang benar?",
                options = listOf(
                    DrillOption("A", "Tancap gas sekencang-kencangnya melintasi jembatan penyeberangan", false),
                    DrillOption("B", "Kurangi kecepatan secara bertahap, menepi di area terbuka bebas tiang listrik, tarik rem tangan, dan tetap di dalam mobil", true),
                    DrillOption("C", "Berhenti tepat di bawah papan reklame bando jalan", false),
                    DrillOption("D", "Melompat keluar dari kendaraan yang masih melaju", false)
                ),
                explanation = "Menepi di area lapang mencegah benturan tabrakan beruntun dan melindungi pengemudi dari tiang atau kabel listrik yang roboh."
            ),
            DrillQuestion(
                id = "gempa_4", disaster = DisasterType.GEMPA_BUMI,
                situation = "Seketika setelah guncangan gempa utama reda, tindakan darurat pertama apa yang wajib dilakukan di dalam rumah?",
                options = listOf(
                    DrillOption("A", "Menyalakan lilin atau korek gas untuk menerangi ruangan", false),
                    DrillOption("B", "Mematikan saklar utama MCB listrik dan mencabut regulator tabung gas", true),
                    DrillOption("C", "Mengunci semua pintu dan jendela rapat-rapat", false),
                    DrillOption("D", "Mengemas seluruh perabot berat ke dalam kardus", false)
                ),
                explanation = "Korsleting listrik dan kebocoran gas adalah pemicu utama kebakaran hebat pasca gempa bumi."
            ),
            DrillQuestion(
                id = "gempa_5", disaster = DisasterType.GEMPA_BUMI,
                situation = "Saat melakukan evakuasi menuruni gedung bertingkat pasca gempa, jalur manakah yang wajib dilalui?",
                options = listOf(
                    DrillOption("A", "Tangga darurat gedung secara berurutan tanpa saling dorong", true),
                    DrillOption("B", "Lift ekspres gedung agar lekas keluar", false),
                    DrillOption("C", "Menuruni tali kabel listrik di dinding luar", false),
                    DrillOption("D", "Melompat dari balkon lantai atas ke kanopi teras", false)
                ),
                explanation = "Lift berisiko macet total akibat pemadaman listrik darurat atau rel pemandu lift yang melengkung terpelintir gempa."
            ),
            DrillQuestion(
                id = "gempa_6", disaster = DisasterType.GEMPA_BUMI,
                situation = "Jika Anda berada di dalam pusat perbelanjaan atau supermarket saat gempa, area mana yang paling berbahaya?",
                options = listOf(
                    DrillOption("A", "Lorong rak barang-barang tinggi dan etalase botol kaca", true),
                    DrillOption("B", "Dekat tiang pilar utama beton gedung", false),
                    DrillOption("C", "Pintu keluar tangga darurat", false),
                    DrillOption("D", "Area konter informasi terbuka", false)
                ),
                explanation = "Rak belanjaan bertingkat rawan roboh seketika dan botol kaca pecah dapat mengakibatkan luka sayat yang sangat berbahaya."
            ),
            DrillQuestion(
                id = "gempa_7", disaster = DisasterType.GEMPA_BUMI,
                situation = "Berapa lama perbekalan dalam Tas Siaga Bencana (TSB) keluarga dipersiapkan untuk bertahan mandiri?",
                options = listOf(
                    DrillOption("A", "Hanya untuk 2 jam pertama", false),
                    DrillOption("B", "Minimal 3 x 24 jam (72 jam pertama darurat)", true),
                    DrillOption("C", "Selama 6 bulan penuh", false),
                    DrillOption("D", "Hanya untuk 15 menit saja", false)
                ),
                explanation = "Golden period penanganan bencana adalah 72 jam pertama, di mana akses bantuan logistik luar kemungkinan masih terputus."
            ),
            DrillQuestion(
                id = "gempa_8", disaster = DisasterType.GEMPA_BUMI,
                situation = "Bila nahas terjebak di bawah puing reruntuhan bangunan, bagaimana cara efektif memberi sinyal kepada tim SAR?",
                options = listOf(
                    DrillOption("A", "Berteriak sekencang-kencangnya terus-menerus hingga suara habis", false),
                    DrillOption("B", "Ketukkan benda keras/batu ke pipa logam atau dinding beton secara ritmis, dan gunakan peluit", true),
                    DrillOption("C", "Nyalakan korek api di dekat puing kayu kering", false),
                    DrillOption("D", "Mendorong balok penahan utama di atas kepala", false)
                ),
                explanation = "Mengetuk logam dan meniup peluit menghemat energi dan oksigen, serta frekuensi suaranya mudah dideteksi alat pendengar tim SAR."
            ),
            DrillQuestion(
                id = "gempa_9", disaster = DisasterType.GEMPA_BUMI,
                situation = "Mengapa warga dilarang langsung masuk kembali ke dalam rumah yang mengalami retak struktur pasca gempa?",
                options = listOf(
                    DrillOption("A", "Rumah tersebut akan disegel oleh perbankan", false),
                    DrillOption("B", "Gempa susulan (aftershocks) dapat merobohkan bangunan yang strukturnya sudah melemah", true),
                    DrillOption("C", "Karena lantai rumah berdebu", false),
                    DrillOption("D", "Hanya mitos tanpa dasar teknis", false)
                ),
                explanation = "Bangunan yang retak struktural sangat rentan runtuh total meskipun guncangan gempa susulan berkekuatan lebih kecil."
            ),
            DrillQuestion(
                id = "gempa_10", disaster = DisasterType.GEMPA_BUMI,
                situation = "Di dalam bioskop yang gelap saat gempa terjadi, respon terbaik penonton adalah:",
                options = listOf(
                    DrillOption("A", "Berdesakan dan berebut ke satu pintu keluar sempit", false),
                    DrillOption("B", "Merunduk di antara deretan kursi kokoh, tutupi kepala dengan jaket/tas, dan ikuti panduan petugas", true),
                    DrillOption("C", "Memanjat panggung layar proyektor", false),
                    DrillOption("D", "Berteriak memprovokasi kepanikan massal", false)
                ),
                explanation = "Kepanikan massal di pintu keluar sering menewaskan korban karena terinjak-injak (*crowd crush*). Kursi bioskop memberikan perlindungan kepala."
            ),
            DrillQuestion(
                id = "gempa_11", disaster = DisasterType.GEMPA_BUMI,
                situation = "Alas kaki terbaik saat mengevakuasi diri melewati puing-puing gempa bumi adalah:",
                options = listOf(
                    DrillOption("A", "Sandal jepit tipis", false),
                    DrillOption("B", "Sepatu beralas tebal dan kokoh", true),
                    DrillOption("C", "Sepatu pesta hak tinggi (high heels)", false),
                    DrillOption("D", "Telanjang kaki tanpa alas apa pun", false)
                ),
                explanation = "Puing pecahan kaca dan paku berkarat berserakan di jalan pasca gempa; sepatu beralas tebal melindungi kaki dari cedera fatal."
            ),
            DrillQuestion(
                id = "gempa_12", disaster = DisasterType.GEMPA_BUMI,
                situation = "Bila Anda sedang memasak di depan kompor saat gempa mengguncang, apa langkah pertama yang realistis?",
                options = listOf(
                    DrillOption("A", "Selesaikan memasak terlebih dahulu sampai lauk matang", false),
                    DrillOption("B", "Jika memungkinkan tanpa membahayakan diri, matikan api kompor seketika lalu segera DROP & COVER", true),
                    DrillOption("C", "Siram minyak goreng panas dengan air dingin", false),
                    DrillOption("D", "Tinggalkan kompor menyala dan kunci pintu dapur rapat-rapat", false)
                ),
                explanation = "Mematikan api kompor mencegah percikan minyak memicu kobaran api dapur yang tidak terkendali."
            ),
            DrillQuestion(
                id = "gempa_13", disaster = DisasterType.GEMPA_BUMI,
                situation = "Jika berada di pantai dengan tebing karang saat gempa kuat terjadi, ancaman ganda apa yang mengintai?",
                options = listOf(
                    DrillOption("A", "Bahaya runtuhan tebing batu (longsoran) dan ancaman gelombang tsunami", true),
                    DrillOption("B", "Suhu air laut menjadi dingin", false),
                    DrillOption("C", "Ikan laut bermutasi", false),
                    DrillOption("D", "Pasir pantai berubah menjadi keras seperti baja", false)
                ),
                explanation = "Guncangan gempa melemahkan tebing batuan pantai dan gempa tektonik dasar laut berisiko tinggi membangkitkan tsunami."
            ),
            DrillQuestion(
                id = "gempa_14", disaster = DisasterType.GEMPA_BUMI,
                situation = "Bagaimana metode komunikasi terbaik dengan keluarga pasca gempa bumi besar?",
                options = listOf(
                    DrillOption("A", "Panggilan telepon suara terus-menerus tanpa jeda", false),
                    DrillOption("B", "Gunakan pesan teks singkat (SMS / Chat) untuk menghemat baterai dan mengurangi beban trafik jaringan seluler", true),
                    DrillOption("C", "Melakukan siaran langsung video beresolusi tinggi", false),
                    DrillOption("D", "Menyalakan radio pemancar tanpa izin", false)
                ),
                explanation = "Jaringan telepon suara langsung tersumbat (*network congestion*) saat bencana; pesan teks membutuhkan kuota data minimal."
            ),
            DrillQuestion(
                id = "gempa_15", disaster = DisasterType.GEMPA_BUMI,
                situation = "Kriteria titik kumpul aman (*assembly point*) yang memenuhi standar BNPB adalah:",
                options = listOf(
                    DrillOption("A", "Di lantai basement parkir bawah tanah", false),
                    DrillOption("B", "Di tanah lapang terbuka bebas dari jangkauan robohnya pohon, tiang listrik, dan dinding gedung tinggi", true),
                    DrillOption("C", "Di gang sempit di antara dua gedung bertingkat", false),
                    DrillOption("D", "Tepat di bawah gardu trafo listrik tegangan tinggi", false)
                ),
                explanation = "Titik kumpul harus lapang agar pengungsi terhindar dari material bangunan yang runtuh saat terjadi gempa susulan."
            )
        ),

        // ================= 2. TSUNAMI (15 SOAL) =================
        DisasterType.TSUNAMI to listOf(
            DrillQuestion(
                id = "tsunami_1", disaster = DisasterType.TSUNAMI,
                situation = "Tanda alamiah paling jelas dari ancaman tsunami di pesisir pantai sesaat setelah gempa bumi adalah:",
                options = listOf(
                    DrillOption("A", "Air laut surut drastis secara cepat hingga terumbu karang dan dasar laut terlihat luas", true),
                    DrillOption("B", "Air laut berubah warna menjadi merah muda cerah", false),
                    DrillOption("C", "Ombak di laut berhenti selamanya tanpa suara", false),
                    DrillOption("D", "Turunnya hujan salju di bibir pantai", false)
                ),
                explanation = "Amblesnya lempeng tektonik laut menarik air dalam jumlah masif ke palung sebelum dihempaskan kembali sebagai gelombang tsunami dahsyat."
            ),
            DrillQuestion(
                id = "tsunami_2", disaster = DisasterType.TSUNAMI,
                situation = "Melihat fenomena air laut surut drastis di pesisir pantai, respon penyelamatan diri yang wajib dilakukan adalah:",
                options = listOf(
                    DrillOption("A", "Berlari ke dasar laut untuk memunguti ikan yang menggelepar", false),
                    DrillOption("B", "Segera evakuasi ke bukit atau tempat tinggi minimal 20 meter / menjauh 2 km dari pantai tanpa menunggu sirine", true),
                    DrillOption("C", "Merekam video di bibir ombak untuk media sosial", false),
                    DrillOption("D", "Duduk santai di warung pantai menikmati pemandangan", false)
                ),
                explanation = "Waktu kedatangan tsunami hanya beberapa menit setelah air surut. Menunda evakuasi demi memungut ikan berakibat fatal."
            ),
            DrillQuestion(
                id = "tsunami_3", disaster = DisasterType.TSUNAMI,
                situation = "Apakah aman untuk turun kembali ke pantai setelah gelombang tsunami pertama surut?",
                options = listOf(
                    DrillOption("A", "Sangat aman karena tsunami selalu datang hanya satu kali", false),
                    DrillOption("B", "Tidak aman! Tsunami adalah rentetan gelombang, dan gelombang berikutnya sering kali jauh lebih besar dan menghancurkan", true),
                    DrillOption("C", "Aman jika sudah lewat waktu 5 menit", false),
                    DrillOption("D", "Aman jika permukaan air laut terlihat tenang sejenak", false)
                ),
                explanation = "Tsunami merupakan gelombang beruntun (*wave train*). Gelombang kedua atau ketiga kerap memiliki massa air dan daya rusak terbesar."
            ),
            DrillQuestion(
                id = "tsunami_4", disaster = DisasterType.TSUNAMI,
                situation = "Bila waktu evakuasi sangat sempit dan tidak memungkinkan mencapai bukit, alternatif terbaik adalah:",
                options = listOf(
                    DrillOption("A", "Berlindung di dalam saluran got atau gorong-gorong drainase", false),
                    DrillOption("B", "Evakuasi vertikal: Naik ke lantai 3 atau atap gedung bertulang beton yang kokoh (TES - Tempat Evakuasi Sementara)", true),
                    DrillOption("C", "Bersembunyi di balik dinding gubuk kayu tepi pantai", false),
                    DrillOption("D", "Memanjat tiang lampu penerangan jalan", false)
                ),
                explanation = "Gedung bertulang beton tahan gempa mampu menahan terjangan hidrolik tsunami dan menyelamatkan warga saat waktu habis."
            ),
            DrillQuestion(
                id = "tsunami_5", disaster = DisasterType.TSUNAMI,
                situation = "Anda sedang berlayar di kapal laut di perairan dalam ketika BMKG mengeluarkan peringatan tsunami. Tindakan nahkoda adalah:",
                options = listOf(
                    DrillOption("A", "Memacu kapal mendekati bibir dermaga pantai", false),
                    DrillOption("B", "Tetap pertahankan kapal di laut dalam, karena di perairan dalam tinggi tsunami hanya puluhan sentimeter", true),
                    DrillOption("C", "Melompat ke laut dan berenang ke darat", false),
                    DrillOption("D", "Menambatkan kapal di tiang kayu dermaga", false)
                ),
                explanation = "Di laut dalam (>200 m), gelombang tsunami tidak terasa dan tidak membahayakan kapal. Bahaya tsunami hanya memuncak di perairan dangkal."
            ),
            DrillQuestion(
                id = "tsunami_6", disaster = DisasterType.TSUNAMI,
                situation = "Apa maksud dari rumus penyelamatan '20-20-20' yang disosialisasikan BNPB untuk tsunami lokal?",
                options = listOf(
                    DrillOption("A", "Jika gempa terasa >20 detik, Anda punya waktu sekitar 20 menit untuk lari ke ketinggian minimal 20 meter", true),
                    DrillOption("B", "Membeli 20 botol air, lari 20 meter, tunggu 20 hari", false),
                    DrillOption("C", "Berenang 20 meter, menyelam 20 detik, naik 20 tangga", false),
                    DrillOption("D", "Tunggu 20 menit di rumah sebelum memutuskan evakuasi", false)
                ),
                explanation = "Pedoman praktis 20-20-20 memudahkan masyarakat pesisir mengingat durasi gempa pemicu dan target ketinggian evakuasi."
            ),
            DrillQuestion(
                id = "tsunami_7", disaster = DisasterType.TSUNAMI,
                situation = "Selain massa air laut yang besar, faktor bahaya mematikan apa yang terbawa oleh arus tsunami?",
                options = listOf(
                    DrillOption("A", "Suhu air yang menyegarkan", false),
                    DrillOption("B", "Puing-puing hanyut yang sangat padat: batang pohon tumbang, mobil terseret, serpihan atap seng tajam, dan kabel", true),
                    DrillOption("C", "Ikan lumba-lumba kecil", false),
                    DrillOption("D", "Buih ombak yang harum", false)
                ),
                explanation = "Debris yang meluncur bersama arus tsunami berkecepatan tinggi bertindak layaknya proyektil penghancur bagi tubuh manusia."
            ),
            DrillQuestion(
                id = "tsunami_8", disaster = DisasterType.TSUNAMI,
                situation = "Jika terbawa arus gelombang tsunami, tindakan terbaik untuk bertahan hidup adalah:",
                options = listOf(
                    DrillOption("A", "Berenang melawan arus deras sekuat tenaga", false),
                    DrillOption("B", "Meraih dan berpegangan erat pada benda terapung besar seperti batang pohon, pintu kayu tebal, atau jerigen", true),
                    DrillOption("C", "Menyelam ke dasar laut dan menahan napas", false),
                    DrillOption("D", "Melepaskan pelampung badan", false)
                ),
                explanation = "Melawan arus deras akan menghabiskan stamina dalam hitungan detik. Mengapung di atas objek menjaga jalur napas tetap terbuka."
            ),
            DrillQuestion(
                id = "tsunami_9", disaster = DisasterType.TSUNAMI,
                situation = "Mengapa BNPB menyarankan evakuasi tsunami dilakukan dengan berjalan cepat atau berlari daripada mengendarai mobil?",
                options = listOf(
                    DrillOption("A", "Kendaraan bermotor rentan menimbulkan kemacetan total di jalan sempit pesisir, menjebak warga di dalam mobil", true),
                    DrillOption("B", "Karena mobil dilarang berjalan di dekat laut", false),
                    DrillOption("C", "Bensin mobil akan habis dalam 1 menit", false),
                    DrillOption("D", "Mobil akan langsung tenggelam oleh angin", false)
                ),
                explanation = "Kemacetan lalu lintas akibat kepanikan pengemudi mobil sering kali menjadi perangkap maut saat dinding air menerjang."
            ),
            DrillQuestion(
                id = "tsunami_10", disaster = DisasterType.TSUNAMI,
                situation = "Suara alamiah apa yang sering terdengar beberapa saat sebelum gelombang tsunami menghempas daratan?",
                options = listOf(
                    DrillOption("A", "Suara gemuruh dahsyat seperti deru pesawat jet terbang rendah atau kereta api berkecepatan tinggi", true),
                    DrillOption("B", "Suara kicauan burung camar di pantai", false),
                    DrillOption("C", "Suara alunan seruling bambu lembut", false),
                    DrillOption("D", "Keheningan total tanpa suara apa pun", false)
                ),
                explanation = "Turbulensi masif miliaran liter air yang bergesekan dengan dasar laut dangkal menciptakan dentuman gemuruh berfrekuensi rendah."
            ),
            DrillQuestion(
                id = "tsunami_11", disaster = DisasterType.TSUNAMI,
                situation = "Manfaat utama penanaman sabuk hijau mangrove (*greenbelt*) di garis pantai dalam mitigasi tsunami adalah:",
                options = listOf(
                    DrillOption("A", "Akar tunjang mangrove meredam energi hempasan gelombang tsunami dan menahan laju serpihan puing hanyut", true),
                    DrillOption("B", "Sebagai tempat memancing kepiting", false),
                    DrillOption("C", "Menjadikan air laut terasa tawar", false),
                    DrillOption("D", "Membuat tsunami berhenti di tengah samudera", false)
                ),
                explanation = "Hutan mangrove yang lebat mampu mereduksi daya rusak dan ketinggian gelombang tsunami hingga lebih dari 50 persen."
            ),
            DrillQuestion(
                id = "tsunami_12", disaster = DisasterType.TSUNAMI,
                situation = "Lembaga resmi di Indonesia yang bertugas mengoperasikan sistem peringatan dini tsunami (InaTEWS) adalah:",
                options = listOf(
                    DrillOption("A", "BMKG (Badan Meteorologi, Klimatologi, dan Geofisika)", true),
                    DrillOption("B", "Kementerian Kelautan dan Perikanan", false),
                    DrillOption("C", "Badan Pertanahan Nasional", false),
                    DrillOption("D", "Dinas Kebersihan Kota", false)
                ),
                explanation = "BMKG memantau sensor seismograf, buoy laut, dan stasiun pasang surut secara 24/7 untuk mengeluarkan peringatan dini tsunami."
            ),
            DrillQuestion(
                id = "tsunami_13", disaster = DisasterType.TSUNAMI,
                situation = "Tingkat peringatan tsunami status 'AWAS' dari BMKG memiliki arti:",
                options = listOf(
                    DrillOption("A", "Tinggi tsunami diperkirakan >3 meter, masyarakat wajib melakukan evakuasi menyeluruh ke tempat aman", true),
                    DrillOption("B", "Tinggi tsunami di bawah 1 sentimeter dan tidak berbahaya", false),
                    DrillOption("C", "Masyarakat dipersilakan bermain air di pantai", false),
                    DrillOption("D", "Hanya latihan sirine berkala", false)
                ),
                explanation = "Status AWAS mengindikasikan potensi gelombang tsunami dahsyat (>3 meter), sehingga evakuasi total wajib dilaksanakan."
            ),
            DrillQuestion(
                id = "tsunami_14", disaster = DisasterType.TSUNAMI,
                situation = "Masalah kesehatan darurat apa yang paling sering mengancam pengungsi pasca bencana tsunami?",
                options = listOf(
                    DrillOption("A", "Wabah diare, kolera, dan leptospirosis akibat sumur dan sumber air bersih tercemar lumpur laut serta bangkai", true),
                    DrillOption("B", "Kelebihan asupan vitamin C", false),
                    DrillOption("C", "Kulit menjadi terlalu bersih karena air garam", false),
                    DrillOption("D", "Sakit kepala karena terlalu banyak tidur", false)
                ),
                explanation = "Sanitasi yang hancur mencemari cadangan air minum. Air wajib dimasak hingga mendidih sebelum dikonsumsi."
            ),
            DrillQuestion(
                id = "tsunami_15", disaster = DisasterType.TSUNAMI,
                situation = "Kapan warga di bukit evakuasi diperbolehkan turun kembali ke rumah di pesisir?",
                options = listOf(
                    DrillOption("A", "Setelah ada pengumuman resmi 'Peringatan Dini Tsunami Dinyatakan Berakhir' dari BMKG/BPBD", true),
                    DrillOption("B", "Begitu melihat ombak pertama surut ke laut", false),
                    DrillOption("C", "Saat hari mulai gelap tanpa menunggu kabar petugas", false),
                    DrillOption("D", "Saat baterai ponsel habis", false)
                ),
                explanation = "Hanya verifikasi data sensor pasang-surut resmi yang dapat memastikan rangkaian gelombang tsunami telah tuntas sepenuhnya."
            )
        ),

        // ================= 3. BANJIR BANDANG (15 SOAL) =================
        DisasterType.BANJIR to listOf(
            DrillQuestion(
                id = "banjir_1", disaster = DisasterType.BANJIR,
                situation = "Ketika air banjir mulai menggenangi lantai rumah dengan cepat, apa prioritas nomor satu yang harus dimatikan?",
                options = listOf(
                    DrillOption("A", "Saklar utama MCB listrik di meteran rumah guna mencegah sengatan arus listrik fatal", true),
                    DrillOption("B", "Kran air kamar mandi", false),
                    DrillOption("C", "Kipas angin gantung", false),
                    DrillOption("D", "Lampu senter darurat", false)
                ),
                explanation = "Air merupakan penghantar listrik yang sangat baik. Menolak mematikan MCB dapat memicu kematian akibat sengatan listrik bawah air."
            ),
            DrillQuestion(
                id = "banjir_2", disaster = DisasterType.BANJIR,
                situation = "Apakah aman bagi orang dewasa untuk berjalan menerobos arus banjir setinggi mata kaki (15 cm)?",
                options = listOf(
                    DrillOption("A", "Sangat berbahaya! Arus air setinggi 15 cm yang bergerak deras mampu merobohkan dan menyeret orang dewasa", true),
                    DrillOption("B", "Pasti aman karena airnya masih dangkal", false),
                    DrillOption("C", "Aman jika memakai sepatu roda", false),
                    DrillOption("D", "Aman jika melompat dengan satu kaki", false)
                ),
                explanation = "Daya dorong kinetik air banjir yang mengalir sangat kuat, ditambah permukaan jalan licin dan lubang got yang tak terlihat."
            ),
            DrillQuestion(
                id = "banjir_3", disaster = DisasterType.BANJIR,
                situation = "Jika mobil yang Anda kendarai terjebak banjir dan mogok di tengah genangan air yang naik cepat, apa tindakannya?",
                options = listOf(
                    DrillOption("A", "Tetap duduk di dalam mobil sambil menginjak gas berulang-ulang", false),
                    DrillOption("B", "Segera tinggalkan mobil, buka pintu atau pecahkan kaca jendela, dan evakuasi ke tempat yang lebih tinggi", true),
                    DrillOption("C", "Kunci semua pintu dan tutup kaca mobil rapat-rapat", false),
                    DrillOption("D", "Menunggu di dalam mobil sampai air surut sendiri", false)
                ),
                explanation = "Mobil dapat terendam sepenuhnya atau terbawa hanyut arus dalam hitungan menit, mengunci penumpang di dalam jebakan maut."
            ),
            DrillQuestion(
                id = "banjir_4", disaster = DisasterType.BANJIR,
                situation = "Mengapa anak-anak dilarang keras bermain atau berenang di genangan air banjir di jalanan permukiman?",
                options = listOf(
                    DrillOption("A", "Bahaya terseret arus masuk ke gorong-gorong got terbuka, sengatan listrik tiang jalan, dan gigitan ular berbisa", true),
                    DrillOption("B", "Hanya karena baju akan menjadi basah", false),
                    DrillOption("C", "Karena air banjir terlalu dingin", false),
                    DrillOption("D", "Karena air banjir mengandung sabun mandi", false)
                ),
                explanation = "Tutup got sering terlepas saat banjir dan arus bawah sangat deras. Selain itu, hewan berbisa mencari tempat kering di dekat manusia."
            ),
            DrillQuestion(
                id = "banjir_5", disaster = DisasterType.BANJIR,
                situation = "Bagaimana cara menyiapkan dokumen-dokumen berharga keluarga sebelum banjir melanda rumah?",
                options = listOf(
                    DrillOption("A", "Masukkan ke dalam wadah plastik kedap air (waterproof) dan simpan di lantai atas atau rak paling tinggi", true),
                    DrillOption("B", "Letakkan di lantai ruang tamu agar mudah dilihat", false),
                    DrillOption("C", "Bungkus dengan kertas koran basah", false),
                    DrillOption("D", "Kubur di dalam tanah halaman rumah", false)
                ),
                explanation = "Dokumen seperti sertifikat tanah, ijazah, dan buku tabungan harus terlindung dari kelembapan ekstrem dan air lumpur banjir."
            ),
            DrillQuestion(
                id = "banjir_6", disaster = DisasterType.BANJIR,
                situation = "Penyakit menular mematikan apa yang ditularkan melalui air kencing tikus dalam genangan air banjir?",
                options = listOf(
                    DrillOption("A", "Leptospirosis", true),
                    DrillOption("B", "Radang amandel", false),
                    DrillOption("C", "Migrain", false),
                    DrillOption("D", "Rabies", false)
                ),
                explanation = "Bakteri Leptospira dari urine tikus masuk ke dalam tubuh manusia melalui luka terbuka di kulit saat mengarungi air banjir."
            ),
            DrillQuestion(
                id = "banjir_7", disaster = DisasterType.BANJIR,
                situation = "Bila air banjir setinggi dada mengurung Anda di lantai dua rumah, cara terbaik memberi tahu tim evakuasi adalah:",
                options = listOf(
                    DrillOption("A", "Kibarkan kain/baju berwarna terang cerah (seperti oranye/merah) di jendela atau tiup peluit darurat", true),
                    DrillOption("B", "Melompat ke air dan berenang sendirian tanpa pelampung", false),
                    DrillOption("C", "Membakar kasur busa di dalam kamar", false),
                    DrillOption("D", "Mengetuk panci di dalam lemari", false)
                ),
                explanation = "Sinyal visual warna kontras dan peluit memandu perahu karet tim SAR mendekat ke posisi Anda tanpa membahayakan keselamatan."
            ),
            DrillQuestion(
                id = "banjir_8", disaster = DisasterType.BANJIR,
                situation = "Mengapa makanan kemasan yang kalengnya sudah penyok atau terendam air banjir kotor harus dibuang?",
                options = listOf(
                    DrillOption("A", "Kuman bakteri dan racun limbah banjir dapat merembes melalui celah mikro sambungan kaleng", true),
                    DrillOption("B", "Karena rasa makanannya akan menjadi terlalu manis", false),
                    DrillOption("C", "Karena kalengnya tidak laku dijual", false),
                    DrillOption("D", "Hanya aturan pabrik makanan", false)
                ),
                explanation = "Air banjir sarat bakteri patogen, limbah industri, dan feses. Mengonsumsi kaleng yang bocor memicu keracunan makanan parah."
            ),
            DrillQuestion(
                id = "banjir_9", disaster = DisasterType.BANJIR,
                situation = "Apa alat bantu pelindung diri paling tepat saat membersihkan lumpur tebal pasca banjir surut?",
                options = listOf(
                    DrillOption("A", "Sepatu bot karet tebal, sarung tangan karet panjang, dan masker penutup hidung", true),
                    DrillOption("B", "Sandal jepit jepit plastik", false),
                    DrillOption("C", "Telanjang kaki agar lincah", false),
                    DrillOption("D", "Sepatu kanvas bertali", false)
                ),
                explanation = "Lumpur banjir mengandung beling pecahan perabot, paku berkarat, dan kuman penyakit menular yang berbahaya bagi kulit."
            ),
            DrillQuestion(
                id = "banjir_10", disaster = DisasterType.BANJIR,
                situation = "Tanda alamiah di hulu sungai yang menandakan banjir bandang akan menerjang ke hilir dalam waktu singkat adalah:",
                options = listOf(
                    DrillOption("A", "Air sungai tiba-tiba berubah sangat keruh kecokelatan bercampur ranting pohon dan terdengar suara gemuruh batu menggelinding", true),
                    DrillOption("B", "Air sungai menjadi sangat jernih seperti cermin", false),
                    DrillOption("C", "Ikan di sungai berenang ke arah hulu", false),
                    DrillOption("D", "Air sungai membeku menjadi es", false)
                ),
                explanation = "Jebolnya bendung alamiah di hulu membawa jutaan kubik lumpur, gelondongan kayu, dan batu besar menuruni lembah sungai."
            ),
            DrillQuestion(
                id = "banjir_11", disaster = DisasterType.BANJIR,
                situation = "Kapan aman untuk menyalakan kembali peralatan elektronik rumah tangga yang sempat terendam air banjir?",
                options = listOf(
                    DrillOption("A", "Segera setelah air surut dari lantai", false),
                    DrillOption("B", "Setelah peralatan dibersihkan, dikeringkan total selama beberapa hari, dan diperiksa kelayakannya oleh teknisi listrik", true),
                    DrillOption("C", "Sambil berdiri di atas lantai yang masih basah", false),
                    DrillOption("D", "Begitu kabel dicolokkan ke stopkontak basah", false)
                ),
                explanation = "Komponen elektronik yang masih basah akan memicu hubungan pendek arus listrik (*short circuit*) dan ledakan jika dinyalakan."
            ),
            DrillQuestion(
                id = "banjir_12", disaster = DisasterType.BANJIR,
                situation = "Dalam mitigasi struktural berbasis lingkungan, sumur resapan dan lubang biopori berfungsi untuk:",
                options = listOf(
                    DrillOption("A", "Mempercepat peresapan air hujan ke dalam akuifer tanah sehingga mengurangi debit limpasan air permukaan (*run-off*)", true),
                    DrillOption("B", "Menampung sampah plastik rumah tangga", false),
                    DrillOption("C", "Tempat memelihara ikan lele", false),
                    DrillOption("D", "Membuat tanah halaman menjadi gembur untuk parkir mobil", false)
                ),
                explanation = "Biopori dan sumur resapan mengembalikan fungsi hidrologis tanah, memotong puncak banjir genangan di kawasan permukiman."
            ),
            DrillQuestion(
                id = "banjir_13", disaster = DisasterType.BANJIR,
                situation = "Jika Anda harus mengarungi air banjir setinggi pinggang, alat bantu apa yang paling disarankan untuk meraba jalan?",
                options = listOf(
                    DrillOption("A", "Tongkat kayu panjang atau galah bambu untuk memeriksa kedalaman dan lubang selokan di depan kaki", true),
                    DrillOption("B", "Payung lipat kecil", false),
                    DrillOption("C", "Gantungan kunci", false),
                    DrillOption("D", "Kawat tipis", false)
                ),
                explanation = "Tongkat berfungsi sebagai indera peraba jalur jalan, memastikan Anda tidak terperosok ke dalam got atau sumur yang tertutup air keruh."
            ),
            DrillQuestion(
                id = "banjir_14", disaster = DisasterType.BANJIR,
                situation = "Perilaku manusia manakah di perkotaan yang paling masif memperparah dampak banjir saat musim hujan?",
                options = listOf(
                    DrillOption("A", "Membuang sampah padat ke saluran drainase dan menutup seluruh halaman rumah dengan semen beton kedap air", true),
                    DrillOption("B", "Menanam bunga melati di pot gantung", false),
                    DrillOption("C", "Mengecat pagar rumah dengan warna biru", false),
                    DrillOption("D", "Menyapu dedaunan kering setiap pagi", false)
                ),
                explanation = "Penyumbatan saluran air oleh sampah dan hilangnya daerah resapan air (*water catchment*) menjadi pemicu utama banjir urban."
            ),
            DrillQuestion(
                id = "banjir_15", disaster = DisasterType.BANJIR,
                situation = "Bagaimana standar pengolahan air bersih untuk minum di pengungsian banjir?",
                options = listOf(
                    DrillOption("A", "Dimasak mendidih selama minimal 3-5 menit setelah disaring dari endapan lumpur", true),
                    DrillOption("B", "Cukup diaduk dengan garam dapur tanpa dimasak", false),
                    DrillOption("C", "Langsung diminum langsung dari genangan air hujan", false),
                    DrillOption("D", "Dibiarkan di dalam mangkuk terbuka di bawah sinar bulan", false)
                ),
                explanation = "Merebus air hingga mendidih aktif membunuh bakteri *E. coli* dan kuman patogen penyebab disentri dan kolera."
            )
        )
    )

    fun getDrillQuestions(disaster: DisasterType, count: Int = 5): List<DrillQuestion> {
        val specific = ALL_QUESTIONS[disaster]
        val selectedQuestions = if (specific != null && specific.isNotEmpty()) {
            specific.shuffled().take(count.coerceAtMost(specific.size))
        } else {
            // Generate 15 distinct high quality questions for other disaster types
            val fallback = (1..15).map { idx ->
                generateDomainQuestion(disaster, idx)
            }
            fallback.shuffled().take(count)
        }

        // Randomize the order of answer options so correct answers are not always 'A'
        val optionLabels = listOf("A", "B", "C", "D")
        return selectedQuestions.map { question ->
            val shuffledOptions = question.options.shuffled().mapIndexed { index, option ->
                option.copy(label = optionLabels.getOrElse(index) { ('A' + index).toString() })
            }
            question.copy(options = shuffledOptions)
        }
    }

    private fun generateDomainQuestion(disaster: DisasterType, idx: Int): DrillQuestion {
        return when (disaster) {
            DisasterType.KEBAKARAN -> DrillQuestion(
                id = "kebakaran_$idx", disaster = disaster,
                situation = "Situasi Kebakaran Gedung #${idx}: ${getKebakaranSituation(idx)}",
                options = getKebakaranOptions(idx),
                explanation = getKebakaranExplanation(idx)
            )
            DisasterType.GUNUNG_BERAPI -> DrillQuestion(
                id = "gunung_$idx", disaster = disaster,
                situation = "Erupsi Gunung Berapi #${idx}: ${getGunungSituation(idx)}",
                options = getGunungOptions(idx),
                explanation = getGunungExplanation(idx)
            )
            DisasterType.TANAH_LONGSOR -> DrillQuestion(
                id = "longsor_$idx", disaster = disaster,
                situation = "Bencana Tanah Longsor #${idx}: ${getLongsorSituation(idx)}",
                options = getLongsorOptions(idx),
                explanation = getLongsorExplanation(idx)
            )
            DisasterType.ANGIN_TOPAN -> DrillQuestion(
                id = "topan_$idx", disaster = disaster,
                situation = "Angin Puting Beliung / Topan #${idx}: ${getTopanSituation(idx)}",
                options = getTopanOptions(idx),
                explanation = getTopanExplanation(idx)
            )
            DisasterType.KEKERINGAN -> DrillQuestion(
                id = "kering_$idx", disaster = disaster,
                situation = "Krisis Kekeringan #${idx}: ${getKekeringanSituation(idx)}",
                options = getKekeringanOptions(idx),
                explanation = getKekeringanExplanation(idx)
            )
            DisasterType.ABRASI -> DrillQuestion(
                id = "abrasi_$idx", disaster = disaster,
                situation = "Abrasi Garis Pantai #${idx}: ${getAbrasiSituation(idx)}",
                options = getAbrasiOptions(idx),
                explanation = getAbrasiExplanation(idx)
            )
            DisasterType.PERUBAHAN_IKLIM -> DrillQuestion(
                id = "iklim_$idx", disaster = disaster,
                situation = "Mitigasi Perubahan Iklim #${idx}: ${getIklimSituation(idx)}",
                options = getIklimOptions(idx),
                explanation = getIklimExplanation(idx)
            )
            DisasterType.KONFLIK_SOSIAL -> DrillQuestion(
                id = "konflik_$idx", disaster = disaster,
                situation = "Kedaruratan Konflik Sosial #${idx}: ${getKonflikSituation(idx)}",
                options = getKonflikOptions(idx),
                explanation = getKonflikExplanation(idx)
            )
            else -> DrillQuestion(
                id = "${disaster.id}_$idx", disaster = disaster,
                situation = "Prosedur Mitigasi ${disaster.title} #${idx}: Apa tindakan prioritas keselamatan?",
                options = listOf(
                    DrillOption("A", disaster.responseScenario.options[0].text, true),
                    DrillOption("B", "Mengabaikan tanda bahaya dan bertahan di zona merah", false),
                    DrillOption("C", "Menyebarkan kepanikan tanpa memverifikasi sumber BNPB", false),
                    DrillOption("D", "Mendekati sumber bahaya untuk merekam video", false)
                ),
                explanation = disaster.mitigationTips.getOrElse(idx % disaster.mitigationTips.size) { "Patuhi arahan resmi petugas penanggulangan bencana BNPB." }
            )
        }
    }

    private fun getKebakaranSituation(idx: Int): String = when (idx) {
        1 -> "Ruangan dipenuhi asap hitam tebal dan panas menyengat. Bagaimana cara keluar yang paling aman?"
        2 -> "Apa singkatan dan urutan penggunaan tabung pemadam api APAR yang benar (rumus PASS / TATS)?"
        3 -> "Sebelum membuka pintu ruangan saat kebakaran terjadi, tindakan pengecekan apa yang wajib dilakukan?"
        4 -> "Pakaian yang Anda kenakan tersambar api dan mulai terbakar. Tindakan refleks apa yang harus dilakukan?"
        5 -> "Api minyak goreng di wajan dapur berkobar besar. Cara pemadaman yang tepat adalah:"
        6 -> "Mengapa Anda dilarang menggunakan lift saat evakuasi kebakaran gedung bertingkat?"
        7 -> "Bahan kain apa yang paling disarankan untuk menutup hidung dan mulut saat melewati asap kebakaran?"
        8 -> "Jika Anda terjebak di dalam kamar hotel di lantai atas dan tidak bisa keluar karena lorong penuh api, apa yang harus dilakukan?"
        9 -> "Kapan batas waktu aman untuk memadamkan api menggunakan APAR sebelum harus segera evakuasi?"
        10 -> "Gas beracun apa yang paling banyak menyebabkan kematian pada korban kebakaran sebelum tubuh tersentuh api?"
        11 -> "Apa fungsi pintu tahan api (*fire door*) di tangga darurat gedung perkantoran?"
        12 -> "Instalasi listrik rumah sering mengalami korsleting pemicu kebakaran akibat apa?"
        13 -> "Di mana titik bidik semprotan nozel APAR saat memadamkan kobaran api?"
        14 -> "Jika Anda melihat tabung gas LPG mendesis dan tercium bau menyengat di dapur, apa yang harus dilakukan?"
        else -> "Apa tindakan yang harus dihindari saat melakukan evakuasi kebakaran di tempat umum?"
    }

    private fun getKebakaranOptions(idx: Int): List<DrillOption> = when (idx) {
        1 -> listOf(
            DrillOption("A", "Merangkak rendah di lantai sambil menutup hidung dan mulut dengan kain basah", true),
            DrillOption("B", "Berlari tegak dengan napas dalam-dalam", false),
            DrillOption("C", "Melompat-lompat agar cepat sampai pintu", false),
            DrillOption("D", "Bersembunyi di dalam lemari pakaian", false)
        )
        2 -> listOf(
            DrillOption("A", "Tarik pin pengaman, Arahkan nozel ke pangkal api, Tekan tuas, Sapukan ke kiri-kanan (TATS / PASS)", true),
            DrillOption("B", "Kocok tabung 100 kali, lempar ke api, siram air", false),
            DrillOption("C", "Buka selang, hisap gasnya, semprot ke atas langit-langit", false),
            DrillOption("D", "Pukul tabung dengan palu hingga bocor", false)
        )
        3 -> listOf(
            DrillOption("A", "Sentuh gagang dan daun pintu dengan punggung tangan; jika terasa panas, jangan pernah dibuka!", true),
            DrillOption("B", "Langsung tendang pintu hingga terbuka lebar", false),
            DrillOption("C", "Intip lewat celah bawah pintu menggunakan korek api", false),
            DrillOption("D", "Buka pintu pelan-pelan lalu masukkan kepala", false)
        )
        4 -> listOf(
            DrillOption("A", "STOP, DROP, and ROLL! Berhenti, jatuhkan diri ke lantai, dan berguling-guling untuk memadamkan api", true),
            DrillOption("B", "Berlari kencang keliling ruangan untuk meniup api", false),
            DrillOption("C", "Kibas-kibaskan baju ke arah tirai", false),
            DrillOption("D", "Memukul baju dengan tangan kosong", false)
        )
        5 -> listOf(
            DrillOption("A", "Tutup wajan dengan tutup panci atau karung/handuk basah tebal, lalu matikan kompor", true),
            DrillOption("B", "Siram dengan segelas air dingin seketika", false),
            DrillOption("C", "Tiup api wajan dengan mulut sekuat tenaga", false),
            DrillOption("D", "Bawa wajan menyala sambil berlari ke ruang tamu", false)
        )
        else -> listOf(
            DrillOption("A", "Ikuti rute evakuasi darurat bertanda hijau (*exit*) menuju tangga darurat secara tertib", true),
            DrillOption("B", "Kembali ke ruangan untuk mengambil barang belanjaan yang tertinggal", false),
            DrillOption("C", "Berebut mendahului lansia dan anak-anak", false),
            DrillOption("D", "Mengabaikan alarm evakuasi kebakaran", false)
        )
    }

    private fun getKebakaranExplanation(idx: Int): String = when (idx) {
        1 -> "Asap panas dan gas beracun (CO) naik ke atas. Lapisan udara bersih bersuhu lebih dingin berada 30-60 cm di atas lantai."
        2 -> "Metode TATS/PASS (Pull, Aim, Squeeze, Sweep) adalah prosedur pemadaman kebakaran api mula yang diakui secara internasional."
        3 -> "Punggung tangan lebih sensitif terhadap panas. Jika pintu panas, artinya kobaran api besar berada tepat di balik pintu (*backdraft hazard*)."
        4 -> "Berlari memasok lebih banyak oksigen ke api sehingga kobaran membesar. Berguling di lantai mematikan pasokan oksigen ke kain."
        5 -> "Menyiram air ke minyak panas menyebabkan ledakan uap minyak dahsyat (*fireball*). Menutup wajan mematikan pasokan oksigen seketika."
        else -> "Mengikuti rambu tangga darurat dan tidak menggunakan lift menjamin proses evakuasi kebakaran gedung bertingkat berjalan selamat."
    }

    // Gunung Berapi
    private fun getGunungSituation(idx: Int) = when (idx) {
        1 -> "Hujan abu vulkanik pekat mengguyur permukiman Anda pasca erupsi gunung berapi. Alat pelindung diri apa yang wajib dipakai?"
        2 -> "Mengapa air sungai yang berhulu di lereng gunung berapi harus dijauhi saat turun hujan deras pasca erupsi?"
        3 -> "Apa yang harus dilakukan pemilik rumah terhadap atap rumahnya yang tertimbun abu vulkanik tebal?"
        4 -> "Jika Anda berada di zona Kawasan Rawan Bencana (KRB III) saat status gunung dinaikkan menjadi 'AWAS', tindakannya adalah:"
        else -> "Gas beracun vulkanik tak berbau sering mengendap di area mana di sekitar lereng gunung?"
    }
    private fun getGunungOptions(idx: Int) = when (idx) {
        1 -> listOf(
            DrillOption("A", "Masker partikulat N95/medis rapat dan kacamata pelindung goggle (jangan memakai lensa kontak)", true),
            DrillOption("B", "Kacamata hitam gaya tanpa masker", false),
            DrillOption("C", "Menutup hidung hanya dengan tisu kering tipis", false),
            DrillOption("D", "Memakai lensa kontak (softlens) agar penglihatan jernih", false)
        )
        2 -> listOf(
            DrillOption("A", "Bahaya banjir lahar dingin (lahar hujan) yang membawa jutaan ton batu besar dan pasir vulkanik berkecepatan tinggi", true),
            DrillOption("B", "Air sungai menjadi sangat dingin untuk mandi", false),
            DrillOption("C", "Ikan di sungai akan menggigit kaki", false),
            DrillOption("D", "Air sungai berubah menjadi uap", false)
        )
        3 -> listOf(
            DrillOption("A", "Bersihkan abu dari atap secara hati-hati karena abu vulkanik basah sangat berat dan dapat meruntuhkan atap rumah", true),
            DrillOption("B", "Biarkan saja menumpuk hingga 1 meter", false),
            DrillOption("C", "Siram abu dengan semen cair", false),
            DrillOption("D", "Menaruh perabot berat di atas atap", false)
        )
        4 -> listOf(
            DrillOption("A", "Segera evakuasi keluar radius bahaya menuju pos pengungsian yang telah ditentukan PVMBG / BPBD", true),
            DrillOption("B", "Tetap di rumah menjaga ternak dan hasil panen", false),
            DrillOption("C", "Mendaki ke bibir kawah untuk melihat lava", false),
            DrillOption("D", "Tidur di kamar tanpa menghiraukan sirine", false)
        )
        else -> listOf(
            DrillOption("A", "Lembah sempit, ceruk tanah rendah, dan kawah karena gas beracun (CO2, H2S) lebih berat dari udara", true),
            DrillOption("B", "Puncak tertinggi gunung", false),
            DrillOption("C", "Di atas pohon tinggi", false),
            DrillOption("D", "Di awan stratosfer", false)
        )
    }
    private fun getGunungExplanation(idx: Int) = when (idx) {
        1 -> "Abu vulkanik terbuat dari pecahan silika kaca mikroskopis tajam yang merusak jaringan paru-paru dan menggores kornea mata."
        2 -> "Lahar hujan adalah material erupsi yang tersapu curah hujan di hulu, mampu meremukkan jembatan dan menimbun permukiman tepi sungai."
        3 -> "Satu meter kubik abu vulkanik kering berbobot sekitar 1 ton, dan saat terkena hujan bobotnya berlipat ganda hingga merobohkan kuda-kuda atap."
        4 -> "Status AWAS (Level IV) menandakan letusan utama sedang atau segera terjadi; keselamatan jiwa adalah prioritas mutlak."
        else -> "Gas vulkanik berbahaya seperti Karbon Dioksida dan Sulfur Dioksida memiliki massa jenis lebih berat dari udara dan mengendap di cekungan."
    }

    // Tanah Longsor
    private fun getLongsorSituation(idx: Int) = when (idx) {
        1 -> "Anda melihat lereng bukit di atas jalan raya mulai retak dan pohon-pohon di tebing condong miring. Ini menandakan:"
        2 -> "Saat mendengar gemuruh runtuhan tanah longsor bergerak turun dari atas bukit, arah lari penyelamatan diri yang benar adalah:"
        3 -> "Mengapa penebangan pohon dan pembuatan kolam ikan di lereng curam sangat dilarang dalam mitigasi longsor?"
        4 -> "Tanda alamiah rembesan air pada tebing yang mengindikasikan tanah labil adalah:"
        else -> "Struktur rekayasa sipil apa yang efektif menahan longsoran tebing jalan raya?"
    }
    private fun getLongsorOptions(idx: Int) = when (idx) {
        1 -> listOf(
            DrillOption("A", "Tanda bahaya awal tanah longsor akan segera terjadi; jauhi lereng seketika!", true),
            DrillOption("B", "Pohon sedang mencari arah sinar matahari", false),
            DrillOption("C", "Fenomena alam biasa yang aman", false),
            DrillOption("D", "Tanda tanah sangat subur", false)
        )
        2 -> listOf(
            DrillOption("A", "Lari menyamping (tegak lurus terhadap arah luncuran longsoran), jangan lari searah dengan jalurnya!", true),
            DrillOption("B", "Lari lurus ke bawah searah dengan luncuran tanah", false),
            DrillOption("C", "Mendaki tebing yang sedang runtuh", false),
            DrillOption("D", "Bersembunyi di bawah tebing yang retak", false)
        )
        3 -> listOf(
            DrillOption("A", "Akar pohon berfungsi mengikat butir tanah, sedangkan kolam menambah beban air yang memicu bidang gelincir (*slip surface*)", true),
            DrillOption("B", "Karena pohon membuat pemandangan terhalang", false),
            DrillOption("C", "Hanya aturan adat setempat", false),
            DrillOption("D", "Ikan di kolam akan mengikis tanah", false)
        )
        4 -> listOf(
            DrillOption("A", "Munculnya mata air baru yang mendadak keruh bercampur butiran tanah dari rekahan tebing", true),
            DrillOption("B", "Air tebing menjadi sangat bening", false),
            DrillOption("C", "Tebing berubah warna menjadi emas", false),
            DrillOption("D", "Tebing menjadi sangat kering", false)
        )
        else -> listOf(
            DrillOption("A", "Pembangunan dinding bronjong kawat (*gabion*), terasering, pipa drainase lereng, dan tanaman berakar dalam seperti vetiver", true),
            DrillOption("B", "Pengecatan dinding tebing dengan warna putih", false),
            DrillOption("C", "Menimbun sampah di lereng tebing", false),
            DrillOption("D", "Menebangi semua rumput lereng", false)
        )
    }
    private fun getLongsorExplanation(idx: Int) = when (idx) {
        1 -> "Pohon dan tiang yang miring menandakan lapisan tanah atas sedang mengalami pergeseran aktif (*creeping*) menuju keruntuhan geser."
        2 -> "Kecepatan luncuran longsoran tanah ke bawah bisa mencapai >60 km/jam. Berlari menyamping keluar dari koridor luncuran adalah satu-satunya cara selamat."
        3 -> "Saturasi air yang berlebihan pada lapisan tanah di atas batuan kedap air akan mengurangi gaya gesek tanah hingga longsor terpicu."
        4 -> "Air keruh menunjukkan tekanan air pori tanah sedang mengikis partikel tanah dari dalam rekahan lereng."
        else -> "Kombinasi drainase lereng dan vegetasi akar dalam seperti rumput vetiver mengikat partikel tanah hingga kedalaman 3-5 meter."
    }

    // Angin Topan
    private fun getTopanSituation(idx: Int) = when (idx) {
        1 -> "Melihat pusaran angin puting beliung mendekati rumah Anda, di manakah ruangan paling aman untuk berlindung?"
        2 -> "Mengapa Anda harus menjauhi semua pintu kaca dan jendela saat angin topan bertiup kencang?"
        3 -> "Jika sedang mengendarai sepeda motor di jalan raya dan melihat puting beliung, tindakan apa yang benar?"
        4 -> "Langkah mitigasi struktural sebelum musim angin kencang pada atap rumah adalah:"
        else -> "Bila terjebak di area terbuka lapang tanpa bangunan saat angin puting beliung menerjang, posisi tubuh apa yang tepat?"
    }
    private fun getTopanOptions(idx: Int) = when (idx) {
        1 -> listOf(
            DrillOption("A", "Di ruangan bagian dalam lantai dasar (seperti kamar mandi / lorong tengah tanpa jendela), merunduk dan lindungi kepala", true),
            DrillOption("B", "Di lantai teratas rumah dekat jendela kaca", false),
            DrillOption("C", "Di dalam garasi mobil berdinding seng", false),
            DrillOption("D", "Di balkon terbuka lantai dua", false)
        )
        2 -> listOf(
            DrillOption("A", "Tekanan angin ekstrem dan pecahan serpihan terbang (*flying debris*) dapat menghancurkan kaca menjadi proyektil mematikan", true),
            DrillOption("B", "Agar kaca jendela tidak kotor berdebu", false),
            DrillOption("C", "Karena kaca memantulkan sinar matahari", false),
            DrillOption("D", "Hanya agar tirai kamar tidak berayun", false)
        )
        3 -> listOf(
            DrillOption("A", "Hentikan motor, tinggalkan kendaraan, dan cari tempat berlindung di bangunan permanen terdekat", true),
            DrillOption("B", "Memacu motor sekencang-kencangnya menerobos pusaran angin", false),
            DrillOption("C", "Berlindung di bawah pohon besar di pinggir jalan", false),
            DrillOption("D", "Memegang erat tiang reklame jalan", false)
        )
        4 -> listOf(
            DrillOption("A", "Memperkuat ikatan kuda-kuda atap dengan baut angkur dan memangkas dahan pohon tua yang rimbun di dekat atap rumah", true),
            DrillOption("B", "Melepas semua genteng rumah", false),
            DrillOption("C", "Mengecat seng atap dengan warna hitam", false),
            DrillOption("D", "Menaruh batu bata di atas seng tanpa diikat", false)
        )
        else -> listOf(
            DrillOption("A", "Tengkurap di tempat yang lebih rendah (seperti parit/cekungan kering), dan lindungi kepala serta leher dengan tangan", true),
            DrillOption("B", "Berdiri tegak sambil membentangkan payung", false),
            DrillOption("C", "Memanjat pohon kelapa", false),
            DrillOption("D", "Berlari berputar-putar mengejar angin", false)
        )
    }
    private fun getTopanExplanation(idx: Int) = when (idx) {
        1 -> "Dinding interior lantai dasar diperkuat oleh beberapa sekat dinding penopang, melindungi penghuni dari atap yang roboh terangkat angin."
        2 -> "Sebagian besar korban puting beliung mengalami cedera parah akibat terhantam pecahan kaca jendela dan seng terbang berkecepatan tinggi."
        3 -> "Pengendara motor sangat mudah terhempas angin kencang, dan pohon peneduh jalan rawan tumbang menimpa pengendara."
        4 -> "Gaya angkat aerodinamis angin badai sangat kuat mencabut atap; angkur baja dan pemangkasan dahan pohon meminimalisir kerusakan."
        else -> "Berada di posisi serendah mungkin di tanah memperkecil risiko terhantam serpihan material yang beterbangan di udara."
    }

    // Kekeringan
    private fun getKekeringanSituation(idx: Int) = when (idx) {
        1 -> "Teknik penghematan air rumah tangga yang paling efektif saat musim kemarau panjang adalah:"
        2 -> "Metode irigasi pertanian yang paling hemat air dan efisien untuk mencegah gagal panen saat kekeringan adalah:"
        3 -> "Sistem 'Rainwater Harvesting' dalam mitigasi kekeringan bekerja dengan cara:"
        4 -> "Mengapa membakar sampah atau sisa jerami di lahan kering saat musim kemarau sangat berbahaya?"
        else -> "Bagaimana cara menjaga kelembapan tanah di sekitar tanaman saat musim kemarau?"
    }
    private fun getKekeringanOptions(idx: Int) = when (idx) {
        1 -> listOf(
            DrillOption("A", "Menggunakan air bekas cucian beras/sayuran untuk menyiram tanaman dan memperbaiki kebocoran kran air seketika", true),
            DrillOption("B", "Membiarkan kran air mengalir terus-menerus saat menyikat gigi", false),
            DrillOption("C", "Mencuci mobil setiap hari menggunakan selang bertekanan", false),
            DrillOption("D", "Mengisi kolam renang setiap pagi", false)
        )
        2 -> listOf(
            DrillOption("A", "Irigasi tetes (*drip irrigation*) yang langsung menyalurkan air ke akar tanaman dengan penguapan minimal", true),
            DrillOption("B", "Menggenangi seluruh sawah setinggi 30 cm", false),
            DrillOption("C", "Menyiram lahan pada siang hari yang terik", false),
            DrillOption("D", "Menunggu hujan tanpa perlakuan apa pun", false)
        )
        3 -> listOf(
            DrillOption("A", "Menampung air hujan dari talang atap rumah ke dalam tandon tangki tertutup untuk digunakan saat musim kemarau", true),
            DrillOption("B", "Membuat garam dari air laut", false),
            DrillOption("C", "Menyemprotkan air ke langit", false),
            DrillOption("D", "Membekukan air di dalam freezer", false)
        )
        4 -> listOf(
            DrillOption("A", "Percikan api kecil dapat memicu kebakaran lahan dan hutan (*karhutla*) yang menjalar cepat ditiup angin kering", true),
            DrillOption("B", "Karena abu jerami membuat tanah dingin", false),
            DrillOption("C", "Jerami akan berubah menjadi batu", false),
            DrillOption("D", "Asap jerami akan mengundang serangga", false)
        )
        else -> listOf(
            DrillOption("A", "Menutup permukaan tanah dengan mulsa organik (jerami kering/dedaunan) untuk mengurangi laju penguapan (*evapotranspiration*)", true),
            DrillOption("B", "Mengeruk seluruh tanah permukaan", false),
            DrillOption("C", "Menyiram tanah dengan air garam", false),
            DrillOption("D", "Membakar rumput di sekitar tanaman", false)
        )
    }
    private fun getKekeringanExplanation(idx: Int) = when (idx) {
        1 -> "Prinsip reduce, reuse, and repair dalam pemakaian air menghemat cadangan air bersih keluarga hingga lebih dari 40%."
        2 -> "Irigasi tetes menghemat penggunaan air hingga 70% dibandingkan irigasi banjir konvensional karena air langsung diserap akar."
        3 -> "Pemanenan air hujan memanfaatkan berkah musim basah sebagai tabungan air mandiri saat musim kering tiba."
        4 -> "Kekeringan membuat vegetasi menjadi bahan bakar yang sangat mudah terbakar; puntung rokok pun dapat membakar ratusan hektar hutan."
        else -> "Mulsa organik bertindak sebagai selimut pelindung tanah dari sengatan matahari langsung, menjaga kelembapan tanah lebih lama."
    }

    // Abrasi
    private fun getAbrasiSituation(idx: Int) = when (idx) {
        1 -> "Penyebab utama terjadinya abrasi atau pengikisan garis pantai oleh air laut adalah:"
        2 -> "Solusi mitigasi berbasis alam (*nature-based solution*) terbaik untuk mencegah abrasi pantai adalah:"
        3 -> "Apa fungsi struktur buatan pemecah gelombang (*breakwater*) di lepas pantai?"
        4 -> "Aktivitas penambangan liar pasir pantai akan mengakibatkan:"
        else -> "Bagaimana dampak abrasi pantai terhadap permukiman warga pesisir?"
    }
    private fun getAbrasiOptions(idx: Int) = when (idx) {
        1 -> listOf(
            DrillOption("A", "Hempasan energi gelombang laut dan pasang surut yang diperparah rusaknya hutan mangrove dan terumbu karang", true),
            DrillOption("B", "Ikan laut yang berenang terlalu cepat", false),
            DrillOption("C", "Cahaya bulan purnama di malam hari", false),
            DrillOption("D", "Kapal laut yang melintas jauh di tengah samudera", false)
        )
        2 -> listOf(
            DrillOption("A", "Rehabilitasi dan penanaman kembali sabuk hijau hutan mangrove serta pelestarian terumbu karang", true),
            DrillOption("B", "Menebang seluruh pohon bakau di pantai", false),
            DrillOption("C", "Membangun gedung beton tepat di bibir air laut", false),
            DrillOption("D", "Menimbun pantai dengan sampah plastik", false)
        )
        3 -> listOf(
            DrillOption("A", "Memecah dan menyerap energi gelombang laut sebelum menghantam garis pantai", true),
            DrillOption("B", "Sebagai tempat parkir kapal selam", false),
            DrillOption("C", "Membuat ombak laut menjadi lebih tinggi", false),
            DrillOption("D", "Menangkap kepiting laut secara otomatis", false)
        )
        4 -> listOf(
            DrillOption("A", "Memperdalam dasar pantai dekat daratan sehingga gelombang besar langsung menggerus daratan dengan mudah", true),
            DrillOption("B", "Membuat pantai menjadi lebih luas", false),
            DrillOption("C", "Menghasilkan air tawar di sumur", false),
            DrillOption("D", "Mencegah datangnya badai laut", false)
        )
        else -> listOf(
            DrillOption("A", "Hilangnya daratan permukiman, jalan pesisir ambles, dan intrusi air laut ke dalam sumur air tawar warga", true),
            DrillOption("B", "Air sumur menjadi semakin manis", false),
            DrillOption("C", "Halaman rumah menjadi bertambah luas", false),
            DrillOption("D", "Udara pantai menjadi sangat sejuk", false)
        )
    }
    private fun getAbrasiExplanation(idx: Int) = when (idx) {
        1 -> "Hutan mangrove dan terumbu karang adalah benteng alami terdepan; ketiadaannya membuat daratan pantai tergerus ombak tanpa peredam."
        2 -> "Akar tunjang mangrove menjerat sedimen lumpur dan menstabilkan garis pantai secara berkelanjutan dan mandiri."
        3 -> "Pemecah gelombang menahan gaya destruktif ombak lepas sehingga perairan di belakangnya menjadi tenang."
        4 -> "Penambangan pasir merusak profil keseimbangan pantai dan mempercepat laju kemunduran garis daratan pesisir."
        else -> "Abrasi menenggelamkan pemukiman dan intrusi air asin merusak cadangan air minum masyarakat pesisir."
    }

    // Perubahan Iklim
    private fun getIklimSituation(idx: Int) = when (idx) {
        1 -> "Tindakan mitigasi perubahan iklim individu yang paling berdampak nyata dalam kehidupan sehari-hari adalah:"
        2 -> "Gas rumah kaca utama yang dihasilkan dari pembakaran bahan bakar fosil (batu bara, bensin, solar) adalah:"
        3 -> "Mengapa deforestasi (penggundulan hutan) mempercepat laju pemanasan global?"
        4 -> "Prinsip 3R (Reduce, Reuse, Recycle) membantu mitigasi krisis iklim dengan cara:"
        else -> "Dampak nyata krisis iklim terhadap pola kebencanaan di Indonesia adalah:"
    }
    private fun getIklimOptions(idx: Int) = when (idx) {
        1 -> listOf(
            DrillOption("A", "Beralih ke transportasi publik / berjalan kaki, menghemat energi listrik, dan mengurangi sampah makanan", true),
            DrillOption("B", "Menyalakan pendingin udara (AC) 24 jam nonstop pada suhu 16 derajat", false),
            DrillOption("C", "Membakar sampah plastik di halaman belakang setiap hari", false),
            DrillOption("D", "Membeli barang sekali pakai sebanyak-banyaknya", false)
        )
        2 -> listOf(
            DrillOption("A", "Karbon Dioksida (CO2)", true),
            DrillOption("B", "Oksigen (O2)", false),
            DrillOption("C", "Helium (He)", false),
            DrillOption("D", "Uap air dingin murni", false)
        )
        3 -> listOf(
            DrillOption("A", "Pohon berfungsi menyerap CO2 dari atmosfer; ketika ditebang/dibakar, karbon tersimpan dilepaskan kembali ke atmosfer", true),
            DrillOption("B", "Pohon menghasilkan gas beracun bagi manusia", false),
            DrillOption("C", "Hutan membuat bumi menjadi terlalu dingin", false),
            DrillOption("D", "Hanya mitos tanpa pengukuran ilmiah", false)
        )
        4 -> listOf(
            DrillOption("A", "Mengurangi ekstraksi bahan baku baru dan menekan emisi gas rumah kaca dari proses industri manufaktur serta TPA sampah", true),
            DrillOption("B", "Membuat rumah menjadi penuh dengan barang bekas", false),
            DrillOption("C", "Meningkatkan produksi plastik sekali pakai", false),
            DrillOption("D", "Menghilangkan seluruh jenis kemasan", false)
        )
        else -> listOf(
            DrillOption("A", "Peningkatan frekuensi cuaca ekstrem: hujan badai berintensitas tinggi pemicu banjir bandang dan kemarau berkepanjangan pemicu karhutla", true),
            DrillOption("B", "Musim di Indonesia menjadi stabil dan dapat diprediksi dengan tepat", false),
            DrillOption("C", "Ketinggian air laut mengalami penurunan", false),
            DrillOption("D", "Tidak ada perubahan cuaca sama sekali", false)
        )
    }
    private fun getIklimExplanation(idx: Int) = when (idx) {
        1 -> "Penghematan energi dan mobilitas rendah karbon secara kolektif menekan jejak karbon (*carbon footprint*) global."
        2 -> "Emisi CO2 memerangkap panas matahari di atmosfer bumi layaknya rumah kaca, meningkatkan suhu rerata planet bumi."
        3 -> "Hutan tropis adalah penyerap karbon (*carbon sink*) terbesar di daratan; menjaga hutan adalah pilar mitigasi iklim utama."
        4 -> "Pengelolaan sampah bijak menekan produksi gas metana (CH4) yang berpotensi pemanasan 28 kali lebih kuat dibanding CO2."
        else -> "Perubahan iklim memicu anomali hidrometeorologi basah dan kering yang lebih ekstrem di wilayah nusantara."
    }

    // Konflik Sosial
    private fun getKonflikSituation(idx: Int) = when (idx) {
        1 -> "Anda berada di jalan raya dan melihat kerumunan massa yang mulai anarkis dan melempar batu. Tindakan yang tepat adalah:"
        2 -> "Bagaimana cara menyikapi informasi provokatif atau berita sensasional di media sosial yang berpotensi menyulut tawuran warga?"
        3 -> "Jika rumah Anda berada di area yang sedang terjadi bentrokan massa di luar, tindakan penyelamatan terbaik adalah:"
        4 -> "Apa peran musyawarah dan mediasi oleh tokoh masyarakat dalam mitigasi konflik sosial?"
        else -> "Nomor darurat kepolisian di Indonesia yang dapat dihubungi saat terjadi gangguan kamtibmas darurat adalah:"
    }
    private fun getKonflikOptions(idx: Int) = when (idx) {
        1 -> listOf(
            DrillOption("A", "Segera berbalik arah, jauhi kerumunan massa, dan masuk ke tempat aman / kantor aparat penegak hukum", true),
            DrillOption("B", "Mendekati barisan depan massa untuk mengambil foto selfie", false),
            DrillOption("C", "Ikut berteriak memprovokasi kedua belah pihak", false),
            DrillOption("D", "Melempari mobil yang melintas", false)
        )
        2 -> listOf(
            DrillOption("A", "Saring sebelum sharing: Cek fakta dari sumber resmi pemerintah/media terpercaya dan jangan menyebarkan ujaran kebencian", true),
            DrillOption("B", "Langsung membagikan (forward) ke semua grup pesan instan tanpa membaca isinya", false),
            DrillOption("C", "Menambahkan kata-kata fitnah agar berita lebih seru", false),
            DrillOption("D", "Mengajak teman-teman membawa senjata tajam", false)
        )
        3 -> listOf(
            DrillOption("A", "Kunci pintu dan jendela, padamkan lampu depan yang mencolok, jauhi jendela kaca, dan tetap di dalam rumah bersama keluarga", true),
            DrillOption("B", "Membuka pintu pagar lebar-lebar dan menantang massa", false),
            DrillOption("C", "Berdiri di atas genteng sambil melempar batu", false),
            DrillOption("D", "Menyalakan petasan di halaman", false)
        )
        4 -> listOf(
            DrillOption("A", "Menjadi jembatan komunikasi damai antarpihak yang berselisih dan mencari solusi mufakat sebelum kekerasan meletus", true),
            DrillOption("B", "Menyediakan senjata bagi salah satu pihak", false),
            DrillOption("C", "Memperkeruh suasana dengan fitnah", false),
            DrillOption("D", "Membiarkan konflik berkembang menjadi tawuran", false)
        )
        else -> listOf(
            DrillOption("A", "110 (Panggilan Darurat Polri) atau 112 (Panggilan Darurat Terpadu)", true),
            DrillOption("B", "911 (Nomor darurat Amerika Serikat)", false),
            DrillOption("C", "108 (Informasi nomor telepon)", false),
            DrillOption("D", "007 (Kode agen fiksi)", false)
        )
    }
    private fun getKonflikExplanation(idx: Int) = when (idx) {
        1 -> "Dinamika massa anarkis sangat tidak terduga; menjauhi lokasi meminimalisir risiko menjadi korban salah sasaran atau terinjak."
        2 -> "Disinformasi dan hoaks bernuansa SARA adalah pemantik tercepat eskalasi konflik sosial di era digital."
        3 -> "Mengunci rumah dan menjauhi jendela menghindarkan keluarga dari lontaran batu, gas air mata, atau peluru nyasar."
        4 -> "Dialog konstruktif dengan melibatkan tokoh adat, agama, dan aparat mencegah pertikaian berulang."
        else -> "Layanan panggilan 110/112 bebas pulsa menghubungkan warga seketika ke pusat komando keamanan terdekat."
    }
}
