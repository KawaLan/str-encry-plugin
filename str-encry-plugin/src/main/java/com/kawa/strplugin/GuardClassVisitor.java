package com.kawa.strplugin;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class GuardClassVisitor extends ClassVisitor implements Opcodes {

  private String owner;

  public GuardClassVisitor(final ClassVisitor cv) {
    super(Opcodes.ASM4, cv);
  }

  public GuardClassVisitor(ClassVisitor cv,String decryptFile,String decryptMethod) {
    super(Opcodes.ASM4, cv);
    ClassConst.NATIVE_ENCRYPT = decryptFile;
    ClassConst.DECRYPT_METHOD_NAME = decryptMethod;
    System.out.println(ClassConst.NATIVE_ENCRYPT+"___"+ClassConst.DECRYPT_METHOD_NAME);
  }

  @Override
  public void visit(int version, int access, String name, String signature,
                    String superName, String[] interfaces) {
    cv.visit(version, access, name, signature, superName, interfaces);
    owner = name;
  }

  @Override
  public FieldVisitor visitField(
      int access,
      String name,
      String desc,
      String sig,
      Object value) {
    if (value != null && value instanceof String) {
      //如果字段加final ,则可以有默认值value,否则为null
      if ((access & Opcodes.ACC_STATIC) != 0 && (access & Opcodes.ACC_FINAL) != 0) {
        ClassConst.guardFinalStaticFieldMaps.put(name, new GuardField(name, (String) value, access));
      }else if((access & Opcodes.ACC_FINAL) != 0){
        // final字段有个特别的地方，就是final字段值设置null无效，后面还是有内容
        ClassConst.guardFinalFiedlMaps.put(name, new GuardField(name, (String) value, access));
      }
      //把字段值设置空
      return super.visitField(access, name, desc, sig, null);
    }
    return super.visitField(access, name, desc, sig, value);
  }

  @Override
  public MethodVisitor visitMethod(
      int access,
      String name,
      String desc,
      String signature,
      String[] exceptions) {

    MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);//先得到原始的方法

    // 在类的构造方法和静态代码块对字段进行加密处理
    // 这里有一个问题就是加密操作可能会重复，比如类中有多个构造方法，如果只在一个构造方法中处理，无法判断外部到底调用哪个类型的构造方法
    if ("<init>".equals(name)) {
      // 构造方法，加密final类型变量
      return new GuardMethodVisitor(mv, owner, ClassConst.guardFinalFiedlMaps);
    }
    if ("<clinit>".equals(name)) {
      // 静态代码块，加密final static类型变量
      return new GuardMethodVisitor(mv, owner, ClassConst.guardFinalStaticFieldMaps);
    }

    return new GuardMethodVisitor(mv); //访问需要修改的方法
  }

}

