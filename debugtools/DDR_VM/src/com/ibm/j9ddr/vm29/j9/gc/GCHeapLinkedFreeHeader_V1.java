/*******************************************************************************
 * Copyright (c) 1991, 2014 IBM Corp. and others
 *
 * This program and the accompanying materials are made available under
 * the terms of the Eclipse Public License 2.0 which accompanies this
 * distribution and is available at https://www.eclipse.org/legal/epl-2.0/
 * or the Apache License, Version 2.0 which accompanies this distribution and
 * is available at https://www.apache.org/licenses/LICENSE-2.0.
 *
 * This Source Code may also be made available under the following
 * Secondary Licenses when the conditions for such availability set
 * forth in the Eclipse Public License, v. 2.0 are satisfied: GNU
 * General Public License, version 2 with the GNU Classpath
 * Exception [1] and GNU General Public License, version 2 with the
 * OpenJDK Assembly Exception [2].
 *
 * [1] https://www.gnu.org/software/classpath/license.html
 * [2] http://openjdk.java.net/legal/assembly-exception.html
 *
 * SPDX-License-Identifier: EPL-2.0 OR Apache-2.0
 *******************************************************************************/
package com.ibm.j9ddr.vm29.j9.gc;

import com.ibm.j9ddr.AddressedCorruptDataException;
import com.ibm.j9ddr.CorruptDataException;
import com.ibm.j9ddr.vm29.pointer.generated.MM_HeapLinkedFreeHeaderPointer;
import com.ibm.j9ddr.vm29.types.U32;
import com.ibm.j9ddr.vm29.types.UDATA;
import com.ibm.j9ddr.vm29.pointer.generated.J9BuildFlags;
import com.ibm.j9ddr.vm29.structure.J9Consts;

class GCHeapLinkedFreeHeader_V1 extends GCHeapLinkedFreeHeader {
	protected GCHeapLinkedFreeHeader_V1(MM_HeapLinkedFreeHeaderPointer heapLinkedFreeHeaderPointer)
	{
		super(heapLinkedFreeHeaderPointer);
	}
	
	protected GCHeapLinkedFreeHeader_V1(UDATA udata) throws CorruptDataException 
	{
		super(udata);
	}
	
	private UDATA getNextImpl() throws CorruptDataException
	{
		if(J9BuildFlags.interp_compressedObjectHeader) {
			//_next_v1() is the compr version
			U32 lowBits = heapLinkedFreeHeaderPointer._next_v1();
			U32 highBits = heapLinkedFreeHeaderPointer._nextHighBits();
			return new UDATA(highBits).leftShift(32).bitOr(lowBits);
		} else {
			return heapLinkedFreeHeaderPointer._next();
		}
	}

	@Override
	public GCHeapLinkedFreeHeader getNext() throws CorruptDataException 
	{
		UDATA next = getNextImpl();
		if(!next.anyBitsIn(J9Consts.J9_GC_OBJ_HEAP_HOLE_MASK)) {
			throw new AddressedCorruptDataException(heapLinkedFreeHeaderPointer.getAddress(), "Next pointer not tagged");
		}
		next = next.bitAnd(~J9Consts.J9_GC_OBJ_HEAP_HOLE_MASK);
		
		// Presumably code within a version implementation does not need to look up the required version
		return new GCHeapLinkedFreeHeader_V1(next);

	}

	@Override
	public UDATA getSize() throws CorruptDataException 
	{
		return heapLinkedFreeHeaderPointer._size();
	}
}