import express from "express";
import axios from "axios";

const SERVICE1PORT = process.env.SERVICE1PORT;
const SERVICE2PORT = process.env.SERVICE2PORT;
const STORAGEPORT = process.env.STORAGEPORT;

const app = express();

app.get("/status", async (req, res) => {
  console.log("STATUS");
  const response = await axios.get(`http://service2:${SERVICE2PORT}/status`);
});

app.get("/log", async (req, res) => {
  console.log("LOG");
  const response = await axios.get(`http://storage:${STORAGEPORT}/log`);
});

app.listen(SERVICE1PORT, () => {
  console.log(`Server is running on http://localhost:${SERVICE1PORT}`);
});
