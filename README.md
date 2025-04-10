# Passenger Transport Marketplace

## Описание проекта

Проект **Passenger Transport Marketplace** представляет собой веб-приложение для поиска и бронирования маршрутов различных видов транспорта (авиа, ж/д, автобус). Приложение состоит из двух основных частей:
- **Фронтенд**: Реализован на Next.js с использованием Tailwind CSS.
- **Бэкенд**: Реализован на Spring Boot с поддержкой SQLite.

---

## Стек технологий

### Фронтенд
- **Next.js**: Для серверного рендеринга и API-роутов.
- **Tailwind CSS**: Для стилизации интерфейса.
- **next-auth**: Для аутентификации пользователей.
- **Fetch**: Для взаимодействия с бэкендом.

### Бэкенд
- **Spring Boot**: Для реализации REST API.
- **Hibernate/JPA**: Для работы с базой данных.
- **SQLite**: Встроенная база данных для хранения данных.
- **BCrypt**: Для безопасного хеширования паролей.

---

## Инструкция по запуску

### 1. Установка зависимостей

#### a) Установите Node.js и npm/yarn
Убедитесь, что у вас установлены следующие версии:
- Node.js: v18+
- npm: v8+ или yarn: v1+

Проверьте установку:
```bash
node -v
npm -v
```

#### b) Установите Java и Maven
Убедитесь, что у вас установлены следующие версии:
- Java: v17+
- Maven: v3.8+

Проверьте установку:
```bash
java -version
mvn -version
```

---

### 2. Клонирование репозитория

Клонируйте репозиторий проекта:
```bash
git clone https://github.com/StarAlexander/passenger-transport-marketplace.git
cd passenger-transport-marketplace
```

---

### 3. Настройка переменных окружения

#### a) Фронтенд
Создайте файл `.env.local` в папке фронтенда (`marketplace-frontend`) и добавьте следующие переменные:
```env
NEXTAUTH_URL=http://localhost:3000
NEXTAUTH_SECRET=your-very-long-secret-key-with-at-least-32-characters
NEXT_PUBLIC_API_URL=http://localhost:8080
```

> **Примечание**: Генерировать секрет можно командой:
> ```bash
> openssl rand -base64 32
> ```

#### b) Бэкенд
Создайте файл `application.properties` в папке бэкенда (`marketplace-backend/src/main/resources`) и добавьте следующие параметры:
```properties
server.port=8080
server.address=0.0.0.0 # Разрешает доступ к бэкенду через все сетевые интерфейсы

spring.datasource.url=jdbc:sqlite:/data/marketplace.db
spring.jpa.hibernate.ddl-auto=create-drop

spring.security.user.name=admin
spring.security.user.password=adminpassword
```

---

### 4. Запуск приложения локально

#### a) Запуск бэкенда
Перейдите в папку бэкенда и выполните сборку:
```bash
cd marketplace-backend
mvn clean install
mvn spring-boot:run
```

Бэкенд будет доступен по адресу:
```
http://localhost:8080
```

#### b) Запуск фронтенда
Откройте новое окно терминала, перейдите в папку фронтенда и установите зависимости:
```bash
cd marketplace-frontend
npm install
```

Затем запустите приложение:
```bash
npm run dev
```

Фронтенд будет доступен по адресу:
```
http://localhost:3000
```

---

### 5. Запуск приложения через Docker

#### a) Установите Docker
Убедитесь, что Docker установлен и работает корректно.

#### b) Создайте `.env` файлы для Docker
Создайте два файла в корне проекта:
1. `.env.local` для разработки:
   ```env
   NEXTAUTH_URL=http://localhost:3000
   NEXTAUTH_SECRET=your-very-long-secret-key-with-at-least-32-characters
   NEXT_PUBLIC_API_URL=http://host.docker.internal:8080
   ```

2. `.env.production` для production:
   ```env
   NEXTAUTH_URL=http://frontend:3000
   NEXTAUTH_SECRET=your-very-long-secret-key-with-at-least-32-characters
   NEXT_PUBLIC_API_URL=http://backend:8080
   ```

#### c) Запуск через Docker Compose
Убедитесь, что у вас есть файл `docker-compose.yml` в корне проекта. Затем выполните:
```bash
docker-compose up --build
```

Приложение будет доступно по адресам:
- Фронтенд: `http://localhost:3000`
- Бэкенд: `http://localhost:8080`

---

### 6. Тестирование приложения

#### a) Регистрация пользователя
Перейдите на страницу `/auth/signup` и зарегистрируйте нового пользователя.

#### b) Авторизация
Используйте форму входа на странице `/auth/signin` для авторизации.

#### c) Поиск маршрутов
На главной странице (`/`) используйте форму поиска для поиска доступных маршрутов. Вы можете фильтровать маршруты по:
- Точке отправления.
- Пункту назначения.
- Типу транспорта.
- Временному диапазону.

#### d) Бронирование маршрутов
Нажмите кнопку "Book" рядом с нужным маршрутом для его бронирования. После успешного бронирования маршрут исчезнет из списка доступных.

#### e) Личный кабинет
Перейдите на страницу `/profile`, чтобы увидеть список ваших бронирований. Вы можете отменить бронирование, нажав кнопку "Cancel".

#### f) Статистика по системе
Перейдите на страницу `/stats`, чтобы увидеть статистику по системе.

#### g) Админ-панель
Авторизуйтесь как администратор и перейдите на страницу `/admin`, чтобы взаимодействовать с админ-панелью.


---

### 7. Архитектура проекта

#### a) Фронтенд
- **Страницы**:
  - `/`: Главная страница с формой поиска и списком доступных маршрутов.
  - `/auth/signin`: Страница входа.
  - `/auth/signup`: Страница регистрации.
  - `/profile`: Страница личного кабинета.
  - `/stats`: Статистика по системе.
  - `/admin`: Админ-панель.

- **API-роуты**:
  - `/api/auth/[...nextauth]/route.js`: Роут для аутентификации через next-auth.
  - `/api/check-user/route.js`: Проверка существования пользователя.

#### b) Бэкенд
- **Контроллеры**:
  - `AuthController`: Обработка запросов входа и регистрации.
  - `RouteController`: Поиск доступных маршрутов.
  - `BookingController`: Создание и отмена бронирований.
  - `AdminRouteController`: Админ-функционал.
  - `AdminStatsController`: Админ-статистика.
  - `AdminUserController`: Управление пользователями.
  - `StatsController`: Статистика по системе.

- **Сущности**:
  - `User`: Модель пользователя.
  - `Route`: Модель маршрута.
  - `Booking`: Модель бронирования.

- **Репозитории**:
  - `UserRepository`: CRUD-операции для пользователей.
  - `RouteRepository`: CRUD-операции для маршрутов.
  - `BookingRepository`: CRUD-операции для бронирований.

---

## Требования к системе

- **Docker**: Для запуска контейнеров.
- **Node.js v18+**: Для фронтенда.
- **Java v17+**: Для бэкенда.
- **Maven v3.8+**: Для сборки бэкенда.

---

