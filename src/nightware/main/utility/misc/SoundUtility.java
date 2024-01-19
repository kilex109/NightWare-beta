package nightware.main.utility.misc;

import nightware.main.utility.Utility;
import java.io.BufferedInputStream;
import java.io.InputStream;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.FloatControl.Type;
import net.minecraft.util.ResourceLocation;

public class SoundUtility implements Utility {
   public static synchronized void playSound(String string, float volume) {
      try {
         Clip clip = AudioSystem.getClip();
         InputStream inputStream = mc.getResourceManager().getResource(new ResourceLocation("nightware/sounds/" + string)).getInputStream();
         BufferedInputStream var4 = new BufferedInputStream(inputStream);
         AudioInputStream var5 = AudioSystem.getAudioInputStream(var4);
         clip.open(var5);
         clip.start();
         FloatControl var6 = (FloatControl)clip.getControl(Type.MASTER_GAIN);
         var6.setValue(volume);
      } catch (Exception var7) {
         var7.printStackTrace();
      }

   }
}
