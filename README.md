# 🎵 Discogs Artist Explorer

This project is an Android app built using Jetpack Compose and MVI architecture. It allows users to search for artists via the Discogs API, view artist information, and explore their releases.
## 📦 Tech Stack

| Layer         | Tools & Libraries                             |
|---------------|-----------------------------------------------|
| Language      | Kotlin (≥ 2.0.0)                              |
| UI            | Jetpack Compose                               |
| Architecture  | MVI, Use Cases, Repository Pattern            |
| DI            | Hilt                                          |
| API           | Retrofit + OkHttp                             |
| Async         | Coroutines + Flow                             |
| Pagination    | Paging 3                                      |
| Image Loader  | Coil (with caching)                           |
| Testing       | JUnit, Mockito, Coroutine Test                |
| Static Checks | Detekt                                        |
| Build Tool    | Gradle (Version Catalog)                      |


## 🚀 Features

- Empty state prompt for artist search
- Search bar and paginated artist results
- Artist detail screen (basic info)
- Shows band members when applicable
- Cached images using Coil
- Error handling for empty/missing results
- Unit tests on paging sources and presentation
- Modular clean architecture: `:data`, `:domain`, `:app`


## 🧠 Architecture

This project follows the [official Android Architecture guidelines](https://developer.android.com/topic/architecture) with a modular and layered setup:
[REPLACE HERE WITH THE ARCH IMAGE]
- **MVI (Model-View-Intent)** was used instead of MVVM due to multiple UI states (Idle, Loading, Success, Empty, Error).
- A shared base class is used across ViewModels and UseCases. This architecture ensures a strict separation of concerns and testability.
- **Repository Pattern** provides abstraction between data sources and domain logic.
- **Paging 3** handles paginated responses from the API.

## 🧪 Testing

| Area               | Tools / Approach                               |
|--------------------|------------------------------------------------|
| Unit Testing       | **JUnit 4**, **Kotlinx Coroutines Test**       |
| Mocking            | **Mockito**                                    |
| ViewModel Testing  | `runTest` with `StandardTestDispatcher`        |
| Paging Source      | Simulated API responses with fake data         |
| Coverage           | Success, error, and empty UI states            |

The project includes unit tests for:

- `ArtistPagingSource`
- `ArtistReleasesPagingSource`
- `SearchArtistViewModel`
- `ArtistReleasesViewMode`l
- `ArtistDetailViewModel`

> Tests focus on validating presentation logic and interactions with mocked use cases no real network or database dependencies.

To run the unit tests:

```bash
./gradlew testDebugUnitTest
```

## 🧰 Static Analysis

`Detekt` is included in the project to enforce code quality.

To run analysis locally:
```bash
./gradlew detekt
```
## 🔧 Project Setup

1. **Clone the repository:**
    ```bash
    git clone https://github.com/your-username/discogs-artist-explorer.git
	```
2. **Add your Discogs token in** `local.properties`:
    ```bash
    DISCOGS_TOKEN=your_discogs_token
	```
> 	You can find more information about the Discogs API and how to get
> your token [here](https://www.discogs.com/settings/developers).
## 🧑‍💻 Development Process

I approached this challenge iteratively, in the following steps:

1.  **API Exploration**: Investigated the Discogs API and endpoints via Postman.

2.  **Data Module**: Created DTOs and Retrofit interfaces. Implemented mappers and paging sources.

3.  **Domain Module**: Designed use cases,  models, and contracts.

4.  **POC**: Built a small proof of concept from ***data → domain → fake UI*** to validate integration.

5.  **App Module**: Finalized the UI using Compose and wired everything together via ViewModels.
## ❌ Limitations & TODOs

-   Album detail screen not implemented.

-   Artist member releases not yet displayed.

-   Only basic filtering supported for now.
## 📝 Notes

-   API authentication is handled via a token in the `Authorization` header.

-   Pagination is supported with 30 elements per page.

-   Graceful error states are displayed for empty lists, 404s, and connectivity issues.

## :camera: Demo