#!/bin/bash

# Enable X11 for the Swing app
xhost + local: >/dev/null 2>&1 || true

# Check if a container with the same name exists
if [ "$(docker ps -aq -f name=erms-client)" ]; then
    echo "Removing old container..."
    docker rm -f erms-client
fi

# Build the image
echo "Building Swing application..."
docker build -t erms-client .

# Run the container with X11 forwarding and network host mode
echo "Starting Swing application..."
docker run -d \
    --name erms-client \
    -e DISPLAY=$DISPLAY \
    -v /tmp/.X11-unix:/tmp/.X11-unix \
    --network host \
    erms-client

echo "Application started! Showing logs..."
docker logs -f erms-client