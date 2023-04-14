package ie.tudublin.visual;

import java.util.ArrayList;

import processing.core.PApplet;

/**
 * VAnimation is a class that is used to handle animating a value over time.
 * User creates an instance of VAnimation which adds it to the list of
 * animations in the Visual class. The user adds transition sections with a
 * selected ease function.
 * The class then fills in the gaps between the sections with linear
 * interpolation.
 *
 * Class details with millisecond time units and float values.
 */
public class VAnimation {
    int lengthMs; // Duration of the animation in milliseconds
    ArrayList<Section> sections;

    public VAnimation(int lengthMs) {
        this.lengthMs = lengthMs;
        sections = new ArrayList<Section>();
    }

    /**
     * Appends a section to the animation
     *
     * @param durationMs
     * @param startValue
     * @param endValue
     * @param easeFunction
     */
    public void appendSection(int durationMs, float startValue, float endValue, EaseFunction easeFunction) {
        // If no sections, set start time to 0
        if (sections.size() == 0) {
            sections.add(new Section(0, durationMs, startValue, endValue, easeFunction));
            return;
        }

        // Set start time of next section to end time of last section
        Section lastSection = sections.get(sections.size() - 1);
        int startTime = lastSection.getEndTime();
        sections.add(new Section(startTime, durationMs, startValue, endValue, easeFunction));
    }

    public void addTransition(int startMs, int durationMs, float startValue, float endValue,
            EaseFunction easeFunction) {
        // Check if it overlaps with any existing sections
        for (Section section : sections) {
            if (startMs >= section.getStartTime() && startMs < section.getEndTime())
                throw new RuntimeException("Transition overlaps with existing section");
        }

        // If right after end of last section,
        // fill gap with linear transition from end value of last section to start value
        // of new section
        // then add transition section
        if (sections.size() != 0) {
            Section lastSection = sections.get(sections.size() - 1);
            if (startMs != lastSection.getEndTime() + 1) {
                int gapDuration = startMs - lastSection.getEndTime() - 1;
                appendSection(gapDuration, lastSection.getValue(lastSection.getEndTime()), startValue, linearEase);
            }
        } else {
            // If no sections, check if start time is 0
            if (startMs != 0) {
                appendSection(startMs - 1, startValue, startValue, linearEase);
            }
        }
        appendSection(durationMs, startValue, endValue, easeFunction);

    }

    // !BUG - It is always returning a 0 value
    public float getValue(int time) {
        // Check if time is within animation
        if (time < 0 || time > lengthMs) {
            // throw new RuntimeException("Time is not within the animation");
            System.out.println("Time is not within the animation");
        }

        // Find the section that the time is in
        for (Section section : sections) {
            if (time >= section.getStartTime() && time < section.getEndTime()) {
                return section.getValue(time);
            }
        }

        System.out.println("Time is not within the animation");
        return 0;
    }

    // Represents a section of the animation which as a start, duration and ease
    // function
    class Section {
        private int startTime;
        private int duration;
        private float startValue;
        private float endValue;
        private EaseFunction easeFunction;

        public Section(int startTimeMs, int durationMs, float startValue, float endValue, EaseFunction easeFunction) {
            this.startTime = startTimeMs;
            this.duration = durationMs;
            this.startValue = startValue;
            this.endValue = endValue;
            this.easeFunction = easeFunction;
        }

        /** Returns the start time of the section */
        public int getStartTime() {
            return startTime;
        }

        public int getEndTime() {
            return startTime + duration;
        }

        /**
         * Returns the value of the animation at the given time
         *
         * @param time
         * @return
         */
        public float getValue(int time) {
            if (time < startTime || time > startTime + duration) {
                throw new RuntimeException("Time is not within the section");
            }

            int relativeTime = time - startTime;
            float timePercent = relativeTime / (float) duration;
            float easedTime = easeFunction.ease(timePercent);
            return PApplet.lerp(startValue, endValue, easedTime);
        }
    }

    public interface EaseFunction {
        float ease(float t);
    }

    // Ease functions
    // https://easings.net/
    // https://web.dev/choosing-the-right-easing/
    // - Ease in, ease out or both, 200ms - 500ms is recommended

    /**
     * y = x
     * Oh you thought its more complicated than that?
     * Here is the full equation:
     * y = (2(x^2-12345)^2*100/2(x^2-12345)^2) - sqrt(10^4)
     */
    public static EaseFunction linearEase = (t) -> t;
    /**
     * Smoothstep is quad in and quad out lerped
     * lerp(start, stop, value) = start + (stop - start) * value
     * y = lerp(quadEaseIn(x), quadEaseOut(x), x)
     * Simplifying the equation gives us
     * y = x - x(x - 1)^2
     */
    public static EaseFunction smoothstepEase = (f) -> {
        return f * f + f * ((1 - (1 - f) * (1 - f)) - f * f);
    };

    /** y = x * (2 - x) */
    public static EaseFunction outQuadEase = (f) -> f * (2 - f);
    /** y = 1 + (1 - x)^5 */
    public static EaseFunction outQuintEase = (f) -> 1 + (--f) * f * f * f * f;

    /**
     * easeOutBounce is a combination of easeOutQuad and easeOutQuint
     * Added as something a lil fun
     */
    public static EaseFunction outBounceEase = (f) -> {
        final float n1 = 7.5625f;
        final float d1 = 2.75f;

        if (f < 1 / d1) {
            return n1 * f * f;
        } else if (f < 2 / d1) {
            return n1 * (f -= 1.5f / d1) * f + 0.75f;
        } else if (f < 2.5 / d1) {
            return n1 * (f -= 2.25f / d1) * f + 0.9375f;
        } else {
            return n1 * (f -= 2.625f / d1) * f + 0.984375f;
        }
    };

}