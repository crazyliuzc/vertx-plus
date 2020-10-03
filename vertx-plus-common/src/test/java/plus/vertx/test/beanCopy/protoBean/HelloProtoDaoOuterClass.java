package plus.vertx.test.beanCopy.protoBean;

/**
 *
 * @author crazyliu
 */
public class HelloProtoDaoOuterClass {

    private HelloProtoDaoOuterClass() {
    }

    public static void registerAllExtensions(
            com.google.protobuf.ExtensionRegistryLite registry) {
    }

    public static void registerAllExtensions(
            com.google.protobuf.ExtensionRegistry registry) {
        registerAllExtensions(
                (com.google.protobuf.ExtensionRegistryLite) registry);
    }
    static final com.google.protobuf.Descriptors.Descriptor internal_static_com_ywmapp_dao_HelloWorldRequest_descriptor;
    static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable internal_static_com_ywmapp_dao_HelloWorldRequest_fieldAccessorTable;
    static final com.google.protobuf.Descriptors.Descriptor internal_static_com_ywmapp_dao_HelloWorldResponse_descriptor;
    static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable internal_static_com_ywmapp_dao_HelloWorldResponse_fieldAccessorTable;

    public static com.google.protobuf.Descriptors.FileDescriptor
            getDescriptor() {
        return descriptor;
    }
    private static com.google.protobuf.Descriptors.FileDescriptor descriptor;

    static {
        java.lang.String[] descriptorData = {
            "\n\023HelloWorldDao.protoBean\022\016com.ywmapp.dao\"$\n"
            + "\021HelloWorldRequest\022\017\n\007message\030\001 \001(\t\"5\n\022H"
            + "elloWorldResponse\022\017\n\007message\030\001 \001(\t\022\016\n\006st"
            + "atus\030\002 \001(\005*:\n\rServingStatus\022\013\n\007UNKNOWN\020\000"
            + "\022\013\n\007SERVING\020\001\022\017\n\013NOT_SERVING\020\0022j\n\rHelloW"
            + "orldDao\022Y\n\016HelloWorldTest\022!.com.ywmapp.d"
            + "ao.HelloWorldRequest\032\".com.ywmapp.dao.He"
            + "lloWorldResponse\"\000B\022\n\016com.ywmapp.daoP\001b\006"
            + "proto3"
        };
        com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner
                = new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
            public com.google.protobuf.ExtensionRegistry assignDescriptors(
                    com.google.protobuf.Descriptors.FileDescriptor root) {
                descriptor = root;
                return null;
            }
        };
        com.google.protobuf.Descriptors.FileDescriptor
                .internalBuildGeneratedFileFrom(descriptorData,
                        new com.google.protobuf.Descriptors.FileDescriptor[]{}, assigner);
        internal_static_com_ywmapp_dao_HelloWorldRequest_descriptor
                = getDescriptor().getMessageTypes().get(0);
        internal_static_com_ywmapp_dao_HelloWorldRequest_fieldAccessorTable = new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
                internal_static_com_ywmapp_dao_HelloWorldRequest_descriptor,
                new java.lang.String[]{"Message",});
        internal_static_com_ywmapp_dao_HelloWorldResponse_descriptor
                = getDescriptor().getMessageTypes().get(1);
        internal_static_com_ywmapp_dao_HelloWorldResponse_fieldAccessorTable = new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
                internal_static_com_ywmapp_dao_HelloWorldResponse_descriptor,
                new java.lang.String[]{"Message", "Status",});
    }
}
