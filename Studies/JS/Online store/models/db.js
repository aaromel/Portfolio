const mongoose = require('mongoose');
require('dotenv').config({ path: 'c:/Users/aarom/Desktop/wd1/groupwork/.env'});

/**
 * Get database connect URL.
 *
 * Returns the MongoDB connection URL from DBURL environment variable,
 * or if the environment variable is not defined, return the default URL
 * mongodb://localhost:27017/WebShopDb
 *
 * @returns {string} connection URL
 */
const getDbUrl = () => {
  // Own code ->
  console.log(process.env.DBURL);
  if (process.env.DBURL) {
    return process.env.DBURL;
  }
  return "mongodb://localhost:27017/WebShopDb";
  // <- Own code
};

function connectDB() {
  // Do nothing if already connected
  if (!mongoose.connection || mongoose.connection.readyState === 0) {
    mongoose
      .connect(getDbUrl(), {
        useNewUrlParser: true,
        useUnifiedTopology: true,
        useFindAndModify: false,
        useCreateIndex: true,
        autoIndex: true
      })
      .then(() => {
        mongoose.connection.on('error', err => {
          console.error(err);
        });

        mongoose.connection.on('reconnectFailed', handleCriticalError);
      })
      .catch(handleCriticalError);
  }
}

function handleCriticalError(err) {
  console.error(err);
  throw err;
}

function disconnectDB() {
  mongoose.disconnect();
}

module.exports = { connectDB, disconnectDB, getDbUrl };