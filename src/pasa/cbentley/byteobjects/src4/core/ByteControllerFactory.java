/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.byteobjects.src4.core;

import pasa.cbentley.byteobjects.src4.ctx.BOCtx;
import pasa.cbentley.byteobjects.src4.ctx.IBOTypesBOC;
import pasa.cbentley.byteobjects.src4.interfaces.IJavaObjectFactory;
import pasa.cbentley.byteobjects.src4.sources.ByteArraySource;
import pasa.cbentley.byteobjects.src4.sources.MemorySource;
import pasa.cbentley.byteobjects.src4.tech.ITechByteControler;
import pasa.cbentley.byteobjects.src4.tech.ITechObjectManaged;
import pasa.cbentley.core.src4.helpers.BytesIterator;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.utils.ShortUtils;

/**
 * 
 * @author Charles Bentley
 *
 */
public class ByteControllerFactory extends BOAbstractFactory implements ITechByteControler {

   public ByteControllerFactory(BOCtx boc) {
      super(boc);
   }

   public void checkMagicWordAndThrow(byte[] data, int offset) {
      if (ShortUtils.readShortBEUnsigned(data, offset + AGENT_OFFSET_17_MAGIC2) != AGENT_MAGIC_WORD) {
         throw new RuntimeException("Corrupted ByteManageData Data");
      }
   }

   public ByteObjectManaged createRootObject(IJavaObjectFactory fac, byte[] data) {
      ByteController bc = new ByteController(boc, fac, data);
      return bc.getAgentRoot();
   }

   MemorySource[] getArray(byte[] data, int offset) {
      return new MemorySource[] { new ByteArraySource(boc, data, offset) };
   }

   /**
    * Returns an enveloppe of a {@link ByteController}
    * @param mod
    * @param buffSize
    * @param numAgents
    * @return
    */
   public ByteObjectManaged getEnveloppe(int buffSize, int numAgents) {
      byte[] data = new byte[MEMC_BASIC_SIZE + buffSize];
      ByteObjectManaged bo = new ByteObjectManaged(boc, data);
      bo.set1(A_OBJECT_OFFSET_1_TYPE1, IBOTypesBOC.TYPE_036_BYTE_CONTROLLER);
      bo.set2(AGENT_OFFSET_17_MAGIC2, ITechObjectManaged.AGENT_MAGIC_WORD);
      bo.set2(AGENT_OFFSET_13_LEN_HEADER2, MEMC_HEADER_SIZE);
      bo.set3(AGENT_OFFSET_15_LEN_BUFFER_3, buffSize);
      bo.set3(MEMC_OFFSET_05_NUM_AGENTS3, numAgents);
      bo.set4(MEMC_OFFSET_01_LEN4, data.length);
      return bo;
   }

   /**
    * Returns parameter when that is has been processed by the {@link BytesIterator} in method 
    * {@link ByteController#getHeader(ByteController, BytesIterator, Object[], int)}.
    * 
    * <br>
    * <br>
    * 
    * @param currentHeader
    * @param bc
    * @return
    */
   public ByteObject getHeader(ByteObject currentHeader, BytesIterator bc) {
      if (currentHeader.getByteObjectData() != bc.getArray()) {
         //the Bytes Counter
         currentHeader = boc.getByteObjectFactory().createByteObjectFromWrap(bc);
      }
      return currentHeader;
   }

   /**
    * Tech default of a {@link ITechByteControler}
    * @return
    */
   public ByteObjectManaged getTechDefault() {
      ByteObjectManaged bo = boc.getByteObjectManagedFactory().createByteObject(ITechByteControler.MEMC_BASIC_SIZE);
      bo.set1(A_OBJECT_OFFSET_1_TYPE1, IBOTypesBOC.TYPE_036_BYTE_CONTROLLER);
      bo.set2(AGENT_OFFSET_13_LEN_HEADER2, MEMC_HEADER_SIZE);
      bo.set1(MEMC_OFFSET_07_ARRAY_START1, 1);
      bo.set1(MEMC_OFFSET_08_ARRAY_INCREMENT1, 1);
      return bo;
   }

   ByteObjectManaged getTechFromSource(MemorySource[] sources) {
      ByteObjectManaged bom = null;
      if (sources != null && sources.length > 0) {
         byte[] src = sources[0].load();
         int offset = sources[0].getOffset();
         bom = new ByteObjectManaged(boc, src, offset);
         if (bom.getType() == IBOTypesBOC.TYPE_036_BYTE_CONTROLLER) {
            return bom.cloneBOMHeader();
         }
      }
      if (bom == null) {
         bom = getTechDefault();
      }
      return bom;
   }

   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, "ByteControllerFactory");
      toStringPrivate(dc);
      super.toString(dc.sup());
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, "ByteControllerFactory");
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   public void toStringB1Line(Dctx sb, ByteObject bo) {
      sb.root(bo, "ByteController");
      sb.append(" NumAgents=" + bo.get3(MEMC_OFFSET_05_NUM_AGENTS3));
      sb.append(" RootRef=" + bo.get2(MEMC_OFFSET_06_ROOT_REF2));
      sb.append(" Expulsed=" + bo.hasFlag(MEMC_OFFSET_09_FLAG1, MEMC_FLAG_4_HEADER_ALONE));

   }

   public void toStringByteController(Dctx sb, ByteObject bo) {
      sb.root(bo, "ByteController");
      sb.append(" NumAgents=" + bo.get3(MEMC_OFFSET_05_NUM_AGENTS3));
      sb.append(" RootRef=" + bo.get2(MEMC_OFFSET_06_ROOT_REF2));
      sb.append(" Mode=" + bo.get2(MEMC_OFFSET_02_MODE1));
      sb.nl();
      sb.append(" Expulsed=" + bo.hasFlag(MEMC_OFFSET_09_FLAG1, MEMC_FLAG_4_HEADER_ALONE));
      sb.append(" SetMemory=" + bo.hasFlag(MEMC_OFFSET_09_FLAG1, MEMC_FLAG_1_SET_MEMORY));
      sb.append(" LeanMemory=" + bo.hasFlag(MEMC_OFFSET_09_FLAG1, MEMC_FLAG_3_LEAN_MEMORY));

   }

   public String toStringPolicy(int val) {
      switch (val) {
         case MEMC_EX_POLICY_3_SINGLE:
            return "Single";
         case MEMC_EX_POLICY_1_MULTIPLE:
            return "Multiple";
         case MEMC_EX_POLICY_2_EXPULSE:
            return "Expulse";
         case MEMC_EX_POLICY_0_DEFAULT:
            return "None";
         default:
            return "Unknown" + val;
      }
   }

   private void toStringPrivate(Dctx dc) {

   }

   //#enddebug
}
