const bcrypt = require('bcryptjs');
const mongoose = require('mongoose');
const Schema = mongoose.Schema;

// You can use SALT_ROUNDS when hashing the password with bcrypt.hashSync()
const SALT_ROUNDS = 10;

const SCHEMA_DEFAULTS = {
  name: {
    minLength: 1,
    maxLength: 50
  },
  email: {
    match: /[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?/
  },
  password: {
    minLength: 10
  },
  role: {
    values: ['admin', 'customer'],
    defaultValue: 'customer'
  }
};

// Own code ->
const userSchema = new Schema({
  name: {
    type: String,
    minLength: 1,
    maxLength: 50,
    trim: true,
    required: true
  },
  email: {
    type: String,
    match: SCHEMA_DEFAULTS.email.match,
    trim: true,
    required: true,
    unique: true
  },
  password: {
    type: String,
    required: true,
    minLength: 10
  },
  role: {
    type: String,
    required: true,
    trim: true,
    enum: ['admin', 'customer'],
    default: 'customer',
    lowercase: true
  }
});

userSchema.path('password').set(function(value) {
  if (value == "" || value.length < 10) {
    return value;
  }
  return bcrypt.hashSync(value, 10);
});
// <- Own code

/**
 * Compare supplied password with user's own (hashed) password
 *
 * @param {string} password
 * @returns {Promise<boolean>} promise that resolves to the comparison result
 */
userSchema.methods.checkPassword = async function(password) {
  // Own code ->
  return bcrypt.compare(password, this.password);
  // <- Own code
};

// Omit the version key when serialized to JSON
userSchema.set('toJSON', { virtuals: false, versionKey: false });

const User = new mongoose.model('User', userSchema);
module.exports = User;