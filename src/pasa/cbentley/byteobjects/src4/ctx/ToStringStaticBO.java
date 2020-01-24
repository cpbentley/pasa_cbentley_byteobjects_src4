package pasa.cbentley.byteobjects.src4.ctx;

import pasa.cbentley.byteobjects.src4.tech.ITechAcceptor;

public class ToStringStaticBO {

   
   public static String toStringAcceptorType(int type) {
      
      switch (type) {
         case ITechAcceptor.ACC_TYPE_0_INT:
            return "int";
         case ITechAcceptor.ACC_TYPE_1_ARRAY:
            return "ints";
         case ITechAcceptor.ACC_TYPE_2_BYTEOBJECT:
            return "byteobject";
         case ITechAcceptor.ACC_TYPE_3_STRING:
            return "string";
         case ITechAcceptor.ACC_TYPE_4_INT_DATE:
            return "dateint";
         case ITechAcceptor.ACC_TYPE_5_DATE_LONG:
            return "datelong";
         case ITechAcceptor.ACC_TYPE_6_STRING_POINTER:
            return "stringpointer";
         case ITechAcceptor.ACC_TYPE_7_EXPRESSION:
            return "expression";
         default:
            return null;
      }
   }
   
   public static String toStringOp(int op) {
      switch (op) {
         case ITechAcceptor.OP_COMP_0_EQUAL:
            return "=";
         case ITechAcceptor.OP_COMP_1_SMALLER:
            return "<";
         case ITechAcceptor.OP_COMP_2_BIGGER:
            return ">";
         case ITechAcceptor.OP_COMP_3_DIFFERENT:
            return "!=";
         case ITechAcceptor.OP_DATE_DAY:
            return "day";
         case ITechAcceptor.OP_DATE_LAST:
            return "last";
         case ITechAcceptor.OP_DATE_MONTH:
            return "month";
         case ITechAcceptor.OP_DATE_WEEK:
            return "week";
         case ITechAcceptor.OP_DATE_YEAR:
            return "year";
         default:
            return null;
      }
   }
   
}
