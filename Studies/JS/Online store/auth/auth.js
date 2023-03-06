/**
 * Get current user based on the request headers
 *
 * @param {http.IncomingMessage} request
 * @returns {Object|null} current authenticated user or null if not yet authenticated
 */
const { getCredentials } = require('../utils/requestUtils');
const User = require('../models/user');

// Own code ->
const getCurrentUser = async request => {
  try {
    const c = getCredentials(request);
    if (c == null) {
      return null;
    }
    else {
      const user = await User.findOne({ email: c[0] }).exec();
      const pw = await user.checkPassword(c[1]);

      if (user && pw) {
        return user;
      }
      else {
        return null;
      }
    }
  }
  catch (error) {
    return null;
  }
};

module.exports = { getCurrentUser };

// <- Own code