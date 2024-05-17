const fs = require('fs');
const path = require('path');

const filePath = path.join(__dirname, 'dist/app.js');
const data = fs.readFileSync(filePath, 'utf8');
const result = '#!/usr/bin/env node\n' + data;

fs.writeFileSync(filePath, result, 'utf8');