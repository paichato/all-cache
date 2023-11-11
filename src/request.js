const pg = require("pg-promise");
const asyncRedis = require("async-redis");
const redis = asyncRedis.createClient();
const db = pg("postgree string");

const ID = 258;

const QUERY = `SELECT * from clients WHERE id=${ID}`;

const cachableQuery = async () => {
  let cachedClient = redis.get(`${ID}`);

  if (!cachedClient) {
    const client = await db.query(QUERY);
    redis.set(`${client}`, JSON.stringify(client));
    return (cachedClient = client);
  }

  return JSON.parse(cachedClient);
};
