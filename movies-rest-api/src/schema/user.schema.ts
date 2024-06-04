import * as yup from "yup";

// Login
export const loginSchema = yup.object({
  body: yup.object({
    email: yup.string().email().required(),
    password: yup.string().min(3).max(20).required(),
  }),
});

export type LoginBody = yup.InferType<typeof loginSchema>["body"];

// Signup
export const signupSchema = yup.object({
  body: yup.object({
    name: yup.string().required(),
    email: yup.string().email().required(),
    password: yup.string().min(3).max(20).required(),
    birthday: yup.string().required(),
  }),
});

export type SignupBody = yup.InferType<typeof signupSchema>["body"];

// Update Profile
export const updateUserSchema = yup.object({
  body: yup.object({
    name: yup.string(),
    photoURL: yup.string(),
    birthday: yup.string().required(),
  }),
});

export type UpdateUserBody = yup.InferType<typeof updateUserSchema>["body"];
