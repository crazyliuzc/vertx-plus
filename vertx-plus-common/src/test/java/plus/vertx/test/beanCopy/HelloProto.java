package plus.vertx.test.beanCopy;

/**
 *
 * @author crazyliu
 */
public class HelloProto extends
        com.google.protobuf.GeneratedMessageV3 implements
        // @@protoc_insertion_point(message_implements:HelloProto)
        HelloProtoRequestOrBuilder {

    private HelloProto(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
        super(builder);
    }

    private HelloProto() {
        message_ = "";
    }

    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
            getUnknownFields() {
        return com.google.protobuf.UnknownFieldSet.getDefaultInstance();
    }

    private HelloProto(
            com.google.protobuf.CodedInputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
        this();
        int mutable_bitField0_ = 0;
        try {
            boolean done = false;
            while (!done) {
                int tag = input.readTag();
                switch (tag) {
                    case 0:
                        done = true;
                        break;
                    default: {
                        if (!input.skipField(tag)) {
                            done = true;
                        }
                        break;
                    }
                    case 10: {
                        java.lang.String s = input.readStringRequireUtf8();

                        message_ = s;
                        break;
                    }
                }
            }
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
            throw e.setUnfinishedMessage(this);
        } catch (java.io.IOException e) {
            throw new com.google.protobuf.InvalidProtocolBufferException(
                    e).setUnfinishedMessage(this);
        } finally {
            makeExtensionsImmutable();
        }
    }

    public static final com.google.protobuf.Descriptors.Descriptor
            getDescriptor() {
        return HelloProtoDaoOuterClass.internal_static_com_ywmapp_dao_HelloWorldRequest_descriptor;
    }

    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
            internalGetFieldAccessorTable() {
        return HelloProtoDaoOuterClass.internal_static_com_ywmapp_dao_HelloWorldRequest_fieldAccessorTable
                .ensureFieldAccessorsInitialized(
                        HelloProto.class, HelloProto.Builder.class);
    }

    public static final int MESSAGE_FIELD_NUMBER = 1;
    private volatile java.lang.Object message_;

    /**
     * <code>string message = 1;</code>
     */
    public java.lang.String getMessage() {
        java.lang.Object ref = message_;
        if (ref instanceof java.lang.String) {
            return (java.lang.String) ref;
        } else {
            com.google.protobuf.ByteString bs
                    = (com.google.protobuf.ByteString) ref;
            java.lang.String s = bs.toStringUtf8();
            message_ = s;
            return s;
        }
    }

    /**
     * <code>string message = 1;</code>
     */
    public com.google.protobuf.ByteString
            getMessageBytes() {
        java.lang.Object ref = message_;
        if (ref instanceof java.lang.String) {
            com.google.protobuf.ByteString b
                    = com.google.protobuf.ByteString.copyFromUtf8(
                            (java.lang.String) ref);
            message_ = b;
            return b;
        } else {
            return (com.google.protobuf.ByteString) ref;
        }
    }

    private byte memoizedIsInitialized = -1;

    public final boolean isInitialized() {
        byte isInitialized = memoizedIsInitialized;
        if (isInitialized == 1) {
            return true;
        }
        if (isInitialized == 0) {
            return false;
        }

        memoizedIsInitialized = 1;
        return true;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
            throws java.io.IOException {
        if (!getMessageBytes().isEmpty()) {
            com.google.protobuf.GeneratedMessageV3.writeString(output, 1, message_);
        }
    }

    public int getSerializedSize() {
        int size = memoizedSize;
        if (size != -1) {
            return size;
        }

        size = 0;
        if (!getMessageBytes().isEmpty()) {
            size += com.google.protobuf.GeneratedMessageV3.computeStringSize(1, message_);
        }
        memoizedSize = size;
        return size;
    }

    private static final long serialVersionUID = 0L;

    @java.lang.Override
    public boolean equals(final java.lang.Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof HelloProto)) {
            return super.equals(obj);
        }
        HelloProto other = (HelloProto) obj;

        boolean result = true;
        result = result && getMessage()
                .equals(other.getMessage());
        return result;
    }

    @java.lang.Override
    public int hashCode() {
        if (memoizedHashCode != 0) {
            return memoizedHashCode;
        }
        int hash = 41;
        hash = (19 * hash) + getDescriptor().hashCode();
        hash = (37 * hash) + MESSAGE_FIELD_NUMBER;
        hash = (53 * hash) + getMessage().hashCode();
        hash = (29 * hash) + unknownFields.hashCode();
        memoizedHashCode = hash;
        return hash;
    }

    public static HelloProto parseFrom(
            com.google.protobuf.ByteString data)
            throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data);
    }

    public static HelloProto parseFrom(
            com.google.protobuf.ByteString data,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data, extensionRegistry);
    }

    public static HelloProto parseFrom(byte[] data)
            throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data);
    }

    public static HelloProto parseFrom(
            byte[] data,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data, extensionRegistry);
    }

    public static HelloProto parseFrom(java.io.InputStream input)
            throws java.io.IOException {
        return com.google.protobuf.GeneratedMessageV3
                .parseWithIOException(PARSER, input);
    }

    public static HelloProto parseFrom(
            java.io.InputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws java.io.IOException {
        return com.google.protobuf.GeneratedMessageV3
                .parseWithIOException(PARSER, input, extensionRegistry);
    }

    public static HelloProto parseDelimitedFrom(java.io.InputStream input)
            throws java.io.IOException {
        return com.google.protobuf.GeneratedMessageV3
                .parseDelimitedWithIOException(PARSER, input);
    }

    public static HelloProto parseDelimitedFrom(
            java.io.InputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws java.io.IOException {
        return com.google.protobuf.GeneratedMessageV3
                .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }

    public static HelloProto parseFrom(
            com.google.protobuf.CodedInputStream input)
            throws java.io.IOException {
        return com.google.protobuf.GeneratedMessageV3
                .parseWithIOException(PARSER, input);
    }

    public static HelloProto parseFrom(
            com.google.protobuf.CodedInputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws java.io.IOException {
        return com.google.protobuf.GeneratedMessageV3
                .parseWithIOException(PARSER, input, extensionRegistry);
    }

    public Builder newBuilderForType() {
        return newBuilder();
    }

    public static Builder newBuilder() {
        return DEFAULT_INSTANCE.toBuilder();
    }

    public static Builder newBuilder(HelloProto prototype) {
        return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }

    public Builder toBuilder() {
        return this == DEFAULT_INSTANCE
                ? new Builder() : new Builder().mergeFrom(this);
    }

    @java.lang.Override
    protected Builder newBuilderForType(
            com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
        Builder builder = new Builder(parent);
        return builder;
    }

    /**
     * Protobuf type {@code HelloProto}
     */
    public static final class Builder extends
            com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
            // @@protoc_insertion_point(builder_implements:HelloProto)
            HelloProtoRequestOrBuilder {

        public static final com.google.protobuf.Descriptors.Descriptor
                getDescriptor() {
            return HelloProtoDaoOuterClass.internal_static_com_ywmapp_dao_HelloWorldRequest_descriptor;
        }

        protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
                internalGetFieldAccessorTable() {
            return HelloProtoDaoOuterClass.internal_static_com_ywmapp_dao_HelloWorldRequest_fieldAccessorTable
                    .ensureFieldAccessorsInitialized(
                            HelloProto.class, HelloProto.Builder.class);
        }

        // Construct using HelloProto.newBuilder()
        private Builder() {
            maybeForceBuilderInitialization();
        }

        private Builder(
                com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
            super(parent);
            maybeForceBuilderInitialization();
        }

        private void maybeForceBuilderInitialization() {
            if (com.google.protobuf.GeneratedMessageV3.alwaysUseFieldBuilders) {
            }
        }

        public Builder clear() {
            super.clear();
            message_ = "";

            return this;
        }

        public com.google.protobuf.Descriptors.Descriptor
                getDescriptorForType() {
            return HelloProtoDaoOuterClass.internal_static_com_ywmapp_dao_HelloWorldRequest_descriptor;
        }

        public HelloProto getDefaultInstanceForType() {
            return HelloProto.getDefaultInstance();
        }

        public HelloProto build() {
            HelloProto result = buildPartial();
            if (!result.isInitialized()) {
                throw newUninitializedMessageException(result);
            }
            return result;
        }

        public HelloProto buildPartial() {
            HelloProto result = new HelloProto(this);
            result.message_ = message_;
            onBuilt();
            return result;
        }

        public Builder clone() {
            return (Builder) super.clone();
        }

        public Builder setField(
                com.google.protobuf.Descriptors.FieldDescriptor field,
                Object value) {
            return (Builder) super.setField(field, value);
        }

        public Builder clearField(
                com.google.protobuf.Descriptors.FieldDescriptor field) {
            return (Builder) super.clearField(field);
        }

        public Builder clearOneof(
                com.google.protobuf.Descriptors.OneofDescriptor oneof) {
            return (Builder) super.clearOneof(oneof);
        }

        public Builder setRepeatedField(
                com.google.protobuf.Descriptors.FieldDescriptor field,
                int index, Object value) {
            return (Builder) super.setRepeatedField(field, index, value);
        }

        public Builder addRepeatedField(
                com.google.protobuf.Descriptors.FieldDescriptor field,
                Object value) {
            return (Builder) super.addRepeatedField(field, value);
        }

        public Builder mergeFrom(com.google.protobuf.Message other) {
            if (other instanceof HelloProto) {
                return mergeFrom((HelloProto) other);
            } else {
                super.mergeFrom(other);
                return this;
            }
        }

        public Builder mergeFrom(HelloProto other) {
            if (other == HelloProto.getDefaultInstance()) {
                return this;
            }
            if (!other.getMessage().isEmpty()) {
                message_ = other.message_;
                onChanged();
            }
            onChanged();
            return this;
        }

        public final boolean isInitialized() {
            return true;
        }

        public Builder mergeFrom(
                com.google.protobuf.CodedInputStream input,
                com.google.protobuf.ExtensionRegistryLite extensionRegistry)
                throws java.io.IOException {
            HelloProto parsedMessage = null;
            try {
                parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
            } catch (com.google.protobuf.InvalidProtocolBufferException e) {
                parsedMessage = (HelloProto) e.getUnfinishedMessage();
                throw e.unwrapIOException();
            } finally {
                if (parsedMessage != null) {
                    mergeFrom(parsedMessage);
                }
            }
            return this;
        }

        private java.lang.Object message_ = "";

        /**
         * <code>string message = 1;</code>
         */
        public java.lang.String getMessage() {
            java.lang.Object ref = message_;
            if (!(ref instanceof java.lang.String)) {
                com.google.protobuf.ByteString bs
                        = (com.google.protobuf.ByteString) ref;
                java.lang.String s = bs.toStringUtf8();
                message_ = s;
                return s;
            } else {
                return (java.lang.String) ref;
            }
        }

        /**
         * <code>string message = 1;</code>
         */
        public com.google.protobuf.ByteString
                getMessageBytes() {
            java.lang.Object ref = message_;
            if (ref instanceof String) {
                com.google.protobuf.ByteString b
                        = com.google.protobuf.ByteString.copyFromUtf8(
                                (java.lang.String) ref);
                message_ = b;
                return b;
            } else {
                return (com.google.protobuf.ByteString) ref;
            }
        }

        /**
         * <code>string message = 1;</code>
         */
        public Builder setMessage(
                java.lang.String value) {
            if (value == null) {
                throw new NullPointerException();
            }

            message_ = value;
            onChanged();
            return this;
        }

        /**
         * <code>string message = 1;</code>
         */
        public Builder clearMessage() {

            message_ = getDefaultInstance().getMessage();
            onChanged();
            return this;
        }

        /**
         * <code>string message = 1;</code>
         */
        public Builder setMessageBytes(
                com.google.protobuf.ByteString value) {
            if (value == null) {
                throw new NullPointerException();
            }
            checkByteStringIsUtf8(value);

            message_ = value;
            onChanged();
            return this;
        }

        public final Builder setUnknownFields(
                final com.google.protobuf.UnknownFieldSet unknownFields) {
            return this;
        }

        public final Builder mergeUnknownFields(
                final com.google.protobuf.UnknownFieldSet unknownFields) {
            return this;
        }

        // @@protoc_insertion_point(builder_scope:HelloProto)
    }

    // @@protoc_insertion_point(class_scope:HelloProto)
    private static final HelloProto DEFAULT_INSTANCE;

    static {
        DEFAULT_INSTANCE = new HelloProto();
    }

    public static HelloProto getDefaultInstance() {
        return DEFAULT_INSTANCE;
    }

    private static final com.google.protobuf.Parser<HelloProto> PARSER = new com.google.protobuf.AbstractParser<HelloProto>() {
        public HelloProto parsePartialFrom(
                com.google.protobuf.CodedInputStream input,
                com.google.protobuf.ExtensionRegistryLite extensionRegistry)
                throws com.google.protobuf.InvalidProtocolBufferException {
            return new HelloProto(input, extensionRegistry);
        }
    };

    public static com.google.protobuf.Parser<HelloProto> parser() {
        return PARSER;
    }

    @java.lang.Override
    public com.google.protobuf.Parser<HelloProto> getParserForType() {
        return PARSER;
    }

    public HelloProto getDefaultInstanceForType() {
        return DEFAULT_INSTANCE;
    }

}
