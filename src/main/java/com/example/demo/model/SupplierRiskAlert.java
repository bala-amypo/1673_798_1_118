ovider.java: Recompile with -Xlint:deprecation for details.
[INFO] -------------------------------------------------------------
[ERROR] COMPILATION ERROR : 
[INFO] -------------------------------------------------------------
[ERROR] /home/coder/Workspace/demo/src/main/java/com/example/demo/service/impl/DelayScoreServiceImpl.java:[87,39] constructor SupplierRiskAlert in class com.example.demo.model.SupplierRiskAlert cannot be applied to given types;
  required: no arguments
  found:    java.lang.Long,java.lang.String,java.lang.String
  reason: actual and formal argument lists differ in length
[INFO] 1 error
[INFO] -------------------------------------------------------------
[INFO] ------------------------------------------------------------------------
[INFO] BUILD FAILURE
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  23.696 s
[INFO] Finished at: 2025-12-26T09:14:04Z
[INFO] ------------------------------------------------------------------------
[ERROR] Failed to execute goal org.apache.maven.plugins:maven-compiler-plugin:3.13.0:compile (default-compile) on project demo: Compilation failure
[ERROR] /home/coder/Workspace/demo/src/main/java/com/example/demo/service/impl/DelayScoreServiceImpl.java:[87,39] constructor SupplierRiskAlert in class com.example.demo.model.SupplierRiskAlert cannot be applied to given types;
[ERROR]   required: no arguments
[ERROR]   found:    java.lang.Long,java.lang.String,java.lang.String
[ERROR]   reason: actual and formal argument lists differ in length
[ERROR] 
[ERROR] -> [Help 1]
[ERROR] 
[ERROR] To see the full stack trace of the errors, re-run Maven with the -e switch.
[ERROR] Re-run Maven using the -X switch to enable full debug logging.
[ERROR] 
[ERROR] For more information about the errors and possible solutions, please read the following articles:
[ERROR] [Help 1] http://cwiki.apache.org/confluence/display/MAVEN/MojoFailureException