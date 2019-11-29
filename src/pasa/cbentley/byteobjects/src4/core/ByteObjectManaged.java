package pasa.cbentley.byteobjects.src4.core;

import java.io.IOException;

import pasa.cbentley.byteobjects.src4.ctx.BOCtx;
import pasa.cbentley.byteobjects.src4.ctx.IBOTypesBOC;
import pasa.cbentley.byteobjects.src4.interfaces.IMemorySource;
import pasa.cbentley.byteobjects.src4.sources.MemorySource;
import pasa.cbentley.byteobjects.src4.tech.ITechByteObject;
import pasa.cbentley.byteobjects.src4.tech.ITechObjectManaged;
import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.helpers.BytesIterator;
import pasa.cbentley.core.src4.io.BADataIS;
import pasa.cbentley.core.src4.io.BADataOS;
import pasa.cbentley.core.src4.io.BAByteIS;
import pasa.cbentley.core.src4.io.BAByteOS;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.logging.IDLog;
import pasa.cbentley.core.src4.structs.synch.MutexSignal;
import pasa.cbentley.core.src4.utils.IntUtils;

/**
 * {@link ByteObjectManaged} is a special type of {@link ByteObject} whose expansion, loading and saving is managed by a {@link ByteController}.
 * Its type is {@link IBOTypesBOC#TYPE_035_OBJECT_MANAGED} 
 * <br>
 * <br>
 * {@link ByteObjectManaged} can be unmanaged i.e. null {@link ByteController} and thus behave like a regular {@link ByteObject}.
 * <br>
 * <br>
 * Following the {@link ByteObject} header defined in {@link ITechByteObject} of size {@link ITechByteObject#A_OBJECT_BASIC_SIZE}, all {@link ByteObjectManaged}
 * have a {@link ITechObjectManaged} header of size {@link ITechObjectManaged#AGENT_BASIC_SIZE}. Unlike simple {@link ByteObject}s philosophy of <b>small and many</b>, {@link ByteObjectManaged}
 * have big header, so they fit the use case of <b>few and big</b>.
 * <br>
 * This header's field helps the {@link ByteController} loading data structures from various {@link IMemorySource}.
 * <br>
 * <br>
 * A {@link ByteObjectManaged} type is defined by several fields:
 * <li>
 * 
 * Key methods of {@link ByteObjectManaged}
 * <li> {@link ByteObjectManaged#expandArrayInternal(int, int)} : Extends the data byte area.
 * <li> {@link ByteObjectManaged#getDataOffsetStartLoaded()} : 
 * <li> {@link ByteObjectManaged#getLastUsedByte()} : Lastusedbyte is relative to offset and must be equal or smaller than the length attribute.
 * <br>
 * Immediately following we have the Memory Managing header {@link ITechObjectManaged#AGENT_OFFSET_17_MAGIC2}
 * <br>
 * <br>
 * The length is controlled by {@link ITechObjectManaged#AGENT_OFFSET_16_LEN4}. This field only records data in the actual byte array.
 * If the sub class of {@link ByteObjectManaged} uses un-serialized integer arrays to store some data, the length field
 * does not record it.
 * <br>
 * <br>
 * Loading from source and saving from source can be done by user without knowing the source.
 * <br>
 * <br>
 * The minimum for the controller to match a memory area is an Agent's id.<br>
 * It is provided either in the constructor or given in the data array<br>
 * <br>
 * NOTE: {@link ByteController} has a memory environment variable which tells whether memory is plenty or scarce.<br>
 * <br>
 * Subclass may decide to expand their buffer differently in a scarce situation.
 * <br>
 * Instead of doubling buffer size, a 10-20% increase is reasonable. In such a case  trimming is not necessary.
 * <br>
 * The subclass checks the SCARCE condition and call the expand method with a matching parameter
 * <br>
 * <br>
 * The trim method can be called. it will shrink the array by removing the buffer.
 * <br>
 * <br>
 * <br>
 * <li>{@link ITechObjectManaged#AGENT_OFFSET_01_FLAG_1}
 * <li>{@link ITechObjectManaged#AGENT_OFFSET_03_FLAGZ_1}
 * <li>{@link ITechObjectManaged#AGENT_OFFSET_02_FLAGX_1}
 * <li>{@link ITechObjectManaged#AGENT_OFFSET_04_INTERFACE_ID2}
 * <li>{@link ITechObjectManaged#AGENT_OFFSET_05_CLASS_ID2}
 * <li>{@link ITechObjectManaged#AGENT_OFFSET_06_GSOURCE_ID2}
 * <li>{@link ITechObjectManaged#AGENT_OFFSET_07_INSTANCE_ID2}
 * <li>{@link ITechObjectManaged#AGENT_OFFSET_08_REF_ID2}
 * <li>{@link ITechObjectManaged#AGENT_OFFSET_09_CTRL_ID2}
 * <li>{@link ITechObjectManaged#AGENT_OFFSET_10_PAKAGE_ID2}
 * <li>{@link ITechObjectManaged#AGENT_OFFSET_11_APP_ID2}
 * <li>{@link ITechObjectManaged#AGENT_OFFSET_12_LEN_HEADER_DYN2}
 * <li>{@link ITechObjectManaged#AGENT_OFFSET_13_LEN_HEADER2}
 * <li>{@link ITechObjectManaged#AGENT_OFFSET_14_LEN_DATA_4}
 * <li>{@link ITechObjectManaged#AGENT_OFFSET_15_LEN_BUFFER_3}
 * <li>{@link ITechObjectManaged#AGENT_OFFSET_16_LEN4}
 * <li>{@link ITechObjectManaged#AGENT_OFFSET_17_MAGIC2}
 * @author Charles Bentley
 *
 */
public class ByteObjectManaged extends ByteObject implements ITechObjectManaged {

   public static final int INTERFACE_OFFSET_MASK10BITS = 0x3FF;

   /**
    * Set by the Memory thread to start sync on the reload
    */
   public static final int SYNCRO_FLAG_1_UNLOADED      = 1;

   /**
    * Volatile use for the lock. With this volatile flag,
    * The {@link ByteController} always knows if the object is being used.
    * When memory is purged,
    * <li>lock it using this
    * <li> Set flag for multi thread
    */
   public static final int SYNCRO_FLAG_2_LOCKED        = 2;

   public static final int SYNCRO_FLAG_3_WRITE_LOCKED  = 4;

   public static final int SYNCRO_FLAG_4_WAITING       = 8;

   /**
    * {@link ByteObjectManaged#getIDInterfaces()}.
    * To be Inlined.
    * @param num
    * @param offset the offset t
    * @return
    */
   public static int interfaceCode(int num, int offset) {
      int val = offset & INTERFACE_OFFSET_MASK10BITS;
      int numBits = (num << 10);
      return val + numBits;
   }

   /**
    * The {@link ByteController} managing this {@link ByteObjectManaged}.
    * <br>
    * When null, the {@link ByteObjectManaged} is not managed by a {@link ByteController}.
    * <br>
    * An agent instance can only belong to one {@link ByteController}.
    * 
    * <br>
    * <br>
    * For complex construction, use a {@link ByteController} inside others.
    */
   public ByteController  byteCon;

   /**
    * Position of Agent {@link ByteObjectManaged} in its {@link ByteController} agent array.
    * <br>
    * Field is {@link ByteController#agentsRefArray}
    * <br>
    * This field is filled when the agent is added physically by {@link ByteController#addAgent(ByteObjectManaged)}
    * <br>
    * Consequence: A {@link ByteObjectManaged} reference can only belong at most to 1 {@link ByteController}.
    */
   int                    memoryAgentIndex     = -1;

   /**
    * Position of inside Byte Array in Agent's {@link ByteController}.
    * <br>
    */
   int                    memoryByteArrayIndex = -1;

   /**
    * ID that was used to read the byte array
    */
   int                    memoryMemSrcID       = -1;

   /**
    * Index of the {@link MemorySource} used to source the byte array of this {@link ByteObjectManaged}.
    * <br>
    * Set by the {@link ByteController} when {@link ByteController#dataSources}.
    * <br>
    * Only one source can be used.
    */
   int                    memoryMemSrcIndex    = -1;

   //#debug
   private Thread         myThread;                 //this field is used

   MutexSignal            sem;

   /**
    * Volatile flags used to synch.
    * This won't be used
    */
   protected volatile int syncroFlags;

   /**
    * Version of the {@link ByteObjectManaged} when it is created.
    * <br>
    * {@link ByteObjectManaged} without versionning 
    */
   protected int          versionStart;

   /**
    * Creates an empty {@link ByteObject}
    * @param boc
    */
   public ByteObjectManaged(BOCtx boc) {
      super(boc, boc.getByteObjectManagedFactory().getTechDefault());
      initBOManaged();
   }

   public ByteObjectManaged(BOCtx boc, byte[] data) {
      this(boc, data, 0);
   }

   /**
    * Constructor with null {@link ByteController}.
    * <br>
    * <br>
    * Does not check if data has the right header.
    * <br>
    * <br>
    * @param data
    * @param index
    */
   public ByteObjectManaged(BOCtx boc, byte[] data, int index) {
      super(boc, data, index);
   }

   public ByteObjectManaged(BOCtx boc, ByteController bc) {
      super(boc, boc.getByteObjectManagedFactory().getTechDefault());
      this.byteCon = bc;
      initBOManaged();
   }

   /**
    * Checks if array has.
    * The agent data starting with the {@link ByteObject} header is known to start at offset
    * <br>
    * <br>
    * @param bc
    * @param array
    */
   ByteObjectManaged(BOCtx mod, ByteController bc, byte[] array) {
      this(mod, bc, array, 0);
   }

   /**
    * The {@link ByteController} decides which agent gets what data sources.
    * <br>
    * 
    * no instance id, therefore, Controller will use first unused source of class id.
    * 
    * <br>
    * <br>
    * @param bc
    */
   //   public ByteObjectManaged(BOCtx mod, ByteController bc, int classid) {
   //      this(mod, new SourcePoint(bc, classid));
   //   }

   /**
    * Create an agent and reads a memory space header
    * <br>
    * <br>
    * MemorySource is void
    * With this constructor, the array is already loaded with MemorySpace.
    * <br>
    * <br>
    * @param bc the memory controller to which the agent has already been added.
    * @param array memory source for agents
    * @param offset offset for the memory space (loaded with header)
    * @param len LOADED with MemorySpace
    */
   ByteObjectManaged(BOCtx mod, ByteController bc, byte[] array, int offset) {
      super(mod, array, offset);
      magicWordCheck(array, offset);
      byteCon = bc;
   }

   /**
    * True when {@link ITechObjectManaged#AGENT_FLAGZ_CTRL_5_LOCKED}
    * @return
    */
   public boolean isLocked() {
      return hasFlag(AGENT_OFFSET_03_FLAGZ_1, AGENT_FLAGZ_CTRL_5_LOCKED);
   }

   /**
    * True when {@link ITechObjectManaged#AGENT_FLAGZ_CTRL_6_WAITING}
    * @return
    */
   public boolean isWaiting() {
      return hasFlag(AGENT_OFFSET_03_FLAGZ_1, AGENT_FLAGZ_CTRL_6_WAITING);
   }

   /**
    * Constructor gives the base tech header. Copies all the references. Does not copy the content of the byte array and parameters.
    * <br>
    * <br>
    * The {@link ByteObject} may have sub object which are transferred to the
    * newly created {@link ByteObjectManaged} which only copies the header and data of
    * size given by {@link ITechByteObject#A_OBJECT_OFFSET_3_LENGTH2} and {@link ITechObjectManaged#AGENT_OFFSET_16_LEN4}
    * <br>
    * <br> 
    * Sets the {@link ITechObjectManaged#AGENT_OFFSET_16_LEN4} with the length of the array.
    * <br>
    * <br>
    * 
    * @param tech the {@link ByteObjectManaged} describes the technical parameters of the structure.
    * Uses the {@link ByteController} of the tech if any.
    * It is designed just
    */
   public ByteObjectManaged(BOCtx mod, ByteObjectManaged tech) {
      this(mod, tech.getByteObjectData(), tech.getByteObjectOffset());
      if (tech instanceof ByteController) {
         throw new IllegalArgumentException();
      }
      this.param = tech.getSubs();
      this.byteCon = tech.byteCon;
      if (byteCon != null) {
         byteCon.instantiateFrom(this, tech);
      }
      initBOManaged();
   }

   public ByteObjectManaged(ByteController bc, byte[] data) {
      this(bc, data, 0);
   }

   public ByteObjectManaged(ByteController bc, byte[] data, int index) {
      super(bc.getBOC(), data, index);
      this.byteCon = bc;
      initBOManaged();
   }

   /**
    * Either controller or source is null
    * <br>
    * <br>
    * @param sp
    */
   //   public ByteObjectManaged(BOCtx mod, SourcePoint sp) {
   //      super(mod, null);
   //      if (sp.byteCon == null && sp.source == null) {
   //         throw new NullPointerException();
   //      }
   //      if (sp.byteCon == null && sp.source != null) {
   //         //no controller
   //         data = sp.source;
   //         index = sp.sourceOffset;
   //      } else {
   //         byteCon = sp.byteCon;
   //         sp.agentID = getIDClass();
   //         //we must lookup the source in the memory controller
   //         //byteCon.addAgent(this, sp);
   //      }
   //      backup = sp;
   //   }

   public ByteObjectManaged(ByteObject tech) {
      super(tech.boc, tech.data, tech.index);
      this.param = tech.param;
   }

   public ByteObjectManaged(ByteObjectManaged tech) {
      this(tech.boc, tech.getByteObjectData(), tech.getByteObjectOffset());
      this.param = tech.getSubs();
      this.byteCon = tech.byteCon;
      initBOManaged();
   }

   /**
    * The {@link ByteObjectManaged} has the concept of a buffer area part of the {@link ByteObject}.
    *  but without any business use.
    * <br>
    * <br>
    * This method removes those bytes that are dead weight in a read only use case.
    * <br>
    * <br>
    * 
    */
   public void bufferTrim() {
      if (get3(AGENT_OFFSET_15_LEN_BUFFER_3) != 0) {
         int add = get3(AGENT_OFFSET_15_LEN_BUFFER_3);
         this.expandArrayInternal(0 - add, getBufferOffsetStart());
         incrementLengthBuffer(-add);
      }
   }

   /**
    * Copy the header, except the length.
    * Must be a valid
    * @param tech
    */
   public void burnHeader(ByteObjectManaged tech) {
      int hl = tech.get2(AGENT_OFFSET_13_LEN_HEADER2);
      int thisHl = get2(AGENT_OFFSET_13_LEN_HEADER2);
      int o1 = get2(AGENT_OFFSET_13_LEN_HEADER2);
      int o2 = get4(AGENT_OFFSET_14_LEN_DATA_4);
      int o3 = get3(AGENT_OFFSET_15_LEN_BUFFER_3);
      int o4 = get4(AGENT_OFFSET_16_LEN4);
      int min = Math.min(hl, thisHl);
      System.arraycopy(tech.data, tech.index, this.data, this.index, min);
      set2(AGENT_OFFSET_13_LEN_HEADER2, o1);
      set4(AGENT_OFFSET_14_LEN_DATA_4, o2);
      set3(AGENT_OFFSET_15_LEN_BUFFER_3, o3);
      set4(AGENT_OFFSET_16_LEN4, o4);
   }

   /**
    * Returns a {@link ByteObjectManaged} with the same
    * <li> byte array,
    * <li> same offset,
    * <li>same {@link ByteController}
    * <br>
    * When is this used?
    */
   public Object clone() {
      ByteObjectManaged b = new ByteObjectManaged(boc, this);
      return b;
   }

   /**
    * Create a {@link ByteObjectManaged} with only the Static and Dynamic Header
    * @return
    */
   public ByteObjectManaged cloneBOMHeader() {
      int headerLen = getLengthFullHeaders();
      byte[] ar = new byte[headerLen];
      System.arraycopy(data, index, ar, 0, ar.length);
      ByteObjectManaged bom = new ByteObjectManaged(boc, ar);
      return bom;
   }

   public void closeAgent() {
      memoryClear();
   }

   /**
    * Appends the the data byte at the end of the current data bytes.
    * <br>
    * <br>
    * Check the buffer. Eats the buffer if any
    * <br>
    * <br>
    * 
    * @param data
    * @param offset
    */
   public void copyAppendData(byte[] data, int offset, int len) {
      int cs = getDataOffsetEndLoaded(); //current end of data is our start position of appending
      expandData(len);//first expand the data
      System.arraycopy(data, offset, this.data, cs, len);
   }

   /**
    * Copies the agent into the array
    * @param array
    * @param offset
    * @return the number of bytes
    */
   public int copyByteObjectTo(byte[] array, int offset) {
      int len = getLength();
      System.arraycopy(data, index, array, offset, len);
      return len;
   }

   public int copyBytesTo(int indexNotLoaded, byte[] array, int offset, int len) {
      System.arraycopy(data, index + indexNotLoaded, array, offset, len);
      return len;
   }

   /**
    * Replace the data of {@link ByteObjectManaged} with the given byte interval
    * @param data
    * @param offset
    * @param len
    */
   public void copyReplaceData(byte[] data, int offset, int len) {
      expandResetArrayData(len);
      int cs = getDataOffsetStartLoaded(); //current end of data is our start position of appending
      System.arraycopy(data, offset, this.data, cs, len);
   }

   /**
    * Lock accecss to data. thread reaching this with a lock will wait for the locking method
    * to finish.
    * The thread waits for and register being locked.
    * <br>
    * When the call of {@link ByteObjectManaged#dataUnLock()} is not sent
    * after the end of the method, there will be a deadlock. it can be because
    * of an uncheck exception didn't call.
    * 
    * No reentrant locks.
    */
   public void dataLock() {
      //only checks for lock if multi threads
      if (hasFlag(AGENT_OFFSET_02_FLAGX_1, AGENT_FLAGX_2_MUTLI_THREAD)) {
         if (isSyncroFlag(SYNCRO_FLAG_2_LOCKED)) {
            //what kind of lock?
            //lock thread
            boc.getLock().wait(this, Thread.currentThread());
            //thread locks the 
         } else {
            //object wasn't locked. lock it for ourselves
            setSyncroFlag(SYNCRO_FLAG_2_LOCKED, true);
            setFlag(AGENT_OFFSET_03_FLAGZ_1, AGENT_FLAGZ_CTRL_5_LOCKED, true);
         }
      } else {
         //otherwise check if current thread is valid. only in test mode.
         //remove this in productio mode
         //#mdebug
         if (myThread != Thread.currentThread()) {
            //TODO document this why
            //throw new IllegalArgumentException();
         }
         //#enddebug
      }
   }

   /**
    * Asks the {@link ByteController} to reload the agent data.
    * <br>
    * The data must have been unloaded. i.e. {@link ITechObjectManaged#AGENT_FLAG_CTRL_7_DATA_UNLOADED} set to true by the ByteCon.
    * Why was the data unloaded? To free memory.
    * <br>
    * When reloading the data, what happens to dependencies ? If those havent been unloaded or they have and then reloaded
    * by another structure (i.e. dictionnary shared)
    */
   public void dataReload() {
      if (byteCon != null) {
         if (hasFlag(AGENT_OFFSET_01_FLAG_1, AGENT_FLAG_CTRL_7_DATA_UNLOADED)) {
            byteCon.reload(this); //
         }
      }
   }

   public void dataUnload() {

   }

   /**
    * No unlocking if 
    */
   public void dataUnLock() {
      if (hasFlag(AGENT_OFFSET_02_FLAGX_1, AGENT_FLAGX_1_GARBAGE_COLLECTED)) {
         //when garbage collected, we must make sure our collector is
         //not waiting on a signal. if it is.. send signal through semaphore
         //check bytecontroller volatile flags
         if (byteCon != null) {
            if (byteCon.isSyncroFlag(SYNCRO_FLAG_4_WAITING)) {
               //notify garbage thread that object is unlocking
               //get the semaphore for the GC thread
            }
         }
      }
      if (hasFlag(AGENT_OFFSET_02_FLAGX_1, AGENT_FLAGX_2_MUTLI_THREAD)) {
         //volatile.
         setSyncroFlag(SYNCRO_FLAG_2_LOCKED, false);
         setFlag(AGENT_OFFSET_03_FLAGZ_1, AGENT_FLAGZ_CTRL_5_LOCKED, false);
         //only do the expensive notification if there is at least one waiting.
         if (hasFlag(AGENT_OFFSET_03_FLAGZ_1, AGENT_FLAGZ_CTRL_6_WAITING)) {
            //notify waiting first waiting thread
            //TODO use a semaphore. to avoid missing signals
            boc.getLock().notify(this);
         }
      }
   }

   /**
    * Checks
    * <li> type
    * <li> null
    * <li> content starting after {@link ByteObjectManaged} header
    * @param p2
    * @return
    */
   public boolean equalsContent(ByteObjectManaged p2) {
      ByteObject p1 = this;
      if (p1 == null || p2 == null)
         return false;
      if (p1.getType() != p2.getType()) {
         return false;
      }
      //only check data between inside header
      int start1 = p1.index + AGENT_BASIC_SIZE;
      int start2 = p2.index + AGENT_BASIC_SIZE;
      int len = Math.min(p1.getLength(), p2.getLength()) - AGENT_BASIC_SIZE;
      for (int i = 0; i < len; i++) {
         if (p1.data[start1] != p2.data[start2]) {
            return false;
         }
         start1++;
         start2++;
      }
      return true;
   }

   /**
    * Expand array increasing the data region. Use the buffer area if any.
    * <br>
    * {@link ITechObjectManaged#AGENT_OFFSET_15_LEN_BUFFER_3}. Will try to eat into the buffer zone
    * See {@link ByteObjectManaged#expandArrayInternal(int, int)}
    * <br>
    * <br>
    * @param incr
    */
   public void expandArray(int incr) {
      this.expandArrayInternal(incr, getDataOffsetEndLoaded());
   }

   /**
    * Expansion of {@link ByteObject} byte array. Parameter has a byte meaning manage the agent's header automatically 
    * <br>
    * <br>
    * This method is one of the major reason the {@link ByteObjectManaged} class exists. It allows the class
    * to delegate memory management to the {@link ByteController} if there is one.
    * <br>
    * <br>
    * @param incr number of bytes to be added at position. When incr is negative
    * @param position offset loaded position from 0.
    */
   void expandArrayInternal(int incr, int position) {
      //#debug
      //dLog().printMemory("#ByteObjectManaged#expandArray Change of " + incr + " bytes at position " + position + " arraylen=" + data.length, ByteObjectManaged.class);
      //if we have a ByteController, ask him to manage the expansion
      if (byteCon != null) {
         byteCon.expandMemory(this, incr, position);
      } else {
         increaseArray(incr, position);
      }
   }

   /**
    * Expanding buffer.
    * <br>
    * See {@link ITechObjectManaged#AGENT_OFFSET_15_LEN_BUFFER_3}
    * @param incr when increment is negative, reduce buffer.
    * 
    * See {@link ByteObjectManaged#bufferTrim()} to remove buffer
    */
   public void expandBuffer(int incr) {
      int position = getDataOffsetEndLoaded();
      //the byte controller only deal with the actual expansion that might require
      // memory management
      expandArrayInternal(incr, position);
      incrementLengthBuffer(incr);
   }

   /**
    * Expands
    * @param incr
    */
   public void expandData(int incr) {
      //check data
      this.expandDataArray(incr, getDataOffsetEndLoaded());
   }

   /**
    * Main method for increasing the data region. Uses buffer region if available.
    * <br>
    * Otherwise ask increase through {@link ByteController} if any.
    *
    * <br>
    * @param incr
    * @param position
    */
   public void expandDataArray(int incr, int position) {
      if (get3(AGENT_OFFSET_15_LEN_BUFFER_3) > incr) {
         this.incrementNoVersion(AGENT_OFFSET_15_LEN_BUFFER_3, 3, -incr);
         this.incrementNoVersion(AGENT_OFFSET_14_LEN_DATA_4, 4, incr);
      } else {
         //we must expand array
         expandArrayInternal(incr, position);
         incrementLengthData(incr);
      }
      setSaveFlag(false);
   }

   /**
    * Cut a new array
    * Sets the number of bytes available for the data part {@link ITechObjectManaged#AGENT_OFFSET_14_LEN_DATA_4}. Those bytes are set to 0.
    * <br>
    * <br>
    * Does not reset header values. Header size is returned by {@link ByteObjectManaged#}.
    * 
    * @param incr
    */
   public void expandResetArrayData(int newDataSize) {
      int dataSize = get4(AGENT_OFFSET_14_LEN_DATA_4);
      if (newDataSize < 0) {
         newDataSize = 0;
      }
      int headerLen = get2(AGENT_OFFSET_13_LEN_HEADER2);
      int totalSize = headerLen + newDataSize;
      if (byteCon != null) {
         byte[] oldData = this.data;
         int indexOld = this.index;
         byteCon.setAgentData(this, totalSize);
         System.arraycopy(oldData, indexOld, data, index, headerLen);
      } else {
         byte[] newData = new byte[totalSize];
         System.arraycopy(data, index, newData, 0, headerLen);
         data = newData;
         index = 0;
      }
      //update the size values
      set4(AGENT_OFFSET_14_LEN_DATA_4, newDataSize);
      set4(AGENT_OFFSET_16_LEN4, totalSize);
      set3(AGENT_OFFSET_15_LEN_BUFFER_3, 0);

   }

   public void flagDataChange() {
      setSaveFlag(false);
   }

   public int getBCAgentIndex() {
      return memoryAgentIndex;
   }

   public int getBCInstanceIndex() {
      return memoryMemSrcID;
   }

   public int getBCSourceIndex() {
      return memoryMemSrcIndex;
   }

   public int getBufferOffsetEnd() {
      return getBufferOffsetStart() + get3(AGENT_OFFSET_15_LEN_BUFFER_3);
   }

   public int getBufferOffsetStart() {
      return getDataOffsetStartLoaded() + get4(AGENT_OFFSET_14_LEN_DATA_4);
   }

   public ByteController getByteController() {
      return byteCon;
   }
   
   /**
    * Creates a {@link ByteController} 
    * <br>
    * Caller in fact wants to creates a {@link ByteController} but only with parameter is null.
    * <br>
    * @param mod
    * @param b
    * @return
    */
   public ByteController getByteControllerCreateIfNull() {
      if (byteCon == null) {
         byteCon = new ByteController(boc);
      }
      return byteCon;
   }

   /**
    * Utility method to creates an object to start reading at {@link ITechObjectManaged#AGENT_OFFSET_14_LEN_DATA_4}
    * 
    * Creates a {@link DataBAInputStream} to read the {@link ByteObjectManaged} data.
    * @return
    */
   public BADataIS getDataInputStream() {
      int offset = getDataOffsetStartLoaded();
      int dataLen = get4(AGENT_OFFSET_14_LEN_DATA_4);
      UCtx uc = getUCtx();
      BAByteIS bais = new BAByteIS(uc, data, offset, dataLen);
      return new BADataIS(uc, bais);
   }

   public int getDataOffsetEnd() {
      return get2(AGENT_OFFSET_13_LEN_HEADER2) + get4(AGENT_OFFSET_14_LEN_DATA_4);
   }

   /**
    * Offset of ending data, excluding buffer
    * <br>
    * Buffer is transparent for subclass. Ease the
    * <br>
    * Never use this value as a starting point. Always a closing point.
    * i.e. data end does not mean trailer start
    * @return
    */
   public int getDataOffsetEndLoaded() {
      return getDataOffsetStartLoaded() + get4(AGENT_OFFSET_14_LEN_DATA_4);
   }

   /**
    * Index at which the subclass starts.
    * Returns the loaded Offset!!
    * @return
    */
   public int getDataOffsetStartLoaded() {
      return index + getLengthFullHeaders();
   }

   /**
    * Returns {@link ITechObjectManaged#AGENT_OFFSET_05_CLASS_ID2} from the {@link ByteObjectManaged} byte array header.
    * <br>
    * <br>
    * 
    * may return -1 in a constructor
    */
   public int getIDClass() {
      return get2(AGENT_OFFSET_05_CLASS_ID2);
   }

   /**
    * {@link ITechObjectManaged#AGENT_OFFSET_09_CTRL_ID2}
    * @return
    */
   public int getIDCtrl() {
      return get2(AGENT_OFFSET_09_CTRL_ID2);
   }

   /**
    * Return the GSource id written in the Agent's header
    * 0 if no data
    * {@link ITechObjectManaged#AGENT_OFFSET_06_GSOURCE_ID2}
    */
   public int getIDGSource() {
      return get2(AGENT_OFFSET_06_GSOURCE_ID2);
   }

   /**
    * Return the instance id written in the Agent's header
    * 0 if no data
    * {@link ITechObjectManaged#AGENT_OFFSET_07_INSTANCE_ID2}
    */
   public int getIDInstance() {
      return get2(AGENT_OFFSET_07_INSTANCE_ID2);
   }

   /**
    * Reads the interface IDs
    * Max 2*2*2*2*2 = 16 interfaces
    * 
    * @return empty array if no interfaces
    */
   public int[] getIDInterfaces() {
      return getDynBoParamValues0Null(AGENT_OFFSET_04_INTERFACE_ID2);
      //      int val = get2(AGENT_OFFSET_04_INTERFACE_ID2);
      //      if (val == 0) {
      //         return new int[0];
      //      } else {
      //         if (val > INTERFACE_OFFSET_MASK10BITS) {
      //            //4 bits for the number of interfaces
      //            int num = (val >> 10) & 0x3F; //5bits
      //            int offset = val & INTERFACE_OFFSET_MASK10BITS; //10bits for the offset at which to start reading for inteface IDS
      //            int[] vs = new int[num];
      //            for (int i = 0; i < num; i++) {
      //               vs[i] = get2(offset);
      //               offset += 2;
      //            }
      //            return vs;
      //         } else {
      //            //only one interface ID
      //            return new int[] { val };
      //         }
      //      }
   }

   /**
    * {@link ITechObjectManaged#AGENT_OFFSET_08_REF_ID2}
    * @return
    */
   public int getIDRef() {
      return get2(AGENT_OFFSET_08_REF_ID2);
   }

   /**
    * We override because the length is decided by 4 bytes
    * <br>
    * All {@link ByteObjectManaged} have a maxed out {@link ITechByteObject#A_OBJECT_OFFSET_3_LENGTH2} field.
    * <br>
    * <br>
    * @see ITechByteObject#A_OBJECT_OFFSET_3_LENGTH2
    * @see ITechObjectManaged#AGENT_OFFSET_16_LEN4
    */
   public int getLength() {
      return get4(AGENT_OFFSET_16_LEN4);
   }

   public int getLengthDynHeader() {
      return get2(AGENT_OFFSET_12_LEN_HEADER_DYN2);
   }

   /**
    * 
    * @return
    */
   public int getLengthFullHeaders() {
      return get2(AGENT_OFFSET_13_LEN_HEADER2) + get2(AGENT_OFFSET_12_LEN_HEADER_DYN2);
   }

   /**
    * Gets the {@link ITechObjectManaged#AGENT_OFFSET_16_LEN4} field value.
    * <br>
    * <br>
    * 
    */
   public int getLengthManaged() {
      return get4(AGENT_OFFSET_16_LEN4);
   }

   /**
    * Gets the {@link ITechObjectManaged#AGENT_OFFSET_13_LEN_HEADER2} field value.
    * <br>
    * <br>
    * This value is set during creation and is the fixed header length.
    * @return
    */
   public int getLengthStaticHeader() {
      return get2(AGENT_OFFSET_13_LEN_HEADER2);
   }

   public int getMagicWord() {
      return get2(AGENT_OFFSET_17_MAGIC2);
   }

   /**
    * The {@link ByteController} who loaded the structure.
    * <br>
    * <br>
    * Possibly null which means the {@link ByteObjectManaged} is not part of a bigger structure.
    * <br>
    * <br>
    * 
    * @return
    */
   public ByteController getMemController() {
      return byteCon;
   }

   /**
    * Return a reference to the byte[] array used by {@link ByteObject}
    * <br>
    * <br>
    * 
    * @return
    */
   public byte[] getMemory() {
      return data;
   }

   /**
    * Returns the reference of an array without  Serialize without buffer.
    * <br>
    * <br>
    * 
    * @return
    */
   public byte[] getMemoryOfRightLength() {
      int len = getLength();
      if (len == data.length) {
         return data;
      } else {
         byte[] d = new byte[len];
         System.arraycopy(data, 0, d, 0, len);
         return d;
      }
   }

   public int getNextLastDataOffset() {
      return index + get4(AGENT_OFFSET_16_LEN4);
   }

   public int getOffset() {
      return index;
   }

   /**
    * 
    * @param ref
    * @return
    */
   public ByteObjectManaged getTechSub(int ref) {
      return (ByteObjectManaged) param[ref];
   }

   public int getTrailerSize() {
      return super.getTrailerSize();
   }

   /**
    * Check whether {@link ITechObjectManaged#AGENT_OFFSET_14_LEN_DATA_4} is 0
    * 
    * @return return true if != 0
    */
   public boolean hasData() {
      return get4(AGENT_OFFSET_14_LEN_DATA_4) != 0;
   }

   byte[] increaseArray(int incr, int position) {
      if (incr < 0) {
         data = getMem().decreaseCapacity(data, 0 - incr, position);
      } else {
         data = getMem().increaseCapacity(data, incr, position);
      }
      return data;
   }

   /**
    * Increment the Dynamic Header field. 
    * Calling this method also increases the array and
    * @param incr
    */
   public void incrementDynHeader(int incr) {
      expandArrayInternal(incr, index + getLengthFullHeaders());
      increment(AGENT_OFFSET_16_LEN4, 4, incr);
      increment(AGENT_OFFSET_12_LEN_HEADER_DYN2, 2, incr);
   }

   /**
    * Increment the {@link ITechObjectManaged#AGENT_OFFSET_16_LEN4} field.
    * <br>
    * <br>
    * Used when increasing/decreasing suffix trailers.
    * <br>
    * <br>
    * 
    * @param incr
    */
   protected void incrementLength(int incr) {
      this.incrementNoVersion(AGENT_OFFSET_16_LEN4, 4, incr);
   }

   protected void incrementLengthBuffer(int incr) {
      this.incrementNoVersion(AGENT_OFFSET_16_LEN4, 4, incr);
      this.incrementNoVersion(AGENT_OFFSET_15_LEN_BUFFER_3, 3, incr);
   }

   /**
    * 
    * @param incr
    */
   protected void incrementLengthData(int incr) {
      this.incrementNoVersion(AGENT_OFFSET_16_LEN4, 4, incr);
      this.incrementNoVersion(AGENT_OFFSET_14_LEN_DATA_4, 4, incr);
   }

   /**
    * 
    * @param incr
    */
   public void incrementOffset(int incr) {
      index += incr;
      offsetChanged();
   }

   /**
    * Called by {@link ByteController} when merged within a bigger strucuture, references
    * are updated by an increment
    * <br>
    * @param incr
    */
   public void incrementREFID(int incr) {

   }

   /**
    * Initialize critical fields
    */
   protected void initBOManaged() {
      if (get4(AGENT_OFFSET_16_LEN4) == 0) {
         setValueNoVersion(AGENT_OFFSET_16_LEN4, super.getLength(), 4);
      }
      setValueNoVersion(A_OBJECT_OFFSET_1_TYPE1, AGENT_BASE_TYPE, 1);
      setValueNoVersion(A_OBJECT_OFFSET_3_LENGTH2, 0xFFFF, 2);
      //init field with zero
      if (get4(AGENT_OFFSET_14_LEN_DATA_4) == 0) {
         setValueNoVersion(AGENT_OFFSET_14_LEN_DATA_4, 0, 4);
      }
   }

   /**
    * Called by
    * <li> {@link ByteController} once memory source has been set up
    * <li> Programmer who creates manually a new instance
    * @return
    */
   public ByteObjectManaged initMe() {
      setFlag(AGENT_OFFSET_01_FLAG_1, AGENT_FLAG_CTRL_7_DATA_UNLOADED, false);
      serializeReverse();
      return this;
   }

   /**
    * Returns true when Agent is alone in its byte array
    * @return
    */
   public boolean isAlone() {
      return hasFlag(AGENT_OFFSET_03_FLAGZ_1, AGENT_FLAGZ_CTRL_2_EXPULSED);
   }

   public boolean isMagicValid() {
      return getMagicWord() == AGENT_MAGIC_WORD;
   }

   public boolean isMemoryConstrained() {
      if (byteCon != null) {
         return byteCon.isRunningLeanMemory();
      }
      return false;
   }

   /**
    * We want to know whether a {@link ByteObjectManaged} content has been modified since its initial creation.
    * <br>
    * a {@link ByteObjectManaged} in a {@link ByteController} only belongs to ONE {@link MemorySource}.
    * <br>
    * <li>Is the flag {@link ITechObjectManaged#AGENT_FLAG_CTRL_6_READ_ONLY}
    * <li> {@link ITechByteObject#A_OBJECT_FLAG_7_IMMUTABLE}
    * <li>
    * Flag it when it is obvious
    * <li>Take a CRC check for critical
    * <br>
    * <br>
    * Otherwise assume it has not been modified
    * <br>
    * When byte data is modified, the flag must be set externally
    * @return
    */
   public boolean isModified() {
      if (hasFlag(AGENT_OFFSET_01_FLAG_1, AGENT_FLAG_CTRL_6_READ_ONLY)) {
         return false;
      }
      return !hasFlag(AGENT_OFFSET_03_FLAGZ_1, AGENT_FLAGZ_CTRL_7_SAVED);
   }

   public boolean isRoot() {
      return hasFlag(AGENT_OFFSET_01_FLAG_1, AGENT_FLAG_CTRL_5_ROOT);
   }

   public boolean isSaved() {
      return hasFlag(AGENT_OFFSET_03_FLAGZ_1, AGENT_FLAGZ_CTRL_7_SAVED);
   }

   public boolean isSyncroFlag(int flag) {
      return (syncroFlags & flag) == flag;
   }

   private void magicWordCheck(byte[] array, int offset) {
      if (get2(AGENT_OFFSET_17_MAGIC2) != AGENT_MAGIC_WORD) {
         throw new RuntimeException("Agent Magic Word Missing");
      }
   }

   public void memoryClear() {
      if (byteCon != null) {
         byteCon.memoryClear(memoryMemSrcIndex, memoryMemSrcID);
      }
   }

   /**
    * Sub class makes its best effort to remove its data from memory. It always keep its {@link ITechObjectManaged} header.
    * <br>
    * The flag is set {@link ITechObjectManaged#AGENT_FLAG_CTRL_7_DATA_UNLOADED}
    * <br>
    * {@link ITechObjectManaged#AGENT_FLAG_CTRL_8_DATA_ON_DEMAND}
    * When a method is called, it must reload itself on the {@link ByteController}
    */
   protected void memoryClearSub() {
   }

   public void methodEnds() {
      dataUnLock();
   }

   public void methodEndsWrite() {
      dataUnLock();
   }

   /**
    * 
    */
   private void methodStartReloadCheck() {
      if (hasFlag(AGENT_OFFSET_01_FLAG_1, AGENT_FLAG_CTRL_7_DATA_UNLOADED)) {
         throw new IllegalStateException("ByteObjectManaged#initMe not called after constructor");
      }
      if (hasFlag(AGENT_OFFSET_02_FLAGX_1, AGENT_FLAGX_1_GARBAGE_COLLECTED)) {
         //we want to detect if we have different thread accessing our data
         //if our GC happens
         if (isSyncroFlag(SYNCRO_FLAG_1_UNLOADED)) {
            //can we accept other threads trying to access during the reload? NO. So we must sync
            synchronized (this) {
               //check if still unloaded
               dataReload();
            }
         }
      }
   }

   /**
    * Check if another thread is already using this object.
    * <br>
    * Checks if data needs to be reloaded 
    * <li> Byte data was cleared by {@link ByteController}
    * <li> The Constructor never initialized
    * <br>
    * The agent must be flagged to prevent memory loss during a GC inside the method execution.
    * <br>
    * Kind of a synchro flag
    */
   public void methodStarts() {
      dataLock();
      methodStartReloadCheck();
   }

   public void methodStartsWrite() {
      //first lock it so that the GC thread cannot remove it after this
      dataLock();
      methodStartReloadCheck();
   }

   /**
    * Call this method when memory state has changed.
    * offset has shifted or memory is completely gone
    */
   public void offsetChanged() {

   }

   /**
    * Removes all the payload by shrinking the byte array.
    * <br>
    * <br>
    * Keeps the headers and trailers. If a {@link ByteController} , 
    * {@link ByteController#removeData(ByteObjectManaged)}
    * <br>
    * 
    */
   public void removeData() {
      if (byteCon != null) {
         byteCon.removeData(this);
      } else {
         expandResetArrayData(0);
      }
   }

   /**
    * Zeros data and send it to buffer
    */
   public void resetDataToBuffer() {
      int dataSize = get4(AGENT_OFFSET_14_LEN_DATA_4);
      int start = getDataOffsetStartLoaded();
      int index = start;
      for (int i = 0; i < dataSize; i++) {
         data[index] = 0;
         index++;
      }
      incrementNoVersion(AGENT_OFFSET_14_LEN_DATA_4, 4, -dataSize);
      incrementNoVersion(AGENT_OFFSET_15_LEN_BUFFER_3, 3, dataSize);

   }

   /**
    * 
    */
   public void saveAgent() {
      byteCon.saveAgent(this);
   }

   /**
    * Packs the whole {@link ByteObjectManaged} and all its sub components into a single byte array
    * wrapped in a {@link ByteController} header.
    * <br>
    * Sets the flag root in this one.
    * <br>
    * If the {@link ByteObjectManaged} is controlled by the {@link ByteController},
    * it is ignored because it can be part of a bigger structure.
    * the method {@link ByteController#serializeAll()}
    * 
    * According to the {@link ByteObject#createByteObjectFromWrap(BOCtx, BytesIterator)} .
    * <br>
    * This method breaks the one to one
    * 
    * {@link ITechObjectManaged#AGENT_FLAG_CTRL_5_ROOT} is set to true
    */
   public byte[] serializePack() {
      ByteController bc = new ByteController(boc);
      byte[] array = null;
      synchronized (data) {
         //sets the root 
         this.setFlag(AGENT_OFFSET_01_FLAG_1, AGENT_FLAG_CTRL_5_ROOT, true);
         serializeTo(bc);
         this.setFlag(AGENT_OFFSET_01_FLAG_1, AGENT_FLAG_CTRL_5_ROOT, false);
         array = bc.serializeAll();
      }
      return array;
   }

   public byte[] serializeRawHelper(byte[] data) {
      byte[] header = super.toByteArray();
      ByteObjectManaged tr = new ByteObjectManaged(boc, header);
      if (data != null) {
         tr.expandResetArrayData(data.length);
         int offsetData = tr.getDataOffsetStartLoaded();
         byte[] mdata = tr.getByteObjectData();
         System.arraycopy(data, 0, mdata, offsetData, data.length);
      }
      return tr.getByteObjectData();
   }

   /**
    * 
    * @param dataSize
    * @return
    * @throws IOException
    */
   public BADataOS serializeRawHelper(int dataSize) {
      BAByteOS bos = new BAByteOS(boc.getUCtx(), dataSize + 100);
      ByteObjectManaged tr = cloneBOMHeader();
      int size = tr.getLength();
      tr.expandResetArrayData(dataSize);
      BADataOS dos = new BADataOS(boc.getUCtx(), bos);
      dos.write(tr.data, tr.index, size);//trick here to copy header
      return dos;
   }

   /**
    * Called by the {@link ByteController} once the Object has to read its state from the byte array.
    * <br>
    * For those objects whose state IS the byte array, this method does nothing.
    * <br>
    * 
    * @param bc
    * @param byc
    * @param params
    */
   public void serializeReverse() {

   }

   /**
    * Copies the content of the agent to the {@link ByteController} by creating
    * a new agent assigning the {@link ByteController} to its field.
    * <br>
    *  when the{@link ByteObjectManaged} doesn't have a {@link ByteController}.
    * <br>
    * All agents are added to the {@link ByteController} in parameters and will be serialized.
    * <br>
    * The agents state is NOT modified whatsoever. The method takes a snapshot of the state.
    * <br>
    * The snapshot content is modified as followed:
    * <li> Flag {@link IObjectManaged#}
    * <li> Field {@link ITechObjectManaged#AGENT_OFFSET_08_REF_ID2} is set according
    * to entry
    * <br>
    * What if {@link ByteController} has special MemorySource rule? GID are mapped equally
    * or sent to 0
    * <br>
    * The simple case is when data is packed into a single byte source.
    * In this case, reference id is sequential.
    * <br>
    *  are up to date in the {@link ByteController}
    * <br>
    * First call {@link ByteController#isSerializationNeeded()}
    * @param bc
    * @return return the new {@link ByteObjectManaged} that was created in the {@link ByteController}
    */
   public ByteObjectManaged serializeTo(ByteController bc) {
      return this;
   }

   public void serializeToCheck(ByteController bc) {
      if (byteCon != null && byteCon.isSerializationNeeded(this)) {
         serializeTo(bc);
      }
   }

   /**
    * Used internally by {@link ByteController}.
    * @param controller
    */
   void setByteController(ByteController controller) {
      byteCon = controller;
   }

   protected void setInitNoload() {
      setFlag(AGENT_OFFSET_01_FLAG_1, AGENT_FLAG_CTRL_7_DATA_UNLOADED, true);
   }

   protected void setLength(byte[] bytes, int offset, int len) {
      IntUtils.writeIntBE(bytes, offset + AGENT_OFFSET_16_LEN4, len);
   }

   /**
    * Sets offset to 0
    * @param array
    */
   public void setMemory(byte[] array) {
      data = array;
      index = 0;
   }

   public void setOffset(int offset) {
      index = offset;
      offsetChanged();
   }

   public void setSaveFlag(boolean b) {
      setFlag(AGENT_OFFSET_03_FLAGZ_1, AGENT_FLAGZ_CTRL_7_SAVED, b);
   }

   /**
    * Sets the Semaphore on which {@link MutexSignal#release()}
    * will be called when {@link ByteObjectManaged#methodEnds()} is called.
    * <br>
    * @param sema
    */
   public void setSemaphoreLock(MutexSignal sema) {
      sem = sema;
   }

   public void setSyncroFlag(int flag, boolean b) {
      if (b)
         syncroFlags = syncroFlags | flag;
      else
         syncroFlags = syncroFlags & ~flag;
   }

   public void setValueFrom(ByteObjectManaged ob, int offset, int size) {
      int v = ob.getValue(offset, size);
      this.setValue(offset, v, size);
   }

   public byte[] toByteArray() {
      return super.toByteArray();
   }

   //#mdebug
   public IDLog toLog() {
      return boc.getUCtx().toDLog();
   }

   public void toString(Dctx dc) {
      dc.root(this, "ByteObjectManaged");
      dc.nl();
      dc.append("type=" + getType());
      dc.append(" classid=" + getIDClass());
      //module.toString(dc, this);

      dc.nlLvl("", byteCon);
   }

   public void toString1Line(Dctx sb) {
      sb.root(this, "ByteObjectManaged ");
      sb.append(toStringType());
      sb.appendPretty(" SrcIndex=", memoryMemSrcIndex, 10);
      sb.appendPretty(" MemSrcID=", memoryMemSrcID, 10);
      sb.appendPretty(" ref=", get2(AGENT_OFFSET_08_REF_ID2), 10);
      sb.append(" clid=" + get2(AGENT_OFFSET_05_CLASS_ID2));
      sb.append(" intid=" + get2(AGENT_OFFSET_04_INTERFACE_ID2));
      sb.append(" gid=" + get2(AGENT_OFFSET_06_GSOURCE_ID2) + " iid=" + get2(AGENT_OFFSET_07_INSTANCE_ID2));
      sb.append("\ttype=" + getType() + " len=" + getLength() + " bytes");
   }
   //#enddebug
}
