package gg.codie.mineonline.patches;

import gg.codie.mineonline.Settings;
import net.bytebuddy.asm.Advice;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.Month;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ClassGetResourceAdvice {

    @Advice.OnMethodExit
    static void intercept(@Advice.Argument(0) String resourceName, @Advice.Return(readOnly = false) InputStream inputStream) {
        try {
            if(resourceName.endsWith("splashes.txt")) {
                LocalDate today = LocalDate.now();
                if(today.getMonth() == Month.MARCH && today.getDayOfMonth() == 26) {
                    inputStream = new ByteArrayInputStream("Happy Birthday Codie!".getBytes());
                    return;
                }

                String customSplashes =
                        "Black lives matter!\n" +
                        "Be anti-racist!\n" +
                        "Learn about allyship!\n" +
                        "Speak OUT against injustice and UP for equality!\n" +
                        "Amplify and listen to BIPOC voices!\n" +
                        "Educate your friends on anti-racism!\n" +
                        "Support the BIPOC community and creators!\n" +
                        "Stand up for equality in your community!\n" +
                        "Trans Rights!\n" +
                        "Now Playing: Home - Resonance\n" +
                        "you love chips and i love chips\n" +
                        "MineOnline!\n" +
                        "@codieradical\n" +
                        "Hello Kai!\n" +
                        "The Great Journey Awaits!\n";

                byte[] splashesTxt = new byte[inputStream.available()];
                inputStream.read(splashesTxt);

                byte[] splashBytes = (byte[])ClassLoader.getSystemClassLoader().loadClass("gg.codie.common.utils.ArrayUtils").getMethod("concatenate", byte[].class, byte[].class).invoke(null, customSplashes.getBytes(), splashesTxt);

                inputStream = new ByteArrayInputStream(splashBytes);
            } else if (resourceName.endsWith("default.png") || resourceName.endsWith("default.gif")) {
                boolean useCustomFonts = (boolean)ClassLoader.getSystemClassLoader().loadClass("gg.codie.mineonline.Settings").getDeclaredMethod("getCustomFonts").invoke(ClassLoader.getSystemClassLoader().loadClass("gg.codie.mineonline.Settings").getDeclaredField("singleton").get(null));

                if(!useCustomFonts)
                    return;

                String texturePack = (String)ClassLoader.getSystemClassLoader().loadClass("gg.codie.mineonline.Settings").getDeclaredMethod("getTexturePack").invoke(ClassLoader.getSystemClassLoader().loadClass("gg.codie.mineonline.Settings").getDeclaredField("singleton").get(null));
                String texturePacksPath = (String) ClassLoader.getSystemClassLoader().loadClass("gg.codie.mineonline.LauncherFiles").getField("MINECRAFT_TEXTURE_PACKS_PATH").get(null);

                ZipFile texturesZip = new ZipFile(texturePacksPath + texturePack);
                ZipEntry texture = texturesZip.getEntry(resourceName.substring(1));
                if (texture != null) {
                    inputStream = texturesZip.getInputStream(texture);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
