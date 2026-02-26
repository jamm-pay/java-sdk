package com.api.v1;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@io.grpc.stub.annotations.GrpcGenerated
public final class CustomerServiceGrpc {

  private CustomerServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "api.v1.CustomerService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.api.v1.CreateCustomerRequest,
      com.api.v1.CreateCustomerResponse> getCreateCustomerMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CreateCustomer",
      requestType = com.api.v1.CreateCustomerRequest.class,
      responseType = com.api.v1.CreateCustomerResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.api.v1.CreateCustomerRequest,
      com.api.v1.CreateCustomerResponse> getCreateCustomerMethod() {
    io.grpc.MethodDescriptor<com.api.v1.CreateCustomerRequest, com.api.v1.CreateCustomerResponse> getCreateCustomerMethod;
    if ((getCreateCustomerMethod = CustomerServiceGrpc.getCreateCustomerMethod) == null) {
      synchronized (CustomerServiceGrpc.class) {
        if ((getCreateCustomerMethod = CustomerServiceGrpc.getCreateCustomerMethod) == null) {
          CustomerServiceGrpc.getCreateCustomerMethod = getCreateCustomerMethod =
              io.grpc.MethodDescriptor.<com.api.v1.CreateCustomerRequest, com.api.v1.CreateCustomerResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "CreateCustomer"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.api.v1.CreateCustomerRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.api.v1.CreateCustomerResponse.getDefaultInstance()))
              .setSchemaDescriptor(new CustomerServiceMethodDescriptorSupplier("CreateCustomer"))
              .build();
        }
      }
    }
    return getCreateCustomerMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.api.v1.GetCustomerRequest,
      com.api.v1.GetCustomerResponse> getGetCustomerMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetCustomer",
      requestType = com.api.v1.GetCustomerRequest.class,
      responseType = com.api.v1.GetCustomerResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.api.v1.GetCustomerRequest,
      com.api.v1.GetCustomerResponse> getGetCustomerMethod() {
    io.grpc.MethodDescriptor<com.api.v1.GetCustomerRequest, com.api.v1.GetCustomerResponse> getGetCustomerMethod;
    if ((getGetCustomerMethod = CustomerServiceGrpc.getGetCustomerMethod) == null) {
      synchronized (CustomerServiceGrpc.class) {
        if ((getGetCustomerMethod = CustomerServiceGrpc.getGetCustomerMethod) == null) {
          CustomerServiceGrpc.getGetCustomerMethod = getGetCustomerMethod =
              io.grpc.MethodDescriptor.<com.api.v1.GetCustomerRequest, com.api.v1.GetCustomerResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetCustomer"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.api.v1.GetCustomerRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.api.v1.GetCustomerResponse.getDefaultInstance()))
              .setSchemaDescriptor(new CustomerServiceMethodDescriptorSupplier("GetCustomer"))
              .build();
        }
      }
    }
    return getGetCustomerMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.api.v1.UpdateCustomerRequest,
      com.api.v1.UpdateCustomerResponse> getUpdateCustomerMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "UpdateCustomer",
      requestType = com.api.v1.UpdateCustomerRequest.class,
      responseType = com.api.v1.UpdateCustomerResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.api.v1.UpdateCustomerRequest,
      com.api.v1.UpdateCustomerResponse> getUpdateCustomerMethod() {
    io.grpc.MethodDescriptor<com.api.v1.UpdateCustomerRequest, com.api.v1.UpdateCustomerResponse> getUpdateCustomerMethod;
    if ((getUpdateCustomerMethod = CustomerServiceGrpc.getUpdateCustomerMethod) == null) {
      synchronized (CustomerServiceGrpc.class) {
        if ((getUpdateCustomerMethod = CustomerServiceGrpc.getUpdateCustomerMethod) == null) {
          CustomerServiceGrpc.getUpdateCustomerMethod = getUpdateCustomerMethod =
              io.grpc.MethodDescriptor.<com.api.v1.UpdateCustomerRequest, com.api.v1.UpdateCustomerResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "UpdateCustomer"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.api.v1.UpdateCustomerRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.api.v1.UpdateCustomerResponse.getDefaultInstance()))
              .setSchemaDescriptor(new CustomerServiceMethodDescriptorSupplier("UpdateCustomer"))
              .build();
        }
      }
    }
    return getUpdateCustomerMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.api.v1.DeleteCustomerRequest,
      com.api.v1.DeleteCustomerResponse> getDeleteCustomerMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "DeleteCustomer",
      requestType = com.api.v1.DeleteCustomerRequest.class,
      responseType = com.api.v1.DeleteCustomerResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.api.v1.DeleteCustomerRequest,
      com.api.v1.DeleteCustomerResponse> getDeleteCustomerMethod() {
    io.grpc.MethodDescriptor<com.api.v1.DeleteCustomerRequest, com.api.v1.DeleteCustomerResponse> getDeleteCustomerMethod;
    if ((getDeleteCustomerMethod = CustomerServiceGrpc.getDeleteCustomerMethod) == null) {
      synchronized (CustomerServiceGrpc.class) {
        if ((getDeleteCustomerMethod = CustomerServiceGrpc.getDeleteCustomerMethod) == null) {
          CustomerServiceGrpc.getDeleteCustomerMethod = getDeleteCustomerMethod =
              io.grpc.MethodDescriptor.<com.api.v1.DeleteCustomerRequest, com.api.v1.DeleteCustomerResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "DeleteCustomer"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.api.v1.DeleteCustomerRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.api.v1.DeleteCustomerResponse.getDefaultInstance()))
              .setSchemaDescriptor(new CustomerServiceMethodDescriptorSupplier("DeleteCustomer"))
              .build();
        }
      }
    }
    return getDeleteCustomerMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.api.v1.GetContractRequest,
      com.api.v1.GetContractResponse> getGetContractMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetContract",
      requestType = com.api.v1.GetContractRequest.class,
      responseType = com.api.v1.GetContractResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.api.v1.GetContractRequest,
      com.api.v1.GetContractResponse> getGetContractMethod() {
    io.grpc.MethodDescriptor<com.api.v1.GetContractRequest, com.api.v1.GetContractResponse> getGetContractMethod;
    if ((getGetContractMethod = CustomerServiceGrpc.getGetContractMethod) == null) {
      synchronized (CustomerServiceGrpc.class) {
        if ((getGetContractMethod = CustomerServiceGrpc.getGetContractMethod) == null) {
          CustomerServiceGrpc.getGetContractMethod = getGetContractMethod =
              io.grpc.MethodDescriptor.<com.api.v1.GetContractRequest, com.api.v1.GetContractResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetContract"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.api.v1.GetContractRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.api.v1.GetContractResponse.getDefaultInstance()))
              .setSchemaDescriptor(new CustomerServiceMethodDescriptorSupplier("GetContract"))
              .build();
        }
      }
    }
    return getGetContractMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static CustomerServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<CustomerServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<CustomerServiceStub>() {
        @java.lang.Override
        public CustomerServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new CustomerServiceStub(channel, callOptions);
        }
      };
    return CustomerServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports all types of calls on the service
   */
  public static CustomerServiceBlockingV2Stub newBlockingV2Stub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<CustomerServiceBlockingV2Stub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<CustomerServiceBlockingV2Stub>() {
        @java.lang.Override
        public CustomerServiceBlockingV2Stub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new CustomerServiceBlockingV2Stub(channel, callOptions);
        }
      };
    return CustomerServiceBlockingV2Stub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static CustomerServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<CustomerServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<CustomerServiceBlockingStub>() {
        @java.lang.Override
        public CustomerServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new CustomerServiceBlockingStub(channel, callOptions);
        }
      };
    return CustomerServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static CustomerServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<CustomerServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<CustomerServiceFutureStub>() {
        @java.lang.Override
        public CustomerServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new CustomerServiceFutureStub(channel, callOptions);
        }
      };
    return CustomerServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     * <pre>
     * Create a new customer on Jamm.
     * The response will contain the Jamm's customer ID, which can be used to
     * make a purchase calls.
     * Jammに新しいCustomerを作成します。
     * APIコールが成功するとJammはCustomer IDを返します。このIDは決済を行うために必要です。
     * </pre>
     */
    default void createCustomer(com.api.v1.CreateCustomerRequest request,
        io.grpc.stub.StreamObserver<com.api.v1.CreateCustomerResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCreateCustomerMethod(), responseObserver);
    }

    /**
     * <pre>
     * GetCustomer retrieves a Jamm customer.
     * Customerを取得します。
     * </pre>
     */
    default void getCustomer(com.api.v1.GetCustomerRequest request,
        io.grpc.stub.StreamObserver<com.api.v1.GetCustomerResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetCustomerMethod(), responseObserver);
    }

    /**
     * <pre>
     * Update a customer.
     * Customerを更新します。
     * </pre>
     */
    default void updateCustomer(com.api.v1.UpdateCustomerRequest request,
        io.grpc.stub.StreamObserver<com.api.v1.UpdateCustomerResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getUpdateCustomerMethod(), responseObserver);
    }

    /**
     * <pre>
     * Delete a customer.
     * Customerを削除します。
     * </pre>
     */
    default void deleteCustomer(com.api.v1.DeleteCustomerRequest request,
        io.grpc.stub.StreamObserver<com.api.v1.DeleteCustomerResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getDeleteCustomerMethod(), responseObserver);
    }

    /**
     */
    default void getContract(com.api.v1.GetContractRequest request,
        io.grpc.stub.StreamObserver<com.api.v1.GetContractResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetContractMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service CustomerService.
   */
  public static abstract class CustomerServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return CustomerServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service CustomerService.
   */
  public static final class CustomerServiceStub
      extends io.grpc.stub.AbstractAsyncStub<CustomerServiceStub> {
    private CustomerServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected CustomerServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new CustomerServiceStub(channel, callOptions);
    }

    /**
     * <pre>
     * Create a new customer on Jamm.
     * The response will contain the Jamm's customer ID, which can be used to
     * make a purchase calls.
     * Jammに新しいCustomerを作成します。
     * APIコールが成功するとJammはCustomer IDを返します。このIDは決済を行うために必要です。
     * </pre>
     */
    public void createCustomer(com.api.v1.CreateCustomerRequest request,
        io.grpc.stub.StreamObserver<com.api.v1.CreateCustomerResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCreateCustomerMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * GetCustomer retrieves a Jamm customer.
     * Customerを取得します。
     * </pre>
     */
    public void getCustomer(com.api.v1.GetCustomerRequest request,
        io.grpc.stub.StreamObserver<com.api.v1.GetCustomerResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetCustomerMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Update a customer.
     * Customerを更新します。
     * </pre>
     */
    public void updateCustomer(com.api.v1.UpdateCustomerRequest request,
        io.grpc.stub.StreamObserver<com.api.v1.UpdateCustomerResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getUpdateCustomerMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Delete a customer.
     * Customerを削除します。
     * </pre>
     */
    public void deleteCustomer(com.api.v1.DeleteCustomerRequest request,
        io.grpc.stub.StreamObserver<com.api.v1.DeleteCustomerResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getDeleteCustomerMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getContract(com.api.v1.GetContractRequest request,
        io.grpc.stub.StreamObserver<com.api.v1.GetContractResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetContractMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service CustomerService.
   */
  public static final class CustomerServiceBlockingV2Stub
      extends io.grpc.stub.AbstractBlockingStub<CustomerServiceBlockingV2Stub> {
    private CustomerServiceBlockingV2Stub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected CustomerServiceBlockingV2Stub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new CustomerServiceBlockingV2Stub(channel, callOptions);
    }

    /**
     * <pre>
     * Create a new customer on Jamm.
     * The response will contain the Jamm's customer ID, which can be used to
     * make a purchase calls.
     * Jammに新しいCustomerを作成します。
     * APIコールが成功するとJammはCustomer IDを返します。このIDは決済を行うために必要です。
     * </pre>
     */
    public com.api.v1.CreateCustomerResponse createCustomer(com.api.v1.CreateCustomerRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getCreateCustomerMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * GetCustomer retrieves a Jamm customer.
     * Customerを取得します。
     * </pre>
     */
    public com.api.v1.GetCustomerResponse getCustomer(com.api.v1.GetCustomerRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getGetCustomerMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Update a customer.
     * Customerを更新します。
     * </pre>
     */
    public com.api.v1.UpdateCustomerResponse updateCustomer(com.api.v1.UpdateCustomerRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getUpdateCustomerMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Delete a customer.
     * Customerを削除します。
     * </pre>
     */
    public com.api.v1.DeleteCustomerResponse deleteCustomer(com.api.v1.DeleteCustomerRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getDeleteCustomerMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.api.v1.GetContractResponse getContract(com.api.v1.GetContractRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getGetContractMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do limited synchronous rpc calls to service CustomerService.
   */
  public static final class CustomerServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<CustomerServiceBlockingStub> {
    private CustomerServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected CustomerServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new CustomerServiceBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * Create a new customer on Jamm.
     * The response will contain the Jamm's customer ID, which can be used to
     * make a purchase calls.
     * Jammに新しいCustomerを作成します。
     * APIコールが成功するとJammはCustomer IDを返します。このIDは決済を行うために必要です。
     * </pre>
     */
    public com.api.v1.CreateCustomerResponse createCustomer(com.api.v1.CreateCustomerRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCreateCustomerMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * GetCustomer retrieves a Jamm customer.
     * Customerを取得します。
     * </pre>
     */
    public com.api.v1.GetCustomerResponse getCustomer(com.api.v1.GetCustomerRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetCustomerMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Update a customer.
     * Customerを更新します。
     * </pre>
     */
    public com.api.v1.UpdateCustomerResponse updateCustomer(com.api.v1.UpdateCustomerRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getUpdateCustomerMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Delete a customer.
     * Customerを削除します。
     * </pre>
     */
    public com.api.v1.DeleteCustomerResponse deleteCustomer(com.api.v1.DeleteCustomerRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getDeleteCustomerMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.api.v1.GetContractResponse getContract(com.api.v1.GetContractRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetContractMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service CustomerService.
   */
  public static final class CustomerServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<CustomerServiceFutureStub> {
    private CustomerServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected CustomerServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new CustomerServiceFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * Create a new customer on Jamm.
     * The response will contain the Jamm's customer ID, which can be used to
     * make a purchase calls.
     * Jammに新しいCustomerを作成します。
     * APIコールが成功するとJammはCustomer IDを返します。このIDは決済を行うために必要です。
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.api.v1.CreateCustomerResponse> createCustomer(
        com.api.v1.CreateCustomerRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCreateCustomerMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * GetCustomer retrieves a Jamm customer.
     * Customerを取得します。
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.api.v1.GetCustomerResponse> getCustomer(
        com.api.v1.GetCustomerRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetCustomerMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Update a customer.
     * Customerを更新します。
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.api.v1.UpdateCustomerResponse> updateCustomer(
        com.api.v1.UpdateCustomerRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getUpdateCustomerMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Delete a customer.
     * Customerを削除します。
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.api.v1.DeleteCustomerResponse> deleteCustomer(
        com.api.v1.DeleteCustomerRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getDeleteCustomerMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.api.v1.GetContractResponse> getContract(
        com.api.v1.GetContractRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetContractMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_CREATE_CUSTOMER = 0;
  private static final int METHODID_GET_CUSTOMER = 1;
  private static final int METHODID_UPDATE_CUSTOMER = 2;
  private static final int METHODID_DELETE_CUSTOMER = 3;
  private static final int METHODID_GET_CONTRACT = 4;

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
        case METHODID_CREATE_CUSTOMER:
          serviceImpl.createCustomer((com.api.v1.CreateCustomerRequest) request,
              (io.grpc.stub.StreamObserver<com.api.v1.CreateCustomerResponse>) responseObserver);
          break;
        case METHODID_GET_CUSTOMER:
          serviceImpl.getCustomer((com.api.v1.GetCustomerRequest) request,
              (io.grpc.stub.StreamObserver<com.api.v1.GetCustomerResponse>) responseObserver);
          break;
        case METHODID_UPDATE_CUSTOMER:
          serviceImpl.updateCustomer((com.api.v1.UpdateCustomerRequest) request,
              (io.grpc.stub.StreamObserver<com.api.v1.UpdateCustomerResponse>) responseObserver);
          break;
        case METHODID_DELETE_CUSTOMER:
          serviceImpl.deleteCustomer((com.api.v1.DeleteCustomerRequest) request,
              (io.grpc.stub.StreamObserver<com.api.v1.DeleteCustomerResponse>) responseObserver);
          break;
        case METHODID_GET_CONTRACT:
          serviceImpl.getContract((com.api.v1.GetContractRequest) request,
              (io.grpc.stub.StreamObserver<com.api.v1.GetContractResponse>) responseObserver);
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
          getCreateCustomerMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.api.v1.CreateCustomerRequest,
              com.api.v1.CreateCustomerResponse>(
                service, METHODID_CREATE_CUSTOMER)))
        .addMethod(
          getGetCustomerMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.api.v1.GetCustomerRequest,
              com.api.v1.GetCustomerResponse>(
                service, METHODID_GET_CUSTOMER)))
        .addMethod(
          getUpdateCustomerMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.api.v1.UpdateCustomerRequest,
              com.api.v1.UpdateCustomerResponse>(
                service, METHODID_UPDATE_CUSTOMER)))
        .addMethod(
          getDeleteCustomerMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.api.v1.DeleteCustomerRequest,
              com.api.v1.DeleteCustomerResponse>(
                service, METHODID_DELETE_CUSTOMER)))
        .addMethod(
          getGetContractMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.api.v1.GetContractRequest,
              com.api.v1.GetContractResponse>(
                service, METHODID_GET_CONTRACT)))
        .build();
  }

  private static abstract class CustomerServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    CustomerServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.api.v1.CustomerProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("CustomerService");
    }
  }

  private static final class CustomerServiceFileDescriptorSupplier
      extends CustomerServiceBaseDescriptorSupplier {
    CustomerServiceFileDescriptorSupplier() {}
  }

  private static final class CustomerServiceMethodDescriptorSupplier
      extends CustomerServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    CustomerServiceMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (CustomerServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new CustomerServiceFileDescriptorSupplier())
              .addMethod(getCreateCustomerMethod())
              .addMethod(getGetCustomerMethod())
              .addMethod(getUpdateCustomerMethod())
              .addMethod(getDeleteCustomerMethod())
              .addMethod(getGetContractMethod())
              .build();
        }
      }
    }
    return result;
  }
}
