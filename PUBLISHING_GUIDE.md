# Publishing Guide for Core Auth Starter

This guide covers the complete process for publishing new versions of the Core Auth Starter library to GitHub Packages.

## Prerequisites

Before publishing, ensure you have:

- Git repository access with push permissions
- GitHub Personal Access Token (PAT) with `write:packages` permission
- Gradle wrapper configured in the project
- All changes committed and tested

## Publishing Process

### 1. Update Version Number

Edit `build.gradle.kts` and update the version:

```kotlin
version = "1.0.X"  // Replace X with new version number
```

### 2. Update Changelog

Add a new entry to `CHANGELOG.md` documenting:
- New features
- Bug fixes
- Breaking changes
- Technical improvements

### 3. Build the Project

Clean and build the project to ensure everything compiles:

```bash
./gradlew clean build
```

This will:
- Compile all Java sources
- Run tests
- Generate JAR files (main, sources, javadoc)
- Validate the build

### 4. Publish to GitHub Packages

Publish the artifacts:

```bash
./gradlew publish
```

This publishes:
- Main JAR file
- Sources JAR
- JavaDoc JAR
- Maven metadata
- All checksums (SHA1, MD5, SHA256, SHA512)

### 5. Commit and Tag

Commit all changes with a descriptive message:

```bash
git add -A
git commit -m "Release vX.X.X: Brief description of changes

Features:
- Feature 1
- Feature 2

Technical improvements:
- Improvement 1
- Improvement 2"
```

Create a git tag:

```bash
git tag -a vX.X.X -m "Version X.X.X - Brief description"
```

### 6. Push Changes

Push both commits and tags:

```bash
git push origin main
git push origin vX.X.X
```

## Authentication Setup

### GitHub Personal Access Token

1. Go to GitHub Settings → Developer settings → Personal access tokens
2. Generate a new token with `write:packages` permission
3. Store the token securely

### Gradle Properties

Create or update `~/.gradle/gradle.properties`:

```properties
gpr.user=your-github-username
gpr.key=your-personal-access-token
```

### Environment Variables (Alternative)

Set environment variables instead of gradle.properties:

```bash
export USERNAME=your-github-username
export TOKEN=your-personal-access-token
```

## Version Numbering

Follow semantic versioning (MAJOR.MINOR.PATCH):

- **MAJOR**: Breaking changes
- **MINOR**: New features (backward compatible)
- **PATCH**: Bug fixes (backward compatible)

## Published Artifacts

Each release publishes these artifacts to GitHub Packages:

- `core-auth-starter-X.X.X.jar` - Main library
- `core-auth-starter-X.X.X-sources.jar` - Source code
- `core-auth-starter-X.X.X-javadoc.jar` - API documentation
- `core-auth-starter-X.X.X.pom` - Maven metadata
- Various checksum files for integrity verification

## Using Published Versions

### Gradle

```kotlin
repositories {
    maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/papapitufo/core")
        credentials {
            username = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
            password = project.findProperty("gpr.key") as String? ?: System.getenv("TOKEN")
        }
    }
}

dependencies {
    implementation("com.control:core-auth-starter:X.X.X")
}
```

### Maven

```xml
<repositories>
    <repository>
        <id>github</id>
        <url>https://maven.pkg.github.com/papapitufo/core</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.control</groupId>
        <artifactId>core-auth-starter</artifactId>
        <version>X.X.X</version>
    </dependency>
</dependencies>
```

## Troubleshooting

### Build Failures

- Ensure all tests pass: `./gradlew test`
- Check Java version compatibility (requires Java 17+)
- Verify Spring Boot version compatibility

### Publishing Issues

- Verify GitHub token has correct permissions
- Check repository access and credentials
- Ensure version number doesn't already exist

### Authentication Problems

- Verify token is not expired
- Check username/token combination
- Ensure `write:packages` permission is granted

## Best Practices

1. **Always test before publishing** - Run the full test suite
2. **Document changes** - Update changelog with all modifications
3. **Use semantic versioning** - Follow MAJOR.MINOR.PATCH format
4. **Tag releases** - Create git tags for version tracking
5. **Backup important versions** - Keep stable versions tagged
6. **Review dependencies** - Ensure all dependencies are up to date

## Quick Reference Commands

```bash
# Complete publishing workflow
./gradlew clean build
./gradlew publish
git add -A
git commit -m "Release vX.X.X: Description"
git tag -a vX.X.X -m "Version X.X.X"
git push origin main
git push origin vX.X.X
```

## Support

For issues with publishing:
1. Check the GitHub Actions logs
2. Verify GitHub Packages permissions
3. Review the gradle build output
4. Ensure all dependencies are resolved correctly
