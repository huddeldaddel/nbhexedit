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

import java.io.*;
import java.nio.charset.Charset;

/**
 * Performs encoding / decoding between Strings and bytes using various charsets.
 * 
 * @author Thomas Werner
 */
class EncoderDecoder {
    
    private String encoding;
    
    public EncoderDecoder() {
        this(Charset.defaultCharset().name());
    }
    
    public EncoderDecoder(String encoding) {
        this.encoding = encoding;
    }
    
    public byte[] decode(String text) {
        if((null == text) || text.isEmpty())
            return new byte[0];
        
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedWriter bw = null;
        try {
            final OutputStreamWriter osw = new OutputStreamWriter(baos, encoding);
            bw = new BufferedWriter(osw);
            osw.write(text);
            bw.flush();
        } catch(Exception ex) {
        } finally {
            if(null != bw)
                try {
                    bw.close();
                } catch(Exception ex) { }
        }
        return baos.toByteArray();
    }
    
    public String encode(Object bytes) {
        if(!(bytes instanceof byte[]))
            return "";

        final StringBuilder result = new StringBuilder();
        BufferedReader br = null;
        try {
            final ByteArrayInputStream bais = new ByteArrayInputStream((byte[]) bytes);
            final InputStreamReader isr = new InputStreamReader(bais, encoding);
            br = new BufferedReader(isr);
            while(true) {
                String line = br.readLine();
                if(null == line)
                    break;
                result.append(line);
            }
        } catch(Exception ex) {
        } finally {
            if(null != br)
                try {
                    br.close();
                } catch(Exception ex) { }
        }
        return result.toString();
    }
    
}
