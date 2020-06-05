/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.byteobjects.src4.sources;

import pasa.cbentley.byteobjects.src4.ctx.BOCtx;
import pasa.cbentley.core.src4.utils.BitUtils;

/**
 * Reads memory data from a file in the jar.
 * <br>
 * <br>
 * @author Charles Bentley
 *
 */
public class JarSource extends MemorySource {

   private String filename;

   public JarSource(BOCtx boc, String file) {
      super(boc);
      filename = file;
   }

   public int[] getValidIDs() {
      return null;
   }

   /**
    * 
    * @return
    */
   public byte[] load() {
      return null;
   }


   public void save(byte[] memory, int offset, int len) {
      //cannot save a file in a jar. read only.
   }

   public void save(byte[] memory, int offset, int len, int id) {
      //cannot save a file in a jar. read only.
   }

   /**
    * id loaded
    * @param id
    * @return
    */
   public byte[] load(int id) {
      return null;
   }

   /**
    * load memory controller from file and write directly in the array
    * at offset
    */
   public void load(int id, byte[] array, int offset) {
   
   }

   public byte[] preload() {
      // TODO Auto-generated method stub
      return null;
   }

   public byte[] load(int id, byte[] data, int offset, int len) {
      // TODO Auto-generated method stub
      return null;
   }

   public byte[] loadHeader(int size) {
      // TODO Auto-generated method stub
      return null;
   }

   public String getSrcID() {
      return filename;
   }

}
