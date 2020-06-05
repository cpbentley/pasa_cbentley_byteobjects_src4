/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.byteobjects.src4.tech;

import pasa.cbentley.byteobjects.src4.core.ByteObject;

/**
 * Set a true/false Condition on a ByteObject pointer data.
 * <br>
 * <br>
 * A set of condition each mapped to a pointer is used to filter ByteObjects.
 * 
 * <li> bit
 * <li> byte
 * <li> int
 * <li> string
 * <br>
 * <br>
 * 
 * <b>Date matching</b>:
 * <br>
 * <br>
 * <b>Color Matching</b>
 * Breaks integers into 4 channels.
 * <br>
 * <br>
 * {@link ByteObject} have a field with 32 bits dates. We want to accept/reject based on a date operator.
 * <br>
 * <br>
 * Acceptor is composite of several Acceptor in a logical expression.
 * <br>
 * @author Charles Bentley
 *
 */
public interface ITechAcceptor extends ITechByteObject {

   /**
    * 1byte flag
    * 4 bytes base
    */
   public static final int ACC_BASIC_SIZE            = A_OBJECT_BASIC_SIZE + 8;

   /**
    * When set, the acceptor returns true
    * <br>
    * When not set, the acceptor rejects.
    * <br>
    * 
    */
   public static final int ACC_FLAG_1_ACCEPT         = 1;

   public static final int ACC_FLAG_2_COUNT_REJECTS  = 2;

   public static final int ACC_FLAG_3_ARRAY          = 4;

   public static final int ACC_FLAG_5_CHANNEL        = 16;

   /**
    * Sets when acceptor definition defines its operand
    */
   public static final int ACC_FLAG_8_OPERAND        = 128;

   public static final int ACC_OFFSET_01_FLAG        = A_OBJECT_BASIC_SIZE;

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
   public static final int ACC_OFFSET_02_OPERATOR1   = A_OBJECT_BASIC_SIZE + 1;

   /**
    * <li> {@link ITechAcceptor#ACC_TYPE_0_INT}
    * <li> {@link ITechAcceptor#ACC_TYPE_1_ARRAY}
    * <li> {@link ITechAcceptor#ACC_TYPE_2_BYTEOBJECT}
    * <li> {@link ITechAcceptor#ACC_TYPE_3_STRING}
    * <li> {@link ITechAcceptor#ACC_TYPE_4_INT_DATE}
    * <li> {@link ITechAcceptor#ACC_TYPE_7_EXPRESSION}
    * 
    */
   public static final int ACC_OFFSET_03_TYPE1       = A_OBJECT_BASIC_SIZE + 2;

   public static final int ACC_OFFSET_04_1           = A_OBJECT_BASIC_SIZE + 3;

   /**
    * When operand fits in 4 bytes.
    * <br>
    * When operand is a String or integer array, pointer 2 bytes of data start, number of char 2 bytes.
    */
   public static final int ACC_OFFSET_05_OPERAND4    = A_OBJECT_BASIC_SIZE + 4;

   /**
    * Compares input value with = > < operators against an operand
    */
   public static final int ACC_TYPE_0_INT            = 0;

   /**
    * Accepts input value if it is in the operand int [] array
    */
   public static final int ACC_TYPE_1_ARRAY          = 1;

   /**
    * Pointer on a ByteObject
    */
   public static final int ACC_TYPE_2_BYTEOBJECT     = 2;

   /**
    * Accepts String
    */
   public static final int ACC_TYPE_3_STRING         = 3;

   /**
    * Consider integer input value as a date years/month/day/minutes.
    */
   public static final int ACC_TYPE_4_INT_DATE       = 4;

   /**
    * Consider integer input value as a date years/month/day/minutes.
    */
   public static final int ACC_TYPE_5_DATE_LONG      = 5;

   /**
    * Acceptor is a composite of 2 Acceptor + 
    */
   public static final int ACC_TYPE_7_EXPRESSION     = 7;

   public static final int ACC_TYPE_6_STRING_POINTER = 6;

   public static final int OP_COMP_0_EQUAL           = 0;

   public static final int OP_COMP_1_SMALLER         = 1;

   public static final int OP_COMP_2_BIGGER          = 2;

   public static final int OP_COMP_3_DIFFERENT       = 3;

   /**
    * is input date in the same day as operand.
    */
   public static final int OP_DATE_DAY               = 24;

   /**
    * 
    * long hosts starting date int hosts the number of days backwards any
    * time interval can be modelized with this operator
    */
   public static final int OP_DATE_LAST              = 25;

   /**
    * is input date in the same month as operand.
    */
   public static final int OP_DATE_MONTH             = 21;

   /**
    * is input date in the same week as operand.
    */
   public static final int OP_DATE_WEEK              = 23;

   /**
    * is input date in the same year as operand.
    */
   public static final int OP_DATE_YEAR              = 22;

   /**
    * existance operator for listing Filterable without a given field
    */
   public static final int OP_DOES_NOT_EXIST         = 100;

   public static final int OP_INT_0_EQUAL            = 0;

   public static final int OP_INT_1_HIGHER           = 1;

   public static final int OP_INT_2_LOWER            = 2;

   public static final int OP_INT_ARRAY_CONTAINS     = 3;

   public static final int OP_LOGICAL_0_AND          = 0;

   public static final int OP_LOGICAL_1_OR           = 1;

   public static final int OP_LOGICAL_2_NAND         = 2;

   public static final int OP_LOGICAL_3_XOR          = 3;

   public static final int OP_NOT_EQUAL              = 101;

   public static final int OP_STRING_0_EQUALS        = 0;

   /**
    * Is input string starting with operand string
    */
   public static final int OP_STRING_1_STARTS        = 1;

   public static final int OP_STRING_2_ENDS          = 2;

}
