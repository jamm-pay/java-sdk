package com.api.v1;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 * <pre>
 * # Payment Service
 * This service provides functionality for managing contracts and charges.
 * It allows for creating contracts both with and without initial charges,
 * as well as adding charges to existing contracts.
 * </pre>
 */
@io.grpc.stub.annotations.GrpcGenerated
public final class PaymentServiceGrpc {

  private PaymentServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "api.v1.PaymentService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.api.v1.CreateContractWithoutChargeRequest,
      com.api.v1.CreateContractWithoutChargeResponse> getCreateContractWithoutChargeMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CreateContractWithoutCharge",
      requestType = com.api.v1.CreateContractWithoutChargeRequest.class,
      responseType = com.api.v1.CreateContractWithoutChargeResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.api.v1.CreateContractWithoutChargeRequest,
      com.api.v1.CreateContractWithoutChargeResponse> getCreateContractWithoutChargeMethod() {
    io.grpc.MethodDescriptor<com.api.v1.CreateContractWithoutChargeRequest, com.api.v1.CreateContractWithoutChargeResponse> getCreateContractWithoutChargeMethod;
    if ((getCreateContractWithoutChargeMethod = PaymentServiceGrpc.getCreateContractWithoutChargeMethod) == null) {
      synchronized (PaymentServiceGrpc.class) {
        if ((getCreateContractWithoutChargeMethod = PaymentServiceGrpc.getCreateContractWithoutChargeMethod) == null) {
          PaymentServiceGrpc.getCreateContractWithoutChargeMethod = getCreateContractWithoutChargeMethod =
              io.grpc.MethodDescriptor.<com.api.v1.CreateContractWithoutChargeRequest, com.api.v1.CreateContractWithoutChargeResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "CreateContractWithoutCharge"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.api.v1.CreateContractWithoutChargeRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.api.v1.CreateContractWithoutChargeResponse.getDefaultInstance()))
              .setSchemaDescriptor(new PaymentServiceMethodDescriptorSupplier("CreateContractWithoutCharge"))
              .build();
        }
      }
    }
    return getCreateContractWithoutChargeMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.api.v1.CreateContractWithChargeRequest,
      com.api.v1.CreateContractWithChargeResponse> getCreateContractWithChargeMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CreateContractWithCharge",
      requestType = com.api.v1.CreateContractWithChargeRequest.class,
      responseType = com.api.v1.CreateContractWithChargeResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.api.v1.CreateContractWithChargeRequest,
      com.api.v1.CreateContractWithChargeResponse> getCreateContractWithChargeMethod() {
    io.grpc.MethodDescriptor<com.api.v1.CreateContractWithChargeRequest, com.api.v1.CreateContractWithChargeResponse> getCreateContractWithChargeMethod;
    if ((getCreateContractWithChargeMethod = PaymentServiceGrpc.getCreateContractWithChargeMethod) == null) {
      synchronized (PaymentServiceGrpc.class) {
        if ((getCreateContractWithChargeMethod = PaymentServiceGrpc.getCreateContractWithChargeMethod) == null) {
          PaymentServiceGrpc.getCreateContractWithChargeMethod = getCreateContractWithChargeMethod =
              io.grpc.MethodDescriptor.<com.api.v1.CreateContractWithChargeRequest, com.api.v1.CreateContractWithChargeResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "CreateContractWithCharge"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.api.v1.CreateContractWithChargeRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.api.v1.CreateContractWithChargeResponse.getDefaultInstance()))
              .setSchemaDescriptor(new PaymentServiceMethodDescriptorSupplier("CreateContractWithCharge"))
              .build();
        }
      }
    }
    return getCreateContractWithChargeMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.api.v1.AddChargeRequest,
      com.api.v1.AddChargeResponse> getAddChargeMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "AddCharge",
      requestType = com.api.v1.AddChargeRequest.class,
      responseType = com.api.v1.AddChargeResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.api.v1.AddChargeRequest,
      com.api.v1.AddChargeResponse> getAddChargeMethod() {
    io.grpc.MethodDescriptor<com.api.v1.AddChargeRequest, com.api.v1.AddChargeResponse> getAddChargeMethod;
    if ((getAddChargeMethod = PaymentServiceGrpc.getAddChargeMethod) == null) {
      synchronized (PaymentServiceGrpc.class) {
        if ((getAddChargeMethod = PaymentServiceGrpc.getAddChargeMethod) == null) {
          PaymentServiceGrpc.getAddChargeMethod = getAddChargeMethod =
              io.grpc.MethodDescriptor.<com.api.v1.AddChargeRequest, com.api.v1.AddChargeResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "AddCharge"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.api.v1.AddChargeRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.api.v1.AddChargeResponse.getDefaultInstance()))
              .setSchemaDescriptor(new PaymentServiceMethodDescriptorSupplier("AddCharge"))
              .build();
        }
      }
    }
    return getAddChargeMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.api.v1.GetChargeRequest,
      com.api.v1.GetChargeResponse> getGetChargeMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetCharge",
      requestType = com.api.v1.GetChargeRequest.class,
      responseType = com.api.v1.GetChargeResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.api.v1.GetChargeRequest,
      com.api.v1.GetChargeResponse> getGetChargeMethod() {
    io.grpc.MethodDescriptor<com.api.v1.GetChargeRequest, com.api.v1.GetChargeResponse> getGetChargeMethod;
    if ((getGetChargeMethod = PaymentServiceGrpc.getGetChargeMethod) == null) {
      synchronized (PaymentServiceGrpc.class) {
        if ((getGetChargeMethod = PaymentServiceGrpc.getGetChargeMethod) == null) {
          PaymentServiceGrpc.getGetChargeMethod = getGetChargeMethod =
              io.grpc.MethodDescriptor.<com.api.v1.GetChargeRequest, com.api.v1.GetChargeResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetCharge"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.api.v1.GetChargeRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.api.v1.GetChargeResponse.getDefaultInstance()))
              .setSchemaDescriptor(new PaymentServiceMethodDescriptorSupplier("GetCharge"))
              .build();
        }
      }
    }
    return getGetChargeMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.api.v1.GetChargesRequest,
      com.api.v1.GetChargesResponse> getGetChargesMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetCharges",
      requestType = com.api.v1.GetChargesRequest.class,
      responseType = com.api.v1.GetChargesResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.api.v1.GetChargesRequest,
      com.api.v1.GetChargesResponse> getGetChargesMethod() {
    io.grpc.MethodDescriptor<com.api.v1.GetChargesRequest, com.api.v1.GetChargesResponse> getGetChargesMethod;
    if ((getGetChargesMethod = PaymentServiceGrpc.getGetChargesMethod) == null) {
      synchronized (PaymentServiceGrpc.class) {
        if ((getGetChargesMethod = PaymentServiceGrpc.getGetChargesMethod) == null) {
          PaymentServiceGrpc.getGetChargesMethod = getGetChargesMethod =
              io.grpc.MethodDescriptor.<com.api.v1.GetChargesRequest, com.api.v1.GetChargesResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetCharges"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.api.v1.GetChargesRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.api.v1.GetChargesResponse.getDefaultInstance()))
              .setSchemaDescriptor(new PaymentServiceMethodDescriptorSupplier("GetCharges"))
              .build();
        }
      }
    }
    return getGetChargesMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.api.v1.WithdrawRequest,
      com.api.v1.WithdrawResponse> getWithdrawMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Withdraw",
      requestType = com.api.v1.WithdrawRequest.class,
      responseType = com.api.v1.WithdrawResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.api.v1.WithdrawRequest,
      com.api.v1.WithdrawResponse> getWithdrawMethod() {
    io.grpc.MethodDescriptor<com.api.v1.WithdrawRequest, com.api.v1.WithdrawResponse> getWithdrawMethod;
    if ((getWithdrawMethod = PaymentServiceGrpc.getWithdrawMethod) == null) {
      synchronized (PaymentServiceGrpc.class) {
        if ((getWithdrawMethod = PaymentServiceGrpc.getWithdrawMethod) == null) {
          PaymentServiceGrpc.getWithdrawMethod = getWithdrawMethod =
              io.grpc.MethodDescriptor.<com.api.v1.WithdrawRequest, com.api.v1.WithdrawResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Withdraw"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.api.v1.WithdrawRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.api.v1.WithdrawResponse.getDefaultInstance()))
              .setSchemaDescriptor(new PaymentServiceMethodDescriptorSupplier("Withdraw"))
              .build();
        }
      }
    }
    return getWithdrawMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.api.v1.WithdrawAsyncRequest,
      com.api.v1.WithdrawAsyncResponse> getWithdrawAsyncMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "WithdrawAsync",
      requestType = com.api.v1.WithdrawAsyncRequest.class,
      responseType = com.api.v1.WithdrawAsyncResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.api.v1.WithdrawAsyncRequest,
      com.api.v1.WithdrawAsyncResponse> getWithdrawAsyncMethod() {
    io.grpc.MethodDescriptor<com.api.v1.WithdrawAsyncRequest, com.api.v1.WithdrawAsyncResponse> getWithdrawAsyncMethod;
    if ((getWithdrawAsyncMethod = PaymentServiceGrpc.getWithdrawAsyncMethod) == null) {
      synchronized (PaymentServiceGrpc.class) {
        if ((getWithdrawAsyncMethod = PaymentServiceGrpc.getWithdrawAsyncMethod) == null) {
          PaymentServiceGrpc.getWithdrawAsyncMethod = getWithdrawAsyncMethod =
              io.grpc.MethodDescriptor.<com.api.v1.WithdrawAsyncRequest, com.api.v1.WithdrawAsyncResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "WithdrawAsync"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.api.v1.WithdrawAsyncRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.api.v1.WithdrawAsyncResponse.getDefaultInstance()))
              .setSchemaDescriptor(new PaymentServiceMethodDescriptorSupplier("WithdrawAsync"))
              .build();
        }
      }
    }
    return getWithdrawAsyncMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.api.v1.OffSessionPaymentAsyncRequest,
      com.api.v1.OffSessionPaymentAsyncResponse> getOffSessionPaymentAsyncMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "OffSessionPaymentAsync",
      requestType = com.api.v1.OffSessionPaymentAsyncRequest.class,
      responseType = com.api.v1.OffSessionPaymentAsyncResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.api.v1.OffSessionPaymentAsyncRequest,
      com.api.v1.OffSessionPaymentAsyncResponse> getOffSessionPaymentAsyncMethod() {
    io.grpc.MethodDescriptor<com.api.v1.OffSessionPaymentAsyncRequest, com.api.v1.OffSessionPaymentAsyncResponse> getOffSessionPaymentAsyncMethod;
    if ((getOffSessionPaymentAsyncMethod = PaymentServiceGrpc.getOffSessionPaymentAsyncMethod) == null) {
      synchronized (PaymentServiceGrpc.class) {
        if ((getOffSessionPaymentAsyncMethod = PaymentServiceGrpc.getOffSessionPaymentAsyncMethod) == null) {
          PaymentServiceGrpc.getOffSessionPaymentAsyncMethod = getOffSessionPaymentAsyncMethod =
              io.grpc.MethodDescriptor.<com.api.v1.OffSessionPaymentAsyncRequest, com.api.v1.OffSessionPaymentAsyncResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "OffSessionPaymentAsync"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.api.v1.OffSessionPaymentAsyncRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.api.v1.OffSessionPaymentAsyncResponse.getDefaultInstance()))
              .setSchemaDescriptor(new PaymentServiceMethodDescriptorSupplier("OffSessionPaymentAsync"))
              .build();
        }
      }
    }
    return getOffSessionPaymentAsyncMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.api.v1.WithdrawAsyncStatusRequest,
      com.api.v1.WithdrawAsyncStatusResponse> getWithdrawAsyncStatusMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "WithdrawAsyncStatus",
      requestType = com.api.v1.WithdrawAsyncStatusRequest.class,
      responseType = com.api.v1.WithdrawAsyncStatusResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.api.v1.WithdrawAsyncStatusRequest,
      com.api.v1.WithdrawAsyncStatusResponse> getWithdrawAsyncStatusMethod() {
    io.grpc.MethodDescriptor<com.api.v1.WithdrawAsyncStatusRequest, com.api.v1.WithdrawAsyncStatusResponse> getWithdrawAsyncStatusMethod;
    if ((getWithdrawAsyncStatusMethod = PaymentServiceGrpc.getWithdrawAsyncStatusMethod) == null) {
      synchronized (PaymentServiceGrpc.class) {
        if ((getWithdrawAsyncStatusMethod = PaymentServiceGrpc.getWithdrawAsyncStatusMethod) == null) {
          PaymentServiceGrpc.getWithdrawAsyncStatusMethod = getWithdrawAsyncStatusMethod =
              io.grpc.MethodDescriptor.<com.api.v1.WithdrawAsyncStatusRequest, com.api.v1.WithdrawAsyncStatusResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "WithdrawAsyncStatus"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.api.v1.WithdrawAsyncStatusRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.api.v1.WithdrawAsyncStatusResponse.getDefaultInstance()))
              .setSchemaDescriptor(new PaymentServiceMethodDescriptorSupplier("WithdrawAsyncStatus"))
              .build();
        }
      }
    }
    return getWithdrawAsyncStatusMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.api.v1.OffSessionPaymentRequest,
      com.api.v1.OffSessionPaymentResponse> getOffSessionPaymentMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "OffSessionPayment",
      requestType = com.api.v1.OffSessionPaymentRequest.class,
      responseType = com.api.v1.OffSessionPaymentResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.api.v1.OffSessionPaymentRequest,
      com.api.v1.OffSessionPaymentResponse> getOffSessionPaymentMethod() {
    io.grpc.MethodDescriptor<com.api.v1.OffSessionPaymentRequest, com.api.v1.OffSessionPaymentResponse> getOffSessionPaymentMethod;
    if ((getOffSessionPaymentMethod = PaymentServiceGrpc.getOffSessionPaymentMethod) == null) {
      synchronized (PaymentServiceGrpc.class) {
        if ((getOffSessionPaymentMethod = PaymentServiceGrpc.getOffSessionPaymentMethod) == null) {
          PaymentServiceGrpc.getOffSessionPaymentMethod = getOffSessionPaymentMethod =
              io.grpc.MethodDescriptor.<com.api.v1.OffSessionPaymentRequest, com.api.v1.OffSessionPaymentResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "OffSessionPayment"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.api.v1.OffSessionPaymentRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.api.v1.OffSessionPaymentResponse.getDefaultInstance()))
              .setSchemaDescriptor(new PaymentServiceMethodDescriptorSupplier("OffSessionPayment"))
              .build();
        }
      }
    }
    return getOffSessionPaymentMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.api.v1.OnSessionPaymentRequest,
      com.api.v1.OnSessionPaymentResponse> getOnSessionPaymentMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "OnSessionPayment",
      requestType = com.api.v1.OnSessionPaymentRequest.class,
      responseType = com.api.v1.OnSessionPaymentResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.api.v1.OnSessionPaymentRequest,
      com.api.v1.OnSessionPaymentResponse> getOnSessionPaymentMethod() {
    io.grpc.MethodDescriptor<com.api.v1.OnSessionPaymentRequest, com.api.v1.OnSessionPaymentResponse> getOnSessionPaymentMethod;
    if ((getOnSessionPaymentMethod = PaymentServiceGrpc.getOnSessionPaymentMethod) == null) {
      synchronized (PaymentServiceGrpc.class) {
        if ((getOnSessionPaymentMethod = PaymentServiceGrpc.getOnSessionPaymentMethod) == null) {
          PaymentServiceGrpc.getOnSessionPaymentMethod = getOnSessionPaymentMethod =
              io.grpc.MethodDescriptor.<com.api.v1.OnSessionPaymentRequest, com.api.v1.OnSessionPaymentResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "OnSessionPayment"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.api.v1.OnSessionPaymentRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.api.v1.OnSessionPaymentResponse.getDefaultInstance()))
              .setSchemaDescriptor(new PaymentServiceMethodDescriptorSupplier("OnSessionPayment"))
              .build();
        }
      }
    }
    return getOnSessionPaymentMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.api.v1.RefundRequest,
      com.api.v1.RefundResponse> getRefundMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Refund",
      requestType = com.api.v1.RefundRequest.class,
      responseType = com.api.v1.RefundResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.api.v1.RefundRequest,
      com.api.v1.RefundResponse> getRefundMethod() {
    io.grpc.MethodDescriptor<com.api.v1.RefundRequest, com.api.v1.RefundResponse> getRefundMethod;
    if ((getRefundMethod = PaymentServiceGrpc.getRefundMethod) == null) {
      synchronized (PaymentServiceGrpc.class) {
        if ((getRefundMethod = PaymentServiceGrpc.getRefundMethod) == null) {
          PaymentServiceGrpc.getRefundMethod = getRefundMethod =
              io.grpc.MethodDescriptor.<com.api.v1.RefundRequest, com.api.v1.RefundResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Refund"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.api.v1.RefundRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.api.v1.RefundResponse.getDefaultInstance()))
              .setSchemaDescriptor(new PaymentServiceMethodDescriptorSupplier("Refund"))
              .build();
        }
      }
    }
    return getRefundMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static PaymentServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<PaymentServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<PaymentServiceStub>() {
        @java.lang.Override
        public PaymentServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new PaymentServiceStub(channel, callOptions);
        }
      };
    return PaymentServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports all types of calls on the service
   */
  public static PaymentServiceBlockingV2Stub newBlockingV2Stub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<PaymentServiceBlockingV2Stub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<PaymentServiceBlockingV2Stub>() {
        @java.lang.Override
        public PaymentServiceBlockingV2Stub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new PaymentServiceBlockingV2Stub(channel, callOptions);
        }
      };
    return PaymentServiceBlockingV2Stub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static PaymentServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<PaymentServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<PaymentServiceBlockingStub>() {
        @java.lang.Override
        public PaymentServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new PaymentServiceBlockingStub(channel, callOptions);
        }
      };
    return PaymentServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static PaymentServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<PaymentServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<PaymentServiceFutureStub>() {
        @java.lang.Override
        public PaymentServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new PaymentServiceFutureStub(channel, callOptions);
        }
      };
    return PaymentServiceFutureStub.newStub(factory, channel);
  }

  /**
   * <pre>
   * # Payment Service
   * This service provides functionality for managing contracts and charges.
   * It allows for creating contracts both with and without initial charges,
   * as well as adding charges to existing contracts.
   * </pre>
   */
  public interface AsyncService {

    /**
     * <pre>
     * # DEPRECATED: Use OnSessionPayment instead.
     * ## CreateContractWithoutCharge
     * Creates a new contract without an initial charge. This is useful when you want to
     * establish a billing relationship with a customer but don't need to charge them immediately.
     * The contract can later be used with AddCharge to apply charges as needed.
     * **Parameters:**
     * - CreateContractWithoutChargeRequest: Contains buyer information and redirect details
     * **Returns:**
     * - CreateContractWithoutChargeResponse: Contains the created payment link with contract ID, merchant customer ID
     * **Service function reference:**
     * ```go
     * func (s *paymentService) CreateContractWithoutCharge(ctx context.Context, req *CreateContractWithoutChargeRequest) (*CreateContractWithoutChargeResponse, error) {
     *   // Validates buyer information
     *   // Creates contract ID in payment info database
     *   // Create merchant customer if not exists
     *   // Creates payment link
     *   // Returns payment link with contract ID and merchant customer ID
     * }
     * ```
     * </pre>
     */
    default void createContractWithoutCharge(com.api.v1.CreateContractWithoutChargeRequest request,
        io.grpc.stub.StreamObserver<com.api.v1.CreateContractWithoutChargeResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCreateContractWithoutChargeMethod(), responseObserver);
    }

    /**
     * <pre>
     * # DEPRECATED: Use OnSessionPayment instead.
     * ## CreateContractWithCharge
     * Creates a new contract with an immediate initial charge. This combines
     * contract creation and charging in a single operation for convenience.
     * This is typically used for initial sign-ups that require immediate payment.
     * **Parameters:**
     * - CreateContractWithChargeRequest: Contains buyer information, redirect details, and charge information
     * **Returns:**
     * - CreateContractWithChargeResponse: Contains the created payment link with contract ID, charge ID, merchant customer ID
     * **Service function reference:**
     * ```go
     * func (s *paymentService) CreateContractWithCharge(ctx context.Context, req *CreateContractWithChargeRequest) (*CreateContractWithChargeResponse, error) {
     *   // Validates buyer and payment information
     *   // Creates contract ID and charge ID in payment info database
     *   // Create merchant customer if not exists
     *   // Creates payment link
     *   // Returns payment link with contract and charge IDs along with merchant customer ID
     * }
     * ```
     * </pre>
     */
    default void createContractWithCharge(com.api.v1.CreateContractWithChargeRequest request,
        io.grpc.stub.StreamObserver<com.api.v1.CreateContractWithChargeResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCreateContractWithChargeMethod(), responseObserver);
    }

    /**
     * <pre>
     * # DEPRECATED: Use OnSessionPayment instead.
     * ## AddCharge
     * Adds a new charge to an existing contract. This allows for charging
     * a jamm user after a contract has been established.
     * Common uses include recurring billing, add-on purchases, or usage-based charges.
     * **Parameters:**
     * - AddChargeRequest: Contains merchant customer ID and charge details
     * **Returns:**
     * - AddChargeResponse: Contains the created payment link with charge ID
     * **Service function reference:**
     * ```go
     * func (s *paymentService) AddCharge(ctx context.Context, req *AddChargeRequest) (*AddChargeResponse, error) {
     *   // Validates contract links to the merchant customer and payment information
     *   // Creates charge ID in payment info database
     *   // Creates payment link
     *   // Returns payment link with contract and charge IDs
     * }
     * ```
     * </pre>
     */
    default void addCharge(com.api.v1.AddChargeRequest request,
        io.grpc.stub.StreamObserver<com.api.v1.AddChargeResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getAddChargeMethod(), responseObserver);
    }

    /**
     */
    default void getCharge(com.api.v1.GetChargeRequest request,
        io.grpc.stub.StreamObserver<com.api.v1.GetChargeResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetChargeMethod(), responseObserver);
    }

    /**
     */
    default void getCharges(com.api.v1.GetChargesRequest request,
        io.grpc.stub.StreamObserver<com.api.v1.GetChargesResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetChargesMethod(), responseObserver);
    }

    /**
     * <pre>
     * DEPRECATED: Use OffSessionPayment instead.
     * Withdraw
     * Withdraws money from a customer immediately. This call is synchronous.
     * &#64;param WithdrawRequest Contains merchant customer ID and charge details
     * &#64;return WithdrawResponse Contains the created payment link with charge ID
     * Service function reference:
     * func (s *paymentService) Withdraw(ctx context.Context, req *WithdrawRequest) (*WithdrawResponse, error) {
     *   // Validates contract links to the merchant customer and payment information
     *   // Creates charge ID in payment info database
     *   // Creates payment link
     *   // Returns payment link with contract and charge IDs
     * }
     * </pre>
     */
    default void withdraw(com.api.v1.WithdrawRequest request,
        io.grpc.stub.StreamObserver<com.api.v1.WithdrawResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getWithdrawMethod(), responseObserver);
    }

    /**
     * <pre>
     * UNDER DEVELOPMENT: For internal use only.
     * WithdrawAsync
     * Initiates a withdrawal asynchronously without redirect.
     * This endpoint is intended for internal workflows and background processing.
     * Not exposed for public SDK usage.
     * </pre>
     */
    default void withdrawAsync(com.api.v1.WithdrawAsyncRequest request,
        io.grpc.stub.StreamObserver<com.api.v1.WithdrawAsyncResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getWithdrawAsyncMethod(), responseObserver);
    }

    /**
     * <pre>
     * ## OffSessionPaymentAsync
     * Initiates an off-session payment asynchronously without redirect.
     * Returns request tracking information for polling progress.
     * </pre>
     */
    default void offSessionPaymentAsync(com.api.v1.OffSessionPaymentAsyncRequest request,
        io.grpc.stub.StreamObserver<com.api.v1.OffSessionPaymentAsyncResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getOffSessionPaymentAsyncMethod(), responseObserver);
    }

    /**
     * <pre>
     * UNDER DEVELOPMENT: For internal use only.
     * WithdrawAsyncStatus
     * Long polling endpoint to fetch async withdraw status.
     * Clients can poll this endpoint using request_id returned from WithdrawAsync.
     * </pre>
     */
    default void withdrawAsyncStatus(com.api.v1.WithdrawAsyncStatusRequest request,
        io.grpc.stub.StreamObserver<com.api.v1.WithdrawAsyncStatusResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getWithdrawAsyncStatusMethod(), responseObserver);
    }

    /**
     * <pre>
     * ## OffSessionPayment
     * Processes a payment directly within your application without requiring redirects.
     * This is an off-session payment that executes immediately and synchronously.
     * This API is used for existing customers - Executes a charge to an existing customer when a customer ID is provided
     * &#64;param OffSessionPaymentRequest: Contains optional customer ID and charge details
     * &#64;return OffSessionPaymentResponse: Contains the charge result and customer information
     * **Service function reference:**
     * ```go
     * func (s *paymentService) Withdraw(ctx context.Context, req *WithdrawRequest) (*WithdrawResponse, error) {
     *   // Validates payment information
     *   // Processes the payment immediately without redirects
     *   // Returns charge result and customer information
     * }
     * ```
     * </pre>
     */
    default void offSessionPayment(com.api.v1.OffSessionPaymentRequest request,
        io.grpc.stub.StreamObserver<com.api.v1.OffSessionPaymentResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getOffSessionPaymentMethod(), responseObserver);
    }

    /**
     * <pre>
     * ## OnSessionPayment
     * Provides a unified interface for creating payment sessions. This API intelligently routes
     * requests to the appropriate method based on the provided parameters:
     * - When a customer ID is provided: Uses AddCharge for existing customers
     * - When buyer + charge are provided: Uses CreateContractWithCharge for new customers with charges
     * - When only buyer is provided: Uses CreateContractWithoutCharge for new customers without charges
     * **Parameters:**
     * - OnSessionPaymentRequest: Contains optional customer/buyer info, optional charge details, and redirect URLs
     * **Returns:**
     * - OnSessionPaymentResponse: Contains unified payment information including payment link
     * **Service function reference:**
     * ```go
     * func (s *paymentService) OnSessionPayment(ctx context.Context, req *OnSessionPaymentRequest) (*OnSessionPaymentResponse, error) {
     *   // Determines appropriate payment strategy based on request parameters
     *   // Routes to AddCharge, CreateContractWithCharge, CreateContractWithoutCharge, or OneTimeCharge
     *   // Returns standardized response with payment link and relevant details
     * }
     * ```
     * </pre>
     */
    default void onSessionPayment(com.api.v1.OnSessionPaymentRequest request,
        io.grpc.stub.StreamObserver<com.api.v1.OnSessionPaymentResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getOnSessionPaymentMethod(), responseObserver);
    }

    /**
     * <pre>
     * ## Refund
     * Refunds a charge. This endpoint is asynchronous — it accepts the refund
     * request and processes it in the background. The final result is delivered via
     * the `refund_succeeded` webhook.
     * Internally, if the same-day (midnight JST from when the charge was created)
     * cancellation window has not passed, the system cancels the charge directly.
     * Otherwise, it creates a bank transfer refund request.
     * **Parameters:**
     * - RefundRequest: Contains the charge ID and optional refund amount
     * **Returns:**
     * - RefundResponse: Contains the charge ID and refund ID for the accepted request
     * </pre>
     */
    default void refund(com.api.v1.RefundRequest request,
        io.grpc.stub.StreamObserver<com.api.v1.RefundResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getRefundMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service PaymentService.
   * <pre>
   * # Payment Service
   * This service provides functionality for managing contracts and charges.
   * It allows for creating contracts both with and without initial charges,
   * as well as adding charges to existing contracts.
   * </pre>
   */
  public static abstract class PaymentServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return PaymentServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service PaymentService.
   * <pre>
   * # Payment Service
   * This service provides functionality for managing contracts and charges.
   * It allows for creating contracts both with and without initial charges,
   * as well as adding charges to existing contracts.
   * </pre>
   */
  public static final class PaymentServiceStub
      extends io.grpc.stub.AbstractAsyncStub<PaymentServiceStub> {
    private PaymentServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected PaymentServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new PaymentServiceStub(channel, callOptions);
    }

    /**
     * <pre>
     * # DEPRECATED: Use OnSessionPayment instead.
     * ## CreateContractWithoutCharge
     * Creates a new contract without an initial charge. This is useful when you want to
     * establish a billing relationship with a customer but don't need to charge them immediately.
     * The contract can later be used with AddCharge to apply charges as needed.
     * **Parameters:**
     * - CreateContractWithoutChargeRequest: Contains buyer information and redirect details
     * **Returns:**
     * - CreateContractWithoutChargeResponse: Contains the created payment link with contract ID, merchant customer ID
     * **Service function reference:**
     * ```go
     * func (s *paymentService) CreateContractWithoutCharge(ctx context.Context, req *CreateContractWithoutChargeRequest) (*CreateContractWithoutChargeResponse, error) {
     *   // Validates buyer information
     *   // Creates contract ID in payment info database
     *   // Create merchant customer if not exists
     *   // Creates payment link
     *   // Returns payment link with contract ID and merchant customer ID
     * }
     * ```
     * </pre>
     */
    public void createContractWithoutCharge(com.api.v1.CreateContractWithoutChargeRequest request,
        io.grpc.stub.StreamObserver<com.api.v1.CreateContractWithoutChargeResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCreateContractWithoutChargeMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * # DEPRECATED: Use OnSessionPayment instead.
     * ## CreateContractWithCharge
     * Creates a new contract with an immediate initial charge. This combines
     * contract creation and charging in a single operation for convenience.
     * This is typically used for initial sign-ups that require immediate payment.
     * **Parameters:**
     * - CreateContractWithChargeRequest: Contains buyer information, redirect details, and charge information
     * **Returns:**
     * - CreateContractWithChargeResponse: Contains the created payment link with contract ID, charge ID, merchant customer ID
     * **Service function reference:**
     * ```go
     * func (s *paymentService) CreateContractWithCharge(ctx context.Context, req *CreateContractWithChargeRequest) (*CreateContractWithChargeResponse, error) {
     *   // Validates buyer and payment information
     *   // Creates contract ID and charge ID in payment info database
     *   // Create merchant customer if not exists
     *   // Creates payment link
     *   // Returns payment link with contract and charge IDs along with merchant customer ID
     * }
     * ```
     * </pre>
     */
    public void createContractWithCharge(com.api.v1.CreateContractWithChargeRequest request,
        io.grpc.stub.StreamObserver<com.api.v1.CreateContractWithChargeResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCreateContractWithChargeMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * # DEPRECATED: Use OnSessionPayment instead.
     * ## AddCharge
     * Adds a new charge to an existing contract. This allows for charging
     * a jamm user after a contract has been established.
     * Common uses include recurring billing, add-on purchases, or usage-based charges.
     * **Parameters:**
     * - AddChargeRequest: Contains merchant customer ID and charge details
     * **Returns:**
     * - AddChargeResponse: Contains the created payment link with charge ID
     * **Service function reference:**
     * ```go
     * func (s *paymentService) AddCharge(ctx context.Context, req *AddChargeRequest) (*AddChargeResponse, error) {
     *   // Validates contract links to the merchant customer and payment information
     *   // Creates charge ID in payment info database
     *   // Creates payment link
     *   // Returns payment link with contract and charge IDs
     * }
     * ```
     * </pre>
     */
    public void addCharge(com.api.v1.AddChargeRequest request,
        io.grpc.stub.StreamObserver<com.api.v1.AddChargeResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getAddChargeMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getCharge(com.api.v1.GetChargeRequest request,
        io.grpc.stub.StreamObserver<com.api.v1.GetChargeResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetChargeMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getCharges(com.api.v1.GetChargesRequest request,
        io.grpc.stub.StreamObserver<com.api.v1.GetChargesResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetChargesMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * DEPRECATED: Use OffSessionPayment instead.
     * Withdraw
     * Withdraws money from a customer immediately. This call is synchronous.
     * &#64;param WithdrawRequest Contains merchant customer ID and charge details
     * &#64;return WithdrawResponse Contains the created payment link with charge ID
     * Service function reference:
     * func (s *paymentService) Withdraw(ctx context.Context, req *WithdrawRequest) (*WithdrawResponse, error) {
     *   // Validates contract links to the merchant customer and payment information
     *   // Creates charge ID in payment info database
     *   // Creates payment link
     *   // Returns payment link with contract and charge IDs
     * }
     * </pre>
     */
    public void withdraw(com.api.v1.WithdrawRequest request,
        io.grpc.stub.StreamObserver<com.api.v1.WithdrawResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getWithdrawMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * UNDER DEVELOPMENT: For internal use only.
     * WithdrawAsync
     * Initiates a withdrawal asynchronously without redirect.
     * This endpoint is intended for internal workflows and background processing.
     * Not exposed for public SDK usage.
     * </pre>
     */
    public void withdrawAsync(com.api.v1.WithdrawAsyncRequest request,
        io.grpc.stub.StreamObserver<com.api.v1.WithdrawAsyncResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getWithdrawAsyncMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * ## OffSessionPaymentAsync
     * Initiates an off-session payment asynchronously without redirect.
     * Returns request tracking information for polling progress.
     * </pre>
     */
    public void offSessionPaymentAsync(com.api.v1.OffSessionPaymentAsyncRequest request,
        io.grpc.stub.StreamObserver<com.api.v1.OffSessionPaymentAsyncResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getOffSessionPaymentAsyncMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * UNDER DEVELOPMENT: For internal use only.
     * WithdrawAsyncStatus
     * Long polling endpoint to fetch async withdraw status.
     * Clients can poll this endpoint using request_id returned from WithdrawAsync.
     * </pre>
     */
    public void withdrawAsyncStatus(com.api.v1.WithdrawAsyncStatusRequest request,
        io.grpc.stub.StreamObserver<com.api.v1.WithdrawAsyncStatusResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getWithdrawAsyncStatusMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * ## OffSessionPayment
     * Processes a payment directly within your application without requiring redirects.
     * This is an off-session payment that executes immediately and synchronously.
     * This API is used for existing customers - Executes a charge to an existing customer when a customer ID is provided
     * &#64;param OffSessionPaymentRequest: Contains optional customer ID and charge details
     * &#64;return OffSessionPaymentResponse: Contains the charge result and customer information
     * **Service function reference:**
     * ```go
     * func (s *paymentService) Withdraw(ctx context.Context, req *WithdrawRequest) (*WithdrawResponse, error) {
     *   // Validates payment information
     *   // Processes the payment immediately without redirects
     *   // Returns charge result and customer information
     * }
     * ```
     * </pre>
     */
    public void offSessionPayment(com.api.v1.OffSessionPaymentRequest request,
        io.grpc.stub.StreamObserver<com.api.v1.OffSessionPaymentResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getOffSessionPaymentMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * ## OnSessionPayment
     * Provides a unified interface for creating payment sessions. This API intelligently routes
     * requests to the appropriate method based on the provided parameters:
     * - When a customer ID is provided: Uses AddCharge for existing customers
     * - When buyer + charge are provided: Uses CreateContractWithCharge for new customers with charges
     * - When only buyer is provided: Uses CreateContractWithoutCharge for new customers without charges
     * **Parameters:**
     * - OnSessionPaymentRequest: Contains optional customer/buyer info, optional charge details, and redirect URLs
     * **Returns:**
     * - OnSessionPaymentResponse: Contains unified payment information including payment link
     * **Service function reference:**
     * ```go
     * func (s *paymentService) OnSessionPayment(ctx context.Context, req *OnSessionPaymentRequest) (*OnSessionPaymentResponse, error) {
     *   // Determines appropriate payment strategy based on request parameters
     *   // Routes to AddCharge, CreateContractWithCharge, CreateContractWithoutCharge, or OneTimeCharge
     *   // Returns standardized response with payment link and relevant details
     * }
     * ```
     * </pre>
     */
    public void onSessionPayment(com.api.v1.OnSessionPaymentRequest request,
        io.grpc.stub.StreamObserver<com.api.v1.OnSessionPaymentResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getOnSessionPaymentMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * ## Refund
     * Refunds a charge. This endpoint is asynchronous — it accepts the refund
     * request and processes it in the background. The final result is delivered via
     * the `refund_succeeded` webhook.
     * Internally, if the same-day (midnight JST from when the charge was created)
     * cancellation window has not passed, the system cancels the charge directly.
     * Otherwise, it creates a bank transfer refund request.
     * **Parameters:**
     * - RefundRequest: Contains the charge ID and optional refund amount
     * **Returns:**
     * - RefundResponse: Contains the charge ID and refund ID for the accepted request
     * </pre>
     */
    public void refund(com.api.v1.RefundRequest request,
        io.grpc.stub.StreamObserver<com.api.v1.RefundResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getRefundMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service PaymentService.
   * <pre>
   * # Payment Service
   * This service provides functionality for managing contracts and charges.
   * It allows for creating contracts both with and without initial charges,
   * as well as adding charges to existing contracts.
   * </pre>
   */
  public static final class PaymentServiceBlockingV2Stub
      extends io.grpc.stub.AbstractBlockingStub<PaymentServiceBlockingV2Stub> {
    private PaymentServiceBlockingV2Stub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected PaymentServiceBlockingV2Stub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new PaymentServiceBlockingV2Stub(channel, callOptions);
    }

    /**
     * <pre>
     * # DEPRECATED: Use OnSessionPayment instead.
     * ## CreateContractWithoutCharge
     * Creates a new contract without an initial charge. This is useful when you want to
     * establish a billing relationship with a customer but don't need to charge them immediately.
     * The contract can later be used with AddCharge to apply charges as needed.
     * **Parameters:**
     * - CreateContractWithoutChargeRequest: Contains buyer information and redirect details
     * **Returns:**
     * - CreateContractWithoutChargeResponse: Contains the created payment link with contract ID, merchant customer ID
     * **Service function reference:**
     * ```go
     * func (s *paymentService) CreateContractWithoutCharge(ctx context.Context, req *CreateContractWithoutChargeRequest) (*CreateContractWithoutChargeResponse, error) {
     *   // Validates buyer information
     *   // Creates contract ID in payment info database
     *   // Create merchant customer if not exists
     *   // Creates payment link
     *   // Returns payment link with contract ID and merchant customer ID
     * }
     * ```
     * </pre>
     */
    public com.api.v1.CreateContractWithoutChargeResponse createContractWithoutCharge(com.api.v1.CreateContractWithoutChargeRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getCreateContractWithoutChargeMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * # DEPRECATED: Use OnSessionPayment instead.
     * ## CreateContractWithCharge
     * Creates a new contract with an immediate initial charge. This combines
     * contract creation and charging in a single operation for convenience.
     * This is typically used for initial sign-ups that require immediate payment.
     * **Parameters:**
     * - CreateContractWithChargeRequest: Contains buyer information, redirect details, and charge information
     * **Returns:**
     * - CreateContractWithChargeResponse: Contains the created payment link with contract ID, charge ID, merchant customer ID
     * **Service function reference:**
     * ```go
     * func (s *paymentService) CreateContractWithCharge(ctx context.Context, req *CreateContractWithChargeRequest) (*CreateContractWithChargeResponse, error) {
     *   // Validates buyer and payment information
     *   // Creates contract ID and charge ID in payment info database
     *   // Create merchant customer if not exists
     *   // Creates payment link
     *   // Returns payment link with contract and charge IDs along with merchant customer ID
     * }
     * ```
     * </pre>
     */
    public com.api.v1.CreateContractWithChargeResponse createContractWithCharge(com.api.v1.CreateContractWithChargeRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getCreateContractWithChargeMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * # DEPRECATED: Use OnSessionPayment instead.
     * ## AddCharge
     * Adds a new charge to an existing contract. This allows for charging
     * a jamm user after a contract has been established.
     * Common uses include recurring billing, add-on purchases, or usage-based charges.
     * **Parameters:**
     * - AddChargeRequest: Contains merchant customer ID and charge details
     * **Returns:**
     * - AddChargeResponse: Contains the created payment link with charge ID
     * **Service function reference:**
     * ```go
     * func (s *paymentService) AddCharge(ctx context.Context, req *AddChargeRequest) (*AddChargeResponse, error) {
     *   // Validates contract links to the merchant customer and payment information
     *   // Creates charge ID in payment info database
     *   // Creates payment link
     *   // Returns payment link with contract and charge IDs
     * }
     * ```
     * </pre>
     */
    public com.api.v1.AddChargeResponse addCharge(com.api.v1.AddChargeRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getAddChargeMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.api.v1.GetChargeResponse getCharge(com.api.v1.GetChargeRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getGetChargeMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.api.v1.GetChargesResponse getCharges(com.api.v1.GetChargesRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getGetChargesMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * DEPRECATED: Use OffSessionPayment instead.
     * Withdraw
     * Withdraws money from a customer immediately. This call is synchronous.
     * &#64;param WithdrawRequest Contains merchant customer ID and charge details
     * &#64;return WithdrawResponse Contains the created payment link with charge ID
     * Service function reference:
     * func (s *paymentService) Withdraw(ctx context.Context, req *WithdrawRequest) (*WithdrawResponse, error) {
     *   // Validates contract links to the merchant customer and payment information
     *   // Creates charge ID in payment info database
     *   // Creates payment link
     *   // Returns payment link with contract and charge IDs
     * }
     * </pre>
     */
    public com.api.v1.WithdrawResponse withdraw(com.api.v1.WithdrawRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getWithdrawMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * UNDER DEVELOPMENT: For internal use only.
     * WithdrawAsync
     * Initiates a withdrawal asynchronously without redirect.
     * This endpoint is intended for internal workflows and background processing.
     * Not exposed for public SDK usage.
     * </pre>
     */
    public com.api.v1.WithdrawAsyncResponse withdrawAsync(com.api.v1.WithdrawAsyncRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getWithdrawAsyncMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * ## OffSessionPaymentAsync
     * Initiates an off-session payment asynchronously without redirect.
     * Returns request tracking information for polling progress.
     * </pre>
     */
    public com.api.v1.OffSessionPaymentAsyncResponse offSessionPaymentAsync(com.api.v1.OffSessionPaymentAsyncRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getOffSessionPaymentAsyncMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * UNDER DEVELOPMENT: For internal use only.
     * WithdrawAsyncStatus
     * Long polling endpoint to fetch async withdraw status.
     * Clients can poll this endpoint using request_id returned from WithdrawAsync.
     * </pre>
     */
    public com.api.v1.WithdrawAsyncStatusResponse withdrawAsyncStatus(com.api.v1.WithdrawAsyncStatusRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getWithdrawAsyncStatusMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * ## OffSessionPayment
     * Processes a payment directly within your application without requiring redirects.
     * This is an off-session payment that executes immediately and synchronously.
     * This API is used for existing customers - Executes a charge to an existing customer when a customer ID is provided
     * &#64;param OffSessionPaymentRequest: Contains optional customer ID and charge details
     * &#64;return OffSessionPaymentResponse: Contains the charge result and customer information
     * **Service function reference:**
     * ```go
     * func (s *paymentService) Withdraw(ctx context.Context, req *WithdrawRequest) (*WithdrawResponse, error) {
     *   // Validates payment information
     *   // Processes the payment immediately without redirects
     *   // Returns charge result and customer information
     * }
     * ```
     * </pre>
     */
    public com.api.v1.OffSessionPaymentResponse offSessionPayment(com.api.v1.OffSessionPaymentRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getOffSessionPaymentMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * ## OnSessionPayment
     * Provides a unified interface for creating payment sessions. This API intelligently routes
     * requests to the appropriate method based on the provided parameters:
     * - When a customer ID is provided: Uses AddCharge for existing customers
     * - When buyer + charge are provided: Uses CreateContractWithCharge for new customers with charges
     * - When only buyer is provided: Uses CreateContractWithoutCharge for new customers without charges
     * **Parameters:**
     * - OnSessionPaymentRequest: Contains optional customer/buyer info, optional charge details, and redirect URLs
     * **Returns:**
     * - OnSessionPaymentResponse: Contains unified payment information including payment link
     * **Service function reference:**
     * ```go
     * func (s *paymentService) OnSessionPayment(ctx context.Context, req *OnSessionPaymentRequest) (*OnSessionPaymentResponse, error) {
     *   // Determines appropriate payment strategy based on request parameters
     *   // Routes to AddCharge, CreateContractWithCharge, CreateContractWithoutCharge, or OneTimeCharge
     *   // Returns standardized response with payment link and relevant details
     * }
     * ```
     * </pre>
     */
    public com.api.v1.OnSessionPaymentResponse onSessionPayment(com.api.v1.OnSessionPaymentRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getOnSessionPaymentMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * ## Refund
     * Refunds a charge. This endpoint is asynchronous — it accepts the refund
     * request and processes it in the background. The final result is delivered via
     * the `refund_succeeded` webhook.
     * Internally, if the same-day (midnight JST from when the charge was created)
     * cancellation window has not passed, the system cancels the charge directly.
     * Otherwise, it creates a bank transfer refund request.
     * **Parameters:**
     * - RefundRequest: Contains the charge ID and optional refund amount
     * **Returns:**
     * - RefundResponse: Contains the charge ID and refund ID for the accepted request
     * </pre>
     */
    public com.api.v1.RefundResponse refund(com.api.v1.RefundRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getRefundMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do limited synchronous rpc calls to service PaymentService.
   * <pre>
   * # Payment Service
   * This service provides functionality for managing contracts and charges.
   * It allows for creating contracts both with and without initial charges,
   * as well as adding charges to existing contracts.
   * </pre>
   */
  public static final class PaymentServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<PaymentServiceBlockingStub> {
    private PaymentServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected PaymentServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new PaymentServiceBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * # DEPRECATED: Use OnSessionPayment instead.
     * ## CreateContractWithoutCharge
     * Creates a new contract without an initial charge. This is useful when you want to
     * establish a billing relationship with a customer but don't need to charge them immediately.
     * The contract can later be used with AddCharge to apply charges as needed.
     * **Parameters:**
     * - CreateContractWithoutChargeRequest: Contains buyer information and redirect details
     * **Returns:**
     * - CreateContractWithoutChargeResponse: Contains the created payment link with contract ID, merchant customer ID
     * **Service function reference:**
     * ```go
     * func (s *paymentService) CreateContractWithoutCharge(ctx context.Context, req *CreateContractWithoutChargeRequest) (*CreateContractWithoutChargeResponse, error) {
     *   // Validates buyer information
     *   // Creates contract ID in payment info database
     *   // Create merchant customer if not exists
     *   // Creates payment link
     *   // Returns payment link with contract ID and merchant customer ID
     * }
     * ```
     * </pre>
     */
    public com.api.v1.CreateContractWithoutChargeResponse createContractWithoutCharge(com.api.v1.CreateContractWithoutChargeRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCreateContractWithoutChargeMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * # DEPRECATED: Use OnSessionPayment instead.
     * ## CreateContractWithCharge
     * Creates a new contract with an immediate initial charge. This combines
     * contract creation and charging in a single operation for convenience.
     * This is typically used for initial sign-ups that require immediate payment.
     * **Parameters:**
     * - CreateContractWithChargeRequest: Contains buyer information, redirect details, and charge information
     * **Returns:**
     * - CreateContractWithChargeResponse: Contains the created payment link with contract ID, charge ID, merchant customer ID
     * **Service function reference:**
     * ```go
     * func (s *paymentService) CreateContractWithCharge(ctx context.Context, req *CreateContractWithChargeRequest) (*CreateContractWithChargeResponse, error) {
     *   // Validates buyer and payment information
     *   // Creates contract ID and charge ID in payment info database
     *   // Create merchant customer if not exists
     *   // Creates payment link
     *   // Returns payment link with contract and charge IDs along with merchant customer ID
     * }
     * ```
     * </pre>
     */
    public com.api.v1.CreateContractWithChargeResponse createContractWithCharge(com.api.v1.CreateContractWithChargeRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCreateContractWithChargeMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * # DEPRECATED: Use OnSessionPayment instead.
     * ## AddCharge
     * Adds a new charge to an existing contract. This allows for charging
     * a jamm user after a contract has been established.
     * Common uses include recurring billing, add-on purchases, or usage-based charges.
     * **Parameters:**
     * - AddChargeRequest: Contains merchant customer ID and charge details
     * **Returns:**
     * - AddChargeResponse: Contains the created payment link with charge ID
     * **Service function reference:**
     * ```go
     * func (s *paymentService) AddCharge(ctx context.Context, req *AddChargeRequest) (*AddChargeResponse, error) {
     *   // Validates contract links to the merchant customer and payment information
     *   // Creates charge ID in payment info database
     *   // Creates payment link
     *   // Returns payment link with contract and charge IDs
     * }
     * ```
     * </pre>
     */
    public com.api.v1.AddChargeResponse addCharge(com.api.v1.AddChargeRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getAddChargeMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.api.v1.GetChargeResponse getCharge(com.api.v1.GetChargeRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetChargeMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.api.v1.GetChargesResponse getCharges(com.api.v1.GetChargesRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetChargesMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * DEPRECATED: Use OffSessionPayment instead.
     * Withdraw
     * Withdraws money from a customer immediately. This call is synchronous.
     * &#64;param WithdrawRequest Contains merchant customer ID and charge details
     * &#64;return WithdrawResponse Contains the created payment link with charge ID
     * Service function reference:
     * func (s *paymentService) Withdraw(ctx context.Context, req *WithdrawRequest) (*WithdrawResponse, error) {
     *   // Validates contract links to the merchant customer and payment information
     *   // Creates charge ID in payment info database
     *   // Creates payment link
     *   // Returns payment link with contract and charge IDs
     * }
     * </pre>
     */
    public com.api.v1.WithdrawResponse withdraw(com.api.v1.WithdrawRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getWithdrawMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * UNDER DEVELOPMENT: For internal use only.
     * WithdrawAsync
     * Initiates a withdrawal asynchronously without redirect.
     * This endpoint is intended for internal workflows and background processing.
     * Not exposed for public SDK usage.
     * </pre>
     */
    public com.api.v1.WithdrawAsyncResponse withdrawAsync(com.api.v1.WithdrawAsyncRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getWithdrawAsyncMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * ## OffSessionPaymentAsync
     * Initiates an off-session payment asynchronously without redirect.
     * Returns request tracking information for polling progress.
     * </pre>
     */
    public com.api.v1.OffSessionPaymentAsyncResponse offSessionPaymentAsync(com.api.v1.OffSessionPaymentAsyncRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getOffSessionPaymentAsyncMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * UNDER DEVELOPMENT: For internal use only.
     * WithdrawAsyncStatus
     * Long polling endpoint to fetch async withdraw status.
     * Clients can poll this endpoint using request_id returned from WithdrawAsync.
     * </pre>
     */
    public com.api.v1.WithdrawAsyncStatusResponse withdrawAsyncStatus(com.api.v1.WithdrawAsyncStatusRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getWithdrawAsyncStatusMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * ## OffSessionPayment
     * Processes a payment directly within your application without requiring redirects.
     * This is an off-session payment that executes immediately and synchronously.
     * This API is used for existing customers - Executes a charge to an existing customer when a customer ID is provided
     * &#64;param OffSessionPaymentRequest: Contains optional customer ID and charge details
     * &#64;return OffSessionPaymentResponse: Contains the charge result and customer information
     * **Service function reference:**
     * ```go
     * func (s *paymentService) Withdraw(ctx context.Context, req *WithdrawRequest) (*WithdrawResponse, error) {
     *   // Validates payment information
     *   // Processes the payment immediately without redirects
     *   // Returns charge result and customer information
     * }
     * ```
     * </pre>
     */
    public com.api.v1.OffSessionPaymentResponse offSessionPayment(com.api.v1.OffSessionPaymentRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getOffSessionPaymentMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * ## OnSessionPayment
     * Provides a unified interface for creating payment sessions. This API intelligently routes
     * requests to the appropriate method based on the provided parameters:
     * - When a customer ID is provided: Uses AddCharge for existing customers
     * - When buyer + charge are provided: Uses CreateContractWithCharge for new customers with charges
     * - When only buyer is provided: Uses CreateContractWithoutCharge for new customers without charges
     * **Parameters:**
     * - OnSessionPaymentRequest: Contains optional customer/buyer info, optional charge details, and redirect URLs
     * **Returns:**
     * - OnSessionPaymentResponse: Contains unified payment information including payment link
     * **Service function reference:**
     * ```go
     * func (s *paymentService) OnSessionPayment(ctx context.Context, req *OnSessionPaymentRequest) (*OnSessionPaymentResponse, error) {
     *   // Determines appropriate payment strategy based on request parameters
     *   // Routes to AddCharge, CreateContractWithCharge, CreateContractWithoutCharge, or OneTimeCharge
     *   // Returns standardized response with payment link and relevant details
     * }
     * ```
     * </pre>
     */
    public com.api.v1.OnSessionPaymentResponse onSessionPayment(com.api.v1.OnSessionPaymentRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getOnSessionPaymentMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * ## Refund
     * Refunds a charge. This endpoint is asynchronous — it accepts the refund
     * request and processes it in the background. The final result is delivered via
     * the `refund_succeeded` webhook.
     * Internally, if the same-day (midnight JST from when the charge was created)
     * cancellation window has not passed, the system cancels the charge directly.
     * Otherwise, it creates a bank transfer refund request.
     * **Parameters:**
     * - RefundRequest: Contains the charge ID and optional refund amount
     * **Returns:**
     * - RefundResponse: Contains the charge ID and refund ID for the accepted request
     * </pre>
     */
    public com.api.v1.RefundResponse refund(com.api.v1.RefundRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getRefundMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service PaymentService.
   * <pre>
   * # Payment Service
   * This service provides functionality for managing contracts and charges.
   * It allows for creating contracts both with and without initial charges,
   * as well as adding charges to existing contracts.
   * </pre>
   */
  public static final class PaymentServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<PaymentServiceFutureStub> {
    private PaymentServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected PaymentServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new PaymentServiceFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * # DEPRECATED: Use OnSessionPayment instead.
     * ## CreateContractWithoutCharge
     * Creates a new contract without an initial charge. This is useful when you want to
     * establish a billing relationship with a customer but don't need to charge them immediately.
     * The contract can later be used with AddCharge to apply charges as needed.
     * **Parameters:**
     * - CreateContractWithoutChargeRequest: Contains buyer information and redirect details
     * **Returns:**
     * - CreateContractWithoutChargeResponse: Contains the created payment link with contract ID, merchant customer ID
     * **Service function reference:**
     * ```go
     * func (s *paymentService) CreateContractWithoutCharge(ctx context.Context, req *CreateContractWithoutChargeRequest) (*CreateContractWithoutChargeResponse, error) {
     *   // Validates buyer information
     *   // Creates contract ID in payment info database
     *   // Create merchant customer if not exists
     *   // Creates payment link
     *   // Returns payment link with contract ID and merchant customer ID
     * }
     * ```
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.api.v1.CreateContractWithoutChargeResponse> createContractWithoutCharge(
        com.api.v1.CreateContractWithoutChargeRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCreateContractWithoutChargeMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * # DEPRECATED: Use OnSessionPayment instead.
     * ## CreateContractWithCharge
     * Creates a new contract with an immediate initial charge. This combines
     * contract creation and charging in a single operation for convenience.
     * This is typically used for initial sign-ups that require immediate payment.
     * **Parameters:**
     * - CreateContractWithChargeRequest: Contains buyer information, redirect details, and charge information
     * **Returns:**
     * - CreateContractWithChargeResponse: Contains the created payment link with contract ID, charge ID, merchant customer ID
     * **Service function reference:**
     * ```go
     * func (s *paymentService) CreateContractWithCharge(ctx context.Context, req *CreateContractWithChargeRequest) (*CreateContractWithChargeResponse, error) {
     *   // Validates buyer and payment information
     *   // Creates contract ID and charge ID in payment info database
     *   // Create merchant customer if not exists
     *   // Creates payment link
     *   // Returns payment link with contract and charge IDs along with merchant customer ID
     * }
     * ```
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.api.v1.CreateContractWithChargeResponse> createContractWithCharge(
        com.api.v1.CreateContractWithChargeRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCreateContractWithChargeMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * # DEPRECATED: Use OnSessionPayment instead.
     * ## AddCharge
     * Adds a new charge to an existing contract. This allows for charging
     * a jamm user after a contract has been established.
     * Common uses include recurring billing, add-on purchases, or usage-based charges.
     * **Parameters:**
     * - AddChargeRequest: Contains merchant customer ID and charge details
     * **Returns:**
     * - AddChargeResponse: Contains the created payment link with charge ID
     * **Service function reference:**
     * ```go
     * func (s *paymentService) AddCharge(ctx context.Context, req *AddChargeRequest) (*AddChargeResponse, error) {
     *   // Validates contract links to the merchant customer and payment information
     *   // Creates charge ID in payment info database
     *   // Creates payment link
     *   // Returns payment link with contract and charge IDs
     * }
     * ```
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.api.v1.AddChargeResponse> addCharge(
        com.api.v1.AddChargeRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getAddChargeMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.api.v1.GetChargeResponse> getCharge(
        com.api.v1.GetChargeRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetChargeMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.api.v1.GetChargesResponse> getCharges(
        com.api.v1.GetChargesRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetChargesMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * DEPRECATED: Use OffSessionPayment instead.
     * Withdraw
     * Withdraws money from a customer immediately. This call is synchronous.
     * &#64;param WithdrawRequest Contains merchant customer ID and charge details
     * &#64;return WithdrawResponse Contains the created payment link with charge ID
     * Service function reference:
     * func (s *paymentService) Withdraw(ctx context.Context, req *WithdrawRequest) (*WithdrawResponse, error) {
     *   // Validates contract links to the merchant customer and payment information
     *   // Creates charge ID in payment info database
     *   // Creates payment link
     *   // Returns payment link with contract and charge IDs
     * }
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.api.v1.WithdrawResponse> withdraw(
        com.api.v1.WithdrawRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getWithdrawMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * UNDER DEVELOPMENT: For internal use only.
     * WithdrawAsync
     * Initiates a withdrawal asynchronously without redirect.
     * This endpoint is intended for internal workflows and background processing.
     * Not exposed for public SDK usage.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.api.v1.WithdrawAsyncResponse> withdrawAsync(
        com.api.v1.WithdrawAsyncRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getWithdrawAsyncMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * ## OffSessionPaymentAsync
     * Initiates an off-session payment asynchronously without redirect.
     * Returns request tracking information for polling progress.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.api.v1.OffSessionPaymentAsyncResponse> offSessionPaymentAsync(
        com.api.v1.OffSessionPaymentAsyncRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getOffSessionPaymentAsyncMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * UNDER DEVELOPMENT: For internal use only.
     * WithdrawAsyncStatus
     * Long polling endpoint to fetch async withdraw status.
     * Clients can poll this endpoint using request_id returned from WithdrawAsync.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.api.v1.WithdrawAsyncStatusResponse> withdrawAsyncStatus(
        com.api.v1.WithdrawAsyncStatusRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getWithdrawAsyncStatusMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * ## OffSessionPayment
     * Processes a payment directly within your application without requiring redirects.
     * This is an off-session payment that executes immediately and synchronously.
     * This API is used for existing customers - Executes a charge to an existing customer when a customer ID is provided
     * &#64;param OffSessionPaymentRequest: Contains optional customer ID and charge details
     * &#64;return OffSessionPaymentResponse: Contains the charge result and customer information
     * **Service function reference:**
     * ```go
     * func (s *paymentService) Withdraw(ctx context.Context, req *WithdrawRequest) (*WithdrawResponse, error) {
     *   // Validates payment information
     *   // Processes the payment immediately without redirects
     *   // Returns charge result and customer information
     * }
     * ```
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.api.v1.OffSessionPaymentResponse> offSessionPayment(
        com.api.v1.OffSessionPaymentRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getOffSessionPaymentMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * ## OnSessionPayment
     * Provides a unified interface for creating payment sessions. This API intelligently routes
     * requests to the appropriate method based on the provided parameters:
     * - When a customer ID is provided: Uses AddCharge for existing customers
     * - When buyer + charge are provided: Uses CreateContractWithCharge for new customers with charges
     * - When only buyer is provided: Uses CreateContractWithoutCharge for new customers without charges
     * **Parameters:**
     * - OnSessionPaymentRequest: Contains optional customer/buyer info, optional charge details, and redirect URLs
     * **Returns:**
     * - OnSessionPaymentResponse: Contains unified payment information including payment link
     * **Service function reference:**
     * ```go
     * func (s *paymentService) OnSessionPayment(ctx context.Context, req *OnSessionPaymentRequest) (*OnSessionPaymentResponse, error) {
     *   // Determines appropriate payment strategy based on request parameters
     *   // Routes to AddCharge, CreateContractWithCharge, CreateContractWithoutCharge, or OneTimeCharge
     *   // Returns standardized response with payment link and relevant details
     * }
     * ```
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.api.v1.OnSessionPaymentResponse> onSessionPayment(
        com.api.v1.OnSessionPaymentRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getOnSessionPaymentMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * ## Refund
     * Refunds a charge. This endpoint is asynchronous — it accepts the refund
     * request and processes it in the background. The final result is delivered via
     * the `refund_succeeded` webhook.
     * Internally, if the same-day (midnight JST from when the charge was created)
     * cancellation window has not passed, the system cancels the charge directly.
     * Otherwise, it creates a bank transfer refund request.
     * **Parameters:**
     * - RefundRequest: Contains the charge ID and optional refund amount
     * **Returns:**
     * - RefundResponse: Contains the charge ID and refund ID for the accepted request
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.api.v1.RefundResponse> refund(
        com.api.v1.RefundRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getRefundMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_CREATE_CONTRACT_WITHOUT_CHARGE = 0;
  private static final int METHODID_CREATE_CONTRACT_WITH_CHARGE = 1;
  private static final int METHODID_ADD_CHARGE = 2;
  private static final int METHODID_GET_CHARGE = 3;
  private static final int METHODID_GET_CHARGES = 4;
  private static final int METHODID_WITHDRAW = 5;
  private static final int METHODID_WITHDRAW_ASYNC = 6;
  private static final int METHODID_OFF_SESSION_PAYMENT_ASYNC = 7;
  private static final int METHODID_WITHDRAW_ASYNC_STATUS = 8;
  private static final int METHODID_OFF_SESSION_PAYMENT = 9;
  private static final int METHODID_ON_SESSION_PAYMENT = 10;
  private static final int METHODID_REFUND = 11;

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
        case METHODID_CREATE_CONTRACT_WITHOUT_CHARGE:
          serviceImpl.createContractWithoutCharge((com.api.v1.CreateContractWithoutChargeRequest) request,
              (io.grpc.stub.StreamObserver<com.api.v1.CreateContractWithoutChargeResponse>) responseObserver);
          break;
        case METHODID_CREATE_CONTRACT_WITH_CHARGE:
          serviceImpl.createContractWithCharge((com.api.v1.CreateContractWithChargeRequest) request,
              (io.grpc.stub.StreamObserver<com.api.v1.CreateContractWithChargeResponse>) responseObserver);
          break;
        case METHODID_ADD_CHARGE:
          serviceImpl.addCharge((com.api.v1.AddChargeRequest) request,
              (io.grpc.stub.StreamObserver<com.api.v1.AddChargeResponse>) responseObserver);
          break;
        case METHODID_GET_CHARGE:
          serviceImpl.getCharge((com.api.v1.GetChargeRequest) request,
              (io.grpc.stub.StreamObserver<com.api.v1.GetChargeResponse>) responseObserver);
          break;
        case METHODID_GET_CHARGES:
          serviceImpl.getCharges((com.api.v1.GetChargesRequest) request,
              (io.grpc.stub.StreamObserver<com.api.v1.GetChargesResponse>) responseObserver);
          break;
        case METHODID_WITHDRAW:
          serviceImpl.withdraw((com.api.v1.WithdrawRequest) request,
              (io.grpc.stub.StreamObserver<com.api.v1.WithdrawResponse>) responseObserver);
          break;
        case METHODID_WITHDRAW_ASYNC:
          serviceImpl.withdrawAsync((com.api.v1.WithdrawAsyncRequest) request,
              (io.grpc.stub.StreamObserver<com.api.v1.WithdrawAsyncResponse>) responseObserver);
          break;
        case METHODID_OFF_SESSION_PAYMENT_ASYNC:
          serviceImpl.offSessionPaymentAsync((com.api.v1.OffSessionPaymentAsyncRequest) request,
              (io.grpc.stub.StreamObserver<com.api.v1.OffSessionPaymentAsyncResponse>) responseObserver);
          break;
        case METHODID_WITHDRAW_ASYNC_STATUS:
          serviceImpl.withdrawAsyncStatus((com.api.v1.WithdrawAsyncStatusRequest) request,
              (io.grpc.stub.StreamObserver<com.api.v1.WithdrawAsyncStatusResponse>) responseObserver);
          break;
        case METHODID_OFF_SESSION_PAYMENT:
          serviceImpl.offSessionPayment((com.api.v1.OffSessionPaymentRequest) request,
              (io.grpc.stub.StreamObserver<com.api.v1.OffSessionPaymentResponse>) responseObserver);
          break;
        case METHODID_ON_SESSION_PAYMENT:
          serviceImpl.onSessionPayment((com.api.v1.OnSessionPaymentRequest) request,
              (io.grpc.stub.StreamObserver<com.api.v1.OnSessionPaymentResponse>) responseObserver);
          break;
        case METHODID_REFUND:
          serviceImpl.refund((com.api.v1.RefundRequest) request,
              (io.grpc.stub.StreamObserver<com.api.v1.RefundResponse>) responseObserver);
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
          getCreateContractWithoutChargeMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.api.v1.CreateContractWithoutChargeRequest,
              com.api.v1.CreateContractWithoutChargeResponse>(
                service, METHODID_CREATE_CONTRACT_WITHOUT_CHARGE)))
        .addMethod(
          getCreateContractWithChargeMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.api.v1.CreateContractWithChargeRequest,
              com.api.v1.CreateContractWithChargeResponse>(
                service, METHODID_CREATE_CONTRACT_WITH_CHARGE)))
        .addMethod(
          getAddChargeMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.api.v1.AddChargeRequest,
              com.api.v1.AddChargeResponse>(
                service, METHODID_ADD_CHARGE)))
        .addMethod(
          getGetChargeMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.api.v1.GetChargeRequest,
              com.api.v1.GetChargeResponse>(
                service, METHODID_GET_CHARGE)))
        .addMethod(
          getGetChargesMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.api.v1.GetChargesRequest,
              com.api.v1.GetChargesResponse>(
                service, METHODID_GET_CHARGES)))
        .addMethod(
          getWithdrawMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.api.v1.WithdrawRequest,
              com.api.v1.WithdrawResponse>(
                service, METHODID_WITHDRAW)))
        .addMethod(
          getWithdrawAsyncMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.api.v1.WithdrawAsyncRequest,
              com.api.v1.WithdrawAsyncResponse>(
                service, METHODID_WITHDRAW_ASYNC)))
        .addMethod(
          getOffSessionPaymentAsyncMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.api.v1.OffSessionPaymentAsyncRequest,
              com.api.v1.OffSessionPaymentAsyncResponse>(
                service, METHODID_OFF_SESSION_PAYMENT_ASYNC)))
        .addMethod(
          getWithdrawAsyncStatusMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.api.v1.WithdrawAsyncStatusRequest,
              com.api.v1.WithdrawAsyncStatusResponse>(
                service, METHODID_WITHDRAW_ASYNC_STATUS)))
        .addMethod(
          getOffSessionPaymentMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.api.v1.OffSessionPaymentRequest,
              com.api.v1.OffSessionPaymentResponse>(
                service, METHODID_OFF_SESSION_PAYMENT)))
        .addMethod(
          getOnSessionPaymentMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.api.v1.OnSessionPaymentRequest,
              com.api.v1.OnSessionPaymentResponse>(
                service, METHODID_ON_SESSION_PAYMENT)))
        .addMethod(
          getRefundMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.api.v1.RefundRequest,
              com.api.v1.RefundResponse>(
                service, METHODID_REFUND)))
        .build();
  }

  private static abstract class PaymentServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    PaymentServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.api.v1.PaymentProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("PaymentService");
    }
  }

  private static final class PaymentServiceFileDescriptorSupplier
      extends PaymentServiceBaseDescriptorSupplier {
    PaymentServiceFileDescriptorSupplier() {}
  }

  private static final class PaymentServiceMethodDescriptorSupplier
      extends PaymentServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    PaymentServiceMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (PaymentServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new PaymentServiceFileDescriptorSupplier())
              .addMethod(getCreateContractWithoutChargeMethod())
              .addMethod(getCreateContractWithChargeMethod())
              .addMethod(getAddChargeMethod())
              .addMethod(getGetChargeMethod())
              .addMethod(getGetChargesMethod())
              .addMethod(getWithdrawMethod())
              .addMethod(getWithdrawAsyncMethod())
              .addMethod(getOffSessionPaymentAsyncMethod())
              .addMethod(getWithdrawAsyncStatusMethod())
              .addMethod(getOffSessionPaymentMethod())
              .addMethod(getOnSessionPaymentMethod())
              .addMethod(getRefundMethod())
              .build();
        }
      }
    }
    return result;
  }
}
