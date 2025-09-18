import express from "express";
import axios from "axios";
import * as disk from "diskusage";
import { appendFileSync, existsSync, mkdirSync } from "fs";
import { join } from "path";

const SERVICE1PORT = process.env.SERVICE1PORT;
const SERVICE2PORT = process.env.SERVICE2PORT;
const STORAGEPORT = process.env.STORAGEPORT;

const app = express();

const saveLog = async () => {
  const dir = "/status/data";
  const file = "log.json";

  if (!existsSync(dir)) {
    mkdirSync(dir, { recursive: true });
  }

  const logPath = join(dir, file);

  const uptimeSeconds = process.uptime();
  const uptimeHours = (uptimeSeconds / 3600).toFixed(2);

  try {
    const { free } = await disk.check("/");
    const freeMB = Math.floor(free / (1024 * 1024));
    const log = `Timestamp1: uptime ${uptimeHours} hours, free disk in root: ${freeMB} MBytes\n`;
    appendFileSync(logPath, log);
    console.log("Log written: ", log);
    return log;
  } catch (err) {
    console.error("Failed to write log: ", err);
  }
};

app.get("/status", async (req, res) => {
  try {
    const log = await saveLog();

    const response = await axios.get(`http://service2:${SERVICE2PORT}/status`);

    const combined = `${log}${response.data}`;

    res.setHeader("Content-Type", "text/plain");
    res.send(combined);
  } catch (error) {
    console.error("Error in /status:", error);
    res.status(500).send("Internal server error");
  }
});

app.get("/log", async (req, res) => {
  console.log("LOG");
  const response = await axios.get(`http://storage:${STORAGEPORT}/log`);
});

app.listen(SERVICE1PORT, () => {
  console.log(`Server is running on http://localhost:${SERVICE1PORT}`);
});
