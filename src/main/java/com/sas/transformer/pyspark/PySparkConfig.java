package com.sas.transformer.pyspark;

public record PySparkConfig(boolean setupSparkSession, String appName, boolean capitalizeIdentifier) {
  public static PySparkConfig DEFAULT = new PySparkConfig(true, "SAS to PySpark Migration", true);
}
