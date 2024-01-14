package pasa.cbentley.byteobjects.src4.objects.function;

import pasa.cbentley.byteobjects.src4.core.interfaces.IByteObject;

public interface IBOAcceptor extends IByteObject {

   /**
    * 1byte flag
    * 4 bytes base
    */
   int ACC_BASIC_SIZE            = A_OBJECT_BASIC_SIZE + 8;
   /**
    * When set, the acceptor returns true
    * <br>
    * When not set, the acceptor rejects.
    * <br>
    * 
    */
   int ACC_FLAG_1_ACCEPT         = 1;
   int ACC_FLAG_2_COUNT_REJECTS  = 2;
   int ACC_FLAG_3_ARRAY          = 4;
   int ACC_FLAG_5_CHANNEL        = 16;
   /**
    * Sets when acceptor definition defines its operand
    */
   int ACC_FLAG_8_OPERAND        = 128;
   int ACC_OFFSET_01_FLAG        = A_OBJECT_BASIC_SIZE;
   /**
    * <li> {@link ITechAcceptor#OP_COMP_0_EQUAL}
    * <li> {@link ITechAcceptor#OP_COMP_1_SMALLER}
    * <li> {@link ITechAcceptor#OP_COMP_2_BIGGER}
    * <li> {@link ITechAcceptor#OP_DATE_DAY}
    * <li> {@link ITechAcceptor#OP_DATE_LAST}
    * <li> {@link ITechAcceptor#OP_DATE_MONTH}
    * <li> {@link ITechAcceptor#OP_DATE_WEEK}
    * <li> {@link ITechAcceptor#OP_INT_0_EQUAL}
    * <li> {@link ITechAcceptor#OP_INT_1_HIGHER}
    * <li> {@link ITechAcceptor#OP_INT_2_LOWER}
    * <li> {@link ITechAcceptor#OP_INT_ARRAY_CONTAINS}
    * <li> {@link ITechAcceptor#OP_LOGICAL_0_AND}
    * <li> {@link ITechAcceptor#OP_STR_CONTAINS}
    * <li> {@link ITechAcceptor#OP_STR_CONTAINS}
    * 
    */
   int ACC_OFFSET_02_OPERATOR1   = A_OBJECT_BASIC_SIZE + 1;
   /**
    * <li> {@link ITechAcceptor#ACC_TYPE_0_INT}
    * <li> {@link ITechAcceptor#ACC_TYPE_1_ARRAY}
    * <li> {@link ITechAcceptor#ACC_TYPE_2_BYTEOBJECT}
    * <li> {@link ITechAcceptor#ACC_TYPE_3_STRING}
    * <li> {@link ITechAcceptor#ACC_TYPE_4_INT_DATE}
    * <li> {@link ITechAcceptor#ACC_TYPE_7_EXPRESSION}
    * 
    */
   int ACC_OFFSET_03_TYPE1       = A_OBJECT_BASIC_SIZE + 2;
   int ACC_OFFSET_04_1           = A_OBJECT_BASIC_SIZE + 3;
   /**
    * When operand fits in 4 bytes.
    * <br>
    * When operand is a String or integer array, pointer 2 bytes of data start, number of char 2 bytes.
    */
   int ACC_OFFSET_05_OPERAND4    = A_OBJECT_BASIC_SIZE + 4;

}
