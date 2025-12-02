package Sound;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundPlayer {

    // -------------------------------------------------------------------------
    // VOLUME GLOBAL
    // -------------------------------------------------------------------------
    private static float masterVolume = 0.8f; // 0.0 → 1.0
    private static Clip musicClip;
    private static boolean bHardcore = false;

    private static void applyVolume(Clip clip) {
        if (clip == null) return;

        try {
            FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

            float dB = (float) (20 * Math.log10(masterVolume <= 0 ? 0.0001f : masterVolume));
            volume.setValue(dB);
        } catch (Exception e) {
            System.err.println("Volume control not supported: " + e.getMessage());
        }
    }

    // Appelé par UI (+)
    public static void increaseVolume() {
        masterVolume = Math.min(1.0f, masterVolume + 0.1f);
        applyVolume(musicClip);
        //System.out.println("Volume: " + (int)(masterVolume * 100) + "%");
    }

    // Appelé par UI (–)
    public static void decreaseVolume() {
        masterVolume = Math.max(0.0f, masterVolume - 0.1f);
        applyVolume(musicClip);
        //System.out.println("Volume: " + (int)(masterVolume * 100) + "%");
    }

    // -------------------------------------------------------------------------
    // BANQUE DE SFX (NE PAS MODIFIER)
    // -------------------------------------------------------------------------
    public static class MyClip implements Cloneable
    {
        private Clip copy;

        public MyClip() {}
        public Clip get() { return this.copy; }
        public void set(Clip c) { this.copy = c; }

        @Override
        protected Object clone() throws CloneNotSupportedException
        {
            MyClip clone = new MyClip();
            clone.set(this.copy);
            return clone;
        }
    }

    private static final Map<String, MyClip> sfxBank = new HashMap<>();

    public static List<String> listSoundFiles()
    {
        List<String> files = new ArrayList<>();

        try {
            var dirURL = SoundPlayer.class.getResource("/assets/sounds/");
            if (dirURL == null) return files;

            if (dirURL.getProtocol().equals("jar"))
            {
                String path = dirURL.getPath();
                String jarPath = path.substring(5, path.indexOf("!"));

                try (JarFile jar = new JarFile(URLDecoder.decode(jarPath, "UTF-8")))
                {
                    Enumeration<JarEntry> entries = jar.entries();
                    while (entries.hasMoreElements()) {
                        JarEntry entry = entries.nextElement();
                        String name = entry.getName();
                        if (name.startsWith("assets/sounds/") && !entry.isDirectory())
                            files.add(name.substring("assets/sounds/".length()));
                    }
                }
            }
            else
            {
                File folder = new File(dirURL.toURI());
                File[] list = folder.listFiles();
                if (list != null)
                {
                    for (File file : list)
                        if (file.isFile()) files.add(file.getName());
                }
            }
        }
        catch (IOException | URISyntaxException e)
        {
            System.err.println(e.getMessage());
        }

        return files;
    }

    public static void initialize() {
        for (String fileName : listSoundFiles())
        {
            try (var stream = SoundPlayer.class.getResourceAsStream("/assets/sounds/" + fileName))
            {
                if (stream == null) continue;

                try (AudioInputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(stream)))
                {
                    Clip clip = AudioSystem.getClip();
                    clip.open(ais);
                    applyVolume(clip);
                    MyClip c = new MyClip();
                    c.set(clip);
                    sfxBank.put(fileName, c);
                }
            }
            catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }

    public static void playSFX(String fileName)
    {
        if (sfxBank.get(fileName) == null) return;
        try
        {
            Clip clip = ((MyClip)(sfxBank.get(fileName).clone())).get();
            if (clip == null) return;

            if (clip.isRunning())
                clip.stop();

            clip.setFramePosition(0);

            applyVolume(clip); // volume appliqué aux sons joués dynamiquement

            clip.start();
        }
        catch (CloneNotSupportedException e) {
            System.err.println(e.getMessage());
        }
    }

    // -------------------------------------------------------------------------
    // MUSIQUE (NE PAS MODIFIER)
    // -------------------------------------------------------------------------
    public static void playMusicLoop(String fileName)
    {
        if (bHardcore) return;
        try
        {
            if (musicClip != null && musicClip.isRunning())
                musicClip.stop();

            musicClip = AudioSystem.getClip();
            var audioSrc = SoundPlayer.class.getResourceAsStream("/assets/sounds/" + fileName);

            if (audioSrc == null) {
                System.err.println("Music not found: " + fileName);
                return;
            }

            AudioInputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(audioSrc));
            musicClip.open(ais);

            applyVolume(musicClip); // applique volume global

            musicClip.loop(Clip.LOOP_CONTINUOUSLY);
            musicClip.start();
        }
        catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void stopMusic() {
        if (bHardcore) return;
        if (musicClip != null)
            musicClip.stop();
    }

    public static void stopTech() {
        if (!bHardcore) return;
        if (musicClip != null) musicClip.stop();
        bHardcore = false;
    }

    public static void techMode()
    {
        if (bHardcore) return;

        playMusicLoop("ModeTech.wav");
        bHardcore = true;
    }
}
