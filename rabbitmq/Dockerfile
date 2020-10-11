FROM rabbitmq:3-management

#Define environment variables
ENV RABBITMQ_USER android_cl
ENV RABBITMQ_PASSWORD supersecure

ADD init.sh /init.sh
RUN chmod +x /init.sh
EXPOSE 15672
# Define default command
CMD ["/init.sh"]


