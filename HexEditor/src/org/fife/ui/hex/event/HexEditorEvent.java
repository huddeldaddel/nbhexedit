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
package org.fife.ui.hex.event;

import java.util.EventObject;

import org.fife.ui.hex.swing.HexEditor;


/**
 * An event that is fired when certain events occur in a hex editor.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class HexEditorEvent extends EventObject {

	private static final long serialVersionUID = 1L;

	/**
	 * The offset of the change.
	 */
	private int offset;

	/**
	 * The number of bytes added.
	 */
	private int added;

	/**
	 * The number of bytes removed.
	 */
	private int removed;


	/**
	 * Creates a new event object.
	 *
	 * @param editor The source of this event.
	 * @param offs The offset at which bytes were added or removed.
	 * @param added The number of bytes added, or <code>0</code> for none.
	 * @param removed The number of bytes removed, or <code>0</code> for none.
	 */
	public HexEditorEvent(HexEditor editor, int offs, int added, int removed) {
		super(editor);
		this.offset = offs;
		this.added = added;
		this.removed = removed;
	}


	/**
	 * Returns the number of bytes added.  If this value equals the number
	 * of bytes removed, the bytes were actually modified.
	 *
	 * @return The number of bytes added.
	 * @see #getRemovedCount()
	 */
	public int getAddedCount() {
		return added;
	}


	/**
	 * Returns the hex editor that fired this event.
	 *
	 * @return The hex editor.
	 */
	public HexEditor getHexEditor() {
		return (HexEditor)getSource();
	}


	/**
	 * Returns the offset of the change.
	 *
	 * @return The offset of the change.
	 */
	public int getOffset() {
		return offset;
	}


	/**
	 * Returns the number of bytes removed.  If this value equals the number
	 * of bytes added, then the bytes were actually modified.
	 *
	 * @return The number of bytes removed.
	 * @see #getAddedCount()
	 */
	public int getRemovedCount() {
		return removed;
	}


	/**
	 * Returns whether this was a "modification" of bytes; that is, no bytes
	 * were added or removed, bytes were only modified.  This is equivalent
	 * to <code>getAddedCount() == getRemovedCount()</code>.
	 *
	 * @return Whether this is just a "modification" of bytes.
	 */
	public boolean isModification() {
		return getAddedCount()==getRemovedCount();
	}


}