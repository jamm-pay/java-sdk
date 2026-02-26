// Stub for grpc-gateway openapiv2 annotations.
//
// The generated proto files reference com.grpc.gateway.protoc_gen_openapiv2.options.AnnotationsProto,
// but the grpc-ecosystem-protoc-gen-openapiv2 library ships the class under the
// grpc.gateway.protoc_gen_openapiv2.options package (compiled with Java 21).
// Since this project targets Java 11, we cannot delegate to that JAR.
//
// This stub builds a minimal proto descriptor that defines the openapiv2_swagger
// and openapiv2_operation extensions, so that generated proto initializers calling
// registry.add(...) do not throw NullPointerException.

package com.grpc.gateway.protoc_gen_openapiv2.options;

import com.google.protobuf.Any;
import com.google.protobuf.AnyProto;
import com.google.protobuf.DescriptorProtos;
import com.google.protobuf.Descriptors;
import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.ExtensionRegistryLite;
import com.google.protobuf.GeneratedMessage;

public final class AnnotationsProto {
    private AnnotationsProto() {}

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static final GeneratedMessage.GeneratedExtension openapiv2Swagger =
            GeneratedMessage.newFileScopedGeneratedExtension(Any.class, Any.getDefaultInstance());

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static final GeneratedMessage.GeneratedExtension openapiv2Operation =
            GeneratedMessage.newFileScopedGeneratedExtension(Any.class, Any.getDefaultInstance());

    private static final Descriptors.FileDescriptor descriptor;

    static {
        try {
            DescriptorProtos.FileDescriptorProto fileProto = DescriptorProtos.FileDescriptorProto.newBuilder()
                .setName("protoc-gen-openapiv2/options/annotations.proto")
                .setPackage("grpc.gateway.protoc_gen_openapiv2.options")
                .setSyntax("proto3")
                .addDependency("google/protobuf/descriptor.proto")
                .addDependency("google/protobuf/any.proto")
                .addExtension(DescriptorProtos.FieldDescriptorProto.newBuilder()
                    .setName("openapiv2_swagger")
                    .setNumber(1042)
                    .setType(DescriptorProtos.FieldDescriptorProto.Type.TYPE_MESSAGE)
                    .setTypeName(".google.protobuf.Any")
                    .setExtendee(".google.protobuf.FileOptions")
                    .setLabel(DescriptorProtos.FieldDescriptorProto.Label.LABEL_OPTIONAL)
                    .build())
                .addExtension(DescriptorProtos.FieldDescriptorProto.newBuilder()
                    .setName("openapiv2_operation")
                    .setNumber(1042)
                    .setType(DescriptorProtos.FieldDescriptorProto.Type.TYPE_MESSAGE)
                    .setTypeName(".google.protobuf.Any")
                    .setExtendee(".google.protobuf.MethodOptions")
                    .setLabel(DescriptorProtos.FieldDescriptorProto.Label.LABEL_OPTIONAL)
                    .build())
                .build();

            descriptor = Descriptors.FileDescriptor.buildFrom(
                fileProto,
                new Descriptors.FileDescriptor[] {
                    DescriptorProtos.getDescriptor(),
                    AnyProto.getDescriptor()
                });

            openapiv2Swagger.internalInit(descriptor.getExtensions().get(0));
            openapiv2Operation.internalInit(descriptor.getExtensions().get(1));
        } catch (Descriptors.DescriptorValidationException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static Descriptors.FileDescriptor getDescriptor() {
        return descriptor;
    }

    @SuppressWarnings("unchecked")
    public static void registerAllExtensions(ExtensionRegistryLite registry) {
        if (registry instanceof ExtensionRegistry) {
            ExtensionRegistry fullRegistry = (ExtensionRegistry) registry;
            fullRegistry.add(openapiv2Swagger);
            fullRegistry.add(openapiv2Operation);
        }
    }

    public static void registerAllExtensions(ExtensionRegistry registry) {
        registerAllExtensions((ExtensionRegistryLite) registry);
    }
}
