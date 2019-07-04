package pasa.cbentley.byteobjects.core;

import pasa.cbentley.byteobjects.ctx.BOCtx;
import pasa.cbentley.byteobjects.ctx.IBOTypesBOC;
import pasa.cbentley.byteobjects.tech.ITechObjectManaged;
import pasa.cbentley.core.src4.logging.Dctx;

/**
 * 
 * @author Charles Bentley
 *
 */
public class ByteObjectManagedFactory extends BOAbstractFactory implements ITechObjectManaged {

   public ByteObjectManagedFactory(BOCtx boc) {
      super(boc);
   }

   public ByteObjectManaged create(int header, int data) {
      int arraySize = header + data;
      ByteObjectManaged bo = new ByteObjectManaged(boc, new byte[arraySize], 0);
      bo.set2(AGENT_OFFSET_17_MAGIC2, AGENT_MAGIC_WORD);
      bo.set2(A_OBJECT_OFFSET_3_LENGTH2, 0xFFFF);
      bo.set4(AGENT_OFFSET_14_LEN_DATA_4, data);
      bo.set2(AGENT_OFFSET_13_LEN_HEADER2, header);
      bo.set4(AGENT_OFFSET_16_LEN4, arraySize);

      return bo;
   }

   public ByteObjectManaged createByteObject(int headerSize) {
      return getTechDefault(headerSize, 0, 0);
   }

   private ByteObjectManaged createFromHeader(byte[] header, int dataSize) {
      ByteObjectManaged bo = new ByteObjectManaged(boc, header);
      bo.expandResetArrayData(dataSize);
      return bo;
   }

   /**
    * 
    * @param mod
    * @param extraHeaderSize in addition to {@link ITechObjectManaged#AGENT_BASIC_SIZE}
    * @param datalength
    * @return
    */
   public ByteObjectManaged getDefault(BOCtx mod, int extraHeaderSize, int datalength) {
      int hl = AGENT_BASIC_SIZE + extraHeaderSize;
      int arraySize = hl + datalength;
      ByteObjectManaged bo = new ByteObjectManaged(mod, new byte[arraySize], 0);
      bo.set2(AGENT_OFFSET_17_MAGIC2, AGENT_MAGIC_WORD);
      bo.set2(A_OBJECT_OFFSET_3_LENGTH2, 0xFFFF);
      bo.set4(AGENT_OFFSET_14_LEN_DATA_4, datalength);
      bo.set2(AGENT_OFFSET_13_LEN_HEADER2, hl);
      bo.set4(AGENT_OFFSET_16_LEN4, arraySize);

      return bo;
   }

   /**
    * Starts with {@link ITechObjectManaged#AGENT_HEADER_SIZE}, 1 for class id and 0 for interface
    * <br>
    * <br>
    * 
    * @param mod
    * @return
    */
   public ByteObjectManaged getTechDefault() {
      return getTechDefault(AGENT_BASIC_SIZE, 1, 0);
   }

   public ByteObjectManaged getTechDefault(ByteController bc, int headerSize, int classid, int intid) {
      byte[] data = new byte[headerSize];
      ByteObjectManaged bo = new ByteObjectManaged(bc, data);
      bo.set1(A_OBJECT_OFFSET_1_TYPE1, IBOTypesBOC.TYPE_035_OBJECT_MANAGED);
      bo.setValueNoVersion(A_OBJECT_OFFSET_3_LENGTH2, 0xFFFF, 2);
      bo.set2(AGENT_OFFSET_05_CLASS_ID2, classid);
      bo.set2(AGENT_OFFSET_04_INTERFACE_ID2, intid);
      //this tech is always the same
      bo.set2(AGENT_OFFSET_13_LEN_HEADER2, headerSize);
      bo.set4(AGENT_OFFSET_16_LEN4, headerSize);
      bo.set2(AGENT_OFFSET_17_MAGIC2, AGENT_MAGIC_WORD);
      bc.addAgent(bo);
      return bo;
   }

   public ByteObjectManaged getTechDefault(int headerSize, int classid) {
      return getTechDefault(headerSize, classid, 0);
   }

   /**
    * Gets a ByteObject tech with given header size, class and interface
    * @param mod
    * @param headerSize
    * @param classid
    * @param intid single interfaceID
    * @return
    */
   public ByteObjectManaged getTechDefault(int headerSize, int classid, int intid) {
      byte[] data = new byte[headerSize];
      ByteObjectManaged bo = new ByteObjectManaged(boc, data);
      bo.set1(A_OBJECT_OFFSET_1_TYPE1, IBOTypesBOC.TYPE_035_OBJECT_MANAGED);
      bo.setValueNoVersion(A_OBJECT_OFFSET_3_LENGTH2, 0xFFFF, 2);
      bo.set2(AGENT_OFFSET_05_CLASS_ID2, classid);
      bo.set2(AGENT_OFFSET_04_INTERFACE_ID2, intid);
      //this tech is always the same
      bo.set2(AGENT_OFFSET_13_LEN_HEADER2, headerSize);
      bo.set4(AGENT_OFFSET_16_LEN4, headerSize);
      bo.set2(AGENT_OFFSET_17_MAGIC2, AGENT_MAGIC_WORD);
      //set immutable now
      return bo;
   }

   public ByteObjectManaged getTechDefault(int headerSize, int classid, int[] intids) {
      byte[] data = new byte[headerSize];
      ByteObjectManaged bo = new ByteObjectManaged(boc, data);
      bo.set1(A_OBJECT_OFFSET_1_TYPE1, IBOTypesBOC.TYPE_035_OBJECT_MANAGED);
      bo.setValueNoVersion(A_OBJECT_OFFSET_3_LENGTH2, 0xFFFF, 2);
      bo.set2(AGENT_OFFSET_05_CLASS_ID2, classid);
      bo.setDynBOParamValues(AGENT_OFFSET_04_INTERFACE_ID2, intids);
      //this tech is always the same
      bo.set2(AGENT_OFFSET_13_LEN_HEADER2, headerSize);
      bo.set4(AGENT_OFFSET_16_LEN4, headerSize);
      bo.set2(AGENT_OFFSET_17_MAGIC2, AGENT_MAGIC_WORD);
      //set immutable now
      return bo;
   }

   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, "ByteObjectManagedFactory");
      toStringPrivate(dc);
      super.toString(dc.sup());
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, "ByteObjectManagedFactory");
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }


   public void toStringHeader(Dctx dc, ByteObjectManaged tech) {
      toStringHeaderAlone(dc, tech);
      dc.nl();
      tech.toStringBackUp(dc.nLevel());
   }

   public void toStringHeaderAlone(Dctx sb, ByteObjectManaged tech) {
      sb.append("#ByteObjectManaged");
      if (tech.hasFlag(AGENT_OFFSET_01_FLAG_1, AGENT_FLAG_CTRL_5_ROOT)) {
         sb.append(" [Root]");
      }
      if (tech.data == null) {
         sb.append("null data");
      } else {
         int magicword = tech.get2(AGENT_OFFSET_17_MAGIC2);
         if (magicword != AGENT_MAGIC_WORD) {
            sb.append("Maformed Magic Words " + magicword + " != " + AGENT_MAGIC_WORD);
         } else {
            int len = tech.get4(AGENT_OFFSET_16_LEN4);
            int aid = tech.get2(AGENT_OFFSET_05_CLASS_ID2);
            int iid = tech.get2(AGENT_OFFSET_07_INSTANCE_ID2);
            int gid = tech.get2(AGENT_OFFSET_06_GSOURCE_ID2);
            int sid = tech.get2(AGENT_OFFSET_08_REF_ID2);
            int intid = tech.get2(AGENT_OFFSET_04_INTERFACE_ID2);
            int buf = tech.get2(AGENT_OFFSET_15_LEN_BUFFER_3);
            int dal = tech.get2(AGENT_OFFSET_14_LEN_DATA_4);
            int hel = tech.get2(AGENT_OFFSET_13_LEN_HEADER2);
            //ByteController is absolute
            if (tech.byteCon == null) {
               sb.append("[Null ByteController]");
            } else {
               sb.append("[" + tech.byteCon.toStringOneLine() + "]");
            }
            sb.append(" [Length of ");
            sb.append(" Agent=" + len);
            sb.append(" Header=" + hel);
            sb.append(" Data=" + dal);
            sb.append(" Buffer=" + buf);
            sb.append("]");
            sb.nl();
            sb.append("memoryAgentIndex=" + tech.memoryAgentIndex + "\t memoryByteArrayIndex=" + tech.memoryByteArrayIndex + "\t memoryMemSrcIndex=" + tech.memoryMemSrcIndex + "\t memoryMemSrcID=" + tech.memoryMemSrcID);
            sb.nl();
            sb.append("refID=" + sid + "\t ClassID=" + aid + "\t IntID=" + intid + "\t GroupID=" + gid + "\t InstanceID=" + iid);
            sb.nl();
            sb.append("Instantiated=" + tech.hasFlag(AGENT_OFFSET_03_FLAGZ_1, AGENT_FLAGZ_CTRL_4_INSTANTIATED));
            sb.append(" Referenced=" + tech.hasFlag(AGENT_OFFSET_03_FLAGZ_1, AGENT_FLAGZ_CTRL_1_REFERENCED));
            sb.append(" Expulsed=" + tech.hasFlag(AGENT_OFFSET_03_FLAGZ_1, AGENT_FLAGZ_CTRL_2_EXPULSED));
            sb.append(" FromSource=" + tech.hasFlag(AGENT_OFFSET_03_FLAGZ_1, AGENT_FLAGZ_CTRL_3_FROM_SOURCE));
            sb.nl();
            sb.append("Lock=" + tech.hasFlag(AGENT_OFFSET_03_FLAGZ_1, AGENT_FLAGZ_CTRL_5_LOCKED));
            sb.append(" WaitingLock=" + tech.hasFlag(AGENT_OFFSET_03_FLAGZ_1, AGENT_FLAGZ_CTRL_6_WAITING));
            sb.append(" Saved=" + tech.hasFlag(AGENT_OFFSET_03_FLAGZ_1, AGENT_FLAGZ_CTRL_7_SAVED));

            sb.nl();
            sb.append("Root=" + tech.hasFlag(AGENT_OFFSET_01_FLAG_1, AGENT_FLAG_CTRL_5_ROOT));
            sb.append(" Hashed=" + tech.hasFlag(AGENT_OFFSET_01_FLAG_1, AGENT_FLAG_CTRL_1_HASHED));
            sb.append(" Exclusive=" + tech.hasFlag(AGENT_OFFSET_01_FLAG_1, AGENT_FLAG_CTRL_2_EXCLUSIVE));
            sb.append(" Corrupted=" + tech.hasFlag(AGENT_OFFSET_01_FLAG_1, AGENT_FLAG_CTRL_3_CORRUPTED));
            sb.append(" Unpacked=" + tech.hasFlag(AGENT_OFFSET_01_FLAG_1, AGENT_FLAG_CTRL_4_UNPACKED));
            sb.append(" ReadOnly=" + tech.hasFlag(AGENT_OFFSET_01_FLAG_1, AGENT_FLAG_CTRL_6_READ_ONLY));
            sb.append(" DataUnloaded=" + tech.hasFlag(AGENT_OFFSET_01_FLAG_1, AGENT_FLAG_CTRL_7_DATA_UNLOADED));
            sb.append(" DataOnDemand=" + tech.hasFlag(AGENT_OFFSET_01_FLAG_1, AGENT_FLAG_CTRL_8_DATA_ON_DEMAND));
         }
      }
   }

   private void toStringPrivate(Dctx dc) {

   }
   //#enddebug
}
