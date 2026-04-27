#!/usr/bin/env bash

set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
SRC_DIR="$ROOT_DIR/src"
DEPLOY_DIR="$ROOT_DIR/deploy"
BUILDS_DIR="$DEPLOY_DIR/builds"
RELEASES_DIR="$DEPLOY_DIR/releases"
RUNTIME_DIR="$DEPLOY_DIR/runtime"

usage() {
  cat <<'EOF'
Usage:
  scripts/api_lifecycle.sh build [build-id]
  scripts/api_lifecycle.sh release <build-id> <release-id> <env-file>
  scripts/api_lifecycle.sh run <release-id>

Commands:
  build
    Собирает неизменяемый backend-артефакт через Gradle.
    Результат кладётся в deploy/builds/<build-id>/.

  release
    Создаёт релиз как комбинацию build-артефакта и конфигурации окружения.
    env-file должен содержать переменные окружения в формате KEY=VALUE.
    Результат кладётся в deploy/releases/<release-id>/.

  run
    Запускает уже готовый релиз без пересборки и без изменения артефакта.

Examples:
  scripts/api_lifecycle.sh build
  scripts/api_lifecycle.sh release 20260427-120000 20260427-prod /secure/prod-api.env
  scripts/api_lifecycle.sh run 20260427-prod
EOF
}

fail() {
  echo "Error: $*" >&2
  exit 1
}

require_file() {
  local path="$1"
  [[ -f "$path" ]] || fail "File not found: $path"
}

require_dir() {
  local path="$1"
  [[ -d "$path" ]] || fail "Directory not found: $path"
}

ensure_layout() {
  mkdir -p "$BUILDS_DIR" "$RELEASES_DIR" "$RUNTIME_DIR"
}

timestamp() {
  date +"%Y%m%d-%H%M%S"
}

build_cmd() {
  ensure_layout

  local build_id="${1:-$(timestamp)}"
  local build_dir="$BUILDS_DIR/$build_id"
  local artifact_dir="$SRC_DIR/api/build/libs"
  local artifact_path
  local commit_sha

  [[ ! -e "$build_dir" ]] || fail "Build directory already exists: $build_dir"

  mkdir -p "$build_dir"

  (
    cd "$SRC_DIR"
    ./gradlew :api:clean :api:shadowJar
  )

  artifact_path="$(find "$artifact_dir" -maxdepth 1 -type f -name "*-all.jar" | head -n 1)"
  [[ -n "${artifact_path:-}" ]] || fail "Built artifact not found in $artifact_dir"

  cp "$artifact_path" "$build_dir/api.jar"

  commit_sha="$(git -C "$ROOT_DIR" rev-parse HEAD)"

  cat > "$build_dir/build-info.env" <<EOF
BUILD_ID=$build_id
BUILD_TIMESTAMP=$(date -u +"%Y-%m-%dT%H:%M:%SZ")
GIT_COMMIT=$commit_sha
ARTIFACT_NAME=api.jar
SOURCE_ARTIFACT=$artifact_path
EOF

  echo "Build created: $build_dir"
}

release_cmd() {
  ensure_layout

  local build_id="${1:-}"
  local release_id="${2:-}"
  local env_file="${3:-}"
  local build_dir="$BUILDS_DIR/$build_id"
  local release_dir="$RELEASES_DIR/$release_id"

  [[ -n "$build_id" ]] || fail "build-id is required"
  [[ -n "$release_id" ]] || fail "release-id is required"
  [[ -n "$env_file" ]] || fail "env-file is required"

  require_dir "$build_dir"
  require_file "$build_dir/api.jar"
  require_file "$build_dir/build-info.env"
  require_file "$env_file"
  [[ ! -e "$release_dir" ]] || fail "Release directory already exists: $release_dir"

  mkdir -p "$release_dir"
  cp "$build_dir/api.jar" "$release_dir/api.jar"
  cp "$build_dir/build-info.env" "$release_dir/build-info.env"
  cp "$env_file" "$release_dir/release.env"

  cat > "$release_dir/release-info.env" <<EOF
RELEASE_ID=$release_id
RELEASE_TIMESTAMP=$(date -u +"%Y-%m-%dT%H:%M:%SZ")
BUILD_ID=$build_id
ARTIFACT_NAME=api.jar
ENV_FILE=$release_dir/release.env
EOF

  echo "Release created: $release_dir"
}

run_cmd() {
  ensure_layout

  local release_id="${1:-}"
  local release_dir="$RELEASES_DIR/$release_id"

  [[ -n "$release_id" ]] || fail "release-id is required"
  require_dir "$release_dir"
  require_file "$release_dir/api.jar"
  require_file "$release_dir/release.env"

  (
    set -a
    # shellcheck disable=SC1090
    source "$release_dir/release.env"
    set +a

    exec java -jar "$release_dir/api.jar"
  )
}

main() {
  local command="${1:-}"
  shift || true

  case "$command" in
    build)
      build_cmd "$@"
      ;;
    release)
      release_cmd "$@"
      ;;
    run)
      run_cmd "$@"
      ;;
    ""|-h|--help|help)
      usage
      ;;
    *)
      fail "Unknown command: $command"
      ;;
  esac
}

main "$@"
