


// Helper function to hash a password and generate a salt value
const hashPassword = (password) => {
    const crypto = require('crypto');
    const salt = crypto.randomBytes(16).toString('hex');
    const hash = crypto.pbkdf2Sync(password, salt, 10000, 64, 'sha512').toString('hex');
    //return { passwordHash: hash, passwordSalt: salt };
    return hash + ":" + salt;
}

const verifyPassword = (password, storedPassword) => {
    const crypto = require('crypto');
    const [storedHash, storedSalt] = storedPassword.split(':');

    const hash = crypto.pbkdf2Sync(password, storedSalt, 10000, 64, 'sha512').toString('hex');

    return storedHash === hash;
}

const validateEmail = (email) => {
    const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return re.test(String(email).toLowerCase());
}

const removeInputSpace = (email, password) => {
    return { "email": email.replace(/\s/g, ''), "password": password.replace(/\s/g, '') }
}



module.exports = { hashPassword, verifyPassword, validateEmail, removeInputSpace }