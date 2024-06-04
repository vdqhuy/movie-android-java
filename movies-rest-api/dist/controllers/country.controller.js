"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const data_1 = require("../data");
const utils_1 = require("../utils");
const getCountries = (req, res) => {
    try {
        return (0, utils_1.sendResponse)(res, {
            code: 200,
            status: "Success",
            message: "",
            data: data_1.countryData,
        });
    }
    catch (error) { }
};
const CountryController = {
    getCountries,
};
exports.default = CountryController;
