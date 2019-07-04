package pasa.cbentley.byteobjects.ctx;

public interface IDebugIDsBOC {
   
   public static final int DID__ROOT            = BOCtx.BOCTX_ID << 24;

   public static final int DID_01_FUNCTION_TYPE = DID__ROOT + 1;

   public static final int DID_02_POST_OP       = DID__ROOT + 2;

   public static final int DID_03_COUNTER_OP    = DID__ROOT + 3;

   public static final int DID_NUMBER           = 6;
}
