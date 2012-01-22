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

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * Encodes an Array of bytes using a named Charset.
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
