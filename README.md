## Git Pre-Commit Hook Setup

This project uses a custom Git hooks directory (`.githooks`) to enforce pre-commit validations.

### One-Time Setup

After cloning the repository, configure Git to use the project's hooks directory:

```bash
git config core.hooksPath .githooks
```

### Verify Configuration

Verify that Git is configured to use the custom hooks directory:

```bash
git config --get core.hooksPath
```

Expected output:

```text
.githooks
```

### Notes

* The `.githooks/pre-commit` file is version-controlled and shared with all team members.
* The `core.hooksPath` setting is stored in each developer's local Git configuration and is not committed to the repository.
* Each developer must complete the one-time setup after cloning the repository.
* Developers who use Linux or macOS may need to make the hook executable:

```bash
chmod +x .githooks/pre-commit
```
