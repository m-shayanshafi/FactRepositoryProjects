package com.oranda.util;

import java.awt.image.BufferedImage;
import java.awt.Image;
import java.io.InputStream;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;

public class ResourceUtils
{
    
    /*
     * This method should be called from the main thread of the application.
     */
    public Image loadImageFromJar(String path) throws IOException
    {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        //System.out.println("path is " + path + " classLoader is " + cl);
        URL url = cl.getResource(path);
        Image i = (BufferedImage) ImageIO.read(url);
        return i;
    }

    /*
     * This method should be called from the main thread of the application.
     */
    public InputStream getFileInputStreamFromJar(String path) 
    throws IOException
    {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        InputStream is = cl.getResourceAsStream(path);
        return is;
    }
    
    /*
     * This method should be called from the main thread of the application.
     */
    public AudioInputStream getSoundInputStreamFromJar(String path) 
    throws IOException, UnsupportedAudioFileException
    {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        InputStream is = cl.getResourceAsStream(path);
        if (is == null)
        {
            throw new IOException("could not get input stream for " + path);
        }
        AudioInputStream ais = null;
        try
        {
            ais = AudioSystem.getAudioInputStream(is);
        }
        catch (NullPointerException npe)
        {
            throw new IOException("Could not get audio input stream for " 
                    + path);
        }
        return ais;
    }
}