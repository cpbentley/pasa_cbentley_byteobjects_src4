package pasa.cbentley.byteobjects.tech;

public interface ITechLitteral extends ITechByteObject {

   /**
    * 4 bytes for header
    * 2 bytes for length
    * 1 byte for value byte size
    */
   public static final int LITTERAL_ARRAY_BASIC_SIZE = 7;

   /**
    * Offset at which to start writing litterals of the array
    */
   public static final int LITTERAL_OFFSET_ARRAY     = ITechByteObject.A_OBJECT_BASIC_SIZE;

   /**
    * <li> {@link ITechByteObject#A_OBJECT_OFFSET_1_TYPE1}
    * <li> {@link ITechByteObject#A_OBJECT_OFFSET_2_FLAG}
    * <li> {@link ITechByteObject#A_OBJECT_OFFSET_3_LENGTH2}
    * 
    */
   public static final int LITTERAL_HEADER_SIZE      = ITechByteObject.A_OBJECT_BASIC_SIZE;

   /**
    * 4 byte for litteral
    */
   public static final int LITTERAL_INT_SIZE         = ITechByteObject.A_OBJECT_BASIC_SIZE + 4;

   /**
    * Offset where to read the start of litteral data
    */
   public static final int LITTERAL_OFFSET           = ITechByteObject.A_OBJECT_BASIC_SIZE;

}
