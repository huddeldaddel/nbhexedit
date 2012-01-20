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

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.fife.ui.hex.swing.HexEditor;
import org.netbeans.spi.actions.AbstractSavable;
import org.openide.loaders.DataObject;

/**
 *
 * @author Thomas Werner
 */
class Savable extends AbstractSavable {

    private DataObject dataObject;
    private final HexEditorTopComponent component;
    private final HexEditor hexEditor;
    
    public Savable(HexEditorTopComponent component, HexEditor hexEditor) {
        this.hexEditor = hexEditor;
        this.component = component;
    }
    
    public void activate() {
        register();
    }
    
    public void deactivate() {
        unregister();
    }
    
    public void setDataObject(DataObject dataObject) {
        this.dataObject = dataObject;
    }
    
    @Override
    protected String findDisplayName() {
        return null == dataObject ? "<unknown file>" : dataObject.getPrimaryFile().getName();
    }

    @Override
    public boolean equals(Object o) {
        return o == this;
    }

    @Override
    public int hashCode() {
        return System.identityHashCode(this);
    }
    
    @Override
    protected void handleSave() throws IOException {
        if(null == dataObject)
            return;

        OutputStream out = null;
        try {
            out = new BufferedOutputStream(dataObject.getPrimaryFile().getOutputStream());
            if(out != null) {
                final int count = hexEditor.getByteCount();
                for(int i=0; i<count; i++)
                    out.write(hexEditor.getByte(i));
                out.flush();
            }
            component.setModified(false);
        } catch (IOException ioe) {
        } finally {
            if(null != out)
                try {
                   out.close();
                } catch (IOException ex) { }
        }
    }
    
}
