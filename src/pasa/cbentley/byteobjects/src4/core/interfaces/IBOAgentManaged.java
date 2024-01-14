/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.byteobjects.src4.core.interfaces;

import pasa.cbentley.byteobjects.src4.core.ByteController;
import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.core.ByteObjectManaged;
import pasa.cbentley.byteobjects.src4.core.LockManager;
import pasa.cbentley.byteobjects.src4.ctx.IBOTypesBOC;
import pasa.cbentley.byteobjects.src4.sources.MemorySource;
import pasa.cbentley.core.src4.memory.IMemory;

/**
 * All structures that are {@link ByteObjectManaged} will implement this byte structure specification.
 * <br>
 * <br>
 * @author Charles Bentley
 *
 */
public interface IBOAgentManaged extends IByteObject {

   /**
    *  This is the type used in {@link IByteObject#A_OBJECT_OFFSET_1_TYPE1} for all
    */
   public final static int AGENT_BASE_TYPE                  = IBOTypesBOC.TYPE_035_OBJECT_MANAGED;

   /**
    * 36 bytes + 4 bytes base Object
    */
   public final static int AGENT_BASIC_SIZE                 = A_OBJECT_BASIC_SIZE + 36;

   /**
    * When true, there is a CRC/HASH Header validating Header and Data.
    * <br>
    * The Hash Header is actually a ByteObject.
    * <br>
    * If the framework support the algorithm it will check the bytes
    */
   public final static int AGENT_FLAG_CTRL_1_HASHED         = 1 << 1;

   /**
    * By design, this {@link ByteObjectManaged} cannot be shared by several {@link ByteController}
    * <br>
    * The {@link ByteController} to which 
    * <br>
    * 
    */
   public final static int AGENT_FLAG_CTRL_2_EXCLUSIVE      = 1 << 1;

   /**
    * Data Corrupted
    */
   public final static int AGENT_FLAG_CTRL_3_CORRUPTED      = 1 << 2;

   /**
    * This flag is set on a {@link ByteObjectManaged} when the data is not always synchronized
    * with the byte array.
    * <br>
    * When this flag is not set, the method {@link ByteObjectManaged#serializePack()}
    * {@link ByteObjectManaged#getByteObjectData()} returns the agent as a whole already
    * and  {@link ByteObjectManaged#serializeRaw()} call is not needed
    * <br>
    * Usually build mode object store data in class based data structures
    */
   public final static int AGENT_FLAG_CTRL_4_UNPACKED       = 1 << 3;

   /**
    * When {@link IBOAgentManaged#AGENT_FLAG_CTRL_4_UNPACKED}, this flag is set for the root header.
    */
   public final static int AGENT_FLAG_CTRL_5_ROOT           = 1 << 4;

   /**
    * Method {@link ByteObjectManaged#isModified()} will return false
    * and the {@link ByteController} will never write agent to {@link MemorySource}.
    * <br>
    * <br>
    * {@link IByteObject#A_OBJECT_FLAG_7_IMMUTABLE} does not work because we still want to be able
    * to make modifications without generating an exception. Just that those modifications will not be
    * automically saved to the {@link MemorySource}.
    * <br>
    * <br>
    * In Build Mode, a {@link ByteController} will save even if this flag is set.m
    */
   public final static int AGENT_FLAG_CTRL_6_READ_ONLY      = 1 << 5;

   /**
    * Set when data was cleared and must be reloaded with the {@link ByteController}
    */
   public final static int AGENT_FLAG_CTRL_7_DATA_UNLOADED  = 1 << 6;

   /**
    * When this flag is set, the data is never kept in memory.
    * Consequence: the MemorySource
    * Use Case: Code reads big fat data, extract information
    * Better to use Manual Unload in this case?
    */
   public final static int AGENT_FLAG_CTRL_8_DATA_ON_DEMAND = 1 << 7;

   /**
    * When set, this object can be collected by {@link ByteController}.
    * <br>
    * Usually set by {@link ByteController} when creating the object.
    * <br>
    * When freed, the {@link ByteController}
    * sets the {@link IBOAgentManaged#AGENT_FLAG_CTRL_7_DATA_UNLOADED}
    * and a synchronize check is needed.
    * <br>
    */
   public final static int AGENT_FLAGX_1_GARBAGE_COLLECTED  = 1 << 0;

   /**
    * When set, read and writes are protected by locks.
    * <br>
    * The structure will be used by several threads concurently
    */
   public final static int AGENT_FLAGX_2_MUTLI_THREAD       = 1 << 1;

   /**
    * All calls on
    * <li> {@link ByteObjectManaged#methodStarts()}
    * <li> {@link ByteObjectManaged#methodStartsWrite()}
    * will use inside a synchronized
    */
   public final static int AGENT_FLAGX_3_SYNCHRONIZED       = 1 << 2;

   public final static int AGENT_FLAGX_4_                   = 1 << 3;

   public final static int AGENT_FLAGX_5_                   = 1 << 4;

   public final static int AGENT_FLAGX_6_                   = 1 << 5;

   public final static int AGENT_FLAGX_7_                   = 1 << 6;

   public final static int AGENT_FLAGX_8_                   = 1 << 7;

   /**
    * Flag used by the {@link ByteController} to know when an {@link ByteObjectManaged} has already been returned by a
    * get method and is thus being referenced outside its context.
    * <br>
    * Set to false when written to disk.
    */
   public final static int AGENT_FLAGZ_CTRL_1_REFERENCED    = 1;

   /**
    * Set when agent byte array in {@link ByteController} is not inside the controller enveloppe.
    */
   public final static int AGENT_FLAGZ_CTRL_2_EXPULSED      = 2;

   /**
    * Set internally by the {@link ByteController} when the Agent has been read from a {@link MemorySource}.
    * <br>
    * <br>
    * Reset to zero when written to disk/read from disk for the first time.
    */
   public final static int AGENT_FLAGZ_CTRL_3_FROM_SOURCE   = 1 << 2;

   /**
    * {@link ByteController} instantiate objects without initializing them.
    * <br>
    * Tech parameters are not instance either.
    * <br>
    * The method {@link ByteObjectManaged#initMe()} does it.
    * <br>
    * <li> That method reads the byte array and creates the object to support the class.
    * <li> if there is no byte array, it called the Empty
    * This flag is set on a Generic {@link ByteObjectManaged}.
    * <br>
    * When agent has not been instantiated by the Factory
    * <br>
    * In effect, {@link ByteController#loadAllAgents()} creates uninstantiated
    * {@link ByteObjectManaged}. Designed 
    * <br>
    * Break constructor loops as well. C creates A, which creates B which creates C.
    * Because all constructors called by Factory are empty shells.
    */
   public final static int AGENT_FLAGZ_CTRL_4_INSTANTIATED  = 1 << 3;

   /**
    * Flag set when agent is being used by one of its method and the byte array
    * cannot be purged by the {@link IMemory} processes.
    */
   public final static int AGENT_FLAGZ_CTRL_5_LOCKED        = 1 << 4;

   /**
    * At least one thread is waiting for the {@link ByteObjectManaged} to unlock.
    * <br>
    * This flag is only modified within the {@link LockManager}.
    * <br>
    * Why? It helps the {@link ByteObjectManaged#dataUnLock()}. One less notification.
    * <br>
    */
   public final static int AGENT_FLAGZ_CTRL_6_WAITING       = 1 << 5;

   /**
    * When flag is set,we have a READ lock in effect
    * <br>
    * Several thread may read lock together.
    */
   public final static int AGENT_FLAGZ_CTRL_7_READ_LOCK     = 1 << 6;

   /**
    * Flag set when {@link ByteObjectManaged} was saved.
    * <br>
    * Reset to zero when modifications are made 
    */
   public final static int AGENT_FLAGZ_CTRL_7_SAVED         = 1 << 6;

   /**
    */
   public final static int AGENT_FLAGZ_CTRL_8_              = 1 << 7;

   /**
    * offset of zero
    */
   public final static int AGENT_MAGIC_WORD                 = Short.MAX_VALUE - 1;

   /**
    * 8 state flags used by the {@link ByteController}.
    * <li> {@link IBOAgentManaged#AGENT_FLAGZ_CTRL_1_REFERENCED}
    * <li> {@link IBOAgentManaged#AGENT_FLAG_CTRL_2_EXCLUSIVE}
    * <li> {@link IBOAgentManaged#AGENT_FLAGZ_CTRL_3_FROM_SOURCE}
    * <li> {@link IBOAgentManaged#AGENT_FLAG_CTRL_4_UNPACKED}
    * <li> {@link IBOAgentManaged#AGENT_FLAG_CTRL_5_ROOT}
    * <li> {@link IBOAgentManaged#AGENT_FLAG_CTRL_6_READ_ONLY}
    * 
    */
   public final static int AGENT_OFFSET_01_FLAG_1           = A_OBJECT_BASIC_SIZE;

   /**
    * 
    */
   public final static int AGENT_OFFSET_02_FLAGX_1          = A_OBJECT_BASIC_SIZE + 1;

   /**
    * Flags set to FALSE when written to disk.
    */
   public final static int AGENT_OFFSET_03_FLAGZ_1          = A_OBJECT_BASIC_SIZE + 2;

   /**
    * Identifies the main API of this agent. Allows the {@link IJavaObjectFactory} to create
    * implementators without having to stick to a class ID.
    * <br>
    * <li>When value is 0, no interface
    * <li>When value is != 0, the value stored here several interface IDs defined in the header of 
    * <li>When last bit is set to 1, the value is a link
    * <br>
    * <br>
    * Stored at a ByteObject for 2 and more values.
    * 0  means no interface
    */
   public final static int AGENT_OFFSET_04_INTERFACE_ID2    = A_OBJECT_BASIC_SIZE + 3;

   /**
    * Identifies the Java class implementing the behavior of the data {@link ByteObjectManaged}.
    * <br>
    * <br>
    * It allows the {@link ByteController} to look for a byte array with a matching class id header.
    * <br>
    * You would ask how can 2 bytes identifies a class? A {@link ByteObjectManaged} header is given
    * to a 
    * <br>
    */
   public final static int AGENT_OFFSET_05_CLASS_ID2        = A_OBJECT_BASIC_SIZE + 5;

   /**
    * Identifies the ID of the {@link MemorySource} which stores this agent's byte array.
    * <br>
    * Another parallel hierarchy to instance. Matches the {@link IBOByteControler#MEMC_OFFSET_04_CTRL_GROUP_ID2}
    * <br>
    * <br>
    * The MemorySource array order is part of the structure design.
    * <br>
    * 
    */
   public final static int AGENT_OFFSET_06_GSOURCE_ID2      = A_OBJECT_BASIC_SIZE + 7;

   /**
    * Identifies the instance within the {@link IBOAgentManaged#AGENT_OFFSET_06_GSOURCE_ID2}
    * <br>
    * Numerous instances of a given class id inside the same byte array are sorted using this ID.
    * <br>
    * <br>
    * Case of per letter CharTrie, each charTrie has one instance ID.
    * <br>
    * <br>
    * In case of several instance ids, the flag {@link IBOAgentManaged#AGENT_FLAGZ_CTRL_3_FROM_SOURCE} points
    * Instance ID. used to identify the Root 
    */
   public final static int AGENT_OFFSET_07_INSTANCE_ID2     = A_OBJECT_BASIC_SIZE + 9;

   /**
    * Reference ID which is used to directly and precisely identify a {@link ByteObjectManaged} in a cloud
    * of objects within the scope of a {@link ByteController}.
    * <br>
    * Its relative to the ByteController which created it.
    * <br>
    * Scope is inside the {@link ByteController} tree. But could be outside.
    * When first created, the {@link IBOAgentManaged} reference ID is unknown as zero.
    * The reference ID will be set when an {@link IBOAgentManaged} is first added to a {@link ByteController}.
    * <br>
    * There might be reference clashes when an agent was created in another {@link ByteController}
    */
   public final static int AGENT_OFFSET_08_REF_ID2          = A_OBJECT_BASIC_SIZE + 11;

   /**
    * Identifies the {@link ByteController}
    * <br>
    * When zero. not defined. Could be any.
    */
   public final static int AGENT_OFFSET_09_CTRL_ID2         = A_OBJECT_BASIC_SIZE + 13;

   /**
    * Used as a high level ID
    */
   public final static int AGENT_OFFSET_10_PAKAGE_ID2       = A_OBJECT_BASIC_SIZE + 15;

   /**
    * Used as a high level ID
    */
   public final static int AGENT_OFFSET_11_APP_ID2          = A_OBJECT_BASIC_SIZE + 17;

   /**
    * Dynamic Header. Put at the end after the fixed header. Each field accessing
    * a dynamic header, must record in the static the offset and the number of bytes
    * used.
    */
   public final static int AGENT_OFFSET_12_LEN_HEADER_DYN2  = A_OBJECT_BASIC_SIZE + 19;

   /**
    * Number of bytes consumed by the static header.
    * {@link IBOAgentManaged#AGENT_OFFSET_16_LEN4}.
    * <br>
    * Dynamic header is computed inside the data means that offsets of sub object are dynamic too.
    * <br>
    * The length of the header after this header. Includes all the sub headers!
    * <br>
    * For Dynamic Headers, we cannot put them in the data because data can be erased.
    * <br>
    * Each lvl creates dynamic header
    */
   public final static int AGENT_OFFSET_13_LEN_HEADER2      = A_OBJECT_BASIC_SIZE + 21;

   /**
    * Number of bytes for data.
    * Does not include the header length and buffer.
    * <br>
    * <br>
    * Note that in Java, the maximum size of a byte array is {@link Integer#MAX_VALUE} (around 2.1 gb).
    * <br>
    * When objects reach this limit, cut them in several {@link ByteObjectManaged}
    * each with their separate byte array.
    */
   public final static int AGENT_OFFSET_14_LEN_DATA_4       = A_OBJECT_BASIC_SIZE + 23;

   /**
    * The last byte of used data by the {@link ByteObjectManaged}.
    * <br>
    * 0 when no meaning.
    * <br>
    * Some structures want a buffer because they know that they will expand frequently
    * before the structure is completed.
    * <br>
    * The buffer bytes is not written to disk
    */
   public final static int AGENT_OFFSET_15_LEN_BUFFER_3     = A_OBJECT_BASIC_SIZE + 27;

   /**
    * The agent data byte length. Sub length see {@link IByteObject#A_OBJECT_OFFSET_3_LENGTH2}
    * <br>
    * <br>
    * Include {@link ByteObject}, {@link ByteObjectManaged} headers and the whole datagram of the object.
    * <br>
    * <br>
    * This field records the length of
    * <li>header
    * <li> composite {@link ByteObject}
    * <li> payload header (includes any adress to referenced payloads)
    * <li> payload
    * <li> any trailer
    * <br>
    * <br>
    * The  {@link IByteObject#A_OBJECT_OFFSET_3_LENGTH2} only records header data.
    * <br>
    * <br>
    * When is this field computed? Before the constructor of {@link ByteObjectManaged} returns.
    */
   public final static int AGENT_OFFSET_16_LEN4             = A_OBJECT_BASIC_SIZE + 30;

   /**
    * Helps Detect data corruption. A {@link ByteObject} is not designed to be serialized and transfered over the metal (risk of corruption).
    * <br>
    * However that's {@link ByteObjectManaged}'s purpose.
    * <br>
    * <br>
    * 
    */
   public final static int AGENT_OFFSET_17_MAGIC2           = A_OBJECT_BASIC_SIZE + 34;

   public static final int AGENT_REFID_BYTE_SIZE            = 2;

   /**
    * Set when data was cleared and must be reloaded with the {@link ByteController}
    */
   public final static int AGENT_ZFLAG_1_DATA_UNLOADED      = 1 << 0;

   /**
    * 
    */
   public final static int AGENT_ZFLAG_2_DATA_UNLOADED      = 1 << 1;

}
