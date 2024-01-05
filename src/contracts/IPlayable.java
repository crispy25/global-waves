package contracts;

public interface IPlayable {
    /**
     * Start playing the audio entity
     * @param timestamp current time
     */
    void startAudio(int timestamp);

    /**
     * Pause the playing the audio entity
     * @param timestamp current time
     */
    void pauseAudio(int timestamp);

    /**
     *  Resume playing the audio entity
     * @param timestamp current time
     */
    void resumeAudio(int timestamp);

    /**
     *  Update the remaining time of the audio entity
     * @param timestamp current time
     */
    void updateRemainedTime(int timestamp);

    /**
     * Switch between the repeat modes of the audio entity
     * @param timestamp current time
     */
    void setRepeatStateAtTimestamp(int repeatState, int timestamp);

    /**
     * Get the remaining time of the audio entity
     * @return remaining time
     */
    int getRemainedTime();

    /**
     * Check if the audio entity is paused
     * @return true if it's paused, false otherwise
     */
    boolean isPaused();
}
