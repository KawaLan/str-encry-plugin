package com.kawa.strplugin;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.HashMap;
import java.util.Map;

public class GuardMethodVisitor extends MethodVisitor {

  private String owner;
  private Map<String, GuardField> fieldMap = new HashMap<>();
  private boolean isInitMethod;

  public GuardMethodVisitor(MethodVisitor mv) {
    super(Opcodes.ASM4, mv);
  }

  public GuardMethodVisitor(MethodVisitor mv, String owner, Map<String, GuardField> maps) {
    this(mv);
    this.owner = owner;
    isInitMethod = true;
    fieldMap = maps;
  }

  @Override
  public void visitCode() {
    super.visitCode();
  }

  @Override
  public void visitInsn(int opcode) {
    if (!isInitMethod) {
      // 非初始化方法这里不需要赋值类的字段值信息(static, final)
      super.visitInsn(opcode);
      return;
    }
    //返回代码之前添加
    if (opcode == Opcodes.RETURN) {
      for (String key : fieldMap.keySet()) {
        String encryptStr = NativeStringGuard.encryptStr(fieldMap.get(key).value);//加密原始字符串
        System.out.println(encryptStr);
        mv.visitLdcInsn(encryptStr);
        //需要调用解密方法解密字符串，不然程序字符串就乱了
        mv.visitMethodInsn(
            Opcodes.INVOKESTATIC, ClassConst.NATIVE_ENCRYPT,
            ClassConst.DECRYPT_METHOD_NAME, ClassConst.DECRYPT_METHOD_SIGN, false);
        mv.visitFieldInsn(Opcodes.PUTSTATIC, owner, key, "Ljava/lang/String;");
      }
    }
    //方法的每条指令
    super.visitInsn(opcode);
  }

  @Override
  public void visitFieldInsn(int opcode, String owner, String name, String desc) {
    super.visitFieldInsn(opcode, owner, name, desc);
  }

  @Override
  public void visitLdcInsn(Object cst) {
    if (cst != null && cst instanceof String) {
      // ldc string
      /**
       * 第一种情况：String param1 = "xxxxx"
       * 第二种情况：String param2 = param1 + "yyyyy"
       * 第三种情况：return "zzzzzz"
       */
      /**
       * 原始指令：
       *  ldc ....
       * 修改后的指令：
       *  ldc ....
       *  invokestatic
       */
      String encryptStr = NativeStringGuard.encryptStr((String)cst);//加密原始字符串
      mv.visitLdcInsn(encryptStr);
      //需要调用解密方法解密字符串，不然程序字符串就乱了
      mv.visitMethodInsn(
          Opcodes.INVOKESTATIC, ClassConst.NATIVE_ENCRYPT,
          ClassConst.DECRYPT_METHOD_NAME, ClassConst.DECRYPT_METHOD_SIGN, false);

      return;
    }
    super.visitLdcInsn(cst);
  }

  @Override
  public void visitParameter(String s, int i) {
    super.visitParameter(s, i);
  }

}

