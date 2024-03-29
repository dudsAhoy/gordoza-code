package org.jutils.io;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.thoughtworks.xstream.XStreamException;

/*******************************************************************************
 * Default serializer for user options.
 * @param <T> The type of options to be serialized.
 ******************************************************************************/
public class OptionsSerializer<T>
{
    /**
     * The class to use to create default options when the options cannot be
     * read from file.
     */
    private final IOptionsCreator<T> creator;
    /** The file from which the options will be read and written. */
    private final File file;

    /** The last options read/written. */
    private T options;

    /***************************************************************************
     * Creates a serializer that will read/write options of type {@code T} to
     * the given file, using the creator when a default set of options are
     * required.
     * @param creator The class to use to create default options when the
     * options cannot be read from file.
     * @param file The file from which the options will be read and written.
     **************************************************************************/
    public OptionsSerializer( IOptionsCreator<T> creator, File file )
    {
        this.creator = creator;
        this.file = file;
    }

    /***************************************************************************
     * Creates the directories for this file if necessary and returns
     * {@code true} if they exist as of the return of this function.
     **************************************************************************/
    public boolean ensureDirectoryExists()
    {
        File dir = file.getParentFile();

        if( !dir.isDirectory() )
        {
            return dir.mkdirs();
        }

        return true;
    }

    /***************************************************************************
     * Returns the last options read/written.
     **************************************************************************/
    public T getOptions()
    {
        if( options == null )
        {
            options = read();
        }

        return options;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public T getDefault()
    {
        return creator.createDefaultOptions();
    }

    /***************************************************************************
     * Reads the options from file, caching the options before returns them.
     **************************************************************************/
    public T read()
    {
        Object obj = null;

        options = null;

        if( file.isFile() )
        {
            try
            {
                options = getDefault();

                obj = XStreamUtils.readObjectXStream( file );
            }
            catch( FileNotFoundException ex )
            {
                options = getDefault();
                write();
                creator.warn( " User options file does not exist: " +
                    file.getAbsolutePath() );
            }
            catch( IOException ex )
            {
                options = getDefault();
                write();
                ex.printStackTrace();
            }
            catch( XStreamException ex )
            {
                file.renameTo( new File(
                    file.getAbsoluteFile().getParentFile(), file.getName() +
                        ".broken" ) );
                options = getDefault();
                write();
                creator.warn( "User options file is out of date: " +
                    file.getAbsolutePath() + " because: " + ex.getMessage() );
            }
        }
        else
        {
            creator.warn( "File does not exist and will be created: " +
                file.getAbsolutePath() );
        }

        if( obj != null )
        {
            if( obj.getClass().equals( options.getClass() ) )
            {
                @SuppressWarnings( "unchecked")
                T t = ( T )obj;
                options = creator.initialize( t );
                // options = t;
            }
            else
            {
                creator.warn( "Existing user options are of type " +
                    obj.getClass().getName() +
                    " and are not assignable to the type " + options.getClass() );
                options = getDefault();
            }
        }
        else
        {
            options = getDefault();
            write();
        }

        return options;
    }

    /***************************************************************************
     * Writes the last read/written options to file.
     **************************************************************************/
    public void write()
    {
        if( options != null )
        {
            write( options );
        }
    }

    /***************************************************************************
     * Caches the given options and writes to file.
     * @param data the data to be cached and written.
     **************************************************************************/
    public void write( T data )
    {
        this.options = data;

        try
        {
            XStreamUtils.writeObjectXStream( data, file );
        }
        catch( IOException ex )
        {
            creator.warn( "Unable to write options because of an I/O error: " +
                ex.getMessage() );
        }
        catch( XStreamException ex )
        {
            creator.warn( "Unable to write options because of an serialization error: " +
                ex.getMessage() );
        }
    }

    /***************************************************************************
     * Interface used to create a default set of options when an error results
     * during a read from file.
     * @param <T> The type of options to be created.
     **************************************************************************/
    public static interface IOptionsCreator<T>
    {
        /** Creates a default set of options. */
        public T createDefaultOptions();

        /** Called to initialize fields that may have been read as null. */
        public T initialize( T item_read );

        /** Called if there is any issue in reading or writing the file. */
        public void warn( String message );
    }

    /***************************************************************************
     * @param <T>
     **************************************************************************/
    public static class DefaultOptionsCreator<T> implements
        IOptionsCreator<T>
    {
        private final Constructor<T> dataConstructor;

        public DefaultOptionsCreator( Class<T> cls )
        {
            try
            {
                this.dataConstructor = cls.getConstructor();
            }
            catch( NoSuchMethodException ex )
            {
                throw new IllegalArgumentException( cls.getName() +
                    " has no default constructor", ex );
            }
            catch( SecurityException ex )
            {
                throw new IllegalArgumentException( cls.getName() +
                    " has no accesible constructor", ex );
            }
        }

        @Override
        public T createDefaultOptions()
        {
            try
            {
                return dataConstructor.newInstance();
            }
            catch( InstantiationException ex )
            {
                throw new IllegalStateException( ex );
            }
            catch( IllegalAccessException ex )
            {
                throw new IllegalStateException( ex );
            }
            catch( IllegalArgumentException ex )
            {
                throw new IllegalStateException( ex );
            }
            catch( InvocationTargetException ex )
            {
                throw new IllegalStateException( ex );
            }
        }

        @Override
        public T initialize( T item_read )
        {
            return item_read;
        }

        @Override
        public void warn( String message )
        {
            LogUtils.printWarning( message );
        }
    }

    /***************************************************************************
     * Creates an options serializer with the specified file and options creator
     * by ensuring the directory structure exists for the file before creation.
     * @param creator the default creator to be used.
     * @param file the file to be used for serialization.
     * @return the new options serializer.
     **************************************************************************/
    public static <T> OptionsSerializer<T> getUserIO(
        IOptionsCreator<T> creator, File file )
    {
        if( !IOUtils.ensureParentExists( file ) )
        {
            LogUtils.printWarning( "User options directory cannot be created: " +
                file.getParentFile().getAbsolutePath() );
        }

        return new OptionsSerializer<T>( creator, file );
    }

    /***************************************************************************
     * @param <T>
     **************************************************************************/
    public static <T> OptionsSerializer<T> getUserIO( Class<T> cls,
        File file )
    {
        return getUserIO( new DefaultOptionsCreator<T>( cls ), file );
    }
}
