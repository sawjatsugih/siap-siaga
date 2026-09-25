package com.example.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import com.example.model.DisasterType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.Random
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.exp
import kotlin.math.sin

class DisasterSoundEngine(private val context: Context) {

    private val sampleRate = 44100
    private var activeTrack: AudioTrack? = null
    private var playbackJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Default)
    private val random = Random()

    var isMuted: Boolean = false
    var isMaxLoudness: Boolean = false
    var isHapticsEnabled: Boolean = true

    /**
     * Preview sample sound for testing in Settings
     */
    fun playSampleSound() {
        if (isMuted) return
        startDisasterAudio(DisasterType.GEMPA_BUMI)
        scope.launch {
            kotlinx.coroutines.delay(1600)
            stopAudio()
        }
    }

    private val vibrator: Vibrator? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
            vibratorManager?.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        }
    }

    /**
     * Short mechanical tick sound for the roulette spin
     */
    fun playTick() {
        if (isMuted) return
        scope.launch {
            try {
                val tickSamples = (sampleRate * 0.025).toInt()
                val buffer = ShortArray(tickSamples)
                for (i in 0 until tickSamples) {
                    val progress = i.toDouble() / tickSamples
                    val freq = 1600.0 * (1.0 - progress * 0.7)
                    val envelope = exp(-progress * 15.0)
                    val sample = (sin(2.0 * PI * freq * i / sampleRate) * 26000.0 * envelope).toInt()
                    buffer[i] = sample.toShort()
                }

                val track = AudioTrack.Builder()
                    .setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .build()
                    )
                    .setAudioFormat(
                        AudioFormat.Builder()
                            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                            .setSampleRate(sampleRate)
                            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                            .build()
                    )
                    .setBufferSizeInBytes(tickSamples * 2)
                    .setTransferMode(AudioTrack.MODE_STATIC)
                    .build()

                track.write(buffer, 0, tickSamples)
                track.play()
                track.setNotificationMarkerPosition(tickSamples)
                track.setPlaybackPositionUpdateListener(object : AudioTrack.OnPlaybackPositionUpdateListener {
                    override fun onMarkerReached(t: AudioTrack?) {
                        try {
                            t?.stop()
                            t?.release()
                        } catch (_: Exception) {}
                    }
                    override fun onPeriodicNotification(t: AudioTrack?) {}
                })
            } catch (_: Exception) {}
        }
    }

    /**
     * Start playing the intense, high-impact disaster sound and siren simulation
     */
    fun startDisasterAudio(disaster: DisasterType) {
        stopAudio()
        if (isMuted) return

        if (isHapticsEnabled) {
            triggerDisasterHaptics(disaster)
        }

        val bufferSize = AudioTrack.getMinBufferSize(
            sampleRate,
            AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        ).coerceAtLeast(sampleRate / 4)

        try {
            val track = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(sampleRate)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build()
                )
                .setBufferSizeInBytes(bufferSize * 2)
                .setTransferMode(AudioTrack.MODE_STREAM)
                .build()

            activeTrack = track
            track.play()

            val volumeFactor = if (isMaxLoudness) 1.0 else 0.65

            playbackJob = scope.launch {
                val chunkSize = 2048
                val pcmChunk = ShortArray(chunkSize)
                var globalSampleIndex = 0L

                var brownNoise = 0.0
                var pinkB0 = 0.0
                var pinkB1 = 0.0
                var pinkB2 = 0.0

                while (isActive) {
                    for (i in 0 until chunkSize) {
                        val t = (globalSampleIndex + i).toDouble() / sampleRate
                        val white = random.nextDouble() * 2.0 - 1.0
                        pinkB0 = 0.99886 * pinkB0 + white * 0.0555179
                        pinkB1 = 0.99332 * pinkB1 + white * 0.0750759
                        pinkB2 = 0.96900 * pinkB2 + white * 0.1538520
                        val pinkNoise = pinkB0 + pinkB1 + pinkB2 + white * 0.5362
                        brownNoise = (brownNoise + (0.05 * white)) / 1.05

                        val rawSample: Double = when (disaster) {
                            DisasterType.GEMPA_BUMI -> {
                                val bassFreq1 = 42.0 + 8.0 * sin(2.0 * PI * 0.4 * t)
                                val bassFreq2 = 68.0 + 12.0 * cos(2.0 * PI * 0.7 * t)
                                val subBass = sin(2.0 * PI * bassFreq1 * t) * 0.45 + sin(2.0 * PI * bassFreq2 * t) * 0.35
                                val rumble = brownNoise * 0.45
                                val snapProb = if (random.nextInt(1200) < 3) (random.nextDouble() * 2.0 - 1.0) * 0.6 else 0.0
                                val sirenFreq = 850.0 + 350.0 * sin(2.0 * PI * 1.5 * t)
                                val sirenWave = sin(2.0 * PI * sirenFreq * t) * 0.35
                                (subBass + rumble + snapProb + sirenWave)
                            }

                            DisasterType.BANJIR -> {
                                val waterNoise = pinkNoise * 0.16
                                val surgeModulation = 0.6 + 0.4 * abs(sin(2.0 * PI * 0.6 * t) * sin(2.0 * PI * 0.3 * t))
                                val rushingWater = waterNoise * surgeModulation * 1.2
                                val lowChurn = sin(2.0 * PI * 55.0 * t) * 0.25
                                val hornFreq = if ((t % 1.2) < 0.6) 660.0 else 990.0
                                val sirenHorn = sin(2.0 * PI * hornFreq * t) * 0.4
                                (rushingWater + lowChurn + sirenHorn)
                            }

                            DisasterType.TANAH_LONGSOR -> {
                                val groundRumble = brownNoise * 0.55
                                val impactCycle = t % 0.8
                                val impact = if (impactCycle < 0.08) {
                                    val progress = impactCycle / 0.08
                                    sin(2.0 * PI * 65.0 * progress) * exp(-progress * 10.0) * 0.7
                                } else 0.0
                                val gravel = (random.nextDouble() - 0.5) * 0.25
                                val klaxonSweep = 700.0 + 400.0 * ((t * 2.0) % 1.0)
                                val klaxon = sin(2.0 * PI * klaxonSweep * t) * 0.35
                                (groundRumble + impact + gravel + klaxon)
                            }

                            DisasterType.KEBAKARAN -> {
                                val flameRoar = pinkNoise * 0.22
                                val crackle = if (random.nextInt(800) < 14) (random.nextDouble() * 2.0 - 1.0) * 0.7 else 0.0
                                val alarmCycle = t % 0.4
                                val alarmTone = if (alarmCycle < 0.25) {
                                    sin(2.0 * PI * 1250.0 * t) * 0.45 + sin(2.0 * PI * 2500.0 * t) * 0.15
                                } else 0.0
                                (flameRoar + crackle + alarmTone)
                            }

                            DisasterType.TSUNAMI -> {
                                val tidalBass = sin(2.0 * PI * 35.0 * t) * 0.5 + sin(2.0 * PI * 48.0 * t) * 0.35
                                val waveCrest = pinkNoise * (0.3 + 0.4 * abs(sin(2.0 * PI * 0.25 * t)))
                                val tsunamiSiren = sin(2.0 * PI * (600.0 + 500.0 * sin(2.0 * PI * 0.8 * t)) * t) * 0.4
                                (tidalBass + waveCrest + tsunamiSiren)
                            }

                            DisasterType.KEKERINGAN -> {
                                val windGust = pinkNoise * (0.15 + 0.1 * sin(2.0 * PI * 0.2 * t))
                                val dryCricket = if (random.nextInt(300) < 5) sin(2.0 * PI * 4200.0 * t) * 0.2 else 0.0
                                val heatDrone = sin(2.0 * PI * 180.0 * t) * 0.25
                                (windGust + dryCricket + heatDrone)
                            }

                            DisasterType.ANGIN_TOPAN -> {
                                val vortexSpeed = 1.2 + 0.8 * sin(2.0 * PI * 0.5 * t)
                                val windHowlFreq = 400.0 + 600.0 * abs(sin(2.0 * PI * vortexSpeed * t))
                                val howl = sin(2.0 * PI * windHowlFreq * t) * 0.45
                                val buffeting = brownNoise * 0.45
                                val debrisRattle = if (random.nextInt(400) < 10) (random.nextDouble() - 0.5) * 0.5 else 0.0
                                (howl + buffeting + debrisRattle)
                            }

                            DisasterType.GUNUNG_BERAPI -> {
                                val magmaRumble = sin(2.0 * PI * 38.0 * t) * 0.55 + brownNoise * 0.4
                                val eruptionBlast = if ((t % 1.5) < 0.15) {
                                    val prog = (t % 1.5) / 0.15
                                    sin(2.0 * PI * 75.0 * prog) * exp(-prog * 8.0) * 0.85
                                } else 0.0
                                val gasHiss = pinkNoise * 0.25
                                (magmaRumble + eruptionBlast + gasHiss)
                            }

                            DisasterType.ABRASI -> {
                                val breakerCycle = t % 2.0
                                val breaker = if (breakerCycle < 0.5) {
                                    pinkNoise * 0.4 * exp(-breakerCycle * 3.0)
                                } else 0.05 * pinkNoise
                                val tideSwell = sin(2.0 * PI * 45.0 * t) * 0.35
                                val impact = if ((t % 2.0) < 0.08) sin(2.0 * PI * 80.0 * (t % 2.0)) * 0.5 else 0.0
                                (breaker + tideSwell + impact)
                            }

                            DisasterType.PERUBAHAN_IKLIM -> {
                                val pulseFreq = 520.0 + 180.0 * sin(2.0 * PI * 0.4 * t)
                                val climateAlert = sin(2.0 * PI * pulseFreq * t) * 0.35
                                val windShift = pinkNoise * 0.2
                                val subHarmonic = sin(2.0 * PI * 70.0 * t) * 0.25
                                (climateAlert + windShift + subHarmonic)
                            }

                            DisasterType.KONFLIK_SOSIAL -> {
                                val dualTone = if ((t % 0.8) < 0.4) 650.0 else 850.0
                                val policeSiren = sin(2.0 * PI * dualTone * t) * 0.45
                                val bullhorn = if (random.nextInt(600) < 8) sin(2.0 * PI * 1800.0 * t) * 0.3 else 0.0
                                val crowdMurmur = brownNoise * 0.35
                                (policeSiren + bullhorn + crowdMurmur)
                            }
                        }

                        val finalValue = (rawSample * 32000.0 * volumeFactor).toInt().coerceIn(-32767, 32767)
                        pcmChunk[i] = finalValue.toShort()
                    }

                    globalSampleIndex += chunkSize
                    try {
                        track.write(pcmChunk, 0, chunkSize)
                    } catch (_: Exception) {
                        break
                    }
                }
            }
        } catch (_: Exception) {}
    }

    /**
     * Stop all currently running audio and haptic effects
     */
    fun stopAudio() {
        playbackJob?.cancel()
        playbackJob = null
        try {
            activeTrack?.stop()
            activeTrack?.release()
        } catch (_: Exception) {}
        activeTrack = null
        vibrator?.cancel()
    }

    private fun triggerDisasterHaptics(disaster: DisasterType) {
        val vib = vibrator ?: return
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val (timings, amplitudes) = when (disaster) {
                    DisasterType.GEMPA_BUMI -> {
                        longArrayOf(0, 150, 40, 200, 30, 250, 50, 180) to intArrayOf(0, 255, 120, 255, 100, 255, 150, 255)
                    }
                    DisasterType.BANJIR, DisasterType.TSUNAMI, DisasterType.ABRASI -> {
                        longArrayOf(0, 100, 80, 220, 80, 320, 100) to intArrayOf(0, 140, 0, 200, 0, 255, 0)
                    }
                    DisasterType.TANAH_LONGSOR, DisasterType.GUNUNG_BERAPI -> {
                        longArrayOf(0, 260, 80, 120, 60, 350, 100) to intArrayOf(0, 255, 0, 200, 0, 255, 0)
                    }
                    DisasterType.KEBAKARAN, DisasterType.ANGIN_TOPAN, DisasterType.KONFLIK_SOSIAL -> {
                        longArrayOf(0, 80, 70, 90, 70, 80, 70, 100) to intArrayOf(0, 255, 0, 255, 0, 255, 0, 255)
                    }
                    DisasterType.KEKERINGAN, DisasterType.PERUBAHAN_IKLIM -> {
                        longArrayOf(0, 140, 100, 140, 100) to intArrayOf(0, 150, 0, 150, 0)
                    }
                }
                vib.vibrate(VibrationEffect.createWaveform(timings, amplitudes, 0))
            } else {
                @Suppress("DEPRECATION")
                vib.vibrate(longArrayOf(0, 200, 100, 200), 0)
            }
        } catch (_: Exception) {}
    }
}
