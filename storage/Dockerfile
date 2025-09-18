FROM node:18

WORKDIR /app/storage

COPY package*.json ./

RUN npm install

COPY . .

CMD [ "npm", "start" ]
