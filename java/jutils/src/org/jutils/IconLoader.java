package org.jutils;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import javax.swing.ImageIcon;

/*******************************************************************************
 * Defines methods of loading icons that reside either in a directory or
 * relative to a class file. Aids the developer in accessing icons in a manner
 * independent of whether the class files are in a directory or a jar file. All
 * icons must be copied to the directory or jar file by the build tool.
 ******************************************************************************/
public class IconLoader
{
    /** The URL at which all the icons reside. */
    private final URL baseUrl;
    /**
     * The icon cache so that icons are not loaded more than once per instance
     * of this class.
     */
    private final Map<String, ImageIcon> iconMap;

    /***************************************************************************
     * @param basePath
     * @throws MalformedURLException
     **************************************************************************/
    public IconLoader( File basePath ) throws MalformedURLException
    {
        this( basePath.toURI().toURL() );
    }

    /***************************************************************************
     * @param baseClass
     * @param relative
     **************************************************************************/
    public IconLoader( Class<?> baseClass, String relative )
    {
        this( Utils.loadResourceURL( baseClass,
            relative.endsWith( "/" ) ? relative : relative + "/" ) );
    }

    /***************************************************************************
     * @param url
     **************************************************************************/
    public IconLoader( URL url )
    {
        baseUrl = url;
        iconMap = new HashMap<String, ImageIcon>();
    }

    /***************************************************************************
     * @param str String
     * @return ImageIcon
     **************************************************************************/
    public ImageIcon getIcon( String str )
    {
        ImageIcon icon = null;

        if( iconMap.containsKey( str ) )
        {
            icon = iconMap.get( str );
        }
        else
        {
            icon = new ImageIcon( getIconUrl( str ) );
            iconMap.put( str, icon );
        }

        return icon;
    }

    /***************************************************************************
     * @param names
     * @return
     **************************************************************************/
    public List<ImageIcon> getIcons( String... names )
    {
        List<ImageIcon> icons = new ArrayList<ImageIcon>();

        for( String name : names )
        {
            icons.add( getIcon( name ) );
        }

        return icons;
    }

    /***************************************************************************
     * @param str String
     * @return URL
     **************************************************************************/
    public URL getIconUrl( String str )
    {
        URL url;
        try
        {
            url = new URL( baseUrl.toString() + str );
        }
        catch( MalformedURLException ex )
        {
            throw new RuntimeException( ex );
        }

        // System.out.println( "Base URL: " + baseUrl.getPath() );
        // System.out.println( "URL is " + url.getPath() );

        return url;
    }

    /***************************************************************************
     * @param str
     * @return
     * @throws IOException
     **************************************************************************/
    public Image getImage( String str )
    {
        return getIcon( str ).getImage();
    }

    /***************************************************************************
     * @param names
     * @return
     **************************************************************************/
    public List<Image> getImages( String... names )
    {
        List<Image> images = new ArrayList<Image>();

        for( String name : names )
        {
            images.add( getImage( name ) );
        }

        return images;
    }
}