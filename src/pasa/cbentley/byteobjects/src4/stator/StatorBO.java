/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.byteobjects.src4.stator;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.ctx.BOCtx;
import pasa.cbentley.byteobjects.src4.ctx.IBOTypesBOC;
import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.io.BADataIS;
import pasa.cbentley.core.src4.io.BADataOS;
import pasa.cbentley.core.src4.stator.Stator;
import pasa.cbentley.core.src4.structs.IntBuffer;
import pasa.cbentley.core.src4.structs.IntToObjects;

/**
 * 
 * @author Charles Bentley
 *
 */
public class StatorBO extends Stator {

  public StatorBO[]      allstates;


   private IntBuffer       integers;

   /**
    * 
    */
   public IntToObjects     itos;

   private int             subIndex              = 0;

   /**
    * First is Drawable, then depending on the class Hierarchy
    */
   public Object[]         subStates             = new Object[5];

   /**
    * Type of state
    */
   private int             type;

   protected final BOCtx           boc;


   protected final UCtx uc;
   
   private int index;

   public StatorBO(BOCtx boc, int type) {
      super(boc.getUCtx());
      this.boc=boc;
      this.uc=boc.getUCtx();
      this.type = type;
      itos = new IntToObjects(uc);
      integers = new IntBuffer(uc);
   }

   public int getIndex() {
      return index;
   }
   
   public StatorBO getState(int type) {
      if(this.type == type) {
         return this;
      } else {
         for (int i = 0; i < allstates.length; i++) {
            if(allstates[i].getType() == type) {
               return allstates[i];
            }
         }
         //create new state
         StatorBO newState = new StatorBO(boc, type);
         add(newState);
         return newState;
      }
   }


   /**
    * Check if object is not already there
    * @param bo
    */
   public void addBOUnique(ByteObject bo) {
      if(!itos.hasObject(bo)) {
         itos.add(bo);
      } else {
         
      }
   }
   
   public void addBO(ByteObject bo) {
      itos.add(bo);
   }
   /**
    * 
    * @param vsd
    */
   public void add(StatorBO vsd) {
      subStates[subIndex] = vsd;
      subIndex++;
      if (subIndex >= subStates.length) {
         subStates = getUCtx().getMem().increaseCapacity(subStates, 5);
      }
   }

   public UCtx getUCtx() {
      return boc.getUCtx();
   }

   /**
    * Adds a null marker for ordered view state stacking.
    */
   public void addNull() {
      add(null);
   }


   public int getType() {
      return type;
   }

   /**
    * 
    * @param i
    * @return
    */
   public StatorBO getMyState(int i) {
      if (allstates == null || i >= allstates.length) {
         return null;
      }
      return (StatorBO) allstates[i];
   }

   /**
    * 
    * @return
    */
   public ByteObject getState() {
      int num = itos.nextempty;
      int size = 0;
      ByteObject bo = new ByteObject(boc, IBOTypesBOC.TYPE_037_CLASS_STATE, size);
      return bo;
   }

   /**
    * Serialize to the form of a {@link ByteObject}.
    * @return
    */
   public byte[] serialize() {
      return null;
   }

   public ByteObject serializeBO() {
      return null;
   }

   private BADataOS dos;

   private BADataIS dis;

   /**
    * Serialize the ViewState. We cannot use Java Object serialization? No
    * because it is not portable between Java2ME
    * @param ito
    * @param dos
    * @return
    */
   public byte[] serialize(IntToObjects ito, BADataOS dos) {
      return null;
   }

   /**
    * Add Object as bytes
    * @param data
    */
   public void addBytes(byte[] data) {
      // TODO Auto-generated method stub

   }

   public ByteObject readNextByteObject() {
      // TODO Auto-generated method stub
      return null;
   }

   public byte[] readNextObject() {
      // TODO Auto-generated method stub
      return null;
   }

   /**
    * Tells {@link StatorBO}, class is going to add x objects
    * @param size
    */
   public void addObjects(int size) {
      // TODO Auto-generated method stub

   }

   /**
    * Call after {@link StatorBO#addObjects(int)}
    * Finalize Array of Objects
    */
   public void closeArrayObjects() {
      // TODO Auto-generated method stub

   }

   /**
    * Read object and flag it for deletion.
    * <br>
    * @return null if no data for key
    */
   public byte[] readObjectDel(int key) {
      // TODO Auto-generated method stub
      return null;
   }

   public void setVersion(int ver) {
      // TODO Auto-generated method stub

   }

   public int readVersion() {
      // TODO Auto-generated method stub
      return 0;
   }

   public void wrongVersion(int ver) {
      throw new IllegalArgumentException("Wrong Version" + ver);
   }

   public int readInt() {
      // TODO Auto-generated method stub
      return 0;
   }

   public void writeByteObject(ByteObject bo) {
      bo.serializeTo(itos, dos);
   }

   public void writeInt(int behaviors) {
      // TODO Auto-generated method stub

   }


}
