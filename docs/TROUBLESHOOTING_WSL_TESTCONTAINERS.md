# Troubleshooting WSL + Testcontainers

## Síntoma típico
- `Could not find a valid Docker environment`
- `BadRequestException` al intentar usar `unix:///var/run/docker.sock`

## Checklist rápido (WSL)
```bash
docker info
ls -la /var/run/docker.sock
curl --unix-socket /var/run/docker.sock http://localhost/_ping
docker context ls
```

## Fix recomendado (socket local)
Crear o ajustar `~/.testcontainers.properties`:

```bash
cat > ~/.testcontainers.properties <<'EOF'
docker.client.strategy=org.testcontainers.dockerclient.UnixSocketClientProviderStrategy
EOF
```

> Nota: Testcontainers puede modificar este archivo automáticamente.

## Alternativa (último recurso): Docker TCP 2375
1) En Docker Desktop: habilitar **Expose daemon on tcp://localhost:2375 without TLS**.
2) En WSL:
```bash
export DOCKER_HOST=tcp://localhost:2375
export TESTCONTAINERS_HOST_OVERRIDE=host.docker.internal
```

Si falla en WSL, valida conectividad:
```bash
curl http://localhost:2375/_ping
```

## Validación final
```bash
cd backend
mvn test
```
