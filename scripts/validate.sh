#!/bin/bash
for i in {1..18}; do
  if curl -fsS http://localhost:8080/actuator/health > /dev/null; then
    echo "App is healthy"
    exit 0
  fi
  echo "Waiting for app to become healthy ($i/18)..."
  sleep 5
done
echo "App failed to become healthy"
exit 1