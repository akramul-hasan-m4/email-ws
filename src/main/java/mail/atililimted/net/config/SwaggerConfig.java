package mail.atililimted.net.config;

import static com.google.common.collect.Lists.newArrayList;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.RequestMethod;


import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.GrantType;
import springfox.documentation.service.OAuth;
import springfox.documentation.service.ResourceOwnerPasswordCredentialsGrant;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Akramul
 * @since 13 NOV 2019
 * @version 1.0.0
 */

@Configuration
@EnableSwagger2
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfig {

	private static final String RESULT = "string";
	private static final String ATILIMITED_NET = "http://atilimited.net/";

	@Value("${basic.auth.client.id}")
	private String clientId;

	@Value("${basic.auth.client.secret}")
	private String clientSecret;
	
	@Bean
	public Docket api() {
		List<ResponseMessage> list = new java.util.ArrayList<>();
		list.add(new ResponseMessageBuilder().code(500).message("Internal Server Error").responseModel(new ModelRef(RESULT)).build());
		
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.basePackage("mail.atililimted.net.controller"))
				.paths(PathSelectors.any())
				.build()
				.securitySchemes(Collections.singletonList(securitySchema()))
				.securityContexts(Collections.singletonList(securityContext()))
				.useDefaultResponseMessages(false)
				.apiInfo(apiInfo())
				.globalResponseMessage(RequestMethod.GET, list)
				.globalResponseMessage(RequestMethod.POST, list);

	}

	private OAuth securitySchema() {

		List<AuthorizationScope> authorizationScopeList = newArrayList();
		authorizationScopeList.add(new AuthorizationScope("read", "read all"));
		authorizationScopeList.add(new AuthorizationScope("trust", "trust all"));
		authorizationScopeList.add(new AuthorizationScope("write", "access all"));

		List<GrantType> grantTypes = newArrayList();
		GrantType creGrant = new ResourceOwnerPasswordCredentialsGrant("/ati-email-ws/oauth/token");

		grantTypes.add(creGrant);

		return new OAuth("oauth2schema", authorizationScopeList, grantTypes);

	}

	private SecurityContext securityContext() {
		return SecurityContext.builder().securityReferences(defaultAuth()).forPaths(PathSelectors.ant("/**")).build();
	}

	private List<SecurityReference> defaultAuth() {

		final AuthorizationScope[] authorizationScopes = new AuthorizationScope[3];
		authorizationScopes[0] = new AuthorizationScope("read", "read all");
		authorizationScopes[1] = new AuthorizationScope("trust", "trust all");
		authorizationScopes[2] = new AuthorizationScope("write", "write all");

		return Collections.singletonList(new SecurityReference("oauth2schema", authorizationScopes));
	}

	@Bean
	public SecurityConfiguration securityInfo() {
		return SecurityConfigurationBuilder.builder().clientId(clientId).clientSecret(clientSecret).scopeSeparator(" ")
				.useBasicAuthenticationWithAccessCodeGrant(true).build();

	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("ATI Mail Client")
				.description("ATI Mail API for Drug International Ltd. And It's Sister Concern")
				.termsOfServiceUrl(ATILIMITED_NET)
				.contact(new Contact("Akramul Hasan", ATILIMITED_NET, "akramul@atilimited.net")).license("Apache 2.0")
				.licenseUrl(ATILIMITED_NET).version(getClass().getPackage().getImplementationVersion()).build();
	}

}
