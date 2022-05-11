package com.guanghu.config;


import com.linecorp.armeria.common.grpc.GrpcMeterIdPrefixFunction;
import com.linecorp.armeria.server.docs.DocService;
import com.linecorp.armeria.server.grpc.GrpcService;
import com.linecorp.armeria.server.logging.LoggingService;
import com.linecorp.armeria.server.metric.MetricCollectingService;
import com.linecorp.armeria.server.metric.PrometheusExpositionService;
import com.linecorp.armeria.spring.ArmeriaServerConfigurator;
import io.micrometer.core.instrument.MeterRegistry;
import io.prometheus.client.CollectorRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.springframework.context.annotation.Configuration;
import test.HelloServiceGrpc;

@Configuration
public class ArmeriaConfiguration {

//  @Bean
//  PrometheusMeterRegistry getPrometheusMeterRegistry() {
//    return new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
//  }

  @Bean
  public ArmeriaServerConfigurator armeriaServerConfigurator(
      HelloServiceGrpc.HelloServiceImplBase myServiceImplBase
      //, PrometheusMeterRegistry registry
  ) {
    final PrometheusMeterRegistry registry
        = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
    return builder -> {
      builder.decorator(LoggingService.newDecorator());
      builder.serviceUnder("/docs", new DocService());
      builder.service(GrpcService.builder().addService(myServiceImplBase)
          .enableHttpJsonTranscoding(true)
          .build(), MetricCollectingService.newDecorator(
              GrpcMeterIdPrefixFunction.of("grpc.service")));
      builder.meterRegistry(registry);
      builder.service("/metrics",
          PrometheusExpositionService.of(registry.getPrometheusRegistry()));
    };
  }

}
