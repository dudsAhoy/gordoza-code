package org.jutils.ui.fields;

import java.awt.Component;

import javax.swing.JTextField;

import org.jutils.ui.event.updater.IUpdater;
import org.jutils.ui.hex.HexUtils;
import org.jutils.ui.validation.ValidationTextView;
import org.jutils.ui.validators.*;

/*******************************************************************************
 * Defines an {@link IFormField} that contains a byte [] validater.
 ******************************************************************************/
public class HexBytesFormField implements IDataFormField<byte []>
{
    /**  */
    private final String name;
    /**  */
    private final ValidationTextView textField;

    /**  */
    private IUpdater<byte []> updater;
    /**  */
    private byte [] value;

    /***************************************************************************
     * @param name
     * @param units
     * @param columns
     **************************************************************************/
    public HexBytesFormField( String name )
    {
        this( name, null );
    }

    /***************************************************************************
     * @param name
     * @param updater
     **************************************************************************/
    public HexBytesFormField( String name, IUpdater<byte []> updater )
    {
        this( name, 20, updater );
    }

    /***************************************************************************
     * @param name
     * @param units
     * @param columns
     * @param updater
     **************************************************************************/
    public HexBytesFormField( String name, int columns,
        IUpdater<byte []> updater )
    {
        this.name = name;
        this.textField = new ValidationTextView( null, columns );
        this.updater = updater;

        ITextValidator textValidator;

        textValidator = new DataTextValidator<>( new HexBytesValidator(),
            new ValueUpdater( this ) );
        textField.getField().setValidator( textValidator );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String getFieldName()
    {
        return name;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Component getField()
    {
        return textField.getView();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public byte [] getValue()
    {
        return value;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setValue( byte [] value )
    {
        this.value = value;

        String text = value == null ? "" : HexUtils.toHexString( value );
        textField.setText( text );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setUpdater( IUpdater<byte []> updater )
    {
        this.updater = updater;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public IUpdater<byte []> getUpdater()
    {
        return updater;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public IValidationField getValidationField()
    {
        return textField.getField();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public JTextField getTextField()
    {
        return textField.getField().getView();
    }

    /***************************************************************************
     * @param editable
     **************************************************************************/
    public void setEditable( boolean editable )
    {
        textField.getField().setEditable( editable );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class ValueUpdater implements IUpdater<byte []>
    {
        private final HexBytesFormField view;

        public ValueUpdater( HexBytesFormField view )
        {
            this.view = view;
        }

        @Override
        public void update( byte [] data )
        {
            view.value = data;

            if( view.updater != null )
            {
                view.updater.update( data );
            }
        }
    }
}
