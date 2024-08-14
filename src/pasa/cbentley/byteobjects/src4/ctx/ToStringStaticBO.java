/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.byteobjects.src4.ctx;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.objects.color.IBOColorFunction;
import pasa.cbentley.byteobjects.src4.objects.color.IBOColorRnd;
import pasa.cbentley.byteobjects.src4.objects.color.ITechBlend;
import pasa.cbentley.byteobjects.src4.objects.color.ITechColorFunction;
import pasa.cbentley.byteobjects.src4.objects.color.ITechFilter;
import pasa.cbentley.byteobjects.src4.objects.color.ITechGradient;
import pasa.cbentley.byteobjects.src4.objects.function.ITechAcceptor;
import pasa.cbentley.byteobjects.src4.objects.function.ITechFunction;
import pasa.cbentley.byteobjects.src4.objects.function.ITechOperator;
import pasa.cbentley.byteobjects.src4.objects.move.ITechMoveFunction;
import pasa.cbentley.core.src4.helpers.StringBBuilder;
import pasa.cbentley.core.src4.logging.ToStringStaticBase;

public class ToStringStaticBO extends ToStringStaticBase implements IBOColorFunction {

   public static String toStringMoveIncrementType(int v) {
      switch (v) {
         case ITechMoveFunction.INCREMENT_0_PARAM:
            return "Linear";
         case ITechMoveFunction.INCREMENT_1_FIB:
            return "Fib";
         default:
            return "Unknown" + v;
      }
   }

   public static String toStringMoveType(int v) {
      switch (v) {
         case ITechMoveFunction.TYPE_MOVE_0_ASAP:
            return "ASAP";
         case ITechMoveFunction.TYPE_MOVE_1_BRESENHAM:
            return "Bresenham";
         default:
            return "Unknown" + v;
      }
   }
   public static String toStringGradEllipse(int value) {
      switch (value) {
         case ITechGradient.GRADIENT_TYPE_ELLIPSE_00_NORMAL:
            return "Normal";
         case ITechGradient.GRADIENT_TYPE_ELLIPSE_01_HORIZ:
            return "Horizontal";
         case ITechGradient.GRADIENT_TYPE_ELLIPSE_02_VERT:
            return "Vertical";
         case ITechGradient.GRADIENT_TYPE_ELLIPSE_03_TOP_FLAMME:
            return "Top Flamme";
         case ITechGradient.GRADIENT_TYPE_ELLIPSE_04_BOT_FLAMME:
            return "Bot Flamme";
         case ITechGradient.GRADIENT_TYPE_ELLIPSE_05_LEFT_FLAMME:
            return "Left Flame";
         case ITechGradient.GRADIENT_TYPE_ELLIPSE_06_RIGHT_FLAMME:
            return "Right Flame";
         case ITechGradient.GRADIENT_TYPE_ELLIPSE_07_CLOCHE_TOP:
            return "Top Cloche";
         case ITechGradient.GRADIENT_TYPE_ELLIPSE_08_CLOCHE_BOT:
            return "Bot Cloche";
         case ITechGradient.GRADIENT_TYPE_ELLIPSE_09_CLOCHE_LEFT:
            return "Left Cloche";
         case ITechGradient.GRADIENT_TYPE_ELLIPSE_10_CLOCHE_RIGHT:
            return "Right Cloche";
         case ITechGradient.GRADIENT_TYPE_ELLIPSE_11_WATER_DROP_TOP:
            return "Top Water Drop";
         case ITechGradient.GRADIENT_TYPE_ELLIPSE_12_WATER_DROP_BOT:
            return "Bot Water Drop";
         case ITechGradient.GRADIENT_TYPE_ELLIPSE_13_WATER_DROP_LEFT:
            return "Left Water Drop";
         case ITechGradient.GRADIENT_TYPE_ELLIPSE_14_WATER_DROP_RIGHT:
            return "Right Water Drop";
         case ITechGradient.GRADIENT_TYPE_ELLIPSE_15_TOP_LEFT_BUBBLE:
            return "Top Left Bubble";
         case ITechGradient.GRADIENT_TYPE_ELLIPSE_16_TOP_RIGHT_BUBBLE:
            return "Top Right Bubble";
         case ITechGradient.GRADIENT_TYPE_ELLIPSE_17_BOT_LEFT_BUBBLE:
            return "Bot Left Bubble";
         case ITechGradient.GRADIENT_TYPE_ELLIPSE_18_BOT_RIGHT_BUBBLE:
            return "Bot Right Bubble";
         default:
            return "Unknown " + value;
      }
   }

   public static String toStringGradPre(int val) {
      switch (val) {
         case ITechGradient.GRADIENT_PRE_0_NONE:
            return "CK";
         case ITechGradient.GRADIENT_PRE_1_0:
            return "0";
         case ITechGradient.GRADIENT_PRE_2_50:
            return "50 Middle";
         case ITechGradient.GRADIENT_PRE_3_100:
            return "100";
         default:
            return "Unknown Value Type " + val;
      }
   }

   public static String toStringColorRndType(int t) {
      switch (t) {
         case IBOColorRnd.RND_COLORS_TYPE_0_RND_32BITS:
            return "Random 32bits";
         case IBOColorRnd.RND_COLORS_TYPE_1_CHANNEL:
            return "Channel";
         case IBOColorRnd.RND_COLORS_TYPE_2_CHANNEL_SLOPE:
            return "Channel Slope";
         case IBOColorRnd.RND_COLORS_TYPE_3_CHANNEL_MOD:
            return "Channel Mod";
         case IBOColorRnd.RND_COLORS_TYPE_4_GRAYSCALE:
            return "Grayscale";
         case IBOColorRnd.RND_COLORS_TYPE_5_FIXED_BW_ROOT:
            return "Fixed BW Root";
         case IBOColorRnd.RND_COLORS_TYPE_6_PRE_SET:
            return "Preset";
         case IBOColorRnd.RND_COLORS_TYPE_7_BLEND_VARIATION:
            return "Color Blend Variation";
         case IBOColorRnd.RND_COLORS_TYPE_8_FIXEDEXTREMES:
            return "Fixed Extremes";

         default:
            return "Unknown Rnd Colors Type " + t;
      }
   }

   public static String gradLosange(int value) {
      switch (value) {
         case ITechGradient.GRADIENT_TYPE_LOSANGE_0_SQUARE:
            return "Square";
         case ITechGradient.GRADIENT_TYPE_LOSANGE_1_FULLVERTICAL:
            return "FullVertical";
         case ITechGradient.GRADIENT_TYPE_LOSANGE_2_FULLHORIZ:
            return "FullHoriz";
         case ITechGradient.GRADIENT_TYPE_LOSANGE_3_FULLDIAGDOWN:
            return "FullDiagDown";
         case ITechGradient.GRADIENT_TYPE_LOSANGE_4_FULLDIAGUP:
            return "FullDiagUp";
         case ITechGradient.GRADIENT_TYPE_LOSANGE_7_LEFT:
            return "Left";
         case ITechGradient.GRADIENT_TYPE_LOSANGE_8_RIGHT:
            return "Right";
         case ITechGradient.GRADIENT_TYPE_LOSANGE_5_TOP:
            return "Top";
         case ITechGradient.GRADIENT_TYPE_LOSANGE_6_BOT:
            return "Bot";
         default:
            return "Unknown " + value;
      }
   }

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

   public static String toStringGradRect(int value) {
      switch (value) {
         case ITechGradient.GRADIENT_TYPE_RECT_05_BOTLEFT:
            return "BotLeft";
         case ITechGradient.GRADIENT_TYPE_RECT_06_BOTRIGHT:
            return "BotRight";
         case ITechGradient.GRADIENT_TYPE_RECT_01_HORIZ:
            return "Horizontal";
         case ITechGradient.GRADIENT_TYPE_RECT_00_SQUARE:
            return "Square";
         case ITechGradient.GRADIENT_TYPE_RECT_03_TOPLEFT:
            return "TopLeft";
         case ITechGradient.GRADIENT_TYPE_RECT_04_TOPRIGHT:
            return "TopRight";
         case ITechGradient.GRADIENT_TYPE_RECT_02_VERT:
            return "Vertical";
         case ITechGradient.GRADIENT_TYPE_RECT_07_L_TOP:
            return "L Top";
         case ITechGradient.GRADIENT_TYPE_RECT_08_L_BOT:
            return "L Bot";
         case ITechGradient.GRADIENT_TYPE_RECT_09_L_LEFT:
            return "L Left";
         case ITechGradient.GRADIENT_TYPE_RECT_10_L_RIGHT:
            return "L Right";
         default:
            return "Unknown " + value;
      }
   }

   public static String toStringGradTrig(int value) {
      switch (value) {
         case ITechGradient.GRADIENT_TYPE_TRIG_00_TENT:
            return "Tent";
         case ITechGradient.GRADIENT_TYPE_TRIG_01_TENT_JESUS:
            return "JesusTent";
         case ITechGradient.GRADIENT_TYPE_TRIG_02_TOP_JESUS:
            return "JesusTop";
         case ITechGradient.GRADIENT_TYPE_TRIG_03_TUNNEL:
            return "Tunnel";
         case ITechGradient.GRADIENT_TYPE_TRIG_04_FULL:
            return "Full";
         case ITechGradient.GRADIENT_TYPE_TRIG_05_OPAQUEBASE:
            return "OpaqueBase";
         case ITechGradient.GRADIENT_TYPE_TRIG_06_OPAQUE_CENTER:
            return "OpaqueCenter";
         case ITechGradient.GRADIENT_TYPE_TRIG_07_ARROW:
            return "Arrow";
         case ITechGradient.GRADIENT_TYPE_TRIG_08_NORMAL:
            return "Normal";
         case ITechGradient.GRADIENT_TYPE_TRIG_09_FAT_HALO:
            return "Halo";
         case ITechGradient.GRADIENT_TYPE_TRIG_10_SWIPE:
            return "Swipe";

         default:
            return "Unknown " + value;
      }
   }

   public static String toStringFilterType(int val) {
      switch (val) {
         case ITechFilter.FILTER_TYPE_00_FUNCTION_ALL:
            return "GenericFunctionOnAllPixels";
         case ITechFilter.FILTER_TYPE_01_GRAYSCALE:
            return "Grayscale";
         case ITechFilter.FILTER_TYPE_02_BILINEAR:
            return "Bilinear";
         case ITechFilter.FILTER_TYPE_03_ALPHA_TO_COLOR:
            return "AlphaToColor";
         case ITechFilter.FILTER_TYPE_04_SIMPLE_ALPHA:
            return "SimpleAlpha";
         case ITechFilter.FILTER_TYPE_05_REPEAT_PIXEL:
            return "RepeatPixel";
         case ITechFilter.FILTER_TYPE_06_STEP_SMOOTH:
            return "StepSmooth";
         case ITechFilter.FILTER_TYPE_07_TBLR:
            return "TBLR";
         case ITechFilter.FILTER_TYPE_08_TOUCHES:
            return "Touches";
         case ITechFilter.FILTER_TYPE_09_STICK:
            return "Stick";
         case ITechFilter.FILTER_TYPE_10_SEPIA:
            return "Sepia";
         case ITechFilter.FILTER_TYPE_11_HORIZ_AVERAGE:
            return "HorizAverage";
         case ITechFilter.FILTER_TYPE_12_HORIZ_AVERAGE_NEOM:
            return "HorizAverageNeom";
         case ITechFilter.FILTER_TYPE_13_CHANNEL_MOD:
            return "ChannelMod";
         case ITechFilter.FILTER_TYPE_14_BLEND_SELF:
            return "BlendSelf";
         default:
            return "Unknown Filter Type " + val;
      }
   }

   public static String toStringColor(int c) {
      return "(" + ((c >> 24) & 0xFF) + "," + ((c >> 16) & 0xFF) + "," + ((c >> 8) & 0xFF) + "," + (c & 0xFF) + ")";
   }

   public static String toStringOpDuff(int op) {
      switch (op) {
         case ITechBlend.OP_00_SRC_OVER:
            return "src over";
         case ITechBlend.OP_01_SRC:
            return "src";
         case ITechBlend.OP_02_SRC_IN:
            return "src in";
         case ITechBlend.OP_03_SRC_OUT:
            return "src out";
         case ITechBlend.OP_04_SRC_ATOP:
            return "src atop";
         case ITechBlend.OP_05_DST_OVER:
            return "dest over";
         case ITechBlend.OP_06_DST:
            return "dst";
         case ITechBlend.OP_07_DST_IN:
            return "dst in";
         case ITechBlend.OP_08_DST_OUT:
            return "dst out";
         case ITechBlend.OP_09_DST_ATOP:
            return "dst atop";
         case ITechBlend.OP_10_XOR:
            return "xor";
         case ITechBlend.OP_11_CLEAR:
            return "clear";
         default:
            return "Unknown " + op;
      }
   }

   public static String toStringBlend(int mode) {
      switch (mode) {
         case ITechBlend.BLENDING_00_OVER:
            return "OVER";
         case ITechBlend.BLENDING_01_SRC:
            return "SRC";
         case ITechBlend.BLENDING_04_MERGE_ARGB:
            return "MergeARGB";
         case ITechBlend.BLENDING_05_BEHIND:
            return "Behind";
         case ITechBlend.BLENDING_07_INVERSE:
            return "Inverse";
         case ITechBlend.BLENDING_08_DISSOLVE:
            return "Dissolve";
         case ITechBlend.BLENDING_09_INVERSE:
            return "Inverse";
         case ITechBlend.BLENDING_10_HUE:
            return "Hue";
         case ITechBlend.BLENDING_11_HUE_SAT:
            return "Hue Saturation";
         case ITechBlend.BLENDING_12_HUE_LUM:
            return "Hue Luminance";
         case ITechBlend.BLENDING_13_SATURATION:
            return "Saturation";
         case ITechBlend.BLENDING_14_SAT_LUM:
            return "SaturationLuminance";
         case ITechBlend.BLENDING_15_LUMINANCE:
            return "Luminance";
         case ITechBlend.BLENDING_16_MULTIPLY_BURN:
            return "Multiply Burn";
         case ITechBlend.BLENDING_17_COLOR_BURN:
            return "Color Burn";
         case ITechBlend.BLENDING_18_LINEAR_BURN:
            return "Luminance";
         case ITechBlend.BLENDING_19_SCREEN_DODGE:
            return "Screen Dodge";
         case ITechBlend.BLENDING_20_COLOR_DODGE:
            return "Color Dodge";
         case ITechBlend.BLENDING_21_LINEAR_DODGE:
            return "Linear Dodge";
         case ITechBlend.BLENDING_22_HARD_MIX:
            return "Hard Mix";
         case ITechBlend.BLENDING_23_ADDITION:
            return "Addition";
         case ITechBlend.BLENDING_26_:
            return "Substraction";
         case ITechBlend.BLENDING_25_DIVIDE:
            return "Divide";
         case ITechBlend.BLENDING_24_DIFFERENCE:
            return "Difference";
         case ITechBlend.BLENDING_27_EXCLUSION_NEGATION:
            return "Exclusion";
         case ITechBlend.BLENDING_28_:
            return "_";
         case ITechBlend.BLENDING_29_PIN_LIGHT:
            return "Pin Light";
         case ITechBlend.BLENDING_02_DARKEN:
            return "Darken";
         case ITechBlend.BLENDING_03_LIGHTEN:
            return "Lighten";

         default:
            return "Unknown " + mode;
      }
   }

   public static String toStringAlpha(int mode) {
      switch (mode) {
         case ITechBlend.ALPHA_0_OVER:
            return "Over";
         default:
            return "Unknown " + mode;
      }
   }

   public static String toStringOpacity(int op) {
      switch (op) {
         case ITechBlend.OPACITY_00_SRC:
            return "src";
         case ITechBlend.OPACITY_01_MIN_OVERIDE_SRC:
            return "min src";
         case ITechBlend.OPACITY_02_MAX_OVERIDE_SRC:
            return "max src";
         case ITechBlend.OPACITY_03_OVERIDE_SRC:
            return "overide src";
         default:
            return "Unknown " + op;
      }
   }

   public static String toStringOperand(int op) {
      switch (op) {
         case ITechAcceptor.ACC_TYPE_0_INT:
            return "Int";
         case ITechAcceptor.ACC_TYPE_1_ARRAY:
            return "Array";
         case ITechAcceptor.ACC_TYPE_2_BYTEOBJECT:
            return "ByteObject";
         case ITechAcceptor.ACC_TYPE_3_STRING:
            return "String";
         case ITechAcceptor.ACC_TYPE_4_INT_DATE:
            return "IntDate";
         case ITechAcceptor.ACC_TYPE_5_DATE_LONG:
            return "DateLong";
         case ITechAcceptor.ACC_TYPE_6_STRING_POINTER:
            return "StringPointer";
         case ITechAcceptor.ACC_TYPE_7_EXPRESSION:
            return "Expression";

         default:
            return "Unknown Operand" + op;
      }
   }

   /**
    * {@link IToStringsDIDsBoc#DID_05_ACCEPTOR_OP}
    * @param op
    * @return
    */
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
            return "Unknown Op" + op;
      }
   }

   public static void debugFunctionChannel(StringBBuilder sb, ByteObject f) {
      sb.append("channels=" + f.hasFlag(FUN_OFFSET_03_FLAGP, FUN_FLAGP_4_CHANNELS));
      sb.append(" a=" + f.hasFlag(FUN_OFFSET_03_FLAGP, IBOColorFunction.FUNCTION_FLAGP_5_ALPHA));
      sb.append(" r=" + f.hasFlag(FUN_OFFSET_03_FLAGP, IBOColorFunction.FUNCTION_FLAGP_6_RED));
      sb.append(" g=" + f.hasFlag(FUN_OFFSET_03_FLAGP, IBOColorFunction.FUNCTION_FLAGP_7_GREEN));
      sb.append(" b=" + f.hasFlag(FUN_OFFSET_03_FLAGP, IBOColorFunction.FUNCTION_FLAGP_8_BLUE));
   }

   public static String toStringRndColorType(int i) {
      switch (i) {
         case IBOColorRnd.RND_COLORS_TYPE_0_RND_32BITS:
            return "Random 32bits";
         case IBOColorRnd.RND_COLORS_TYPE_1_CHANNEL:
            return "Random Channels";
         case IBOColorRnd.RND_COLORS_TYPE_8_FIXEDEXTREMES:
            return "Extremes";
         case IBOColorRnd.RND_COLORS_TYPE_5_FIXED_BW_ROOT:
            return "Black&White+Root";
         case IBOColorRnd.RND_COLORS_TYPE_4_GRAYSCALE:
            return "Grayscale";
         default:
            return "UNKNOWN TYPE " + i;
      }
   }

   public static String toStringSwitchMode(int mode) {
      switch (mode) {
         case ITechColorFunction.MODE_1_ALPHA_CHANNEL:
            return "MODE_ALPHA_CHANNEL";
         case ITechColorFunction.MODE_2_ARGB_CHANNELS:
            return "MODE_ARGB_CHANNELS";
         case ITechColorFunction.MODE_3_32_BITS:
            return "MODE_32_BITS";
         case ITechColorFunction.MODE_0_SAFETY:
            return "SAFETY";
         default:
            return "UNKNOWN " + mode;
      }
   }

   public static String toStringCounterOp(int type) {
      switch (type) {
         case ITechFunction.FUN_COUNTER_OP_0_ASC:
            return "Ascending";
         case ITechFunction.FUN_COUNTER_OP_1_DESC:
            return "Descending";
         case ITechFunction.FUN_COUNTER_OP_2_RANDOM:
            return "Random";
         case ITechFunction.FUN_COUNTER_OP_3_UP_DOWN:
            return "Up and Down";
         default:
            return "Unknown Counter Op Type" + type;
      }
   }

   public static String toStringFunType(int type) {
      switch (type) {
         case ITechFunction.FUN_TYPE_00_AXC:
            return "AxC";
         case ITechFunction.FUN_TYPE_01_VALUES:
            return "Preset Values";
         case ITechFunction.FUN_TYPE_02_ID:
            return "ID";
         case ITechFunction.FUN_TYPE_03_RANDOM_INT:
            return "Random Int";
         case ITechFunction.FUN_TYPE_04_TICK:
            return "Tick";
         case ITechFunction.FUN_TYPE_05_MOVE:
            return "Move";
         case ITechFunction.FUN_TYPE_06_COLOR:
            return "Color";
         case ITechFunction.FUN_TYPE_07_MATH_OPERATOR:
            return "Math Operator";
         default:
            return "Unknown Fun Type" + type;
      }
   }

   public static String toStringPostOp(int op) {
      switch (op) {
         case ITechOperator.OP_POST_0_NONE:
            return "None";
         case ITechOperator.OP_POST_1_X_MAX:
            return "Maximum";
         case ITechOperator.OP_POST_2_X_MIN:
            return "Minimum";
         case ITechOperator.OP_POST_3_DISTANCE_SET:
            return "Distance";
         case ITechOperator.OP_POST_4_DISTANCE_SUBSTRACT_MIN0:
            return "Substract Distance To 0";
         case ITechOperator.OP_POST_5_DISTANCE_ADDITION:
            return "Add Distance";
         case ITechOperator.OP_POST_6_ABS_MULTIPLY:
            return "Absolute of Multiply";
         default:
            return "Unknown Operator" + op;
      }
   }

}
