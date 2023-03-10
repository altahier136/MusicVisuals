// package ie.tudublin.Visual;

// /**
//  * Keyframes is an class which will hold elapsed frames and transition
//  * keyframes for the Music Visualiser
//  */
// public class Keyframes {
//     private int[] frames;

//     public Keyframes(int[] frames) {
//         this.frames = frames;
//     }

//     /**
//      * Interpolates between keyframes
//      * @param frame
//      * @return
//      */
//     public int getValue(int frame) {
//         int i = 0;
//         while (i < frames.length && frame > frames[i]) {
//             i++;
//         }
//         if (i == 0) {
//             return 0;
//         }
//         if (i == frames.length) {
//             return 100;
//         }
//         int prevFrame = frames[i - 1];
//         int nextFrame = frames[i];
//         float prevValue = i - 1;
//         float nextValue = i;
//         float value = PApplet.map(frame, prevFrame, nextFrame, prevValue, nextValue);
//         return (int) value;
//     }

//     class keyframe {
//         public int frame;
//         public float value;
//         keyframe(int frame, float value) {
//             this.frame = frame;
//             this.value = value;
//         }
//     }
// }