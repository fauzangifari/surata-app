<p align="center">
  <img src="app/src/main/res/drawable/logo_surata_full.png" alt="Surata Logo" width="200"/>
</p>

<h1 align="center">📄 Surata App</h1>

<p align="center">
  <strong>Aplikasi Manajemen Surat Digital untuk Sekolah</strong>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android-green?style=for-the-badge&logo=android" alt="Platform"/>
  <img src="https://img.shields.io/badge/Kotlin-2.0.21-blue?style=for-the-badge&logo=kotlin" alt="Kotlin"/>
  <img src="https://img.shields.io/badge/Jetpack_Compose-Material3-purple?style=for-the-badge&logo=jetpackcompose" alt="Jetpack Compose"/>
  <img src="https://img.shields.io/badge/Min_SDK-26-orange?style=for-the-badge" alt="Min SDK"/>
  <img src="https://img.shields.io/badge/Target_SDK-35-brightgreen?style=for-the-badge" alt="Target SDK"/>
</p>

---

## 📖 Deskripsi

**Surata** adalah aplikasi mobile Android yang dirancang untuk memudahkan pengelolaan surat-menyurat digital di lingkungan sekolah. Aplikasi ini memungkinkan pengguna (siswa, guru, dan staf) untuk membuat, mengajukan, melacak, dan mengelola berbagai jenis surat dengan mudah dan efisien.

Dengan menggunakan teknologi terkini seperti **Jetpack Compose** dan **Material Design 3**, Surata menawarkan pengalaman pengguna yang modern, responsif, dan intuitif.

---

## ✨ Fitur Utama

### 🔐 Autentikasi
- Login dengan email dan password
- Session management yang aman
- Logout dengan konfirmasi

### 📝 Manajemen Surat
- **Buat Surat Baru** - Ajukan berbagai jenis surat dengan form yang interaktif
- **Lihat Daftar Surat** - Pantau semua pengajuan surat dalam satu tampilan
- **Detail Surat** - Informasi lengkap termasuk status, lampiran, dan riwayat
- **Resubmit Surat** - Ajukan ulang surat yang ditolak
- **Preview PDF** - Lihat dokumen surat yang sudah disetujui

### 🔔 Notifikasi
- Push notification real-time via Firebase Cloud Messaging (FCM)
- Notifikasi untuk update status surat
- Tandai notifikasi sebagai sudah dibaca
- Hapus semua notifikasi

### 👤 Profil Pengguna
- Lihat informasi profil
- Edit data profil (nama, email personal)
- Upload foto profil

### ⚙️ Pengaturan
- Pengaturan akun
- FAQ (Frequently Asked Questions)
- Tentang aplikasi

---

## 🏗️ Arsitektur

Surata menggunakan **Clean Architecture** dengan pemisahan layer yang jelas:

```
┌─────────────────────────────────────────────────────────────┐
│                       Presentation Layer                     │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────────────┐ │
│  │   Screens   │  │  ViewModels │  │     Components      │ │
│  └─────────────┘  └─────────────┘  └─────────────────────┘ │
├─────────────────────────────────────────────────────────────┤
│                        Domain Layer                          │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────────────┐ │
│  │   Models    │  │  Use Cases  │  │    Repositories     │ │
│  │             │  │             │  │    (Interface)      │ │
│  └─────────────┘  └─────────────┘  └─────────────────────┘ │
├─────────────────────────────────────────────────────────────┤
│                         Data Layer                           │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────────────┐ │
│  │  Remote     │  │   Local     │  │    Repositories     │ │
│  │  (Retrofit) │  │  (Room/DS)  │  │  (Implementation)   │ │
│  └─────────────┘  └─────────────┘  └─────────────────────┘ │
└─────────────────────────────────────────────────────────────┘
```

### 📁 Struktur Modul

```
SurataApp/
├── app/                          # Presentation Layer
│   ├── di/                       # Dependency Injection (ViewModel Module)
│   ├── service/                  # Firebase Messaging Service
│   ├── ui/
│   │   ├── components/           # Reusable UI Components
│   │   ├── navigation/           # Navigation Graph & Screen Routes
│   │   ├── screens/              # Screen Composables
│   │   │   ├── about/
│   │   │   ├── detail/
│   │   │   ├── faq/
│   │   │   ├── home/
│   │   │   ├── login/
│   │   │   ├── notification/
│   │   │   ├── profile/
│   │   │   ├── settings/
│   │   │   └── splash/
│   │   └── theme/                # Material Theme Configuration
│   ├── utils/                    # Utility Classes
│   └── viewmodel/                # Shared ViewModels
│
├── domain/                       # Domain Layer
│   ├── common/                   # Common Classes (Resource, etc.)
│   ├── di/                       # Dependency Injection (UseCase Module)
│   ├── model/                    # Data Models/Entities
│   ├── repository/               # Repository Interfaces
│   └── usecase/                  # Use Cases
│
├── data/                         # Data Layer
│   ├── di/                       # Dependency Injection (Repository Module)
│   ├── mapper/                   # Data Mappers
│   ├── repository/               # Repository Implementations
│   ├── source/
│   │   ├── local/
│   │   │   ├── datastore/        # DataStore Preferences
│   │   │   └── room/             # Room Database (DAO, Entity)
│   │   └── remote/
│   │       ├── dto/              # Data Transfer Objects
│   │       └── retrofit/         # API Services
│   └── utils/                    # Data Utilities
│
└── gradle/
    └── libs.versions.toml        # Version Catalog
```

---

## 🛠️ Tech Stack

### Core
| Technology | Version | Description |
|------------|---------|-------------|
| **Kotlin** | 2.0.21 | Bahasa pemrograman utama |
| **Jetpack Compose** | BOM 2025.03.01 | UI toolkit modern untuk Android |
| **Material Design 3** | Latest | Design system untuk UI konsisten |

### Architecture & DI
| Technology | Version | Description |
|------------|---------|-------------|
| **Koin** | 3.5.6 | Dependency Injection framework |
| **ViewModel** | 2.8.7 | Architecture Component untuk UI state |
| **Navigation Compose** | 2.8.9 | Navigasi declarative |

### Networking
| Technology | Version | Description |
|------------|---------|-------------|
| **Retrofit** | 2.11.0 | HTTP client untuk API calls |
| **OkHttp** | 4.12.0 | HTTP client dengan logging |
| **Gson** | 2.11.0 | JSON serialization/deserialization |

### Local Storage
| Technology | Version | Description |
|------------|---------|-------------|
| **Room** | 2.8.2 | Database abstraction layer |
| **DataStore** | 1.1.7 | Preferences storage |

### Firebase
| Technology | Version | Description |
|------------|---------|-------------|
| **Firebase BOM** | 33.5.1 | Firebase version management |
| **Firebase Messaging** | 25.0.1 | Push notifications |
| **Firebase Analytics** | Latest | App analytics |

### UI & Media
| Technology | Version | Description |
|------------|---------|-------------|
| **Coil** | 3.2.0 | Image loading library |
| **Accompanist Navigation** | 0.34.0 | Navigation animations |

### Security
| Technology | Version | Description |
|------------|---------|-------------|
| **Security Crypto** | 1.1.0 | Encrypted storage |

---

## 🚀 Instalasi & Setup

### Prerequisites
- **Android Studio** Hedgehog atau lebih baru
- **JDK 11** atau lebih tinggi
- **Android SDK** dengan API Level 26-35
- Akun **Firebase** untuk FCM

### Langkah Instalasi

1. **Clone repository**
   ```bash
   git clone https://github.com/fauzangifari/SurataApp.git
   cd SurataApp
   ```

2. **Setup Firebase**
   - Buat project di [Firebase Console](https://console.firebase.google.com/)
   - Download `google-services.json`
   - Letakkan file di folder `app/`

3. **Konfigurasi Environment**
   
   Buat file `local.properties` di root project dan tambahkan:
   ```properties
   sdk.dir=<path_to_android_sdk>
   BASE_URL="https://your-api-url.com/"
   BASE_URL_DEV="https://your-dev-api-url.com/"
   ```

4. **Sync dan Build**
   ```bash
   ./gradlew clean build
   ```

5. **Run aplikasi**
   - Buka project di Android Studio
   - Pilih device/emulator
   - Klik Run ▶️

---

## 📡 API Endpoints

Aplikasi ini terhubung dengan backend API untuk operasi berikut:

### Authentication
| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/auth/sign-in/email` | Login dengan email |
| `POST` | `/api/auth/sign-out` | Logout |
| `GET` | `/api/auth/get-session` | Validasi session |

### Letters
| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/letters` | Ambil semua surat |
| `GET` | `/api/users/{userId}/letters` | Ambil surat berdasarkan user |
| `GET` | `/api/letters/{letterId}` | Detail surat |
| `POST` | `/api/letters` | Buat surat baru |
| `PATCH` | `/api/letters/{letterId}` | Update surat |
| `POST` | `/api/letters/{letterId}/resubmit` | Ajukan ulang surat |

### Users
| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/users/me` | Profil user login |
| `GET` | `/api/users` | Semua users |
| `PATCH` | `/api/users/{usersId}` | Update profil user |

### Storage
| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/presigned` | Generate presigned URL untuk upload |

### FCM
| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/fcm/token` | Simpan FCM token |
| `DELETE` | `/api/fcm/token/{token}` | Hapus FCM token |

---

## 🧪 Testing

### Unit Tests
```bash
./gradlew test
```

### Instrumented Tests
```bash
./gradlew connectedAndroidTest
```

### Test Libraries
- **JUnit 4** - Unit testing framework
- **JUnit Jupiter** - Advanced testing
- **MockK** - Kotlin mocking library
- **Robolectric** - Android unit testing
- **Kotlinx Coroutines Test** - Coroutine testing

---

## 🎨 UI Components

Aplikasi ini memiliki komponen UI reusable yang konsisten:

| Component | Description |
|-----------|-------------|
| `BottomBar` | Bottom navigation bar |
| `TopBar` | Custom top app bar |
| `ButtonCustom` | Styled button dengan berbagai variant |
| `CardSurat` | Card untuk menampilkan info surat |
| `TextInput` | Input field dengan validasi |
| `DateInput` | Date picker input |
| `TimeInput` | Time picker input |
| `DropdownField` | Dropdown selector |
| `MultiPickerField` | Multiple selection picker |
| `FileUpload` | File picker dan uploader |
| `CustomToast` | Toast notification custom |
| `CustomDialog` | Alert dialog custom |
| `NotificationCard` | Card untuk notifikasi |
| `ProfileAvatar` | Avatar dengan image loading |
| `PdfViewer` | PDF preview component |
| `Collapse` | Expandable/collapsible content |

---

## 🔐 Security Features

- **Encrypted Preferences** - Token dan data sensitif dienkripsi
- **Auth Token Provider** - Manajemen token yang aman
- **Session Validation** - Validasi session di setiap request
- **FCM Token Management** - Pengelolaan token push notification

---

## 📝 Kontribusi

Kami sangat menghargai kontribusi dari komunitas! Berikut cara berkontribusi:

1. **Fork** repository ini
2. **Create branch** untuk fitur baru
   ```bash
   git checkout -b feature/AmazingFeature
   ```
3. **Commit** perubahan
   ```bash
   git commit -m 'Add some AmazingFeature'
   ```
4. **Push** ke branch
   ```bash
   git push origin feature/AmazingFeature
   ```
5. Buat **Pull Request**

### Code Style
- Gunakan **Kotlin Coding Conventions**
- Ikuti **Clean Architecture** principles
- Dokumentasikan kode dengan KDoc
- Tulis unit test untuk fitur baru

---

## 👨‍💻 Developer

<table>
  <tr>
    <td align="center">
      <a href="https://github.com/fauzangifari">
        <img src="https://github.com/fauzangifari.png" width="100px;" alt="Fauzan Gifari"/>
        <br />
        <sub><b>Fauzan Gifari</b></sub>
      </a>
      <br />
      <sub>Lead Developer</sub>
    </td>
  </tr>
</table>

---

## 📄 Lisensi

```
Copyright © 2025 Fauzan Gifari

All rights reserved. This project and its contents are proprietary 
and confidential. Unauthorized copying, distribution, or modification 
of this project is strictly prohibited.
```

---

## 🙏 Acknowledgements

- [Jetpack Compose](https://developer.android.com/jetpack/compose) - Modern Android UI toolkit
- [Koin](https://insert-koin.io/) - Dependency Injection for Kotlin
- [Retrofit](https://square.github.io/retrofit/) - Type-safe HTTP client
- [Firebase](https://firebase.google.com/) - Backend services
- [Coil](https://coil-kt.github.io/coil/) - Image loading for Android
- [Material Design 3](https://m3.material.io/) - Design system

---

<p align="center">
  Made with ❤️ by <a href="https://github.com/fauzangifari">Fauzan Gifari</a>
</p>

<p align="center">
  <a href="#-surata-app">⬆️ Back to Top</a>
</p>

