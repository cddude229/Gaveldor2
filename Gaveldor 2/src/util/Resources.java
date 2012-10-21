package util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

public class Resources {
    
    public static URL getResource(String name){
        return Resources.class.getResource(name);
    }
    
    public static InputStream getResourceAsStream(String name){
        return Resources.class.getResourceAsStream(name);
    }
    
    public static Image getImage(String name) throws SlickException{
        return new Image(getResourceAsStream(name), name, false);
    }
    
    public static Sound getSound(String name) throws SlickException{
    	if (name.endsWith(".wav")){
    		throw new IllegalArgumentException("We can't use WAVs, because it breaks the JAR when deployed");
    	}
        return new Sound(getResource(name));
    }
    
    private static final String[] LWJGL_NATIVE_NAMES = new String[]{
        "jinput-dx8.dll",
        "jinput-dx8_64.dll",
        "jinput-raw.dll",
        "jinput-raw_64.dll",
        "libjinput-linux.so",
        "libjinput-linux64.so",
        "libjinput-osx.jnilib",
        "liblwjgl.jnilib",
        "liblwjgl.so",
        "liblwjgl64.so",
        "libopenal.so",
        "libopenal64.so",
        "lwjgl.dll",
        "lwjgl64.dll",
        "openal.dylib",
        "OpenAL32.dll",
        "OpenAL64.dll",
    };
    
    public static void setupLWJGLNatives(String path) throws IOException, URISyntaxException{
        File tmpDir = new File(System.getProperty("java.io.tmpdir"), path);
        if (!tmpDir.exists()){
            boolean success = tmpDir.mkdirs();
            if (!success){
                throw new IOException("Temporary directory path could not be made");
            }
        }
        
//        for (String name : new File(Util.class.getResource(path).toURI()).list()){
        for (String name : LWJGL_NATIVE_NAMES){
            File tmpFile = new File(tmpDir, name);
            if (!tmpFile.exists()){
                InputStream in = getResourceAsStream(path + "/" + name);
                OutputStream out = new FileOutputStream(tmpFile);
    
                byte[] buf = new byte[8192];
                int len;
                while ((len = in.read(buf)) != -1) {
                    out.write(buf, 0, len);
                }
    
                in.close();
                out.close();
            }
        }
        
        System.setProperty("org.lwjgl.librarypath", tmpDir.getAbsolutePath());
    }
}
