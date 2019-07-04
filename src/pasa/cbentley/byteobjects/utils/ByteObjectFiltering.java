package pasa.cbentley.byteobjects.utils;

import pasa.cbentley.byteobjects.core.ByteObject;
import pasa.cbentley.byteobjects.functions.Acceptor;

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
