// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: Hello.proto

package plus.vertx.test.beanCopy.protoBean;

public final class Hello {
  private Hello() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_plus_vertx_test_beanCopy_protoBeant_HelloRequest_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_plus_vertx_test_beanCopy_protoBeant_HelloRequest_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_plus_vertx_test_beanCopy_protoBeant_HelloResponse_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_plus_vertx_test_beanCopy_protoBeant_HelloResponse_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    String[] descriptorData = {
      "\n\013Hello.proto\022#plus.vertx.test.beanCopy." +
      "protoBeant\"\037\n\014HelloRequest\022\017\n\007message\030\001 " +
      "\001(\t\"0\n\rHelloResponse\022\017\n\007message\030\001 \001(\t\022\016\n" +
      "\006status\030\002 \001(\005*:\n\rServingStatus\022\013\n\007UNKNOW" +
      "N\020\000\022\013\n\007SERVING\020\001\022\017\n\013NOT_SERVING\020\0022\205\001\n\010He" +
      "lloDao\022y\n\016HelloWorldTest\0221.plus.vertx.te" +
      "st.beanCopy.protoBeant.HelloRequest\0322.pl" +
      "us.vertx.test.beanCopy.protoBeant.HelloR" +
      "esponse\"\000B&\n\"plus.vertx.test.beanCopy.pr" +
      "otoBeanP\001b\006proto3"
    };
    descriptor = com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        });
    internal_static_plus_vertx_test_beanCopy_protoBeant_HelloRequest_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_plus_vertx_test_beanCopy_protoBeant_HelloRequest_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_plus_vertx_test_beanCopy_protoBeant_HelloRequest_descriptor,
        new String[] { "Message", });
    internal_static_plus_vertx_test_beanCopy_protoBeant_HelloResponse_descriptor =
      getDescriptor().getMessageTypes().get(1);
    internal_static_plus_vertx_test_beanCopy_protoBeant_HelloResponse_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_plus_vertx_test_beanCopy_protoBeant_HelloResponse_descriptor,
        new String[] { "Message", "Status", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}