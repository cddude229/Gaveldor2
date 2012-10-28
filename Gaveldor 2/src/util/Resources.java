package util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.util.ResourceLoader;
import org.newdawn.slick.util.ResourceLocation;

public class Resources {
    

    public static class ActualClasspathLocation implements ResourceLocation {
        public URL getResource(String ref) {
            return this.getClass().getResource(ref);
        }
        
        public InputStream getResourceAsStream(String ref) {
            return this.getClass().getResourceAsStream(ref);
        }
    }
    
    static{
        ResourceLoader.addResourceLocation(new ActualClasspathLocation());
    }
    
    public static URL getResource(String ref) {
        URL ret = ResourceLoader.getResource(ref);
        if (ret == null){
            throw new RuntimeException("Resource not found");
        }
        return ret;
    }
    
    public static InputStream getResourceAsStream(String ref){
        InputStream ret = ResourceLoader.getResourceAsStream(ref);
        if (ret == null){
            throw new RuntimeException("Resource not found");
        }
        return ret;
    }
    
    public static Image getImage(String ref){
        try {
            return new Image(getResourceAsStream(ref), ref, false);
        } catch (SlickException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static Sound getSound(String ref){
    	if (ref.endsWith(".wav")){
    		throw new IllegalArgumentException("We can't use WAVs, because it breaks the JAR when deployed");
    	}
        try {
            return new Sound(getResource(ref));
        } catch (SlickException e) {
            throw new RuntimeException(e);
        }
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
    
    public static void setupLWJGLNatives(String path){
        try{
            File tmpDir = new File(System.getProperty("java.io.tmpdir"), path);
            if (!tmpDir.exists()){
                boolean success = tmpDir.mkdirs();
                if (!success){
                    throw new IOException("Temporary directory path could not be made");
                }
            }
            
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
        } catch (Exception e){
            throw new RuntimeException("LWJGL native files could not be set up", e);
        }
    }
}
