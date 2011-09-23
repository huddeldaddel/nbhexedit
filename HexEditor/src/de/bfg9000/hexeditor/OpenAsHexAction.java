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

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import org.openide.loaders.DataObject;
import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.Mode;
import org.openide.windows.WindowManager;

@ActionID(category = "File", id = "de.bfg9000.hexeditor.OpenAsHexAction")
@ActionRegistration(displayName = "#CTL_OpenAsHexAction")
@ActionReferences({
    @ActionReference(path = "Loaders/application/pdf/Actions", position = 175),
    @ActionReference(path = "Loaders/application/x-class-file/Actions", position = 175),
    @ActionReference(path = "Loaders/application/x-java-archive/Actions", position = 175),
    @ActionReference(path = "Loaders/application/xml/Actions", position = 175),
    @ActionReference(path = "Loaders/application/xslt+xml/Actions", position = 175),
    @ActionReference(path = "Loaders/content/unknown/Actions", position = 175),
    @ActionReference(path = "Loaders/image/png-gif-jpeg-bmp/Actions", position = 175),
    @ActionReference(path = "Loaders/text/dtd-xml/Actions", position=175),
    @ActionReference(path = "Loaders/text/html/Actions", position=175),
    @ActionReference(path = "Loaders/text/javascript/Actions", position=175),
    @ActionReference(path = "Loaders/text/url/Actions", position=175),
    @ActionReference(path = "Loaders/text/x-ant+xml/Actions", position=175),
    @ActionReference(path = "Loaders/text/x-css/Actions", position=175),
    @ActionReference(path = "Loaders/text/x-dbschema+xml/Actions", position=175),
    @ActionReference(path = "Loaders/text/x-diff/Actions", position=175),
    @ActionReference(path = "Loaders/text/x-dtd/Actions", position=175),
    @ActionReference(path = "Loaders/text/x-form/Actions", position=175),
    @ActionReference(path = "Loaders/text/x-fx/Actions", position=175),
    @ActionReference(path = "Loaders/text/x-hibernate-cfg+xml/Actions", position=175),
    @ActionReference(path = "Loaders/text/x-hibernate-mapping+xml/Actions", position=175),
    @ActionReference(path = "Loaders/text/x-hibernate-reveng+xml/Actions", position=175),
    @ActionReference(path = "Loaders/text/x-java/Actions", position=175),
    @ActionReference(path = "Loaders/text/x-jnlp+xml/Actions", position=175),
    @ActionReference(path = "Loaders/text/x-json/Actions", position=175),
    @ActionReference(path = "Loaders/text/x-jsp/Actions", position=175),
    @ActionReference(path = "Loaders/text/x-manifest/Actions", position=175),
    @ActionReference(path = "Loaders/text/x-maven-pom+xml/Actions", position=175),
    @ActionReference(path = "Loaders/text/x-nbs/Actions", position=175),
    @ActionReference(path = "Loaders/text/x-netbeans-layer+xml/Actions", position=175),
    @ActionReference(path = "Loaders/text/x-palette-item/Actions", position=175),
    @ActionReference(path = "Loaders/text/x-persistence1.0/Actions", position=175),
    @ActionReference(path = "Loaders/text/x-properties/Actions", position=175),
    @ActionReference(path = "Loaders/text/x-springconfig+xml/Actions", position=175),
    @ActionReference(path = "Loaders/text/x-sql/Actions", position=175),
    @ActionReference(path = "Loaders/text/x-yaml/Actions", position=175),
    @ActionReference(path = "Loaders/text/xhtml/Actions", position=175),
    @ActionReference(path = "Loaders/text/xml/Actions", position=175)
        
})
@Messages("CTL_OpenAsHexAction=Open as Hex")
public final class OpenAsHexAction implements ActionListener {

    private final DataObject context;

    public OpenAsHexAction(DataObject context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        final Mode editorMode = WindowManager.getDefault().findMode("editor");
        if(null == editorMode)
            return;
        
        final HexEditorTopComponent hexEditor = new HexEditorTopComponent();
        editorMode.dockInto(hexEditor);

        hexEditor.openDataObject(context);
        hexEditor.open();
        hexEditor.requestActive();
    }
}
