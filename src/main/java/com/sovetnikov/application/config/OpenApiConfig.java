package com.sovetnikov.application.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
        name = "basicAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "basic"
)
@OpenAPIDefinition(
        info = @Info(
                title = "REST API documentation",
                version = "1.0",
                description = """
                        Excursion Application.
                        <p><b>Test credentials:</b><br>
                        - user@yandex.ru / password<br>
                        - admin@mail.ru / password<br>
                        Приложение для работы с экскурсиями.
                        Приложение поддерживает работы по ролям: обычные пользователи и администраторы.
                        Каждый пользователь может оставлять комментарии и лайки, удалять их, просматривать комментарии и лайки к экскурсиям.
                        Администратор имеет расширенный функционал, например, определить лучшую экскурсию.
                        На экскурсию можно записаться, узнать информацию о ней и многое другое.
                        Вход в приложение осуществляется по эл.почте и паролю. При создании нового пользователя пароль шифруется.
                        То, что может делать обычный пользователь доступно и администратору, но не наоборот.
                        При недостатке прав ответ 403.
                        Более подробно о доступных функциях в их описании.
                        """,
                contact = @Contact(url = "https://github.com/Blynchik", name = "Vadim Sovetnikov", email = "vadimsovetnikov@mail.ru")
        ),
        security = @SecurityRequirement(name = "basicAuth")
)
public class OpenApiConfig {

    @Bean
    public GroupedOpenApi api() {
        return GroupedOpenApi.builder()
                .group("REST API")
                .pathsToMatch("/api/**")
                .build();
    }
}