/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.byteobjects.src4.core;

import pasa.cbentley.core.src4.interfaces.IFactoryBase;

public class FactoryByteObject implements IFactoryBase {

   public Object[] createArray(int size) {
      return new ByteObject[size];
   }

}
