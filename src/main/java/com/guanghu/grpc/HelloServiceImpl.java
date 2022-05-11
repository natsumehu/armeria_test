package com.guanghu.grpc;

import io.grpc.stub.StreamObserver;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import test.HelloServiceGrpc.HelloServiceImplBase;
import test.Test;

@Component
public class HelloServiceImpl extends HelloServiceImplBase {

  @Override
  public void getHello(Test.HelloRequest request,
                       StreamObserver<Test.HelloReply> responseObserver) {
    responseObserver.onNext(Test.HelloReply.getDefaultInstance());
    responseObserver.onCompleted();
  }

}
