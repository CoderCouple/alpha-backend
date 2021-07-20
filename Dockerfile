# This file demonstrate how perform a mutistage build
# https://stackoverflow.com/questions/55365504/deploying-war-file-to-jetty-in-docker-error-404-not-found-but-contexts-are-kno

############################# Stage 1 - Build ######################

# fetch basic image
FROM maven:3-jdk-8 as build

#Arguments
ARG build_dir=/app/alpha-backend/

# copy the source code
COPY ./ ${build_dir}

# application placed into app/alpha-backend
WORKDIR ${build_dir}

#compile the source code and install dependencies
RUN mvn clean install

############################# Stage 2 - Deployment ######################

# fetch basic image
FROM jetty:9.3-jre8 as deploy

# open container port
EXPOSE 8080

# copy the ROOT.war file from the previous build stage
COPY --from=build /app/alpha-backend/api/target/ROOT.war /var/lib/jetty/webapps/ROOT.war

# run jetty
ENTRYPOINT ["java"]
CMD ["-jar", "/usr/local/jetty/start.jar", "--create-startd=jmx, stats"]

#Build the image
# docker build -t alpha-backend .

# docker tag alpha-backend:latest 500229388346.dkr.ecr.us-east-1.amazonaws.com/alpha-backend:latest
# docker push 500229388346.dkr.ecr.us-east-1.amazonaws.com/alpha-backend:latest

#Run the image
# docker run -it -p 8080:8080 alpha-backend

#Enter into the container
# docker exec -it 3274b10d06ee /bin/bash

