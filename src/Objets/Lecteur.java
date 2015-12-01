package Objets;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.videosurface.CanvasVideoSurface;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;
import uk.co.caprica.vlcj.runtime.x.LibXUtil;
/**
 *
 * @author Ferhat
 */
public class Lecteur {
    private JPanel panel; 
    private MediaPlayerFactory mediaPlayerFactory;
    private EmbeddedMediaPlayer mediaPlayer;
    private EmbeddedMediaPlayerComponent mediaPlayerComponent;
    private JDialog dialogLecteur;
    public Lecteur(JPanel p){
        this.panel=p;
        chargerLibrairie();
        init();
    }
    private void chargerLibrairie(){
        NativeLibrary.addSearchPath(
                RuntimeUtil.getLibVlcLibraryName(), "c:/Program Files/VideoLAN/VLC/");
           Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
           //LibXUtil.initialise();
       }
    
    private void init(){
//        //Créer une instance de Canvas
//        Canvas c = new Canvas();
//        //L'arrière plan de la vidéo est noir par défaut
//        c.setBackground(Color.black);
//        panel.setLayout(new BorderLayout());
//        //La vidéo prend toute la surface
//        panel.add(c, BorderLayout.CENTER);
//        //Créer une instance factory
//        mediaPlayerFactory = new MediaPlayerFactory();
//        //Créer une instance lecteur média
//        mediaPlayer = mediaPlayerFactory.newEmbeddedMediaPlayer();
//        mediaPlayer.setVideoSurface(mediaPlayerFactory.newVideoSurface(c));
//        //Plein écran
//        mediaPlayer.toggleFullScreen();
//        //Cacher le curseur de la souris à l'intérieur de JFrame
//        mediaPlayer.setEnableMouseInputHandling(false);
//        //Désactiver le clavier à l'intérieur de JFrame
//        mediaPlayer.setEnableKeyInputHandling(true);
//        //Préparer le fichier
//        //lire le fichier 
//        //mediaPlayer.play();
        
        String[] args = {"--vout=macosx"};
        mediaPlayerFactory = new MediaPlayerFactory(args);
        Canvas canvas = new Canvas();
        canvas.setBackground(Color.black);
        CanvasVideoSurface videoSurface = mediaPlayerFactory.newVideoSurface(canvas);
        mediaPlayer = mediaPlayerFactory.newEmbeddedMediaPlayer();
        mediaPlayer.setVideoSurface(videoSurface);

        mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
        mediaPlayerComponent.mediaChanged(mediaPlayer);
        panel.add(mediaPlayerComponent);
        panel.repaint();
        JFrame frame = null;
        dialogLecteur = new JDialog(frame);
        dialogLecteur.setTitle("Lecteur vidéo");
        dialogLecteur.setLocationRelativeTo(null);
        dialogLecteur.setSize(600, 300);
        dialogLecteur.setAlwaysOnTop(true);
        dialogLecteur.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialogLecteur.setContentPane(mediaPlayerComponent);
        
                    
    }
    
    public void PrepareMedia(String PATH){
       mediaPlayerComponent.getMediaPlayer().prepareMedia(PATH);
    }
    public void play(){
        dialogLecteur.setVisible(true);
        mediaPlayerComponent.getMediaPlayer().play();
    }
    public void pause(){
       mediaPlayerComponent.getMediaPlayer().pause();
    }
    public void stop(){
        mediaPlayerComponent.getMediaPlayer().stop();
    }
    
    
}
