/**
 *   Stigma - Multiplayer online RPG - http://stigma.sourceforge.net
 *   Copyright (C) 2005-2009 Minions Studio
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *   
 */
package pl.org.minions.utils.ui.sprite.animation;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

/**
 * Class to be used to animate <code>double</code> values
 * using key framing.
 * <p>
 * Each {@link AnimatedValue} is associated with a
 * {@link Timeline}.
 */
public class AnimatedValue
{

    private final Timeline timeline;

    private double value;

    private LinkedList<KeyFrame> pastFrames;
    private KeyFrame currentFrame;
    private KeyFrame nextFrame;
    private LinkedList<KeyFrame> futureFrames = new LinkedList<KeyFrame>();

    /**
     * Creates a new animated value with given initial value
     * and adds it to a timeline.
     * <p>
     * The storing of past key frames is off by default.
     * @param timeline
     *            timeline to add this value to
     * @param initialValue
     *            initial value
     */
    public AnimatedValue(Timeline timeline, double initialValue)
    {
        this(timeline, initialValue, false);
    }

    /**
     * Creates a new animated value with given initial value
     * and adds it to a timeline.
     * @param timeline
     *            timeline to add this value to
     * @param initialValue
     *            initial value
     * @param storePastFrames
     *            should the past frames
     */
    AnimatedValue(Timeline timeline,
                  double initialValue,
                  boolean storePastFrames)
    {
        this.timeline = timeline;
        this.value = initialValue;
        this.pastFrames = storePastFrames ? new LinkedList<KeyFrame>() : null;
        timeline.addAnimatedValue(this);
    }

    /**
     * Returns the timeline associated with this value.
     * @return the associated timeline
     */
    public Timeline getTimeline()
    {
        return this.timeline;
    }

    /**
     * Returns value.
     * @return value
     */
    public final double getValue()
    {
        return value;
    }

    /**
     * Returns current time of the {@link Timeline} this
     * animated value is associated with.
     * @return current timeline time
     * @see pl.org.minions.utils.ui.sprite.animation.Timeline#getCurrentTime()
     */
    public long getCurrentTime()
    {
        return timeline.getCurrentTime();
    }

    /**
     * Checks if past key frames are stored by this animated
     * value.
     * @return <code>true</code> if this value keeps track
     *         of key frames before last frame,
     *         <code>false</code> if any past frames are
     *         discarded
     * @see #setStoringPastFramesEnabled(boolean)
     */
    public synchronized boolean isStoringPastFramesEnabled()
    {
        return pastFrames != null;
    }

    /**
     * Sets if this key value should store past key frames.
     * @param store
     *            if <code>true</code> this value will keep
     *            track of key frames earlier than last
     *            frame, if <code>false</code> any past
     *            frames will be discarded
     * @see AnimatedValue#isStoringPastFramesEnabled()
     */
    public synchronized void setStoringPastFramesEnabled(boolean store)
    {
        if (store)
        {
            if (pastFrames == null)
                pastFrames = new LinkedList<KeyFrame>();
        }
        else if (pastFrames != null)
            pastFrames = null;
    }

    /**
     * Sets a key frame in the animation.
     * <p>
     * If this animated value does not store past values and
     * the time of inserted frame is lesser than that of
     * last frame, the frame is not inserted and
     * <code>null</code> is returned.
     * <p>
     * If this animated value already contains a key frame
     * for given time, its attributes are replaced with
     * those given.
     * @param value
     *            key frame value
     * @param time
     *            key frame time in milliseconds in timeline
     *            time
     * @param tangentIn
     *            left tangent
     * @param tangentOut
     *            right tangent
     * @param interpolator
     *            interpolator to use when this frame
     *            becomes next frame in the animation
     * @return the added or updated key frame, or
     *         <code>null</code> if <code>time</code> is
     *         before the time of current frame and storing
     *         past frames is disabled.
     */
    public KeyFrame setKeyFrame(double value,
                                long time,
                                double tangentIn,
                                double tangentOut,
                                Interpolator interpolator)
    {
        return setKeyFrame(new KeyFrame(value,
                                        time,
                                        tangentIn,
                                        tangentOut,
                                        interpolator));
    }

    /**
     * Sets a key frame in the animation.
     * <p>
     * If this animated value does not store past values and
     * the time of inserted frame is lesser than that of
     * last frame, the frame is not inserted and
     * <code>null</code> is returned.
     * <p>
     * If this animated value already contains a key frame
     * for given time, its attributes are replaced with
     * those given.
     * @param value
     *            key frame value
     * @param time
     *            key frame time in milliseconds in timeline
     *            time
     * @param tangent
     *            tangent
     * @param interpolator
     *            interpolator to use when this frame
     *            becomes next frame in the animation
     * @return the added or updated key frame, or
     *         <code>null</code> if <code>time</code> is
     *         before the time of current frame and storing
     *         past frames is disabled.
     */
    public KeyFrame setKeyFrame(double value,
                                long time,
                                double tangent,
                                Interpolator interpolator)
    {
        return setKeyFrame(new KeyFrame(value, time, tangent, interpolator));
    }

    /**
     * Adds a new key frame at current time.
     * <p>
     * Uses current value; the interpolator of next frame if
     * its set, a {@link StepInterpolator} otherwise; the
     * tangent is set using an interpolator.
     * <p>
     * If a key frame at current time already exists, it
     * remains unmodified and is returned instead.
     * @return inserted or updated frame
     */
    public KeyFrame setKeyFrame()
    {
        if (currentFrame != null && currentFrame.getTime() == getCurrentTime())
            return currentFrame;
        else if (currentFrame == null)
            return currentFrame =
                    new KeyFrame(value,
                                 getCurrentTime(),
                                 0,
                                 StepInterpolator.INSTANCE);
        else
        {
            if (pastFrames != null)
                pastFrames.addLast(currentFrame);

            if (nextFrame == null)
                return currentFrame =
                        new KeyFrame(value,
                                     getCurrentTime(),
                                     0,
                                     StepInterpolator.INSTANCE);

            else
                return currentFrame =
                        new KeyFrame(value,
                                     getCurrentTime(),
                                     nextFrame.getInterpolator()
                                              .getTangent(currentFrame.getValue(),
                                                          currentFrame.getTime(),
                                                          currentFrame.getTangentOut(),
                                                          nextFrame.getValue(),
                                                          nextFrame.getTime(),
                                                          nextFrame.getTangentIn(),
                                                          getCurrentTime()),
                                     nextFrame.getInterpolator());

        }

    }

    private KeyFrame setKeyFrame(KeyFrame keyFrame)
    {
        KeyFrame result = keyFrame;
        if (keyFrame.getTime() <= getCurrentTime())
        {
            if (currentFrame == null)
                currentFrame = keyFrame;
            else if (keyFrame.getTime() == currentFrame.getTime()) //Altering current frame
                result = currentFrame;
            else if (keyFrame.getTime() > currentFrame.getTime()) //Inserting during current animation segment
            {
                if (pastFrames != null)
                    pastFrames.addLast(currentFrame);
                currentFrame = keyFrame;
            }
            else if (pastFrames != null) //Altering past frames
            {

                int index =
                        Collections.binarySearch(pastFrames,
                                                 keyFrame,
                                                 new FramePrecedenceComparator());
                if (index < 0)
                    pastFrames.add(-(index + 1), keyFrame);
                else
                    result = pastFrames.get(index);

            }
            else
                //In the past and past storing is disabled
                result = null;
        }
        else
        {
            if (nextFrame == null) //Adding as next frame
                nextFrame = keyFrame;
            else if (keyFrame.getTime() == nextFrame.getTime()) //Altering next frame
                result = nextFrame;
            else if (keyFrame.getTime() < nextFrame.getTime()) // Replacing next frame
            {
                futureFrames.push(nextFrame);
                nextFrame = keyFrame;
            }
            else
            // Altering future frames
            {
                int index =
                        Collections.binarySearch(futureFrames,
                                                 keyFrame,
                                                 new FramePrecedenceComparator());
                if (index < 0)
                    futureFrames.add(-(index + 1), keyFrame);
                else
                    result = futureFrames.get(index);

            }
        }

        if (result != null && result != keyFrame) //Frame with the same time already exists. Replacing it instead.
        {
            result.set(keyFrame.getValue(),
                       keyFrame.getTangentIn(),
                       keyFrame.getTangentOut(),
                       keyFrame.getInterpolator());
        }

        if (result == currentFrame)
            calculateCurrentValue();

        return result;
    }

    /**
     * Remove selected key frame from this animated value.
     * @param keyFrame
     *            key frame.
     */
    public void removeKeyFrame(KeyFrame keyFrame)
    {
        assert keyFrame.getAnimatedValue() == this;
        if (keyFrame.getTime() < getCurrentTime())
        {
            if (keyFrame == currentFrame)
            {
                if (pastFrames != null)
                    currentFrame = pastFrames.pollLast();
                else
                    currentFrame = null;
            }
            else if (pastFrames != null)
                pastFrames.remove(keyFrame);
        }
        else
        {
            if (keyFrame == nextFrame)

                nextFrame = futureFrames.pollFirst();
            else
                futureFrames.remove(keyFrame);
        }

    }

    /**
     * Returns current key frame.
     * @return current frame
     */
    public final KeyFrame getCurrentFrame()
    {
        return currentFrame;
    }

    /**
     * Returns next key frame.
     * @return next frame
     */
    public final KeyFrame getNextFrame()
    {
        return nextFrame;
    }

    /**
     * Updates the current and next key frame and value
     * according to {@link #getCurrentTime() current time}.
     * @param timeDelta
     *            time that passed since last call
     * @see Timeline#animate(long)
     */
    public void animate(long timeDelta)
    {

        if (timeDelta < 0)
            while (currentFrame != null
                && currentFrame.getTime() > getCurrentTime())
            {
                if (nextFrame != null)
                    futureFrames.push(nextFrame);
                nextFrame = currentFrame;
                if (pastFrames != null)
                    currentFrame = pastFrames.pollLast();
            }
        else
        {
            while (nextFrame != null && nextFrame.getTime() <= getCurrentTime())
            {
                if (pastFrames != null)
                    pastFrames.addLast(currentFrame);
                currentFrame = nextFrame;
                nextFrame = futureFrames.pollFirst();
            }
        }

        calculateCurrentValue();

    }

    private void calculateCurrentValue()
    {
        if (currentFrame != null)
        {
            if (nextFrame != null)
                value =
                        nextFrame.getInterpolator()
                                 .interpolate(currentFrame.getValue(),
                                              currentFrame.getTime(),
                                              currentFrame.getTangentOut(),
                                              nextFrame.getValue(),
                                              nextFrame.getTime(),
                                              nextFrame.getTangentIn(),
                                              getCurrentTime());
            else
                value = currentFrame.getValue();
        }
    }

    /**
     * Note: this class has a natural ordering that is
     * inconsistent with equals.
     */
    public final class KeyFrame
    {
        private double value;
        private final long time;
        private double tangentIn;
        private double tangentOut;
        private Interpolator interpolator;

        /**
         * Creates a new key frame.
         * @param value
         *            key frame value
         * @param time
         *            key frame time in milliseconds in
         *            timeline time
         * @param tangentIn
         *            left tangent
         * @param tangentOut
         *            right tangent
         * @param interpolator
         *            interpolator to use when this frame
         *            becomes next frame in the animation
         */
        private KeyFrame(double value,
                         long time,
                         double tangentIn,
                         double tangentOut,
                         Interpolator interpolator)
        {
            this.value = value;
            this.time = time;
            this.tangentIn = tangentIn;
            this.tangentOut = tangentOut;
            this.interpolator = interpolator;
        }

        /**
         * Creates a new key frame.
         * @param value
         *            key frame value
         * @param time
         *            key frame time in milliseconds in
         *            timeline time
         * @param tangent
         *            tangent
         * @param interpolator
         *            interpolator to use when this frame
         *            becomes next frame in the animation
         */
        private KeyFrame(double value,
                         long time,
                         double tangent,
                         Interpolator interpolator)
        {
            this(value, time, tangent, tangent, interpolator);
        }

        /**
         * Returns the {@link AnimatedValue} this key frame
         * belongs to.
         * @return the animated value
         */
        public AnimatedValue getAnimatedValue()
        {
            return AnimatedValue.this;
        }

        /**
         * Returns value.
         * @return value
         */
        public double getValue()
        {
            return value;
        }

        /**
         * Sets new value of value.
         * @param value
         *            the value to set
         */
        public void setValue(double value)
        {
            this.value = value;
        }

        /**
         * Returns time.
         * @return time
         */
        public long getTime()
        {
            return time;
        }

        /**
         * Returns tangentIn.
         * @return tangentIn
         */
        public double getTangentIn()
        {
            return tangentIn;
        }

        /**
         * Sets new value of tangentIn.
         * @param tangentIn
         *            the tangentIn to set
         */
        public void setTangentIn(double tangentIn)
        {
            this.tangentIn = tangentIn;
        }

        /**
         * Returns tangentOut.
         * @return tangentOut
         */
        public double getTangentOut()
        {
            return tangentOut;
        }

        /**
         * Sets new value of tangentOut.
         * @param tangentOut
         *            the tangentOut to set
         */
        public void setTangentOut(double tangentOut)
        {
            this.tangentOut = tangentOut;
        }

        /**
         * Returns interpolator.
         * @return interpolator
         */
        public Interpolator getInterpolator()
        {
            return interpolator;
        }

        /**
         * Sets new value of interpolator.
         * @param interpolator
         *            the interpolator to set
         */
        public void setInterpolator(Interpolator interpolator)
        {
            this.interpolator = interpolator;
        }

        /**
         * Sets attributes of this key frame.
         * <p>
         * <i>Note: Currently time is not alterable.</i>
         * @param value
         *            new value to set
         * @param tangentIn
         *            new tangent in to set
         * @param tangentOut
         *            new tangent out to set
         * @param interpolator
         *            new {@link Interpolator} to set
         */
        public void set(double value,
                        double tangentIn,
                        double tangentOut,
                        Interpolator interpolator)
        {
            this.value = value;
            this.tangentIn = tangentIn;
            this.tangentOut = tangentOut;
            this.interpolator = interpolator;
        }
    }

    private class FramePrecedenceComparator implements Comparator<KeyFrame>
    {
        public FramePrecedenceComparator()
        {
        }

        /** {@inheritDoc} */
        @Override
        public int compare(KeyFrame o1, KeyFrame o2)
        {
            return Long.signum(o1.getTime() - o2.getTime());
        }
    }
}
