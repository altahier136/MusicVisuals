package ie.tudublin.Visual;

public abstract class Scene {
    protected Visual v;
    protected float transition;
    float test;

    public Scene(Visual visual) {
        this.v = visual;
        transition = 0;
    }

    public abstract void render();

    public void setTransition(float transition) {
        this.transition = transition;
    }
}
