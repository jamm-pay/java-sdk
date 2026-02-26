package com.api.v1;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 * <pre>
 * WebhookService is a placeholder service just to let OpenAPI codegen.
 * Jamm's current OpenAPI generator only generates code that is associated
 * with services.
 * </pre>
 */
@io.grpc.stub.annotations.GrpcGenerated
public final class WebhookServiceGrpc {

  private WebhookServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "api.v1.WebhookService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.api.v1.ErrorRequest,
      com.api.v1.ErrorResponse> getErrorMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Error",
      requestType = com.api.v1.ErrorRequest.class,
      responseType = com.api.v1.ErrorResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.api.v1.ErrorRequest,
      com.api.v1.ErrorResponse> getErrorMethod() {
    io.grpc.MethodDescriptor<com.api.v1.ErrorRequest, com.api.v1.ErrorResponse> getErrorMethod;
    if ((getErrorMethod = WebhookServiceGrpc.getErrorMethod) == null) {
      synchronized (WebhookServiceGrpc.class) {
        if ((getErrorMethod = WebhookServiceGrpc.getErrorMethod) == null) {
          WebhookServiceGrpc.getErrorMethod = getErrorMethod =
              io.grpc.MethodDescriptor.<com.api.v1.ErrorRequest, com.api.v1.ErrorResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Error"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.api.v1.ErrorRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.api.v1.ErrorResponse.getDefaultInstance()))
              .setSchemaDescriptor(new WebhookServiceMethodDescriptorSupplier("Error"))
              .build();
        }
      }
    }
    return getErrorMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.api.v1.MessageRequest,
      com.api.v1.MessageResponse> getMessageMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Message",
      requestType = com.api.v1.MessageRequest.class,
      responseType = com.api.v1.MessageResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.api.v1.MessageRequest,
      com.api.v1.MessageResponse> getMessageMethod() {
    io.grpc.MethodDescriptor<com.api.v1.MessageRequest, com.api.v1.MessageResponse> getMessageMethod;
    if ((getMessageMethod = WebhookServiceGrpc.getMessageMethod) == null) {
      synchronized (WebhookServiceGrpc.class) {
        if ((getMessageMethod = WebhookServiceGrpc.getMessageMethod) == null) {
          WebhookServiceGrpc.getMessageMethod = getMessageMethod =
              io.grpc.MethodDescriptor.<com.api.v1.MessageRequest, com.api.v1.MessageResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Message"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.api.v1.MessageRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.api.v1.MessageResponse.getDefaultInstance()))
              .setSchemaDescriptor(new WebhookServiceMethodDescriptorSupplier("Message"))
              .build();
        }
      }
    }
    return getMessageMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static WebhookServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<WebhookServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<WebhookServiceStub>() {
        @java.lang.Override
        public WebhookServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new WebhookServiceStub(channel, callOptions);
        }
      };
    return WebhookServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports all types of calls on the service
   */
  public static WebhookServiceBlockingV2Stub newBlockingV2Stub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<WebhookServiceBlockingV2Stub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<WebhookServiceBlockingV2Stub>() {
        @java.lang.Override
        public WebhookServiceBlockingV2Stub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new WebhookServiceBlockingV2Stub(channel, callOptions);
        }
      };
    return WebhookServiceBlockingV2Stub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static WebhookServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<WebhookServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<WebhookServiceBlockingStub>() {
        @java.lang.Override
        public WebhookServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new WebhookServiceBlockingStub(channel, callOptions);
        }
      };
    return WebhookServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static WebhookServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<WebhookServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<WebhookServiceFutureStub>() {
        @java.lang.Override
        public WebhookServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new WebhookServiceFutureStub(channel, callOptions);
        }
      };
    return WebhookServiceFutureStub.newStub(factory, channel);
  }

  /**
   * <pre>
   * WebhookService is a placeholder service just to let OpenAPI codegen.
   * Jamm's current OpenAPI generator only generates code that is associated
   * with services.
   * </pre>
   */
  public interface AsyncService {

    /**
     * <pre>
     * Error placeholder.
     * This method is not implemented on Jamm backend.
     * </pre>
     */
    default void error(com.api.v1.ErrorRequest request,
        io.grpc.stub.StreamObserver<com.api.v1.ErrorResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getErrorMethod(), responseObserver);
    }

    /**
     * <pre>
     * Message placeholder.
     * This method is not implemented on Jamm backend.
     * </pre>
     */
    default void message(com.api.v1.MessageRequest request,
        io.grpc.stub.StreamObserver<com.api.v1.MessageResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getMessageMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service WebhookService.
   * <pre>
   * WebhookService is a placeholder service just to let OpenAPI codegen.
   * Jamm's current OpenAPI generator only generates code that is associated
   * with services.
   * </pre>
   */
  public static abstract class WebhookServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return WebhookServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service WebhookService.
   * <pre>
   * WebhookService is a placeholder service just to let OpenAPI codegen.
   * Jamm's current OpenAPI generator only generates code that is associated
   * with services.
   * </pre>
   */
  public static final class WebhookServiceStub
      extends io.grpc.stub.AbstractAsyncStub<WebhookServiceStub> {
    private WebhookServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected WebhookServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new WebhookServiceStub(channel, callOptions);
    }

    /**
     * <pre>
     * Error placeholder.
     * This method is not implemented on Jamm backend.
     * </pre>
     */
    public void error(com.api.v1.ErrorRequest request,
        io.grpc.stub.StreamObserver<com.api.v1.ErrorResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getErrorMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Message placeholder.
     * This method is not implemented on Jamm backend.
     * </pre>
     */
    public void message(com.api.v1.MessageRequest request,
        io.grpc.stub.StreamObserver<com.api.v1.MessageResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getMessageMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service WebhookService.
   * <pre>
   * WebhookService is a placeholder service just to let OpenAPI codegen.
   * Jamm's current OpenAPI generator only generates code that is associated
   * with services.
   * </pre>
   */
  public static final class WebhookServiceBlockingV2Stub
      extends io.grpc.stub.AbstractBlockingStub<WebhookServiceBlockingV2Stub> {
    private WebhookServiceBlockingV2Stub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected WebhookServiceBlockingV2Stub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new WebhookServiceBlockingV2Stub(channel, callOptions);
    }

    /**
     * <pre>
     * Error placeholder.
     * This method is not implemented on Jamm backend.
     * </pre>
     */
    public com.api.v1.ErrorResponse error(com.api.v1.ErrorRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getErrorMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Message placeholder.
     * This method is not implemented on Jamm backend.
     * </pre>
     */
    public com.api.v1.MessageResponse message(com.api.v1.MessageRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getMessageMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do limited synchronous rpc calls to service WebhookService.
   * <pre>
   * WebhookService is a placeholder service just to let OpenAPI codegen.
   * Jamm's current OpenAPI generator only generates code that is associated
   * with services.
   * </pre>
   */
  public static final class WebhookServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<WebhookServiceBlockingStub> {
    private WebhookServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected WebhookServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new WebhookServiceBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * Error placeholder.
     * This method is not implemented on Jamm backend.
     * </pre>
     */
    public com.api.v1.ErrorResponse error(com.api.v1.ErrorRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getErrorMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Message placeholder.
     * This method is not implemented on Jamm backend.
     * </pre>
     */
    public com.api.v1.MessageResponse message(com.api.v1.MessageRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getMessageMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service WebhookService.
   * <pre>
   * WebhookService is a placeholder service just to let OpenAPI codegen.
   * Jamm's current OpenAPI generator only generates code that is associated
   * with services.
   * </pre>
   */
  public static final class WebhookServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<WebhookServiceFutureStub> {
    private WebhookServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected WebhookServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new WebhookServiceFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * Error placeholder.
     * This method is not implemented on Jamm backend.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.api.v1.ErrorResponse> error(
        com.api.v1.ErrorRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getErrorMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Message placeholder.
     * This method is not implemented on Jamm backend.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.api.v1.MessageResponse> message(
        com.api.v1.MessageRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getMessageMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_ERROR = 0;
  private static final int METHODID_MESSAGE = 1;

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
        case METHODID_ERROR:
          serviceImpl.error((com.api.v1.ErrorRequest) request,
              (io.grpc.stub.StreamObserver<com.api.v1.ErrorResponse>) responseObserver);
          break;
        case METHODID_MESSAGE:
          serviceImpl.message((com.api.v1.MessageRequest) request,
              (io.grpc.stub.StreamObserver<com.api.v1.MessageResponse>) responseObserver);
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
          getErrorMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.api.v1.ErrorRequest,
              com.api.v1.ErrorResponse>(
                service, METHODID_ERROR)))
        .addMethod(
          getMessageMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.api.v1.MessageRequest,
              com.api.v1.MessageResponse>(
                service, METHODID_MESSAGE)))
        .build();
  }

  private static abstract class WebhookServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    WebhookServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.api.v1.MerchantWebhooksProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("WebhookService");
    }
  }

  private static final class WebhookServiceFileDescriptorSupplier
      extends WebhookServiceBaseDescriptorSupplier {
    WebhookServiceFileDescriptorSupplier() {}
  }

  private static final class WebhookServiceMethodDescriptorSupplier
      extends WebhookServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    WebhookServiceMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (WebhookServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new WebhookServiceFileDescriptorSupplier())
              .addMethod(getErrorMethod())
              .addMethod(getMessageMethod())
              .build();
        }
      }
    }
    return result;
  }
}
