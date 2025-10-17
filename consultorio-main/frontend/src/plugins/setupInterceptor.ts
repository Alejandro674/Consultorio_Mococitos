import axiosInstance from "./api.ts";

import { securityStore } from "@/addons/store/general/security.ts";

const setup = () => {
  const security = securityStore();

  axiosInstance.defaults.headers.common["Authorization"] = security.jwt;
  axiosInstance.interceptors.request.use( ( config ) => {
    if ( config.url !== "/login" && config.url !== "/logout" ) {
      if ( security.jwt ) {
        security.verifyJwt();
        config.headers["Authorization"] = security.jwt;
      }
    }
    return config;
  },
      error => console.log(error) );

  axiosInstance.interceptors.response.use( ( res ) => {
    return res;
  }, error => {
    console.log(error);
    return Promise.reject()
  });
}

const auth = ( header: string ) => {
  axiosInstance.defaults.headers.common['Authorization'] = header;
}

const setHeader = ( valor: string ) => {
  axiosInstance.defaults.headers.common['Usuario'] = valor;
}

export { auth, setup, setHeader }