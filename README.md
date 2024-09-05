# Learn Spring Boot Project
>  Results from following codes inside the [Learn Microservices with Spring Boot 3](https://link.springer.com/book/10.1007/978-1-4842-6131-6) book

I've tried to implement it in Kotlin + Gradle.kts though, so technically it's not just copying the code inside the book's [repo](https://github.com/Book-Microservices-v3)

---

## How to Run

### via command line
Following the book instructions, one can open terminal on each subfolder (except docker one...) and run corresponding command

```bash
./gradlew bootRun
```
or if its the frontend subfolder, use
```bash
npm run start
```

### via docker
Want to run all of those in one go? Then docker is the way.
But still, one need to build each image that is needed in the docker-compose.yml inside the /docker subfolder...

The book said to use ``./gradlew bootBuildImage`` to generate the spring boot images, 
and for the frontend, there's already a Dockerfile that could be used with ``docker build -t 'imagename:version' .`` syntax

Don't forget to build the consul-importer inside the /docker/consul folder. It creates a centralized config that could be passed on each microservices.

Well, for detailed instructions, just read the chapter 08 of the book. After everything is ready, one can just run
```bash
docker compose up
```
to run the entire stack
