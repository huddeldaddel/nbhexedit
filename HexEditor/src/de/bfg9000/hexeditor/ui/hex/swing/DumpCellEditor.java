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
package de.bfg9000.hexeditor.ui.hex.swing;

import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultCellEditor;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

class DumpCellEditor extends DefaultCellEditor {
    
    private static final long serialVersionUID = 1L;
    
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
        return delegate.getCellEditorValue();
    }
    
    private static final class EditorField extends JTextField {
        
        private static final long serialVersionUID = 1L;
        
        public EditorField() {
            setDocument(new EditorDocument());
        }
        
    }
    
    private static final class EditorDocument extends PlainDocument {
        
        private static final long serialVersionUID = 1L;
        
        private ArrayList<Byte> byteData;
        private EncoderDecoder encoder;
        
        @Override
        public void insertString(int offset, String str, AttributeSet attr)
                    throws BadLocationException {
            if((byteData != null) && (encoder != null)) {
                final String start = getText(0, offset);
                final byte[] toBeAdded = encoder.decode(str);
                final int byteOffset = encoder.decode(start).length;
                for(int i=0; i<toBeAdded.length; i++)
                    byteData.add(byteOffset +i, toBeAdded[i]);
            }
            
            super.insertString(offset, str, attr);            
        }
        
        @Override
        public void remove(int offs, int len) throws BadLocationException {
            if((byteData != null) && (encoder != null)) {
                final String start = getText(0, offs);
                final String toBeRemoved = getText(offs, len);
                final int byteOffset = encoder.decode(start).length;
                final int byteLength = encoder.decode(toBeRemoved).length;
                for(int i=0; i<byteLength; i++)
                    byteData.remove(byteOffset);
            }
            
            super.remove(offs, len);
        }
        
        @Override
        public void replace(int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            super.replace(offset, length, text, attrs);
        }
        
    }
    
    private final class ByteConverterDelegate extends EditorDelegate {
        
        private static final long serialVersionUID = 1L;
        
        @Override
        public void setValue(Object value) {
            final EditorField editor = (EditorField) editorComponent;
            final EditorDocument document = (EditorDocument)editor.getDocument();
            editor.setText(encoder.encode(value));
            document.byteData = toList((byte[]) value);
            document.encoder = encoder;
        }

        @Override
        public Object getCellEditorValue() {
            final EditorField editor = (EditorField) editorComponent;
            final EditorDocument document = (EditorDocument)editor.getDocument();
            return toArray(document.byteData);
        }
        
        private byte[] toArray(List<Byte> data) {
            final byte[] result = new byte[data.size()];
            for(int i=0; i<data.size(); i++)
                result[i] = data.get(i);
            return result;
        }
        
        private ArrayList<Byte> toList(byte[] data) {
            final ArrayList<Byte> result = new ArrayList<Byte>(data.length);
            for(int i=0; i<data.length; i++)
                result.add(data[i]);
            return result;
        }        
    
    }
    
}

