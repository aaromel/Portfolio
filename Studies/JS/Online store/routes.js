const responseUtils = require('./utils/responseUtils');
const { acceptsJson, isJson, parseBodyJson } = require('./utils/requestUtils');
const { renderPublic } = require('./utils/render');
const { getCurrentUser } = require('./auth/auth');
const User = require('./models/user');
const { emailInUse, validateUser } = require('./utils/users');

/**
 * Known API routes and their allowed methods
 *
 * Used to check allowed methods and also to send correct header value
 * in response to an OPTIONS request by sendOptions() (Access-Control-Allow-Methods)
 */
const allowedMethods = {
  '/api/register': ['POST'],
  '/api/users': ['GET'],
  '/api/products': ['GET']
};

const data = {
  // make copies of users (prevents changing from outside this module/file)
  products: require('./products.json').map(product => ({...product })),
};

/**
 * Send response to client options request.
 *
 * @param {string} filePath pathname of the request URL
 * @param {http.ServerResponse} response
 */
const sendOptions = (filePath, response) => {
  if (filePath in allowedMethods) {
    response.writeHead(204, {
      'Access-Control-Allow-Methods': allowedMethods[filePath].join(','),
      'Access-Control-Allow-Headers': 'Content-Type,Accept',
      'Access-Control-Max-Age': '86400',
      'Access-Control-Expose-Headers': 'Content-Type,Accept'
    });
    return response.end();
  }

  return responseUtils.notFound(response);
};

/**
 * Does the url have an ID component as its last part? (e.g. /api/users/dsf7844e)
 *
 * @param {string} url filePath
 * @param {string} prefix
 * @returns {boolean}
 */
const matchIdRoute = (url, prefix) => {
  const idPattern = '[0-9a-z]{8,24}';
  const regex = new RegExp(`^(/api)?/${prefix}/${idPattern}$`);
  return regex.test(url);
};

const getAllProducts = () => data.products.map(product => ({...product }));

/**
 * Does the URL match /api/users/{id}
 *
 * @param {string} url filePath
 * @returns {boolean}
 */
const matchUserId = url => {
  return matchIdRoute(url, 'users');
};

const handleRequest = async(request, response) => {
  const { url, method, headers } = request;
  const filePath = new URL(url, `http://${headers.host}`).pathname;

  // serve static files from public/ and return immediately
  if (method.toUpperCase() === 'GET' && !filePath.startsWith('/api')) {
    const fileName = filePath === '/' || filePath === '' ? 'index.html' : filePath;
    return renderPublic(fileName, response);
  }

  // Own code ->
  // View, update and delete a single user by ID (GET, PUT, DELETE)
  if (matchUserId(filePath)) {
    try {
      const uId = filePath.slice(11);
      const usr = await User.findById(uId).exec();
      
      const crnt = await getCurrentUser(request);
      
      if (!usr) {
        return responseUtils.notFound(response);
      }

      else if (crnt.role == "customer") {
        return responseUtils.forbidden(response);
      }
      
      if (method.toUpperCase() === 'GET' && crnt.role == "admin") {
        return responseUtils.sendJson(response, usr);
      }

      if (method.toUpperCase() === 'PUT' && crnt.role == "admin") {
        const psd = await parseBodyJson(request);
        if (psd.role != 'admin' && psd.role != 'customer') {
          throw new Error('Unknown role');
        }
        usr.role = psd.role;
        await usr.save();
        return responseUtils.sendJson(response, usr);
      }

      if (method.toUpperCase() === 'DELETE' && crnt.role == "admin") {
        await User.deleteOne({ _id: usr._id });
        return responseUtils.sendJson(response, usr);
      }

    } catch (error) {
      if (error == 'Error: Unknown role') {
        return responseUtils.badRequest(response, "Role error");
      }
      return responseUtils.basicAuthChallenge(response);
    }
    
    if (method.toUpperCase() === 'OPTIONS') {
      return sendOptions(filePath, response);
    }
  }
  // <- Own code

  // Default to 404 Not Found if unknown url
  if (!(filePath in allowedMethods)) return responseUtils.notFound(response);

  // See: http://restcookbook.com/HTTP%20Methods/options/
  if (method.toUpperCase() === 'OPTIONS') return sendOptions(filePath, response);

  // Check for allowable methods
  if (!allowedMethods[filePath].includes(method.toUpperCase())) {
    return responseUtils.methodNotAllowed(response);
  }

  // Require a correct accept header (require 'application/json' or '*/*')
  if (!acceptsJson(request)) {
    return responseUtils.contentTypeNotAcceptable(response);
  }

  // Own code ->
  
  // Get all products
  if (filePath === '/api/products' && method === 'GET') {
    let logged = await getCurrentUser(request);
    try {
      if (!logged) {
        return responseUtils.basicAuthChallenge(response);
      }
    }
    catch (error) {
      return responseUtils.basicAuthChallenge(response);
    }
    return responseUtils.sendJson(response, getAllProducts());
  }

  // Get all users
  if (filePath === '/api/users' && method.toUpperCase() === 'GET') {
    try {
      const c = await getCurrentUser(request);
      if (c.role == "admin") {
        const all = await User.find({});
        return responseUtils.sendJson(response, all);
      }
      else if (c.role == "customer") {
          return responseUtils.forbidden(response);
      }
    } catch (error) {
      return responseUtils.basicAuthChallenge(response);
    }
  }

  // Register new user
  if (filePath === '/api/register' && method.toUpperCase() === 'POST') {
    if (!isJson(request)) {
      return responseUtils.badRequest(response, 'Invalid Content-Type. Expected application/json');
    }

    const body = await parseBodyJson(request);
    console.log(emailInUse(body.email));
    if (body.role == "admin") {
      body.role = "customer";
    }
    
    const err = validateUser(body);
    if (err[0] == "Missing email") {
      return responseUtils.badRequest(response, 'Email missing');
    }
    
    if (err[0] == "Missing name") {
      return responseUtils.badRequest(response, 'Name missing');
    }

    if (err[0] == "Missing password") {
      return responseUtils.badRequest(response, 'Password missing');
    }

    if (emailInUse(body.email)) {
      return responseUtils.badRequest(response, 'Email in use already');
    }

    const newUser = new User(body);
    await newUser.save();
    return responseUtils.createdResource(response, newUser);
  }

  // <- Own code
};

module.exports = { handleRequest };