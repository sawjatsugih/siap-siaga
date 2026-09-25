package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material.icons.filled.ZoomOut
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.ProgressionRepository
import com.example.data.ProgressionReward
import com.example.model.DisasterType
import kotlinx.coroutines.launch

enum class HumanActionType {
    DROP_COVER_HOLD,
    SHUT_OFF_UTILITIES,
    EVACUATE_STAIRS,
    RUN_UPHILL_TSUNAMI,
    EVACUATE_VERTICAL_ROOF,
    CRAWL_LOW_SMOKE,
    OPERATE_FIRE_EXTINGUISHER,
    TOUCH_DOOR_TEST,
    WEAR_N95_GOGGLES,
    RUN_LATERAL_SLOPE,
    SHELTER_INTERIOR_ROOM,
    RAINWATER_HARVEST,
    PLANT_MANGROVE,
    AVOID_CROWD_PEACE
}

data class SafetyStep(
    val stepIndex: Int,
    val title: String,
    val instruction: String,
    val bnpbRule: String,
    val actionType: HumanActionType
)

object OfflineSafetyGuideProvider {

    fun getSteps(disaster: DisasterType): List<SafetyStep> = when (disaster) {
        DisasterType.GEMPA_BUMI -> listOf(
            SafetyStep(
                stepIndex = 1,
                title = "1. DROP, COVER, HOLD ON (Berlutut, Berlindung, Bertahan)",
                instruction = "Seketika saat guncangan terasa, jatuhkan tubuh ke lantai (DROP), masuk ke bawah meja kokoh untuk melindungi kepala dan leher (COVER), serta pegang erat kaki meja hingga guncangan selesai (HOLD ON).",
                bnpbRule = "Standar BNPB: Jangan berdiri dekat jendela kaca atau mencoba berlari keluar saat tanah berguncang.",
                actionType = HumanActionType.DROP_COVER_HOLD
            ),
            SafetyStep(
                stepIndex = 2,
                title = "2. Putuskan Arus Listrik (MCB) & Cabut Regulator Gas",
                instruction = "Setelah guncangan utama berhenti, segera datangi meteran listrik rumah dan matikan saklar MCB. Lepaskan juga regulator tabung gas untuk mencegah pemicu api sekunder.",
                bnpbRule = "Standar BNPB: Korsleting listrik pasca gempa adalah penyebab 60% kebakaran permukiman.",
                actionType = HumanActionType.SHUT_OFF_UTILITIES
            ),
            SafetyStep(
                stepIndex = 3,
                title = "3. Evakuasi Keluar Lewat Tangga Darurat",
                instruction = "Berjalan cepat menuruni tangga darurat secara tertib sambil melindungi kepala dengan tas/tangan. Jangan pernah menggunakan lift, dan segera menuju lapangan terbuka.",
                bnpbRule = "Standar BNPB: Lift rawan macet seketika akibat kabel putus atau pemadaman listrik otomatis.",
                actionType = HumanActionType.EVACUATE_STAIRS
            )
        )

        DisasterType.TSUNAMI -> listOf(
            SafetyStep(
                stepIndex = 1,
                title = "1. Lari ke Dataran Tinggi / Perbukitan Minimal 20 Meter",
                instruction = "Bila merasakan gempa kuat di pantai atau melihat air laut surut drastis, segera lari menuju bukit atau daerah berketinggian minimal 20 meter / menjauh 2 km tanpa menunggu sirine.",
                bnpbRule = "Pedoman 20-20-20 BNPB: Gempa 20 detik, ada waktu 20 menit, lari ke ketinggian 20 meter.",
                actionType = HumanActionType.RUN_UPHILL_TSUNAMI
            ),
            SafetyStep(
                stepIndex = 2,
                title = "2. Evakuasi Vertikal ke Lantai Atas Gedung Beton",
                instruction = "Jika terjebak dan waktu sempit untuk ke bukit, naiklah segera ke lantai 3 atau atap gedung bertulang beton yang kokoh (Tempat Evakuasi Sementara).",
                bnpbRule = "Standar BNPB: Gedung beton bertulang mampu menahan hantaman hidrolik massa air tsunami.",
                actionType = HumanActionType.EVACUATE_VERTICAL_ROOF
            ),
            SafetyStep(
                stepIndex = 3,
                title = "3. Tetap di Ketinggian Hingga Pengumuman Resmi",
                instruction = "Jangan sekali-kali turun ke pesisir setelah gelombang pertama surut. Gelombang kedua dan ketiga sering kali jauh lebih dahsyat. Tunggu arahan BMKG/BPBD.",
                bnpbRule = "Standar BNPB: Tsunami adalah rangkaian gelombang yang dapat berlangsung berjam-jam.",
                actionType = HumanActionType.RUN_UPHILL_TSUNAMI
            )
        )

        DisasterType.KEBAKARAN -> listOf(
            SafetyStep(
                stepIndex = 1,
                title = "1. Merangkak Rendah di Bawah Asap Pekat",
                instruction = "Asap panas dan racun gas karbon monoksida naik ke langit-langit. Merangkaklah dengan posisi dada 30 cm dari lantai sambil menutup hidung dan mulut dengan kain basah.",
                bnpbRule = "Standar Damkar: Udara bersih dan dingin berada dekat lantai; menghirup 2 napas asap pekat bisa membuat pingsan.",
                actionType = HumanActionType.CRAWL_LOW_SMOKE
            ),
            SafetyStep(
                stepIndex = 2,
                title = "2. Periksa Pintu Sebelum Dibuka",
                instruction = "Sebelum membuka pintu ruangan evakuasi, sentuh daun pintu dan gagang logamnya dengan punggung tangan. Jika terasa panas membara, jangan pernah membukanya karena ada api besar di baliknya.",
                bnpbRule = "Standar Damkar: Membuka pintu panas memicu ledakan 'backdraft' akibat oksigen mendadak masuk.",
                actionType = HumanActionType.TOUCH_DOOR_TEST
            ),
            SafetyStep(
                stepIndex = 3,
                title = "3. Padamkan Api Awal dengan APAR (Teknik P-A-S-S)",
                instruction = "Jika api masih kecil: Pull (Tarik pin), Aim (Arahkan ke pangkal api), Squeeze (Tekan tuas semprot), dan Sweep (Sapukan dari kiri ke kanan secara merata).",
                bnpbRule = "Standar Damkar: Jangan arahkan semprotan ke lidah api atas, semprotkan tepat ke bahan yang terbakar di bawah.",
                actionType = HumanActionType.OPERATE_FIRE_EXTINGUISHER
            )
        )

        DisasterType.GUNUNG_BERAPI -> listOf(
            SafetyStep(
                stepIndex = 1,
                title = "1. Gunakan Masker Partikulat N95 & Kacamata Goggle",
                instruction = "Abu vulkanik mengandung pecahan kaca mikroskopis tajam. Pasang masker respirator N95 menutupi hidung dan mulut rapat, serta gunakan kacamata pelindung tertutup (jangan pakai lensa kontak).",
                bnpbRule = "Standar PVMBG: Lindungi saluran napas dari silikosis dan mata dari luka gores kornea.",
                actionType = HumanActionType.WEAR_N95_GOGGLES
            ),
            SafetyStep(
                stepIndex = 2,
                title = "2. Evakuasi Menjauhi Lembah & Daerah Aliran Sungai",
                instruction = "Segera tinggalkan zona Kawasan Rawan Bencana (KRB III). Jauhi aliran sungai karena hujan di puncak gunung akan memicu banjir lahar dingin berkecepatan tinggi.",
                bnpbRule = "Standar PVMBG: Aliran lahar dingin membawa batu gelondongan besar yang menghancurkan jembatan.",
                actionType = HumanActionType.EVACUATE_STAIRS
            ),
            SafetyStep(
                stepIndex = 3,
                title = "3. Bersihkan Abu di Atap Rumah Secara Berkala",
                instruction = "Abu vulkanik basah sangat berat dan dapat meruntuhkan atap. Gunakan tangga aman dan sapu timbunan abu dari atap sebelum menumpuk tebal.",
                bnpbRule = "Standar BNPB: 10 cm abu basah memiliki bobot setara ratusan kilogram per meter persegi.",
                actionType = HumanActionType.SHUT_OFF_UTILITIES
            )
        )

        DisasterType.BANJIR -> listOf(
            SafetyStep(
                stepIndex = 1,
                title = "1. Putuskan Aliran Listrik Utama Rumah",
                instruction = "Sebelum air banjir merendam stopkontak di dinding, segera matikan MCB utama pada meteran listrik rumah guna mencegah sengatan arus listrik mematikan.",
                bnpbRule = "Standar PLN/BNPB: Jangan menyentuh peralatan listrik apa pun saat berdiri di atas genangan air.",
                actionType = HumanActionType.SHUT_OFF_UTILITIES
            ),
            SafetyStep(
                stepIndex = 2,
                title = "2. Amankan Dokumen & Evakuasi ke Lantai Atas",
                instruction = "Simpan tas siaga dan surat berharga dalam kantong kedap air, lalu berpindahlah ke lantai dua atau tempat tinggi yang aman sebelum air meluap setinggi dada.",
                bnpbRule = "Standar SAR: Siapkan peluit dan pakaian warna mencolok untuk memudahkan identifikasi tim perahu karet.",
                actionType = HumanActionType.EVACUATE_VERTICAL_ROOF
            ),
            SafetyStep(
                stepIndex = 3,
                title = "3. Jangan Menerobos Arus Banjir yang Mengalir",
                instruction = "Arus air setinggi mata kaki (15 cm) yang mengalir deras sanggup merobohkan orang dewasa dan menyembunyikan lubang got terbuka. Gunakan tongkat untuk meraba jalan.",
                bnpbRule = "Standar BNPB: Sebagian besar korban banjir hanyut karena nekat menyeberang arus jalan.",
                actionType = HumanActionType.EVACUATE_STAIRS
            )
        )

        DisasterType.TANAH_LONGSOR -> listOf(
            SafetyStep(
                stepIndex = 1,
                title = "1. Lari Menyamping dari Arah Luncuran Tebing",
                instruction = "Saat mendengar suara gemuruh longsoran tanah dari atas tebing, larilah sekencang-kencangnya ke arah samping (tegak lurus), jangan pernah lari searah luncuran ke bawah!",
                bnpbRule = "Standar Geologi: Kecepatan luncuran longsoran tanah dapat melampaui kecepatan lari manusia.",
                actionType = HumanActionType.RUN_LATERAL_SLOPE
            ),
            SafetyStep(
                stepIndex = 2,
                title = "2. Jauhi Lereng Rawan Retakan Saat Hujan Lebat",
                instruction = "Jika melihat pohon atau tiang miring serta mata air mendadak keruh di tebing, segera evakuasi ke tempat datar yang jauh dari kaki bukit.",
                bnpbRule = "Standar BNPB: Hujan lebat berdurasi lebih dari 2 jam meningkatkan tekanan air pori pemicu longsor.",
                actionType = HumanActionType.EVACUATE_STAIRS
            )
        )

        DisasterType.ANGIN_TOPAN -> listOf(
            SafetyStep(
                stepIndex = 1,
                title = "1. Berlindung di Ruangan Tengah Lantai Dasar",
                instruction = "Masuklah ke ruangan paling dalam di lantai dasar yang tidak memiliki jendela (seperti kamar mandi atau bawah tangga beton). Merunduk dan tutupi kepala dengan kasur/bantal.",
                bnpbRule = "Standar BMKG: Jauhi semua dinding dan pintu kaca dari bahaya pecahan proyektil angin puting beliung.",
                actionType = HumanActionType.SHELTER_INTERIOR_ROOM
            ),
            SafetyStep(
                stepIndex = 2,
                title = "2. Kunci Pintu & Matikan Aliran Listrik",
                instruction = "Tutup semua pintu rapat-rapat dan matikan instalasi listrik rumah untuk menghindari korsleting akibat atap yang tersingkap angin kencang.",
                bnpbRule = "Standar BNPB: Angin badai dapat mencabut seng atap; matikan listrik guna cegah percikan api.",
                actionType = HumanActionType.SHUT_OFF_UTILITIES
            )
        )

        DisasterType.KEKERINGAN -> listOf(
            SafetyStep(
                stepIndex = 1,
                title = "1. Pemanenan & Penampungan Air Hujan Mandiri",
                instruction = "Salurkan air hujan dari talang atap rumah ke dalam tandon tangki tertutup yang dilengkapi filter kassa untuk cadangan air saat musim kemarau.",
                bnpbRule = "Standar BNPB: Rainwater harvesting menjamin ketahanan air bersih keluarga saat sumur mengering.",
                actionType = HumanActionType.RAINWATER_HARVEST
            ),
            SafetyStep(
                stepIndex = 2,
                title = "2. Hemat & Daur Ulang Air untuk Tanaman",
                instruction = "Gunakan kembali air bilasan cucian beras dan sayur untuk menyiram tanaman, serta tutup tanah dengan mulsa jerami agar tanah tidak cepat mengering.",
                bnpbRule = "Standar Konservasi: Daur ulang air menghemat cadangan air tanah hingga 40%.",
                actionType = HumanActionType.RAINWATER_HARVEST
            )
        )

        DisasterType.ABRASI -> listOf(
            SafetyStep(
                stepIndex = 1,
                title = "1. Penanaman Sabuk Hijau Mangrove di Pantai",
                instruction = "Tanam bibit pohon bakau (mangrove) dengan sistem bronjong pelindung di sepanjang pesisir. Akar tunjang bakau mengikat lumpur dan memecah energi gelombang laut.",
                bnpbRule = "Standar Pesisir: Sabuk mangrove lebat meredam hempasan ombak pasang hingga lebih dari 60%.",
                actionType = HumanActionType.PLANT_MANGROVE
            ),
            SafetyStep(
                stepIndex = 2,
                title = "2. Pembangunan Pemecah Gelombang & Jauhi Sempadan",
                instruction = "Pasang tetrapod pemecah ombak di lepas pantai dan pastikan bangunan rumah berada di luar sempadan pantai minimal 100 meter dari titik pasang tertinggi.",
                bnpbRule = "Standar Tata Ruang: Menjaga sempadan pantai melindungi pemukiman dari abrasi berkelanjutan.",
                actionType = HumanActionType.PLANT_MANGROVE
            )
        )

        DisasterType.PERUBAHAN_IKLIM -> listOf(
            SafetyStep(
                stepIndex = 1,
                title = "1. Efisiensi Energi & Gaya Hidup Rendah Karbon",
                instruction = "Matikan peralatan listrik yang tidak terpakai, beralih ke transportasi umum atau sepeda, serta kurangi pemakaian plastik sekali pakai untuk menekan emisi gas rumah kaca.",
                bnpbRule = "Standar Iklim: Pengurangan jejak karbon individu memperlambat laju kenaikan suhu permukaan bumi.",
                actionType = HumanActionType.RAINWATER_HARVEST
            ),
            SafetyStep(
                stepIndex = 2,
                title = "2. Penghijauan & Pengelolaan Sampah Organik",
                instruction = "Tanam pohon peneduh di sekitar rumah dan olah sampah organik menjadi kompos guna mencegah timbulan gas metana dari tempat pembuangan akhir.",
                bnpbRule = "Standar Lingkungan: Pohon bertindak sebagai penyerap karbon alami dan penyaring udara.",
                actionType = HumanActionType.PLANT_MANGROVE
            )
        )

        DisasterType.KONFLIK_SOSIAL -> listOf(
            SafetyStep(
                stepIndex = 1,
                title = "1. Segera Menjauh dari Kerumunan Massa Anarkis",
                instruction = "Jika melihat eskalasi bentrokan massa, jangan mendekat atau merekam. Berbaliklah dengan tenang menuju tempat perlindungan aman atau kantor aparat keamanan.",
                bnpbRule = "Standar Keamanan: Kerumunan anarkis memiliki dinamika tidak terduga; menjauh mencegah salah sasaran.",
                actionType = HumanActionType.AVOID_CROWD_PEACE
            ),
            SafetyStep(
                stepIndex = 2,
                title = "2. Saring Informasi & Lindungi Rumah",
                instruction = "Kunci pintu dan jendela rumah rapat-rapat, jangan mudah terprovokasi hoaks di media sosial, dan hubungi pusat bantuan darurat 110/112 jika lingkungan terancam.",
                bnpbRule = "Standar Kamtibmas: Menolak menyebarkan hoaks provokatif memotong rantai eskalasi bentrok.",
                actionType = HumanActionType.AVOID_CROWD_PEACE
            )
        )
    }
}

/**
 * Educational safety guide dialog showing step-by-step human action simulation graphics.
 * Fully offline, 100% reliable, zero AI API dependency.
 */
@Composable
fun WhatToDoModal(
    disaster: DisasterType,
    onStartDrillQuiz: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val steps = remember(disaster) { OfflineSafetyGuideProvider.getSteps(disaster) }
    var currentStepIndex by remember { mutableIntStateOf(0) }
    val currentStep = steps[currentStepIndex.coerceIn(0, steps.size - 1)]

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val progressionRepo = remember { ProgressionRepository(context) }
    var celebrationReward by remember { mutableStateOf<ProgressionReward?>(null) }
    val badgeTitle = remember(disaster) { ProgressionRepository.getBadgeTitleForDisaster(disaster) }
    var showImageZoomModal by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = modifier
                    .fillMaxWidth(0.94f)
                    .widthIn(max = 620.dp)
                    .padding(vertical = 14.dp)
                    .testTag("what_to_do_modal"),
                shape = RoundedCornerShape(26.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(2.dp, disaster.primaryColor.copy(alpha = 0.5f)),
                elevation = CardDefaults.cardElevation(18.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Top Header Bar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = disaster.softColor,
                                modifier = Modifier.size(46.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(text = disaster.emoji, fontSize = 24.sp)
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "PANDUAN KESELAMATAN NYATA",
                                    color = Color(0xFF0F172A),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Black
                                )
                                Text(
                                    text = "${disaster.title} • Langkah ${currentStepIndex + 1} dari ${steps.size}",
                                    color = disaster.primaryColor,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFF1F5F9))
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Tutup",
                                tint = Color(0xFF475569),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Badge Incentive Banner
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFFFEF3C7),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFDE68A)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.MilitaryTech,
                                contentDescription = null,
                                tint = Color(0xFFB45309),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Selesaikan panduan untuk raih Lencana '$badgeTitle' (+150 PP)",
                                color = Color(0xFF92400E),
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Step indicator dots / pill bar
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        steps.forEachIndexed { idx, _ ->
                            val isActive = idx == currentStepIndex
                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 4.dp)
                                    .height(6.dp)
                                    .width(if (isActive) 32.dp else 10.dp)
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(if (isActive) disaster.primaryColor else Color(0xFFE2E8F0))
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Premium Animated Image Container with Smooth Fading + Zoom Transition
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(210.dp)
                            .clip(RoundedCornerShape(22.dp))
                            .clickable { showImageZoomModal = true }
                            .testTag("safety_image_canvas_card"),
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, disaster.primaryColor.copy(alpha = 0.5f)),
                        elevation = CardDefaults.cardElevation(8.dp)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            // Dedicated image transition with smooth Zoom & Fading
                            AnimatedContent(
                                targetState = currentStep,
                                transitionSpec = {
                                    val isForward = targetState.stepIndex > initialState.stepIndex
                                    val inScale = if (isForward) 0.88f else 1.12f
                                    val outScale = if (isForward) 1.10f else 0.90f
                                    (fadeIn(animationSpec = tween(420, easing = FastOutSlowInEasing)) +
                                            scaleIn(initialScale = inScale, animationSpec = tween(420, easing = FastOutSlowInEasing)))
                                        .togetherWith(
                                            fadeOut(animationSpec = tween(320, easing = FastOutLinearInEasing)) +
                                                    scaleOut(targetScale = outScale, animationSpec = tween(320, easing = FastOutLinearInEasing))
                                        )
                                },
                                label = "safety_image_zoom_fade_transition"
                            ) { step ->
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    HumanActionIllustrationCanvas(
                                        actionType = step.actionType,
                                        disaster = disaster
                                    )
                                }
                            }

                            // Top overlay badges
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.TopCenter)
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = Color.Black.copy(alpha = 0.75f)
                                ) {
                                    Text(
                                        text = "SIMULASI VISUAL GERAKAN",
                                        color = Color(0xFF38BDF8),
                                        fontSize = 9.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }

                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = Color.Black.copy(alpha = 0.65f)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.ZoomIn,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(13.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "Perbesar",
                                            color = Color.White,
                                            fontSize = 9.5.sp,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }
                            }

                            // Bottom overlay indicator
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = Color.Black.copy(alpha = 0.65f),
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .padding(10.dp)
                            ) {
                                Text(
                                    text = "Gambar ${currentStepIndex + 1} / ${steps.size}",
                                    color = Color(0xFFE2E8F0),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Step Text Description & Rules (with smooth vertical fade transition)
                    AnimatedContent(
                        targetState = currentStep,
                        transitionSpec = {
                            (fadeIn(animationSpec = tween(380, delayMillis = 60, easing = FastOutSlowInEasing)) +
                                    slideInVertically(animationSpec = tween(380, delayMillis = 60)) { 20 })
                                .togetherWith(fadeOut(animationSpec = tween(200, easing = FastOutLinearInEasing)))
                        },
                        label = "safety_text_transition"
                    ) { step ->
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Step Title
                            Text(
                                text = step.title,
                                color = Color(0xFF0F172A),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            // Practical Instruction
                            Text(
                                text = step.instruction,
                                color = Color(0xFF334155),
                                fontSize = 13.5.sp,
                                lineHeight = 19.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // BNPB Standard Tip Box
                            Card(
                                colors = CardDefaults.cardColors(containerColor = disaster.softColor),
                                shape = RoundedCornerShape(14.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, disaster.primaryColor.copy(alpha = 0.4f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Security,
                                        contentDescription = null,
                                        tint = disaster.primaryColor,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = step.bnpbRule,
                                        color = Color(0xFF1E293B),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        lineHeight = 16.sp
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Step Navigation Controls (Sebelumnya, Lanjut, Uji Kuis Tanggap)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (currentStepIndex > 0) {
                            OutlinedButton(
                                onClick = { currentStepIndex-- },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                                    .testTag("prev_safety_step_button"),
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF475569))
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Sebelumnya", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        if (currentStepIndex < steps.size - 1) {
                            Button(
                                onClick = { currentStepIndex++ },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                                    .testTag("next_safety_step_button"),
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = disaster.primaryColor)
                            ) {
                                Text("Lanjut", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        } else {
                            Button(
                                onClick = {
                                    coroutineScope.launch {
                                        val reward = progressionRepo.completeSafetyManual(disaster)
                                        celebrationReward = reward
                                    }
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                                    .testTag("complete_safety_manual_button"),
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7)),
                                elevation = ButtonDefaults.buttonElevation(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MilitaryTech,
                                    contentDescription = null,
                                    tint = Color(0xFFFBBF24),
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Selesaikan & Klaim Lencana (+150 PP)",
                                    color = Color.White,
                                    fontSize = 12.5.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Interactive Zoom Modal for Full Inspection
    if (showImageZoomModal) {
        SafetyImageZoomDialog(
            step = currentStep,
            disaster = disaster,
            onDismiss = { showImageZoomModal = false }
        )
    }

    // Celebration Dialog when manual completed
    celebrationReward?.let { reward ->
        BadgeUnlockedCelebrationDialog(
            reward = reward,
            disaster = disaster,
            onStartDrillQuiz = {
                celebrationReward = null
                onStartDrillQuiz()
            },
            onDismiss = {
                celebrationReward = null
                onDismiss()
            }
        )
    }
}

/**
 * Inspection modal allowing users to zoom in and out of the safety action illustration.
 */
@Composable
private fun SafetyImageZoomDialog(
    step: SafetyStep,
    disaster: DisasterType,
    onDismiss: () -> Unit
) {
    var zoomScale by remember { mutableFloatStateOf(1.2f) }
    val animatedScale by animateFloatAsState(
        targetValue = zoomScale,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "zoom_dialog_scale"
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.93f))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 680.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header Bar with step info and close button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Langkah ${step.stepIndex}: ${step.title}",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Detail Ilustrasi Keselamatan • ${disaster.title}",
                            color = Color(0xFF38BDF8),
                            fontSize = 12.sp
                        )
                    }
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF1E293B))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Tutup",
                            tint = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Zoomed Canvas Container
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(Color(0xFF0F172A))
                        .border(2.dp, disaster.primaryColor.copy(alpha = 0.6f), RoundedCornerShape(24.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer {
                                scaleX = animatedScale
                                scaleY = animatedScale
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        HumanActionIllustrationCanvas(
                            actionType = step.actionType,
                            disaster = disaster
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Zoom Control Buttons (+ / - / Reset)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = { zoomScale = (zoomScale - 0.25f).coerceAtLeast(1.0f) },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.ZoomOut, contentDescription = "Perkecil", modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Perkecil")
                    }

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFF1E293B)
                    ) {
                        Text(
                            text = "${(animatedScale * 100).toInt()}%",
                            color = Color(0xFF38BDF8),
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                        )
                    }

                    Button(
                        onClick = { zoomScale = (zoomScale + 0.25f).coerceAtMost(2.5f) },
                        colors = ButtonDefaults.buttonColors(containerColor = disaster.primaryColor),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.ZoomIn, contentDescription = "Perbesar", modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Perbesar")
                    }
                }
            }
        }
    }
}

/**
 * High-clarity Canvas that renders human figures executing specific disaster safety actions.
 */
@Composable
private fun HumanActionIllustrationCanvas(
    actionType: HumanActionType,
    disaster: DisasterType
) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        when (actionType) {
            HumanActionType.DROP_COVER_HOLD -> drawHumanDropCoverHold()
            HumanActionType.SHUT_OFF_UTILITIES -> drawHumanShutOffUtilities()
            HumanActionType.EVACUATE_STAIRS -> drawHumanEvacuateStairs()
            HumanActionType.RUN_UPHILL_TSUNAMI -> drawHumanRunUphillTsunami()
            HumanActionType.EVACUATE_VERTICAL_ROOF -> drawHumanEvacuateVertical()
            HumanActionType.CRAWL_LOW_SMOKE -> drawHumanCrawlLowSmoke()
            HumanActionType.OPERATE_FIRE_EXTINGUISHER -> drawHumanOperateExtinguisher()
            HumanActionType.TOUCH_DOOR_TEST -> drawHumanTouchDoorTest()
            HumanActionType.WEAR_N95_GOGGLES -> drawHumanWearN95Goggles()
            HumanActionType.RUN_LATERAL_SLOPE -> drawHumanRunLateralSlope()
            HumanActionType.SHELTER_INTERIOR_ROOM -> drawHumanShelterInteriorRoom()
            HumanActionType.RAINWATER_HARVEST -> drawHumanRainwaterHarvest()
            HumanActionType.PLANT_MANGROVE -> drawHumanPlantMangrove()
            HumanActionType.AVOID_CROWD_PEACE -> drawHumanAvoidCrowdPeace()
        }
    }
}

// 1. Human Drop, Cover, Hold under a sturdy table
private fun DrawScope.drawHumanDropCoverHold() {
    val w = size.width
    val h = size.height

    // Room Floor & Background
    drawRect(
        brush = Brush.verticalGradient(
            colors = listOf(Color(0xFF1E293B), Color(0xFF0F172A))
        )
    )
    drawRect(color = Color(0xFF334155), topLeft = Offset(0f, h * 0.85f), size = Size(w, h * 0.15f))

    // Sturdy Desk / Table
    val tableX = w * 0.32f
    val tableY = h * 0.32f
    val tableW = w * 0.44f
    val tableH = h * 0.54f

    // Tabletop
    drawRoundRect(
        color = Color(0xFFB45309),
        topLeft = Offset(tableX, tableY),
        size = Size(tableW, 16f),
        cornerRadius = CornerRadius(4f, 4f)
    )
    // Table Legs
    drawRect(color = Color(0xFF78350F), topLeft = Offset(tableX + 6f, tableY + 16f), size = Size(12f, tableH))
    drawRect(color = Color(0xFF78350F), topLeft = Offset(tableX + tableW - 18f, tableY + 16f), size = Size(12f, tableH))

    // Falling plaster debris (danger averted)
    for (i in 0 until 6) {
        val dx = tableX + i * 26f
        val dy = tableY - 14f - (i % 3) * 12f
        drawCircle(color = Color(0xFFE2E8F0), radius = 4.5f, center = Offset(dx, dy))
    }

    // HUMAN FIGURE (Crouched on all fours under the table)
    val humanBodyColor = Color(0xFF38BDF8)
    val skinColor = Color(0xFFFFD1A4)

    val bodyX = tableX + tableW * 0.48f
    val bodyY = h * 0.64f

    // Torso (curved low)
    drawRoundRect(
        color = humanBodyColor,
        topLeft = Offset(bodyX - 28f, bodyY - 10f),
        size = Size(46f, 22f),
        cornerRadius = CornerRadius(10f, 10f)
    )

    // Head (tucked low under desk, facing downwards)
    drawCircle(
        color = skinColor,
        radius = 12f,
        center = Offset(bodyX + 22f, bodyY - 4f)
    )
    // Protective arm shielding back of head/neck
    drawLine(
        color = humanBodyColor,
        start = Offset(bodyX + 2f, bodyY - 6f),
        end = Offset(bodyX + 22f, bodyY - 16f),
        strokeWidth = 8f,
        cap = StrokeCap.Round
    )

    // Arm reaching forward and holding firmly to the table leg (HOLD ON)
    drawLine(
        color = humanBodyColor,
        start = Offset(bodyX + 8f, bodyY + 4f),
        end = Offset(tableX + tableW - 14f, tableY + 36f),
        strokeWidth = 7f,
        cap = StrokeCap.Round
    )

    // Bent Legs (kneeling on floor)
    val legPath = Path().apply {
        moveTo(bodyX - 26f, bodyY)
        lineTo(bodyX - 38f, bodyY + 18f)
        lineTo(bodyX - 18f, bodyY + 24f)
    }
    drawPath(legPath, color = Color(0xFF1E3A8A), style = Stroke(width = 8f, cap = StrokeCap.Round, join = StrokeJoin.Round))

    // Green Safety Shield icon above table
    drawCircle(color = Color(0xFF10B981), radius = 18f, center = Offset(tableX + tableW / 2f, tableY - 26f))
    val checkMark = Path().apply {
        moveTo(tableX + tableW / 2f - 6f, tableY - 26f)
        lineTo(tableX + tableW / 2f - 1f, tableY - 21f)
        lineTo(tableX + tableW / 2f + 7f, tableY - 31f)
    }
    drawPath(checkMark, color = Color.White, style = Stroke(width = 3.5f, cap = StrokeCap.Round))
}

// 2. Human Shutting Off Master Electrical Breaker (MCB) & Gas Regulator
private fun DrawScope.drawHumanShutOffUtilities() {
    val w = size.width
    val h = size.height

    drawRect(Color(0xFF0F172A))
    drawRect(color = Color(0xFF1E293B), topLeft = Offset(0f, h * 0.86f), size = Size(w, h * 0.14f))

    // Electrical MCB Panel on wall
    val panelX = w * 0.65f
    val panelY = h * 0.22f
    drawRoundRect(
        color = Color(0xFF475569),
        topLeft = Offset(panelX, panelY),
        size = Size(54f, 74f),
        cornerRadius = CornerRadius(6f, 6f)
    )
    drawRect(color = Color(0xFF0F172A), topLeft = Offset(panelX + 8f, panelY + 12f), size = Size(38f, 36f))
    // Switch lever pointing OFF (down)
    drawLine(
        color = Color(0xFFEF4444),
        start = Offset(panelX + 27f, panelY + 22f),
        end = Offset(panelX + 27f, panelY + 40f),
        strokeWidth = 6f,
        cap = StrokeCap.Round
    )

    // Gas tank on floor
    drawRoundRect(
        color = Color(0xFF16A34A),
        topLeft = Offset(panelX + 4f, h * 0.62f),
        size = Size(46f, 52f),
        cornerRadius = CornerRadius(14f, 14f)
    )

    // HUMAN FIGURE (Standing, hand reaching up to flip switch OFF)
    val humanX = w * 0.42f
    val humanY = h * 0.54f
    val skinColor = Color(0xFFFFD1A4)

    // Head
    drawCircle(color = skinColor, radius = 15f, center = Offset(humanX, humanY - 52f))
    // Torso
    drawRoundRect(
        color = Color(0xFF2563EB),
        topLeft = Offset(humanX - 14f, humanY - 36f),
        size = Size(28f, 52f),
        cornerRadius = CornerRadius(8f, 8f)
    )
    // Legs
    drawLine(color = Color(0xFF1E293B), start = Offset(humanX - 8f, humanY + 16f), end = Offset(humanX - 10f, h * 0.86f), strokeWidth = 9f, cap = StrokeCap.Round)
    drawLine(color = Color(0xFF1E293B), start = Offset(humanX + 8f, humanY + 16f), end = Offset(humanX + 10f, h * 0.86f), strokeWidth = 9f, cap = StrokeCap.Round)

    // Extended Arm reaching to MCB switch
    drawLine(
        color = Color(0xFF2563EB),
        start = Offset(humanX + 10f, humanY - 26f),
        end = Offset(panelX + 22f, panelY + 32f),
        strokeWidth = 8f,
        cap = StrokeCap.Round
    )

    // Power OFF text badge
    drawCircle(color = Color(0xFFEF4444), radius = 16f, center = Offset(panelX + 78f, panelY + 30f))
    drawLine(color = Color.White, start = Offset(panelX + 78f, panelY + 20f), end = Offset(panelX + 78f, panelY + 28f), strokeWidth = 3f, cap = StrokeCap.Round)
    drawCircle(color = Color.White, radius = 8f, center = Offset(panelX + 78f, panelY + 30f), style = Stroke(width = 2.5f))
}

// 3. Human Evacuating down stairs holding handrail
private fun DrawScope.drawHumanEvacuateStairs() {
    val w = size.width
    val h = size.height

    drawRect(Color(0xFF0F172A))

    // Staircase steps
    val stairSteps = 5
    for (s in 0 until stairSteps) {
        val sx = w * 0.2f + s * (w * 0.6f / stairSteps)
        val sy = h * 0.35f + s * (h * 0.5f / stairSteps)
        drawRect(
            color = Color(0xFF334155),
            topLeft = Offset(sx, sy),
            size = Size(w * 0.6f / stairSteps + 2f, h - sy)
        )
        // Step tread line
        drawLine(color = Color(0xFF94A3B8), start = Offset(sx, sy), end = Offset(sx + (w * 0.6f / stairSteps), sy), strokeWidth = 3f)
    }

    // Handrail
    drawLine(
        color = Color(0xFFE2E8F0),
        start = Offset(w * 0.15f, h * 0.22f),
        end = Offset(w * 0.85f, h * 0.72f),
        strokeWidth = 5f,
        cap = StrokeCap.Round
    )

    // HUMAN FIGURE (Walking down steps, holding handrail, backpack protecting back)
    val humanX = w * 0.52f
    val humanY = h * 0.44f
    val skinColor = Color(0xFFFFD1A4)

    // Head
    drawCircle(color = skinColor, radius = 13f, center = Offset(humanX, humanY - 44f))
    // Backpack (Red)
    drawRoundRect(
        color = Color(0xFFDC2626),
        topLeft = Offset(humanX - 22f, humanY - 36f),
        size = Size(14f, 28f),
        cornerRadius = CornerRadius(6f, 6f)
    )
    // Body (Green shirt)
    drawRoundRect(
        color = Color(0xFF10B981),
        topLeft = Offset(humanX - 10f, humanY - 32f),
        size = Size(22f, 44f),
        cornerRadius = CornerRadius(8f, 8f)
    )

    // Arm holding handrail
    drawLine(
        color = Color(0xFF10B981),
        start = Offset(humanX + 6f, humanY - 22f),
        end = Offset(humanX + 22f, humanY - 12f),
        strokeWidth = 7f,
        cap = StrokeCap.Round
    )

    // Walking legs on steps
    drawLine(color = Color(0xFF1E293B), start = Offset(humanX - 4f, humanY + 10f), end = Offset(humanX - 16f, humanY + 44f), strokeWidth = 8f, cap = StrokeCap.Round)
    drawLine(color = Color(0xFF1E293B), start = Offset(humanX + 6f, humanY + 10f), end = Offset(humanX + 18f, humanY + 54f), strokeWidth = 8f, cap = StrokeCap.Round)

    // Green Emergency Exit Sign
    drawRoundRect(color = Color(0xFF16A34A), topLeft = Offset(w * 0.78f, h * 0.12f), size = Size(52f, 26f), cornerRadius = CornerRadius(6f, 6f))
    drawCircle(color = Color.White, radius = 4f, center = Offset(w * 0.85f, h * 0.25f))
}

// 4. Human running uphill away from tsunami wave
private fun DrawScope.drawHumanRunUphillTsunami() {
    val w = size.width
    val h = size.height

    // Ocean & Sky
    drawRect(
        brush = Brush.verticalGradient(
            colors = listOf(Color(0xFF075985), Color(0xFF0C2A44))
        )
    )

    // Giant Tsunami Wave on Left
    val wave = Path().apply {
        moveTo(0f, h)
        lineTo(0f, h * 0.38f)
        cubicTo(w * 0.15f, h * 0.28f, w * 0.3f, h * 0.18f, w * 0.4f, h * 0.58f)
        lineTo(w * 0.4f, h)
        close()
    }
    drawPath(wave, brush = Brush.verticalGradient(listOf(Color(0xFF38BDF8), Color(0xFF0369A1))))

    // Green High Hill on Right
    val hill = Path().apply {
        moveTo(w * 0.38f, h)
        cubicTo(w * 0.52f, h * 0.65f, w * 0.7f, h * 0.35f, w, h * 0.22f)
        lineTo(w, h)
        close()
    }
    drawPath(hill, brush = Brush.verticalGradient(listOf(Color(0xFF22C55E), Color(0xFF15803D))))

    // Evacuation Signpost on hilltop
    drawLine(color = Color(0xFFE2E8F0), start = Offset(w * 0.88f, h * 0.26f), end = Offset(w * 0.88f, h * 0.42f), strokeWidth = 4f)
    drawRoundRect(color = Color(0xFF10B981), topLeft = Offset(w * 0.82f, h * 0.2f), size = Size(38f, 20f), cornerRadius = CornerRadius(4f, 4f))

    // HUMAN FIGURE (Running uphill to the right)
    val humanX = w * 0.62f
    val humanY = h * 0.52f
    val skinColor = Color(0xFFFFD1A4)

    // Head
    drawCircle(color = skinColor, radius = 13f, center = Offset(humanX + 6f, humanY - 44f))
    // Torso (leaning forward into sprint)
    drawLine(color = Color(0xFFEA580C), start = Offset(humanX, humanY - 32f), end = Offset(humanX + 12f, humanY + 2f), strokeWidth = 16f, cap = StrokeCap.Round)
    // Running arms
    drawLine(color = Color(0xFFEA580C), start = Offset(humanX + 4f, humanY - 24f), end = Offset(humanX + 24f, humanY - 28f), strokeWidth = 7f, cap = StrokeCap.Round)
    drawLine(color = Color(0xFFEA580C), start = Offset(humanX - 2f, humanY - 20f), end = Offset(humanX - 16f, humanY - 10f), strokeWidth = 7f, cap = StrokeCap.Round)
    // Running legs
    drawLine(color = Color(0xFF1E293B), start = Offset(humanX + 8f, humanY), end = Offset(humanX + 26f, humanY + 26f), strokeWidth = 8f, cap = StrokeCap.Round)
    drawLine(color = Color(0xFF1E293B), start = Offset(humanX + 8f, humanY), end = Offset(humanX - 14f, humanY + 22f), strokeWidth = 8f, cap = StrokeCap.Round)
}

// 5. Human Evacuate Vertical Roof
private fun DrawScope.drawHumanEvacuateVertical() {
    val w = size.width
    val h = size.height

    drawRect(Color(0xFF0F172A))
    // Flood water surging below
    drawRect(color = Color(0xFF0284C7), topLeft = Offset(0f, h * 0.78f), size = Size(w, h * 0.22f))

    // Concrete Building Structure
    val bldgX = w * 0.32f
    val bldgW = w * 0.38f
    drawRect(color = Color(0xFF334155), topLeft = Offset(bldgX, h * 0.32f), size = Size(bldgW, h * 0.5f))
    // Rooftop parapet
    drawRect(color = Color(0xFF64748B), topLeft = Offset(bldgX - 6f, h * 0.3f), size = Size(bldgW + 12f, 14f))

    // HUMAN FIGURE on roof waving bright orange emergency cloth
    val humanX = bldgX + bldgW * 0.5f
    val humanY = h * 0.24f
    val skinColor = Color(0xFFFFD1A4)

    drawCircle(color = skinColor, radius = 12f, center = Offset(humanX, humanY - 26f))
    drawRoundRect(color = Color(0xFF0284C7), topLeft = Offset(humanX - 9f, humanY - 14f), size = Size(18f, 32f), cornerRadius = CornerRadius(6f, 6f))
    drawLine(color = Color(0xFF1E293B), start = Offset(humanX - 5f, humanY + 18f), end = Offset(humanX - 5f, h * 0.3f), strokeWidth = 7f)
    drawLine(color = Color(0xFF1E293B), start = Offset(humanX + 5f, humanY + 18f), end = Offset(humanX + 5f, h * 0.3f), strokeWidth = 7f)

    // Arm raised waving orange flag/cloth
    drawLine(color = Color(0xFF0284C7), start = Offset(humanX + 8f, humanY - 8f), end = Offset(humanX + 22f, humanY - 32f), strokeWidth = 6f, cap = StrokeCap.Round)
    // Orange cloth waving
    drawRoundRect(color = Color(0xFFEA580C), topLeft = Offset(humanX + 20f, humanY - 44f), size = Size(26f, 18f), cornerRadius = CornerRadius(4f, 4f))
}

// 6. Human Crawling low on all fours beneath smoke with wet cloth
private fun DrawScope.drawHumanCrawlLowSmoke() {
    val w = size.width
    val h = size.height

    // Room Floor
    drawRect(Color(0xFF180A04))
    drawRect(color = Color(0xFF451A03), topLeft = Offset(0f, h * 0.82f), size = Size(w, h * 0.18f))

    // Billowing thick dark smoke layer occupying the top 55% of the room
    drawRect(
        brush = Brush.verticalGradient(
            colors = listOf(Color(0xFF0F0500), Color(0xFF3B1808).copy(alpha = 0.95f), Color.Transparent)
        ),
        topLeft = Offset(0f, 0f),
        size = Size(w, h * 0.65f)
    )
    // Flickering orange fire glow in top corner
    for (i in 0 until 4) {
        drawCircle(color = Color(0xFFEF4444).copy(alpha = 0.4f), radius = 24f + i * 8f, center = Offset(w * 0.88f, h * 0.2f))
    }

    // Clean Air Zone Line (arrow indicator at low height)
    drawLine(
        color = Color(0xFF38BDF8).copy(alpha = 0.6f),
        start = Offset(w * 0.1f, h * 0.62f),
        end = Offset(w * 0.9f, h * 0.62f),
        strokeWidth = 2f,
        pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(12f, 8f))
    )

    // HUMAN FIGURE (Crawling on hands and knees beneath the smoke)
    val humanX = w * 0.45f
    val humanY = h * 0.72f
    val skinColor = Color(0xFFFFD1A4)

    // Torso (horizontal, low)
    drawRoundRect(
        color = Color(0xFF0284C7),
        topLeft = Offset(humanX - 28f, humanY - 12f),
        size = Size(52f, 22f),
        cornerRadius = CornerRadius(8f, 8f)
    )

    // Head facing forward/low
    drawCircle(color = skinColor, radius = 12f, center = Offset(humanX + 32f, humanY - 4f))

    // Wet cloth/towel covering mouth and nose (White/Cyan)
    drawRoundRect(
        color = Color.White,
        topLeft = Offset(humanX + 32f, humanY - 6f),
        size = Size(14f, 10f),
        cornerRadius = CornerRadius(3f, 3f)
    )

    // Hand holding cloth to face
    drawLine(color = Color(0xFF0284C7), start = Offset(humanX + 16f, humanY), end = Offset(humanX + 30f, humanY - 2f), strokeWidth = 6f, cap = StrokeCap.Round)

    // Supporting arm on the floor
    drawLine(color = Color(0xFF0284C7), start = Offset(humanX + 6f, humanY + 6f), end = Offset(humanX + 10f, h * 0.82f), strokeWidth = 7f, cap = StrokeCap.Round)

    // Bent legs crawling on floor
    val crawlLeg = Path().apply {
        moveTo(humanX - 24f, humanY)
        lineTo(humanX - 42f, humanY + 10f)
        lineTo(humanX - 22f, h * 0.82f)
    }
    drawPath(crawlLeg, color = Color(0xFF1E293B), style = Stroke(width = 8f, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

// 7. Human operating fire extinguisher (APAR) with P-A-S-S technique
private fun DrawScope.drawHumanOperateExtinguisher() {
    val w = size.width
    val h = size.height

    drawRect(Color(0xFF0F172A))
    drawRect(color = Color(0xFF1E293B), topLeft = Offset(0f, h * 0.84f), size = Size(w, h * 0.16f))

    // Flame on Right
    val flameX = w * 0.82f
    val flamePath = Path().apply {
        moveTo(flameX - 24f, h * 0.84f)
        cubicTo(flameX - 12f, h * 0.6f, flameX + 16f, h * 0.5f, flameX, h * 0.38f)
        cubicTo(flameX + 16f, h * 0.5f, flameX + 22f, h * 0.65f, flameX + 24f, h * 0.84f)
        close()
    }
    drawPath(flamePath, brush = Brush.verticalGradient(listOf(Color(0xFFFDE047), Color(0xFFEA580C), Color(0xFFDC2626))))

    // HUMAN FIGURE (Athletic wide stance holding APAR)
    val humanX = w * 0.32f
    val humanY = h * 0.56f
    val skinColor = Color(0xFFFFD1A4)

    // Head
    drawCircle(color = skinColor, radius = 14f, center = Offset(humanX, humanY - 46f))
    // Torso
    drawRoundRect(color = Color(0xFF2563EB), topLeft = Offset(humanX - 12f, humanY - 32f), size = Size(24f, 48f), cornerRadius = CornerRadius(6f, 6f))
    // Legs in sturdy stance
    drawLine(color = Color(0xFF1E293B), start = Offset(humanX - 6f, humanY + 16f), end = Offset(humanX - 22f, h * 0.84f), strokeWidth = 9f, cap = StrokeCap.Round)
    drawLine(color = Color(0xFF1E293B), start = Offset(humanX + 6f, humanY + 16f), end = Offset(humanX + 18f, h * 0.84f), strokeWidth = 9f, cap = StrokeCap.Round)

    // Red APAR cylinder held in front
    val aparX = humanX + 16f
    val aparY = humanY - 8f
    drawRoundRect(color = Color(0xFFDC2626), topLeft = Offset(aparX, aparY), size = Size(18f, 44f), cornerRadius = CornerRadius(6f, 6f))

    // Arm holding handle
    drawLine(color = Color(0xFF2563EB), start = Offset(humanX + 4f, humanY - 20f), end = Offset(aparX + 6f, aparY - 4f), strokeWidth = 7f, cap = StrokeCap.Round)

    // Arm aiming hose toward the BASE of the flame
    drawLine(color = Color(0xFF2563EB), start = Offset(humanX + 8f, humanY - 14f), end = Offset(aparX + 26f, aparY + 12f), strokeWidth = 7f, cap = StrokeCap.Round)
    drawLine(color = Color(0xFF0F172A), start = Offset(aparX + 16f, aparY + 6f), end = Offset(flameX - 28f, h * 0.78f), strokeWidth = 4.5f, cap = StrokeCap.Round)

    // White extinguishing powder spray cone aimed at base
    val spray = Path().apply {
        moveTo(flameX - 28f, h * 0.78f)
        lineTo(flameX + 10f, h * 0.74f)
        lineTo(flameX + 10f, h * 0.84f)
        close()
    }
    drawPath(spray, color = Color.White.copy(alpha = 0.75f))
}

// 8. Human Testing Door with back of hand
private fun DrawScope.drawHumanTouchDoorTest() {
    val w = size.width
    val h = size.height

    drawRect(Color(0xFF0F172A))
    drawRect(color = Color(0xFF1E293B), topLeft = Offset(0f, h * 0.86f), size = Size(w, h * 0.14f))

    // Door frame & Door
    val doorX = w * 0.65f
    val doorY = h * 0.18f
    val doorW = 54f
    val doorH = h * 0.68f

    drawRect(color = Color(0xFF78350F), topLeft = Offset(doorX, doorY), size = Size(doorW, doorH))
    // Glowing heat behind door
    drawRect(
        brush = Brush.horizontalGradient(listOf(Color.Transparent, Color(0xFFEF4444).copy(alpha = 0.4f))),
        topLeft = Offset(doorX + doorW, doorY),
        size = Size(20f, doorH)
    )
    // Metal door handle
    val handleX = doorX + 10f
    val handleY = doorY + doorH * 0.5f
    drawCircle(color = Color(0xFFFBBF24), radius = 5f, center = Offset(handleX, handleY))

    // HUMAN FIGURE (Extending back of hand toward doorknob)
    val humanX = w * 0.4f
    val humanY = h * 0.54f
    val skinColor = Color(0xFFFFD1A4)

    drawCircle(color = skinColor, radius = 14f, center = Offset(humanX, humanY - 48f))
    drawRoundRect(color = Color(0xFF2563EB), topLeft = Offset(humanX - 12f, humanY - 34f), size = Size(24f, 50f), cornerRadius = CornerRadius(8f, 8f))
    drawLine(color = Color(0xFF1E293B), start = Offset(humanX - 6f, humanY + 16f), end = Offset(humanX - 8f, h * 0.86f), strokeWidth = 9f, cap = StrokeCap.Round)
    drawLine(color = Color(0xFF1E293B), start = Offset(humanX + 6f, humanY + 16f), end = Offset(humanX + 8f, h * 0.86f), strokeWidth = 9f, cap = StrokeCap.Round)

    // Arm extending back of hand
    drawLine(color = Color(0xFF2563EB), start = Offset(humanX + 8f, humanY - 22f), end = Offset(handleX - 10f, handleY - 4f), strokeWidth = 7f, cap = StrokeCap.Round)
    drawCircle(color = skinColor, radius = 6f, center = Offset(handleX - 8f, handleY - 4f))
}

// 9. Human Wearing N95 Mask and Sealed Goggles
private fun DrawScope.drawHumanWearN95Goggles() {
    val w = size.width
    val h = size.height

    // Volcanic ash sky backdrop
    drawRect(
        brush = Brush.verticalGradient(
            colors = listOf(Color(0xFF2E1065), Color(0xFF180829))
        )
    )
    // Falling ash flakes
    for (i in 0 until 24) {
        val ax = (i * 37 % 100) / 100f * w
        val ay = (i * 29 % 100) / 100f * h
        drawCircle(color = Color(0xFFCBD5E1).copy(alpha = 0.6f), radius = 2.5f, center = Offset(ax, ay))
    }

    // CLOSE-UP HUMAN PORTRAIT WITH N95 MASK & GOGGLES
    val cx = w * 0.5f
    val cy = h * 0.48f
    val skinColor = Color(0xFFFFD1A4)

    // Head
    drawCircle(color = skinColor, radius = 48f, center = Offset(cx, cy))

    // Protective Safety Goggles with clear blue lenses
    drawRoundRect(
        color = Color(0xFF38BDF8).copy(alpha = 0.85f),
        topLeft = Offset(cx - 38f, cy - 22f),
        size = Size(76f, 22f),
        cornerRadius = CornerRadius(10f, 10f)
    )
    drawRoundRect(
        color = Color.White,
        topLeft = Offset(cx - 38f, cy - 22f),
        size = Size(76f, 22f),
        cornerRadius = CornerRadius(10f, 10f),
        style = Stroke(width = 3f)
    )

    // N95 Respirator Mask covering nose and mouth (Cone shape)
    val mask = Path().apply {
        moveTo(cx - 32f, cy + 2f)
        cubicTo(cx, cy - 8f, cx + 16f, cy - 8f, cx + 32f, cy + 2f)
        lineTo(cx + 26f, cy + 42f)
        cubicTo(cx, cy + 54f, cx - 12f, cy + 50f, cx - 26f, cy + 42f)
        close()
    }
    drawPath(mask, color = Color.White)
    drawPath(mask, color = Color(0xFF0284C7), style = Stroke(width = 2.5f))

    // Mask Elastic Straps around head
    drawLine(color = Color(0xFF0284C7), start = Offset(cx - 30f, cy + 8f), end = Offset(cx - 48f, cy), strokeWidth = 2.5f)
    drawLine(color = Color(0xFF0284C7), start = Offset(cx + 30f, cy + 8f), end = Offset(cx + 48f, cy), strokeWidth = 2.5f)

    // Hands adjusting mask strap for airtight seal
    drawCircle(color = skinColor, radius = 9f, center = Offset(cx - 36f, cy + 8f))
    drawCircle(color = skinColor, radius = 9f, center = Offset(cx + 36f, cy + 8f))
}

// 10. Human Running Lateral to Slope (Landslide)
private fun DrawScope.drawHumanRunLateralSlope() {
    val w = size.width
    val h = size.height

    drawRect(Color(0xFF1C1917))

    // Tumbling mud/rock slide path cascading down
    val slide = Path().apply {
        moveTo(w * 0.15f, 0f)
        lineTo(w * 0.42f, 0f)
        lineTo(w * 0.48f, h)
        lineTo(w * 0.2f, h)
        close()
    }
    drawPath(slide, brush = Brush.verticalGradient(listOf(Color(0xFF78350F), Color(0xFF451A03))))

    // Falling tumbling rocks
    for (i in 0 until 6) {
        val rx = w * 0.25f + (i % 2) * 20f
        val ry = h * 0.2f + i * 24f
        drawCircle(color = Color(0xFF57534E), radius = 8f, center = Offset(rx, ry))
    }

    // Green Lateral Arrow (90 degrees away from slide)
    val arrowStart = Offset(w * 0.42f, h * 0.5f)
    val arrowEnd = Offset(w * 0.78f, h * 0.5f)
    drawLine(color = Color(0xFF22C55E), start = arrowStart, end = arrowEnd, strokeWidth = 6f, cap = StrokeCap.Round)

    // HUMAN FIGURE sprinting laterally to safety
    val humanX = w * 0.72f
    val humanY = h * 0.54f
    val skinColor = Color(0xFFFFD1A4)

    drawCircle(color = skinColor, radius = 13f, center = Offset(humanX + 4f, humanY - 42f))
    drawLine(color = Color(0xFF10B981), start = Offset(humanX, humanY - 30f), end = Offset(humanX + 10f, humanY + 2f), strokeWidth = 16f, cap = StrokeCap.Round)
    drawLine(color = Color(0xFF1E293B), start = Offset(humanX + 6f, humanY), end = Offset(humanX + 24f, humanY + 26f), strokeWidth = 8f, cap = StrokeCap.Round)
    drawLine(color = Color(0xFF1E293B), start = Offset(humanX + 6f, humanY), end = Offset(humanX - 16f, humanY + 24f), strokeWidth = 8f, cap = StrokeCap.Round)
}

// 11. Human Shelter Interior Room (Tornado)
private fun DrawScope.drawHumanShelterInteriorRoom() {
    val w = size.width
    val h = size.height

    drawRect(Color(0xFF0F172A))
    // Outer turbulent wind funnel outside
    for (i in 0 until 5) {
        val rx = w * 0.88f + (i % 2) * 12f
        val ry = h * 0.2f + i * 28f
        drawOval(color = Color(0xFF38BDF8).copy(alpha = 0.4f), topLeft = Offset(rx - 20f, ry), size = Size(40f, 12f), style = Stroke(width = 2f))
    }

    // Concrete Safe Room Bunker
    val roomX = w * 0.22f
    val roomY = h * 0.28f
    drawRoundRect(color = Color(0xFF334155), topLeft = Offset(roomX, roomY), size = Size(w * 0.54f, h * 0.58f), cornerRadius = CornerRadius(12f, 12f))

    // HUMAN crouched in corner covering head with thick mattress
    val humanX = roomX + 38f
    val humanY = roomY + h * 0.42f
    val skinColor = Color(0xFFFFD1A4)

    drawCircle(color = skinColor, radius = 12f, center = Offset(humanX, humanY))
    drawRoundRect(color = Color(0xFF2563EB), topLeft = Offset(humanX - 12f, humanY + 12f), size = Size(24f, 24f), cornerRadius = CornerRadius(6f, 6f))

    // Thick Protective Mattress over head (Golden Amber)
    drawRoundRect(color = Color(0xFFFBBF24), topLeft = Offset(humanX - 22f, humanY - 24f), size = Size(46f, 16f), cornerRadius = CornerRadius(6f, 6f))
    drawRoundRect(color = Color(0xFFD97706), topLeft = Offset(humanX - 22f, humanY - 24f), size = Size(46f, 16f), cornerRadius = CornerRadius(6f, 6f), style = Stroke(width = 2f))
}

// 12. Human Rainwater Harvesting
private fun DrawScope.drawHumanRainwaterHarvest() {
    val w = size.width
    val h = size.height

    drawRect(Color(0xFF0F172A))

    // Rain Barrel Cistern
    val barrelX = w * 0.62f
    val barrelY = h * 0.44f
    drawRoundRect(color = Color(0xFF0284C7), topLeft = Offset(barrelX, barrelY), size = Size(52f, 74f), cornerRadius = CornerRadius(10f, 10f))
    // Water level inside
    drawRoundRect(color = Color(0xFF38BDF8), topLeft = Offset(barrelX + 4f, barrelY + 24f), size = Size(44f, 46f), cornerRadius = CornerRadius(6f, 6f))

    // Gutter spout from roof
    drawLine(color = Color(0xFF94A3B8), start = Offset(barrelX + 26f, 0f), end = Offset(barrelX + 26f, barrelY - 6f), strokeWidth = 8f)

    // HUMAN inspecting clear water
    val humanX = w * 0.38f
    val humanY = h * 0.54f
    val skinColor = Color(0xFFFFD1A4)

    drawCircle(color = skinColor, radius = 13f, center = Offset(humanX, humanY - 44f))
    drawRoundRect(color = Color(0xFF10B981), topLeft = Offset(humanX - 10f, humanY - 30f), size = Size(20f, 44f), cornerRadius = CornerRadius(6f, 6f))
    drawLine(color = Color(0xFF1E293B), start = Offset(humanX - 4f, humanY + 14f), end = Offset(humanX - 4f, h * 0.86f), strokeWidth = 8f)
    drawLine(color = Color(0xFF1E293B), start = Offset(humanX + 4f, humanY + 14f), end = Offset(humanX + 4f, h * 0.86f), strokeWidth = 8f)

    // Arm holding water pitcher
    drawLine(color = Color(0xFF10B981), start = Offset(humanX + 6f, humanY - 18f), end = Offset(barrelX - 8f, barrelY + 12f), strokeWidth = 6f, cap = StrokeCap.Round)
}

// 13. Human Planting Mangrove Greenbelt
private fun DrawScope.drawHumanPlantMangrove() {
    val w = size.width
    val h = size.height

    drawRect(Color(0xFF042F2E))
    // Mudflat shoreline
    drawRect(color = Color(0xFF134E4A), topLeft = Offset(0f, h * 0.72f), size = Size(w, h * 0.28f))

    // Mangrove Sapling with Stilt roots
    val treeX = w * 0.65f
    val treeY = h * 0.54f
    // Canopy
    drawCircle(color = Color(0xFF22C55E), radius = 22f, center = Offset(treeX, treeY - 26f))
    // Trunk & roots
    drawLine(color = Color(0xFF78350F), start = Offset(treeX, treeY - 8f), end = Offset(treeX, h * 0.72f), strokeWidth = 6f)
    drawLine(color = Color(0xFF78350F), start = Offset(treeX, h * 0.66f), end = Offset(treeX - 14f, h * 0.78f), strokeWidth = 4f)
    drawLine(color = Color(0xFF78350F), start = Offset(treeX, h * 0.66f), end = Offset(treeX + 14f, h * 0.78f), strokeWidth = 4f)

    // HUMAN crouching and planting seedling
    val humanX = w * 0.42f
    val humanY = h * 0.64f
    val skinColor = Color(0xFFFFD1A4)

    drawCircle(color = skinColor, radius = 12f, center = Offset(humanX, humanY - 24f))
    drawRoundRect(color = Color(0xFF0D9488), topLeft = Offset(humanX - 12f, humanY - 10f), size = Size(24f, 32f), cornerRadius = CornerRadius(6f, 6f))
    // Arm placing soil around roots
    drawLine(color = Color(0xFF0D9488), start = Offset(humanX + 6f, humanY), end = Offset(treeX - 8f, h * 0.72f), strokeWidth = 6f, cap = StrokeCap.Round)
    // Kneeling leg
    drawLine(color = Color(0xFF1E293B), start = Offset(humanX - 8f, humanY + 18f), end = Offset(humanX - 16f, h * 0.78f), strokeWidth = 7f)
}

// 14. Human Avoiding Crowd Peace
private fun DrawScope.drawHumanAvoidCrowdPeace() {
    val w = size.width
    val h = size.height

    drawRect(Color(0xFF0F172A))

    // Unruly crowd silhouette in distant left with red beacons
    drawCircle(color = Color(0xFFDC2626).copy(alpha = 0.5f), radius = 40f, center = Offset(w * 0.18f, h * 0.38f))
    for (i in 0 until 5) {
        val cx = w * 0.1f + i * 14f
        drawCircle(color = Color(0xFF334155), radius = 8f, center = Offset(cx, h * 0.52f))
    }

    // Peaceful Safe Zone on Right (Blue sanctuary barrier)
    drawRoundRect(color = Color(0xFF1D4ED8).copy(alpha = 0.3f), topLeft = Offset(w * 0.7f, h * 0.25f), size = Size(w * 0.28f, h * 0.65f), cornerRadius = CornerRadius(14f, 14f))
    drawCircle(color = Color(0xFF3B82F6), radius = 18f, center = Offset(w * 0.84f, h * 0.38f))

    // HUMAN walking calmly to the right towards safety
    val humanX = w * 0.52f
    val humanY = h * 0.54f
    val skinColor = Color(0xFFFFD1A4)

    drawCircle(color = skinColor, radius = 13f, center = Offset(humanX, humanY - 44f))
    drawRoundRect(color = Color(0xFF10B981), topLeft = Offset(humanX - 10f, humanY - 30f), size = Size(20f, 44f), cornerRadius = CornerRadius(6f, 6f))
    drawLine(color = Color(0xFF1E293B), start = Offset(humanX - 4f, humanY + 14f), end = Offset(humanX - 8f, h * 0.84f), strokeWidth = 8f)
    drawLine(color = Color(0xFF1E293B), start = Offset(humanX + 4f, humanY + 14f), end = Offset(humanX + 10f, h * 0.84f), strokeWidth = 8f)
}
