# Makefile for managing docker-compose stack (Postgres + Redis)

COMPOSE_FILE = docker-compose.yml
PROJECT_NAME = eventmgmt

# Default target
.PHONY: help
help:
	@echo "Usage:"
	@echo "  make up       - Start Postgres & Redis in background"
	@echo "  make down     - Stop and remove containers"
	@echo "  make restart  - Restart containers"
	@echo "  make logs     - View logs (follow mode)"
	@echo "  make ps       - List running containers"
	@echo "  make clean    - Stop, remove containers, networks, and volumes"

.PHONY: up
up:
	docker compose -p $(PROJECT_NAME) -f $(COMPOSE_FILE) up -d

.PHONY: down
down:
	docker compose -p $(PROJECT_NAME) -f $(COMPOSE_FILE) down -v

.PHONY: restart
restart: down up

.PHONY: logs
logs:
	docker compose -p $(PROJECT_NAME) -f $(COMPOSE_FILE) logs -f

.PHONY: ps
ps:
	docker compose -p $(PROJECT_NAME) -f $(COMPOSE_FILE) ps

.PHONY: clean
clean:
	docker compose -p $(PROJECT_NAME) -f $(COMPOSE_FILE) down -v --remove-orphans
