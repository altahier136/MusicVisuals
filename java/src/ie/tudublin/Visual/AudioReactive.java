package ie.tudublin.Visuals;

import ie.tudublin.*;

/**
 * AudioReactive is an abstract class which will be used to create
 * objects within the Music Visualiser which are audio reactive
 *
 * @version 1.0
 * @since 06-03-2023
 * @author AdrianCapacite
 */
public abstract class AudioReactive {
    Visual visual;

    public AudioReactive(Visual visual) {
        this.visual = visual;
    }
    public abstract void render();

}
