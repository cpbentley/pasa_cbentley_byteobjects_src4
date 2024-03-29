/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.byteobjects.src4.objects.pointer;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.ctx.BOCtx;

/**
 * We don't want this..
 * ByteObject don't have Java typing.
 * <br>
 * <br>
 * They must be instantiated as a {@link ByteObject}, based on type, a "static" instance method
 * is called.
 * 
 * That's how this framework was designed.
 * 
 * <br>
 * <br>
 * If you really need Pointer typing, you will have to create a Pointer class with a ByteObject field
 * @author Charles Bentley
 *
 */
public class PointerBO extends ByteObject implements IBOPointer {

   public PointerBO(BOCtx boc) {
      super(boc, new byte[POINTER_BASIC_SIZE]);
   }
}
