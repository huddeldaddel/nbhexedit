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
package org.fife.ui.hex.swing;

import javax.swing.DefaultCellEditor;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

class DumpCellEditor extends DefaultCellEditor {
    
    private EncoderDecoder encoder;
    
    public DumpCellEditor(EncoderDecoder encoder) {
        super(new EditorField());
        delegate = new ByteConverterDelegate();
        this.encoder = encoder;
    }
    
    public void setEncoder(EncoderDecoder encoder) {
        this.encoder = encoder;
    }
    
    @Override
    public Object getCellEditorValue() {
        return null;
    }
    
    private static final class EditorField extends JTextField {
        
        public EditorField() {
            setDocument(new EditorDocument());
        }
        
    }
    
    private static final class EditorDocument extends PlainDocument {
        
        @Override
        public void insertString(int offset, String str, AttributeSet attr)
                    throws BadLocationException {
            if(str == null)
                return;

            super.insertString(offset, str, attr);
        }
        
        @Override
        public void remove(int offs, int len) throws BadLocationException {
            super.remove(offs, len);
        }
        
        @Override
        public void replace(int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if(length == 0 && (text == null || text.length() == 0))
                return;
        
        }
        
    }
    
    private final class ByteConverterDelegate extends EditorDelegate {
        
        @Override
        public void setValue(Object value) {
            ((EditorField)editorComponent).setText(encoder.encode(value));
        }

        @Override
        public Object getCellEditorValue() {
            return ((EditorField)editorComponent).getText();
        }        
    
    }
    
}

