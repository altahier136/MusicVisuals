package ie.tudublin;

import c21348423.AdriansVisual;
import c21415904.SarahVisual;
import global.Demo;
import global.GlobalVisual;
import ie.tudublin.visual.*;

/*
    Song lyrics:
    Intro - 00:00 - Sarah
    Verse 1 - 00:21
        00:21 It's not in the way that you hold me
        00:26 It's not in the way you say you care
        00:31 It's not in the way you've been treating my friends
        00:36 It's not in the way that you stayed till the end
        00:40 It's not in the way you look or the things that you say that you'll do
    Chorus - 00:45 - Sarah
        00:45 Hold the line
        00:48 Love isn't always on time
        00:55 Hold the line
        00:58 Love isn't always on time
    Verse 2 - 01:03 - Jennifer
        01:05 It's not in the words that you told me
        01:11 It's not in the way you say you're mine
        01:15 It's not in the way that you came back to me
        01:21 It's not in the way that your love set me free
        01:25 It's not in the way you look or the things that you say that you'll do
    Chorus - 01:30 - Jennifer
        01:30 Hold the line
        01:33 Love isn't always on time
        01:40 Hold the line
        01:43 Love isn't always on time
    Instrumental Solo 01:48 - Adrian
    Verse 3 - 02:31 - Altahier
        02:31 It's not in the words that you told me
        02:36 It's not in the way you say you're mine
        02:41 It's not in the way that you came back to me
        02:46 It's not in the way that your love set me free
        02:51 It's not in the way you look or the things that you say that you'll do
    Outro - 02:55 - Altahier
        02:55 Hold the line
        02:59 Love isn't always on time
        03:05 Hold the line
        03:09 Love isn't always on time
        03:11 Love isn't always on time
        03:15 Hold the line
        03:18 Love isn't always on time
        03:21 Love isn't always on time
        03:22 Love isn't always on time
        03:25 Hold the line
        03:28 Love isn't always on time
        03:34 Love isn't always on time
        03:38 Love isn't always on time
        03:33 Love isn't always on time
        03:47 Whoah-ooh-ooh
    END - 03:58
*/
public class HoldTheLine extends Visual {
    VScene av;
    VScene gv;
    VScene sv;
    VScene demo;

    HoldTheLine() {
        super(1024, 44100, 0.5f);
    }

    public void settings() {
        fullScreen(P3D);
    }

    public void setup() {
        beginAudio("Toto - Hold The Line.wav");
        gv = new GlobalVisual(this);
        av = new AdriansVisual(this);
        sv = new SarahVisual(this);
        demo = new Demo(this);
    }

    public void draw() {
        int elapsed = audioPlayer().position();
        background(0);
        text(elapsed, 10, 10);

        // gv.render(elapsed);
        av.render(elapsed);
        sv.render(elapsed);
    }

    public void keyPressed() {
        switch (key) {
            case '1':
                seek(0);
                break;
            case '2':
                seek(1, 3);
                break;
            case '3':
                seek(1, 48);
                break;
            case '4':
                seek(2, 31);
                break;
            case ' ':
                pausePlay();
                break;
            default:
                break;
        }
    }
}
