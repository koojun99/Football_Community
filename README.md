# Football Community Backend

축구 커뮤니티 플랫폼의 백엔드 애플리케이션입니다. 실시간 경기 정보, 위키, 댓글, 회원 관리 등의 기능을 제공합니다.

## 📋 목차

- [기술 스택](#기술-스택)
- [주요 기능](#주요-기능)
- [프로젝트 구조](#프로젝트-구조)
- [실행 방법](#실행-방법)
- [환경 변수 설정](#환경-변수-설정)
- [API 엔드포인트](#api-엔드포인트)
- [데이터베이스](#데이터베이스)
- [Swagger 문서](#swagger-문서)

## 기술 스택

- **Java**: 17
- **Spring Boot**: 3.3.2
- **Spring Data JPA**: 데이터베이스 접근
- **Spring Security**: 인증/인가
- **JWT**: 토큰 기반 인증
- **MySQL**: 관계형 데이터베이스
- **Redis**: 캐싱 및 세션 관리
- **QueryDSL**: 타입 안전한 쿼리 작성
- **OkHttp**: 외부 API 호출 (api-football.com)
- **SpringDoc OpenAPI**: API 문서화 (Swagger)
- **HikariCP**: 데이터베이스 연결 풀
- **Gradle**: 빌드 도구

## 주요 기능

### 1. 인증 및 회원 관리

- 이메일 기반 회원가입/로그인
- JWT 토큰 기반 인증 (Access Token, Refresh Token)
- 회원 프로필 조회 및 수정
- 회원 탈퇴
- 회원 차단(Blind) 기능

### 2. 경기 정보 관리

- 외부 API (api-football.com)를 통한 실시간 경기 정보 조회
- 팀별/리그별 경기 일정 조회
- 오늘 기준 예정된 경기 조회 (`/fixtures/upcoming`)
- 과거 경기 기록 조회 (`/fixtures/past`)
- 경기 상세 정보 조회
- 경기 선발 라인업 조회 (`/fixtures/{fixtureId}/lineups`)
- 경기 선수별 통계 조회 (`/fixtures/{fixtureId}/players`)
- Follow 기능을 통한 관심 경기 관리

### 3. 실시간 경기 이벤트 브로드캐스팅

- SSE (Server-Sent Events)를 통한 실시간 경기 이벤트 전송
- Follow된 경기만 실시간 이벤트 수신 가능
- 자동 스케줄러를 통한 경기 상태 업데이트
- 경기 상태별 동적 스케줄링 (NS, HT, FINISHED 등)

### 4. 위키 및 댓글

- 위키 목록 조회
- 팀/리그 위키 페이지 조회
- 위키 카테고리(목차) 생성/수정/삭제
  - 나무위키의 목차처럼 사용자가 자유롭게 생성할 수 있는 인덱스 개념
  - 카테고리는 orderIndex 순서로 정렬되어 표시
- 위키에 댓글 작성/조회/수정/삭제

### 5. Follow 기능

- 특정 경기 Follow 추가/삭제
- Follow한 경기 목록 조회
- Follow한 경기 정보 일괄 조회
- Follow된 경기에 대한 실시간 이벤트 브로드캐스팅

### 6. Favorite 기능

- 팀/리그 즐겨찾기 추가/삭제
- 즐겨찾기 목록 조회

### 7. 헬스체크

- 애플리케이션 상태 확인
- 데이터베이스 연결 상태 확인

## 프로젝트 구조

```
src/main/java/honajun/football_community/
├── auth/                    # 인증 관련
│   ├── controller/         # AuthController
│   ├── service/            # AuthService
│   └── dto/                # 인증 관련 DTO
├── member/                  # 회원 관리
│   ├── controller/         # MemberController
│   ├── service/            # MemberService
│   ├── entity/             # Member 엔티티
│   └── dto/                # 회원 관련 DTO
├── fixture/                 # 경기 정보
│   ├── controller/         # FixtureController, FollowController
│   ├── service/            # FixtureService, FollowService, FixtureUpdateScheduler
│   ├── entity/             # Follow 엔티티
│   ├── event/              # 실시간 이벤트
│   │   ├── controller/    # FixtureEventController (SSE)
│   │   └── service/       # FixtureEventService
│   └── dto/                # 경기 관련 DTO
├── favorite/                # 즐겨찾기
│   ├── controller/         # FavoriteController
│   ├── service/            # FavoriteService
│   ├── entity/             # Favorite 엔티티
│   └── dto/                # 즐겨찾기 관련 DTO
├── wiki/                    # 위키
│   ├── controller/         # WikiController
│   ├── service/            # WikiService
│   ├── entity/             # Wiki, WikiCategory 엔티티
│   ├── comment/            # 댓글
│   │   ├── controller/    # CommentController
│   │   ├── service/       # CommentService
│   │   └── entity/        # Comment 엔티티
│   └── dto/                # 위키 관련 DTO
├── blind/                   # 회원 차단
│   ├── entity/             # Blind 엔티티
│   └── repository/          # BlindRepository
├── team/                    # 팀 정보
│   └── entity/             # Team 엔티티
├── league/                  # 리그 정보
│   └── entity/             # League 엔티티
├── global/                  # 전역 설정 및 유틸리티
│   ├── config/             # SecurityConfig, SwaggerConfig 등
│   ├── security/            # JWT, 암호화 등
│   ├── response/            # CommonResponse
│   ├── enums/               # Enum 클래스들
│   └── HealthCheckController.java
└── FootballCommunityApplication.java
```

## 실행 방법

### 1. 사전 요구사항

- Java 17 이상
- MySQL 8.0 이상
- Redis (선택사항, 캐싱용)
- Gradle 7.x 이상

### 2. 프로젝트 클론 및 빌드

```bash
# 프로젝트 클론
git clone <repository-url>
cd Football_Community_BE

# Gradle 빌드
./gradlew build

# 또는 Windows의 경우
gradlew.bat build
```

### 3. 환경 변수 설정

`.env` 파일을 생성하고 필요한 환경 변수를 설정합니다. 자세한 내용은 [환경 변수 설정](#환경-변수-설정) 섹션을 참조하세요.

### 4. 애플리케이션 실행

```bash
# Gradle을 통한 실행
./gradlew bootRun

# 또는 빌드된 JAR 파일 실행
java -jar build/libs/Football_Community-0.0.1-SNAPSHOT.jar
```

애플리케이션이 성공적으로 실행되면 기본적으로 `http://localhost:8080`에서 접근할 수 있습니다.

## 환경 변수 설정

프로젝트 실행 전에 환경 변수를 설정해야 합니다.

### 1. .env 파일 생성

프로젝트 루트 디렉토리에 `.env` 파일을 생성하고 필요한 값들을 입력합니다.

```bash
cp .env.example .env  # .env.example이 있는 경우
```

`.env` 파일에는 다음 환경 변수들이 필요합니다:

```env
# AES 암호화 키 (16, 24, 또는 32 바이트)
AES_KEY=your-aes-key-here

# 데이터베이스 설정
DB_USERNAME=your-db-username
DB_PASSWORD=your-db-password
DB_URL=localhost:3306
DB_NAME=your-db-name

# JWT 시크릿 키
JWT_SECRET=your-jwt-secret-key-here

# Football API 설정
FOOTBALL_API_KEY=your-api-football-key
FOOTBALL_API_HOST=api-football-v1.p.rapidapi.com
```

### 2. 로컬 개발 환경에서 환경 변수 사용

Spring Boot는 시스템 환경 변수를 자동으로 읽습니다. 로컬 개발 시에는 다음과 같은 방법으로 환경 변수를 설정할 수 있습니다:

#### macOS/Linux:

```bash
export AES_KEY=your-aes-key
export DB_USERNAME=your-db-username
# ... 기타 환경 변수들

# 또는 .env 파일을 source로 읽기
set -a
source .env
set +a
```

#### Windows (PowerShell):

```powershell
$env:AES_KEY="your-aes-key"
$env:DB_USERNAME="your-db-username"
# ... 기타 환경 변수들
```

#### IntelliJ IDEA:

Run/Debug Configurations에서 "Environment variables"에 환경 변수를 추가하거나, `.env` 파일을 직접 사용할 수 있는 플러그인을 설치하여 사용할 수 있습니다.

### 3. GitHub Actions CI/CD 환경 변수 설정

GitHub Actions에서 환경 변수를 사용하려면 GitHub Repository의 Settings > Secrets and variables > Actions에서 다음 Secrets를 추가해야 합니다:

- `AES_KEY`
- `DB_USERNAME`
- `DB_PASSWORD`
- `DB_URL`
- `DB_NAME`
- `JWT_SECRET`
- `FOOTBALL_API_KEY`
- `FOOTBALL_API_HOST`

GitHub Actions workflow 파일에서 다음과 같이 사용할 수 있습니다:

```yaml
env:
  AES_KEY: ${{ secrets.AES_KEY }}
  DB_USERNAME: ${{ secrets.DB_USERNAME }}
  DB_PASSWORD: ${{ secrets.DB_PASSWORD }}
  # ... 기타 환경 변수들
```

### 주의사항

- `.env` 파일은 민감한 정보를 포함하므로 절대 Git에 커밋하지 않습니다.
- `.gitignore`에 `.env` 파일이 포함되어 있는지 확인하세요.
- 프로덕션 환경에서는 환경 변수를 안전하게 관리하는 시스템(예: Kubernetes Secrets, AWS Secrets Manager 등)을 사용하는 것을 권장합니다.

## API 엔드포인트

### 인증 (Auth)

| Method | Endpoint                   | 설명             | 인증 필요 |
| ------ | -------------------------- | ---------------- | --------- |
| GET    | `/auth/email-verification` | 이메일 중복 확인 | ❌        |
| POST   | `/auth/signup`             | 회원가입         | ❌        |
| POST   | `/auth/login`              | 로그인           | ❌        |
| PATCH  | `/auth/password`           | 비밀번호 변경    | ✅        |

### 회원 관리 (Member)

| Method | Endpoint                    | 설명               | 인증 필요 |
| ------ | --------------------------- | ------------------ | --------- |
| GET    | `/members/my-profile`       | 내 프로필 조회     | ✅        |
| GET    | `/members/{memberId}`       | 회원 프로필 조회   | ❌        |
| PUT    | `/members`                  | 회원 정보 수정     | ✅        |
| DELETE | `/members`                  | 회원 탈퇴          | ✅        |
| POST   | `/members/{memberId}/blind` | 회원 블라인드      | ✅        |
| PATCH  | `/members/{memberId}/blind` | 회원 블라인드 해제 | ✅        |

### 경기 정보 (Fixture)

| Method | Endpoint                      | 설명                                                                               | 인증 필요 |
| ------ | ----------------------------- | ---------------------------------------------------------------------------------- | --------- |
| GET    | `/fixtures/upcoming`          | 오늘 기준 예정된 경기 조회                                                         | ❌        |
| GET    | `/fixtures/past`              | 오늘 기준 과거 경기 기록 조회 (Follow 여부와 관계없이 조회 가능)                   | ❌        |
| GET    | `/fixtures/followed`          | Follow한 경기 정보 조회                                                            | ✅        |
| GET    | `/fixtures/team/{teamId}`     | 팀의 경기 일정 조회                                                                | ❌        |
| GET    | `/fixtures/league/{leagueId}` | 리그의 경기 일정 조회                                                              | ❌        |
| GET    | `/fixtures/{fixtureId}`       | 경기 상세조회 (Follow 여부와 관계없이 조회 가능, 종료된 경기는 `events` 필드 포함) | ❌        |
| GET    | `/fixtures/{fixtureId}/lineups` | 경기 선발 라인업 조회 (경기 시작 전이면 빈 배열 반환)                              | ❌        |
| GET    | `/fixtures/{fixtureId}/players` | 경기 선수별 통계 조회 (경기 시작 전이거나 데이터가 없으면 빈 배열 반환)            | ❌        |

### Follow 기능

| Method | Endpoint              | 설명             | 인증 필요 |
| ------ | --------------------- | ---------------- | --------- |
| POST   | `/follows`            | 경기 Follow 추가 | ✅        |
| DELETE | `/follows/{followId}` | Follow 삭제      | ✅        |
| GET    | `/follows`            | Follow 목록 조회 | ✅        |

### Favorite 기능

| Method | Endpoint                  | 설명               | 인증 필요 |
| ------ | ------------------------- | ------------------ | --------- |
| POST   | `/favorites`              | 즐겨찾기 추가      | ✅        |
| DELETE | `/favorites/{favoriteId}` | 즐겨찾기 삭제      | ✅        |
| GET    | `/favorites`              | 즐겨찾기 목록 조회 | ✅        |

### 실시간 경기 이벤트 (SSE)

| Method | Endpoint                                 | 설명                    | 인증 필요 |
| ------ | ---------------------------------------- | ----------------------- | --------- |
| GET    | `/fixtures/events/{fixtureId}/subscribe` | 실시간 경기 이벤트 구독 | ❌        |

### 위키 (Wiki)

| Method | Endpoint                                      | 설명              | 인증 필요 |
| ------ | --------------------------------------------- | ----------------- | --------- |
| GET    | `/wikis`                                      | 위키 목록 조회    | ❌        |
| GET    | `/wikis/{wikiId}`                             | 팀/리그 위키 조회 | ❌        |
| POST   | `/wikis/{wikiId}/categories`                  | 카테고리 생성     | ✅        |
| PUT    | `/wikis/{wikiId}/categories/{wikiCategoryId}` | 카테고리 수정     | ✅        |
| DELETE | `/wikis/{wikiId}/categories/{wikiCategoryId}` | 카테고리 삭제     | ✅        |

### 댓글 (Comment)

| Method | Endpoint                               | 설명      | 인증 필요 |
| ------ | -------------------------------------- | --------- | --------- |
| POST   | `/wikis/{wikiId}/comments`             | 댓글 작성 | ✅        |
| GET    | `/wikis/{wikiId}/comments`             | 댓글 조회 | ❌        |
| PUT    | `/wikis/{wikiId}/comments/{commentId}` | 댓글 수정 | ✅        |
| DELETE | `/wikis/{wikiId}/comments/{commentId}` | 댓글 삭제 | ✅        |

### 헬스체크

| Method | Endpoint  | 설명                         | 인증 필요 |
| ------ | --------- | ---------------------------- | --------- |
| GET    | `/health` | 애플리케이션 및 DB 상태 확인 | ❌        |

## 데이터베이스

### 주요 엔티티

- **Member**: 회원 정보
- **Follow**: 경기 Follow 관계 (Member ↔ Fixture)
- **Favorite**: 팀/리그 즐겨찾기 (Member ↔ Team/League)
- **Wiki**: 위키 페이지
- **WikiCategory**: 위키 카테고리
- **Comment**: 위키 댓글
- **Team**: 팀 정보
- **League**: 리그 정보
- **Blind**: 회원 차단 관계

### 데이터베이스 설정

`application.yml`에서 데이터베이스 연결 정보를 설정합니다:

```yaml
spring:
  datasource:
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
    url: jdbc:mysql://${DB_URL}/${DB_NAME}?autoReconnect=true&setTimezone=Asia/Seoul
    hikari:
      maximum-pool-size: 30
      minimum-idle: 10
      connection-timeout: 30000
      idle-timeout: 600000
      max-lifetime: 1800000
  jpa:
    hibernate:
      ddl-auto: update
```

## Swagger 문서

애플리케이션 실행 후 다음 URL에서 Swagger UI에 접근할 수 있습니다:

- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8080/v3/api-docs`

Swagger UI를 통해 모든 API 엔드포인트를 확인하고 테스트할 수 있습니다.

## 주요 특징

### 1. 스케줄러 최적화

- Follow된 경기만 업데이트하여 외부 API 호출 최소화
- 경기 상태별 동적 스케줄링 (NS, HT, FINISHED 등)
- 자정마다 오늘 날짜 경기 자동 재활성화

### 2. 실시간 이벤트 브로드캐스팅

- SSE를 통한 실시간 경기 이벤트 전송
- Follow된 경기만 구독 가능
- 연결 타임아웃 관리 (30분)

### 3. 데이터베이스 연결 풀 최적화

- HikariCP를 통한 효율적인 연결 관리
- 캐싱을 통한 DB 쿼리 최소화

### 4. 보안

- JWT 토큰 기반 인증
- AES 암호화를 통한 민감 정보 보호
- Spring Security를 통한 엔드포인트 보호

## 라이선스

이 프로젝트는 개인 프로젝트입니다.
