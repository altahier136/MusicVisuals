package global;

import ie.tudublin.visual.VScene;
import ie.tudublin.visual.Visual;

public class GlobalVisual extends VScene {

    public GlobalVisual(Visual v) {
        super(v);
    }

    @Override
    public void render(int elapsed) {
        // 4 lines from the corners of the screen extending from the z-axis
        v.stroke(255);
        // v.line(0, 0, -1000000, 0, 0, 1000);
        // v.line(0, v.height, -1000000, 0, v.height, 1000);
        // v.line(v.width, 0, -1000000, v.width, 0, 1000);
        // v.line(v.width, v.height, -1000000, v.width, v.height, 1000);

        // Make is translate with the mouse

    }


}
