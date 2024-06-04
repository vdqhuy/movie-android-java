"use strict";
var __createBinding = (this && this.__createBinding) || (Object.create ? (function(o, m, k, k2) {
    if (k2 === undefined) k2 = k;
    var desc = Object.getOwnPropertyDescriptor(m, k);
    if (!desc || ("get" in desc ? !m.__esModule : desc.writable || desc.configurable)) {
      desc = { enumerable: true, get: function() { return m[k]; } };
    }
    Object.defineProperty(o, k2, desc);
}) : (function(o, m, k, k2) {
    if (k2 === undefined) k2 = k;
    o[k2] = m[k];
}));
var __setModuleDefault = (this && this.__setModuleDefault) || (Object.create ? (function(o, v) {
    Object.defineProperty(o, "default", { enumerable: true, value: v });
}) : function(o, v) {
    o["default"] = v;
});
var __importStar = (this && this.__importStar) || function (mod) {
    if (mod && mod.__esModule) return mod;
    var result = {};
    if (mod != null) for (var k in mod) if (k !== "default" && Object.prototype.hasOwnProperty.call(mod, k)) __createBinding(result, mod, k);
    __setModuleDefault(result, mod);
    return result;
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.updateMovieSchema = exports.createMovieSchema = void 0;
const yup = __importStar(require("yup"));
// Create a movie
exports.createMovieSchema = yup.object({
    body: yup.object({
        title: yup.string().required(),
        description: yup.string().required(),
        genre: yup
            .mixed()
            .test((value) => {
            if (Array.isArray(value)) {
                return yup.array().of(yup.string()).isValidSync(value);
            }
            else if (typeof value === "number") {
                return yup.number().isValidSync(value);
            }
            else if (typeof value === "string") {
                return yup.string().isValidSync(value);
            }
            return false;
        })
            .required(),
        director: yup.string().required(),
        releaseYear: yup.string().required("Release year is required"),
        duration: yup.number().integer().required(),
        posterHorizontal: yup.string().required(),
        posterVertical: yup.string().required(),
        country: yup.string().required(),
        actors: yup.string().required(),
        videoURL: yup
            .mixed()
            .test((value) => {
            if (Array.isArray(value)) {
                return yup.array().of(yup.string()).isValidSync(value);
            }
            else if (typeof value === "number") {
                return yup.number().isValidSync(value);
            }
            else if (typeof value === "string") {
                return yup.string().isValidSync(value);
            }
            return false;
        })
            .required(),
        trailerURL: yup
            .string()
            .url("Link trailer không hợp lệ")
            .required("Trailer URL is required"),
    }),
});
// Update a movie
exports.updateMovieSchema = yup.object().shape({
    body: yup.object().shape({
        title: yup.string().notRequired(),
        description: yup.string().notRequired(),
        genre: yup
            .mixed()
            .test("isValidGenre", "Genre is invalid", (value) => {
            if (value !== undefined) {
                if (Array.isArray(value)) {
                    return yup.array().of(yup.string()).isValidSync(value);
                }
                else if (typeof value === "number") {
                    return yup.number().isValidSync(value);
                }
                else if (typeof value === "string") {
                    return yup.string().isValidSync(value);
                }
                return false;
            }
            return true;
        })
            .nullable()
            .transform((value, originalValue) => originalValue === "" ? null : value),
        director: yup.string().notRequired(),
        releaseYear: yup.string().notRequired(),
        duration: yup.number().integer().notRequired(),
        posterHorizontal: yup.string().notRequired(),
        posterVertical: yup.string().notRequired(),
        country: yup.string().notRequired(),
        actors: yup.string().notRequired(),
        videoURL: yup
            .mixed()
            .test("isValidVideoURL", "Video URL is invalid", (value) => {
            if (value !== undefined) {
                if (Array.isArray(value)) {
                    return yup.array().of(yup.string()).isValidSync(value);
                }
                else if (typeof value === "number") {
                    return yup.number().isValidSync(value);
                }
                else if (typeof value === "string") {
                    return yup.string().isValidSync(value);
                }
                return false;
            }
            return true;
        })
            .nullable()
            .transform((value, originalValue) => originalValue === "" ? null : value),
        trailerURL: yup.string().url("Invalid trailer URL").notRequired(),
    }),
});
