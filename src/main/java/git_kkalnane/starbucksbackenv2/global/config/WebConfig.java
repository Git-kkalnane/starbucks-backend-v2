package git_kkalnane.starbucksbackenv2.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final MemberAuthInterceptor memberAuthInterceptor;
    private final MerchantAuthInterceptor merchantAuthInterceptor;

    @Value("${cors.allowed-origins}")
    private String allowedOrigins;

    @Value("${cors.allowed-methods}")
    private String allowedMethods;

    @Value("${cors.allowed-headers}")
    private String allowedHeaders;

    @Value("${cors.max-age}")
    private int maxAge;

    /**
     * AuthInterceptor를 적용 또는 제외할 URI를 설정하는 메서드
     *
     * @param registry {@link InterceptorRegistry}
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        final String[] SWAGGER_PATH = {"/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**"};

        registry.addInterceptor(memberAuthInterceptor).addPathPatterns("/**")
                        .excludePathPatterns(SWAGGER_PATH) // Swagger 관련 모든 경로 제외
                        .excludePathPatterns("/actuator/**")
                        .excludePathPatterns("/items/**", "/stores/**") // item과 매장 정보 GET 관련 제외
                        .excludePathPatterns("/members/signup", "/auth/login") // 고객 회원가입, 로그인 엔드포인트
                                                                               // 제외
                        .excludePathPatterns("/merchant/**");

        registry.addInterceptor(merchantAuthInterceptor).addPathPatterns("/merchant/**")
                        .excludePathPatterns("/merchant/signup", "/merchant/auth/login") // 매장 회원가입,
                                                                                         // 로그인
                                                                                         // 엔드포인트 제외
        ;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOriginPatterns("*")
                        .allowedMethods(allowedMethods.split(",")).allowedHeaders(allowedHeaders)
                        .exposedHeaders("Authorization", "Set-Cookie").allowCredentials(true)
                        .maxAge(maxAge);
    }
}
