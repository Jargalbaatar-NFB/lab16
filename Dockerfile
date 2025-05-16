# 1-р үе шат: Build хийх
FROM node:18 AS builder

WORKDIR /app
COPY front-end/ ./front-end/
WORKDIR /app/front-end
RUN npm install && npm run build

# 2-р үе шат: Serve хийх
FROM node:18

WORKDIR /app
COPY --from=builder /app/front-end/build ./build

# static сервер ашиглах
RUN npm install -g serve

EXPOSE 8080
CMD ["serve", "-s", "build", "-l", "8080"]
