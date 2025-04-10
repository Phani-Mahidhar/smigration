package com.sas.transformer.pyspark;

public record PySparkConfig(boolean setupSparkSession, String appName) {
  public static PySparkConfig DEFAULT = new PySparkConfig(true, "SasMigration");
}
