# BookEase

BookEase é uma plataforma de gerenciamento de agendamentos e serviços.

## Funcionalidades
- Cadastro e autenticação de usuários
- Perfis para Clientes e Profissionais
- Gerenciamento de usuários por Administradores (Ativação/Desativação)
- Atualização de perfil e senha

## Tecnologias
- Java 17
- Spring Boot 3.x
- Spring Security (JWT)
- Spring Data JPA
- PostgreSQL / Flyway
- MapStruct

## Como executar
1. Certifique-se de ter o Docker e Docker Compose instalados.
2. Execute o comando: `docker-compose up -d`
3. O projeto utiliza o Maven, então você pode rodar com: `./mvnw spring-boot:run`

## Testes
Para rodar os testes unitários e de integração:
```bash
./mvnw test
```
