package com.api.v1;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@io.grpc.stub.annotations.GrpcGenerated
public final class HealthcheckServiceGrpc {

  private HealthcheckServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "api.v1.HealthcheckService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.api.v1.PingRequest,
      com.api.v1.PingResponse> getPingMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Ping",
      requestType = com.api.v1.PingRequest.class,
      responseType = com.api.v1.PingResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.api.v1.PingRequest,
      com.api.v1.PingResponse> getPingMethod() {
    io.grpc.MethodDescriptor<com.api.v1.PingRequest, com.api.v1.PingResponse> getPingMethod;
    if ((getPingMethod = HealthcheckServiceGrpc.getPingMethod) == null) {
      synchronized (HealthcheckServiceGrpc.class) {
        if ((getPingMethod = HealthcheckServiceGrpc.getPingMethod) == null) {
          HealthcheckServiceGrpc.getPingMethod = getPingMethod =
              io.grpc.MethodDescriptor.<com.api.v1.PingRequest, com.api.v1.PingResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Ping"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.api.v1.PingRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.api.v1.PingResponse.getDefaultInstance()))
              .setSchemaDescriptor(new HealthcheckServiceMethodDescriptorSupplier("Ping"))
              .build();
        }
      }
    }
    return getPingMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static HealthcheckServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<HealthcheckServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<HealthcheckServiceStub>() {
        @java.lang.Override
        public HealthcheckServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new HealthcheckServiceStub(channel, callOptions);
        }
      };
    return HealthcheckServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports all types of calls on the service
   */
  public static HealthcheckServiceBlockingV2Stub newBlockingV2Stub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<HealthcheckServiceBlockingV2Stub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<HealthcheckServiceBlockingV2Stub>() {
        @java.lang.Override
        public HealthcheckServiceBlockingV2Stub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new HealthcheckServiceBlockingV2Stub(channel, callOptions);
        }
      };
    return HealthcheckServiceBlockingV2Stub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static HealthcheckServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<HealthcheckServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<HealthcheckServiceBlockingStub>() {
        @java.lang.Override
        public HealthcheckServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new HealthcheckServiceBlockingStub(channel, callOptions);
        }
      };
    return HealthcheckServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static HealthcheckServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<HealthcheckServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<HealthcheckServiceFutureStub>() {
        @java.lang.Override
        public HealthcheckServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new HealthcheckServiceFutureStub(channel, callOptions);
        }
      };
    return HealthcheckServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     * <pre>
     * Ping Jamm server to check connection.
     * </pre>
     */
    default void ping(com.api.v1.PingRequest request,
        io.grpc.stub.StreamObserver<com.api.v1.PingResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getPingMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service HealthcheckService.
   */
  public static abstract class HealthcheckServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return HealthcheckServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service HealthcheckService.
   */
  public static final class HealthcheckServiceStub
      extends io.grpc.stub.AbstractAsyncStub<HealthcheckServiceStub> {
    private HealthcheckServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected HealthcheckServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new HealthcheckServiceStub(channel, callOptions);
    }

    /**
     * <pre>
     * Ping Jamm server to check connection.
     * </pre>
     */
    public void ping(com.api.v1.PingRequest request,
        io.grpc.stub.StreamObserver<com.api.v1.PingResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getPingMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service HealthcheckService.
   */
  public static final class HealthcheckServiceBlockingV2Stub
      extends io.grpc.stub.AbstractBlockingStub<HealthcheckServiceBlockingV2Stub> {
    private HealthcheckServiceBlockingV2Stub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected HealthcheckServiceBlockingV2Stub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new HealthcheckServiceBlockingV2Stub(channel, callOptions);
    }

    /**
     * <pre>
     * Ping Jamm server to check connection.
     * </pre>
     */
    public com.api.v1.PingResponse ping(com.api.v1.PingRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getPingMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do limited synchronous rpc calls to service HealthcheckService.
   */
  public static final class HealthcheckServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<HealthcheckServiceBlockingStub> {
    private HealthcheckServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected HealthcheckServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new HealthcheckServiceBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * Ping Jamm server to check connection.
     * </pre>
     */
    public com.api.v1.PingResponse ping(com.api.v1.PingRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getPingMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service HealthcheckService.
   */
  public static final class HealthcheckServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<HealthcheckServiceFutureStub> {
    private HealthcheckServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected HealthcheckServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new HealthcheckServiceFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * Ping Jamm server to check connection.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.api.v1.PingResponse> ping(
        com.api.v1.PingRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getPingMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_PING = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_PING:
          serviceImpl.ping((com.api.v1.PingRequest) request,
              (io.grpc.stub.StreamObserver<com.api.v1.PingResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getPingMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.api.v1.PingRequest,
              com.api.v1.PingResponse>(
                service, METHODID_PING)))
        .build();
  }

  private static abstract class HealthcheckServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    HealthcheckServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.api.v1.HealthcheckProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("HealthcheckService");
    }
  }

  private static final class HealthcheckServiceFileDescriptorSupplier
      extends HealthcheckServiceBaseDescriptorSupplier {
    HealthcheckServiceFileDescriptorSupplier() {}
  }

  private static final class HealthcheckServiceMethodDescriptorSupplier
      extends HealthcheckServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    HealthcheckServiceMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (HealthcheckServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new HealthcheckServiceFileDescriptorSupplier())
              .addMethod(getPingMethod())
              .build();
        }
      }
    }
    return result;
  }
}
