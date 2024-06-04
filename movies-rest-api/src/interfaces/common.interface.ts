export enum Status {
  "Success" = "Success",
  "Error" = "Error",
}

export interface ResponseResult<T> {
  code: number;
  status: keyof typeof Status;
  message?: any | string;
  data?: T;
  accessToken?: string;
}
