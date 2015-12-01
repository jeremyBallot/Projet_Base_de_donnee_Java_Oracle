package Objets;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author Ferhat
 */

public class AudioThread extends Thread {
    
    
    private static final int BUFFER_SIZE = 16000;
    private static boolean stop=false;
    private static File file;
    private static int pourcentage;
    private static int pourc;
    
    public AudioThread(File filee,int Pourc){
                file=filee;
                pourc=Pourc;
        }
    
    
    @Override
    public void run()
    {
        try {
            lireAudio();
        } catch (UnsupportedAudioFileException ex) {
            Logger.getLogger(AudioThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AudioThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (LineUnavailableException ex) {
            Logger.getLogger(AudioThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 
    
    public int getPourcentage(){return pourcentage;}
    public void setPourcentage(int pourc)
    {
        pourcentage=pourc;
        
    }
    
    public void lireAudio() throws UnsupportedAudioFileException, IOException, LineUnavailableException
    {
       AudioInputStream audioInputStream = null;

       //obtention d'un flux audio à partir d'un fichier (objet File)
       audioInputStream = AudioSystem.getAudioInputStream(file);

       
       AudioFormat baseFormat = audioInputStream.getFormat();
       AudioFormat decodedFormat = new AudioFormat(
   AudioFormat.Encoding.PCM_SIGNED, // Encoding to use
   baseFormat.getSampleRate(),      // sample rate (same as base format)
   16,                  // sample size in bits (thx to Javazoom)
   baseFormat.getChannels(),      // # of Channels
   baseFormat.getChannels()*2,      // Frame Size
   baseFormat.getSampleRate(),      // Frame Rate
   false                  // Big Endian
       );
       AudioInputStream din = AudioSystem.getAudioInputStream(decodedFormat, audioInputStream);
       //AudioFormat audioFormat = audioInputStream.getFormat();
       DataLine.Info info = new DataLine.Info(SourceDataLine.class,decodedFormat);
       SourceDataLine line = getLine(decodedFormat);
       
       try {
   line.open(decodedFormat);
       } catch (LineUnavailableException e) {
       e.printStackTrace();
       return;
       }
       line.start();
       byte bytes[] = new byte[BUFFER_SIZE];
       int bytesRead=0;
       long audioLength = audioInputStream.available();
       System.out.println(audioLength);
       long comptBytesRead=Math.round(pourc*audioLength/1000000000);
       pourcentage=pourc;
       din.skip(comptBytesRead);
       while (((bytesRead = din.read(bytes, 0, bytes.length)) != -1))
       {
           while(stop)
           {}
           line.write(bytes, 0, bytesRead);
           comptBytesRead+=bytesRead;
           pourcentage=Math.round(1000000000*comptBytesRead/audioLength);
           //System.out.println(pourcentage);
       }
       pourcentage=1000000000;
       line.drain();
       line.close();
    }
    
    private SourceDataLine getLine(AudioFormat audioFormat) throws LineUnavailableException
    {
     SourceDataLine res = null;
     DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
     res = (SourceDataLine) AudioSystem.getLine(info);
     res.open(audioFormat);
     return res;
    }
    
    public void pauseOff()
    {
        stop=false;
    }
    
    public void pause()
    {
        stop=!stop;
    }
}

