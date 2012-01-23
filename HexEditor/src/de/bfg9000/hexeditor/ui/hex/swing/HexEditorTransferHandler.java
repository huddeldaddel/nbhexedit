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
package de.bfg9000.hexeditor.ui.hex.swing;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import javax.swing.JComponent;
import javax.swing.TransferHandler;


/**
 * The default transfer handler for <code>HexEditor</code>s.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class HexEditorTransferHandler extends TransferHandler {

	private static final long serialVersionUID = 1L;


    @Override
	public boolean canImport(JComponent comp, DataFlavor[] flavors) {
		HexEditor editor = (HexEditor)comp;
		if (!editor.isEnabled()) {
			return false;
		}
		return getImportFlavor(flavors, editor)!=null;
	}


    @Override
	protected Transferable createTransferable(JComponent c) {
		HexEditor e = (HexEditor)c;
		int start = e.getSmallestSelectionIndex();
		int end = e.getLargestSelectionIndex();
		byte[] array = new byte[end-start+1];
		for (int i=end; i>=start; i--) {
			array[i-start] = e.getByte(i);
		}
		ByteArrayTransferable bat = new ByteArrayTransferable(start, array);
		return bat;
	}


    @Override
	protected void exportDone(JComponent source, Transferable data, int action){
		if (action==MOVE) {
			ByteArrayTransferable bat = (ByteArrayTransferable)data;
			int offs = bat.getOffset();
			HexEditor e = (HexEditor)source;
			e.removeBytes(offs, bat.getLength());
		}
	}


	private DataFlavor getImportFlavor(DataFlavor[] flavors, HexEditor e) {
		for (int i=0; i<flavors.length; i++) {
			if (flavors[i].equals(DataFlavor.stringFlavor)) {
				return flavors[i];
			}
		}
		return null;
	}


	/**
	 * Returns what operations can be done on a hex editor (copy and move, or
	 * just copy).
	 *
	 * @param c The <code>HexEditor</code>.
	 * @return The permitted operations.
	 */
    @Override
	public int getSourceActions(JComponent c) {
		HexEditor e = (HexEditor)c;
		return e.isEnabled() ? COPY_OR_MOVE : COPY;
	}


	/**
	 * Imports data into a hex editor component.
	 *
	 * @param c The <code>HexEditor</code> component.
	 * @param t The data to be imported.
	 * @return Whether the data was successfully imported.
	 */
    @Override
	public boolean importData(JComponent c, Transferable t) {
		final HexEditor e = (HexEditor)c;
		final DataFlavor flavor = getImportFlavor(t.getTransferDataFlavors(), e);
		if (flavor!=null) {
			try {
				Object data = t.getTransferData(flavor);
				if (flavor.equals(DataFlavor.stringFlavor)) {
					String text = (String)data;
					byte[] bytes = text.getBytes();
					e.replaceSelection(bytes);
				}
			} catch (UnsupportedFlavorException ufe) {
				ufe.printStackTrace(); // Never happens.
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}

		return false;
	}


}