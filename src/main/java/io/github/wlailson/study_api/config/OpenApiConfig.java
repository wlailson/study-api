package io.github.wlailson.study_api.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title("Study Manager API")
                        .version("1.0.0")
                        .description("""
                                API para gerenciamento de estudos.
                                
                                ## Usuário de teste
                                
                                ### CLIENT
                                - **Username:** `maria@gmail.com`
                                - **Password:** `123456`
                                
                                Essas credenciais são destinadas exclusivamente aos testes da API.
                                
                                ## Autenticação
                                
                                Esta API utiliza OAuth2 para autenticação através do Swagger UI.
                                
                                O Swagger utiliza o cliente OAuth2 configurado no ambiente:
                                
                                - **Client ID:** `myclientid`
                                - **Client Secret:** `myclientsecret`
                                
                                ### Como autenticar
                                
                                1. Clique em **Authorize**.
                                2. Informe o username e password do usuário de teste.
                                3. O client ID e client secret são configurados automaticamente pelo Swagger UI.
                                4. Clique em **Authorize** para obter o JWT.
                                5. Após a autenticação, os endpoints protegidos poderão ser executados diretamente pelo Swagger UI.
                                """))

                .components(
                        new Components()
                                .addSecuritySchemes(
                                        "oauth2",
                                        new SecurityScheme()
                                                .type(SecurityScheme.Type.OAUTH2)
                                                .flows(
                                                        new OAuthFlows()
                                                                .password(
                                                                        new OAuthFlow()
                                                                                .tokenUrl("/oauth2/token")
                                                                                .scopes(
                                                                                        new Scopes()
                                                                                                .addString(
                                                                                                        "read",
                                                                                                        "Permissão de leitura"
                                                                                                )
                                                                                                .addString(
                                                                                                        "write",
                                                                                                        "Permissão de escrita"
                                                                                                )
                                                                                )
                                                                )
                                                )
                                )
                )

                .addSecurityItem(
                        new SecurityRequirement()
                                .addList("oauth2")
                );
    }
}