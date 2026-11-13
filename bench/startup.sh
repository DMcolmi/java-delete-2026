#!/usr/bin/env bash
# Startup time with and without an AOT cache (JEP 483 + 514, Java 24/25).
# Usage: bench/startup.sh [runs]    (uses java from JAVA_HOME or PATH, run from the repo root)
set -euo pipefail
runs=${1:-10}
java=${JAVA_HOME:+$JAVA_HOME/bin/}java
javac=${JAVA_HOME:+$JAVA_HOME/bin/}javac
jar=${JAVA_HOME:+$JAVA_HOME/bin/}jar
out=target/bench
mkdir -p "$out/classes"

mvn -q dependency:build-classpath -Dmdep.outputFile="$out/cp.txt" -Dmdep.includeScope=runtime
"$javac" -d "$out/classes" -cp "$(cat "$out/cp.txt")" bench/Startup.java
"$jar" --create --file "$out/startup.jar" -C "$out/classes" .   # the AOT cache only covers classes from JARs
cp="$out/startup.jar:$(cat "$out/cp.txt")"

# Training run: records which classes get loaded and linked, and writes the cache
"$java" -XX:AOTCacheOutput="$out/app.aot" -cp "$cp" bench.Startup > /dev/null 2>&1

time_ms() {
    local start end
    start=$(perl -MTime::HiRes=time -e 'printf "%.0f", time * 1000')
    "$java" "$@" -cp "$cp" bench.Startup
    end=$(perl -MTime::HiRes=time -e 'printf "%.0f", time * 1000')
    echo $((end - start))
}

average() {
    local total=0
    for _ in $(seq "$runs"); do total=$((total + $(time_ms "$@"))); done
    echo $((total / runs))
}

"$java" -XX:AOTCache="$out/app.aot" -cp "$cp" bench.Startup print   # sanity check: the app still works
echo "$("$java" -version 2>&1 | head -1), $runs runs each"
echo "without AOT cache: $(average -Xshare:auto) ms"
echo "with AOT cache:    $(average -XX:AOTCache="$out/app.aot") ms"
