/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.byteobjects.src4.core;

import pasa.cbentley.byteobjects.src4.ctx.BOCtx;
import pasa.cbentley.byteobjects.src4.sources.MemorySource;
import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.logging.IStringable;

/**
 * Encapsulates a source point for reading a {@link ByteObjectManaged}.
 * <br>
 * <br>
 * @author Charles Bentley
 *
 */
public class SourcePoint implements IStringable {

   public int            agentID;

   public int            groupID;

   /**
    * either instanceID or groupID. Value is used in {@link MemorySource#load(int)}
    */
   public int            chunkID;

   public int            instanceID;

   public int            len;

   public ByteController byteCon;

   /**
    * The source array must be a valid {@link ByteObjectManaged} header.
    */
   public byte[]         source;

   public int            sourceOffset;

   public int            subInstanceID;

   private BOCtx boc;
   
   public SourcePoint() {

   }

   public SourcePoint(byte[] data, int offset, int len, int agentid) {
      this.source = data;
      this.sourceOffset = offset;
      this.len = len;
      this.agentID = agentid;
   }


   public SourcePoint(ByteController mc, byte[] data) {
      this.source = data;
      this.len = data.length;
      this.byteCon = mc;
   }

   public SourcePoint(ByteController mc, byte[] fullData, int agentid) {
      byteCon = mc;
      source = fullData;
      sourceOffset = 0;
      len = fullData.length;
      this.agentID = agentid;
   }

   public SourcePoint(ByteController mc, int agentid) {
      byteCon = mc;
      this.agentID = agentid;
   }


   //#mdebug
   public String toString() {
      return Dctx.toString(this);
   }

   public void toString(Dctx dc) {
      dc.root(this, "SourcePoint");
   }

   public UCtx toStringGetUCtx() {
      return boc.getUCtx();
   }

   public String toString1Line() {
      return Dctx.toString1Line(this);
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, "SourcePoint");
      dc.append(" agentID=" + agentID);
      dc.append(" instanceID=" + instanceID);
   }

   //#enddebug
   
   
   

}
