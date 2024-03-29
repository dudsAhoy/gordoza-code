package org.jutils.ui.validators;

import java.util.List;

import org.jutils.ui.hex.HexUtils;
import org.jutils.ui.validation.ValidationException;

/*******************************************************************************
 * 
 ******************************************************************************/
public class HexBytesValidator implements IDataValidator<byte []>
{
    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public byte [] validate( String text ) throws ValidationException
    {
        List<Byte> bytes;
        byte [] bytearray = null;

        text.replaceAll( "\\s", "" );

        try
        {
            bytes = HexUtils.fromHexString( text );
        }
        catch( NumberFormatException ex )
        {
            throw new ValidationException( ex.getMessage(), ex );
        }

        if( bytes.size() > 0 )
        {
            bytearray = new byte[bytes.size()];

            for( int i = 0; i < bytes.size(); i++ )
            {
                bytearray[i] = bytes.get( i );
            }
        }
        else
        {
            throw new ValidationException( "Empty string." );
        }

        return bytearray;
    }
}
