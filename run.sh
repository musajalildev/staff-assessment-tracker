#!/bin/bash

RUN_CLIENT=false
RUN_SERVER=false

# Parse arguments
while [[ $# -gt 0 ]]; do
    case "$1" in
        --client)
            RUN_CLIENT=true
            ;;
        --server)
            RUN_SERVER=true
            ;;
    esac
    shift
done

# ===== Client =====
if [ "$RUN_CLIENT" = true ]; then
    echo "Running React client..."
    (cd client && npm run dev) &
fi

# ===== Server =====
if [ "$RUN_SERVER" = true ]; then
    echo "Running Spring Boot server..."
    (cd server && ./mvnw spring-boot:run) &
fi

# Wait for background processes
wait