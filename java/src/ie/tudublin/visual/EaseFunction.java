package ie.tudublin.visual;

public interface EaseFunction {
    float ease(float t);
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