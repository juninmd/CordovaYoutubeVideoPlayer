# Contributing to CordovaYoutubeVideoPlayer

We love your input! We want to make contributing to this project as easy and transparent as possible.

## Development Process

1. Fork the repo and create your branch from `develop`.
2. Run `npm install` to install dependencies.
3. Make your changes.
4. Run `npm run lint` to check code style.
5. Run `npm test` to ensure tests pass with >= 80% coverage.
6. Run `npm run format` to check formatting.
7. Update tests as needed.
8. Ensure the CI/CD pipeline passes.

## CI/CD Pipeline

This project uses GitHub Actions for CI/CD:

- **Lint**: ESLint + Prettier checks
- **Test**: Jest unit tests with coverage
- **Security**: npm audit + dependency review
- **Build**: Plugin packaging
- **Deploy**: Release creation (main branch only)

### Pipeline Requirements

- All linting checks must pass
- Test coverage must be >= 80%
- No high/critical severity vulnerabilities
- Build artifacts must be valid

## Code Style

- JavaScript: ES6+ with semicolons, single quotes
- Follow the existing code conventions in the project
- No unnecessary comments in code

## Testing

- Write unit tests for all new functionality
- Maintain minimum 80% code coverage
- Tests are in the `test/` directory using Jest
- Mock `cordova/exec` for plugin unit tests

## Pull Request Process

1. Update the README.md with details of changes if needed.
2. Update the version in `package.json` and `plugin.xml`.
3. The PR will be merged once CI passes and a reviewer approves.
4. For significant changes, open an issue first to discuss.

## Environment Variables

| Variable | Description | Required |
|---|---|---|
| `CODECOV_TOKEN` | Token for uploading coverage reports | CI only |
| `SLACK_WEBHOOK_URL` | Webhook for deployment notifications | CI only |

## License

By contributing, you agree that your contributions will be licensed under the MIT License.
