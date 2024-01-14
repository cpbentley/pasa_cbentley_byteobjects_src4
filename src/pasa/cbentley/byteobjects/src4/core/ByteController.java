/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.byteobjects.src4.core;

import pasa.cbentley.byteobjects.src4.core.interfaces.IJavaObjectFactory;
import pasa.cbentley.byteobjects.src4.core.interfaces.IBOByteControler;
import pasa.cbentley.byteobjects.src4.core.interfaces.IBOAgentManaged;
import pasa.cbentley.byteobjects.src4.ctx.BOCtx;
import pasa.cbentley.byteobjects.src4.ctx.IBOTypesBOC;
import pasa.cbentley.byteobjects.src4.ctx.IToStringFlagsBO;
import pasa.cbentley.byteobjects.src4.sources.ByteArraySource;
import pasa.cbentley.byteobjects.src4.sources.JarSource;
import pasa.cbentley.byteobjects.src4.sources.MemorySource;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.logging.ITechLvl;
import pasa.cbentley.core.src4.memory.IMemFreeable;
import pasa.cbentley.core.src4.memory.IMemory;
import pasa.cbentley.core.src4.stator.IStatorable;
import pasa.cbentley.core.src4.structs.IntBuffer;
import pasa.cbentley.core.src4.structs.synch.MutexSignal;
import pasa.cbentley.core.src4.thread.IBProgessable;
import pasa.cbentley.core.src4.utils.BitUtils;
import pasa.cbentley.core.src4.utils.IntUtils;

/**
 * Controls the loading, expansion and saving of {@link ByteObjectManaged}'s memory spaces inside a datastructure.
 * <br>
 * Much more fine grained control unlike the {@link IStatorable} base specification.
 * A class will create a {@link ByteController} if the {@link IStatorable} does not load any
 * <br>
 * In the context of an Application, IMemo
 * <br>
 * 
 * Each {@link ByteObjectManaged} of the structure has a reference to the {@link ByteController}. When that blocks needs more memory, it expands through the controller.
 * The controller manages the propagation to other blocks.
 * <br>
 * <br>
 * The memory controller also manages the initialization of memory spaces for {@link ByteObjectManaged}
 * <br>
 * <br>
 * <b>Key Method</b>
 * <li> {@link ByteController#addAgent(ByteObjectManaged)}
 * <li> {@link ByteController#getAgentFromRefOrCreate(int, ByteObjectManaged, int)}
 * <li> {@link ByteController#addAgent(ByteObjectManaged)}
 * 
 * <br>
 * <br>
 * <b>Key Methods</b> on {@link ByteObjectManaged}
 * <li> {@link ByteObjectManaged#serializeTo(ByteController)}
 * <li>
 * <br>
 * A data structure like a Trie is composed of several {@link ByteObjectManaged} and each of them might use different {@link MemorySource}. 
 * <br>
 * <li> {@link ByteArraySource}
 * <li> {@link JarSource}
 * <li> RecordStoreSource.
 * <br>
 * <br>
 * <b>Creation</b>
 * <br>
 * 1)Create a Trie. Serialize using {@link ByteObjectManaged#serializePack()}. Save the byte array as a single block. 
 * <br>
 * 2)Create a Trie. Fill it. Create a {@link ByteController} with several memory sources. Each {@link ByteObjectManaged} is assigned several IDs.
 * {@link ByteController} links each Trie component to its memory source using {@link IBOAgentManaged#AGENT_OFFSET_06_GSOURCE_ID2} for the memory source
 * and {@link IBOAgentManaged#AGENT_OFFSET_07_INSTANCE_ID2} for the instance in that memory source.
 * Call {@link ByteObjectManaged#serializeTo(ByteController)} and then {@link ByteController#saveAgents()}
 * <br>
 * <br>
 * Every chunk of memory from {@link MemorySource} is wrapped in a byte header defined by {@link IBOByteControler}.
 *  
 * <br>
 * <b>Loading</b>
 * <br>
 * 1) {@link MemorySource} is created.  {@link ByteController} is created. The {@link ByteController#getAgentRoot()}
 * <br>
 * 2) When loading from several memory sources, the order is decided by the {@link ByteController} header.
 * <br>
 * <br>
 * 
 * <b>Combining several structures</b>
 * A Trie is used independently in a context. In another context, several tries are components of a bigger Trie.
 * A {@link ByteController} repository tracks at the application level
 * <br>
 * We want {@link ByteController} to merge. Depending on the which {@link ByteController} is root, the root structure is different.
 * 
 * <br>
 * <br>
 * 
 * Trie BB references all the words in a database of quotes. Which links back to the 
 * When loading a structure, one creates the {@link ByteController} with one or several {@link MemorySource}. Then initialize
 * the {@link ByteObjectManaged} structures with that {@link ByteController}.
 * <br>
 * The byte format recognized by the {@link ByteController} the one produced by {@link ByteObject#toByteArray()}
 * <br>
 * 
 * This would usually be the case for MULTIPLE mode<br>
 * The sources are given by the controller. Sources are order based<br>
 * <br>
 * The application who creates the memory controller provides the memory spaces<br>
 * and will know where to save serialization of memory spaces, if needed<br>
 *  <br>
 * Serialization of agent memory space<br>
 * <br>
 * DataSources are given. At least one.<br>
 * The rows are the agent count. i<br>
 * The offset to be used is computed from 0<br>
 * The length of the agent is serialized at the front<br>
 * <br>
 * <br>
 * <b>CASE 1</b>: Monolith READ ONLY<br>
 * <br>
 * User application reads a single byte[] array and automatically loads the agents<br>
 * [CharCollector][TrieNodes][TrieData]<br>
 *  <br>
 * <b>CASE 2</b>: Monolith WRITE<br>
 * <br>
 * <br>
 * <b>CASE 3</b>: MULTIPLE WRITE<br>
 * User application reads several byte[] array. (CharCollector,TrieNodes,TrieDatas)<br>
 * Loads them in the ByteController using IntToObjects<br>
 * ByteObjectManaged contructor initialize the memory space<br>
 * <br>
 * <b>AUTOMATIC SAVING</b><br>
 * seeding idea: a CharTrie may have a personalized frequency index. This index tracks the last
 * used words for a prefix so that these words are showed first when the same prefix is typed again by the user.
 * We would like the application to automatically save this information when the application closes and when
 * the application idles for more than 10 seconds. The save would only occurs if data has been modified.
 * <br>
 * Once the event AUTO_SAVE is fired, All Memory Controller are called to check for AUTO_SAVE.
 * Inside a controller, each memory agents is checked. Memory agents have a field _modified set to true 
 * when memory space is expanded. If modified, save method is called by the Controller. The controller
 * uses the Agent Savers object to save the byte data to the correct destination.
 * (Identical agents will be treated in the same order as they were created.)
 * <br>
 * Since ByteController is generic, the Saver class is abstract. Custom implementations for ByteArrayOutputStream
 * RecordStore, InternetStore, Bluetooth saves are the responsibility of the user.
 * When a save is required, the save method is called on all Savebles who have been modified.
 * In Expulsed mode, if an agent's memory block has been expanded, the whole block is serialized again
 * and saved
 * <br>
 * - To prevent memory leaks, when a memory space is expanded, source memory array reference should be set to null
 * <br>

 * <br>
 * <br>
 * @author Charles Bentley
 *
 */
public class ByteController extends ByteObjectManaged implements IBOByteControler, IMemFreeable {

   /**
    * Index of the first free slot in agent array.
    * <br>
    * 
    */
   private int                   agentIndex = 0;

   /**
    * Array of agents that were expulsed of their envelope due to policy
    */
   byte[]                        agentsExpulsed;

   /**
    * the {@link ByteObjectManaged}s controlled by this {@link ByteController}.
    * <br>
    * The index of this array is {@link ByteObjectManaged#memoryAgentIndex}
    * <br>
    * An agent is created from a byte array loaded from a {@link MemorySource}
    * or
    * In construction mode, it is added programmatically by the build method {@link ByteController#addAgent(ByteObjectManaged)}.
    * <br>
    * Array is never null after construction.
    * <br>
    * Agent references inside the array can be null. 
    */
   ByteObjectManaged[]           agentsRefArray;

   /** 
    * Collection of {@link ByteController} controlled by the {@link ByteController}.
    * <br>
    * 
    * Headers of root ByteController from ith {@link MemorySource}. 
    * <br>
    * {@link IBOByteControler} headers that match the {@link ByteController}.
    * <br>
    * When the flag is set, the header is alone and none of the data is not loaded
    * {@link IBOByteControler#MEMC_FLAG_4_HEADER_ALONE}.
    * <br>
    * Initialized during constructor with size of MemorySource array.
    * <br>
    * When null, the data has not been loaded yet.
    */
   private ByteObjectManaged[][] bcse;

   /**
    * {@link MemorySource} references to be used for loading data and saving {@link ByteObjectManaged}s.
    * <br>
    * <br>
    * All {@link ByteObjectManaged}s have are wrapped in {@link MemorySource}.
    * <br>
    * The most basic one being {@link ByteArraySource} which is just a wrapper around a byte array.
    * <br>
    * This abstracts the source of the data and gives the {@link ByteController} the possibility to load
    * byte arrays on demand. This is especially useful performance-wise when loading big datastructures fragmented
    * over several files. 
    * <br>
    * <br>
    * 
    * The array of {@link MemorySource}s is given during the construction of the {@link ByteController}.
    * <br>
    * <br>
    * Where is the array referenced? The root 0 is stored at the same index value.
    * <br>
    * <br>
    * Never null once the constructor return
    */
   MemorySource[]                dataSources;

   /**
    * Factory for creating instance of {@link ByteObjectManaged} based on {@link IBOAgentManaged#AGENT_OFFSET_05_CLASS_ID2}
    * <br>
    * <br>
    * If none is set, uses the 
    */
   private IJavaObjectFactory    factory;

   private int                   flags;

   private boolean               isDataSourceLoad;

   /**
    * Every {@link ByteController} has a root agent which.
    * <br>
    * Case when a {@link ByteController} is created out of a {@link MemorySource} not tagged
    * with a root agent? The first one is set as root.
    */
   private ByteObjectManaged     rootAgent;

   public ByteController(BOCtx mod) {
      this(mod, null, mod.getByteControllerFactory().getTechDefault());
   }

   public void setFactory(IJavaObjectFactory factory) {
      this.factory = factory;
   }

   /**
    * Reads the {@link ByteController} header from the byte array, unless it is a single {@link ByteObjectManaged}
    * <br>
    * <br>
    * 
    * @param mod
    * @param factory
    * @param singleSource
    */
   public ByteController(BOCtx mod, IJavaObjectFactory factory, byte[] singleSource) {
      this(mod, factory, singleSource, 0);
   }

   public ByteController(BOCtx mod, IJavaObjectFactory fac) {
      this(mod, fac, mod.getByteControllerFactory().getTechDefault());
   }

   public ByteController(BOCtx mod, byte[] singleSource) {
      this(mod, null, singleSource, 0);
   }

   public ByteController(BOCtx mod, IJavaObjectFactory factory, byte[] singleSource, int offset) {
      this(mod, factory, mod.getByteControllerFactory().getArray(singleSource, offset));
   }

   /**
    * Use this constructor for building a {@link ByteController} from scratch.
    * <br>
    * 
    * @param mod
    * @param fac
    * @param tech
    */
   public ByteController(BOCtx mod, IJavaObjectFactory fac, ByteObjectManaged tech) {
      super(mod, tech);
      this.setFactory(fac);
      if (mod == null)
         throw new NullPointerException();
      initStructs(0);
   }

   public ByteController(BOCtx mod, ByteObjectManaged tech) {
      super(mod, tech);
      if (mod == null)
         throw new NullPointerException();
      initStructs(0);
   }

   public ByteController(BOCtx mod, IJavaObjectFactory factory, ByteObjectManaged tech, MemorySource dataSources) {
      this(mod, factory, tech, new MemorySource[] { dataSources });
   }

   /**
    * 
    * @param mod
    * @param factory
    * @param tech
    * @param dataSources
    */
   public ByteController(BOCtx mod, IJavaObjectFactory factory, ByteObjectManaged tech, MemorySource[] dataSources) {
      super(mod, tech);
      if (factory == null) {
         throw new NullPointerException("IBytefactory cannot be null");
      }
      this.setFactory(factory);
      initStructs(dataSources.length);
      for (int i = 0; i < dataSources.length; i++) {
         //this.arraysByte[i] = dataSources[i].preload();
         this.dataSources[i] = dataSources[i];
         //for each data source, get the header
         byte[] data = dataSources[i].loadHeader(MEMC_BASIC_SIZE);
      }
      mod.getUCtx().getMem().addMemFreeable(this);
   }

   /**
    * Single Source with ID parameters
    * Agents are instanced with an ID.
    * <br>
    * <br>
    * @param fac {@link IJavaObjectFactory}
    * @param dataSource
    */
   public ByteController(BOCtx mod, IJavaObjectFactory fac, MemorySource dataSource) {
      this(mod, fac, mod.getByteControllerFactory().getTechDefault(), new MemorySource[] { dataSource });
   }

   /**
    * Reads the Tech from the first {@link MemorySource}
    * @param mod
    * @param factory
    * @param tech
    * @param dataSources
    */
   public ByteController(BOCtx mod, IJavaObjectFactory factory, MemorySource[] dataSources) {
      super(mod, mod.getByteControllerFactory().getTechFromSource(dataSources));
      this.setFactory(factory);
      initStructs(dataSources.length);
      for (int i = 0; i < dataSources.length; i++) {
         //this.arraysByte[i] = dataSources[i].preload();
         this.dataSources[i] = dataSources[i];
      }
      mod.getUCtx().getMem().addMemFreeable(this);
   }

   /**
    * Adds the {@link ByteObjectManaged} to the {@link ByteController} internal list. 
    * <br>
    * <br>
    * This method is called when building
    * The {@link ByteObjectManaged} links to 
    * Check is byte array reference is already controlled.
    * <br>
    * <br>
    * <b>PRE:</b> {@link ByteObjectManaged}'s {@link ByteController} is null or already belongs
    * <br>
    * <b>POST:</b> the agent is linked to the controller.
    * Adds the Agent to the list. Agent takes next index.
    * Since the memory area is known
    * <br>
    * The agent is linked to the data source {@link IBOAgentManaged#AGENT_OFFSET_06_GSOURCE_ID2}.
    * 
    * <br>
    * @param agent {@link ByteObjectManaged}
    * @throws IllegalArgumentException when {@link ByteObjectManaged} is already managed by another {@link ByteController}.
    */
   public void addAgent(ByteObjectManaged agent) {
      //#debug
      toDLog().pMemory("", agent, ByteController.class, "addAgent");

      if (agent.getByteController() != null && agent.getByteController() != this) {
         throw new IllegalArgumentException("Agent cannot belong to 2 different ByteController");
      }
      //sets source id
      if (agent.memoryMemSrcIndex == -1) {
         agent.memoryMemSrcIndex = agent.get2(AGENT_OFFSET_06_GSOURCE_ID2);
         //if gid is not compatible with ByteController setup? use the 0,0 position
      }
      if (agent.memoryMemSrcIndex >= dataSources.length) {
         agent.memoryMemSrcIndex = 0;
      }
      if (agent.memoryMemSrcID == -1) {
         agent.memoryMemSrcID = agent.get2(AGENT_OFFSET_07_INSTANCE_ID2);
      }
      agent.setByteController(this);
      agent.setSaveFlag(false); //flag agent as not saved

      //the first agent is set as root. one agent can be the root of one 
      if (agent.hasFlag(AGENT_OFFSET_01_FLAG_1, AGENT_FLAG_CTRL_5_ROOT)) {
         rootAgent = agent;
         this.set2(MEMC_OFFSET_06_ROOT_REF2, agent.get2(AGENT_OFFSET_08_REF_ID2));
      }
      //first agent is root
      if (rootAgent == null) {
         rootAgent = agent;
      }
      if (agent.get2(AGENT_OFFSET_08_REF_ID2) == 0) {
         agent.set2(AGENT_OFFSET_08_REF_ID2, getNextReferenceID());
      }
      // Add the Agent and decides what should be his reference ID.
      if (agent.memoryAgentIndex == -1) {
         addAgentToListAgent(agent);
      }

   }

   /**
    * physically create space for the new agent and link the agent to the {@link ByteController}
    * <br>
    * <br>
    * 
    * @param agent
    */
   private synchronized void addAgentToListAgent(ByteObjectManaged agent) {
      //we have a virgin agent. ByteController in build mode
      if (agentIndex + 1 >= agentsRefArray.length) {
         int buffIncr = get1(MEMC_OFFSET_08_ARRAY_INCREMENT1);
         if (buffIncr <= 0) {
            buffIncr = 1;
         }
         agentsRefArray = boc.getBOU().increaseCapacity(agentsRefArray, buffIncr);
      }
      int magentIndex = agentIndex;
      agentsRefArray[agentIndex] = agent;
      agentIndex++;
      agent.memoryAgentIndex = magentIndex;
   }

   /**
    * 
    * @param bom
    * @param refid
    */
   public void addNextReferenceID(ByteObjectManaged bom, int refid) {
      bom.set2(AGENT_OFFSET_08_REF_ID2, refid);
      addAgent(bom);
   }

   /**
    * Iterates over all {@link MemorySource}, counting all agents in the arrays
    * <br>
    * <br>
    * Method loads all {@link MemorySource}.
    * <br>
    * @return
    */
   public int countAgents() {
      loadAllAgents(null);
      return countAgentsLive();
   }

   private int countAgents(byte[] arr) {
      return countAgents(arr, 0);
   }

   /**
    * Counts the number of consecutive {@link ByteObjectManaged} stored in this byte array.
    * <br>
    * <br>
    * The array is supposed to be valid
    * @param arr
    * @return
    */
   private int countAgents(byte[] arr, int offset) {
      int arSize = arr.length;
      int count = 0;
      if (arr != null) {
         while (offset < arSize) {
            int len = IntUtils.readIntBE(arr, offset + AGENT_OFFSET_16_LEN4);
            if (len == 0) {
               throw new RuntimeException("Corrupted Memory Data");
            }
            offset += len;
            count++;
         }
      }
      return count;
   }

   public int countAgentsByte() {
      int size = 0;
      for (int i = 0; i < agentsRefArray.length; i++) {
         if (agentsRefArray[i] != null) {
            size += agentsRefArray[i].getLengthManaged();
         }
      }
      return size;
   }

   /**
    * Count the number of agents that have the classID.
    * Only loaded agents
    * @param classid
    * @return
    */
   public int countAgentsClass(int classid) {
      return countAgentsClass(AGENT_OFFSET_05_CLASS_ID2, classid);
   }

   public int countAgentsClass(int field, int value) {
      int total = 0;
      for (int i = 0; i < agentsRefArray.length; i++) {
         if (agentsRefArray[i] != null && agentsRefArray[i].get2(field) == value) {
            total++;
         }
      }
      return total;
   }

   /**
    * in the live sources, the number of agents with that agent id
    * @param agentid
    * @return
    */
   public int countAgentsLive() {
      int total = 0;
      for (int i = 0; i < bcse.length; i++) {
         if (bcse[i] != null) {
            for (int j = 0; j < bcse[i].length; j++) {
               if (bcse[i][j] != null) {
                  total += bcse[i][j].get3(MEMC_OFFSET_05_NUM_AGENTS3);
               }
            }
         }
      }
      return total;
   }

   /**
    * Create and add Agent using factory
    * @param intId
    */
   public void createAgent(int intId) {

      getFactory().createObject(null, intId, this);
   }

   public ByteObjectManaged createObject(ByteObjectManaged bom, int index, int intId) {
      int refid = bom.get2(index);
      ByteObjectManaged ob = getAgentFromRefOrCreate(refid, null, intId);
      return ob;
   }

   public void dataCorrupted(ByteObjectManaged bom) {
      bom.setFlag(AGENT_OFFSET_01_FLAG_1, AGENT_FLAG_CTRL_3_CORRUPTED, true);
   }

   private void expandExpulse(ByteObjectManaged block, int bytesIncr, int position) {
      int offset = block.getOffset();
      expulse(block);
      //position
      int newposition = position - offset;
      block.increaseArray(bytesIncr, newposition);
   }

   /**
    * Look up {@link ByteObjectManaged} and expands its memory array using the {@link ByteController}'s policy.
    * <br>
    * Defaults to increasing the byte array of the {@link ByteObjectManaged}
    * Expands by incr bytes the byte array used by the {@link ByteObjectManaged} at position.
    * <br>
    * <br>
    * Expansion of memory follows different directives
    * <li> {@link IBOByteControler#MEMC_EX_POLICY_0_DEFAULT}
    * <li> {@link IBOByteControler#MEMC_EX_POLICY_3_SINGLE}
    * <li> {@link IBOByteControler#MEMC_EX_POLICY_1_MULTIPLE}
    * <li> {@link IBOByteControler#MEMC_EX_POLICY_2_EXPULSE}
    * <br>
    * <br>
    * The goal is to reach
    * <br>
    * Case 1: Several agents are all packed into a single byte array. This is efficient.
    * When one agent needs to expand its memory.
    * 
    * When memory is plenty, each agent has its own byte array once its needs expanding.
    * When expansion is needed, all agents are given their own byte array and the old one is discared.
    * <br>
    * @param block agent must belong to this {@link ByteController}.
    * @param position offset loaded position
    * @param bytesIncr
    */
   void expandMemory(ByteObjectManaged block, int bytesIncr, int position) {
      block.setSaveFlag(false);
      int index = block.memoryAgentIndex;
      if (index == -1) {
         //agent must be controlled by this
         throw new IllegalStateException();
      }
      if (block.isAlone()) {
         //case of agent in its own array without a MEMC header. it occurs when agent has been expulsed once.
         block.increaseArray(bytesIncr, position);
      } else {
         int expansionPolicy2 = getExpansionPolicy();
         if (expansionPolicy2 == MEMC_EX_POLICY_0_DEFAULT) {
            expandExpulse(block, bytesIncr, position);
         } else if (expansionPolicy2 == MEMC_EX_POLICY_3_SINGLE) {
            expandSingle(block, bytesIncr, position);
         } else if (expansionPolicy2 == MEMC_EX_POLICY_2_EXPULSE) {
            expandExpulse(block, bytesIncr, position);
         }
      }
   }

   private void expandSingle(ByteObjectManaged block, int bytesIncr, int position) {
      int gid = block.memoryMemSrcIndex;
      int iid = block.memoryMemSrcID;
      ByteObjectManaged[] as = getSourceAgents(gid, iid);
      int offset = block.getOffset();
      byte[] source = block.getMemory();
      byte[] data = block.increaseArray(bytesIncr, position);
      for (int i = 0; i < as.length; i++) {
         if (as[i] != block) {
            byte[] dataI = as[i].getMemory();
            if (dataI == source) { //the agent really uses the same array and 
               //has not been expulsed for some reason.
               if (as[i].getOffset() > offset) {
                  as[i].incrementOffset(bytesIncr);
               }
               as[i].setMemory(data);
            }
         }
      }
   }

   private void expulse(ByteObjectManaged block) {
      int offset = block.getOffset();
      int len = block.getLength();
      byte[] source = block.getMemory();
      byte[] arrayExpusled = new byte[len];
      System.arraycopy(source, offset, arrayExpusled, 0, len);
      block.setMemory(arrayExpusled);
      block.setOffset(0);
      block.setFlagNoVersion(AGENT_OFFSET_03_FLAGZ_1, AGENT_FLAGZ_CTRL_2_EXPULSED, true);

   }

   /**
    * Send the agents outside the ByteController enveloppe and shrinks that enveloppe to just the header
    * <br>
    * All agents from a gid/iid are either expulsed or they are not.
    * @param gid
    * @param iid
    */
   public synchronized void expulse(int gid, int iid) {
      //
      initCheckGID(gid, dataSources[gid]);
      if (bcse[gid][iid] != null) {
         ByteObjectManaged env = bcse[gid][iid];
         if (!env.hasFlag(MEMC_OFFSET_09_FLAG1, MEMC_FLAG_4_HEADER_ALONE)) {
            for (int i = 0; i < agentsRefArray.length; i++) {
               if (agentsRefArray[i].memoryMemSrcIndex == gid && agentsRefArray[i].memoryMemSrcID == iid) {
                  expulse(agentsRefArray[i]);
               }
            }
            //make new header.
            ByteObjectManaged newEnv = env.cloneBOMHeader();
            newEnv.setFlag(MEMC_OFFSET_09_FLAG1, MEMC_FLAG_4_HEADER_ALONE, true);
            bcse[gid][iid] = newEnv;
         }
      }
   }

   /**
    * Find agents that match the given criterias
    * <br>
    * <br>
    * Shall call {@link ByteController#loadAllAgents()} before to make sure all objects are loaded
    * <br>
    * <br>
    * 
    * @param tech 0 values are ignored and considered a wildcard
    * @return
    */
   public ByteObjectManaged[] findAgents(ByteObjectManaged tech) {
      IntBuffer buf = new IntBuffer(boc.getUCtx());
      for (int i = 0; i < agentsRefArray.length; i++) {
         ByteObjectManaged bom = agentsRefArray[i];
         if (bom != null) {
            //
            if (isMatch(bom, tech)) {
               buf.addInt(i);
            }
         }
      }
      ByteObjectManaged[] ar = new ByteObjectManaged[buf.getSize()];
      for (int i = 0; i < ar.length; i++) {
         int index = buf.get(0);
         ar[i] = agentsRefArray[index];
      }
      return ar;

   }

   public ByteObjectManaged[] findAgents(int offset, int size, int value) {
      IntBuffer buf = new IntBuffer(boc.getUCtx());
      for (int i = 0; i < agentsRefArray.length; i++) {
         ByteObjectManaged bom = agentsRefArray[i];
         if (bom != null) {
            if (value == bom.getValue(offset, size)) {
               buf.addInt(i);
            }
         }
      }
      ByteObjectManaged[] ar = new ByteObjectManaged[buf.getSize()];
      for (int i = 0; i < ar.length; i++) {
         int index = buf.get(i);
         ar[i] = agentsRefArray[index];
      }
      return ar;
   }

   public ByteObjectManaged[] findAgentsClass(int classID) {
      return findAgents(AGENT_OFFSET_05_CLASS_ID2, 2, classID);
   }

   /**
    * Agent that have AGENT_OFFSET_05_GROUP_ID2
    * @param groupdID
    * @return
    */
   public ByteObjectManaged[] findAgentsGroup(int groupdID) {
      return findAgents(AGENT_OFFSET_06_GSOURCE_ID2, 2, groupdID);
   }

   /**
    * Method from {@link IMemory}
    */
   public void freeMemory() {
      // TODO Auto-generated method stub

   }

   /**
    * Unload all data
    */
   public synchronized void freeMemoryDataUnload() {

   }

   /**
    * Called when the MemorySource can free the byte array
    */
   public void freeMemorySource() {

   }

   public ByteObjectManaged getAgent(int redID) {
      ByteObjectManaged bom = getAgentFromRef(redID);
      if (bom != null) {
         if (isUnInstantiated(bom)) {
            bom = instancetiate(bom);
         }
      }
      return bom;
   }

   /**
    * Returns null if no agent of that classid is found
    * <br>
    * <br>
    * This method loads all the data in memory searching for the Agent.
    * @param classid
    * @return
    */
   public ByteObjectManaged getAgentFromClassID(int classid) {
      return getBOMLoad(AGENT_OFFSET_05_CLASS_ID2, 2, classid);
   }

   /**
    * Get the {@link ByteObjectManaged} at that positions  Search it in the list
    * @param gid
    * @param iid
    * @param tech check the class ID. if not found, create one
    * @param safetyIntID
    * @return null if could not be found at that position
    */
   public ByteObjectManaged getAgentFromGroupInstance(int gid, int iid, ByteObjectManaged tech, int safetyIntID) {
      ByteObjectManaged bom = null;
      if (bcse[gid][iid] == null) {
         MemorySource ms = dataSources[gid];
         byte[] d = ms.load(iid);
         loadAgents(d, gid, iid);
      }
      //try to find it
      ByteObjectManaged bc = bcse[gid][iid];
      if (bc != null) {
         for (int i = 0; i < agentsRefArray.length; i++) {
            if (agentsRefArray[i] != null) {
               if (agentsRefArray[i].memoryMemSrcIndex == gid && agentsRefArray[i].memoryMemSrcID == iid) {
                  if (agentsRefArray[i].getIDClass() == tech.getIDClass()) {
                     return agentsRefArray[i];
                  }
               }
            }
         }
      }
      if (bom == null) {
         bom = (ByteObjectManaged) getFactory().createObject(tech, safetyIntID, this);
      }
      if (bom != null) {
         if (isUnInstantiated(bom)) {
            bom = instancetiate(bom);
         }
      }
      return bom;
   }

   //   public Object getAgent(ByteObjectManaged bom, int interfaceID) {
   //      return getBOMLoad(AGENT_OFFSET_03_INTERFACE_ID2, 2, interfaceID);
   //   }

   /**
    * Look up the {@link ByteObjectManaged} with {@link IBOAgentManaged#AGENT_OFFSET_08_REF_ID2}.
    * <br>
    * <br>
    * @see ByteController#getBOMLive(int, int, int)
    * @param redID
    * @return
    */
   public ByteObjectManaged getAgentFromRef(int redID) {
      ByteObjectManaged bom = getBOMLoad(AGENT_OFFSET_08_REF_ID2, 2, redID);
      if (bom != null) {
         if (isUnInstantiated(bom)) {
            bom = instancetiate(bom);
         }
      }
      return bom;
   }

   /**
    * 
    * Entry point for creating a {@link ByteObjectManaged} from {@link MemorySource}.
    * <br>
    * <br>
    * This method is called by constuctors of data structures.
    * 
    * When creating an empty strucutre, call this method with the tech.
    * 
    * {@link ByteController#getAgentRoot()} is the starting point.
    * <br>
    * <br>
    * If the tech is not null, {@link IBOAgentManaged#AGENT_OFFSET_06_GSOURCE_ID2} gives a hint
    * on which memory source the object resides.
    * If it is not found, the {@link ByteController} will scan all {@link MemorySource} for the reference.
    * <br>
    * If it is yet not found, the {@link IJavaObjectFactory} creates a new instance and assign the reference refID.
    * <br>
    * The agent is added to the list
    * <br>
    * @param refID {@link IBOAgentManaged#AGENT_OFFSET_08_REF_ID2} to look for. When 0, method uses the factory
    * to create a new instance object based on the {@link ByteObjectManaged} tech parameters and safety Interface.
    * @param tech
    * @return
    */
   public ByteObjectManaged getAgentFromRefOrCreate(int refID, ByteObjectManaged tech, int safetyIntID) {
      ByteObjectManaged bom = null;
      if (refID != 0) {
         if (tech != null) {
            int gid = tech.get2(AGENT_OFFSET_06_GSOURCE_ID2);
            int iid = tech.get2(AGENT_OFFSET_07_INSTANCE_ID2);
            if (gid != 0) {
               loadDefaultDataSource(gid, iid);
            }
         }
         bom = getBOMLive(AGENT_OFFSET_08_REF_ID2, 2, refID);
      }
      if (bom == null) {
         //create object with default tech on a byteController?
         bom = (ByteObjectManaged) getFactory().createObject(tech, safetyIntID, this);
      }
      if (bom != null) {
         if (isUnInstantiated(bom)) {
            bom = instancetiate(bom);
         }
      }
      return bom;
   }

   /**
    * 
    * @param refID
    * @param safetyIntID interface ID in case reference is not found
    * @return never null
    */
   public ByteObjectManaged getAgentFromRefOrCreate(int refID, int safetyIntID) {
      ByteObjectManaged bom = getAgentFromRefOrCreate(refID, null, safetyIntID);
      if (bom == null) {
         throw new NullPointerException();
      }
      return bom;
   }

   /**
    * Return the offsets to the start of the header of all agents in the data
    * <br>
    * <br>
    * @param data
    * @return
    */
   public int[] getAgentOffsets(byte[] data) {
      int start = 0;
      int c = countAgents(data);
      int[] offsets = new int[c];
      if (IntUtils.readIntBE(data, 0) == MEMC_MAGIC_WORD) {
         start = MEMC_HEADER_SIZE;
      }
      int count = 0;
      while (start + AGENT_BASIC_SIZE < data.length) {
         int len = IntUtils.readIntBE(data, start + AGENT_OFFSET_16_LEN4);
         if (len == 0 || start + len + AGENT_BASIC_SIZE >= data.length) {
            throw new RuntimeException("Corrupted Data");
         }
         offsets[count] = start;
      }
      return offsets;
   }

   /**
    * 
    * Each {@link ByteController} usually has a root object. That object uses.
    * <br>
    * In a TableTrie {@link ByteController}, the root is the tabletrie. Other sub trie
    * <br>
    * However, in the context of a UserTrie, that tabletrie of the ByteController no more root.
    * <br>
    * So how do we solve this issue in the hierarchy of ByteController?
    * <br>
    * {@link ByteController#getAgentFromRefOrCreate(int, ByteObjectManaged, int)}
    * <br>
    * The root enveloppe is used to select the agent Root.
    * @return
    */
   public ByteObjectManaged getAgentRoot() {
      //ByteController root reference.
      if (rootAgent == null) {
         int refid = this.get2(MEMC_OFFSET_06_ROOT_REF2);
         ByteObjectManaged bom = getAgentFromRef(refid);
         if (bom == null) {
            //the reference could not be found. generate a data inconsistency warning
            //#debug
            boc.toDLog().pMemory("Root ID " + refid + " not found. Trying to load First", this, ByteController.class, "getAgentRoot", ITechLvl.LVL_09_WARNING, false);
            bom = agentsRefArray[0];
         }
         rootAgent = bom;
      }
      if (isUnInstantiated(rootAgent)) {
         rootAgent = instancetiate(rootAgent);
      }
      return rootAgent;
   }

   /**
    * 
    * @return
    */
   public ByteObjectManaged getAgentRootOut() {
      ByteObjectManaged bom = getAgentRoot();
      removeAgent(bom);
      return bom;
   }

   protected ByteObjectManaged[] getAgentsFromArray(byte[] array) {
      ByteObjectManaged[] agents = null;
      ByteObjectManaged bc = new ByteObjectManaged(boc, array);
      if (bc.getType() == IBOTypesBOC.TYPE_036_BYTE_CONTROLLER) {
         int num = bc.get3(MEMC_OFFSET_05_NUM_AGENTS3);
         agents = new ByteObjectManaged[num];
         int offset = MEMC_BASIC_SIZE;
         int acount = 0;
         for (int j = 0; j < num; j++) {
            ByteObjectManaged ag = new ByteObjectManaged(boc, array, offset);
            ag.setFlag(AGENT_OFFSET_03_FLAGZ_1, AGENT_FLAGZ_CTRL_3_FROM_SOURCE, true);
            agents[acount] = ag;
            offset += ag.getLength();
            acount++;
         }

      } else {
         //support only one agent by memory source
         if (bc.isMagicValid()) {
            int length = bc.getLength();
            bc = new ByteObjectManaged(boc, array, 0);
            bc.setFlag(AGENT_OFFSET_01_FLAG_1, AGENT_FLAGZ_CTRL_3_FROM_SOURCE, true);
            agents = new ByteObjectManaged[] { bc };
         } else {
            //invalid data
            throw new IllegalArgumentException("ByteController#addAgentsFromBytes Array malformed");
         }
      }
      return agents;
   }

   protected ByteObjectManaged[] getAgentsFromBC(ByteObjectManaged bc) {
      ByteObjectManaged[] agents = null;
      byte[] array = bc.getByteObjectData();
      if (bc.getType() == IBOTypesBOC.TYPE_036_BYTE_CONTROLLER) {
         int num = bc.get3(MEMC_OFFSET_05_NUM_AGENTS3);
         agents = new ByteObjectManaged[num];
         int offset = MEMC_BASIC_SIZE;
         int acount = 0;
         for (int j = 0; j < num; j++) {
            //create tech with ByteController, which will be used to initialize inside the factory
            //this constructor check magic word
            ByteObjectManaged fin = new ByteObjectManaged(boc, this, array, offset);
            fin.setFlag(AGENT_OFFSET_03_FLAGZ_1, AGENT_FLAGZ_CTRL_3_FROM_SOURCE, true);
            fin.memoryByteArrayIndex = j;
            agents[acount] = fin;
            offset += fin.getLength();
            acount++;
         }
      } else {
         //invalid data
         throw new IllegalArgumentException("ByteController#getAgentsFromBC Must be of Type " + 23);
      }
      return agents;
   }

   /**
    * Search for a {@link ByteObjectManaged} in the live Array with the given field value.
    * <br>
    * Returns the first or return null.
    * <br>
    * Usually when a Agent is looked for it must be found.
    * <br>
    * Otherwise it is an exception. there was some corrupted data in the memory source
    * <br>
    * <br>
    * When looking at the data sources
    * @param field
    * @param size
    * @param value
    * @return
    */
   public ByteObjectManaged getBOMLive(int field, int size, int value) {
      for (int i = 0; i < agentsRefArray.length; i++) {
         if (agentsRefArray[i] != null) {
            if (agentsRefArray[i].getValue(field, size) == value) {
               return agentsRefArray[i];
            }
         }
      }
      return null;
   }

   /**
    * Search the {@link ByteController} for a {@link ByteObjectManaged} with the given field value.
    * <br>
    * <li>Search live Agents.
    * <li>Search {@link ByteController} {@link MemorySource}
    * <li>Search root {@link ByteController}
    * <br>
    * Loads {@link MemorySource} Returns the first or return null.
    * <br>
    * <br>
    * Null if it could not be found.
    * @param field
    * @param size
    * @param value
    * @return
    */
   public ByteObjectManaged getBOMLoad(int field, int size, int value) {
      ByteObjectManaged bom = getBOMLive(field, size, value);
      if (bom == null) {
         //TODO recursively loads looking for the field
         loadDefaultDataSources();
         bom = getBOMLive(field, size, value);
      }
      if (bom != null) {
         bom.setFlag(AGENT_OFFSET_03_FLAGZ_1, AGENT_FLAGZ_CTRL_1_REFERENCED, true);
      }
      return bom;
   }

   /**
    * Returns the byte value of memory consumption by the byte[] array
    * <br>
    * <br>
    * @return
    */
   public int getByteConsumed() {
      int total = 0;
      for (int i = 0; i < bcse.length; i++) {
         if (bcse[i] != null) {
            for (int j = 0; j < bcse[i].length; j++) {
               if (bcse[i][j] != null)
                  total += bcse[i][j].getLength();
            }
         }
      }
      return total;
   }

   /**
    * Create a {@link ByteController} header for the {@link ByteObjectManaged}.
    * and write the agent to it
    * Copies all the agents
    * @param a0
    * @return
    */
   public ByteObjectManaged getByteController(ByteObjectManaged[] a0) {
      int totalSize = MEMC_BASIC_SIZE;
      int numAgents = a0.length;
      for (int j = 0; j < numAgents; j++) {
         totalSize += a0[j].getLength();
      }
      ByteObjectManaged ctrl = boc.getByteControllerFactory().getEnveloppe(totalSize, numAgents);
      for (int j = 0; j < numAgents; j++) {
         ByteObjectManaged agent = a0[j];
         agent.serializeToCheck(this);
         ctrl.copyAppendData(agent.getByteArrayCopy(), agent.getOffset(), agent.getLength());
      }
      return ctrl;
   }

   /**
    * Computes the total number of bytes consumed by the main byte arrays.
    * <br>
    * Agents {@link IBOAgentManaged#AGENT_FLAG_CTRL_4_UNPACKED} are requested to compute unpacked data cost
    * @return
    */
   public int getBytesConsumed() {
      int total = 0;
      total += getBytesConsumedLiveAgents();

      return total;
   }

   /**
    * Returns the bytes consumed by all the agents in the AgentArray.
    * 
    * @return
    */
   public int getBytesConsumedLiveAgents() {
      int total = 0;
      for (int i = 0; i < agentIndex; i++) {
         if (agentsRefArray[i] != null) {
            total += agentsRefArray[i].getLength();
         }
      }
      return total;
   }

   public int getExpansionPolicy() {
      return get1(MEMC_OFFSET_02_MODE1);
   }

   /**
    * Retusn {@link IJavaObjectFactory} set with {@link ByteController#setFactory(IJavaObjectFactory)}
    * or the default one
    * @return
    */
   public IJavaObjectFactory getFactory() {
      if (factory == null) {
         //returns default one
         return boc.getBOModuleManager().getDefaultFactory();
      }
      return factory;
   }

   /**
    * 
    * @return
    * @throws IllegalStateException when {@link ByteController} is final
    */
   public int getNextReferenceID() {
      return agentIndex + 1;
   }

   /**
    * Static ID
    * @param id
    * @return
    */
   public int getNextReferenceID(int id) {
      return id;
   }

   public int getNumLiveAgents() {
      int coun = 0;
      for (int i = 0; i < agentsRefArray.length; i++) {
         if (agentsRefArray[i] != null) {
            coun++;
         }
      }
      return coun;
   }

   public int getNumMemoryAreas() {
      int coun = 0;
      for (int i = 0; i < bcse.length; i++) {
         if (bcse[i] != null) {
            coun++;
         }
      }
      return coun;

   }

   /**
    * Builds a unique structure ID, from ByteCon 
    * <br>
    * <br>
    * @param intId
    * @return
    */
   public int getRefID(int intId) {
      int v = ((0 & 0xff) << 24);
      v += ((0 & 0xff) << 16);
      v += ((0 & 0xff) << 8);
      v += ((intId & 0xff) << 0);
      return v;
   }

   /**
    * Agents with the sid and iid
    * @param sid
    * @param iid
    * @return
    */
   public ByteObjectManaged[] getSourceAgents(int sid, int iid) {
      IntBuffer ib = new IntBuffer(boc.getUCtx(), 5);
      for (int i = 0; i < agentsRefArray.length; i++) {
         if (agentsRefArray[i] != null) {
            if (agentsRefArray[i].memoryMemSrcIndex == sid && agentsRefArray[i].memoryMemSrcID == iid) {
               ib.addInt(i);
            }
         }
      }
      ByteObjectManaged[] os = new ByteObjectManaged[ib.getSize()];
      for (int i = 0; i < os.length; i++) {
         int index = ib.get(i);
         os[i] = agentsRefArray[index];
      }
      return os;
   }

   public boolean hasMagicCtrl(byte[] data, int offset) {
      return IntUtils.readIntBE(data, offset + MEMC_OFFSET_00_MAGICWORD4) == MEMC_MAGIC_WORD;
   }

   private int[] initCheckGID(int gid, MemorySource ms) {
      int[] ids = new int[] { 0 };
      if (bcse[gid] == null) {

         if (ms.hasMSFlag(MS_FLAG_1_IDS)) {
            //initialize
            int[] mids = ms.getValidIDs();
            if (mids != null && mids.length != 0) {
               ids = mids;
               int max = IntUtils.getMax(ids);
               bcse[gid] = new ByteObjectManaged[max + 1];
            } else {
               bcse[gid] = new ByteObjectManaged[1];
            }
         } else {
            bcse[gid] = new ByteObjectManaged[1];
         }
      }
      return ids;
   }

   private void initStructs(int arrayBuffer) {
      int arStart = get1(MEMC_OFFSET_07_ARRAY_START1);
      agentsRefArray = new ByteObjectManaged[arStart];
      dataSources = new MemorySource[arrayBuffer];
      bcse = new ByteObjectManaged[arrayBuffer][];
   }

   /**
    * The first line will call
    * {@link ByteController#instantiateFrom(ByteObjectManaged, ByteObjectManaged)}
    * <br>
    * Then 
    * state.
    * <br>
    * <br>
    * @param tech
    * @return
    */
   private ByteObjectManaged instancetiate(ByteObjectManaged tech) {
      ByteObjectManaged fin = (ByteObjectManaged) getFactory().createObject(this, tech);
      //the init is separated so the object can be referenced inside the reverse?
      //at this stage, the method instantiateFrom has been called and
      // fin is docked to the ByteController
      //reset the flag so the serializeReverse can call methods
      if (fin == null) {

         //#debug
         toDLog().pNull("Factory -> " + getFactory().toString1Line() + " could not create object for " + tech.toString1Line(), this, ByteController.class, "instancetiate", LVL_05_FINE, false);

         throw new IllegalArgumentException("Could not create object for " + tech.toString());
      }
      fin.initMe();
      fin.setFlag(AGENT_OFFSET_03_FLAGZ_1, AGENT_FLAGZ_CTRL_7_SAVED, true);

      return fin;
   }

   /**
    * Called by {@link ByteObjectManaged} constructor.
    * <br>
    * @param fin
    * @param tech
    */
   void instantiateFrom(ByteObjectManaged fin, ByteObjectManaged tech) {
      fin.memoryMemSrcIndex = tech.memoryMemSrcIndex;
      fin.memoryMemSrcID = tech.memoryMemSrcID;
      fin.memoryByteArrayIndex = tech.memoryByteArrayIndex;
      fin.memoryAgentIndex = tech.memoryAgentIndex;
      fin.setFlag(AGENT_OFFSET_03_FLAGZ_1, AGENT_FLAGZ_CTRL_4_INSTANTIATED, true);
      agentsRefArray[tech.memoryAgentIndex] = fin;
      //update the reference states
      if (tech.isRoot() && tech == rootAgent) {
         rootAgent = fin;
      }
   }

   private boolean isInstantiated(ByteObjectManaged bom) {
      return bom.hasFlag(AGENT_OFFSET_03_FLAGZ_1, AGENT_FLAGZ_CTRL_4_INSTANTIATED);
   }

   private boolean isMatch(ByteObjectManaged bom, ByteObjectManaged techPattern) {
      int techClass = techPattern.get2(AGENT_OFFSET_05_CLASS_ID2);
      if (techClass != 0) {
         if (techClass != bom.get2(AGENT_OFFSET_05_CLASS_ID2)) {
            return false;
         }
      }
      techClass = techPattern.get2(AGENT_OFFSET_07_INSTANCE_ID2);
      if (techClass != 0) {
         if (techClass != bom.get2(AGENT_OFFSET_07_INSTANCE_ID2)) {
            return false;
         }
      }
      techClass = techPattern.get2(AGENT_OFFSET_06_GSOURCE_ID2);
      if (techClass != 0) {
         if (techClass != bom.get2(AGENT_OFFSET_06_GSOURCE_ID2)) {
            return false;

         }
      }
      //check if all interface
      int[] ints = techPattern.getIDInterfaces();
      if (ints.length != 0) {
         int[] bomInts = bom.getIDInterfaces();
         if (!boc.getUCtx().getIU().equals(ints, bomInts)) {
            return false;
         }
      }
      return true;
   }

   /**
    * {@link IBOByteControler#MEMC_FLAG_3_LEAN_MEMORY}
    * Decided by Master Memory Controller based on Memory Conditions
    * @return
    */
   public boolean isRunningLeanMemory() {
      return hasFlag(MEMC_OFFSET_09_FLAG1, MEMC_FLAG_3_LEAN_MEMORY);
   }

   /**
    * Collects the {@link ByteObjectManaged} that need to be saved on the MemorySource.
    * <br>
    * Null if no agents need to be saved at the {@link MemorySource}.
    * @param gid
    * @return an array of {@link ByteObjectManaged} whose {@link IBOAgentManaged#AGENT_OFFSET_06_GSOURCE_ID2} is "gid"
    */
   public ByteObjectManaged[] isSaveNeed(int gid) {
      ByteObjectManaged[] a0 = findAgentsGroup(gid);
      boolean saveNeeded = false;
      for (int j = 0; j < a0.length; j++) {
         if (a0[j].isModified()) {
            saveNeeded = true;
            break;
         }
      }
      if (saveNeeded) {
         return a0;
      } else {
         return null;
      }
   }

   /**
    * True if {@link ByteObjectManaged} needs to be serialized
    * <br>
    * That is: was modified, is new, is Build (data not in the byte array)
    * @param agent
    * @return
    */
   public boolean isSerializationNeeded(ByteObjectManaged agent) {
      if (!agent.isModified()) {
         return false;
      }
      return true;
   }

   /**
    * 
    * @return
    */
   public boolean isSourceCovered() {
      return false;
   }

   private boolean isUnInstantiated(ByteObjectManaged bom) {
      return !bom.hasFlag(AGENT_OFFSET_03_FLAGZ_1, AGENT_FLAGZ_CTRL_4_INSTANTIATED);
   }

   /**
    * 
    * @param agent
    */
   public void linkAllAgents(ByteObjectManaged agent) {
      addAgent(agent);
      ByteObject[] objs = agent.getSubs();
      if (objs != null) {
         for (int i = 0; i < objs.length; i++) {
            if (objs[i] != null && objs[i] instanceof ByteObjectManaged) {
               linkAllAgents((ByteObjectManaged) objs[i]);
            }
         }
      }
   }

   /**
    * Assume byte data has not been parsed before.
    * @param rootData
    * @param srcIndex
    * @param iid
    */
   private void loadAgents(byte[] rootData, int srcIndex, int iid) {
      if (rootData != null && rootData.length != 0) {
         ByteObjectManaged bc = new ByteObjectManaged(boc, rootData);
         //data must be wrapped around a ByteController enveloppe.
         if (bc.getType() != IBOTypesBOC.TYPE_036_BYTE_CONTROLLER) {

            //#debug
            toDLog().pMemoryWarn("Problem loading MemorySource Data " + srcIndex, this, ByteController.class, "loadAgents");

            //when its not, create that enveloppe and tries to read the agents
            int numAgents = 1;
            bc = boc.getByteControllerFactory().getEnveloppe(MEMC_BASIC_SIZE, numAgents);
            bc.copyAppendData(rootData, 0, rootData.length);

         }
         bc.memoryMemSrcIndex = srcIndex;
         bc.memoryMemSrcID = iid;
         bc.setFlag(AGENT_OFFSET_03_FLAGZ_1, AGENT_FLAGZ_CTRL_3_FROM_SOURCE, true);
         bcse[srcIndex][iid] = bc;

         ByteObjectManaged[] agents = getAgentsFromBC(bc);

         //case of a memory reload
         if (hasFlag(MEMC_OFFSET_10_FLAGZ1, MEMC_FLAGZ_1_MEMORYCLEARED)) {
            //reload must be matched with existing agents how? with the 
            for (int i = 0; i < agents.length; i++) {
               int pos = agents[i].memoryByteArrayIndex;
               for (int j = 0; j < agentsRefArray.length; j++) {
                  ByteObjectManaged bom = agentsRefArray[i];
                  if (bom.memoryMemSrcIndex == srcIndex) {
                     if (bom.memoryMemSrcID == iid) {
                        if (bom.memoryByteArrayIndex == pos) {
                           //found live agent
                           bom.data = agents[i].data;
                           bom.index = agents[i].index;
                           bom.set1(AGENT_OFFSET_03_FLAGZ_1, 0);
                           agents[i] = null;
                           break;
                        }
                     }
                  }
               }
            }
         }
         //once all loaded.
         for (int j = 0; j < agents.length; j++) {
            if (agents[j] != null) {
               agents[j].memoryMemSrcIndex = srcIndex;
               agents[j].memoryMemSrcID = iid;
               agents[j].set1(AGENT_OFFSET_03_FLAGZ_1, 0); //reset the flag
               addAgent(agents[j]);
               //the agent has been added without being instantiated. which must be done when going out
            }
         }
         //once all loaded. reverse them by reading data
         for (int j = 0; j < agents.length; j++) {
            ByteObjectManaged tech = agents[j];
            if (tech != null) {
               //check if tech has not been
               int ref = tech.memoryAgentIndex;
               if (agentsRefArray[ref] == tech) {
                  //factory instantiates the class only.
                  ByteObjectManaged fin = instancetiate(tech);
                  agents[j] = fin;
               }
            }
         }
      }
   }

   /**
    * Force the reading into memory of all {@link MemorySource} associated with this {@link ByteController}
    * <br>
    * <br>
    * Typically this will be executed in a worker thread, with the GUI listening with {@link IMProgessable}.
    * <br>
    * It will be a wrapper running in the worker thread that will fetch localized Strings and feed another {@link IMProgessable} in the GUI thread . The {@link IMProgessable#setLabel(String)} will provide
    * info about the specific agents being read.
    * <br>
    * <br>
    * <b>Corruption Report</b>
    * <br>
    * This method can be called at any time, it will load unloaded sources
    * <br>
    * TODO if we care about user feedback. we must be implemented as a task!
    * <br>
    * So updates can be done based on a timeframe
    * @param progress {@link IMProgessable}. Can be null.
    *
    */
   public void loadAllAgents(IBProgessable progress) {
      if (progress != null) {
         for (int i = 0; i < dataSources.length; i++) {
            progress.setValue(i);
            dataSources[i].setProgress(progress.getChild());
         }
      }
      loadDefaultDataSources();
   }

   private void loadData(int gid, int iid, MemorySource ms) {
      if (gid < 0 || gid >= bcse.length) {
         throw new IllegalArgumentException("bad gid=" + gid + " with iid=" + iid);
      }
      if (iid < 0 || iid >= bcse[gid].length) {
         throw new IllegalArgumentException("bad iid=" + iid + " with gid=" + gid);
      }
      if (bcse[gid][iid] == null) {
         byte[] d = ms.load(iid);
         loadAgents(d, gid, iid);
      }
   }

   /**
    * If not already read, Reads the ith {@link MemorySource} and set it inside. 
    * When null
    * @param i
    * @return true if it could create it.
    */
   private void loadDefaultDataSource(int gid) {
      MemorySource ms = dataSources[gid];
      if (ms == null) {
         return;
      }
      int[] ids = initCheckGID(gid, ms);
      //if only 1 ID, case single
      int num = ids.length;
      for (int j = 0; j < num; j++) {
         int iid = ids[j];
         loadData(gid, iid, ms);
      }

   }

   /**
    * Force the reading into memory of all {@link MemorySource} associated with this {@link ByteController}
    * <br>
    * <br>
    * An {@link IMProgessable} Read all sources.
    * <br>
    * 
    * Corruption report
    * <br>
    * <br>
    * This method can be called at any time, it will load unloaded sources
    * <br>
    * This can be a lengthy process.
    */
   public void loadAllAgents() {
      //we don't have progress defined at this level
      //            if (progress != null) {
      //               //how to internationize strings at this level? Put translatable strings in a module API.
      //               progress.set(isp.getIString("loadingAgents", "Loading Agents"), null, null, dataSources.length, IMProgessable.LVL_0_USER);
      //            }
      //            for (int i = 0; i < dataSources.length; i++) {
      //               if (progress != null) {
      //                  IString sourceStr = isp.getIString("sourceSpace", "Source ", "" + (i + 1));
      //                  progress.setLabel(sourceStr);
      //                  progress.setValue(i);
      //                  dataSources[i].setProgress(progress.getChild());
      //               }
      //            }
      loadDefaultDataSources();
   }

   /**
    * 
    * @param gid
    * @param iid
    * @throws IllegalArgumentException when iid is not a valid
    */
   private void loadDefaultDataSource(int gid, int iid) {
      MemorySource ms = dataSources[gid];
      if (ms == null) {
         return;
      }
      int[] ids = initCheckGID(gid, ms);
      if (bcse[gid].length <= iid) {
         throw new IllegalArgumentException();
      }
      loadData(gid, iid, ms);
   }

   /**
    * Loads all the data from all the {@link MemorySource} available.
    * <br>
    * the {@link ByteController#loadDataSource(int, int)} must be called individually.
    * <br>
    * Calling this method resets
    */
   private void loadDefaultDataSources() {
      for (int i = 0; i < dataSources.length; i++) {
         loadDefaultDataSource(i);
      }
      isDataSourceLoad = true;
   }

   /**
    * Save Agents to their memory Source.
    * <br>
    * Clear the byte arrays.
    * 
    * <br>
    * Should it be synchronized?
    */
   public synchronized void memoryClear() {
      saveAgents();
      for (int i = 0; i < agentsRefArray.length; i++) {
         if (agentsRefArray[i] != null) {
            //only clear it if it is linked from a Memory Source
            if (agentsRefArray[i].hasFlag(AGENT_OFFSET_03_FLAGZ_1, AGENT_FLAGZ_CTRL_3_FROM_SOURCE)) {
               ByteObjectManaged bomClearing = agentsRefArray[i];
               bomClearing.setFlag(AGENT_OFFSET_01_FLAG_1, AGENT_FLAG_CTRL_7_DATA_UNLOADED, true);
               int headerLen = bomClearing.get2(AGENT_OFFSET_13_LEN_HEADER2);
               //we need to keep the header otherwise we can't read the UNLOADED flag 
               ByteObjectManaged bom = boc.getByteObjectManagedFactory().createByteObject(headerLen);
               bom.burnHeader(bomClearing);
               bomClearing.memoryClearSub();
               bomClearing.data = bom.data;
               bomClearing.index = 0;
            }
         }
      }
      for (int i = 0; i < bcse.length; i++) {
         for (int j = 0; j < bcse[i].length; j++) {
            bcse[i][j] = null;
         }
      }
      setFlag(MEMC_OFFSET_10_FLAGZ1, MEMC_FLAGZ_1_MEMORYCLEARED, true);
   }

   /**
    * Clears all the agent at that source point
    * <br>
    * @param memoryMemSrcIndex
    * @param memoryMemSrcID
    */
   public void memoryClear(int memoryMemSrcIndex, int memoryMemSrcID) {
      // TODO Auto-generated method stub

   }

   /**
    * Takes it away from the other byte Con
    * @param bom
    */
   public void mergeBOM(ByteObjectManaged bom) {
      ByteController olbc = bom.getByteController();
      if (olbc != this) {
         olbc.removeAgent(bom);
         addAgent(bom);
      }
   }

   /**
    * Merge {@link ByteController}
    * <li> Shift {@link IBOAgentManaged#AGENT_OFFSET_08_REF_ID2}
    * <li> Shift {@link IBOAgentManaged#AGENT_OFFSET_06_GSOURCE_ID2}
    * <li> Update {@link ByteObjectManaged} references
    * <li> 
    * 
    * @param parent
    */
   public void mergeController(ByteController parent) {

   }

   /**
    * Read the datasource of the Agent, if it has been nulled
    * <br>
    * The {@link MemorySource} is identified by {@link IBOAgentManaged#AGENT_OFFSET_06_GSOURCE_ID2} and {@link IBOAgentManaged#AGENT_OFFSET_07_INSTANCE_ID2}
    * <br>
    * <br>
    * The agent data is not saved. it is serialized in reverse.
    * @param agent
    */
   public void reload(ByteObjectManaged agent) {
      if (agent.byteCon != this) {
         throw new IllegalStateException();
      }
      if (!agent.hasFlag(AGENT_OFFSET_01_FLAG_1, AGENT_FLAG_CTRL_7_DATA_UNLOADED)) {
         throw new IllegalStateException("Agent must have been unloaded");
      }
      int gid = agent.memoryMemSrcIndex;
      int iid = agent.memoryMemSrcID;
      if (bcse.length > gid) { //when possible
         if (bcse[gid].length > iid) { //when possible
            if (bcse[gid][iid] == null) {
               loadData(gid, iid, dataSources[gid]);
            }
         }
      }
      //ask the agent to reload itself
      agent.initMe();
   }

   /**
    * Removes the instance
    * @param bom
    */
   public void removeAgent(ByteObjectManaged bom) {
      if (bom.byteCon == this) {
         bom.byteCon = null;
         agentsRefArray[bom.memoryAgentIndex] = null;
         bom.memoryMemSrcID = -1;
         bom.memoryMemSrcIndex = -1;
         bom.memoryAgentIndex = -1;
         bom.memoryByteArrayIndex = -1;
         bom.set2(AGENT_OFFSET_08_REF_ID2, 0);//reset reference
         bom.set1(AGENT_OFFSET_03_FLAGZ_1, 0);
         bom.setFlag(AGENT_OFFSET_01_FLAG_1, AGENT_FLAG_CTRL_5_ROOT, false);
      }
   }

   /**
    * Removes the data held by the object.
    * <br>
    * usually used by Build objects.
    * <br>
    * The {@link ByteController} usually expulses all agents from array.
    * @param bom
    */
   public void removeData(ByteObjectManaged bom) {
      if (bom.getOffset() != 0) {
         expulse(bom.memoryMemSrcIndex, bom.memoryMemSrcID);
      }
      bom.expandResetArrayData(0);

   }

   public void saveAgent(ByteObjectManaged bom) {
      saveAgent(new int[2], bom);
   }

   /**
    * Saves a single agent.
    * <br>
    * In effect, saves all agents that belong to the same {@link MemorySource} adress.
    * 
    * @param bom
    */
   public void saveAgent(int[] stats, ByteObjectManaged bom) {
      if (isSerializationNeeded(bom)) {
         int idSource = bom.memoryMemSrcIndex;
         int idInstance = bom.memoryMemSrcID;
         //
         ByteObjectManaged[] a0 = getSourceAgents(idSource, idInstance);
         ByteObjectManaged ctrl = getByteController(a0);
         byte[] datac = ctrl.getByteObjectData();
         stats[1] += datac.length;
         //#mdebug
         if (idSource >= dataSources.length) {
            toDLog().pMemory("gid is not index to a datasource :" + idSource, this, ByteController.class, "saveAgent");
         }
         //#enddebug
         dataSources[idSource].save(datac, 0, datac.length, idInstance);
         for (int j = 0; j < a0.length; j++) {
            ByteObjectManaged agent = a0[j];
            agent.setSaveFlag(true);//flag agent as saved
            stats[0]++;
         }
      }
   }

   /**
    * This method will be called before exiting the main application or by the user.
    * <br>
    * Can be run in its own thread. Thread safe.
    * All agents on the controller's list will have the modified data saved
    * The modified flag is automatically managed by the {@link ByteController} when agent's memory array is expanded
    * 
    * Save agents with the {@link IObjectManaged#}.
    * <br>
    * When saving an {@link ByteObjectManaged} to a {@link MemorySource}, code writes all {@link ByteObjectManaged}s from that belong to that
    * {@link MemorySource}.
    * <br>
    * <br>
    * A {@link MemorySource} will not be saved when none of its {@link ByteObjectManaged} were modified.
    * <br>
    * <br>
    * When a source id and instance id agent has been modified, all agents from that source
    * are written.
    * <br>
    * When a source point is loaded, all the agents are loaded in memory, therefore all agents must be written
    * back even if only one agent from that source point was modified.
    * 
    * @return array with 2 values. first value is the number of agents saved and then the number of bytes written.
    */
   public int[] saveAgents() {
      //how do we make sure cached agents are what they are in other threads?
      //lock the agents, each thread increase a shared counter?
      //some statistics
      int[] stats = new int[] { 0, 0 };
      IntBuffer waitingOn = new IntBuffer(getUCtx());
      if (dataSources.length != 0) {
         for (int i = 0; i < agentsRefArray.length; i++) {
            ByteObjectManaged bom = agentsRefArray[i];
            if (bom != null) {
               //agent is currently being written to or read by another thread
               if (bom.isSyncroFlag(SYNCRO_FLAG_2_LOCKED)) {
                  waitingOn.addInt(i);
                  saveThread(bom, stats);
                  //go to next agent
                  continue;
               } else {
                  bom.dataLock();
               }
               saveAgent(stats, bom);
               bom.dataUnLock();
            }
         }
         //now wait for all saveThreads to complete
         if (waitingOn.getSize() != 0) {

         }
      }
      return stats;
   }

   protected void saveMultiples(int[] stats, int i, ByteObjectManaged[] a0) {
      //get the root agent header
      int max = 0;
      ByteObjectManaged[] arZero = new ByteObjectManaged[0];
      for (int j = 0; j < a0.length; j++) {
         ByteObjectManaged agent = a0[j];
         if (agent != null) {
            int instID = agent.get2(AGENT_OFFSET_07_INSTANCE_ID2);
            if (instID <= 1) {
               //save it along with the header
               arZero = boc.getBOU().addToArray(arZero, agent);
            } else {
               if (agent.isModified()) {
                  //only one agent???
                  dataSources[i].save(agent.getByteObjectData(), agent.getOffset(), agent.getLength(), instID);
                  agent.setSaveFlag(true);
                  stats[1] += agent.getLength();
                  if (instID > max) {
                     max = instID;
                  }
               }
            }
         }
      }
      saveToMemorySource(stats, i, arZero);
   }

   private void saveThread(final ByteObjectManaged ag, final int[] stats) {
      new Thread(new Runnable() {

         public void run() {

            MutexSignal se = new MutexSignal(boc.getUCtx());
            //asks the the agent to notify this waiting thread
            synchronized (se) {
               //wait for the thread to finish
               ag.setSemaphoreLock(se);
               try {
                  se.acquire();
               } catch (InterruptedException e) {
                  e.printStackTrace();
               }
               //when finished waiting we lock the object
               ag.dataLock();
               //now we want to go back to the calling thread to notify it that o
               //our agent is ready to be saved
            }
            saveAgent(stats, ag);
         }
      }).start();
   }

   /**
    * 
    * @param stats int array with stats
    * @param i
    * @param a0
    */
   protected void saveToMemorySource(int[] stats, int i, ByteObjectManaged[] a0) {
      //build a bc header
      ByteObjectManaged ctrl = getByteController(a0);
      byte[] datac = ctrl.getByteObjectData();
      stats[1] += datac.length;
      dataSources[i].save(datac, 0, datac.length);
      for (int j = 0; j < a0.length; j++) {
         ByteObjectManaged agent = a0[j];
         agent.setSaveFlag(true);//flag agent as saved
      }
   }

   /**
    * Packs all the data into a single byte array inside a {@link IBOByteControler} enveloppe.
    * <br>
    * 
    * @return
    */
   public byte[] serializeAll() {
      loadAllAgents(null);
      return serializeLiveAgents();
   }

   /**
    * Return the raw byte array for all the loaded agents in the Array of Agents.
    * [block 1]
    * [block 2]
    * [block 3]
    * <br>
    * <br>
    * Agents not loaded are not serialized. Removes the data of {@link MemorySource}
    * <br>
    * 
    * The returned array can be used by the controller
    * the size is memoryConsumed + ENVELOPPE_SIZE
    * <br>
    * <br>
    * Order of {@link ByteObjectManaged} appearance is not deterministic
    * @return
    */
   public byte[] serializeLiveAgents() {
      int size = getBytesConsumedLiveAgents();
      ByteObjectManaged bom = boc.getByteControllerFactory().getEnveloppe(size, agentIndex);
      //#debug
      toDLog().pMemory("Serialize all live agents for a total of " + bom.getLength() + " bytes", this, ByteController.class, "serializeLiveAgents");
      for (int i = 0; i < agentIndex; i++) {
         ByteObjectManaged agent = agentsRefArray[i];
         if (agent != null) {
            if (agent.isRoot()) {
               bom.set2(MEMC_OFFSET_06_ROOT_REF2, agent.get2(AGENT_OFFSET_08_REF_ID2));
            }
            bom.copyAppendData(agent.getByteObjectData(), agent.getByteObjectOffset(), agent.getLength());
            //#debug
            toDLog().pMemory("Agent #" + (i + 1), agent, ByteController.class, "serializeLiveAgents");
         }
      }
      return bom.getByteObjectData();
   }

   /**
    * Look up any reference to {@link ByteObjectManaged}, replace it by a new {@link ByteObjectManaged}
    * using the given data.
    * <br>
    * Called exclusively by {@link ByteObjectManaged#serializeTo(ByteController)}.
    * <br>
    * 
    * Updates the Agents reference ID
    * <br>
    *  Called in the process of serialization.
    * <br>
    * <br>
    * Adds a new instance.
    * <br>
    * TODO make a loop break mechanism
    * @param bom
    * @param data
    */
   public ByteObjectManaged serializeToUpdateAgentData(byte[] data) {
      ByteObjectManaged bom = new ByteObjectManaged(boc, this, data);
      this.addAgent(bom);
      return bom;
   }

   /**
    * Depending on the {@link ByteController} policy, the {@link ByteObjectManaged} wants a new fresh array of the given size
    * @param bom
    * @param data
    * @return
    */
   public void setAgentData(ByteObjectManaged bom, int size) {
      bom.data = new byte[size];
      bom.index = 0;
   }

   public void setDefaultSourceID(int i) {
      // TODO Auto-generated method stub

   }

   public void setExpansionPolicy(int policy) {
      set1(MEMC_OFFSET_03_POLICY_MODE1, policy);
   }

   public void setFlag(int flag, boolean v) {
      flags = BitUtils.setFlag(flags, flag, v);
   }

   public void setMemorySources(MemorySource[] mss) {
      // TODO Auto-generated method stub

   }

   /**
    * 
    * {@link ByteObject}s that are in fact {@link ByteObjectManaged}
    * @param from
    * @param to
    * @param offsetTo
    */
   public void setRefFromTo(ByteObjectManaged from, ByteObjectManaged to, int offsetTo) {
      int ref = from.get2(AGENT_OFFSET_08_REF_ID2);
      to.set2(offsetTo, ref);
   }

   /**
    * When a {@link ByteController} is merged with another, REF Ids are shifted,
    * except the static IDs.
    * <br>
    * @param shiftSize
    */
   public void shiftReferenceIDs(int shiftSize) {

   }

   /**
    * 
    * @param incr increment the length of the {@link ByteObject}
    * @param position
    * @param agentIndex
    * @param array
    */
   private void simpleIncrease(int incr, int position, int agentIndex, byte[] array) {
      byte[] newArray = boc.getUCtx().getMem().increaseCapacity(array, incr, position);
      agentsRefArray[agentIndex].setMemory(newArray);
      agentsRefArray[agentIndex].incrementLengthData(incr);
   }

   //#mdebug
   public String toString() {
      return Dctx.toString(this);
   }

   public void toString(Dctx dc) {
      dc.root(this, "ByteController");
      toStringHeaderHeader(dc);
      super.toString(dc.newLevel());
      dc.nlLvl(factory, "Factory");
      toStringMemoryStats(dc.newLevel());
      toStringLiveAgents(dc.newLevel());
      toStringBCHeaders(dc.newLevel());
      toStringDataSources(dc.newLevel());
   }

   public String toString1Line() {
      return Dctx.toString1Line(this);
   }

   public void toString1Line(Dctx dc) {
      dc.root(this, "ByteController");
   }

   private void toStringBCHeaders(Dctx sb) {
      sb.append("=>ByteController Internal Headers #" + bcse.length);
      for (int k = 0; k < bcse.length; k++) {
         sb.nl();
         sb.append(k + " : ");
         if (bcse[k] == null) {
            sb.append("null");
         } else {
            sb.append("#" + bcse[k].length);
            for (int i = 0; i < bcse[k].length; i++) {
               if (bcse[k][i] == null) {
                  sb.append("null");
               } else {
                  //toString only the ByteController header
                  if (boc.toStringHasToStringFlag(IToStringFlagsBO.TOSTRING_FLAG_4_BYTEOBJECT_1LINE)) {
                     sb.nlLvl1Line(bcse[k][i]);
                  } else {
                     sb.nlLvl(bcse[k][i]);
                  }
               }
            }
         }
      }
   }

   public void toStringBusiness(Dctx dc) {
      toStringHeaderHeader(dc);
      dc.nl();
      dc.append("Internal Headers #" + bcse.length);
      for (int k = 0; k < bcse.length; k++) {
         dc.nl();
         dc.append(k + " : ");
         if (bcse[k] == null) {
            dc.append("null");
         } else {
            dc.append("#" + bcse[k].length);
            for (int i = 0; i < bcse[k].length; i++) {
               if (bcse[k][i] == null) {
                  dc.append("null");
               } else {
                  dc.nlLvl1Line(bcse[k][i]);
                  //
                  for (int j = 0; j < agentsRefArray.length; j++) {
                     if (agentsRefArray[j].memoryMemSrcIndex == k) {
                        dc.nlLvl1Line(agentsRefArray[j]);
                     }
                  }
               }
            }
         }
      }
      dc.nl();
      toStringDataSources(dc.newLevel());
      dc.nl();
      toStringMemoryStats(dc.newLevel());
   }

   private void toStringDataSources(Dctx sb) {
      int i = 0;
      sb.append("=>Listing Datasources #" + dataSources.length);
      for (int k = 0; k < dataSources.length; k++) {
         sb.nl();
         sb.append(k + " : ");
         int[] ids = dataSources[i].getValidIDs();
         boc.getUCtx().getIU().toStringIntArray1Line(sb, "IDs", ids, ",");
         //we have a branch
         if (boc.toStringHasToStringFlag(IToStringFlagsBO.TOSTRING_FLAG_4_BYTEOBJECT_1LINE)) {
            dataSources[k].toString1Line(sb);
         } else {
            sb.nlLvl(dataSources[k]);
         }
      }
   }

   private void toStringHeaderHeader(Dctx sb) {
      String mode = boc.getByteControllerFactory().toStringPolicy(getExpansionPolicy());
      sb.append("#ByteController Mode=" + mode + " #LiveAgents=" + agentIndex + " #MemoryAreas=" + bcse.length);
      sb.nl();
      sb.appendVar("Length", get4(MEMC_OFFSET_01_LEN4));
      sb.appendVarWithTab("Mode", get1(MEMC_OFFSET_02_MODE1));
      sb.appendVarWithTab("PolicyMode", get1(MEMC_OFFSET_03_POLICY_MODE1));
      sb.appendVarWithTab("CtrlGroup", get2(MEMC_OFFSET_04_CTRL_GROUP_ID2));
      sb.nl();
      sb.appendVar("NumAgents", get3(MEMC_OFFSET_05_NUM_AGENTS3));
      sb.appendVarWithTab("RootRef", get2(MEMC_OFFSET_06_ROOT_REF2));
      sb.appendVarWithTab("ArrayStart", get1(MEMC_OFFSET_07_ARRAY_START1));
      sb.appendVarWithTab("ArrayIncrement", get1(MEMC_OFFSET_08_ARRAY_INCREMENT1));
   }

   public void toStringLiveAgents(Dctx sb) {
      sb.append("#LiveAgents=" + agentIndex);
      int numChars = String.valueOf(agentIndex).length();
      for (int i = 0; i < agentIndex; i++) {
         sb.nl();
         sb.append("#");
         sb.append(boc.getUCtx().getStrU().prettyInt0Padd(i, numChars));
         sb.append(" ");
         if (agentsRefArray[i] == null) {
            sb.append("null");
         } else {
            //
            if (boc.toStringHasToStringFlag(IToStringFlagsBO.TOSTRING_FLAG_4_BYTEOBJECT_1LINE)) {
               agentsRefArray[i].toString1Line(sb);
            } else {
               agentsRefArray[i].toString(sb.newLevel());
            }
         }
      }
   }

   private void toStringMemoryStats(Dctx sb) {
      sb.append("=>Listing Memory Statistics ");
      sb.nl();
      sb.append("Total Bytes Consumed by Memory Controlled Areas = ");
      sb.append(getUCtx().getStrU().prettyStringMem(this.getByteConsumed()));
      sb.nl();
      sb.append("Total Bytes Consumed by Live Agents = ");
      sb.append(getUCtx().getStrU().prettyStringMem(this.getBytesConsumedLiveAgents()));
      sb.nl();
   }

   //#enddebug
}
