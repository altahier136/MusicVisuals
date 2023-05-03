# Music Visualiser Project

Name - Student Number - Class Group:

- Sarah Barron - C21415904 - TU856-2
- Adrian Thomas Capacite - C21348423 - TU856-2
- Jennifer Kearns - C21383126 - TU856-2
- Altahier Saleh - C21415952 - TU856-2


## Description of the assignment

Music: Toto - Hold The Line (Official Video) - [https://www.youtube.com/watch?v=htgr3pvBr-I](https://www.youtube.com/watch?v=htgr3pvBr-I)

[![Toto - Hold The Line (Official Video)](https://img.youtube.com/vi/htgr3pvBr-I/hqdefault.jpg)](https://www.youtube.com/watch?v=htgr3pvBr-I)

Video: Hold The Line - Music Visuals - [https://youtu.be/BiE8rncqw7Y](https://youtu.be/BiE8rncqw7Y)

[![Hold The Line - Music Visuals](https://img.youtube.com/vi/BiE8rncqw7Y/hqdefault.jpg)](https://youtu.be/BiE8rncqw7Y)

### Adrian's Visuals

Adrian's visuals present a dynamic and engaging Music Visualizer scene featuring a horse called Pony Hopps, a rainbow stage, and a starry
background of superellipses.

Pony Hopps is a 3D object imported into the scene, illuminated with purple/red lights. The horse dynamically moves and
hops in sync with the music while the camera perspective shifts around it. The character was inspired by Maxwell the Cat, who spins happily to a
cheerful song.

The stage is a visually reactive disk composed of rainbow-colored arcs, which respond to the music by changing their length and color,
creating a vibrant and mesmerizing experience.

The background consists of a grid of superellipses that adapt their size and color according to the
music, enhancing the scene's atmosphere and complementing Pony Hopps and the stage.

Together, Adrian's visuals provide a captivating and immersive
environment that beautifully showcases the integration of 3D elements, color palettes, animations, and audio for a unique Music Visualizer.

### Sarah's Visuals

...

### Jennifer's Visuals

...

### Altahier's Visuals

...

## Instructions

### Repo Instructions

- Fork this repository and use it a starter project for your assignment
- Create a new package named your student number and put all your code in this package.
- You should start by creating a subclass of ie.tudublin.Visual
- There is an example visualiser called MyVisual in the example package
- Check out the WaveForm and AudioBandsVisual for examples of how to call the Processing functions from other classes that are not subclasses of PApplet

### Running the Visualiser

1. Follow repo instructions above.
2. If you are using the Java debugger in VSCode, you can run the code from the debugger.

3. If you do not have the debugger set up, you can run the .sh script files

- `./java/compile.sh` will compile your code
- `./java/run.sh` will run the code

## How it works

### The Visual Framework - `ie.tudublin.Visual`

The `ie.tudublin.visual` package is a collection of classes designed to create and manage a Music Visualizer application.

1. `Visual.java`:
This abstract class serves as the main framework for a Music Visualizer. It extends the PApplet class from the Processing library and encapsulates
several audio and analysis classes, including `Minim`, `AudioInput`, `AudioPlayer`, `FFT`, `BeatDetect`, and `AudioAnalysis`. The class contains fields
like `bufferSize`, `sampleRate`, lyrics, and various audio objects. The constructor takes in parameters for the buffer size, sample rate, and lerp
amount. The buffer size must be a power of 2. The class also contains several abstract methods like `settings()`, `setup()`, and `draw()` to be
implemented by subclasses. Additional methods provided in this class include audio-related methods like `beginAudio()`, `pausePlay()`, and `seek()` as
well as lyric-related methods like `loadLyrics()` and `getLyrics()`. The class also provides helper methods for translating the origin to the center of
the screen and converting between time formats.

2. `AudioAnalysis.java`: This class is responsible for managing audio analysis and implements the
AudioListener interface. It processes samples to provide values such as amplitude, waveform, bands, spectrum, and their lerped counterparts, such as
lerpedAmplitude, lerpedWaveform, etc. It includes handling FFT (Fast Fourier Transform) and BeatDetect objects and provides methods to analyze and
process audio data, allowing for the generation of visualization based on the audio input.

3. `EaseFunction.java`: This interface represents an easing
function that can be applied to animations or transitions within the visualizer. It provides lambda functions for smooth and visually appealing
interpolation of values over time.

4. `VAnimation.java`: This class manages animations and transitions within the visualizer. It provides methods to
define and control animations, including timing and easing functions, allowing for a dynamic visual experience based on the audio input.

5. `VObject.java`: This abstract class represents a visual object in the visualizer scene, including position, rotation, scale, and effect. It provides
overridable functions for `render()` and `render(elapsed)` to manage the object's characteristics. Subclasses can override these methods to create
custom visual objects for the visualizer.

6. `VScene.java`: This class inherits from VObject and manages the overall visual presentation of the Music
Visualizer. It is intended to contain a student's visuals in a dedicated time space within the visuals. It provides methods to manage and render
scenes, including adding VObjects and handling transitions between different parts of the visualization.

### Putting it all together - `HoldTheLine.java`

A class `HoldTheLine.java` is created to extend the `Visual.java` class. This class is responsible for creating the visualizer
by managing each of the visual scenes created by the students.

Finally we have the Main.java which starts the `HoldTheLine.java` PApplet sketch.

### Adrian's Visuals - `AdriansVisuals.java`

Adrian's visuals features a Horse named Pony Hopps on a rainbow stage and a starry background of superelpises.

`AdriansVisuals.java` is a class that extends `VScene` and provides a custom visual scene for the Music Visualizer application. The class is part of the `c21348423` package and includes various visual components like `VObject`, custom animations, and color palettes.

In this class, different visual components such as `Circle`, `HappyHorse`, `SquigglyArcs`, and `SuperStars` are created, which extend `VObject` for customized rendering. It also utilizes the `EaseFunction` and `VAnimation` from the `ie.tudublin.visual` package to create smooth animations and transitions within the visualizer.  The `Circle`, `HappyHorse` The constructor of`AdriansVisual`initializes the visual components and sets the scene animations. The`render(int elapsed)` method is responsible for rendering the visual elements in this custom scene based on the elapsed time and audio input.

components and sets the scene animations. The `render(int elapsed)` method is responsible for rendering the visual elements in this custom scene based on the elapsed time and audio input.  The `Circle`, `HappyHorse`, `SquigglyArcs`, and `SuperStars` classes are inner classes in `AdriansVisuals`, which render unique visuals within the scene. These classes override the `render()` function in their parent `VObject` class to create custom visualizations.

In summary, the `AdriansVisuals.java` class is an extension of `VScene` that provides a customized visual scene for the Music Visualizer application. It showcases the use of various visual components, color palettes, animations, and audio integration to create a dynamic and engaging visual experience.

### Sarah's Visuals - `SarahsVisuals.java`

...

### Jennifer's Visuals - `JennifersVisuals.java`

...

### Altahier's Visuals - `AltahiersVisuals.java`

...
## What I am most proud of in the assignment

### Sarah Barron

### Adrian Thomas Capacite

I am most proud of the Visual PApplet framework that I have created for this assignment. As well as that the demos that demonstrate the use of the framework.

I set up the PApplet framework `ie.tudublin.visual` to make the process of creating the visuals and scenes easier.
I also set up two demos to demonstrate the use of the framework such as one for demoing the AudioAnalysis and the other for
demoing the VAnimation and VObject classes.
I also created Pony Hopps The Happy Horse along with the other visuals in the scene. The other visuals are
the Squiggly Arcs and the Super Stars.

I am most

### Jennifer Kearns

### Altahier Saleh
