package audio;

public class AudioManager {
    private static AudioManager instance;
    private boolean muted = false;

    private AudioManager() {}

    public static synchronized AudioManager getInstance() {
        if (instance == null) {
            instance = new AudioManager();
        }
        return instance;
    }

    public void playSound(String soundName) {
        if (!muted) {
            System.out.println("[AUDIO] Playing: " + soundName);
        }
    }

    public void playMusic(String musicName) {
        if (!muted) {
            System.out.println("[AUDIO] Music: " + musicName);
        }
    }

    public void setMuted(boolean muted) {
        this.muted = muted;
    }

    public boolean isMuted() {
        return muted;
    }
}
