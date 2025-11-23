# Javalin Starter Project

This is a minimal Javalin Java project scaffolded for you.

Quick start

1. Build:

```bash
mvn -q -DskipTests package
```

2. Run using the exec plugin:

```bash
mvn -q exec:java
```

The app starts on port `7000`. Try:

```bash
curl http://localhost:7000/
curl http://localhost:7000/health
```

If you don't have Maven installed, install it (Homebrew):

```bash
brew install maven
```
