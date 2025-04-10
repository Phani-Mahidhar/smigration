package com.sas.core.types;

public class BasicType {
  public static final BasicType UNKNOWN = new BasicType(TypeName.UNKNOWN);

  public static final BasicType NUMBER = new BasicType(TypeName.NUMBER);

  public static final BasicType STRING = new BasicType(TypeName.STRING);
  public static final BasicType BOOLEAN = new BasicType(TypeName.BOOLEAN);

  public static final BasicType DATE = new BasicType(TypeName.DATE);

  private final TypeName typeName;

  BasicType(TypeName typeName) {
    this.typeName = typeName;
  }

  public TypeName getTypeName() {
    return typeName;
  }
}