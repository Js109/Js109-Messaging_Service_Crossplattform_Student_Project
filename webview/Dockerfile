FROM node:12.18.3

WORKDIR /app

ENV PATH /app/node_modules/.bin:$PATH
ARG PUBLIC_BACKEND_URL 

COPY package.json /app/package.json
RUN npm install
RUN npm install -g @angular/cli@7.3.9

COPY . /app

#RUN sed -i "s/backendurl/$(echo $PUBLIC_BACKEND_URL | sed 's/\//\\\//g')/" /app/src/environments/environment.prod.ts
#RUN URL=$(echo $PUBLIC_BACKENDURL | sed 's/\//\\\//g'); sed -i "s/backendurl/$URL/g" /app/src/environments/environment.prod.ts

## working ##
#RUN sed -i "s/backendurl/teststring/" /app/src/environments/environment.prod.ts
RUN sed -i "s|backendurl|${PUBLIC_BACKEND_URL}|g" /app/src/environments/environment.prod.ts

CMD ng serve --prod --host 0.0.0.0
