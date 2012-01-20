/*
 * Copyright (c) 2011 Robert Futrell
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name "HexEditor" nor the names of its contributors may
 *       be used to endorse or promote products derived from this software
 *       without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY ''AS IS'' AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
 * EVENT SHALL THE CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.fife.ui.hex.swing;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.io.StringReader;


/**
 * A <code>Transferable</code> that transfers an array of bytes.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class ByteArrayTransferable implements Transferable {

	private int offset;
	private byte[] bytes;

	private static final DataFlavor[] FLAVORS = {
		DataFlavor.stringFlavor,
		DataFlavor.plainTextFlavor,
	};


	/**
	 * Creates a transferable object.
	 *
	 * @param bytes The bytes to transfer.
	 */
	public ByteArrayTransferable(int offset, byte[] bytes) {
		this.offset = offset;
		if (bytes!=null) {
			this.bytes = (byte[])bytes.clone();
		}
		else {
			this.bytes = new byte[0];
		}
	}


	/**
	 * Returns the number of bytes being transferred.
	 *
	 * @return The number of bytes being transferred.
	 * @see #getOffset()
	 */
	public int getLength() {
		return bytes.length;
	}


	/**
	 * Returns the offset of the first byte being transferred.
	 *
	 * @return The offset of the first byte.
	 * @see #getLength()
	 */
	public int getOffset() {
		return offset;
	}


	/**
	 * Returns the data being transferred in a format specified by the <code>DataFlavor</code>.
	 *
	 * @param flavor Dictates in what format the data should be returned.
	 * @throws UnsupportedFlavorException If the specified flavor is not supported.
	 * @throws IOException If an IO error occurs.
	 * @see DataFlavor#getRepresentationClass()
	 */
    @Override
	public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
		if(flavor.equals(FLAVORS[0]))
			return new String(bytes); // Use platform default charset.

		if(flavor.equals(FLAVORS[1]))
			return new StringReader(new String(bytes));

	    throw new UnsupportedFlavorException(flavor);
	}


	/**
	 * Returns an array of DataFlavor objects indicating the flavors the data can be provided in. The array is ordered
     * according to preference for providing the data (from most richly descriptive to least descriptive).
	 *
	 * @return An array of data flavors in which this data can be transferred.
	 */
    @Override
	public DataFlavor[] getTransferDataFlavors() {
		return FLAVORS.clone();
	}


	/**
	 * Returns whether a data flavor is supported.
	 *
	 * @param flavor The flavor to check.
	 * @return Whether the specified flavor is supported.
	 */
    @Override
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		for(int i=0; i<FLAVORS.length; i++)
			if (flavor.equals(FLAVORS[i]))
				return true;

		return false;
	}

}