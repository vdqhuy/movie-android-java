"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.connection = void 0;
var db_config_1 = require("./db.config");
Object.defineProperty(exports, "connection", { enumerable: true, get: function () { return __importDefault(db_config_1).default; } });
