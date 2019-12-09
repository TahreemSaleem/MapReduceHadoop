 
From  ubuntu:18.04
  
RUN apt update && apt install -y python3 ant openssh-client git openjdk-8-jdk openjdk-8-jre python3-requests
RUN echo 2 | update-alternatives --config java 
RUN update-alternatives --display java
ENV PATH $PATH:/usr/lib/jvm/java-8-openjdk-amd64/bin
ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64
COPY Source /Source 
RUN cd /Source && ant && ant runAllExamples
