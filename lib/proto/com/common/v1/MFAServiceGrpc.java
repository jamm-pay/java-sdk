package com.common.v1;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@io.grpc.stub.annotations.GrpcGenerated
public final class MFAServiceGrpc {

  private MFAServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "common.v1.MFAService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.common.v1.InitiateSmsVerificationRequest,
      com.common.v1.InitiateSmsVerificationResponse> getInitiateSmsVerificationMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "InitiateSmsVerification",
      requestType = com.common.v1.InitiateSmsVerificationRequest.class,
      responseType = com.common.v1.InitiateSmsVerificationResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.common.v1.InitiateSmsVerificationRequest,
      com.common.v1.InitiateSmsVerificationResponse> getInitiateSmsVerificationMethod() {
    io.grpc.MethodDescriptor<com.common.v1.InitiateSmsVerificationRequest, com.common.v1.InitiateSmsVerificationResponse> getInitiateSmsVerificationMethod;
    if ((getInitiateSmsVerificationMethod = MFAServiceGrpc.getInitiateSmsVerificationMethod) == null) {
      synchronized (MFAServiceGrpc.class) {
        if ((getInitiateSmsVerificationMethod = MFAServiceGrpc.getInitiateSmsVerificationMethod) == null) {
          MFAServiceGrpc.getInitiateSmsVerificationMethod = getInitiateSmsVerificationMethod =
              io.grpc.MethodDescriptor.<com.common.v1.InitiateSmsVerificationRequest, com.common.v1.InitiateSmsVerificationResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "InitiateSmsVerification"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.common.v1.InitiateSmsVerificationRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.common.v1.InitiateSmsVerificationResponse.getDefaultInstance()))
              .setSchemaDescriptor(new MFAServiceMethodDescriptorSupplier("InitiateSmsVerification"))
              .build();
        }
      }
    }
    return getInitiateSmsVerificationMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.common.v1.ResendSmsVerificationRequest,
      com.common.v1.ResendSmsVerificationResponse> getResendSmsVerificationMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ResendSmsVerification",
      requestType = com.common.v1.ResendSmsVerificationRequest.class,
      responseType = com.common.v1.ResendSmsVerificationResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.common.v1.ResendSmsVerificationRequest,
      com.common.v1.ResendSmsVerificationResponse> getResendSmsVerificationMethod() {
    io.grpc.MethodDescriptor<com.common.v1.ResendSmsVerificationRequest, com.common.v1.ResendSmsVerificationResponse> getResendSmsVerificationMethod;
    if ((getResendSmsVerificationMethod = MFAServiceGrpc.getResendSmsVerificationMethod) == null) {
      synchronized (MFAServiceGrpc.class) {
        if ((getResendSmsVerificationMethod = MFAServiceGrpc.getResendSmsVerificationMethod) == null) {
          MFAServiceGrpc.getResendSmsVerificationMethod = getResendSmsVerificationMethod =
              io.grpc.MethodDescriptor.<com.common.v1.ResendSmsVerificationRequest, com.common.v1.ResendSmsVerificationResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ResendSmsVerification"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.common.v1.ResendSmsVerificationRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.common.v1.ResendSmsVerificationResponse.getDefaultInstance()))
              .setSchemaDescriptor(new MFAServiceMethodDescriptorSupplier("ResendSmsVerification"))
              .build();
        }
      }
    }
    return getResendSmsVerificationMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.common.v1.VerifySmsOtpRequest,
      com.common.v1.VerifySmsOtpResponse> getVerifySmsOtpMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "VerifySmsOtp",
      requestType = com.common.v1.VerifySmsOtpRequest.class,
      responseType = com.common.v1.VerifySmsOtpResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.common.v1.VerifySmsOtpRequest,
      com.common.v1.VerifySmsOtpResponse> getVerifySmsOtpMethod() {
    io.grpc.MethodDescriptor<com.common.v1.VerifySmsOtpRequest, com.common.v1.VerifySmsOtpResponse> getVerifySmsOtpMethod;
    if ((getVerifySmsOtpMethod = MFAServiceGrpc.getVerifySmsOtpMethod) == null) {
      synchronized (MFAServiceGrpc.class) {
        if ((getVerifySmsOtpMethod = MFAServiceGrpc.getVerifySmsOtpMethod) == null) {
          MFAServiceGrpc.getVerifySmsOtpMethod = getVerifySmsOtpMethod =
              io.grpc.MethodDescriptor.<com.common.v1.VerifySmsOtpRequest, com.common.v1.VerifySmsOtpResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "VerifySmsOtp"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.common.v1.VerifySmsOtpRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.common.v1.VerifySmsOtpResponse.getDefaultInstance()))
              .setSchemaDescriptor(new MFAServiceMethodDescriptorSupplier("VerifySmsOtp"))
              .build();
        }
      }
    }
    return getVerifySmsOtpMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static MFAServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<MFAServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<MFAServiceStub>() {
        @java.lang.Override
        public MFAServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new MFAServiceStub(channel, callOptions);
        }
      };
    return MFAServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports all types of calls on the service
   */
  public static MFAServiceBlockingV2Stub newBlockingV2Stub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<MFAServiceBlockingV2Stub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<MFAServiceBlockingV2Stub>() {
        @java.lang.Override
        public MFAServiceBlockingV2Stub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new MFAServiceBlockingV2Stub(channel, callOptions);
        }
      };
    return MFAServiceBlockingV2Stub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static MFAServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<MFAServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<MFAServiceBlockingStub>() {
        @java.lang.Override
        public MFAServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new MFAServiceBlockingStub(channel, callOptions);
        }
      };
    return MFAServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static MFAServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<MFAServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<MFAServiceFutureStub>() {
        @java.lang.Override
        public MFAServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new MFAServiceFutureStub(channel, callOptions);
        }
      };
    return MFAServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     * <pre>
     * Send an OTP to session
     * </pre>
     */
    default void initiateSmsVerification(com.common.v1.InitiateSmsVerificationRequest request,
        io.grpc.stub.StreamObserver<com.common.v1.InitiateSmsVerificationResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getInitiateSmsVerificationMethod(), responseObserver);
    }

    /**
     * <pre>
     * Resend OTP to session
     * </pre>
     */
    default void resendSmsVerification(com.common.v1.ResendSmsVerificationRequest request,
        io.grpc.stub.StreamObserver<com.common.v1.ResendSmsVerificationResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getResendSmsVerificationMethod(), responseObserver);
    }

    /**
     * <pre>
     * Verify OTP against session
     * </pre>
     */
    default void verifySmsOtp(com.common.v1.VerifySmsOtpRequest request,
        io.grpc.stub.StreamObserver<com.common.v1.VerifySmsOtpResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getVerifySmsOtpMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service MFAService.
   */
  public static abstract class MFAServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return MFAServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service MFAService.
   */
  public static final class MFAServiceStub
      extends io.grpc.stub.AbstractAsyncStub<MFAServiceStub> {
    private MFAServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MFAServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new MFAServiceStub(channel, callOptions);
    }

    /**
     * <pre>
     * Send an OTP to session
     * </pre>
     */
    public void initiateSmsVerification(com.common.v1.InitiateSmsVerificationRequest request,
        io.grpc.stub.StreamObserver<com.common.v1.InitiateSmsVerificationResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getInitiateSmsVerificationMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Resend OTP to session
     * </pre>
     */
    public void resendSmsVerification(com.common.v1.ResendSmsVerificationRequest request,
        io.grpc.stub.StreamObserver<com.common.v1.ResendSmsVerificationResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getResendSmsVerificationMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Verify OTP against session
     * </pre>
     */
    public void verifySmsOtp(com.common.v1.VerifySmsOtpRequest request,
        io.grpc.stub.StreamObserver<com.common.v1.VerifySmsOtpResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getVerifySmsOtpMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service MFAService.
   */
  public static final class MFAServiceBlockingV2Stub
      extends io.grpc.stub.AbstractBlockingStub<MFAServiceBlockingV2Stub> {
    private MFAServiceBlockingV2Stub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MFAServiceBlockingV2Stub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new MFAServiceBlockingV2Stub(channel, callOptions);
    }

    /**
     * <pre>
     * Send an OTP to session
     * </pre>
     */
    public com.common.v1.InitiateSmsVerificationResponse initiateSmsVerification(com.common.v1.InitiateSmsVerificationRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getInitiateSmsVerificationMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Resend OTP to session
     * </pre>
     */
    public com.common.v1.ResendSmsVerificationResponse resendSmsVerification(com.common.v1.ResendSmsVerificationRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getResendSmsVerificationMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Verify OTP against session
     * </pre>
     */
    public com.common.v1.VerifySmsOtpResponse verifySmsOtp(com.common.v1.VerifySmsOtpRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getVerifySmsOtpMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do limited synchronous rpc calls to service MFAService.
   */
  public static final class MFAServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<MFAServiceBlockingStub> {
    private MFAServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MFAServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new MFAServiceBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * Send an OTP to session
     * </pre>
     */
    public com.common.v1.InitiateSmsVerificationResponse initiateSmsVerification(com.common.v1.InitiateSmsVerificationRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getInitiateSmsVerificationMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Resend OTP to session
     * </pre>
     */
    public com.common.v1.ResendSmsVerificationResponse resendSmsVerification(com.common.v1.ResendSmsVerificationRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getResendSmsVerificationMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Verify OTP against session
     * </pre>
     */
    public com.common.v1.VerifySmsOtpResponse verifySmsOtp(com.common.v1.VerifySmsOtpRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getVerifySmsOtpMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service MFAService.
   */
  public static final class MFAServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<MFAServiceFutureStub> {
    private MFAServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MFAServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new MFAServiceFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * Send an OTP to session
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.common.v1.InitiateSmsVerificationResponse> initiateSmsVerification(
        com.common.v1.InitiateSmsVerificationRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getInitiateSmsVerificationMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Resend OTP to session
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.common.v1.ResendSmsVerificationResponse> resendSmsVerification(
        com.common.v1.ResendSmsVerificationRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getResendSmsVerificationMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Verify OTP against session
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.common.v1.VerifySmsOtpResponse> verifySmsOtp(
        com.common.v1.VerifySmsOtpRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getVerifySmsOtpMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_INITIATE_SMS_VERIFICATION = 0;
  private static final int METHODID_RESEND_SMS_VERIFICATION = 1;
  private static final int METHODID_VERIFY_SMS_OTP = 2;

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
        case METHODID_INITIATE_SMS_VERIFICATION:
          serviceImpl.initiateSmsVerification((com.common.v1.InitiateSmsVerificationRequest) request,
              (io.grpc.stub.StreamObserver<com.common.v1.InitiateSmsVerificationResponse>) responseObserver);
          break;
        case METHODID_RESEND_SMS_VERIFICATION:
          serviceImpl.resendSmsVerification((com.common.v1.ResendSmsVerificationRequest) request,
              (io.grpc.stub.StreamObserver<com.common.v1.ResendSmsVerificationResponse>) responseObserver);
          break;
        case METHODID_VERIFY_SMS_OTP:
          serviceImpl.verifySmsOtp((com.common.v1.VerifySmsOtpRequest) request,
              (io.grpc.stub.StreamObserver<com.common.v1.VerifySmsOtpResponse>) responseObserver);
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
          getInitiateSmsVerificationMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.common.v1.InitiateSmsVerificationRequest,
              com.common.v1.InitiateSmsVerificationResponse>(
                service, METHODID_INITIATE_SMS_VERIFICATION)))
        .addMethod(
          getResendSmsVerificationMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.common.v1.ResendSmsVerificationRequest,
              com.common.v1.ResendSmsVerificationResponse>(
                service, METHODID_RESEND_SMS_VERIFICATION)))
        .addMethod(
          getVerifySmsOtpMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.common.v1.VerifySmsOtpRequest,
              com.common.v1.VerifySmsOtpResponse>(
                service, METHODID_VERIFY_SMS_OTP)))
        .build();
  }

  private static abstract class MFAServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    MFAServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.common.v1.MfaProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("MFAService");
    }
  }

  private static final class MFAServiceFileDescriptorSupplier
      extends MFAServiceBaseDescriptorSupplier {
    MFAServiceFileDescriptorSupplier() {}
  }

  private static final class MFAServiceMethodDescriptorSupplier
      extends MFAServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    MFAServiceMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (MFAServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new MFAServiceFileDescriptorSupplier())
              .addMethod(getInitiateSmsVerificationMethod())
              .addMethod(getResendSmsVerificationMethod())
              .addMethod(getVerifySmsOtpMethod())
              .build();
        }
      }
    }
    return result;
  }
}
