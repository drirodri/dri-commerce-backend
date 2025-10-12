# DRI Commerce - Backend

Projeto de estudos de uma API REST para e-commerce utilizando Clean Architecture e Domain-Driven Design (DDD).

## Stack Tecnológica

- **Java 21** - Linguagem principal
- **Quarkus 3.28.2** - Framework web
- **MongoDB 8.0** - Banco de dados NoSQL
- **SmallRye JWT** - Autenticação JWT com RSA-256
- **BCrypt** - Hash de senhas
- **Maven** - Gerenciamento de dependências
- **Docker** - Containerização

## Funcionalidades Implementadas

### v0.3.0 - Autenticação e Autorização

- Autenticação JWT com access e refresh tokens
- Rate limiting configurável por anotação
- Autorização baseada em roles (ADMIN, CUSTOMER, SELLER)
- Soft delete de usuários
- Endpoints protegidos com @RolesAllowed

### v0.2.0 - User Service

- CRUD completo de usuários
- Validação de email duplicado
- Hash de senha com BCrypt
- Paginação em listagem
- Exception handling centralizado

### v0.1.0 - Setup Inicial

- Clean Architecture + DDD
- Value Objects (UserId, UserEmail, UserPassword)
- MongoDB configurado
- Docker Compose

## Como Executar

### Usando Docker (Recomendado)

```bash
docker-compose up --build
```

Acesse: http://localhost:8080

### Modo Desenvolvimento (Local)

```bash
./mvnw quarkus:dev
```

Dev UI: http://localhost:8080/q/dev

## Endpoints Principais

### Autenticacao

- `POST /api/v1/auth/login` - Login (rate limit: 5 tentativas/15min)

- `POST /api/v1/auth/refresh` - Renovar token

- `GET /api/v1/auth/me` - Dados do usuario autenticado.

### Usuarios

- `POST /api/v1/users` - Criar usuario (publico)

- `GET /api/v1/users` - Listar usuarios (ADMIN)

- `GET /api/v1/users/{id}` - Buscar usuario (ADMIN ou proprio usuario)

- `PUT /api/v1/users/{id}` - Atualizar usuario (ADMIN ou proprio usuario)

- `DELETE /api/v1/users/{id}` - Desativar usuario (ADMIN)

- `POST /api/v1/users/{id}/activate` - Reativar usuario (ADMIN)

### Health & Docs

- `GET /health` - Status da aplicacao

- `GET /docs` - Swagger UI

- `GET /q/health` - Health checks detalhados

## Testando a API

### Postman

Importe os arquivos:

- `postman_collection.json` - Collection completa
- `postman_environment.json` - Variáveis de ambiente

### cURL

```bash
# Login
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@admin.com","password":"admin123"}'

# Listar usuários (com token)
curl -X GET http://localhost:8080/api/v1/users \
  -H "Authorization: Bearer {token}"
```

## Estrutura do Projeto

```
src/main/java/dri/commerce/
├── auth/                           # Módulo de autenticação
│   ├── application/usecase/        # Casos de uso
│   ├── domain/                     # Regras de negócio
│   │   ├── exception/              # Exceções de domínio
│   │   └── service/                # Serviços de domínio
│   └── presentation/               # Camada de apresentação
│       ├── annotation/             # Anotações customizadas
│       ├── controller/             # Controllers REST
│       ├── dto/                    # DTOs
│       └── filter/                 # Filtros JAX-RS
├── user/                           # Módulo de usuários
│   ├── application/usecase/        # Casos de uso
│   ├── domain/                     # Entidades e value objects
│   ├── infrastructure/             # Persistência
│   └── presentation/               # API REST
└── config/                         # Configurações gerais
```

## Configuração

### application.properties

```properties
# MongoDB
quarkus.mongodb.connection-string=mongodb://localhost:27018
quarkus.mongodb.database=dri-commerce

# JWT
mp.jwt.verify.publickey.location=/META-INF/publicKey.pem
mp.jwt.verify.issuer=dri-commerce
jwt.token.ttl=3600
jwt.refresh.ttl=604800

# Rate Limiting
rate-limit.login.max-attempts=5
rate-limit.login.window-minutes=15
```

## Estrutura do Projeto

```
src/main/java/dri/commerce/
├── auth/                           # Modulo de autenticacao- [SmallRye JWT Guide](https://quarkus.io/guides/security-jwt)

│   ├── application/usecase/        # Casos de uso- [Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)

│   ├── domain/                     # Regras de negocio- [Domain-Driven Design](https://martinfowler.com/bliki/DomainDrivenDesign.html)

│   │   ├── exception/              # Excecoes de dominio

│   │   └── service/                # Servicos de dominio## Licenca

│   └── presentation/               # Camada de apresentacao

│       ├── annotation/             # Anotacoes customizadasProjeto de estudos - Uso educacional

│       ├── controller/             # Controllers REST
│       ├── dto/                    # DTOs
│       └── filter/                 # Filtros JAX-RS
├── user/                           # Modulo de usuarios
│   ├── application/usecase/        # Casos de uso
│   ├── domain/                     # Entidades e value objects
│   ├── infrastructure/             # Persistencia
│   └── presentation/               # API REST
└── config/                         # Configuracoes gerais
```

## Configuracao

### application.properties

```properties
# MongoDB
quarkus.mongodb.connection-string=mongodb://localhost:27018
quarkus.mongodb.database=dri-commerce

# JWT
mp.jwt.verify.publickey.location=/META-INF/publicKey.pem
mp.jwt.verify.issuer=dri-commerce
jwt.token.ttl=3600
jwt.refresh.ttl=604800

# Rate Limiting
rate-limit.login.max-attempts=5
rate-limit.login.window-minutes=15
```

## Proximos Passos

- Product Microservice
- Cart Microservice
- Admin CLI
- Email verification
- Password reset
- User profile endpoints

## Licenca

Projeto de estudos - Uso educacional
