/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.byteobjects.src4.core;

import pasa.cbentley.byteobjects.src4.ctx.BOCtx;
import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.logging.IStringable;

/**
 * In a typical application life cycle, GUI is loaded, User does an action and exits application.
 * <br>
 * 
 * @author Charles Bentley
 *
 */
public class ByteObjectRef implements IStringable {

   private BOCtx boc;

   private int          nextReference = 1;

   private ByteObject[] referenced    = new ByteObject[10];

   public ByteObjectRef(BOCtx boc) {
      super();
      this.boc = boc;
   }

   /**
    * {@link ByteObject} that want to be referenced by 16bits integer have to be pinned into memory.
    * <br>
    * Once referenced, they will never be garbaged collected and will pin all sub {@link ByteObject}.
    * @param bo
    * @return
    */
   public int addReference(ByteObject bo) {
      if (nextReference + 1 >= referenced.length)
         referenced = boc.getBOU().increaseCapacity(referenced, 15);
      nextReference++;
      referenced[nextReference] = bo;
      return nextReference;
   }

   /**
    * Gets the {@link ByteObject} located at Reference ID (index in the repository)
    * <br>
    * <br>
    * @param refID
    * @return
    * @throws IllegalArgumentException when refid -1
    * @throws ArrayIndexOutOfBoundsException refid too big
    */
   public ByteObject getReference(int refID) {
      if (refID <= 0)
         throw new IllegalArgumentException();
      if (refID >= referenced.length) {
         throw new ArrayIndexOutOfBoundsException("refID (" + refID + ") is too big for repository size =" + referenced.length);
      }
      return referenced[refID];
   }

   //#mdebug
   public String toString() {
      return Dctx.toString(this);
   }

   public void toString(Dctx dc) {
      dc.root(this, "ByteObjectRef");
   }

   public String toString1Line() {
      return Dctx.toString1Line(this);
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, "ByteObjectRef");
   }

   public UCtx toStringGetUCtx() {
      return boc.getUCtx();
   }
   //#enddebug
}
