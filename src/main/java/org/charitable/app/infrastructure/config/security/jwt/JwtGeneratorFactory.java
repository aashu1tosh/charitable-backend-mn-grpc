//package org.charitable.app.infrastructure.config.security.jwt;
//
//import io.micronaut.context.annotation.Primary;
//import io.micronaut.security.token.jwt.encryption.EncryptionConfiguration;
//import io.micronaut.security.token.jwt.generator.JwtTokenGenerator;
//import io.micronaut.security.token.jwt.signature.SignatureConfiguration;
//import io.micronaut.security.token.jwt.signature.SignatureGeneratorConfiguration;
//import jakarta.inject.Named;
//import jakarta.inject.Singleton;
//import io.micronaut.context.annotation.Bean;
//import io.micronaut.context.annotation.Factory;
//
//@Factory
//public class JwtGeneratorFactory {
//
//    @Bean
//    @Singleton
//    @Primary
//    @Named("access")
//    public JwtTokenGenerator accessTokenGenerator(SignatureGeneratorConfiguration accessSignature,
//                                                  EncryptionConfiguration configuration) {
//        return new JwtTokenGenerator(accessSignature, configuration, null);
//    }
//
//    @Bean
//    @Singleton
//    @Named("refresh")
//    public JwtTokenGenerator refreshTokenGenerator(SignatureGeneratorConfiguration refreshSignature,
//                                                   EncryptionConfiguration configuration) {
//        return new JwtTokenGenerator(refreshSignature, configuration, null);
//    }
//
//}
