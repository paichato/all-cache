const http = require("http");

const PORT = 3333;

const server = http.createServer((req, res) => {
  res.statusCode = 200;
  res.setHeader("Content-Type", "text/plain");
  res.end("Hello World");
});

server.listen(PORT, undefined, () => {
  console.log(`Server running at port:${PORT}`);
});
