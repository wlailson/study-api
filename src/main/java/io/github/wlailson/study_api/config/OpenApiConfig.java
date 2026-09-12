package io.github.wlailson.study_api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Study API",
                version = "1.0",
                description = """
                        API para gerenciamento de estudos.
                        
                        Usuário para testes:
                        E-mail: maria@gmail.com
                        Senha: 123456
                        
                        Para acessar endpoints protegidos:
                        1. Faça login em POST /users/login.
                        2. Copie o token JWT retornado.
                        3. Clique em "Authorize" e informe o token.
                        """
        )
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class OpenApiConfig {
}