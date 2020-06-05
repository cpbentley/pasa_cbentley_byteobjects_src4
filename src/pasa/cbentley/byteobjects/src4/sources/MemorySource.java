/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.byteobjects.src4.sources;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import pasa.cbentley.byteobjects.src4.core.ByteController;
import pasa.cbentley.byteobjects.src4.core.ByteObjectManaged;
import pasa.cbentley.byteobjects.src4.ctx.BOCtx;
import pasa.cbentley.byteobjects.src4.interfaces.IMemorySource;
import pasa.cbentley.byteobjects.src4.tech.ITechByteControler;
import pasa.cbentley.byteobjects.src4.tech.ITechObjectManaged;
import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.logging.IStringable;
import pasa.cbentley.core.src4.thread.IBProgessable;
import pasa.cbentley.core.src4.utils.BitUtils;

/**
 * Source for loading and saving byte[] data. It is like an {@link InputStream} and {@link OutputStream} with preloading
 * and index loading.
 * <br>
 * One {@link MemorySource} hosts at least one {@link ByteController} header.
 * <br>
 * For each {@link MemorySource}, there is a header, the root segment and IDs segments.
 * <br>
 * 
 * 
 * <b>Why</b>? Loading chunks of Trie data into memory.
 * Trie works with {@link MemorySource} without bothering whether it is coming from Disk/Jar/Internet/RAM.
 * <br>
 * <br>
 * A {@link MemorySource} is identified by a {@link ITechByteControler} header.
 * <br>
 * the {@link ITechObjectManaged#AGENT_OFFSET_06_GSOURCE_ID2} of each agents is the same value as
 * {@link ITechByteControler#MEMC_OFFSET_11_SOURCE_ID2}
 * <br>
 * <br>
 * A memory source can be used by several {@link ByteController} but a byte array only loads to one
 * {@link ByteObjectManaged}
 * <br>
 * <br>
 * 
 * @author Charles Bentley
 * @see ByteArraySource
 * @see JarSource
 */
public abstract class MemorySource implements IMemorySource, ITechMemorySource, IStringable {


   protected int           flags              = 0;

   protected IBProgessable progress;

   protected final BOCtx           boc;

   public MemorySource(BOCtx boc) {
      this.boc = boc;
   }

   /**
    * True if load(id) may return data
    * @return
    */
   public boolean areBlocksSupported() {
      return false;
   }

   /**
    * Offset of the main array. Returns zero by default
    * <br>
    * Offset for IDs are always zero
    * @return
    */
   public int getOffset() {
      return 0;
   }

   public IBProgessable getProgress() {
      return progress;
   }

   public abstract String getSrcID();

   /**
    * Returns the valid integer values in increasing order to be fed in the {@link MemorySource#load(int)}
    * <br>
    * Order is from the smallest to the biggest
    * <br>
    * null if no IDs. i.e. return always the same data for any id {@link MemorySource#load()}
    * @return
    */
   public abstract int[] getValidIDs();

   public boolean hasMSFlag(int flag) {
      return BitUtils.hasFlag(flags, flag);
   }

   /**
    * Tells whether byte array returns by this MemorySource will be prefixed
    * with MemAgent header and MemController header
    * @return
    */
   public boolean isHeaderPrefixed() {
      return true;
   }

   /**
    * Method for loading the data in memory.
    * <br>
    * <br>
    * Might use the {@link IMProgessable}
    * <br>
    * 
    * in this case, load(0) must NOT return null
    * A memory source can return data here and at chunks
    * This method returns the "root" memory
    * <br>
    * @return empty if nothing in the store yet or null if there is an {@link IOException} ?
    * 
    */
   public abstract byte[] load();

   /**
    * Reads the data at ID.
    * <br>
    * {@link {@link MemorySource#load(int)})} 0 is equivalent to {@link MemorySource#load()}
    * @param id
    * @return data prefixed with {@link ByteObjectManaged} header.
    * null if this memory source does not support blocks
    */
   public abstract byte[] load(int id);

   /**
    * Write loaded data of id into data at offset. creates a new array if too small.
    * <br>
    * <br>
    * Return data if no new array
    * <br>
    * <br>
    * Since it writes concatenated {@link ByteObjectManaged}, it is possible to know the number of bytes written.
    * <br>
    * <br>
    * @param id
    * @param data
    * @param offset
    * @return
    */
   //public abstract byte[] load(int id, byte[] data, int offset, int len);

   /**
    * Writes/Loads data in the byte array starting at offset. 
    * <br>
    * <br>
    * 
    * MemController must control agent header if {@link MemorySource} is not headerPrefixed
    * <br>
    * <br>
    * @param id starts at 0.
    * @param array make sure array is big enough
    * @param offset
    * @return the number of bytes actually loaded
    * @throws ArrayIndexOutOfBoundsException if array is too small.
    */
   public abstract void load(int id, byte[] array, int offset);

   /**
    * Method that only reads the {@link ByteController} header.
    * <br>
    * Allows the {@link ByteController} to know what to expect from the {@link MemorySource} without loading
    * the whole structure.
    * <br>
    * Some implementations will load the whole byte array.
    * 
    * @return
    */
   public abstract byte[] loadHeader(int size);

   /**
    * Loads the {@link ByteController} header. calls this method in its constructor.
    * <br>
    * <br>
    * In some cases, this call loads everything in memory. if it is too big. the implementation
    * must wait the call of {@link MemorySource#load()}
    * @return
    */
   public abstract byte[] preload();

   /**
    * Called by the {@link ByteController} to save the byte array back to its location.
    * It must save a copy as the data in memory might change after the method's call
    * <br>
    * <br>
    * Some {@link MemorySource} will do nothing in this method because the only known location is within the class.
    * <br>
    * <br>
    * 
    * @param memory
    * @param offset start of memory to be saved
    * @param len length of data to be saved
    * Default ID is used
    * @return the number of bytes actually saved
    */
   public abstract void save(byte[] memory, int offset, int len);

   /**
    * Same as default save method, but for a single block. Identified by an integer.
    * <br>
    * <br>
    * @param memory
    * @param offset
    * @param len
    * @param id
    */
   public abstract void save(byte[] memory, int offset, int len, int id);

   public void save(ByteObjectManaged bom) {
      save(bom.getByteObjectData(), bom.getByteObjectOffset(), bom.getLength());
   }

   public void save(ByteObjectManaged bom, int id) {
      save(bom.getByteObjectData(), bom.getByteObjectOffset(), bom.getLength(), id);
   }

   public void setProgress(IBProgessable progress) {
      this.progress = progress;
   }

   //#mdebug
   public String toString() {
      return Dctx.toString(this);
   }

   public void toString(Dctx dc) {
      dc.root(this, "MemorySource");
   }


   public UCtx toStringGetUCtx() {
      return boc.getUCtx();
   }

   public String toString1Line() {
      return Dctx.toString1Line(this);
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, "MemorySource");
   }
   //#enddebug
}
