package pasa.cbentley.byteobjects.tech;

import pasa.cbentley.byteobjects.ctx.IBOTypesBOC;

public interface ITechByteObjectArray extends ITechByteObject {

   public final static int BOA_BASE_TYPE               = IBOTypesBOC.TYPE_034_ARRAY_BIG;

   /**
    * 36 bytes + 4 bytes base Object
    */
   public final static int BOA_BASIC_SIZE              = A_OBJECT_BASIC_SIZE + 36;

   public final static int BOA_FLAG_1_VARIABLE_SIZE    = 1 << 0;

   /**
    * When set, all byteobject share the header of length.
    * <br>
    * In that case, when constructing 
    */
   public final static int BOA_FLAG_2_SHARED_HEADER    = 1 << 1;

   public final static int BOA_FLAG_3_                 = 1 << 2;

   public final static int BOA_FLAG_4_                 = 1 << 3;

   public final static int BOA_FLAG_5_                 = 1 << 4;

   public final static int BOA_FLAG_6_                 = 1 << 5;

   public final static int BOA_FLAG_7_                 = 1 << 6;

   public final static int BOA_FLAG_8_                 = 1 << 7;

   /**
    * Total byte size of this array
    */
   public final static int BOA_OFFSET_01_SIZE4         = A_OBJECT_BASIC_SIZE;

   public final static int BOA_OFFSET_02_FLAG1         = A_OBJECT_BASIC_SIZE + 4;

   /**
    * Contains the size of this header
    */
   public final static int BOA_OFFSET_03_HEADER_LEN2   = A_OBJECT_BASIC_SIZE + 4;

   /**
    * End of Pointer mapping, start of 
    */
   public final static int BOA_OFFSET_04_OFFSET_DATA4  = A_OBJECT_BASIC_SIZE + 4;

   /**
    * Number of elements in this part
    */
   public final static int BOA_OFFSET_05_NUM_ELEMENTS4 = A_OBJECT_BASIC_SIZE + 4;

   /**
    * When fixed size
    */
   public final static int BOA_OFFSET_06_SIZE_ELEMENT2 = A_OBJECT_BASIC_SIZE + 4;

}
