FROM node:18

WORKDIR /app/service1

COPY package*.json ./

RUN npm install

COPY . .

CMD [ "npm", "start" ]
