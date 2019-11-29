package pasa.cbentley.byteobjects.src4.tech;

import pasa.cbentley.byteobjects.src4.core.ByteController;
import pasa.cbentley.byteobjects.src4.core.ByteObjectManaged;
import pasa.cbentley.byteobjects.src4.sources.MemorySource;

/**
 * Enveloppe over one or several {@link ITechObjectManaged}.
 * <br>
 * <br>
 * Used to serialize a {@link ByteController}.
 * <br>
 * <br>
 * 
 * @author Charles-Philip
 *
 */
public interface ITechByteControler extends ITechObjectManaged {

   public static final int MEMC_BASIC_SIZE                 = AGENT_BASIC_SIZE + 29;

   /**
    * Default policy.
    * Data is loaded normally. Upon expanding agents are expulsed or not depending
    * on byte ratio within its.
    * <br>
    * All agents are given their own byte array as soon as one agent in the block needs expanding.
    */
   public static final int MEMC_EX_POLICY_0_DEFAULT        = 0;

   /**
    * One memory space for every agents. 
    * <br>So every data source are broken up
    * upon construction
    * <br>
    * Compatible with multiple memory sources
    */
   public static final int MEMC_EX_POLICY_1_MULTIPLE       = 1;

   /**
    * {@link MemController} expansion policy.
    * <br>
    * 
    * Upon modification, controller creates a memory space for the expanding agent only.
    * <br>
    */
   public static final int MEMC_EX_POLICY_2_EXPULSE        = 2;

   /**
    * {@link ByteController} expansion policy that forces a single byte[] array is used to store for all agents/sources
    * <br>
    * All agents are concatenated along their buffer areas.
    * <br>
    * The {@link ByteController} will change the reference of all {@link ByteObjectManaged} everytime
    * the byte array is expanded.
    * <br>
    * The {@link ByteController} uses a buffer area.
    * <br>
    * When using different memory sources, this policy is not possible.
    */
   public static final int MEMC_EX_POLICY_3_SINGLE         = 3;

   /**
    * Each memory space can only be used by 1 and only 1 agent.
    * <br>
    * An exception is thrown otherwise
    */
   public static final int MEMC_FIND_POLICY_0_ONLY_ONCE    = 0;

   /**
    * If no free memory space can be found. reuse the first one.
    */
   public static final int MEMC_FIND_POLICY_1_REUSE        = 1;

   /**
    * true if memory area cannot be modified if true and not enough space, outofmemoryerror is thrown
    */
   public static final int MEMC_FLAG_1_SET_MEMORY          = 1 << 0;

   /**
    * 
    */
   public static final int MEMC_FLAG_2_BUILDER             = 1 << 1;

   /**
    * Flag set by application memory manager when memory is scarse. 
    * <br>
    * Agents should behave in a certain way to minize memory use
    */
   public static final int MEMC_FLAG_3_LEAN_MEMORY         = 1 << 2;

   /**
    * Set when the header is alone without its data
    */
   public static final int MEMC_FLAG_4_HEADER_ALONE        = 1 << 3;

   /**
    * Set once
    */
   public static final int MEMC_FLAGZ_1_MEMORYCLEARED      = 1 << 0;

   public static final int MEMC_HEADER_SIZE                = MEMC_BASIC_SIZE;

   /**
    * Starting size of mem agent array
    */
   public static final int MEMC_INIT_AGENT_NUM_IN_BUFFER   = 15;

   /**
    * Special ID to identify memory controlled byte array
    */
   public static final int MEMC_MAGIC_WORD                 = 1733963483;

   /**
    * Create a byte array whenever it is needed
    */
   public static final int MEMC_MANAGE_POLICY_0_NONE       = 0;

   /**
    * The {@link MemController} has a given fixed amount of bytes.
    * <br>
    * It must manage all agents inside that byte array
    */
   public static final int MEMC_MANAGE_POLICY_1_SET        = 1;

   /**
    * Simplest managing where each agent has a single byte[] area in the main array.
    */
   public static final int MEMC_MANAGE_POLICY_2_AREA_AGENT = 2;

   /**
    * Area for storing the magic word
    */
   public static final int MEMC_OFFSET_00_MAGICWORD4       = AGENT_BASIC_SIZE + 0;

   /**
    * The number of bytes including the {@link MemController} header size.
    * <br>
    * Overrides the {@link ITechByteObject#A_OBJECT_OFFSET_3_LENGTH2} fields.
    */
   public static final int MEMC_OFFSET_01_LEN4             = AGENT_BASIC_SIZE + 4;

   /**
    * When a byte array has to be increased
    * <li> {@link ITechByteControler#MEMC_EX_POLICY_3_SINGLE}
    * <li> {@link ITechByteControler#MEMC_EX_POLICY_1_MULTIPLE}
    * <li> {@link ITechByteControler#MEMC_EX_POLICY_2_EXPULSE}
    * 
    */
   public static final int MEMC_OFFSET_02_MODE1            = AGENT_BASIC_SIZE + 8;

   /**
    * 
    */
   public static final int MEMC_OFFSET_03_POLICY_MODE1     = AGENT_BASIC_SIZE + 9;

   /**
    * Controller Group ID will indicate agents from a group
    * 2 bytes
    */
   public static final int MEMC_OFFSET_04_CTRL_GROUP_ID2   = AGENT_BASIC_SIZE + 10;

   /**
    * Number of agents stored in this header. Agents not stored here
    */
   public static final int MEMC_OFFSET_05_NUM_AGENTS3      = AGENT_BASIC_SIZE + 12;

   /**
    * Position within the enveloppe of the root.
    * <br>
    * Reference ID of the root agent of this controller.
    * <br>
    * {@link ITechObjectManaged#AGENT_OFFSET_08_REF_ID2}
    */
   public static final int MEMC_OFFSET_06_ROOT_REF2        = AGENT_BASIC_SIZE + 15;

   public static final int MEMC_OFFSET_07_ARRAY_START1     = AGENT_BASIC_SIZE + 17;

   /**
    * Increment value when agent array is too small
    * Number of free slots created.
    */
   public static final int MEMC_OFFSET_08_ARRAY_INCREMENT1 = AGENT_BASIC_SIZE + 18;

   /**
    * For
    * <li> {@link ITechByteControler#MEMC_FLAG_1_SET_MEMORY} 
    * <li> {@link ITechByteControler#MEMC_FLAG_2_BUILDER} 
    * 
    */
   public static final int MEMC_OFFSET_09_FLAG1            = AGENT_BASIC_SIZE + 19;

   public static final int MEMC_OFFSET_10_FLAGZ1           = AGENT_BASIC_SIZE + 20;

   public static final int MEMC_OFFSET_11_APP_ID2          = AGENT_BASIC_SIZE + 21;

   /**
    * Links the {@link ByteController} to a {@link MemorySource}.
    * <br>
    * When Zero, the {@link ByteController} is a Master root.
    */
   public static final int MEMC_OFFSET_11_SOURCE_ID2       = AGENT_BASIC_SIZE + 23;

   /**
    * Version of Java code to reading the byte object inside.
    */
   public static final int MEMC_OFFSET_12_VERSION2         = AGENT_BASIC_SIZE + 25;

   /**
    * 
    */
   public static final int MEMC_OFFSET_13_MODULE_ID2       = AGENT_BASIC_SIZE + 27;

}
