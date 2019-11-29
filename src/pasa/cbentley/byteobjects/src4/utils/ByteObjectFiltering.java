package pasa.cbentley.byteobjects.src4.utils;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.functions.Acceptor;

/**
 * Filters Accepts/Rejects {@link ByteObject}.
 * <br>
 * <br>
 * @author Charles Bentley
 *
 */
public class ByteObjectFiltering {

   private int      pointer;

   private int      size;

   private Acceptor acceptor;

   /**
    * Asks if that byte array matches the ByteObject, given the condition
    * @param pointer pointer to field
    * @param ar
    * @param fc
    * @return
    */
   public boolean matches(ByteObject bo) {
      int val = bo.getValue(pointer, size);
      return acceptor.accept(val);
   }
}
