import express from "express";
import { appendFileSync, existsSync, mkdirSync, readFileSync } from "fs";
import { join } from "path";

const app = express();
app.use(express.text());
const STORAGEPORT = process.env.STORAGEPORT;

const LOG_DIR = "/status/data";
const LOG_FILE = "log.txt";
const LOG_PATH = join(LOG_DIR, LOG_FILE);

// Luo kansio jos ei ole
if (!existsSync(LOG_DIR)) {
  mkdirSync(LOG_DIR, { recursive: true });
}

app.get("/log", async (req, res) => {
  if (!existsSync(LOG_PATH)) {
    return res.status(404).send("Log file not found");
  }

  try {
    const content = readFileSync(LOG_PATH, "utf-8");
    res.type("text/plain").send(content);
  } catch (err) {
    console.error("Error reading log:", err);
    res.status(500).send("Internal server error");
  }
});

app.post("/log", async (req, res) => {
  const record = req.body;
  if (!record || typeof record !== "string" || record.trim() === "") {
    return res.status(400).send("Empty log record");
  }

  console.log("Received: " + record);

  try {
    appendFileSync(LOG_PATH, record.trim() + "\n");
    res.status(201).send("Log appended");
  } catch (err) {
    console.error("Error writing log:", err);
    res.status(500).send("Internal server error");
  }
});

app.listen(STORAGEPORT, () => {
  console.log(`Server is running on http://localhost:${STORAGEPORT}`);
});
