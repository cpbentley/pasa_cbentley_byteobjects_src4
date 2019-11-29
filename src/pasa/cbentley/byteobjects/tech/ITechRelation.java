package pasa.cbentley.byteobjects.tech;

public interface ITechRelation extends ITechByteObject {

   public static final int RELATION_BASIC_SIZE      = A_OBJECT_BASIC_SIZE + 2;

   public static final int RELATION_OFFSET_01_FLAG  = RELATION_BASIC_SIZE;

   public static final int RELATION_OFFSET_02_TYPE1 = RELATION_BASIC_SIZE + 1;

}
