/**
 * Decode, parse and return user credentials (username and password)
 * from the Authorization header.
 *
 * @param {http.incomingMessage} request
 * @returns {Array|null} array [username, password] from Authorization header, or null if header is missing
 */
const getCredentials = request => {
  // Own code ->
  if (!request.headers['authorization'] == "" && request.headers.authorization.includes('Basic')) {
    let str = request.headers.authorization;
    str = str.slice(6);
    const buff = Buffer.from(str, 'base64');
    const decoded = buff.toString('utf-8');
    const c = decoded.split(":");
    return c;
  }
  return null;
  // <- Own code
};

/**
 * Does the client accept JSON responses?
 *
 * @param {http.incomingMessage} request
 * @returns {boolean}
 */
const acceptsJson = request => {
  //Check if the client accepts JSON as a response based on "Accept" request header
  // NOTE: "Accept" header format allows several comma separated values simultaneously
  // as in "text/html,application/xhtml+xml,application/json,application/xml;q=0.9,*/*;q=0.8"
  // Do not rely on the header value containing only single content type!
  const acceptHeader = request.headers.accept || '';
  return acceptHeader.includes('application/json') || acceptHeader.includes('*/*');
};

/**
 * Is the client request content type JSON? Return true if it is.
 *
 * @param {http.incomingMessage} request
 * @returns {boolean}
 */
const isJson = request => {
  // Own code ->
  return request.headers['content-type'] == "application/json";
  // <- Own code
};

/**
 * Asynchronously parse request body to JSON
 *
 * Remember that an async function always returns a Promise which
 * needs to be awaited or handled with then() as in:
 *
 *   const json = await parseBodyJson(request);
 *
 *   -- OR --
 *
 *   parseBodyJson(request).then(json => {
 *     // Do something with the json
 *   })
 *
 * @param {http.IncomingMessage} request
 * @returns {Promise<*>} Promise resolves to JSON content of the body
 */
const parseBodyJson = request => {
  return new Promise((resolve, reject) => {
    let body = '';

    request.on('error', err => reject(err));

    request.on('data', chunk => {
      body += chunk.toString();
    });

    request.on('end', () => {
      resolve(JSON.parse(body));
    });
  });
};

module.exports = { acceptsJson, getCredentials, isJson, parseBodyJson };