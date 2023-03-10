package ie.tudublin;

public class Main
{
	public static void holdTheLine()
	{
		String[] a = {"MAIN"};
        processing.core.PApplet.runSketch( a, new HoldTheLine());
	}

	public static void main(String[] args)
	{
		holdTheLine();
	}
}