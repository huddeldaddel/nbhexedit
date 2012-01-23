/*
 * Copyright (c) 2012, Thomas Werner
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 *
 * - Redistributions of source code must retain the above copyright notice, this list of conditions and the following
 *   disclaimer.
 * - Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following
 *   disclaimer in the documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package de.bfg9000.hexeditor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedMap;
import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import de.bfg9000.hexeditor.ui.hex.event.HexEditorEvent;
import de.bfg9000.hexeditor.ui.hex.event.HexEditorListener;
import de.bfg9000.hexeditor.ui.hex.swing.HexEditor;
import org.netbeans.api.queries.FileEncodingQuery;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.UndoRedo;
import org.openide.loaders.DataObject;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 * Top component which displays a file in a Hex Editor component.
 * 
 * @author Thomas Werner
 */
@ConvertAsProperties(dtd = "-//de.bfg9000.hexeditor//HexEditor//EN", autostore = false)
@TopComponent.Description(preferredID = "HexEditorTopComponent",
                        //iconBase="SET/PATH/TO/ICON/HERE",
                          persistenceType = TopComponent.PERSISTENCE_NEVER)
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@TopComponent.OpenActionRegistration(displayName = "#CTL_HexEditorAction", preferredID = "HexEditorTopComponent")
public final class HexEditorTopComponent extends TopComponent implements UndoRedo.Provider, HexEditorListener {

    private final HexEditorNode node;
    private final HexEditor hexEditor;
    private final UndoRedo.Manager undoRedo;
    private final Savable savable;
    private final InstanceContent content = new InstanceContent();
    
    private boolean opened = false;
    protected String displayName;
    
    public HexEditorTopComponent() {
        initComponents();

        hexEditor = new HexEditor();
        hexEditor.addHexEditorListener(this);
        add(hexEditor, BorderLayout.CENTER);

        node = new HexEditorNode(hexEditor);
        content.add(node);
        savable = new Savable(this, hexEditor);
        
        setActivatedNodes(new Node[]{node});

        undoRedo = new UndoRedo.Manager();
        hexEditor.setUndoManager(undoRedo);
        
        setName(NbBundle.getMessage(HexEditorTopComponent.class, "CTL_HexEditorTopComponent"));
        setToolTipText(NbBundle.getMessage(HexEditorTopComponent.class, "HINT_HexEditorTopComponent"));
        
        cmbEncoding.setModel(new DefaultComboBoxModel(getSupportedEncodings()));
        
        getActionMap().put("copy-to-clipboard", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hexEditor.copy();
            }
        });
        getActionMap().put("cut-to-clipboard", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hexEditor.cut();
            }
        });
        getActionMap().put("paste-from-clipboard", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hexEditor.paste();
            }
        });   
        
        associateLookup(new AbstractLookup(content));
    }
    
    @Override
    public boolean canClose() {
        final Savable savable = getLookup().lookup(Savable.class);
        if(null == savable)
            return true;
            
        final Component parent = WindowManager.getDefault().getMainWindow();
        final Object[] options = new Object[]{ "Save", "Discard", "Cancel" };
        final String message = "File " +displayName +" is modified. Save?";
        final int choice = JOptionPane.showOptionDialog(parent, message, "Question", JOptionPane.YES_NO_CANCEL_OPTION, 
                           JOptionPane.QUESTION_MESSAGE, null, options, JOptionPane.YES_OPTION);
        if(JOptionPane.CANCEL_OPTION == choice)
            return false;
        
        if(JOptionPane.YES_OPTION == choice)
            try {
                savable.handleSave();
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        
        return true;
    }
    
    @Override
    public void hexBytesChanged(HexEditorEvent hee) {
        setModified(true);
    }
    
    void setModified(boolean modified) {
        if(modified && opened) {
            savable.activate();
            content.add(savable);
            setHtmlDisplayName("<html><b>" +displayName +"</b></html>");
        } else {
            savable.deactivate();
            content.remove(savable);
            setHtmlDisplayName(displayName);
        }
    }
    
    public void openDataObject(DataObject dataObject) {
        displayName = dataObject.getPrimaryFile().getNameExt();
        setHtmlDisplayName(displayName);
        node.openFile(dataObject);
        savable.setDataObject(dataObject);
        opened = true;
        
        final Charset encoding = FileEncodingQuery.getEncoding(dataObject.getPrimaryFile()); 
        cmbEncoding.setSelectedItem(encoding.name());        
        hexEditor.setEncoding(encoding.name());
    }
    
    @Override
    public UndoRedo getUndoRedo() {
        return undoRedo;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlToolbar = new javax.swing.JPanel();
        lblEncoding = new javax.swing.JLabel();
        cmbEncoding = new javax.swing.JComboBox();

        setLayout(new java.awt.BorderLayout());

        pnlToolbar.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        org.openide.awt.Mnemonics.setLocalizedText(lblEncoding, org.openide.util.NbBundle.getMessage(HexEditorTopComponent.class, "HexEditorTopComponent.lblEncoding.text")); // NOI18N
        pnlToolbar.add(lblEncoding);

        cmbEncoding.setPreferredSize(new java.awt.Dimension(200, 20));
        cmbEncoding.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbEncodingActionPerformed(evt);
            }
        });
        pnlToolbar.add(cmbEncoding);

        add(pnlToolbar, java.awt.BorderLayout.NORTH);
    }// </editor-fold>//GEN-END:initComponents

    private void cmbEncodingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbEncodingActionPerformed
        hexEditor.setEncoding(cmbEncoding.getSelectedItem().toString());
    }//GEN-LAST:event_cmbEncodingActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cmbEncoding;
    private javax.swing.JLabel lblEncoding;
    private javax.swing.JPanel pnlToolbar;
    // End of variables declaration//GEN-END:variables
    
    @Override
    public void componentOpened() {  }

    @Override
    public void componentClosed() { }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
    }
 
    private Object[] getSupportedEncodings() {
        final SortedMap<String, Charset> availableCharsets = Charset.availableCharsets();
        final List<String> encodings = new ArrayList<String>(availableCharsets.keySet());
        Collections.sort(encodings);
        return encodings.toArray();
    }
    
}
