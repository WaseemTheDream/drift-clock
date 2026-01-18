# Release Build Command

[no-commit]

## Overview

Added the `/release` command for building signed release APKs, along with comprehensive documentation for keystore setup.

## Changes Made

### New Files Created

1. **`.claude/commands/release.md`**
   - Interactive release build command
   - Guides users through keystore setup
   - Supports both existing and new keystores
   - Builds signed APK with proper error handling

2. **`.claude/android-keystore/.gitignore`**
   - Protects `.jks` and `.keystore` files
   - Ignores `keystore.properties` credentials
   - Keeps README for instructions

3. **`.claude/android-keystore/README.md`**
   - Quick reference for the keystore directory
   - Security reminders

### Files Modified

1. **`README.md`**
   - Added `/release` to command table
   - Added complete "Release Builds" section
   - Instructions for existing keystore users (Option A)
   - Instructions for new keystore creation (Option B)
   - Security best practices table
   - Updated project structure diagram

2. **`CLAUDE.md`**
   - Added `/release` to Build & Deploy commands
   - Added `android-keystore/` to project structure

## Technical Details

### Keystore Configuration

The release command expects:
- Keystore file: `.claude/android-keystore/release.jks`
- Credentials: `.claude/android-keystore/keystore.properties`

Properties file format:
```properties
storeFile=../.claude/android-keystore/release.jks
storePassword=<password>
keyAlias=<alias>
keyPassword=<key-password>
```

### Gradle Integration

The release command modifies `app/build.gradle.kts` to:
1. Load keystore properties
2. Create release signing config
3. Apply to release build type

### Security

- All keystore files are git-ignored
- Credentials never leave the local machine
- Users are reminded to back up keystores securely

## User Experience

- Experienced Android devs: Copy existing `.jks`, create properties, run `/release`
- New Android devs: Follow keytool command, create properties, run `/release`
- Command guides users interactively if setup is incomplete
