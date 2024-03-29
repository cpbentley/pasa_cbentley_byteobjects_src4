/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.byteobjects.src4.sources;

import pasa.cbentley.byteobjects.src4.ctx.BOCtx;
import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.logging.IStringable;
import pasa.cbentley.core.src4.structs.IntToObjects;

/**
 * The RootSource managing
 * @author Charles Bentley
 *
 */
public class RootSource implements IStringable {

   private IntToObjects srcs;


   private BOCtx        boc;

   public RootSource(BOCtx boc) {
      this(boc, 10);
   }

   public RootSource(BOCtx boc, int add) {
      this.boc = boc;
      srcs = new IntToObjects(boc.getUC(), add);
   }

   public MemorySource findSource(String name) {
      for (int i = 0; i < srcs.nextempty; i++) {
         MemorySource objectAtIndex = (MemorySource) srcs.getObjectAtIndex(i);
         if (objectAtIndex.getSrcID().equals(name)) {
            return objectAtIndex;
         }
      }
      return null;
   }

   /**
    * When Dev code creates a {@link MemorySource} it must register and get maybe a new reference
    * 
    * @param ms
    */
   public synchronized MemorySource registerMemorySource(MemorySource ms) {
      //find if already here
      MemorySource msFound = findSource(ms.getSrcID());
      if(msFound == null) {
         srcs.add(ms);
         return ms;
      } else {
         return msFound;
      }
   }

   
   //#mdebug
   public String toString() {
      return Dctx.toString(this);
   }

   public void toString(Dctx dc) {
      dc.root(this, "RootSource");
      toStringPrivate(dc);
   }

   public String toString1Line() {
      return Dctx.toString1Line(this);
   }

   private void toStringPrivate(Dctx dc) {

   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, "RootSource");
      toStringPrivate(dc);
   }

   public UCtx toStringGetUCtx() {
      return boc.getUC();
   }

   //#enddebug
   

}
