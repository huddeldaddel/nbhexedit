/*
 * Copyright (c) 2011, Thomas Werner
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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.fife.ui.hex.event.HexEditorEvent;
import org.fife.ui.hex.event.HexEditorListener;
import org.fife.ui.hex.swing.HexEditor;
import org.openide.cookies.SaveCookie;
import org.openide.loaders.DataObject;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;

/**
 *
 * @author devx
 */
class HexEditorNode extends AbstractNode implements HexEditorListener {

    private final SaveCookie saveCookie;
    private final HexEditor hexEditor;

    private boolean opened = false;
    private DataObject dataObject;

    public HexEditorNode(HexEditor hexEditor) {
        super(Children.LEAF);
        saveCookie = new SaveCookie() {
            @Override
            public void save() throws IOException {
                saveChanges();
                setModified(false);
            }
        };

        this.hexEditor = hexEditor;
        this.hexEditor.addHexEditorListener(this);
    }

    public void setDataObject(DataObject dataObject) {
        this.dataObject = dataObject;
        openFile(dataObject);
    }

    public void setModified(boolean modified) {
        if(modified)
            getCookieSet().assign(SaveCookie.class, saveCookie);
        else
            getCookieSet().assign(SaveCookie.class);
    }

    @Override
    public void hexBytesChanged(HexEditorEvent hee) {
        if(opened)
            setModified(true);
    }

    private void saveChanges() {
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
		} catch (IOException ioe) {
		} finally {
            if(null != out)
                try {
                   out.close();
                } catch (IOException ex) { }
        }
    }

    private void openFile(DataObject dataObject) {
        InputStream in = null;
		try {
            in = dataObject.getPrimaryFile().getInputStream();
			if(in != null) {
				hexEditor.open(new BufferedInputStream(in));
                hexEditor.setEnabled(dataObject.getPrimaryFile().canWrite());
                opened = true;
            }
		} catch (IOException ioe) {
		} finally {
            if(null != in)
                try {
                   in.close();
                } catch (IOException ex) { }
        }
    }

}
