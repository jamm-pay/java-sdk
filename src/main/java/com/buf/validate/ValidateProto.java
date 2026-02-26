// Stub implementation for buf.validate proto extensions
// Delegates to build.buf.validate.ValidateProto from the protovalidate library
// which provides the actual proto descriptor and extensions.
//
// The generated proto files reference com.buf.validate.ValidateProto,
// but the protovalidate library ships the class under the build.buf.validate package.
// This bridge class resolves the package mismatch.

package com.buf.validate;

import com.google.protobuf.Descriptors;
import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.GeneratedMessage;

public final class ValidateProto {
    private ValidateProto() {}

    public static Descriptors.FileDescriptor getDescriptor() {
        return build.buf.validate.ValidateProto.getDescriptor();
    }

    public static final GeneratedMessage.GeneratedExtension<
            com.google.protobuf.DescriptorProtos.FieldOptions,
            build.buf.validate.FieldRules> field =
            build.buf.validate.ValidateProto.field;

    public static final GeneratedMessage.GeneratedExtension<
            com.google.protobuf.DescriptorProtos.MessageOptions,
            build.buf.validate.MessageRules> message =
            build.buf.validate.ValidateProto.message;

    public static final GeneratedMessage.GeneratedExtension<
            com.google.protobuf.DescriptorProtos.OneofOptions,
            build.buf.validate.OneofRules> oneof =
            build.buf.validate.ValidateProto.oneof;

    public static void registerAllExtensions(ExtensionRegistry registry) {
        build.buf.validate.ValidateProto.registerAllExtensions(registry);
    }
}
