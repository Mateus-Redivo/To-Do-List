#!/bin/bash

# Script de inicialização com retry para conectar ao MySQL
set -e

host="$1"
port="$2"
shift 2
cmd="$@"

until nc -z "$host" "$port"; do
  >&2 echo "MySQL não está disponível ainda - aguardando..."
  sleep 2
done

>&2 echo "MySQL está disponível - iniciando aplicação"
exec $cmd