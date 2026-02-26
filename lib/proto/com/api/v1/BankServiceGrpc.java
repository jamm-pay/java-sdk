package com.api.v1;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@io.grpc.stub.annotations.GrpcGenerated
public final class BankServiceGrpc {

  private BankServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "api.v1.BankService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.api.v1.GetBankRequest,
      com.api.v1.GetBankResponse> getGetBankMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetBank",
      requestType = com.api.v1.GetBankRequest.class,
      responseType = com.api.v1.GetBankResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.api.v1.GetBankRequest,
      com.api.v1.GetBankResponse> getGetBankMethod() {
    io.grpc.MethodDescriptor<com.api.v1.GetBankRequest, com.api.v1.GetBankResponse> getGetBankMethod;
    if ((getGetBankMethod = BankServiceGrpc.getGetBankMethod) == null) {
      synchronized (BankServiceGrpc.class) {
        if ((getGetBankMethod = BankServiceGrpc.getGetBankMethod) == null) {
          BankServiceGrpc.getGetBankMethod = getGetBankMethod =
              io.grpc.MethodDescriptor.<com.api.v1.GetBankRequest, com.api.v1.GetBankResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetBank"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.api.v1.GetBankRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.api.v1.GetBankResponse.getDefaultInstance()))
              .setSchemaDescriptor(new BankServiceMethodDescriptorSupplier("GetBank"))
              .build();
        }
      }
    }
    return getGetBankMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.api.v1.SearchBanksRequest,
      com.api.v1.SearchBanksResponse> getSearchBanksMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SearchBanks",
      requestType = com.api.v1.SearchBanksRequest.class,
      responseType = com.api.v1.SearchBanksResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.api.v1.SearchBanksRequest,
      com.api.v1.SearchBanksResponse> getSearchBanksMethod() {
    io.grpc.MethodDescriptor<com.api.v1.SearchBanksRequest, com.api.v1.SearchBanksResponse> getSearchBanksMethod;
    if ((getSearchBanksMethod = BankServiceGrpc.getSearchBanksMethod) == null) {
      synchronized (BankServiceGrpc.class) {
        if ((getSearchBanksMethod = BankServiceGrpc.getSearchBanksMethod) == null) {
          BankServiceGrpc.getSearchBanksMethod = getSearchBanksMethod =
              io.grpc.MethodDescriptor.<com.api.v1.SearchBanksRequest, com.api.v1.SearchBanksResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SearchBanks"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.api.v1.SearchBanksRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.api.v1.SearchBanksResponse.getDefaultInstance()))
              .setSchemaDescriptor(new BankServiceMethodDescriptorSupplier("SearchBanks"))
              .build();
        }
      }
    }
    return getSearchBanksMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.api.v1.GetBranchRequest,
      com.api.v1.GetBranchResponse> getGetBranchMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetBranch",
      requestType = com.api.v1.GetBranchRequest.class,
      responseType = com.api.v1.GetBranchResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.api.v1.GetBranchRequest,
      com.api.v1.GetBranchResponse> getGetBranchMethod() {
    io.grpc.MethodDescriptor<com.api.v1.GetBranchRequest, com.api.v1.GetBranchResponse> getGetBranchMethod;
    if ((getGetBranchMethod = BankServiceGrpc.getGetBranchMethod) == null) {
      synchronized (BankServiceGrpc.class) {
        if ((getGetBranchMethod = BankServiceGrpc.getGetBranchMethod) == null) {
          BankServiceGrpc.getGetBranchMethod = getGetBranchMethod =
              io.grpc.MethodDescriptor.<com.api.v1.GetBranchRequest, com.api.v1.GetBranchResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetBranch"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.api.v1.GetBranchRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.api.v1.GetBranchResponse.getDefaultInstance()))
              .setSchemaDescriptor(new BankServiceMethodDescriptorSupplier("GetBranch"))
              .build();
        }
      }
    }
    return getGetBranchMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.api.v1.GetBranchesRequest,
      com.api.v1.GetBranchesResponse> getGetBranchesMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetBranches",
      requestType = com.api.v1.GetBranchesRequest.class,
      responseType = com.api.v1.GetBranchesResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.api.v1.GetBranchesRequest,
      com.api.v1.GetBranchesResponse> getGetBranchesMethod() {
    io.grpc.MethodDescriptor<com.api.v1.GetBranchesRequest, com.api.v1.GetBranchesResponse> getGetBranchesMethod;
    if ((getGetBranchesMethod = BankServiceGrpc.getGetBranchesMethod) == null) {
      synchronized (BankServiceGrpc.class) {
        if ((getGetBranchesMethod = BankServiceGrpc.getGetBranchesMethod) == null) {
          BankServiceGrpc.getGetBranchesMethod = getGetBranchesMethod =
              io.grpc.MethodDescriptor.<com.api.v1.GetBranchesRequest, com.api.v1.GetBranchesResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetBranches"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.api.v1.GetBranchesRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.api.v1.GetBranchesResponse.getDefaultInstance()))
              .setSchemaDescriptor(new BankServiceMethodDescriptorSupplier("GetBranches"))
              .build();
        }
      }
    }
    return getGetBranchesMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.api.v1.SearchBranchesRequest,
      com.api.v1.SearchBranchesResponse> getSearchBranchesMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SearchBranches",
      requestType = com.api.v1.SearchBranchesRequest.class,
      responseType = com.api.v1.SearchBranchesResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.api.v1.SearchBranchesRequest,
      com.api.v1.SearchBranchesResponse> getSearchBranchesMethod() {
    io.grpc.MethodDescriptor<com.api.v1.SearchBranchesRequest, com.api.v1.SearchBranchesResponse> getSearchBranchesMethod;
    if ((getSearchBranchesMethod = BankServiceGrpc.getSearchBranchesMethod) == null) {
      synchronized (BankServiceGrpc.class) {
        if ((getSearchBranchesMethod = BankServiceGrpc.getSearchBranchesMethod) == null) {
          BankServiceGrpc.getSearchBranchesMethod = getSearchBranchesMethod =
              io.grpc.MethodDescriptor.<com.api.v1.SearchBranchesRequest, com.api.v1.SearchBranchesResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SearchBranches"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.api.v1.SearchBranchesRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.api.v1.SearchBranchesResponse.getDefaultInstance()))
              .setSchemaDescriptor(new BankServiceMethodDescriptorSupplier("SearchBranches"))
              .build();
        }
      }
    }
    return getSearchBranchesMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static BankServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<BankServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<BankServiceStub>() {
        @java.lang.Override
        public BankServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new BankServiceStub(channel, callOptions);
        }
      };
    return BankServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports all types of calls on the service
   */
  public static BankServiceBlockingV2Stub newBlockingV2Stub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<BankServiceBlockingV2Stub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<BankServiceBlockingV2Stub>() {
        @java.lang.Override
        public BankServiceBlockingV2Stub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new BankServiceBlockingV2Stub(channel, callOptions);
        }
      };
    return BankServiceBlockingV2Stub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static BankServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<BankServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<BankServiceBlockingStub>() {
        @java.lang.Override
        public BankServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new BankServiceBlockingStub(channel, callOptions);
        }
      };
    return BankServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static BankServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<BankServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<BankServiceFutureStub>() {
        @java.lang.Override
        public BankServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new BankServiceFutureStub(channel, callOptions);
        }
      };
    return BankServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     * <pre>
     * GetBank returns a bank by the bank code.
     * </pre>
     */
    default void getBank(com.api.v1.GetBankRequest request,
        io.grpc.stub.StreamObserver<com.api.v1.GetBankResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetBankMethod(), responseObserver);
    }

    /**
     * <pre>
     * SearchBanks returns a list of banks that match the query.
     * </pre>
     */
    default void searchBanks(com.api.v1.SearchBanksRequest request,
        io.grpc.stub.StreamObserver<com.api.v1.SearchBanksResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSearchBanksMethod(), responseObserver);
    }

    /**
     * <pre>
     * GetBranch returns a branch by the bank code and branch code.
     * </pre>
     */
    default void getBranch(com.api.v1.GetBranchRequest request,
        io.grpc.stub.StreamObserver<com.api.v1.GetBranchResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetBranchMethod(), responseObserver);
    }

    /**
     * <pre>
     * GetBranches returns a list of branches by the bank code.
     * </pre>
     */
    default void getBranches(com.api.v1.GetBranchesRequest request,
        io.grpc.stub.StreamObserver<com.api.v1.GetBranchesResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetBranchesMethod(), responseObserver);
    }

    /**
     * <pre>
     * SearchBranches returns a list of branches that match the query.
     * </pre>
     */
    default void searchBranches(com.api.v1.SearchBranchesRequest request,
        io.grpc.stub.StreamObserver<com.api.v1.SearchBranchesResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSearchBranchesMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service BankService.
   */
  public static abstract class BankServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return BankServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service BankService.
   */
  public static final class BankServiceStub
      extends io.grpc.stub.AbstractAsyncStub<BankServiceStub> {
    private BankServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected BankServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new BankServiceStub(channel, callOptions);
    }

    /**
     * <pre>
     * GetBank returns a bank by the bank code.
     * </pre>
     */
    public void getBank(com.api.v1.GetBankRequest request,
        io.grpc.stub.StreamObserver<com.api.v1.GetBankResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetBankMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * SearchBanks returns a list of banks that match the query.
     * </pre>
     */
    public void searchBanks(com.api.v1.SearchBanksRequest request,
        io.grpc.stub.StreamObserver<com.api.v1.SearchBanksResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSearchBanksMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * GetBranch returns a branch by the bank code and branch code.
     * </pre>
     */
    public void getBranch(com.api.v1.GetBranchRequest request,
        io.grpc.stub.StreamObserver<com.api.v1.GetBranchResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetBranchMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * GetBranches returns a list of branches by the bank code.
     * </pre>
     */
    public void getBranches(com.api.v1.GetBranchesRequest request,
        io.grpc.stub.StreamObserver<com.api.v1.GetBranchesResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetBranchesMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * SearchBranches returns a list of branches that match the query.
     * </pre>
     */
    public void searchBranches(com.api.v1.SearchBranchesRequest request,
        io.grpc.stub.StreamObserver<com.api.v1.SearchBranchesResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSearchBranchesMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service BankService.
   */
  public static final class BankServiceBlockingV2Stub
      extends io.grpc.stub.AbstractBlockingStub<BankServiceBlockingV2Stub> {
    private BankServiceBlockingV2Stub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected BankServiceBlockingV2Stub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new BankServiceBlockingV2Stub(channel, callOptions);
    }

    /**
     * <pre>
     * GetBank returns a bank by the bank code.
     * </pre>
     */
    public com.api.v1.GetBankResponse getBank(com.api.v1.GetBankRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getGetBankMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * SearchBanks returns a list of banks that match the query.
     * </pre>
     */
    public com.api.v1.SearchBanksResponse searchBanks(com.api.v1.SearchBanksRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getSearchBanksMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * GetBranch returns a branch by the bank code and branch code.
     * </pre>
     */
    public com.api.v1.GetBranchResponse getBranch(com.api.v1.GetBranchRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getGetBranchMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * GetBranches returns a list of branches by the bank code.
     * </pre>
     */
    public com.api.v1.GetBranchesResponse getBranches(com.api.v1.GetBranchesRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getGetBranchesMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * SearchBranches returns a list of branches that match the query.
     * </pre>
     */
    public com.api.v1.SearchBranchesResponse searchBranches(com.api.v1.SearchBranchesRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getSearchBranchesMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do limited synchronous rpc calls to service BankService.
   */
  public static final class BankServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<BankServiceBlockingStub> {
    private BankServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected BankServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new BankServiceBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * GetBank returns a bank by the bank code.
     * </pre>
     */
    public com.api.v1.GetBankResponse getBank(com.api.v1.GetBankRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetBankMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * SearchBanks returns a list of banks that match the query.
     * </pre>
     */
    public com.api.v1.SearchBanksResponse searchBanks(com.api.v1.SearchBanksRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSearchBanksMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * GetBranch returns a branch by the bank code and branch code.
     * </pre>
     */
    public com.api.v1.GetBranchResponse getBranch(com.api.v1.GetBranchRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetBranchMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * GetBranches returns a list of branches by the bank code.
     * </pre>
     */
    public com.api.v1.GetBranchesResponse getBranches(com.api.v1.GetBranchesRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetBranchesMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * SearchBranches returns a list of branches that match the query.
     * </pre>
     */
    public com.api.v1.SearchBranchesResponse searchBranches(com.api.v1.SearchBranchesRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSearchBranchesMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service BankService.
   */
  public static final class BankServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<BankServiceFutureStub> {
    private BankServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected BankServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new BankServiceFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * GetBank returns a bank by the bank code.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.api.v1.GetBankResponse> getBank(
        com.api.v1.GetBankRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetBankMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * SearchBanks returns a list of banks that match the query.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.api.v1.SearchBanksResponse> searchBanks(
        com.api.v1.SearchBanksRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSearchBanksMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * GetBranch returns a branch by the bank code and branch code.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.api.v1.GetBranchResponse> getBranch(
        com.api.v1.GetBranchRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetBranchMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * GetBranches returns a list of branches by the bank code.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.api.v1.GetBranchesResponse> getBranches(
        com.api.v1.GetBranchesRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetBranchesMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * SearchBranches returns a list of branches that match the query.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.api.v1.SearchBranchesResponse> searchBranches(
        com.api.v1.SearchBranchesRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSearchBranchesMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_BANK = 0;
  private static final int METHODID_SEARCH_BANKS = 1;
  private static final int METHODID_GET_BRANCH = 2;
  private static final int METHODID_GET_BRANCHES = 3;
  private static final int METHODID_SEARCH_BRANCHES = 4;

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
        case METHODID_GET_BANK:
          serviceImpl.getBank((com.api.v1.GetBankRequest) request,
              (io.grpc.stub.StreamObserver<com.api.v1.GetBankResponse>) responseObserver);
          break;
        case METHODID_SEARCH_BANKS:
          serviceImpl.searchBanks((com.api.v1.SearchBanksRequest) request,
              (io.grpc.stub.StreamObserver<com.api.v1.SearchBanksResponse>) responseObserver);
          break;
        case METHODID_GET_BRANCH:
          serviceImpl.getBranch((com.api.v1.GetBranchRequest) request,
              (io.grpc.stub.StreamObserver<com.api.v1.GetBranchResponse>) responseObserver);
          break;
        case METHODID_GET_BRANCHES:
          serviceImpl.getBranches((com.api.v1.GetBranchesRequest) request,
              (io.grpc.stub.StreamObserver<com.api.v1.GetBranchesResponse>) responseObserver);
          break;
        case METHODID_SEARCH_BRANCHES:
          serviceImpl.searchBranches((com.api.v1.SearchBranchesRequest) request,
              (io.grpc.stub.StreamObserver<com.api.v1.SearchBranchesResponse>) responseObserver);
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
          getGetBankMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.api.v1.GetBankRequest,
              com.api.v1.GetBankResponse>(
                service, METHODID_GET_BANK)))
        .addMethod(
          getSearchBanksMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.api.v1.SearchBanksRequest,
              com.api.v1.SearchBanksResponse>(
                service, METHODID_SEARCH_BANKS)))
        .addMethod(
          getGetBranchMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.api.v1.GetBranchRequest,
              com.api.v1.GetBranchResponse>(
                service, METHODID_GET_BRANCH)))
        .addMethod(
          getGetBranchesMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.api.v1.GetBranchesRequest,
              com.api.v1.GetBranchesResponse>(
                service, METHODID_GET_BRANCHES)))
        .addMethod(
          getSearchBranchesMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.api.v1.SearchBranchesRequest,
              com.api.v1.SearchBranchesResponse>(
                service, METHODID_SEARCH_BRANCHES)))
        .build();
  }

  private static abstract class BankServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    BankServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.api.v1.BankProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("BankService");
    }
  }

  private static final class BankServiceFileDescriptorSupplier
      extends BankServiceBaseDescriptorSupplier {
    BankServiceFileDescriptorSupplier() {}
  }

  private static final class BankServiceMethodDescriptorSupplier
      extends BankServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    BankServiceMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (BankServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new BankServiceFileDescriptorSupplier())
              .addMethod(getGetBankMethod())
              .addMethod(getSearchBanksMethod())
              .addMethod(getGetBranchMethod())
              .addMethod(getGetBranchesMethod())
              .addMethod(getSearchBranchesMethod())
              .build();
        }
      }
    }
    return result;
  }
}
