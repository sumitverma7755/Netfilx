# Netfix - Modern Android Streaming App

<div align="center">
    <img src="app/src/main/res/mipmap-xxxhdpi/ic_launcher_round.png" alt="Netfix Logo" width="120"/>
</div>

A sophisticated Netflix-inspired Android streaming application showcasing modern Android development practices and architecture. This app demonstrates advanced features including video streaming, offline downloads, user authentication, and AI-powered content recommendations using LM Studio integration.

## 🌟 Key Features

### Core Features
- 🎨 Modern, intuitive UI built with Jetpack Compose
- 🎥 Smooth video streaming with Media3 (ExoPlayer)
- 💾 Download management with offline viewing
- 🔒 User authentication and profile system
- 🎯 Personalized content recommendations
- 🌙 Dark mode support
- 📱 Responsive design for all screen sizes

### Advanced Features
- 🤖 AI integration with LM Studio for smart content suggestions
- 📶 Wi-Fi only download option
- 🔄 Background download support
- 🎮 Picture-in-Picture mode
- 📊 Download progress tracking
- 🔍 Advanced search functionality
- 📝 User profiles with preferences

## 🛠️ Tech Stack

### Core Technologies
- 📱 Kotlin - Primary programming language
- 🎨 Jetpack Compose - Modern UI toolkit
- 🎬 Media3 (ExoPlayer) - Video playback and streaming
- 🌐 Retrofit - Network communication
- 💉 Hilt - Dependency injection
- 💾 Room - Local database
- 🔧 DataStore - Key-value storage
- ⚡ Coroutines - Asynchronous programming

### Architecture & Design
- 🏗️ MVVM Architecture
- 🧩 Clean Architecture principles
- 📐 Material Design 3
- 🔄 Repository pattern
- 🎯 Use case pattern
- 📦 Single source of truth

### Testing
- 🧪 JUnit for Unit tests
- 🤖 Espresso for UI tests
- 🔄 Mockk for mocking
- ⚡ Turbine for Flow testing

## 📱 Screenshots

<div align="center">
<table>
  <tr>
    <td><img src="screenshots/home.png" width="200" alt="Home Screen"/></td>
    <td><img src="screenshots/player.png" width="200" alt="Video Player"/></td>
    <td><img src="screenshots/downloads.png" width="200" alt="Downloads"/></td>
  </tr>
  <tr>
    <td>Home Screen</td>
    <td>Video Player</td>
    <td>Downloads</td>
  </tr>
  <tr>
    <td><img src="screenshots/profile.png" width="200" alt="Profile"/></td>
    <td><img src="screenshots/settings.png" width="200" alt="Settings"/></td>
    <td><img src="screenshots/search.png" width="200" alt="Search"/></td>
  </tr>
  <tr>
    <td>Profile Management</td>
    <td>Settings</td>
    <td>Search</td>
  </tr>
</table>
</div>

## 🚀 Getting Started

### Prerequisites
- Android Studio Arctic Fox or later
- JDK 11 or higher
- Android device or emulator running API 24 (Android 7.0) or higher

### Installation
1. Clone the repository
```bash
git clone https://github.com/sumitverma7755/Netfilx.git
```
2. Open the project in Android Studio
3. Sync project with Gradle files
4. Run the app on an emulator or physical device

### Build Variants
- `debug` - Development build with logging and debugging features
- `release` - Optimized build for release with ProGuard rules

## 🤖 LM Studio Integration

For AI-powered features:

1. Install and run LM Studio on your computer
2. Enable the API server in LM Studio (Server tab)
3. Configure the connection in the app settings:
   - Go to Settings > AI Integration
   - Enter your computer's IP address
   - Test the connection
4. Requirements:
   - LM Studio running on your computer
   - API server enabled
   - Phone and computer on same network
   - No firewall blocking port 1234

## 🤝 Contributing

We welcome contributions! Here's how you can help:

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

Please ensure you read our Contributing Guidelines before making a submission.

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 📱 App Demo

Watch our [video demo](https://www.youtube.com/watch?v=demo) to see Netfix in action!

## 🙏 Acknowledgments

- [ExoPlayer](https://exoplayer.dev/) for the amazing video player
- [LM Studio](https://lmstudio.ai/) for AI capabilities
- [Material Design](https://material.io/) for the design system
- [Jetpack Compose](https://developer.android.com/jetpack/compose) for the modern UI toolkit