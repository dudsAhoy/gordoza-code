package org.jutils.ui;

import java.awt.Dialog.ModalityType;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.*;

import org.jutils.Utils;
import org.jutils.io.IOUtils;
import org.jutils.io.LogUtils;
import org.jutils.ui.app.AppRunner;
import org.jutils.ui.app.IApplication;

/*******************************************************************************
 *
 ******************************************************************************/
public class DirectoryChooser
{
    /**  */
    private final JDialog dialog;
    /**  */
    private final DirectoryTree tree;
    /**  */
    private final JLabel messageLabel;

    /***************************************************************************
     * @param owner
     **************************************************************************/
    public DirectoryChooser( Window owner )
    {
        this( owner, "Choose Directory" );
    }

    /***************************************************************************
     * @param owner Frame
     * @param title String
     **************************************************************************/
    public DirectoryChooser( Window owner, String title )
    {
        this( owner, title, "Please choose a folder or folders:" );
    }

    /***************************************************************************
     * @param owner Frame
     * @param title String
     * @param message String
     **************************************************************************/
    public DirectoryChooser( Window owner, String title, String message )
    {
        this.dialog = new JDialog( owner, title, ModalityType.APPLICATION_MODAL );
        this.tree = new DirectoryTree();
        this.messageLabel = new JLabel();

        dialog.setDefaultCloseOperation( JDialog.DISPOSE_ON_CLOSE );

        dialog.setContentPane( createContentPanel( message ) );

        dialog.setSize( 350, 550 );
        dialog.validate();
        dialog.setLocationRelativeTo( owner );
    }

    /***************************************************************************
     * @param message
     * @return
     **************************************************************************/
    private JPanel createContentPanel( String message )
    {
        JPanel mainPanel = new JPanel( new GridBagLayout() );
        JScrollPane scrollPane = new JScrollPane( tree.getView() );
        GridBagConstraints constraints;

        messageLabel.setText( message );

        scrollPane.setMinimumSize( new Dimension(
            messageLabel.getPreferredSize().width,
            messageLabel.getPreferredSize().width ) );
        scrollPane.setPreferredSize( scrollPane.getMinimumSize() );

        constraints = new GridBagConstraints( 0, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets( 8, 8,
                0, 8 ), 0, 0 );
        mainPanel.add( messageLabel, constraints );

        constraints = new GridBagConstraints( 0, 1, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets( 8,
                8, 8, 8 ), 0, 0 );
        mainPanel.add( scrollPane, constraints );

        constraints = new GridBagConstraints( 0, 2, 1, 1, 0.0, 0.0,
            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(
                8, 4, 8, 4 ), 0, 0 );
        mainPanel.add( createButtonPanel(), constraints );

        return mainPanel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createButtonPanel()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;
        JButton newButton;
        JButton okButton;
        JButton cancelButton;

        // ---------------------------------------------------------------------
        //
        // ---------------------------------------------------------------------
        newButton = new JButton();
        newButton.setText( "New Directory" );
        newButton.addActionListener( new NewDirectoryListener( this ) );

        constraints = new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets( 2, 4,
                2, 2 ), 4, 0 );
        panel.add( newButton, constraints );

        // ---------------------------------------------------------------------
        //
        // ---------------------------------------------------------------------
        okButton = new JButton();
        okButton.setText( "OK" );
        okButton.addActionListener( new OkListener( this ) );

        constraints = new GridBagConstraints( 1, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets( 2, 2,
                2, 2 ), 4, 0 );
        panel.add( okButton, constraints );

        // ---------------------------------------------------------------------
        //
        // ---------------------------------------------------------------------
        cancelButton = new JButton();
        cancelButton.setText( "Cancel" );
        cancelButton.addActionListener( new CancelListener( this ) );

        constraints = new GridBagConstraints( 2, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets( 2, 2,
                2, 4 ), 4, 0 );
        panel.add( cancelButton, constraints );

        Dimension dim = Utils.getMaxComponentSize( newButton, okButton,
            cancelButton );

        newButton.setMinimumSize( dim );
        newButton.setPreferredSize( dim );
        okButton.setMinimumSize( dim );
        okButton.setPreferredSize( dim );
        cancelButton.setMinimumSize( dim );
        cancelButton.setPreferredSize( dim );

        return panel;
    }

    /***************************************************************************
     * @param visible
     **************************************************************************/
    public void setVisible( boolean visible )
    {
        if( visible && !dialog.isVisible() )
        {
            dialog.validate();
            dialog.setLocationRelativeTo( dialog.getParent() );
        }
        dialog.setVisible( visible );
    }

    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String [] args )
    {
        IApplication app = new IApplication()
        {
            @Override
            public String getLookAndFeelName()
            {
                return null;
            }

            @Override
            public void createAndShowUi()
            {
                DirectoryChooser dialog = new DirectoryChooser( null );

                dialog.setSelectedPaths( new File( IOUtils.USERS_DIR,
                    "garbage_jfdkslfjsdl" ).getAbsolutePath() );

                dialog.setVisible( true );

                String paths = dialog.getSelectedPaths();
                LogUtils.printDebug( paths );
            }
        };

        SwingUtilities.invokeLater( new AppRunner( app ) );
    }

    /***************************************************************************
     * @return File[]
     **************************************************************************/
    public File [] getSelected()
    {
        return tree.getSelected();
    }

    /***************************************************************************
     * @param files File[]
     **************************************************************************/
    public void setSelected( File [] files )
    {
        tree.setSelected( files );
    }

    /***************************************************************************
     * @param paths String
     **************************************************************************/
    public void setSelectedPaths( String paths )
    {
        tree.setSelectedPaths( paths );
    }

    /***************************************************************************
     * @return String
     **************************************************************************/
    public String getSelectedPaths()
    {
        return tree.getSelectedPaths();
    }

    public void setSize( int width, int height )
    {
        dialog.setSize( width, height );
    }

    public void setTitle( String title )
    {
        dialog.setTitle( title );
    }

    public void setMessage( String message )
    {
        messageLabel.setText( message );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class CancelListener implements ActionListener
    {
        private final DirectoryChooser chooser;

        public CancelListener( DirectoryChooser dialog )
        {
            this.chooser = dialog;
        }

        public void actionPerformed( ActionEvent e )
        {
            chooser.tree.clearSelection();
            chooser.dialog.dispose();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class OkListener implements ActionListener
    {
        private final DirectoryChooser chooser;

        public OkListener( DirectoryChooser dialog )
        {
            this.chooser = dialog;
        }

        public void actionPerformed( ActionEvent e )
        {
            chooser.dialog.dispose();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class NewDirectoryListener implements ActionListener
    {
        private final DirectoryChooser chooser;

        public NewDirectoryListener( DirectoryChooser dialog )
        {
            this.chooser = dialog;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            File [] selectedFiles = chooser.tree.getSelected();

            if( selectedFiles != null && selectedFiles.length == 1 )
            {
                String name = JOptionPane.showInputDialog( chooser.dialog,
                    "Enter the new directory name:", "New Directory Name",
                    JOptionPane.QUESTION_MESSAGE );

                if( name != null )
                {
                    File parentDir = selectedFiles[0];
                    File newDir = new File( parentDir, name );

                    if( !newDir.exists() )
                    {
                        if( !newDir.mkdir() )
                        {
                            JOptionPane.showMessageDialog(
                                chooser.dialog,
                                "Please check the permissions on the parent directory.",
                                "Cannot Create New Directory",
                                JOptionPane.ERROR_MESSAGE );
                        }
                        else
                        {
                            chooser.tree.refreshSelected();
                        }
                    }
                    else
                    {
                        JOptionPane.showMessageDialog( chooser.dialog,
                            "The directory or file, '" + name +
                                "', already exists.",
                            "Cannot Create New Directory",
                            JOptionPane.ERROR_MESSAGE );
                    }
                }
            }
            else
            {
                JOptionPane.showMessageDialog(
                    chooser.dialog,
                    "Please choose only one directory when creating a sub-directory.",
                    "Cannot Create New Directory", JOptionPane.ERROR_MESSAGE );
            }
        }
    }
}
