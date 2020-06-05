package pasa.cbentley.byteobjects.src4.factory;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.core.src4.interfaces.IFactoryBase;

public class FactoryByteObject implements IFactoryBase {

   public Object[] createArray(int size) {
      return new ByteObject[size];
   }

}
