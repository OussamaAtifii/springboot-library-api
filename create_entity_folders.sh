#!/bin/bash

ROUTE="$1"

mkdir -p "$ROUTE"/{controller,exception,mapper,model,dto,repository,service}
mkdir -p "$ROUTE"/dto/{request,response}

echo "Entity folders created in: $ROUTE"